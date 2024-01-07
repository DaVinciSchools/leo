import Cookies from 'js-cookie';
import {HandleErrorType} from './HandleError/HandleError';
import {IGlobalState, LoadedState} from './GlobalState';
import {pl_types, user_x_management} from 'pl-pb';
import {createService} from './protos';
import UserXManagementService = user_x_management.UserXManagementService;

export const FORWARD_PARAM = 'returnTo';
export const USERNAME_PARAM = 'username';
export const PASSWORD_PARAM = 'password';
export const LOAD_CREDENTIALS_PARAM = 'loadCredentials';
export const DEFAULT_FORWARD_URL = '/dashboards/redirect.html';

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

export function usernamePasswordLogin(
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
    .catch(error => {
      console.error('Failed to logout before login.', error);
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
              onError(new Error(JSON.stringify(response)));
            } else if (response.redirected) {
              if (new URL(response.url).searchParams.get('failed') != null) {
                onFailure();
              } else {
                onError(new Error(JSON.stringify(response)));
              }
            } else if (response.body == null) {
              onError(new Error('Response had no body.'));
            } else {
              response.body
                .getReader()
                .read()
                .then(result => {
                  try {
                    global.setUserX(pl_types.UserX.decode(result.value!));
                    onSuccess();
                  } catch (error) {
                    onError(error);
                  }
                })
                .catch(error => {
                  onError(error);
                });
            }
          })
          .catch(error => {
            onError(error);
          });
      } catch (error) {
        onError(error);
      }
    });
}

export function loadCredentialsLogin(
  global: IGlobalState,
  onSuccess: () => void,
  onFailure: () => void,
  onError: (error?: HandleErrorType) => void
) {
  logout(global)
    .catch(error => {
      console.error('Failed to logout before login.', error);
    })
    .finally(() => {
      createService(UserXManagementService, 'UserXManagementService')
        .getUserXs({ofSelf: true})
        .then(response => {
          if (response.userXs.length === 1) {
            global.setUserX(response.userXs?.[0]?.userX);
            onSuccess();
          } else {
            onFailure();
          }
        })
        .catch(error => {
          onError(error);
        });
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

export function addBaseUrl(url: string) {
  return `${window.location.protocol}//${window.location.host}${url}`;
}
