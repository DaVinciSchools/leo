import './MyAccount.scss';
import {CommonAccountFields} from '../../../libs/CommonAccountFields/CommonAccountFields';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {useContext, useEffect, useState} from 'react';
import {pl_types, user_x_management} from '../../../generated/protobuf-js';
import IUserX = pl_types.IUserX;
import {Button, Form} from 'antd';
import {createService} from '../../../libs/protos';
import UserXManagementService = user_x_management.UserXManagementService;
import IUpsertUserXRequest = user_x_management.IUpsertUserXRequest;
import {GlobalStateContext} from '../../../libs/GlobalState';

export function MyAccount() {
  const global = useContext(GlobalStateContext);
  if (!global.requireUserX(userX => userX?.isAuthenticated)) {
    return <></>;
  }

  const [form] = Form.useForm();
  const [userX, setUserX] = useState<IUserX | undefined>();
  const [errorMessage, setErrorMessage] = useState('');

  const userXService = createService(
    UserXManagementService,
    'UserXManagementService'
  );

  useEffect(() => {
    form.setFieldsValue(userX ?? {});
  }, [userX]);

  useEffect(() => {
    userXService
      .getUserXDetails({userXId: global.userX?.userXId})
      .then(response => setUserX(response.userX?.userX ?? {}))
      .catch(global.setError);
  }, []);

  useEffect(() => {
    if (errorMessage) {
      setTimeout(() => setErrorMessage(''), 5000);
    }
  }, [errorMessage]);

  function finish(values: FormData) {
    const newUserX = Object.assign({}, userX ?? {}, values as IUserX);
    const upsertRequest = {user: newUserX, ...values} as IUpsertUserXRequest;
    userXService
      .upsertUserX(upsertRequest)
      .then(response => {
        if (response.error) {
          setErrorMessage(response.error);
        } else {
          setUserX(response?.userX?.userX ?? {});
          global.setUserX(response?.userX?.userX);
        }
      })
      .catch(reason => global.setError({error: reason, reload: false}));
  }

  return (
    <>
      <DefaultPage title="My Account">
        <div className="space-filler">
          <div
            className="error-notice"
            style={{display: errorMessage !== '' ? undefined : 'none'}}
          >
            {errorMessage}
          </div>
        </div>
        <div className="form-container">
          <Form form={form} onFinish={finish}>
            <CommonAccountFields form={form} />
            <Form.Item>
              <Button type="primary" htmlType="submit">
                Save
              </Button>
            </Form.Item>
          </Form>
        </div>
      </DefaultPage>
    </>
  );
}
