import {HandleError, HandleErrorType} from './HandleError/HandleError';
import {createContext, PropsWithChildren, useEffect, useState} from 'react';
import {createService} from './protos';
import {pl_types, user_management} from '../generated/protobuf-js';
import {useNavigate} from 'react-router';

import UserManagementService = user_management.UserManagementService;
import {LOGIN_RETURN_TO_PARAM} from './authentication';

export interface IGlobalState {
  readonly user?: pl_types.IUser;
  readonly error?: HandleErrorType;
  readonly loaded: boolean;
  setUser: (user?: pl_types.IUser | null) => void;
  setError: (error?: HandleErrorType) => void;
  requireUser: (
    userReq: (user: pl_types.IUser) => boolean | null | undefined,
    forwardUrl?: string
  ) => boolean;
}

export const GlobalStateContext = createContext<IGlobalState>({
  loaded: false,
  setUser: throwUnimplementedError,
  setError: throwUnimplementedError,
  requireUser: throwUnimplementedError,
});

export function GlobalState(props: PropsWithChildren<{}>) {
  const [user, setUser] = useState<pl_types.IUser | undefined>();
  const [error, setError] = useState<HandleErrorType>();
  const [loaded, setLoaded] = useState(false);

  const global = {
    user,
    error,
    loaded,
    setUser,
    setError,
    requireUser: (
      userReq: (user: pl_types.IUser) => boolean,
      forwardUrl?: string
    ) => {
      const navigate = useNavigate();
      const [doForwardUrl, setDoForwardUrl] = useState(false);

      useEffect(() => {
        if (doForwardUrl) {
          if (forwardUrl) {
            navigate(forwardUrl);
          } else {
            const url = new URL(window.location.href);
            navigate(
              `/users/login.html?${LOGIN_RETURN_TO_PARAM}=${encodeURIComponent(
                url.pathname + url.search
              )}`
            );
          }
        }
      }, [doForwardUrl]);

      if (doForwardUrl) {
        return false;
      } else if (!loaded) {
        return false;
      } else if (user == null || !userReq(user)) {
        setDoForwardUrl(true);
        return false;
      }

      return true;
    },
  } as IGlobalState;

  useEffect(() => {
    createService(UserManagementService, 'UserManagementService')
      .getUserDetails({})
      .then(response => {
        if (response?.user?.user != null) {
          setUser(response.user.user);
        }
        return response?.user?.user ?? undefined;
      })
      .catch(setError)
      .finally(() => setLoaded(true));
  }, []);

  return (
    <GlobalStateContext.Provider value={global}>
      <HandleError error={error} setError={setError} />
      {props.children}
    </GlobalStateContext.Provider>
  );
}

function throwUnimplementedError<T>(): T {
  throw new Error('Unimplemented');
}
