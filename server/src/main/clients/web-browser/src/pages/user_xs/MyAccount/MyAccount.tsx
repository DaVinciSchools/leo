import './MyAccount.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {useContext, useEffect, useState} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {useFormFields} from '../../../libs/forms';
import {useDelayedAction} from '../../../libs/delayed_action';
import {
  EditorProfile,
  ProfileEditor,
} from '../../../libs/ProfileEditor/ProfileEditor';
import {createService} from '../../../libs/protos';
import {user_x_management} from '../../../generated/protobuf-js';
import UserXManagementService = user_x_management.UserXManagementService;

export function MyAccount() {
  const global = useContext(GlobalStateContext);

  const profileForm = useFormFields({
    onChange: () => autoSave.trigger(),
  });
  const [profileSaveStatus, setProfileSaveStatus] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');

  const autoSave = useDelayedAction(
    () => {
      setProfileSaveStatus('Modified');
    },
    () => {
      setProfileSaveStatus('Saving...');
      const fullProfile: EditorProfile = profileForm.getValuesObject(true);
      createService(UserXManagementService, 'UserXManagementService')
        .upsertUserX({
          // TODO: Get rid of this hack.
          ...fullProfile,
          userX: {
            ...fullProfile,
            userX: fullProfile,
          },
        })
        .then(response => {
          setProfileSaveStatus('Saved');
          if (response.error != null) {
            setErrorMessage(response.error);
          }
        })
        .catch(global.setError);
    },
    1500
  );

  useEffect(() => {
    if (errorMessage !== '') {
      setTimeout(() => {
        setErrorMessage('');
      }, 5000);
    }
  }, [errorMessage]);

  return (
    <>
      <div className="global-flex-column">
        <DefaultPage title="My Account">
          <div className="global-space-filler">
            <div
              className="error-notice"
              style={{display: errorMessage !== '' ? undefined : 'none'}}
            >
              {errorMessage}
            </div>
          </div>
          <form>
            <ProfileEditor
              userXId={global?.userX?.userXId ?? null}
              profileForm={profileForm}
              profileSaveStatus={profileSaveStatus}
            />
          </form>
        </DefaultPage>
      </div>
    </>
  );
}
