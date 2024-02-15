import './LoginForm.scss';

import {Backdrop, Box, Modal} from '@mui/material';
import {LoginForm} from './LoginForm';
import {CredentialsError} from '../GlobalStateProvider/GlobalStateProvider';

export function ModalLoginForm(props: {
  open: boolean;
  onSuccess: () => void;
  onError: (error?: CredentialsError | unknown) => void;
  onCancel: () => void;
}) {
  return (
    <>
      {props.open && (
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
              onError={props.onError}
              onCancel={props.onCancel}
            />
          </Box>
        </Modal>
      )}
    </>
  );
}
