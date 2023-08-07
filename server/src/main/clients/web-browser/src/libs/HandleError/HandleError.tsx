import './HandleError.scss';

import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import React, {useContext, useEffect, useState} from 'react';
import Typography from '@mui/material/Typography';
import {GlobalStateContext} from '../GlobalState';
import {createService} from '../protos';
import {error_service} from '../../generated/protobuf-js';

export const REPORT_ERROR_REQUEST_PROP_NAME = 'projectLeoReportErrorRequest';

enum LogErrorStatus {
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

const baseMailToLink =
  'mailto:seno@davincischools.org?cc=sahendrickson@gmail.com';

// See the following for why this number:
// https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
const maximumEmailUrlLength = 2000;

export type HandleErrorType =
  | {error?: Error | unknown | null | undefined; reload: boolean}
  | Error
  | unknown
  | undefined
  | null;

export function HandleError(props: {
  error: HandleErrorType;
  setError: (error: HandleErrorType) => void;
}) {
  const global = useContext(GlobalStateContext);

  const [reportErrorRequest, setReportErrorRequest] =
    useState<error_service.IReportErrorRequest>({});
  const [errorReload, setErrorReload] = useState(true);

  const [subject, setSubject] = useState('');
  const [logErrorStatus, setLogErrorStatus] = useState(LogErrorStatus.LOGGING);
  const [issueLink, setIssueLink] = useState('');
  const [logErrorBody, setLogErrorBody] = useState('');
  const [errorBody, setErrorBody] = useState('');
  const [mailToLink, setMailToLink] = useState(baseMailToLink);

  useEffect(() => {
    setMailToLink(baseMailToLink);

    if (!props.error || !subject) {
      return;
    }

    const assembledMailToLink =
      baseMailToLink +
      '&subject=' +
      encodeURIComponent(subject) +
      '&body=' +
      encodeURIComponent(`Add any details you think might be useful. Or, just click send.

--- Error ---

${issueLink + logErrorBody + errorBody}`);

    setMailToLink(
      assembledMailToLink.length > maximumEmailUrlLength
        ? assembledMailToLink
            .substring(0, maximumEmailUrlLength - 3)
            .replace(/%.?$/, '') + '...'
        : assembledMailToLink
    );
  }, [subject, issueLink, logErrorBody, errorBody]);

  useEffect(() => {
    setSubject(
      `Project Leo Website Error: '${
        reportErrorRequest.name ?? 'Unknown'
      }' on ${new Date().toUTCString()}`
    );

    setErrorBody(`User Id: ${global.user?.userXId ?? 'none'}

Error: ${reportErrorRequest.name ?? 'Unknown'}
From: ${window.location.href}
  To: ${reportErrorRequest?.request?.url ?? 'Unknown'}
 Via: ${reportErrorRequest?.response?.url ?? 'Unknown'}

Message:
${reportErrorRequest.message ?? 'Unknown'}

Stack Trace:
${reportErrorRequest.stack ?? 'Unknown'}`);
  }, [reportErrorRequest]);

  useEffect(() => {
    setLogErrorStatus(LogErrorStatus.LOGGING);
    setReportErrorRequest({});
    setIssueLink('');
    setLogErrorBody('');
    setErrorReload(true);

    if (props.error != null) {
      let reportErrorRequest: error_service.IReportErrorRequest = {};
      let error = props.error;
      if (
        error != null &&
        typeof error === 'object' &&
        'error' in error &&
        error.error != null
      ) {
        error = error.error;
      }
      if (error != null && typeof error === 'object') {
        const embeddedReportErrorRequest:
          | error_service.IReportErrorRequest
          | undefined = (
          error as {[k: string]: error_service.IReportErrorRequest}
        )[REPORT_ERROR_REQUEST_PROP_NAME];
        if (embeddedReportErrorRequest != null) {
          reportErrorRequest = embeddedReportErrorRequest;
        } else {
          let name = '';
          if ('name' in error) {
            reportErrorRequest.name = name = String(error.name);
          }
          let message = '';
          if ('message' in error!) {
            reportErrorRequest.message = message = String(error.message);
          }
          if ('stack' in error) {
            reportErrorRequest.stack = String(error.stack)
              .replace(name, '[name]')
              .replace(message, '[message]');
          }
        }
        if ('reload' in error) {
          setErrorReload(error.reload === true);
        }
      } else {
        reportErrorRequest.name = 'Unknown';
        reportErrorRequest.message = JSON.stringify(error);
        setErrorReload(true);
      }
      setReportErrorRequest(reportErrorRequest);

      createService(error_service.ErrorService, 'ErrorService')
        .reportError(reportErrorRequest)
        .then(response => {
          if (response.issueLink) {
            setIssueLink(`Issue Link: ${response.issueLink}\n\n`);
          }
          if (!response.failureReason) {
            setLogErrorStatus(LogErrorStatus.LOGGED);
          } else {
            setLogErrorBody(`Log Error: ${response.failureReason}.\n\n`);
            setLogErrorStatus(LogErrorStatus.FAILED);
          }
        })
        .catch(reason => {
          if (reason instanceof Error) {
            setLogErrorBody(`Log Error: ${reason.name}.\n\n`);
          } else {
            setLogErrorBody(`Log Error: ${JSON.stringify(reason)}\n\n`);
          }
          setLogErrorStatus(LogErrorStatus.FAILED);
        });
    }
  }, [props.error]);

  function closeWarning() {
    props.setError(undefined);
  }

  function reloadPage() {
    window.location.reload();
  }

  function goHome() {
    window.open('/', '_self');
  }

  return (
    <Modal
      open={props.error != null}
      onClose={() => (errorReload ? reloadPage() : closeWarning())}
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
                If this is urgent please send an e-mail to{' '}
                <a href={mailToLink} target="_blank" rel="noopener noreferrer">
                  Steve Eno (seno@davincischools.org)
                </a>
                . Use{' '}
                <a href={mailToLink} target="_blank" rel="noopener noreferrer">
                  this link
                </a>{' '}
                to pre-populate your e-mail with error details.
              </p>
              <p>We'll try to respond as soon as possible. Thank you!</p>
            </>
          ) : (
            <>
              <p>Unfortunately, we were unable to log the error.</p>
              <p>
                Please let us know about it by sending an e-mail to{' '}
                <a href={mailToLink} target="_blank" rel="noopener noreferrer">
                  Steve Eno (seno@davincischools.org)
                </a>
                . Use{' '}
                <a href={mailToLink} target="_blank" rel="noopener noreferrer">
                  this link
                </a>{' '}
                to pre-populate your e-mail with error details.
              </p>
              <p>
                We really appreciate your feedback and we'll try to respond as
                soon as possible. Thank you!
              </p>
            </>
          )}
          <div className="error-action-links">
            {!errorReload && <span onClick={closeWarning}>Close</span>}
            <span onClick={reloadPage}>Reload</span>
            <span onClick={goHome}>Go Home</span>
          </div>
        </Typography>
      </Box>
    </Modal>
  );
}
