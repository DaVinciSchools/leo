import './CommonAccountFields.scss';

import {Form, FormInstance, Input, FormRule} from 'antd';
import {
  EyeInvisibleOutlined,
  EyeOutlined,
  LockOutlined,
  MailOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {useEffect, useState} from 'react';
import {getCurrentUser} from '../authentication';

export function CommonAccountFields(props: {
  form: FormInstance;
  fieldsLoaded: boolean;
}) {
  const user = getCurrentUser() ?? {};
  const [showPassword, setShowPassword] = useState(false);
  const passwordReqs = RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,50}$');
  const passwordRules: FormRule[] = [
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
        'Requires: 8+ characters, a number, a lower case letter, and an upper case letter.',
      validator: (rule: FormRule, value: string) => {
        if (value != null && value.length > 0 && !passwordReqs.test(value)) {
          return Promise.reject();
        }
        return Promise.resolve();
      },
    },
  ];

  useEffect(() => {
    if (props.fieldsLoaded) {
      props.form.validateFields();
    }
  });

  return (
    <>
      <div className="form-fields-single-line">
        <Form.Item
          rules={[
            {
              required: true,
              whitespace: true,
              message: 'Please enter your first name.',
            },
          ]}
          name="firstName"
        >
          <Input
            placeholder="First Name"
            maxLength={255}
            autoComplete="given-name"
            prefix={<UserOutlined />}
          />
        </Form.Item>
        <Form.Item
          rules={[
            {
              required: true,
              whitespace: true,
              message: 'Please enter your last name.',
            },
          ]}
          name="lastName"
        >
          <Input
            placeholder="Last Name"
            maxLength={255}
            autoComplete="family-name"
            prefix={<UserOutlined />}
          />
        </Form.Item>
      </div>
      <Form.Item
        rules={[
          {
            required: true,
            whitespace: true,
            message: 'Please enter your email address.',
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
          disabled={!user.isAdmin}
        />
      </Form.Item>
      <Form.Item
        name="currentPassword"
        style={{display: user.isAdmin ? 'none' : undefined}}
        rules={[
          {
            message: 'The current password is required to update the password.',
            validator: () => {
              if (
                !user.isAdmin &&
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
