import Cookies from 'js-cookie';
import {IGlobalState} from './GlobalState';
import {pl_types} from '../generated/protobuf-js';

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
  onFailure: () => void
) {
  const params = new URLSearchParams();
  params.append(USERNAME_PARAM, username);
  params.append(PASSWORD_PARAM, password);

  logout(global, () => {
    try {
      fetch('/api/login.html', {
        method: 'POST',
        headers: addXsrfHeader({
          'Content-Type': 'application/x-www-form-urlencoded',
        }),
        body: params,
        cache: 'no-cache',
        redirect: 'follow',
      })
        .then(response => {
          if (!response.ok) {
            global.setError(response);
          } else if (response.redirected) {
            if (new URL(response.url).searchParams.get('failed') != null) {
              onFailure();
            } else {
              global.setError(response);
            }
          } else if (response.body != null) {
            response.body
              .getReader()
              .read()
              .then(result => {
                try {
                  global.setUser(pl_types.User.decode(result.value!));
                  onSuccess();
                } catch (e) {
                  global.setError(e);
                }
              })
              .catch(global.setError);
          } else {
            global.setError({
              name: 'Error',
              message: 'Response had no body.',
            });
          }
        })
        .catch(global.setError);
    } catch (e) {
      global.setError(e);
    }
  });
}

export function logout(global: IGlobalState, onSuccess: () => void) {
  fetch('/api/logout.html', {
    method: 'POST',
    headers: addXsrfHeader(),
    cache: 'no-cache',
    redirect: 'follow',
  }).finally(() => {
    global.setUser(undefined);
    onSuccess();
  });
}
