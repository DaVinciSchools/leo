import {Button, TextField} from '@mui/material';
import {FormEvent, useContext, useEffect, useState} from 'react';
import {GlobalStateContext} from '../GlobalState';
import {Lock, Person} from '@mui/icons-material';
import {addBaseUrl, usernamePasswordLogin} from '../authentication';
import {useFormFields} from '../form_utils/forms';
import {Link} from 'react-router-dom';
import {styled} from 'styled-components';

const AUTHENTICATION_FAILURE =
  'Invalid username or password. Please try again.';

const StyledForm = styled.form`
  align-items: center;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  gap: ${props => props.theme.spacing(4)};
`;

export function LoginForm(props: {
  onSuccess: () => void;
  onFailure: () => void;
  onError: (error?: unknown) => void;
  onCancel: () => void;
}) {
  const global = useContext(GlobalStateContext);

  const [failure, setFailure] = useState(false);
  const [attempts, setAttempts] = useState(1);

  const formFields = useFormFields();
  const username = formFields.useStringFormField('username', {
    isEmail: true,
    startIcon: <Person />,
    maxLength: 254,
  });
  const password = formFields.useStringFormField('password', {
    isPassword: {
      skipPasswordCheck: true,
    },
    startIcon: <Lock />,
  });

  useEffect(() => {
    if (failure) {
      username.setError(AUTHENTICATION_FAILURE);
      password.setError(AUTHENTICATION_FAILURE);
      setTimeout(() => {
        setFailure(false);
      }, 5000);
    } else {
      if (username.error === AUTHENTICATION_FAILURE) {
        username.setError('');
      }
      if (password.error === AUTHENTICATION_FAILURE) {
        password.setError('');
      }
    }
  }, [failure]);

  async function onFormSubmit(e: FormEvent<HTMLFormElement>) {
    // Prevent the form from reloading the page.
    e.preventDefault();

    if (!formFields.verifyOk(true)) {
      return;
    }

    usernamePasswordLogin(
      global,
      username.getValue()!,
      password.getValue()!,
      props.onSuccess,
      () => {
        if (attempts > 3) {
          props.onFailure();
        }
        setAttempts(attempts + 1);
        setFailure(true);
      },
      props.onError
    );
  }

  useEffect(() => {
    const script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.defer = true;
    document.body.appendChild(script);

    return () => {
      document.body.removeChild(script);
    };
  }, []);

  return (
    <>
      <StyledForm onSubmit={onFormSubmit} className="login-form" noValidate>
        <Link to="/">
          <img src="/images/logo-orange-on-white.svg" />
        </Link>
        <TextField
          required
          autoComplete="email"
          label="Email Address"
          {...username.getTextFieldParams()}
        />
        <TextField
          required
          autoComplete="current-password"
          label="Password"
          {...password.getTextFieldParams()}
        />
        <Button variant="contained" type="submit" fullWidth={true}>
          Log in
        </Button>
        <div>or</div>
        <div style={{height: '45px'}}>
          <div
            id="g_id_onload"
            data-client_id="321696446071-vets88d1bisljtralb1l7en27a3qp9i8.apps.googleusercontent.com"
            data-context="signin"
            data-ux_mode="popup"
            data-login_uri={addBaseUrl('/oauth2/authorization/google')}
            data-cancel_on_tap_outside="true"
            data-itp_support="true"
            data-use_fedcm_for_prompt="true"
          ></div>
          <div
            className="g_id_signin"
            data-type="standard"
            data-shape="rectangular"
            data-theme="filled_blue"
            data-text="signin_with"
            data-size="large"
            data-logo_alignment="left"
          ></div>
        </div>
      </StyledForm>
    </>
  );
}
