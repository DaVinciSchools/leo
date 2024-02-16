import './HandleError.scss';

import Box, {BoxProps} from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import React, {useContext, useEffect, useState} from 'react';
import Typography from '@mui/material/Typography';
import {GlobalStateContext} from '../GlobalState';
import {createService} from '../protos';
import {error_service} from 'pl-pb';
import {genericJsonReplacer} from '../misc';
import {Button} from '@mui/material';
import ErrorService = error_service.ErrorService;
import IReportErrorRequest = error_service.IReportErrorRequest;

export const REPORT_ERROR_REQUEST_PROP_NAME = 'projectLeoReportErrorRequest';

enum LogErrorStatus {
  IDLE,
  LOGGING,
  LOGGED,
  FAILED,
}

const style: BoxProps['sx'] = {
  position: 'absolute' as const,
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: '24px',
  p: 4,
};

// TODO: Restore this to 'mailto:support@projectleo.net'
const baseMailToLink = 'mailto:sahendrickson@gmail.com';

// See the following for why this number:
// https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
// const MAX_EMAIL_URL_LENGTH = 2000;
const MAX_EMAIL_URL_LENGTH = 64000;

export function HandleError() {
  const global = useContext(GlobalStateContext);

  const [logErrorStatus, setLogErrorStatus] = useState(LogErrorStatus.IDLE);
  const [mailToLink, setMailToLink] = useState(baseMailToLink);
  const [issueLink, setIssueLink] = useState<string>();
  const [showIgnoreWarning, setShowIgnoreWarning] = useState(false);
  const [disableIgnoreWarningButton, setDisableIgnoreWarningButton] =
    useState(true);

  useEffect(() => {
    if (global.error == null) {
      setLogErrorStatus(LogErrorStatus.IDLE);
      setMailToLink('');
      setIssueLink(undefined);
      setShowIgnoreWarning(false);
      setDisableIgnoreWarningButton(true);
      return;
    }

    let reportErrorRequest = (
      global.error as {} as {[key: string]: IReportErrorRequest}
    )[REPORT_ERROR_REQUEST_PROP_NAME];
    if (reportErrorRequest == null) {
      reportErrorRequest = {
        requestUrl: window.location.href,
        error: JSON.stringify(global.error, genericJsonReplacer, 2),
      };
      (global.error as {} as {[key: string]: IReportErrorRequest})[
        REPORT_ERROR_REQUEST_PROP_NAME
      ] = reportErrorRequest;
    }

    if (logErrorStatus === LogErrorStatus.IDLE) {
      setLogErrorStatus(LogErrorStatus.LOGGING);
      createService(ErrorService, 'ErrorService')
        .reportError(reportErrorRequest)
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
Thank you for letting us know about this error.

Please add any details here that you think might be relevant to why it happened.

---- Error details are below. Please don't modify the text below this line. ----

  Issue: ${issueLink ?? 'Link is not available.'}
  Error: ${global.error?.name ?? 'Unknown'}
   From: ${window.location.href}
User Id: ${global.optionalUserX()?.id ?? 'Unknown'}

${JSON.stringify(global.error ?? 'Unknown', genericJsonReplacer, 2)}`.trim();

    const mailUrl = new URL(baseMailToLink);
    mailUrl.searchParams.set(
      'subject',
      'Error: ' + window.location.href + ' -- ' + new Date().toLocaleString()
    );
    mailUrl.searchParams.set('body', mailBody);

    let mailUrlString = mailUrl.href;
    mailUrlString =
      mailUrlString.length > MAX_EMAIL_URL_LENGTH
        ? mailUrlString
            .substring(0, MAX_EMAIL_URL_LENGTH - /* room for ellipses = */ 3)
            // Remove any partial encodings that may be clipped, e.g., from %20.
            .replace(/%[0-9]?$/, '') + '...'
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

  function ignoreError() {
    setDisableIgnoreWarningButton(true);
    setShowIgnoreWarning(true);
    setTimeout(() => {
      setDisableIgnoreWarningButton(false);
    }, 2000);
  }

  function goHome() {
    window.open('/', '_self');
  }

  return (
    <>
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
          <div id="modal-modal-description">
            <p>We're sorry for the inconvenience.</p>
            {logErrorStatus === LogErrorStatus.LOGGING ? (
              <>
                <p>We're logging this error now... one sec.</p>
              </>
            ) : logErrorStatus === LogErrorStatus.LOGGED ? (
              <>
                <p>We've logged this error and are going to look into it.</p>
                <p>
                  If this is urgent, please send us an e-mail using the
                  following link:{' '}
                  <a
                    href={mailToLink}
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    Contact Project Leo Support
                  </a>
                  .
                </p>
                <div className="error-action-links">
                  <Button variant="contained" onClick={reloadPage}>
                    Reload this&nbsp;page
                  </Button>
                  <Button variant="contained" onClick={ignoreError}>
                    Ignore this&nbsp;error
                  </Button>
                  <Button variant="contained" onClick={goHome}>
                    Go Home
                  </Button>
                </div>
              </>
            ) : (
              <>
                <p>Unfortunately, we were unable to log the error.</p>
                <p>
                  Please let us know about it by sending us an e-mail using the
                  following link:{' '}
                  <a
                    href={mailToLink}
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    Contact Project Leo Support
                  </a>
                  .
                </p>
                <div className="error-action-links">
                  <Button variant="contained" onClick={reloadPage}>
                    Reload this&nbsp;page
                  </Button>
                  <Button variant="contained" onClick={ignoreError}>
                    Ignore this&nbsp;error
                  </Button>
                  <Button variant="contained" onClick={goHome}>
                    Go Home
                  </Button>
                </div>
              </>
            )}
          </div>
        </Box>
      </Modal>
      <Modal
        open={showIgnoreWarning}
        onClose={closeWarning}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Ignoring this error.
          </Typography>
          <p style={{color: '#ee3300'}}>This may cause unexpected behavior.</p>
          <p>If you continue having issues. Please reload this page.</p>
          <div className="error-action-links">
            <Button variant="contained" onClick={reloadPage}>
              Reload this&nbsp;page
            </Button>
            <Button
              variant="contained"
              onClick={closeWarning}
              disabled={disableIgnoreWarningButton}
            >
              Acknowledge and&nbsp;Continue
              {disableIgnoreWarningButton ? ' (waiting...)' : ''}
            </Button>
          </div>
        </Box>
      </Modal>
    </>
  );
}
