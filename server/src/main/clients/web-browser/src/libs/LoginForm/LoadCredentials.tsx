import './LoginForm.scss';
import {useContext, useEffect} from 'react';
import {
  GlobalStateContext,
  serverAccountLogin,
} from '../GlobalStateProvider/GlobalStateProvider';

export function LoadCredentials(props: {
  loadCredentials: boolean;
  onSuccess: () => void;
  onError: (error?: unknown) => void;
}) {
  const global = useContext(GlobalStateContext);

  useEffect(() => {
    setTimeout(() => {
      if (props.loadCredentials) {
        serverAccountLogin(global).then(props.onSuccess).catch(props.onError);
      } else {
        props.onSuccess();
      }
    }, 0);
  }, []);

  return (
    <>
      <div className="login-form">
        <div className="login-form-logo">
          <div />
          <img src="/images/logo-orange-on-white.svg" />
        </div>
        <div className="space-filler" />
        <div className="logging-in">Logging in...</div>
      </div>
    </>
  );
}
