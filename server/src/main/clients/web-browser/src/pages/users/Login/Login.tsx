import './Login.scss';

import {FORWARD_PARAM} from '../../../libs/authentication';
import {LoginForm} from '../../../libs/LoginForm/LoginForm';
import {useNavigate} from 'react-router';

export function Login() {
  const navigate = useNavigate();
  const queryParameters = new URLSearchParams(window.location.search);

  return (
    <>
      <div className="login-login-form">
        <LoginForm
          onLoggedIn={() => {
            navigate(
              queryParameters.get(FORWARD_PARAM) || '/dashboards/redirect.html'
            );
          }}
          onCancel={() => navigate('/')}
        />
      </div>
    </>
  );
}
