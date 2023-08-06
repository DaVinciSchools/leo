import {pl_types} from './protos';
import {HandleError, HandleErrorType} from './HandleError/HandleError';
import {createContext, PropsWithChildren, useState} from 'react';

export interface IGlobalState {
  readonly user?: pl_types.IUser;
  readonly error?: HandleErrorType;
  setUser: (user?: pl_types.IUser | null) => void;
  setError: (error?: HandleErrorType) => void;
}

export const GlobalStateContext = createContext<IGlobalState>({
  setUser: throwUnimplementedError,
  setError: throwUnimplementedError,
});

export function GlobalState(props: PropsWithChildren<{}>) {
  const [user, setUser] = useState<pl_types.IUser | undefined>();
  const [error, setError] = useState<HandleErrorType>();

  const global = {
    user,
    error,
    setUser,
    setError,
  } as IGlobalState;

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
