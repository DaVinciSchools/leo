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
import {pl_types, user_x_management} from '../../../generated/protobuf-js';
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
import {useContext, useEffect, useRef, useState} from 'react';

import IUpsertUserXRequest = user_x_management.IUpsertUserXRequest;
import IUserX = pl_types.IUserX;
import UserXManagementService = user_x_management.UserXManagementService;
import IFullUserXDetails = user_x_management.IFullUserXDetails;

export function Accounts() {
  const global = useContext(GlobalStateContext);
  if (!global.requireUserX(userX => userX.isAdminX)) {
    return <></>;
  }

  const [showSearchForAccount, setShowSearchForAccount] = useState(false);
  const [searchForm] = Form.useForm();
  const [searchText, setSearchText] = useState('');
  const [userXs, setUserXs] = useState<IFullUserXDetails[]>([]);
  const [totalUserXs, setTotalUserXs] = useState(0);
  const [page, setPage] = useState(1); // One, not zero, based.
  const [pageSize, setPageSize] = useState(5);

  const [selectUserXForm] = Form.useForm();
  const [form] = Form.useForm();
  const [editingUserX, setEditingUserX] = useState<
    IFullUserXDetails | undefined
  >();
  const [errorMessage, setErrorMessage] = useState('');
  // Counter to force updates.
  const [formChangeCount, setFormChangeCount] = useState(0);
  const searchTextRef = useRef<InputRef>(null);

  const userXService = createService(
    UserXManagementService,
    'UserXManagementService'
  );

  useEffect(() => setPage(1), [searchText]);

  useEffect(() => {
    userXService
      .getPagedUserXsDetails({
        districtId: global.userX?.districtId,
        page: page - 1,
        pageSize: pageSize,
        searchText: searchText,
      })
      .then(response => {
        setUserXs(response.userXs);
        setTotalUserXs(response.totalUserXs!);
      })
      .catch(global.setError);
  }, [page, pageSize, searchText, showSearchForAccount]);

  useEffect(() => {
    form.resetFields();
    setFormChangeCount(formChangeCount + 1);
    if (editingUserX != null) {
      form.setFieldsValue(Object.assign({}, editingUserX, editingUserX.userX!));
    }
  }, [editingUserX]);

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
    const newUserX: IUserX = Object.assign(
      {},
      {userXId: editingUserX?.userX?.userXId},
      values ?? {}
    ) as IUserX;
    const upsertRequest = {userX: newUserX, ...values} as IUpsertUserXRequest;
    userXService
      .upsertUserX(upsertRequest)
      .then(response => {
        if (response.error) {
          throw new Error(response.error);
        }
        setEditingUserX(undefined);
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
        <Form form={selectUserXForm} className="form-container">
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
                  const editingUserX: IFullUserXDetails = {userX: {}};
                  setEditingUserX(editingUserX);
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
          disabled={editingUserX == null}
          onChange={() => setFormChangeCount(formChangeCount + 1)}
        >
          <CommonAccountFields form={form} />
          <div style={{display: global.userX?.isAdminX ? undefined : 'none'}}>
            <Form.Item
              style={{margin: '0 0'}}
              name="isAdminX"
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
              style={{display: editingUserX != null ? undefined : 'none'}}
            >
              <Button type="primary" htmlType="submit" style={{width: '6em'}}>
                {editingUserX?.userX?.userXId ?? 0 > 0 ? 'Save' : 'Add'}
              </Button>
              &nbsp;
              <Button
                onClick={() => setEditingUserX(undefined)}
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
                      setEditingUserX(record.value);
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
            dataSource={userXs.map(value => {
              return {
                key: value.userX!.userXId!,
                value: value,
                firstName: value.userX!.firstName!,
                lastName: value.userX!.lastName!,
                emailAddress: value.userX!.emailAddress!,
              };
            })}
            pagination={false}
            size="small"
            scroll={{x: true}}
            onRow={record => ({
              onClick: () => {
                setEditingUserX(record.value);
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
              total={totalUserXs}
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
