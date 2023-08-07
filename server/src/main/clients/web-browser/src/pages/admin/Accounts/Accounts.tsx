import './Accounts.scss';

import {
  CloseSquareOutlined,
  EditOutlined,
  SearchOutlined,
} from '@ant-design/icons';
import {CommonAccountFields} from '../../../libs/CommonAccountFields/CommonAccountFields';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {createService} from '../../../libs/protos';
import {pl_types, user_management} from '../../../generated/protobuf-js';
import {
  Button,
  Checkbox,
  Form,
  Input,
  InputRef,
  Modal,
  Pagination,
  Table,
} from 'antd';
import {sendToLogin} from '../../../libs/authentication';
import {useContext, useEffect, useRef, useState} from 'react';

import IFullUserDetails = user_management.IFullUserDetails;
import IUpsertUserRequest = user_management.IUpsertUserRequest;
import IUser = pl_types.IUser;
import UserManagementService = user_management.UserManagementService;

export function Accounts() {
  const global = useContext(GlobalStateContext);
  if (!global.user?.isAdmin) {
    return sendToLogin();
  }

  const [showSearchForAccount, setShowSearchForAccount] = useState(false);
  const [searchForm] = Form.useForm();
  const [searchText, setSearchText] = useState('');
  const [users, setUsers] = useState<IFullUserDetails[]>([]);
  const [totalUsers, setTotalUsers] = useState(0);
  const [page, setPage] = useState(1); // One, not zero, based.
  const [pageSize, setPageSize] = useState(5);

  const [selectUserForm] = Form.useForm();
  const [form] = Form.useForm();
  const [editingUser, setEditingUser] = useState<
    IFullUserDetails | undefined
  >();
  const [errorMessage, setErrorMessage] = useState('');
  // Counter to force updates.
  const [formChangeCount, setFormChangeCount] = useState(0);
  const searchTextRef = useRef<InputRef>(null);

  const userService = createService(
    UserManagementService,
    'UserManagementService'
  );

  useEffect(() => setPage(1), [searchText]);

  useEffect(() => {
    userService
      .getPagedUsersDetails({
        districtId: global.user?.districtId,
        page: page - 1,
        pageSize: pageSize,
        searchText: searchText,
      })
      .then(response => {
        setUsers(response.users);
        setTotalUsers(response.totalUsers!);
      })
      .catch(global.setError);
  }, [page, pageSize, searchText, showSearchForAccount]);

  useEffect(() => {
    form.resetFields();
    setFormChangeCount(formChangeCount + 1);
    if (editingUser != null) {
      form.setFieldsValue(Object.assign({}, editingUser, editingUser.user!));
    }
  }, [editingUser]);

  useEffect(() => {
    if (errorMessage) {
      setTimeout(() => setErrorMessage(''), 5000);
    }
  }, [errorMessage]);

  useEffect(() => {
    if (searchTextRef && searchTextRef.current) {
      searchTextRef.current.focus();
    }
  }, [searchTextRef.current]);

  function finish(values: FormData) {
    const newUserX: IUser = Object.assign(
      {},
      {userXId: editingUser?.user?.userXId},
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
      .catch(reason => global.setError({error: reason, reload: false}));
  }

  return (
    <>
      <DefaultPage title="Accounts">
        <div className="space-filler" style={{height: '2em'}}>
          <div
            className="error-notice"
            style={{display: errorMessage ? undefined : 'none'}}
          >
            {errorMessage}
          </div>
        </div>
        <Form form={selectUserForm} className="form-container">
          <div className="form-fields-single-line">
            <Form.Item>
              <Button
                type="primary"
                onClick={() => {
                  setShowSearchForAccount(true);
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
        </Form>
        <Form
          form={form}
          className="form-container"
          onFinish={finish}
          disabled={editingUser == null}
          onChange={() => setFormChangeCount(formChangeCount + 1)}
        >
          <CommonAccountFields form={form} />
          <div style={{display: global.user?.isAdmin ? undefined : 'none'}}>
            <Form.Item
              style={{margin: '0 0'}}
              name="isAdmin"
              valuePropName="checked"
            >
              <Checkbox>Administrator</Checkbox>
            </Form.Item>
            <Form.Item
              style={{margin: '0 0'}}
              name="isTeacher"
              valuePropName="checked"
            >
              <Checkbox>Teacher</Checkbox>
            </Form.Item>
            <div
              className="form-subsection"
              style={{
                display:
                  form.getFieldValue('isTeacher') === true ? undefined : 'none',
              }}
            >
              TODO
            </div>
            <Form.Item
              style={{margin: '0 0'}}
              name="isStudent"
              valuePropName="checked"
            >
              <Checkbox
                onChange={() => {
                  if (form.getFieldValue('isStudent') === false) {
                    form.setFieldValue('districtStudentId', undefined);
                    form.setFieldValue('studentGrade', undefined);
                  }
                }}
              >
                Student
              </Checkbox>
            </Form.Item>
            <div
              className="form-subsection"
              style={{
                display:
                  form.getFieldValue('isStudent') === true ? undefined : 'none',
              }}
            >
              <Form.Item
                rules={[
                  {
                    message: 'A student ID must be a number.',
                    pattern: new RegExp(/^[0-9]*$/),
                  },
                ]}
                name="districtStudentId"
                normalize={value => (value.length !== 0 ? value : undefined)}
              >
                <Input placeholder="Student ID" maxLength={25} />
              </Form.Item>
              <Form.Item
                rules={[
                  {
                    message: 'A student grade must be a number.',
                    pattern: new RegExp(/^[0-9]*$/),
                  },
                ]}
                name="studentGrade"
                normalize={value => (value.length !== 0 ? value : undefined)}
              >
                <Input placeholder="Student Grade" maxLength={2} />
              </Form.Item>
            </div>
            <Form.Item
              style={{display: editingUser != null ? undefined : 'none'}}
            >
              <Button type="primary" htmlType="submit" style={{width: '6em'}}>
                {editingUser?.user?.userXId ?? 0 > 0 ? 'Save' : 'Add'}
              </Button>
              &nbsp;
              <Button
                onClick={() => setEditingUser(undefined)}
                style={{width: '6em'}}
              >
                Cancel
              </Button>
            </Form.Item>
          </div>
        </Form>
      </DefaultPage>
      <Modal
        title="Search for Account"
        open={showSearchForAccount}
        onCancel={() => setShowSearchForAccount(false)}
        okButtonProps={{style: {display: 'none'}}}
        width="80%"
        style={{
          position: 'absolute',
          left: '10%',
          top: '5%',
          width: '80%',
          height: '90%',
          minWidth: '80%',
          minHeight: '90%',
        }}
        bodyStyle={{
          width: '100%',
          height: '100%',
          minWidth: '100%',
          minHeight: '100%',
        }}
      >
        <Form form={searchForm} className="form-container">
          <Form.Item style={{margin: '0.5em'}}>
            <Input
              ref={searchTextRef}
              type="text"
              placeholder="Enter Text to Filter List"
              value={searchText}
              onChange={e => setSearchText(e.target.value)}
              autoFocus={true}
              prefix={<SearchOutlined />}
              suffix={<CloseSquareOutlined onClick={() => setSearchText('')} />}
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
                    className="edit-icon"
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
                key: value.user!.userXId!,
                value: value,
                firstName: value.user!.firstName!,
                lastName: value.user!.lastName!,
                emailAddress: value.user!.emailAddress!,
              };
            })}
            pagination={false}
            size="small"
            scroll={{x: true}}
            onRow={record => ({
              onClick: () => {
                setEditingUser(record.value);
                setShowSearchForAccount(false);
              },
            })}
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
