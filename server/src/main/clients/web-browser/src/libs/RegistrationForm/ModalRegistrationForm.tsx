import './RegistrationForm.scss';

import Modal from '@mui/material/Modal';
import {Backdrop, Box} from '@mui/material';
import {RegistrationForm} from './RegistrationForm';
import {user_x_management} from 'pl-pb';

import IRegisterUserXRequest = user_x_management.IRegisterUserXRequest;

export function ModalRegistrationForm(props: {
  open: boolean;
  onRegisterUserX: (request: IRegisterUserXRequest) => void;
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
            onRegisterUserX={props.onRegisterUserX}
            onCancel={props.onCancel}
          />
        </Box>
      </Modal>
    </>
  );
}
