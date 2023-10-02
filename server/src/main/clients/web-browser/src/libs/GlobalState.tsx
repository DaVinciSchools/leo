import {HandleError, HandleErrorType} from './HandleError/HandleError';
import {createContext, PropsWithChildren, useEffect, useState} from 'react';
import {createService} from './protos';
import {pl_types, user_x_management} from '../generated/protobuf-js';
import {useNavigate} from 'react-router';

import UserXManagementService = user_x_management.UserXManagementService;
import {FORWARD_PARAM, logout} from './authentication';

export interface IGlobalState {
  readonly userX?: pl_types.IUserX;
  readonly error?: HandleErrorType;
  readonly loaded: boolean;
  setUserX: (userX?: pl_types.IUserX | null) => void;
  setError: (error?: HandleErrorType) => void;
  requireUserX: (
    userXReq: (userX: pl_types.IUserX) => boolean | null | undefined,
    forwardUrl?: string
  ) => boolean;
}

export const GlobalStateContext = createContext<IGlobalState>({
  loaded: false,
  setUserX: throwUnimplementedError,
  setError: throwUnimplementedError,
  requireUserX: throwUnimplementedError,
});

export function GlobalState(props: PropsWithChildren<{}>) {
  const [userX, setUserX] = useState<pl_types.IUserX | undefined>();
  const [error, setError] = useState<HandleErrorType>();
  const [loaded, setLoaded] = useState(false);

  const global = {
    userX,
    error,
    loaded,
    setUserX,
    setError,
    requireUserX: (
      userXReq: (userX: pl_types.IUserX) => boolean,
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
              `/users/login.html?${FORWARD_PARAM}=${encodeURIComponent(
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
      } else if (userX == null || !userXReq(userX)) {
        setDoForwardUrl(true);
        return false;
      }

      return true;
    },
  } as IGlobalState;

  useEffect(() => {
    createService(UserXManagementService, 'UserXManagementService')
      .getUserXs({ofSelf: true})
      .then(response => {
        if (response?.userXs?.length === 1 && response.userXs[0]?.userX) {
          setUserX(response.userXs[0].userX);
          setLoaded(true);
        } else {
          logout(global).finally(() => setLoaded(true));
        }
      })
      .catch(() => logout(global).finally(() => setLoaded(true)));
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
