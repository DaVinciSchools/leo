import {
  createContext,
  MutableRefObject,
  PropsWithChildren,
  useRef,
  useState,
} from 'react';
import {createService} from '../protos';
import {pl_types, user_x_management} from 'pl-pb';
import {DeepReadOnly} from '../misc';
import {ModalLoginForm} from '../LoginForm/ModalLoginForm';
import Cookies from 'js-cookie';
import UserXManagementService = user_x_management.UserXManagementService;
import IUserX = pl_types.IUserX;

export const PROMPT_PARAM = 'prompt';
export const RETURN_TO_PARAM = 'returnTo';
export const USERNAME_PARAM = 'username';
export const PASSWORD_PARAM = 'password';
export const LOAD_CREDENTIALS_PARAM = 'loadCredentials';
export const DEFAULT_FORWARD_URL = '/dashboards/redirect.html';

export type IGlobalState = DeepReadOnly<{
  // Gets the latest reported error.
  getError: () => unknown | undefined;
  // Combines and reports the error and its cause to the user. Returns the combined error.
  // This will throw the combined error if the global state is not initialized yet.
  setError: (error?: unknown, cause?: unknown) => unknown;

  // Returns the currently logged-in user, if any, undefined otherwise.
  getUserX: () => DeepReadOnly<IUserX> | undefined;
  // Returns a user meeting the userXReq requirements, possibly after login.
  // Returns undefined if the user is not available (possibly while logging in).
  // If called outside a router component, it will throw. Use getUserX.
  useUserXLogin: (
    loginPrompt?: string,
    userXReq?: (userX: DeepReadOnly<IUserX>) => boolean | null | undefined
  ) => DeepReadOnly<IUserX> | undefined;

  logout: () => Promise<void>;
}>;

export type IInternalGlobalState = IGlobalState &
  DeepReadOnly<{
    setUserX: (
      userX: DeepReadOnly<IUserX> | undefined
    ) => Promise<DeepReadOnly<IUserX> | undefined>;
    // This will throw if not yet initialized.
    getLoginLockRef: () => MutableRefObject<{} | undefined>;
  }>;

export const GlobalStateContext = createContext({
  getError: () => undefined,
  setError: (error, cause) => {
    throw combineErrors(
      'setError called before GlobalStateContext was initialized.',
      combineErrors(error, cause)
    );
  },
  getUserX: () => undefined,
  useUserXLogin: () => undefined,
  logout: () => {
    return Promise.reject(
      'logout called before GlobalStateContext was initialized.'
    );
  },

  setUserX: (userX: DeepReadOnly<IUserX> | undefined) => {
    return Promise.reject(
      'setUserX called before GlobalStateContext was initialized.'
    );
  },
  getLoginLockRef: () => {
    throw new Error(
      'getLoginLockRef called before GlobalStateContext was initialized.'
    );
  },
} as IInternalGlobalState as IGlobalState);

export function GlobalStateProvider(props: PropsWithChildren<{}>) {
  const [error, _setError] = useState<unknown>();
  const [userX, _setUserX] = useState<DeepReadOnly<IUserX> | undefined>();
  const loginLockRef = useRef<{} | undefined>();

  const [showLogInModal, setShowLogInModal] = useState<
    | {
        resolve: (
          value: DeepReadOnly<IUserX> | PromiseLike<DeepReadOnly<IUserX>>
        ) => void;
        reject: (reason?: any) => void;
      }
    | undefined
  >();

  const [showReLogInModal, setShowReLogInModal] = useState<
    | {
        currentUserX: DeepReadOnly<IUserX>;
        resolve: (
          value: DeepReadOnly<IUserX> | PromiseLike<DeepReadOnly<IUserX>>
        ) => void;
        reject: (reason?: any) => void;
      }
    | undefined
  >();

  const globalState = {
    getError: () => error,
    setError,
    getUserX: () => userX,
    useUserXLogin,
    logout,

    setUserX,
    getLoginLockRef: () => loginLockRef,
  } as IInternalGlobalState;

  function setError(error?: unknown, cause?: unknown) {
    setTimeout(() => {
      _setError(combineErrors(error, cause));
    }, 0);
  }

  function setUserX(userX: DeepReadOnly<IUserX> | null | undefined) {
    _setUserX(userX ?? undefined);
    return Promise.resolve(userX ?? undefined);
  }

  function useUserXLogin(
    loginPrompt?: string,
    userXReq?: (userX: IUserX) => boolean | null | undefined
  ): DeepReadOnly<IUserX> | undefined {
    // If the lock is set, then a login request is already being processed.
    if (loginLockRef.current != null) {
      return undefined;
    }

    // If the logged-in user meets the requirements, return it.
    if (userX != null && (!userXReq || userXReq(userX))) {
      return userX;
    }

    // If there is no user, then they are probably returning to the page.
    // We should check the server to see if they are logged in through a cookie.
    if (userX == null) {
      loginLockRef.current = {};
      Promise.resolve()
        .then(() =>
          createService(UserXManagementService, 'UserXManagementService')
        )
        .then(service => service.getUserXs({ofSelf: true}))
        .then(response => response.userXs?.[0]?.userX)
        .then(
          userX =>
            new Promise<DeepReadOnly<IUserX>>((resolve, reject) => {
              if (userX != null && (!userXReq || userXReq(userX))) {
                // If the user returned is valid, then return the user and exit.
                resolve(userX);
              } else if (userX == null) {
                // If no user was returned, then log them in.
                setShowLogInModal({resolve, reject});
              } else {
                // A user was returned, but they are not valid. Offer to let them log in again.
                setShowReLogInModal({currentUserX: userX, resolve, reject});
              }
            })
        )
        .then(setUserX)
        .catch(setError)
        .finally(() => (loginLockRef.current = undefined));
    } else {
      loginLockRef.current = {};
      logout()
        .catch(() => Promise.resolve())
        .then(
          () =>
            new Promise<DeepReadOnly<IUserX>>((resolve, reject) =>
              setShowReLogInModal({currentUserX: userX, resolve, reject})
            )
        )
        .then(setUserX)
        .catch(setError)
        .finally(() => (loginLockRef.current = undefined));
    }

    return undefined;
  }

  function logout() {
    return setUserX(undefined)
      .then(() =>
        fetch('/api/logout.html', {
          method: 'POST',
          headers: addXsrfHeader(),
          cache: 'no-cache',
          redirect: 'follow',
        })
      )
      .catch(error => {
        throw combineErrors('Logout fetch error.', error);
      })
      .then(response => {
        if (!response.ok) {
          throw new Error(
            'Logout response not ok:\n' + JSON.stringify(response, null, 2)
          );
        }
      });
  }

  return (
    <GlobalStateContext.Provider
      value={globalState}
      children={
        <>
          {error && <HandleError />}
          {showLogInModal != null && (
            <ModalLoginForm
              onSuccess={showLogInModal.resolve}
              onSuccess={() => {
                const promise = showLoginPromise;
                if (promise != null) {
                  setShowLoginPromise(undefined);
                  promise.then(() => login);
                }
              }}
            />
          )}
          {showReLogInModal != null && <>TODO</>}
          {props.children}
        </>
      }
    />
  );
}

