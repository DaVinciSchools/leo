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
  requireUserX: (
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
  requireUserX: () => undefined,
  setUserX: throwUnimplementedError,
};

export const GlobalStateContext = createContext(DEFAULT_GLOBAL_STATE);

export function GlobalStateProvider(props: PropsWithChildren<{}>) {
  const [userX, setUserX] = useState<
    DeepReadOnly<pl_types.IUserX> | undefined
  >();
  const [error, setError] = useState<unknown>();
  const [globalState, setGlobalState] =
    useState<IGlobalState>(DEFAULT_GLOBAL_STATE);
  const loadedRef = useRef(LoadedState.NOT_LOADED);

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
          } else if (globalState !== DEFAULT_GLOBAL_STATE) {
            logout(globalState);
          }
        })
        .catch(error => {
          setError(error);
          if (globalState !== DEFAULT_GLOBAL_STATE) {
            logout(globalState);
          }
        })
        .finally(() => (loadedRef.current = LoadedState.LOADED));
    }
  }

  function requireUserX(
    loginPrompt?: string,
    userXReq?: (userX: pl_types.IUserX) => boolean | null | undefined,
    forwardUrl?: string
  ) {
    if (loadedRef.current !== LoadedState.LOADED) {
      setTimeout(loadUserX, 0);
      return;
    }

    if (userXReq) {
      if (userX == null || !userXReq(userX)) {
        const navigate = useNavigate();
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

  useEffect(
    () =>
      setGlobalState({
        error,
        loaded: loadedRef.current,
        setError: (...args) => setTimeout(setErrorIntercept, 0, ...args),
        requireUserX,
        optionalUserX: () => userX,
        setUserX: (...args) => setTimeout(setUserXIntercept, 0, ...args),
      }),
    [loadedRef.current, error, userX]
  );

  return (
    <GlobalStateContext.Provider
      value={globalState}
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
