import './MyAccount.scss';
import '../../../libs/global.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {useContext, useEffect, useState} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {useFormFields} from '../../../libs/form_utils/forms';
import {useDelayedAction} from '../../../libs/delayed_action';
import {
  EditorProfile,
  ProfileEditor,
} from '../../../libs/ProfileEditor/ProfileEditor';
import {createService} from '../../../libs/protos';
import {user_x_management} from '../../../generated/protobuf-js';
import UserXManagementService = user_x_management.UserXManagementService;
import IFullUserXDetails = user_x_management.IFullUserXDetails;

export function MyAccount() {
  const global = useContext(GlobalStateContext);

  const profileForm = useFormFields({
    onChange: () => autoSave.trigger(),
  });
  const [profileSaveStatus, setProfileSaveStatus] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');

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

  const autoSave = useDelayedAction(
    () => {
      setProfileSaveStatus('Modified');
    },
    () => {
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

  return (
    <>
      <div className="global-flex-column">
        <DefaultPage title="My Account">
          <div className="global-space-filler">
            <div
              className="global-error-notice"
              style={{display: errorMessage ? undefined : 'none'}}
            >
              {errorMessage}
            </div>
          </div>
          <form>
            <ProfileEditor
              userXId={global?.userX?.id ?? null}
              profileForm={profileForm}
              profileSaveStatus={profileSaveStatus}
            />
          </form>
        </DefaultPage>
      </div>
    </>
  );
}
