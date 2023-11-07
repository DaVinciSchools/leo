import Cookies from 'js-cookie';
import {HandleErrorType} from './HandleError/HandleError';
import {IGlobalState, LoadedState} from './GlobalState';
import {pl_types} from 'pl-pb';

export const FORWARD_PARAM = 'returnTo';
export const USERNAME_PARAM = 'username';
export const PASSWORD_PARAM = 'password';

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

export function login(
  global: IGlobalState,
  username: string,
  password: string,
  onSuccess: () => void,
  onFailure: () => void,
  onError: (error?: HandleErrorType) => void
) {
  const loginUrl = new URLSearchParams();
  loginUrl.append(USERNAME_PARAM, username);
  loginUrl.append(PASSWORD_PARAM, password);

  logout(global)
    .catch(reason => {
      console.error('Failed to logout before login.', reason);
    })
    .finally(() => {
      try {
        fetch('/api/login.html', {
          method: 'POST',
          headers: addXsrfHeader({
            'Content-Type': 'application/x-www-form-urlencoded',
          }),
          body: loginUrl,
          cache: 'no-cache',
          redirect: 'follow',
        })
          .then(response => {
            if (!response.ok) {
              console.error('Failed to login.', response);
              onError(response);
            } else if (response.redirected) {
              if (new URL(response.url).searchParams.get('failed') != null) {
                onFailure();
              } else {
                console.error('Failed to login.', response);
                onError(response);
              }
            } else if (response.body == null) {
              console.error('Failed to login.', response);
              onError('Response had no body.');
            } else {
              response.body
                .getReader()
                .read()
                .then(result => {
                  try {
                    global.setUserX(pl_types.UserX.decode(result.value!));
                    onSuccess();
                  } catch (e) {
                    onError(e);
                  }
                })
                .catch(onError);
            }
          })
          .catch(onError);
      } catch (e) {
        onError(e);
      }
    });
}

export function logout(global: IGlobalState) {
  if (
    global.optionalUserX() == null &&
    global.loaded === LoadedState.NOT_LOADED
  ) {
    return Promise.resolve();
  }

  global.setUserX(undefined);
  return fetch('/api/logout.html', {
    method: 'POST',
    headers: addXsrfHeader(),
    cache: 'no-cache',
    redirect: 'follow',
  });
}
