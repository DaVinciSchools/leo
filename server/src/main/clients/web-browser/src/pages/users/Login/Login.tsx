import './Login.scss';

import {Button, Checkbox, Form, Input} from 'antd';
import {LockOutlined, UserOutlined} from '@ant-design/icons';
import {useEffect, useRef, useState} from 'react';
import {Link} from 'react-router-dom';
import {addXsrfInputField} from '../../../libs/authentication';

export function Login() {
  const [loginForm] = Form.useForm();
  const [disabled, setDisabled] = useState(false);
  const [formValues, setFormValues] = useState({});

  const formRef = useRef<HTMLFormElement>(null);

  const queryParameters = new URLSearchParams(window.location.search);
  const failed = queryParameters.get('failed');
  const logout = queryParameters.get('logout');

  function onFinish(formValues: {}) {
    setDisabled(true);
    setFormValues(formValues);
  }

  useEffect(() => {
    if (Object.entries(formValues).length > 0) {
      formRef.current!.requestSubmit();
    }
  }, [formValues]);

  return (
    <>
      <div className="space-filler" />
      <div className="logo">
        <Link to="/">
          <img src="/images/logo-orange-on-white.svg" />
        </Link>
      </div>
      <div className="space-filler" />
      <div className="form-container">
        <Form
          form={loginForm}
          onFinish={onFinish}
          disabled={disabled}
          className="form-elements"
        >
          <div
            className="form-padding"
            style={{display: failed != null ? undefined : 'none'}}
          >
            <div className="error">
              Authentication failure. Please try again.
            </div>
          </div>
          <div
            className="form-padding"
            style={{display: logout != null ? undefined : 'none'}}
          >
            <div className="success">
              You have been successfully logged out. Go&nbsp;
              <Link to="/">Home</Link>.
            </div>
          </div>
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
              type="password"
              autoComplete="current-password"
              prefix={<LockOutlined />}
            />
          </Form.Item>
          <div className="footer">
            <Form.Item name="rememberMe" valuePropName="checked">
              <Checkbox disabled={true}>Remember me</Checkbox>
            </Form.Item>
            <Link to="/">TODO: Forgot your password?</Link>
          </div>
          <div className="buttons">
            <Button type="primary" htmlType="submit">
              Submit
            </Button>
          </div>
        </Form>
      </div>
      {/* The SecurityFilterChain must be called from a <form action="...">. */}
      {/* However, React doesn't support actions. So, we dynamically create  */}
      {/* a form with the values and submit it manually.                     */}
      {/* TODO: Explore FormProps, which has an action?                      */}
      <form
        ref={formRef}
        method="post"
        action="/api/login"
        style={{display: 'none'}}
      >
        {/* This won't be detected if it is in the <Form/> component above. */}
        {addXsrfInputField()}
        {Object.entries(formValues).map(([key, value]) => (
          <Input
            key={key}
            type="hidden"
            id={key}
            name={key}
            value={value as string}
          />
        ))}
      </form>
    </>
  );
}
