import {HandleError} from './HandleError/HandleError';
import {
  createContext,
  PropsWithChildren,
  useEffect,
  useRef,
  useState,
} from 'react';
import {createService} from './protos';
import {pl_types, user_x_management} from 'pl-pb';
import {useNavigate} from 'react-router';
import {FORWARD_PARAM, logout} from './authentication';
import {DeepReadOnly} from './misc';
import UserXManagementService = user_x_management.UserXManagementService;

export enum LoadedState {
  NOT_LOADED,
  LOADING,
  LOADED,
}

export interface IGlobalState {
  readonly error?: unknown;
  readonly loaded: LoadedState;
  setError: (error?: unknown) => void;
  optionalUserX: () => DeepReadOnly<pl_types.IUserX> | undefined;
  useUserX: (
    loginPrompt?: string,
    userXReq?: (
      userX: DeepReadOnly<pl_types.IUserX>
    ) => boolean | null | undefined,
    forwardUrl?: string
  ) => DeepReadOnly<pl_types.IUserX> | undefined;
  setUserX: (userX: DeepReadOnly<pl_types.IUserX> | null | undefined) => void;
}

const DEFAULT_GLOBAL_STATE: IGlobalState = {
  loaded: LoadedState.NOT_LOADED,
  setError: throwUnimplementedError,
  optionalUserX: () => undefined,
  useUserX: throwUnimplementedError,
  setUserX: throwUnimplementedError,
};

export const GlobalStateContext = createContext(DEFAULT_GLOBAL_STATE);

export function GlobalStateProvider(props: PropsWithChildren<{}>) {
  const [userX, setUserX] = useState<
    DeepReadOnly<pl_types.IUserX> | undefined
  >();
  const [error, setError] = useState<unknown>();
  const globalStateRef = useRef(DEFAULT_GLOBAL_STATE);
  const loadedRef = useRef(LoadedState.NOT_LOADED);

  useEffect(() => {
    globalStateRef.current = {
      error,
      loaded: loadedRef.current,
      setError: (...args) => setTimeout(setErrorIntercept, 0, ...args),
      useUserX,
      optionalUserX: () => userX,
      setUserX: (...args) => setTimeout(setUserXIntercept, 0, ...args),
    };
  }, [loadedRef.current, error, userX]);

  function setErrorIntercept(error: unknown) {
    setError(error);
  }

  function loadUserX() {
    if (loadedRef.current === LoadedState.NOT_LOADED) {
      loadedRef.current = LoadedState.LOADING;
      createService(UserXManagementService, 'UserXManagementService')
        .getUserXs({ofSelf: true})
        .then(response => {
          if (
            response?.userXs?.length === 1 &&
            response.userXs[0]?.userX != null
          ) {
            setUserXIntercept(response.userXs[0].userX);
          } else if (globalStateRef.current !== DEFAULT_GLOBAL_STATE) {
            logout(globalStateRef.current);
          }
        })
        .catch(error => {
          setError(error);
          if (globalStateRef.current !== DEFAULT_GLOBAL_STATE) {
            logout(globalStateRef.current);
          }
        })
        .finally(() => (loadedRef.current = LoadedState.LOADED));
    }
  }

  // This must only be called from components inside the router.
  function useUserX(
    loginPrompt?: string,
    userXReq?: (userX: pl_types.IUserX) => boolean | null | undefined,
    forwardUrl?: string
  ) {
    const navigate = useNavigate();

    if (loadedRef.current !== LoadedState.LOADED) {
      setTimeout(loadUserX, 0);
      return;
    }

    if (userXReq) {
      if (userX == null || !userXReq(userX)) {
        if (forwardUrl) {
          navigate(forwardUrl ?? '/users/login.html');
        } else {
          navigate(
            `/users/login.html?${FORWARD_PARAM}=${encodeURIComponent(
              window.location.href
            )}`
          );
        }
        return;
      }
    }

    return userX;
  }

  function setUserXIntercept(userX: pl_types.IUserX | null | undefined) {
    if (userX != null) {
      setUserX(userX);
    } else {
      setUserX(undefined);
    }
    loadedRef.current = LoadedState.LOADED;
  }

  return (
    <GlobalStateContext.Provider
      value={{
        error,
        loaded: loadedRef.current,
        setError: (...args) => setTimeout(setErrorIntercept, 0, ...args),
        useUserX,
        optionalUserX: () => userX,
        setUserX: (...args) => setTimeout(setUserXIntercept, 0, ...args),
      }}
      children={
        <>
          <HandleError />
          {props.children}
        </>
      }
    />
  );
}

function throwUnimplementedError<T>(): T {
  throw new Error('Unimplemented');
}
