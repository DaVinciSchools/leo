import './StandardModal.scss';

import {PropsWithChildren, ReactNode} from 'react';
import Modal from '@mui/material/Modal';
import {Backdrop, Button} from '@mui/material';

export function StandardModal(
  props: PropsWithChildren<{
    open: boolean;
    title: string;
    onClose: () => void;
    okText: string;
    onOk: () => void;
    enableOk?: boolean;
    children: ReactNode;
  }>
) {
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
        onClose={props.onClose}
      >
        <div className="standard-modal-layout">
          <div className="standard-modal-title">{props.title}</div>
          <div>{props.children}</div>
          <div className="standard-modal-footer">
            <Button
              variant="contained"
              onClick={props.onOk}
              disabled={props.enableOk === false}
            >
              {props.okText}
            </Button>
            <Button variant="contained" onClick={props.onClose}>
              Cancel
            </Button>
          </div>
        </div>
      </Modal>
    </>
  );
}
