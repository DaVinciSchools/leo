import './Login.scss';

import {LOGIN_RETURN_TO_PARAM} from '../../../libs/authentication';
import {useNavigate} from 'react-router';
import {ModalLoginForm} from '../../../libs/LoginForm/ModalLoginForm';

export function Login() {
  const navigate = useNavigate();
  const queryParameters = new URLSearchParams(window.location.search);

  return (
    <>
      <div style={{width: '100%', height: '100%', background: 'white'}}>
        <ModalLoginForm
          visible={true}
          successAction={() => {
            navigate(
              queryParameters.get(LOGIN_RETURN_TO_PARAM) ||
                '/dashboards/redirect.html'
            );
          }}
          failureAction={() => navigate('/')}
        />
      </div>
    </>
  );
}
