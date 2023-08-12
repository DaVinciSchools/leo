import './LoginForm.scss';

import {Button, TextField} from '@mui/material';
import {FormEvent, useContext, useEffect, useState} from 'react';
import {GlobalStateContext} from '../GlobalState';
import {Lock, Person} from '@mui/icons-material';
import {login} from '../authentication';
import {useFormFields} from '../forms';

const AUTHENTICATION_FAILURE =
  'Invalid username or password. Please try again.';

export function LoginForm(props: {
  onLoggedIn: () => void;
  onCancel: () => void;
}) {
  const global = useContext(GlobalStateContext);

  const [failure, setFailure] = useState(false);

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

    login(
      global,
      username.getTypedValue() ?? '',
      password.getTypedValue() ?? '',
      props.onLoggedIn,
      () => setFailure(true),
      global.setError
    );
  }

  return (
    <>
      <form onSubmit={onFormSubmit} className="login-form" noValidate>
        <div className="login-form-logo">
          <div />
          <img src="/images/logo-orange-on-white.svg" />
        </div>
        <div className="login-form-fields">
          <TextField
            required
            autoComplete="email"
            label="Email Address"
            {...username.textFieldParams()}
          />
          <TextField
            required
            autoComplete="current-password"
            label="Password"
            {...password.textFieldParams()}
          />
          <div className="login-form-buttons">
            <Button
              variant="contained"
              type="submit"
              disabled={!formFields.isTentativelyOkToSubmit()}
            >
              Log In and Continue
            </Button>
            <Button variant="contained" onClick={props.onCancel}>
              Cancel
            </Button>
          </div>
        </div>
      </form>
    </>
  );
}
