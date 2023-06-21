import './Login.scss';

import {LOGIN_RETURN_TO_PARAM} from '../../../libs/authentication';
import {useNavigate} from 'react-router';
import {LoginForm} from '../../../libs/LoginForm/LoginForm';

export function Login() {
  const navigate = useNavigate();
  const queryParameters = new URLSearchParams(window.location.search);

  return (
    <>
      <LoginForm
        successAction={() => {
          navigate(
            queryParameters.get(LOGIN_RETURN_TO_PARAM) ||
              '/dashboards/redirect.html'
          );
        }}
      />
    </>
  );
}
