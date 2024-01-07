import {
  DEFAULT_FORWARD_URL,
  FORWARD_PARAM,
  LOAD_CREDENTIALS_PARAM,
} from '../../../libs/authentication';
import {LoginForm} from '../../../libs/LoginForm/LoginForm';
import {useNavigate} from 'react-router';
import {LoadCredentials} from '../../../libs/LoginForm/LoadCredentials';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function Login() {
  const global = useContext(GlobalStateContext);
  const navigate = useNavigate();
  const queryParameters = new URLSearchParams(window.location.search);
  const forwardUrl = queryParameters.get(FORWARD_PARAM) ?? DEFAULT_FORWARD_URL;
  const loadCredentials = queryParameters.get(LOAD_CREDENTIALS_PARAM) != null;

  return (
    <>
      <div className="login-login-form">
        {loadCredentials ? (
          <LoadCredentials
            loadCredentials={loadCredentials}
            onSuccess={() => navigate(forwardUrl)}
            onFailure={() => navigate('/')}
            onError={global.setError}
          />
        ) : (
          <LoginForm
            onSuccess={() => navigate(forwardUrl)}
            onFailure={() => {
              // TODO: Don't do anything after 3 attempts for now.
            }}
            onError={global.setError}
            onCancel={() => navigate('/')}
          />
        )}
      </div>
    </>
  );
}
