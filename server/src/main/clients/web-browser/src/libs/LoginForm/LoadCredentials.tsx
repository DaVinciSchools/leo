import {useContext, useEffect} from 'react';
import {
  DEFAULT_FORWARD_URL,
  FORWARD_PARAM,
  loadCredentialsLogin,
} from '../authentication';
import {useNavigate} from 'react-router';
import {GlobalStateContext} from '../GlobalState';
import {Link} from 'react-router-dom';

export function LoadCredentials(props: {
  loadCredentials: boolean;
  onSuccess: () => void;
  onFailure: () => void;
  onError: (error?: unknown) => void;
}) {
  const global = useContext(GlobalStateContext);
  const navigate = useNavigate();
  const queryParameters = new URLSearchParams(window.location.search);
  const forwardUrl = queryParameters.get(FORWARD_PARAM) ?? DEFAULT_FORWARD_URL;

  useEffect(() => {
    setTimeout(() => {
      if (props.loadCredentials) {
        loadCredentialsLogin(
          global,
          () => {
            props.onSuccess();
          },
          () => {
            props.onFailure();
          },
          error => {
            props.onError(error);
          }
        );
      } else {
        navigate(forwardUrl);
      }
    }, 0);
  }, []);

  return (
    <>
      <div className="login-form">
        <div className="login-form-logo">
          <div />
          <Link to="/">
            <img src="/images/logo-orange-on-white.svg" />
          </Link>
        </div>
        <div className="space-filler" />
        <div className="logging-in">Logging in...</div>
      </div>
    </>
  );
}