function combineErrors(error?: unknown, cause?: unknown) {
  if (cause == null) {
    return error;
  }

  if (typeof error != 'object') {
    error = new Error(String(error));
  }
  const newError = error as {stack?: unknown};

  if (typeof cause != 'object') {
    cause = new Error(String(cause));
  }
  const newCause = cause as {stack?: unknown};

  if (newCause.stack != null) {
    newError.stack =
      (newError.stack != null
        ? String(newError.stack) + '\n: Caused by: '
        : '') + String(newCause.stack);
  } else {
    newError.stack =
      (newError.stack != null
        ? String(newError.stack) + '\n: Caused by: '
        : '') + JSON.stringify(newCause, null, 2);
  }

  return newError;
}

export function addXsrfHeader(headers?: HeadersInit) {
  if (Cookies.get('XSRF-TOKEN') != null) {
    return {
      ...(headers != null ? headers : {}),
      ...{'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')!},
    };
  }
  return headers != null ? headers : {};
}

export function addXsrfInputField() {
  if (Cookies.get('XSRF-TOKEN') != null) {
    return (
      <input
        id="_csrf"
        name="_csrf"
        type="hidden"
        value={Cookies.get('XSRF-TOKEN')!}
      />
    );
  }
  return <></>;
}

// export function usernamePasswordLogin(
//   global: IGlobalState,
//   username: string,
//   password: string
// ): Promise<DeepReadOnly<IUserX>> {
//   const loginUrlParams = new URLSearchParams();
//   loginUrlParams.append(USERNAME_PARAM, username);
//   loginUrlParams.append(PASSWORD_PARAM, password);
//
//   const loginStateKey = {};it s
//   return (global as IInternalGlobalState)
//     .logout(loginStateKey)
//     .catch(Promise.resolve)
//     .then(() =>
//       fetch('/api/login.html', {
//         method: 'POST',
//         headers: addXsrfHeader({
//           'Content-Type': 'application/x-www-form-urlencoded',
//         }),
//         body: loginUrlParams,
//         cache: 'no-cache',
//         redirect: 'follow',
//       })
//         .catch(error => {
//           throw combineErrors('Username password login fetch error.', error);
//         })
//         .then(response => {
//           if (!response.ok) {
//             throw new Error(
//               'Login response not ok:\n' + JSON.stringify(response, null, 2)
//             );
//           }
//           if (response.redirected) {
//             const failed = new URL(response.url).searchParams.get('failed');
//             if (failed != null) {
//               throw new CredentialsError('Login failed.', failed);
//             }
//           }
//           if (!response.body) {
//             throw new Error(
//               'Login response body empty:\n' + JSON.stringify(response, null, 2)
//             );
//           }
//           return response.body
//             .getReader()
//             .read()
//             .then(result => {
//               if (loginStateKey
//               const userX = pl_types.UserX.decode(result.value!);
//               (global as IInternalGlobalState).setUserX(userX);
//               return userX;
//             })
//             .catch(error => {
//               throw combineErrors(
//                 'Login response body invalid:\n' +
//                   JSON.stringify(response, null, 2),
//                 error
//               );
//             });
//         })
//     );
// }
//
// export function serverAccountLogin(global: IGlobalState) {
//   return global
//     .logout()
//     .catch(Promise.resolve)
//     .then(() =>
//       createService(UserXManagementService, 'UserXManagementService')
//         .getUserXs({ofSelf: true})
//         .then(response => response.userXs?.[0]?.userX)
//         .then(userX => {
//           if (userX == null) {
//             throw new Error('No user logged in.');
//           }
//           (global as IInternalGlobalState).setUserX(userX);
//           return userX;
//         })
//     );
// }
