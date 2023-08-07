import './LoginForm.scss';

import {Button, TextField} from '@mui/material';
import {FormEvent, useContext, useEffect, useRef, useState} from 'react';
import {FormFields} from '../forms';
import {GlobalStateContext} from '../GlobalState';
import {Link} from 'react-router-dom';
import {Lock, Person} from '@mui/icons-material';
import {login} from '../authentication';

const AUTHENTICATION_FAILURE = 'Authentication failure. Please try again.';

export function LoginForm(props: {
  successAction: () => void;
  cancelAction: () => void;
}) {
  const global = useContext(GlobalStateContext);

  const queryParameters = new URLSearchParams(window.location.search);
  const [failure, setFailure] = useState(
    queryParameters.get('failed') != null ? AUTHENTICATION_FAILURE : ''
  );

  const [showPasswords, setShowPasswords] = useState(false);

  const formRef = useRef<HTMLFormElement>(null);
  const formFields = useState(new FormFields(formRef))[0];

  const emailAddressRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLDivElement>(null);

  const [emailAddressError, setEmailAddressError] = useState('');
  const [passwordError, setPasswordError] = useState('');

  useEffect(() => {
    if (failure) {
      setTimeout(() => {
        setFailure('');
      }, 5000);
    }
  }, [failure]);

  async function onFormSubmit(e: FormEvent<HTMLFormElement>) {
    // Prevent the form from reloading the page.
    e.preventDefault();

    formFields.resetErrors();
    if (formFields.checkAndSet(true)) {
      return;
    }

    login(
      global,
      String(formFields.get('username') ?? ''),
      String(formFields.get('password') ?? ''),
      props.successAction,
      () => {
        setFailure(AUTHENTICATION_FAILURE);
      }
    );
  }

  return (
    <>
      <form
        ref={formRef}
        onSubmit={onFormSubmit}
        className="login-form"
        noValidate
      >
        <div className="login-form-logo">
          <div />
          <Link to="/">
            <img src="/images/logo-orange-on-white.svg" />
          </Link>
        </div>
        <div className="login-form-fields">
          <TextField
            required
            autoComplete="email"
            label="Email Address"
            {...formFields.registerProps(
              'username',
              emailAddressRef,
              emailAddressError || failure,
              setEmailAddressError,
              {
                isEmail: true,
                startIcon: <Person />,
                maxLength: 254,
              }
            )}
          />
          <TextField
            required
            autoComplete="current-password"
            label="Password"
            {...formFields.registerProps(
              'password',
              passwordRef,
              passwordError || failure,
              setPasswordError,
              {
                isPassword: {
                  showPasswords,
                  setShowPasswords,
                  skipPasswordCheck: true,
                },
                startIcon: <Lock />,
              }
            )}
          />
          <div className="login-form-buttons">
            <Button variant="contained" type="submit">
              Login and Continue
            </Button>
            <Button variant="contained" onClick={props.cancelAction}>
              Cancel
            </Button>
          </div>
        </div>
      </form>
    </>
  );
}
