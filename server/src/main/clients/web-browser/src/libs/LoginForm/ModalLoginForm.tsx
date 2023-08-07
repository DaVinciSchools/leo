import './LoginForm.scss';

import {Backdrop, Box, Modal} from '@mui/material';
import {LoginForm} from './LoginForm';

export function ModalLoginForm(props: {
  open: boolean;
  onLoggedIn: () => void;
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
        <Box className="login-form-modal" paddingX={8}>
          <LoginForm onLoggedIn={props.onLoggedIn} onCancel={props.onCancel} />
        </Box>
      </Modal>
    </>
  );
}
