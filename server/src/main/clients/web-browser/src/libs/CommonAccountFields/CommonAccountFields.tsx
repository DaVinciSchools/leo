import './CommonAccountFields.scss';

import {Form, FormInstance, Input, FormRule} from 'antd';
import {
  EyeInvisibleOutlined,
  EyeOutlined,
  LockOutlined,
  MailOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {useContext, useState} from 'react';
import {GlobalStateContext} from '../GlobalState';

export function CommonAccountFields(props: {
  form: FormInstance;
  disabled?: boolean;
}) {
  const global = useContext(GlobalStateContext);

  const [showPassword, setShowPassword] = useState(false);
  const passwordReqs = RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$');
  const passwordRules: FormRule[] = [
    {
      message: 'A password is required for new accounts.',
      validator: (rule: FormRule, value: string) => {
        if (
          props.form.getFieldValue('userXId') == null &&
          (value == null || value === '')
        ) {
          return Promise.reject();
        }
        return Promise.resolve();
      },
    },
    {
      message: 'The passwords do not match.',
      validator: () => {
        if (
          (props.form.getFieldValue('newPassword') ?? '') !==
          (props.form.getFieldValue('verifyPassword') ?? '')
        ) {
          return Promise.reject();
        }
        return Promise.resolve();
      },
    },
    {
      message:
        'Must have 8+ characters, a number, and a lower and upper case letter.',
      validator: (rule: FormRule, value: string) => {
        if (value != null && value.length > 0 && !passwordReqs.test(value)) {
          return Promise.reject();
        }
        return Promise.resolve();
      },
    },
  ];

  return (
    <>
      <div className="form-fields-single-line">
        <Form.Item
          rules={[
            {
              required: true,
              whitespace: true,
              message: 'Please enter a first name.',
            },
          ]}
          name="firstName"
        >
          <Input
            placeholder="First Name"
            maxLength={255}
            autoComplete="given-name"
            prefix={<UserOutlined />}
            disabled={props.disabled}
          />
        </Form.Item>
        <Form.Item
          rules={[
            {
              required: true,
              whitespace: true,
              message: 'Please enter a last name.',
            },
          ]}
          name="lastName"
        >
          <Input
            placeholder="Last Name"
            maxLength={255}
            autoComplete="family-name"
            prefix={<UserOutlined />}
            disabled={props.disabled}
          />
        </Form.Item>
      </div>
      <Form.Item
        rules={[
          {
            required: true,
            whitespace: true,
            message: 'Please enter an email address.',
          },
          {
            type: 'email',
            message: 'This e-mail address is not valid',
          },
        ]}
        name="emailAddress"
      >
        <Input
          placeholder="Email Address"
          maxLength={255}
          autoComplete="email"
          prefix={<MailOutlined />}
          disabled={!global.userX?.isAdminX || props.disabled}
        />
      </Form.Item>
      <Form.Item
        name="currentPassword"
        style={{display: global.userX?.isAdminX ? 'none' : undefined}}
        rules={[
          {
            message: 'The current password is required for password updates.',
            validator: () => {
              if (
                !global.userX?.isAdminX &&
                ((props.form.getFieldValue('password') ?? '') !== '' ||
                  (props.form.getFieldValue('verifyPassword') ?? '') !== '') &&
                (props.form.getFieldValue('currentPassword') ?? '') === ''
              ) {
                return Promise.reject();
              }
              return Promise.resolve();
            },
          },
        ]}
        dependencies={[['newPassword'], ['verifyPassword']]}
      >
        <Input
          placeholder="Current Password"
          maxLength={255}
          type={showPassword ? 'text' : 'password'}
          autoComplete="current-password"
          disabled={props.disabled}
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
      <Form.Item
        name="newPassword"
        rules={passwordRules}
        dependencies={[['verifyPassword']]}
      >
        <Input
          placeholder="New Password"
          maxLength={255}
          type={showPassword ? 'text' : 'password'}
          autoComplete="new-password"
          disabled={props.disabled}
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
      <Form.Item
        name="verifyPassword"
        rules={passwordRules}
        dependencies={[['newPassword']]}
      >
        <Input
          placeholder="Re-enter New Password"
          maxLength={255}
          type={showPassword ? 'text' : 'password'}
          autoComplete="new-password"
          disabled={props.disabled}
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
    </>
  );
}
