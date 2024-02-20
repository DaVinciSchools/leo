import {Dialog, Paper} from '@mui/material';
import {LoginForm} from './LoginForm';
import {styled} from 'styled-components';

const StyledDialog = styled(Paper)`
  padding: ${props => props.theme.spacing(4)};
`;

export function ModalLoginForm(props: {
  open: boolean;
  onSuccess: () => void;
  onFailure: () => void;
  onError: (error?: unknown) => void;
  onCancel: () => void;
}) {
  return (
    <Dialog
      open={props.open}
      onClose={() => props.onCancel()}
      PaperComponent={StyledDialog}
    >
      <LoginForm
        onSuccess={props.onSuccess}
        onFailure={props.onFailure}
        onError={props.onError}
        onCancel={props.onCancel}
      />
    </Dialog>
  );
}
