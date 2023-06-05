import './Accounts.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {useEffect, useRef, useState} from 'react';
import {pl_types, user_management} from '../../../generated/protobuf-js';
import {Button, Form, Input, InputRef, Modal, Pagination, Table} from 'antd';
import {createService} from '../../../libs/protos';
import {CommonAccountFields} from '../../../libs/CommonAccountFields/CommonAccountFields';
import {EditOutlined} from '@ant-design/icons';
import UserManagementService = user_management.UserManagementService;
import IFullUserDetails = user_management.IFullUserDetails;
import IUpsertUserRequest = user_management.IUpsertUserRequest;
import IUser = pl_types.IUser;

export function Accounts() {
  const user = getCurrentUser();
  if (user == null || !user.isAdmin) {
    return sendToLogin();
  }

  const [showSearchForAccount, setShowSearchForAccount] = useState(false);
  const [searchForm] = Form.useForm();
  const [searchText, setSearchText] = useState('');
  const searchTextRef = useRef<InputRef>(null);
  const [users, setUsers] = useState<IFullUserDetails[]>([]);
  const [totalUsers, setTotalUsers] = useState(0);
  const [page, setPage] = useState(1); // One, not zero, based.
  const [pageSize, setPageSize] = useState(5);

  const [form] = Form.useForm();
  const [editingUser, setEditingUser] = useState<
    IFullUserDetails | undefined
  >();
  const [errorMessage, setErrorMessage] = useState('');

  const userService = createService(
    UserManagementService,
    'UserManagementService'
  );

  useEffect(() => setPage(1), [searchText]);

  useEffect(() => {
    userService
      .getUsersDetails({
        districtId: user.districtId,
        page: page - 1,
        pageSize: pageSize,
        searchText: searchText,
      })
      .then(response => {
        setUsers(response.users);
        setTotalUsers(response.totalUsers!);
      })
      .catch(error => setErrorMessage(error.message ?? 'unknown error'));
  }, [page, pageSize, searchText]);

  useEffect(() => {
    form.resetFields();
    if (editingUser != null) {
      form.setFieldsValue(Object.assign({}, editingUser && editingUser.user!));
    }
  }, [editingUser]);

  useEffect(() => {
    if (errorMessage) {
      setTimeout(() => setErrorMessage(''), 5000);
    }
  }, [errorMessage]);

  function finish(values: FormData) {
    const newUserX: IUser = Object.assign(
      {},
      {id: editingUser?.user?.id},
      values ?? {}
    ) as IUser;
    const upsertRequest = {user: newUserX, ...values} as IUpsertUserRequest;
    userService
      .upsertUser(upsertRequest)
      .then(response => {
        if (response.error) {
          throw new Error(response.error);
        }
        setEditingUser(undefined);
      })
      .catch(reason => setErrorMessage(reason.message ?? 'unknown error'));
  }

  return (
    <>
      <DefaultPage title="Accounts">
        <div className="space-filler">
          <div
            className="error-notice"
            style={{display: errorMessage ? undefined : 'none'}}
          >
            {errorMessage}
          </div>
        </div>
        <Form form={form} className="form-container" onFinish={finish}>
          <div className="form-fields-single-line">
            <Form.Item>
              <Button
                type="primary"
                onClick={() => {
                  setSearchText('');
                  setShowSearchForAccount(true);
                  if (searchTextRef.current != null) {
                    searchTextRef.current.focus();
                  }
                }}
                style={{width: '100%'}}
              >
                Search for Account
              </Button>
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                onClick={() => {
                  form.resetFields();
                  form.isFieldsTouched;
                  const editingUser: IFullUserDetails = {user: {}};
                  setEditingUser(editingUser);
                }}
                style={{width: '100%'}}
              >
                Create New Account
              </Button>
            </Form.Item>
          </div>
          <CommonAccountFields
            form={form}
            disabled={editingUser?.user == null}
          />
          <Form.Item>
            <Button type="primary" htmlType="submit">
              {editingUser?.user?.id ?? 0 > 0 ? 'Save' : 'Add'}
            </Button>
          </Form.Item>
        </Form>
      </DefaultPage>
      <Modal
        title="Search for Account"
        open={showSearchForAccount}
        onCancel={() => setShowSearchForAccount(false)}
        onOk={() => setShowSearchForAccount(false)}
        centered
        forceRender={true}
      >
        <Form form={searchForm} className="form-container">
          <Form.Item label="Search" style={{margin: '0.5em'}}>
            <Input
              ref={searchTextRef}
              type="text"
              value={searchText}
              onChange={e => setSearchText(e.target.value)}
            />
          </Form.Item>
          <Table
            columns={[
              {
                key: 'action',
                title: '',
                render: (value, record) => (
                  <EditOutlined
                    onClick={() => {
                      setEditingUser(record.value);
                      setShowSearchForAccount(false);
                    }}
                  />
                ),
              },
              {
                key: 'firstName',
                title: 'First Name',
                dataIndex: 'firstName',
              },
              {key: 'lastName', title: 'Last Name', dataIndex: 'lastName'},
              {
                key: 'emailAddress',
                title: 'Email Address',
                dataIndex: 'emailAddress',
              },
            ]}
            dataSource={users.map(value => {
              return {
                key: value.user!.id!,
                value: value,
                firstName: value.user!.firstName!,
                lastName: value.user!.lastName!,
                emailAddress: value.user!.emailAddress!,
              };
            })}
            pagination={false}
            size="small"
            scroll={{x: true}}
          />
          <div
            style={{
              display: 'flex',
              flexFlow: 'column nowrap',
              alignItems: 'center',
              paddingTop: '0.5em',
              paddingBottom: '1em',
            }}
          >
            <Pagination
              total={totalUsers}
              onChange={(newPage, newPageSize) => {
                setPage(newPage);
                setPageSize(newPageSize);
              }}
              pageSize={pageSize}
              defaultPageSize={pageSize}
              current={page}
              defaultCurrent={page}
            />
          </div>
        </Form>
      </Modal>
    </>
  );
}
