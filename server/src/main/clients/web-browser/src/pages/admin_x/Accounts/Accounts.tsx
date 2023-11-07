import '../../../libs/global.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {useContext, useEffect, useRef, useState} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {useFormFields} from '../../../libs/form_utils/forms';
import {useDelayedAction} from '../../../libs/delayed_action';
import {
  EditorProfile,
  ProfileEditor,
} from '../../../libs/ProfileEditor/ProfileEditor';
import {createService} from '../../../libs/protos';
import {user_x_management} from 'pl-pb';
import {Button} from '@mui/material';
import {Form, Input, InputRef, Modal, Pagination, Table} from 'antd';
import {
  CloseSquareOutlined,
  EditOutlined,
  SearchOutlined,
} from '@ant-design/icons';
import UserXManagementService = user_x_management.UserXManagementService;
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import {toLong} from '../../../libs/misc';

export function Accounts() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be an administrator to edit profiles.',
    userX => userX.isAdminX
  );

  const [showSearchForAccount, setShowSearchForAccount] = useState(false);
  const [searchForm] = Form.useForm();
  const [searchText, setSearchText] = useState('');
  const [userXs, setUserXs] = useState<readonly IFullUserXDetails[]>([]);
  const [totalUserXs, setTotalUserXs] = useState(0);
  const [page, setPage] = useState(1); // One, not zero, based.
  const [pageSize, setPageSize] = useState(5);
  const [editingUserX, setEditingUserX] = useState<
    IFullUserXDetails | undefined
  >();

  const disabled = editingUserX?.userX?.id == null;

  const profileForm = useFormFields({
    onChange: () => autoSave.trigger(),
    disabled,
  });
  const [profileSaveStatus, setProfileSaveStatus] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const autoSave = useDelayedAction(
    () => {
      if (disabled) {
        setProfileSaveStatus('');
      } else {
        setProfileSaveStatus('Modified');
      }
    },
    () => {
      if (disabled) {
        setProfileSaveStatus('');
        return;
      }

      setProfileSaveStatus('Saving...');
      const fullProfile: EditorProfile = profileForm.getValuesObject(true);
      createService(UserXManagementService, 'UserXManagementService')
        .upsertUserX(
          // TODO: Get rid of this hack.
          Object.assign({}, fullProfile, {
            userX: Object.assign(
              {},
              fullProfile as unknown as IFullUserXDetails,
              {
                userX: fullProfile as unknown as IFullUserXDetails,
              }
            ),
          })
        )
        .then(response => {
          if (response.error != null) {
            setErrorMessage(response.error);
            setProfileSaveStatus('Save Error');
          } else {
            setProfileSaveStatus('Saved');
          }
        })
        .catch(global.setError);
    },
    1500
  );

  const clearError = useDelayedAction(
    () => {},
    () => {
      setErrorMessage('');
    },
    5000
  );

  useEffect(() => {
    if (errorMessage !== '') {
      clearError.trigger();
    }
  }, [errorMessage]);

  // Counter to force updates.
  const searchTextRef = useRef<InputRef>(null);

  useEffect(() => setPage(1), [searchText]);

  useEffect(() => {
    createService(UserXManagementService, 'UserXManagementService')
      .getUserXs({
        page: page - 1,
        pageSize: pageSize,
        firstLastEmailSearchText: searchText,
      })
      .then(response => {
        setUserXs(response.userXs);
        setTotalUserXs(toLong(response.totalUserXs!).toNumber());
      })
      .catch(global.setError);
  }, [page, pageSize, searchText, showSearchForAccount]);

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

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Accounts">
        <form>
          <div className="global-flex-column">
            <div className="global-flex-column" style={{height: '5em'}}>
              <Button
                variant="contained"
                onClick={() => {
                  setShowSearchForAccount(true);
                }}
              >
                Search for Account
              </Button>
              <div
                className="global-error-notice"
                style={{display: errorMessage ? undefined : 'none'}}
              >
                {errorMessage}
              </div>
            </div>
            <ProfileEditor
              userXId={editingUserX?.userX?.id ?? null}
              profileForm={profileForm}
              profileSaveStatus={profileSaveStatus}
            />
          </div>
        </form>
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
                suffix={
                  <CloseSquareOutlined onClick={() => setSearchText('')} />
                }
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
                  key: value.userX!.id!,
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
      </DefaultPage>
    </>
  );
}
