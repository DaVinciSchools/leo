import './LoginForm.scss';

import {Button, TextField} from '@mui/material';
import {FormEvent, useContext, useEffect, useRef, useState} from 'react';
import {GlobalStateContext} from '../GlobalState';
import {Link} from 'react-router-dom';
import {Lock, Person} from '@mui/icons-material';
import {addXsrfHeader} from '../authentication';
import {convertFormValuesToURLSearchParams, FormFields} from '../forms';
import {pl_types} from '../protos';

import IUser = pl_types.IUser;

const AUTHENTICATION_FAILURE = 'Authentication failure. Please try again.';

export function LoginForm(props: {
  successAction: () => void;
  failureAction: () => void;
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

    try {
      fetch('/api/login.html', {
        method: 'POST',
        headers: addXsrfHeader({
          'Content-Type': 'application/x-www-form-urlencoded',
        }),
        body: convertFormValuesToURLSearchParams(formRef.current),
        cache: 'no-cache',
        redirect: 'follow',
      })
        .then(response => {
          if (!response.ok) {
            global.setError(response);
          } else if (response.redirected) {
            if (new URL(response.url).searchParams.get('failed') != null) {
              setFailure(AUTHENTICATION_FAILURE);
            } else {
              global.setError(response);
            }
          } else if (response.body != null) {
            response.body
              .getReader()
              .read()
              .then(result => {
                try {
                  const user: IUser = pl_types.User.decode(result.value!);
                  global.setUser(user);
                  props.successAction();
                } catch (e) {
                  global.setError(e);
                }
              })
              .catch(global.setError);
          } else {
            global.setError({
              name: 'Error',
              message: 'Response had no body.',
            });
          }
        })
        .catch(global.setError);
    } catch (e) {
      global.setError(e);
    }
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
          <div
            className="login-form-failure"
            style={{visibility: failure ? 'visible' : 'hidden'}}
          >
            &nbsp;{failure}&nbsp;
          </div>
          <TextField
            required
            autoComplete="email"
            label="Email Address"
            {...formFields.registerProps(
              'username',
              emailAddressRef,
              emailAddressError,
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
              passwordError,
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
            <Button variant="contained" onClick={props.failureAction}>
              Cancel
            </Button>
          </div>
        </div>
      </form>
    </>
  );
}
