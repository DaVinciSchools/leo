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
import {Container} from '@mui/material';
import {styled} from 'styled-components';

const StyledContainer = styled(Container)`
  min-height: 100vh;
  display: flex;
  padding-top: ${props => props.theme.spacing(2)};
  padding-bottom: ${props => props.theme.spacing(2)};
  place-items: center;
`;

export function Login() {
  const global = useContext(GlobalStateContext);
  const navigate = useNavigate();
  const queryParameters = new URLSearchParams(window.location.search);
  const forwardUrl = queryParameters.get(FORWARD_PARAM) ?? DEFAULT_FORWARD_URL;
  const loadCredentials = queryParameters.get(LOAD_CREDENTIALS_PARAM) != null;

  return (
    <StyledContainer maxWidth="xs">
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
    </StyledContainer>
  );
}
