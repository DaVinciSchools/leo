import './LoginForm.scss';

import {Button, Checkbox, Form, Input} from 'antd';
import {
  EyeInvisibleOutlined,
  EyeOutlined,
  LockOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {Link} from 'react-router-dom';
import {addXsrfHeader, login} from '../authentication';
import {pl_types} from '../protos';
import {useEffect, useState} from 'react';
import IUser = pl_types.IUser;
import {HandleError, HandleErrorType} from '../HandleError/HandleError';

const AUTHORIZATION_FAILURE = 'Authentication failure. Please try again.';

export function LoginForm(props: {successAction: (user: IUser) => void}) {
  const [handleError, setHandleError] = useState<HandleErrorType>();

  const [loginForm] = Form.useForm();
  const [disabled, setDisabled] = useState(false);
  const queryParameters = new URLSearchParams(window.location.search);
  const [failure, setFailure] = useState(
    queryParameters.get('failed') != null ? AUTHORIZATION_FAILURE : ''
  );
  const [showPassword, setShowPassword] = useState(false);

  useEffect(() => {
    if (queryParameters.get('username') && queryParameters.get('password')) {
      doSubmit({
        username: queryParameters.get('username'),
        password: queryParameters.get('password'),
      });
    }
  }, []);

  useEffect(() => {
    if (failure) {
      setTimeout(() => setFailure(''), 5000);
    }
  }, [failure]);

  async function doSubmit(formValues: {}) {
    try {
      fetch('/api/login.html', {
        method: 'POST',
        headers: addXsrfHeader({
          'Content-Type': 'application/x-www-form-urlencoded',
        }),
        body: new URLSearchParams(formValues),
        cache: 'no-cache',
        redirect: 'follow',
      })
        .then(response => {
          if (!response.ok) {
            setHandleError(response);
          } else if (response.redirected) {
            if (new URL(response.url).searchParams.get('failed') != null) {
              setFailure(AUTHORIZATION_FAILURE);
            } else {
              setHandleError(response);
            }
          } else if (response.body != null) {
            response.body
              .getReader()
              .read()
              .then(result => {
                try {
                  const user: IUser = pl_types.User.decode(result.value!);
                  login(user);
                  props.successAction(user);
                } catch (e) {
                  setHandleError(e);
                }
              })
              .catch(setHandleError);
          } else {
            setHandleError({name: 'Error', message: 'Response had no body.'});
          }
        })
        .catch(setHandleError);
    } catch (e) {
      setHandleError(e);
    }
  }

  function onFinish(formValues: {}) {
    setDisabled(true);
    setFailure('');
    doSubmit(formValues)
      .catch(setHandleError)
      .finally(() => setDisabled(false));
  }

  return (
    <>
      <HandleError error={handleError} setError={setHandleError} />
      <div className="space-filler" />
      <div className="logo">
        <Link to="/">
          <img src="/images/logo-orange-on-white.svg" />
        </Link>
      </div>
      <div className="space-filler">
        <div
          className="error-notice"
          style={{display: failure ? undefined : 'none'}}
        >
          {failure}
        </div>
      </div>
      <div className="form-container">
        <Form
          form={loginForm}
          onFinish={onFinish}
          disabled={disabled}
          className="form-elements"
        >
          <Form.Item
            name="username"
            rules={[
              {
                required: true,
                whitespace: true,
                message: 'Please enter your email address.',
              },
              {
                type: 'email',
                message: 'This e-mail address is not valid.',
              },
            ]}
          >
            <Input
              placeholder="Email Address"
              maxLength={255}
              autoComplete="username"
              autoFocus={true}
              prefix={<UserOutlined />}
            />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[
              {
                required: true,
                message: 'Please enter your password.',
              },
            ]}
          >
            <Input
              placeholder="Password"
              maxLength={255}
              type={showPassword ? 'text' : 'password'}
              autoComplete="current-password"
              prefix={<LockOutlined />}
              suffix={
                showPassword ? (
                  <EyeOutlined onClick={() => setShowPassword(false)} />
                ) : (
                  <EyeInvisibleOutlined onClick={() => setShowPassword(true)} />
                )
              }
            />
          </Form.Item>
          <div className="footer">
            <Form.Item name="rememberMe" valuePropName="checked">
              <Checkbox>Remember me (TODO)</Checkbox>
            </Form.Item>
            <Link to="/">Forgot your password? (TODO)</Link>
          </div>
          <div className="buttons">
            <Button type="primary" htmlType="submit">
              Submit
            </Button>
          </div>
        </Form>
      </div>
    </>
  );
}
