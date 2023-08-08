import './RegistrationForm.scss';

import Modal from '@mui/material/Modal';
import {Backdrop, Box} from '@mui/material';
import {RegistrationForm} from './RegistrationForm';
import {user_management} from '../../generated/protobuf-js';

import IRegisterUserRequest = user_management.IRegisterUserRequest;

export function ModalRegistrationForm(props: {
  open: boolean;
  onRegisterUser: (request: IRegisterUserRequest) => void;
  onCancel: () => void;
}) {
  return (
    <>
      <Modal
        open={props.open}
        slots={{backdrop: Backdrop}}
        slotProps={{
          backdrop: {
            timeout: 500,
          },
        }}
      >
        <Box className="registration-form-modal" padding={3}>
          <RegistrationForm
            onRegisterUser={props.onRegisterUser}
            onCancel={props.onCancel}
          />
        </Box>
      </Modal>
    </>
  );
}
