import './LoginForm.scss';

import {Backdrop, Box, Modal} from '@mui/material';
import {LoginForm} from './LoginForm';

export function ModalLoginForm(props: {
  open: boolean;
  onSuccess: () => void;
  onFailure: () => void;
  onError: (error?: unknown) => void;
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
          <LoginForm
            onSuccess={props.onSuccess}
            onFailure={props.onFailure}
            onError={props.onError}
            onCancel={props.onCancel}
          />
        </Box>
      </Modal>
    </>
  );
}
