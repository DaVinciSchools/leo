import Cookies from 'js-cookie';
import {useEffect} from 'react';
import {redirect} from 'react-router-dom';

export const LOGIN_RETURN_TO_PARAM = 'returnTo';
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

export function sendToLogin() {
  useEffect(() => {
    redirect(
      `/users/login?${LOGIN_RETURN_TO_PARAM}=${encodeURIComponent(
        window.location.href
      )}`
    );
  }, []);
  return <></>;
}
