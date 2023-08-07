import './LoginForm.scss';

import {Backdrop, Box, Modal} from '@mui/material';
import {LoginForm} from './LoginForm';

export function ModalLoginForm(props: {
  open: boolean;
  successAction: () => void;
  failureAction: () => void;
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
            successAction={props.successAction}
            cancelAction={props.failureAction}
          />
        </Box>
      </Modal>
    </>
  );
}
