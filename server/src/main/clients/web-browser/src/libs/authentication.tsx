import {pl_types} from '../generated/protobuf-js';
import IUser = pl_types.IUser;
import User = pl_types.User;
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

const SESSION_STORAGE_USER_KEY = 'project-leo-user';

export function login(user: IUser) {
  sessionStorage.setItem(SESSION_STORAGE_USER_KEY, JSON.stringify(user));
}

export function logout() {
  sessionStorage.removeItem(SESSION_STORAGE_USER_KEY);
}

export function getCurrentUser(): IUser | undefined {
  const userJson = sessionStorage.getItem(SESSION_STORAGE_USER_KEY);
  if (userJson != null) {
    return User.fromObject(JSON.parse(userJson));
  }
  return undefined;
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
