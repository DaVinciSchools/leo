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
import {user_x_management} from 'pl-pb';
import {DynamicUserXAutocomplete} from '../../../libs/common_fields/DynamicUserXAutocomplete';
import {DeepReadOnly} from '../../../libs/misc';
import UserXManagementService = user_x_management.UserXManagementService;
import IFullUserXDetails = user_x_management.IFullUserXDetails;

export function Accounts() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be an administrator to edit profiles.',
    userX => userX.isAdminX
  );

  const formFields = useFormFields();
  const editingUserX =
    formFields.useSingleAutocompleteFormField<DeepReadOnly<IFullUserXDetails>>(
      'userX'
    );
  const disabled = editingUserX.getValue() != null;

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

  useEffect(() => {
    if (errorMessage) {
      setTimeout(() => setErrorMessage(''), 5000);
    }
  }, [errorMessage]);

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Accounts">
        <form>
          <div className="global-flex-column">
            <div className="global-flex-column" style={{height: '5em'}}>
              <DynamicUserXAutocomplete
                label="Search by Name or Email Address"
                baseRequest={{
                  inDistrictIds: userX.isAdminX
                    ? undefined
                    : userX.districtId != null
                    ? [userX.districtId]
                    : undefined,
                }}
                userXField={editingUserX}
              />
              <div
                className="global-error-notice"
                style={{display: errorMessage ? undefined : 'none'}}
              >
                {errorMessage}
              </div>
            </div>
            <ProfileEditor
              userXId={editingUserX.getValue?.()?.userX?.id ?? null}
              profileForm={profileForm}
              profileSaveStatus={profileSaveStatus}
            />
          </div>
        </form>
      </DefaultPage>
    </>
  );
}
