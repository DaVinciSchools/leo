import {
  DEFAULT_FORWARD_URL,
  GlobalStateContext,
  LOAD_CREDENTIALS_PARAM,
  RETURN_TO_PARAM,
} from '../../../libs/GlobalStateProvider/GlobalStateProvider';
import {LoginForm} from '../../../libs/LoginForm/LoginForm';
import {useNavigate} from 'react-router';
import {LoadCredentials} from '../../../libs/LoginForm/LoadCredentials';
import {useContext} from 'react';

export function Login() {
  const global = useContext(GlobalStateContext);
  const navigate = useNavigate();
  const queryParameters = new URLSearchParams(window.location.search);
  const forwardUrl =
    queryParameters.get(RETURN_TO_PARAM) ?? DEFAULT_FORWARD_URL;
  const loadCredentials = queryParameters.get(LOAD_CREDENTIALS_PARAM) != null;

  return (
    <>
      <div className="login-login-form">
        {loadCredentials ? (
          <LoadCredentials
            loadCredentials={loadCredentials}
            onSuccess={() => navigate(forwardUrl)}
            onError={global.setError}
          />
        ) : (
          <LoginForm
            onSuccess={() => navigate(forwardUrl)}
            onError={error => {
              if (!(error instanceof CredentialsError)) {
                global.setError(error);
              }
            }}
            onCancel={() => navigate('/')}
          />
        )}
      </div>
    </>
  );
}
