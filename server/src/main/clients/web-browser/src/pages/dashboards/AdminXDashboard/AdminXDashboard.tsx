import './AdminXDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import React, {Fragment, useContext, useEffect, useState} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {
  Button,
  Collapse,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import {DeepReadOnly, toLong} from '../../../libs/misc';
import {createService} from '../../../libs/protos';
import {task_service} from 'pl-pb';
import {KeyboardArrowDown, KeyboardArrowUp} from '@mui/icons-material';
import formatDuration from 'format-duration';
import TaskService = task_service.TaskService;
import ITaskQueueStatus = task_service.ITaskQueueStatus;

export function AdminXDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be an administrator to view this dashboard.',
    userX => userX.isAdminX
  );
  const [taskQueueStatuses, setTaskQueueStatuses] = useState<
    DeepReadOnly<ITaskQueueStatus>[]
  >([]);
  const [errorDetailsRow, setErrorDetailsRow] = useState<string>();

  useEffect(() => {
    createService(TaskService, 'TaskService')
      .getTaskQueuesStatus({})
      .then(response => {
        setTaskQueueStatuses(response.taskQueueStatuses ?? []);
      });
  }, []);

  function rescanForTasks(name?: string | null) {
    createService(TaskService, 'TaskService')
      .scanForTasks({name})
      .catch(global.setError);
  }

  function resetTaskQueues(name?: string | null) {
    createService(TaskService, 'TaskService')
      .resetTaskQueues({name})
      .catch(global.setError);
  }

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Admin Dashboard">
        <ul>
          <li>
            <a
              href="https://analytics.google.com/analytics/web/?utm_source=marketingplatform.google.com&utm_medium=et&utm_campaign=marketingplatform.google.com%2Fabout%2Fanalytics%2F#/p398130888/reports/intelligenthome"
              target="_blank"
            >
              Google Analytics
            </a>
          </li>
          <li>
            <a
              href="https://trends.google.com/trends/explore?q=project%20leo&date=now%201-d&geo=US&hl=en"
              target="_blank"
            >
              Google Trends
            </a>
          </li>
        </ul>
        <TableContainer
          component={Paper}
          style={{width: 'fit-content', maxWidth: '100%'}}
        >
          <Table aria-label="Task Status">
            <TableHead>
              <TableRow>
                <TableCell>Task Worker</TableCell>
                <TableCell align="center">Last Error</TableCell>
                <TableCell align="right"># Processing</TableCell>
                <TableCell align="right"># Pending</TableCell>
                <TableCell align="right"># Processed</TableCell>
                <TableCell align="right">Avg. Processing Time</TableCell>
                <TableCell align="right"># Skipped</TableCell>
                <TableCell align="right"># Submitted</TableCell>
                <TableCell align="right"># Retries</TableCell>
                <TableCell align="right"># Failures</TableCell>
                <TableCell align="right">Avg. Failure Time</TableCell>
                <TableCell align="right"># Unrecoverable Errors</TableCell>
                <TableCell align="center">
                  <Button onClick={() => rescanForTasks()}>Rescan All</Button>
                </TableCell>
                <TableCell align="center">
                  <Button onClick={() => resetTaskQueues()}>Reset All</Button>
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {taskQueueStatuses
                .sort((a, b) => (a.name ?? '').localeCompare(b.name ?? ''))
                .map(s => (
                  <Fragment key={s.name}>
                    <TableRow
                      sx={{'&:last-child td, &:last-child th': {border: 0}}}
                    >
                      <TableCell component="th" scope="row">
                        {s.name}
                      </TableCell>
                      <TableCell>
                        <IconButton
                          aria-label="expand row"
                          size="small"
                          onClick={() =>
                            setErrorDetailsRow(
                              errorDetailsRow ? undefined : s.name ?? undefined
                            )
                          }
                        >
                          {errorDetailsRow === s.name ? (
                            <KeyboardArrowUp />
                          ) : (
                            <KeyboardArrowDown />
                          )}
                        </IconButton>
                      </TableCell>
                      <TableCell align="right">{s.processingTasks}</TableCell>
                      <TableCell align="right">{s.pendingTasks}</TableCell>
                      <TableCell align="right">{s.processedTasks}</TableCell>
                      <TableCell align="right">
                        {formatDuration(
                          toLong(s.processingTimeMs ?? 0).toNumber(),
                          {ms: true}
                        )}
                      </TableCell>
                      <TableCell align="right">{s.skippedTasks}</TableCell>
                      <TableCell align="right">{s.submittedTasks}</TableCell>
                      <TableCell align="right">{s.retries}</TableCell>
                      <TableCell align="right">{s.failures}</TableCell>
                      <TableCell align="right">
                        {formatDuration(
                          toLong(s.failedProcessingTimeMs ?? 0).toNumber(),
                          {ms: true}
                        )}
                      </TableCell>
                      <TableCell align="right">{s.errors}</TableCell>
                      <TableCell align="center">
                        <Button onClick={() => rescanForTasks(s.name)}>
                          Rescan
                        </Button>
                      </TableCell>
                      <TableCell align="center">
                        <Button onClick={() => resetTaskQueues(s.name)}>
                          Reset
                        </Button>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell style={{padding: 0}} colSpan={9}>
                        <Collapse
                          in={errorDetailsRow === s.name}
                          timeout="auto"
                          unmountOnExit
                        >
                          <pre style={{padding: '1em'}}>
                            {s.lastFailure || 'No failures recorded.'}
                          </pre>
                        </Collapse>
                      </TableCell>
                    </TableRow>
                  </Fragment>
                ))}
            </TableBody>
          </Table>
        </TableContainer>
      </DefaultPage>
    </>
  );
}
