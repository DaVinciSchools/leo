import './TeacherDashboard.scss';
import 'react-quill/dist/quill.snow.css';

import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {
  Autocomplete,
  Grid,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  TextField,
} from '@mui/material';
import {useEffect, useRef, useState} from 'react';
import {assignment_management, pl_types} from '../../../generated/protobuf-js';
import IClassX = pl_types.IClassX;
import IAssignment = pl_types.IAssignment;
import ReactQuill, {Value} from 'react-quill';
import {createService} from '../../../libs/protos';
import AssignmentManagementService = assignment_management.AssignmentManagementService;
import IUser = pl_types.IUser;

export function AssignmentsTab(props: {user: IUser}) {
  const [classXs, setClassXs] = useState<IClassX[]>([]);
  const [assignments, setAssignments] = useState<IAssignment[] | undefined>();
  const [saveStatus, setSaveStatus] = useState<'Saved' | 'Saving...'>('Saved');
  const [showDeleteAssignment, setShowDeleteAssignment] = useState(false);
  const [showCreateAssignment, setShowCreateAssignment] = useState(false);

  // Used to track the assignment to save asynchronously.
  const saveAssignmentTimeout = useRef<NodeJS.Timeout>();
  const assignmentRef = useRef<IAssignment>();
  const [assignment, fnSetAssignment] = useState<IAssignment | null>(null);
  const [classX, setClassX] = useState<IClassX | null>(null);
  const [name, setName] = useState('');
  const [shortDescr, setShortDescr] = useState('');
  const [longDescr, setLongDescr] = useState<Value>('');

  const classXSorter = (a: IClassX, b: IClassX) =>
    (a?.name ?? '').localeCompare(b?.name ?? '');
  const assignmentSorter = (a: IAssignment, b: IAssignment) =>
    classXSorter(a?.classX ?? {}, b?.classX ?? {}) ||
    (a?.name ?? '').localeCompare(b?.name ?? '');

  function setAssignment(newAssignment: IAssignment | null) {
    if (assignment != null) {
      saveAssignment();
    }

    fnSetAssignment(newAssignment);
    setClassX(newAssignment?.classX ?? null);
    setName(newAssignment?.name ?? '');
    setShortDescr(newAssignment?.shortDescr ?? '');
    setLongDescr(newAssignment?.longDescr ?? '');
  }

  // Any change to be written back to the database.
  useEffect(() => {
    clearTimeout(saveAssignmentTimeout.current);
    saveAssignmentTimeout.current = undefined;

    if (assignment != null) {
      setSaveStatus('Saving...');
      assignmentRef.current = assignment ?? undefined;
      assignment.classX = classX;
      assignment.name = name;
      assignment.shortDescr = shortDescr;
      assignment.longDescr = String(longDescr);

      saveAssignmentTimeout.current = setTimeout(() => {
        saveAssignment();
      }, 1000);
    }
  }, [classX, name, shortDescr, longDescr]);

  // Changes that affect labels in the select assignment dropdown.
  useEffect(() => {
    setAssignments(assignments != null ? [...assignments] : []);
  }, [classX, name, shortDescr]);

  function saveAssignment() {
    clearTimeout(saveAssignmentTimeout.current);
    saveAssignmentTimeout.current = undefined;

    if (assignmentRef.current != null) {
      const service = createService(
        AssignmentManagementService,
        'AssignmentManagementService'
      );
      service.saveAssignment({assignment: assignmentRef.current});
      setSaveStatus('Saved');
    }
  }

  useEffect(() => {
    const service = createService(
      AssignmentManagementService,
      'AssignmentManagementService'
    );
    service
      .getAssignments({
        teacherId: props.user.teacherId!,
      })
      .then(response => {
        setClassXs(response.classXs);
        setAssignments(response.assignments);
      })
      .catch(error => console.log('Error: ' + JSON.stringify(error)));
  }, []);

  function createNewAssignment(classXId: number) {
    const service = createService(
      AssignmentManagementService,
      'AssignmentManagementService'
    );
    service
      .createAssignment({classXId: classXId})
      .then(response => {
        setAssignments((assignments ?? []).concat([response.assignment!]));
        setAssignment(response.assignment!);
      })
      .catch(error => console.log('Error: ' + JSON.stringify(error)));
  }

  function deleteAssignment() {
    if (assignment == null) {
      return;
    }

    const service = createService(
      AssignmentManagementService,
      'AssignmentManagementService'
    );
    service
      .deleteAssignment({assignmentId: assignment!.id!})
      .then(() => {
        setAssignment(null);
        setAssignments(
          (assignments ?? []).filter(a => a.id !== assignment?.id)
        );
      })
      .catch(error => console.log('Error: ' + JSON.stringify(error)));
  }

  return (
    <>
      <Grid container paddingY={2} spacing={2} columns={{xs: 6, md: 12}}>
        <Grid item xs={12} className="section-heading">
          <div className="section-title">Select Assignment:</div>
        </Grid>
        <Grid item xs={12}>
          <Autocomplete
            id="assignment"
            value={assignment}
            autoHighlight
            options={(assignments ?? []).sort(assignmentSorter)}
            onChange={(e, value) => setAssignment(value)}
            getOptionLabel={assignment =>
              assignment?.name ?? '(Unnamed Assignment)'
            }
            isOptionEqualToValue={(option, value) => option.id === value.id}
            groupBy={assignment =>
              assignment?.classX?.name ?? '(Unnamed Class)'
            }
            disabled={assignments == null}
            size="small"
            fullWidth={true}
            renderOption={(props, option) => {
              return (
                <li {...props} key={option.id}>
                  {option?.name ?? '(Unnamed Assignment)'}
                </li>
              );
            }}
            renderInput={params => (
              <TextField {...params} label="Select Assignment" />
            )}
            loading={assignments == null}
            loadingText="Loading Assignments..."
          />
        </Grid>
        <Grid item xs={12} className="section-heading">
          <div className="section-title">Edit Assignment:</div>
          <div className="section-links">
            <div style={{display: assignment == null ? 'none' : undefined}}>
              {saveStatus}{' '}
            </div>
            <div style={{display: assignment == null ? 'none' : undefined}}>
              <span
                onClick={() => setShowDeleteAssignment(true)}
                className="clickable"
              >
                Delete
              </span>{' '}
            </div>
            <span
              onClick={() => setShowCreateAssignment(true)}
              className="clickable"
            >
              Create
            </span>
          </div>
        </Grid>
        <Grid item xs={4}>
          <Autocomplete
            id="class"
            value={classX}
            autoHighlight
            options={classXs.sort(classXSorter)}
            onChange={(e, value) => setClassX(value)}
            getOptionLabel={classX => classX?.name ?? ''}
            isOptionEqualToValue={(option, value) => option.id === value.id}
            disabled={true}
            size="small"
            fullWidth={true}
            renderOption={(props, option) => {
              return (
                <li {...props} key={option.id}>
                  {option?.name ?? '(Unnamed Class)'}
                </li>
              );
            }}
            renderInput={params => <TextField {...params} label="Class" />}
            loading={assignments == null}
            loadingText="Loading Assignments..."
          />
        </Grid>
        <Grid item xs={8}>
          <TextField
            required
            label="Name"
            value={name}
            onChange={e => setName(e.target.value)}
            disabled={assignment == null}
            size="small"
            fullWidth={true}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            label="Short Description"
            value={shortDescr}
            onChange={e => setShortDescr(e.target.value)}
            disabled={assignment == null}
            size="small"
            fullWidth={true}
          />
        </Grid>
        <Grid item xs={12}>
          <ReactQuill
            theme="snow"
            preserveWhitespace={true}
            value={longDescr}
            onChange={setLongDescr}
            onBlur={saveAssignment}
            readOnly={assignment == null}
          />
        </Grid>
        <Grid item xs={12} className="section-heading">
          <div className="section-title">Edit Ikigai Project Definition:</div>
          TODO
        </Grid>
      </Grid>
      <Dialog
        open={showCreateAssignment}
        onClose={() => setShowCreateAssignment(false)}
      >
        <DialogTitle style={{borderBottom: 'lightGrey solid 1px'}}>
          Select Class for New Assignment
        </DialogTitle>
        <List style={{padding: '1em 0px'}}>
          {classXs.map(classX => (
            <ListItem key={classX.id!}>
              <ListItemButton
                onClick={() => {
                  setShowCreateAssignment(false);
                  createNewAssignment(classX.id!);
                }}
              >
                <ListItemText primary={classX.name} />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Dialog>
      <Dialog
        open={showDeleteAssignment}
        onClose={() => setShowDeleteAssignment(false)}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">Delete Assignment</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Permanently delete{' '}
            <b>{assignment?.name ?? '(Unnamed Assignment)'}</b>?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => {
              setShowDeleteAssignment(false);
              deleteAssignment();
            }}
          >
            Delete Permanently
          </Button>
          <Button onClick={() => setShowDeleteAssignment(false)} autoFocus>
            Cancel
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
