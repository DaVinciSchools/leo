import './Login.scss';

import {FORWARD_PARAM} from '../../../libs/authentication';
import {useNavigate} from 'react-router';
import {ModalLoginForm} from '../../../libs/LoginForm/ModalLoginForm';

export function Login() {
  const navigate = useNavigate();
  const queryParameters = new URLSearchParams(window.location.search);

  return (
    <>
      <div style={{width: '100%', height: '100%', background: 'white'}}>
        <ModalLoginForm
          open={true}
          successAction={() => {
            navigate(
              queryParameters.get(FORWARD_PARAM) || '/dashboards/redirect.html'
            );
          }}
          failureAction={() => navigate('/')}
        />
      </div>
    </>
  );
}
