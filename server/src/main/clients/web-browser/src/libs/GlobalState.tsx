import {HandleError, HandleErrorType} from './HandleError/HandleError';
import {createContext, PropsWithChildren, useState} from 'react';
import {createService} from './protos';
import {pl_types, user_x_management} from 'pl-pb';
import {useNavigate} from 'react-router';

import UserXManagementService = user_x_management.UserXManagementService;
import {FORWARD_PARAM, logout} from './authentication';

export enum LoadedState {
  NOT_LOADED,
  LOADING,
  LOADED,
}

export interface IGlobalState {
  readonly error?: HandleErrorType;
  readonly loaded: LoadedState;
  setError: (error?: HandleErrorType) => void;
  optionalUserX: () => pl_types.IUserX | undefined;
  requireUserX: (
    loginPrompt?: string,
    userXReq?: (userX: pl_types.IUserX) => boolean | null | undefined,
    forwardUrl?: string
  ) => pl_types.IUserX | undefined;
  setUserX: (userX: pl_types.IUserX | null | undefined) => void;
}

export const GlobalStateContext = createContext<IGlobalState>({
  loaded: LoadedState.NOT_LOADED,
  setError: throwUnimplementedError,
  optionalUserX: throwUnimplementedError,
  requireUserX: throwUnimplementedError,
  setUserX: throwUnimplementedError,
});

export function GlobalState(props: PropsWithChildren<{}>) {
  const [userX, setUserX] = useState<pl_types.IUserX | undefined>();
  const [error, setError] = useState<HandleErrorType>();
  const [loaded, setLoaded] = useState(LoadedState.NOT_LOADED);

  function setErrorIntercept(error: HandleErrorType) {
    console.error('Error occurred. ', error);
    setError(error);
  }

  function loadUserX() {
    if (loaded === LoadedState.NOT_LOADED) {
      setLoaded(LoadedState.LOADING);
      createService(UserXManagementService, 'UserXManagementService')
        .getUserXs({ofSelf: true})
        .then(response => {
          if (response?.userXs?.length === 1 && response.userXs[0]?.userX) {
            setUserX(response.userXs[0].userX);
          } else {
            logout(global);
          }
        })
        .catch(error => {
          setError(error);
          logout(global);
        })
        .finally(() => setLoaded(LoadedState.LOADED));
    }
  }

  function requireUserX(
    loginPrompt?: string,
    userXReq?: (userX: pl_types.IUserX) => boolean | null | undefined,
    forwardUrl?: string
  ) {
    if (loaded !== LoadedState.LOADED) {
      loadUserX();
      return undefined;
    }

    if (userXReq) {
      if (userX == null || !userXReq(userX)) {
        const navigate = useNavigate();
        if (forwardUrl) {
          navigate(forwardUrl);
        } else {
          navigate(
            `/users/login.html?${FORWARD_PARAM}=${encodeURIComponent(
              window.location.href
            )}`
          );
        }
        return undefined;
      }
    }

    return userX;
  }

  function setSetUserXIntercept(userX: pl_types.IUserX | null | undefined) {
    if (userX) {
      setUserX(userX);
    } else {
      setUserX(undefined);
    }
    setLoaded(LoadedState.LOADED);
  }

  const global = {
    error,
    loaded,
    setError: setErrorIntercept,
    requireUserX,
    optionalUserX: () => userX,
    setUserX: setSetUserXIntercept,
  } as IGlobalState;

  return (
    <GlobalStateContext.Provider value={global}>
      <HandleError />
      {props.children}
    </GlobalStateContext.Provider>
  );
}

function throwUnimplementedError<T>(): T {
  throw new Error('Unimplemented');
}
