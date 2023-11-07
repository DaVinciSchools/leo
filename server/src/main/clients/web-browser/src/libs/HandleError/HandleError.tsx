import './HandleError.scss';

import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import React, {useContext, useEffect, useState} from 'react';
import Typography from '@mui/material/Typography';
import {GlobalStateContext} from '../GlobalState';
import {createService} from '../protos';
import {error_service} from 'pl-pb';
import {asObject} from '../misc';
import ErrorService = error_service.ErrorService;

export const REPORT_ERROR_REQUEST_PROP_NAME = 'projectLeoReportErrorRequest';

enum LogErrorStatus {
  IDLE,
  LOGGING,
  LOGGED,
  FAILED,
}

const style = {
  position: 'absolute' as const,
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

const baseMailToLink = 'mailto:support@projectleo.net';

// See the following for why this number:
// https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
const maximumEmailUrlLength = 2000;

export type HandleErrorType = Error | unknown | undefined | null;

export function HandleError() {
  const global = useContext(GlobalStateContext);

  const [logErrorStatus, setLogErrorStatus] = useState(LogErrorStatus.IDLE);
  const [mailToLink, setMailToLink] = useState(baseMailToLink);
  const [issueLink, setIssueLink] = useState<string>();

  useEffect(() => {
    if (global.error == null) {
      setLogErrorStatus(LogErrorStatus.IDLE);
      setMailToLink('');
      setIssueLink(undefined);
      return;
    }

    const error = asObject(global.error);

    const name =
      (error?.name ?? 'Unknown') +
      ' from ' +
      window.location.href +
      ' on ' +
      new Date().toUTCString();

    const message = `
  Error: ${error?.name ?? 'Unknown'}    
   From: ${window.location.href}
     To: ${error?.request?.url ?? 'Unknown'}
    Via: ${error?.response?.url ?? 'Unknown'}
User Id: ${global.optionalUserX()?.id ?? 'Unknown'}

${error ?? 'No more information.'}
`.trim();

    const stack = (error?.stack ?? 'Unavailable')
      .replace(error?.message ?? '', '[message]')
      .replace(error?.name ?? '', '[name]');

    if (logErrorStatus === LogErrorStatus.IDLE) {
      setLogErrorStatus(LogErrorStatus.LOGGING);
      createService(ErrorService, 'ErrorService')
        .reportError({
          name,
          message,
          stack,
          request: {
            url: error?.request?.url ?? 'Unknown',
            body: error?.request ?? 'Unknown',
          },
          response: {
            url: error?.response?.url ?? 'Unknown',
            body: error?.response ?? 'Unknown',
          },
        })
        .then(response => {
          setLogErrorStatus(LogErrorStatus.LOGGED);
          setIssueLink(response?.issueLink ?? undefined);
        })
        .catch(e => {
          setLogErrorStatus(LogErrorStatus.FAILED);
          // This request was best effort. So, ignore any errors.
          console.error(e);
        });
    }

    const mailBody = `
  Issue: ${issueLink ?? 'Link is not available.'}
  Error: ${name}    
   From: ${window.location.href}
     To: ${error?.request?.url ?? 'Unknown'}
    Via: ${error?.response?.url ?? 'Unknown'}
User Id: ${global.optionalUserX()?.id ?? 'Unknown'}

Message:
${error.message ?? 'Unknown'}

Stack Trace:
${error.stack ?? 'Unknown'}`.trim();

    const mailUrl = new URL(baseMailToLink);
    mailUrl.searchParams.set('subject', name);
    mailUrl.searchParams.set('body', mailBody);

    let mailUrlString = mailUrl.href;
    mailUrlString =
      mailUrlString.length > maximumEmailUrlLength
        ? mailUrlString
            .substring(0, maximumEmailUrlLength - /* room for ellipse= */ 3)
            // Remove any partial encodings that may be clipped, e.g., from %20.
            .replace(/%.?$/, '') + '...'
        : mailUrlString;

    if (mailToLink !== mailUrlString) {
      setMailToLink(mailUrlString);
    }
  }, [global.error, global.optionalUserX(), issueLink]);

  function closeWarning() {
    global.setError(undefined);
  }

  function reloadPage() {
    window.location.reload();
  }

  function goHome() {
    window.open('/', '_self');
  }

  return (
    <Modal
      open={global.error != null}
      onClose={() => closeWarning}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          Oops! An error occurred!
        </Typography>
        <Typography id="modal-modal-description" sx={{mt: 2}}>
          <p>We're sorry for the inconvenience.</p>
          {logErrorStatus === LogErrorStatus.LOGGING ? (
            <>
              <p>We're logging this error now... one sec.</p>
            </>
          ) : logErrorStatus === LogErrorStatus.LOGGED ? (
            <>
              <p>We've logged this error and are going to look into it.</p>
              <p>
                If this is urgent, please send us an e-mail using the following
                link:{' '}
                <a href={mailToLink} target="_blank" rel="noopener noreferrer">
                  Contact Project Leo Support
                </a>
                .
              </p>
              <p>We'll try to respond as soon as possible. Thank you!</p>
            </>
          ) : (
            <>
              <p>Unfortunately, we were unable to log the error.</p>
              <p>
                Please let us know about it by sending us an e-mail using the
                following link:{' '}
                <a href={mailToLink} target="_blank" rel="noopener noreferrer">
                  Contact Project Leo Support
                </a>
                .
              </p>
              <p>We'll try to respond as soon as possible. Thank you!</p>
            </>
          )}
          <div className="error-action-links">
            <span onClick={reloadPage}>Reload</span>
            <span onClick={goHome}>Go Home</span>
          </div>
        </Typography>
      </Box>
    </Modal>
  );
}
