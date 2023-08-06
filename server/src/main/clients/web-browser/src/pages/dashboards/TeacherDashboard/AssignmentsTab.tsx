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
import {useContext, useEffect, useRef, useState} from 'react';
import {
  assignment_management,
  pl_types,
  project_management,
} from '../../../generated/protobuf-js';
import ReactQuill, {Value} from 'react-quill';
import {createService} from '../../../libs/protos';
import IClassX = pl_types.IClassX;
import IAssignment = pl_types.IAssignment;
import AssignmentManagementService = assignment_management.AssignmentManagementService;
import IUser = pl_types.IUser;
import IProjectDefinition = pl_types.IProjectDefinition;
import ProjectManagementService = project_management.ProjectManagementService;
import IProjectInputValue = pl_types.IProjectInputValue;
import {GlobalStateContext} from '../../../libs/GlobalState';

export function AssignmentsTab(props: {user: IUser}) {
  const global = useContext(GlobalStateContext);

  const [classXs, setClassXs] = useState<IClassX[]>([]);
  const [assignments, setAssignments] = useState<IAssignment[] | null>(null);
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
  const [longDescrHtml, setLongDescrHtml] = useState<Value>('');

  // Used to track the project definition.
  const [projectDefinitions, setProjectDefinitions] = useState<
    IProjectDefinition[] | null
  >(null);
  const [projectDefinition, setProjectDefinition] =
    useState<IProjectDefinition | null>(null);
  const [categories, setCategories] = useState<IProjectInputValue[]>([]);

  const classXSorter = (a: IClassX, b: IClassX) =>
    (a?.name ?? '').localeCompare(b?.name ?? '');
  const assignmentSorter = (a: IAssignment, b: IAssignment) =>
    classXSorter(a?.classX ?? {}, b?.classX ?? {}) ||
    (a?.name ?? '').localeCompare(b?.name ?? '');
  const projectDefinitionSorter = (
    a: IProjectDefinition,
    b: IProjectDefinition
  ) =>
    (b.template === true ? 1 : -1) - (a.template === true ? 1 : -1) ||
    (a.name ?? '').localeCompare(b.name ?? '');

  function setAssignment(newAssignment: IAssignment | null) {
    if (assignment != null) {
      saveAssignment();
    }

    fnSetAssignment(newAssignment);
    setClassX(newAssignment?.classX ?? null);
    setName(newAssignment?.name ?? '');
    setShortDescr(newAssignment?.shortDescr ?? '');
    setLongDescrHtml(newAssignment?.longDescrHtml ?? '');
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
      assignment.longDescrHtml = String(longDescrHtml);

      saveAssignmentTimeout.current = setTimeout(() => {
        saveAssignment();
      }, 1000);
    }
  }, [classX, name, shortDescr, longDescrHtml]);

  // Changes that affect labels in the select assignment dropdown.
  useEffect(() => {
    setAssignments(assignments != null ? [...assignments] : []);
  }, [classX, name, shortDescr]);

  function saveAssignment() {
    clearTimeout(saveAssignmentTimeout.current);
    saveAssignmentTimeout.current = undefined;

    if (assignmentRef.current != null) {
      createService(AssignmentManagementService, 'AssignmentManagementService')
        .saveAssignment({assignment: assignmentRef.current})
        .catch(global.setError);
      setSaveStatus('Saved');
    }
  }

  useEffect(() => {
    if (assignment == null) {
      setProjectDefinitions(null);
      setProjectDefinition(null);
      return;
    }
    createService(ProjectManagementService, 'ProjectManagementService')
      .getAssignmentProjectDefinitions({assignmentId: assignment.id!})
      .then(response => {
        setProjectDefinitions(response.definitions);
        setProjectDefinition(null);
        for (const definition of response.definitions) {
          if (definition.selected) {
            setProjectDefinition(definition);
            break;
          }
        }
      })
      .catch(global.setError);
  }, [assignment]);

  useEffect(() => {
    if (projectDefinition?.inputs == null) {
      setCategories([]);
      return;
    }
    setCategories(projectDefinition.inputs);
  }, [projectDefinition]);

  useEffect(() => {
    createService(AssignmentManagementService, 'AssignmentManagementService')
      .getAssignments({
        teacherId: props.user.teacherId!,
      })
      .then(response => {
        setClassXs(response.classXs);
        setAssignments(response.assignments);
      })
      .catch(global.setError);
  }, []);

  function createNewAssignment(classXId: number) {
    createService(AssignmentManagementService, 'AssignmentManagementService')
      .createAssignment({classXId: classXId})
      .then(response => {
        setAssignments((assignments ?? []).concat([response.assignment!]));
        setAssignment(response.assignment!);
      })
      .catch(reason => global.setError({error: reason, reload: false}));
  }

  function deleteAssignment() {
    if (assignment == null) {
      return;
    }

    createService(AssignmentManagementService, 'AssignmentManagementService')
      .deleteAssignment({assignmentId: assignment!.id!})
      .then(() => {
        setAssignment(null);
        setAssignments(
          (assignments ?? []).filter(a => a.id !== assignment?.id)
        );
      })
      .catch(reason => global.setError({error: reason, reload: false}));
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
            isOptionEqualToValue={(option, value) => option?.id === value?.id}
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
            loadingText="Loading Classes..."
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
            value={longDescrHtml}
            onChange={setLongDescrHtml}
            onBlur={saveAssignment}
            readOnly={assignment == null}
          />
        </Grid>
        <Grid item xs={12} className="section-heading">
          <div className="section-title">
            Edit Ikigai Project Definition:{' '}
            <span
              style={{color: 'red', fontWeight: 'normal', fontStyle: 'italic'}}
            >
              TODO: Changes here are NOT saved yet.
            </span>
          </div>
        </Grid>
        <Grid item xs={5}>
          <Autocomplete
            id="projectDefinition"
            value={projectDefinition}
            autoHighlight
            options={(projectDefinitions ?? []).sort(projectDefinitionSorter)}
            onChange={(e, value) => setProjectDefinition(value ?? null)}
            getOptionLabel={projectDefinition =>
              projectDefinition?.name ?? '(Unnamed Ikigai Configuration)'
            }
            isOptionEqualToValue={(option, value) => option.id === value.id}
            groupBy={projectDefinition =>
              projectDefinition?.template === true
                ? 'Templates'
                : 'My Ikigai Configurations'
            }
            disabled={(projectDefinitions ?? []).length === 0}
            size="small"
            fullWidth={true}
            renderOption={(props, option) => {
              return (
                <li {...props} key={option.id}>
                  {option?.name ?? '(Unnamed Ikigai Configuration)'}
                </li>
              );
            }}
            renderInput={params => (
              <TextField {...params} label="Select Ikigai Configuration" />
            )}
            loading={projectDefinitions == null}
            loadingText="Select Assignment"
          />
        </Grid>
        <Grid item xs={7}>
          <Grid container rowGap={2} columns={{xs: 6}} width="100%">
            <Grid item xs={7}>
              <TextField
                required
                label="Name"
                value={projectDefinition?.name ?? ''}
                onChange={e => setName(e.target.value)}
                disabled={true}
                size="small"
                fullWidth={true}
              />
            </Grid>
            <Grid item xs={6}>
              {categories.length > 0 ? (
                <ol>
                  {categories.map(category => (
                    <li key={category.category?.id}>
                      {category.category?.name}
                    </li>
                  ))}
                </ol>
              ) : (
                <p style={{paddingLeft: '2.5em'}}>No Categories</p>
              )}
            </Grid>
          </Grid>
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
