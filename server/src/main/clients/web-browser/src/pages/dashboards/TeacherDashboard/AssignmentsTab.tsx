import './TeacherDashboard.scss';
import 'react-quill/dist/quill.snow.css';

import ReactQuill, {Value} from 'react-quill';
import {
  Autocomplete,
  Button,
  Checkbox,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Grid,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  TextField,
} from '@mui/material';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {
  assignment_management,
  class_x_management_service,
  pl_types,
  project_management,
} from 'pl-pb';
import {createService} from '../../../libs/protos';
import {useContext, useEffect, useRef, useState} from 'react';
import {useFormFields} from '../../../libs/form_utils/forms';
import {
  ASSIGNMENT_SORTER,
  CLASS_X_SORTER,
  KNOWLEDGE_AND_SKILL_SORTER,
  PROJECT_DEFINITION_SORTER,
} from '../../../libs/sorters';
import {addClassName} from '../../../libs/tags';
import IAssignment = pl_types.IAssignment;
import IClassX = pl_types.IClassX;
import IProjectDefinition = pl_types.IProjectDefinition;
import IProjectInputValue = pl_types.IProjectInputValue;
import IUserX = pl_types.IUserX;
import IKnowledgeAndSkill = pl_types.IKnowledgeAndSkill;
import ClassXManagementService = class_x_management_service.ClassXManagementService;
import ProjectManagementService = project_management.ProjectManagementService;
import Type = pl_types.KnowledgeAndSkill.Type;
import AssignmentManagementService = assignment_management.AssignmentManagementService;

export function AssignmentsTab(props: {userX: IUserX | undefined}) {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be a teacher to edit assignments.',
    userX => userX.isAdminX || userX.isTeacher
  );

  const [classXs, setClassXs] = useState<readonly IClassX[]>([]);
  const [assignments, setAssignments] = useState<readonly IAssignment[] | null>(
    null
  );
  const [sortedEks, setSortedEks] = useState<readonly IKnowledgeAndSkill[]>([]);
  const [saveStatus, setSaveStatus] = useState<'Saved' | 'Saving...'>('Saved');
  const [showDeleteAssignment, setShowDeleteAssignment] = useState(false);
  const [showCreateAssignment, setShowCreateAssignment] = useState(false);

  // Used to track the assignment to save asynchronously.
  const saveAssignmentTimeout = useRef<NodeJS.Timeout>();
  const assignmentRef = useRef<IAssignment>();
  const [assignment, fnSetAssignment] = useState<IAssignment | null>(null);
  const [classXEksIds, setClassXEksIds] = useState<Set<number>>(new Set());

  const assignmentFormField = useFormFields({
    disabled: assignment == null,
  });
  const [sortedAssignmentEks, setSortedAssignmentEks] = useState<
    readonly IKnowledgeAndSkill[]
  >([]);
  const [classX, setClassX] = useState<IClassX | null>(null);
  const [name, setName] = useState('');
  const [shortDescr, setShortDescr] = useState('');
  const [longDescrHtml, setLongDescrHtml] = useState<Value>('');
  const assignmentEks = assignmentFormField.useAutocompleteFormField<
    IKnowledgeAndSkill,
    true
  >('knowledgeAndSkills');

  // Used to track the project definition.
  const [projectDefinitions, setProjectDefinitions] = useState<
    readonly IProjectDefinition[] | null
  >(null);
  const [projectDefinition, setProjectDefinition] =
    useState<IProjectDefinition | null>(null);
  const [categories, setCategories] = useState<readonly IProjectInputValue[]>(
    []
  );

  useEffect(() => {
    createService(ProjectManagementService, 'ProjectManagementService')
      .getKnowledgeAndSkills({types: [Type.EKS]})
      .then(response =>
        setSortedEks(
          response.knowledgeAndSkills.sort(KNOWLEDGE_AND_SKILL_SORTER)
        )
      )
      .catch(global.setError);
  }, []);

  useEffect(() => {
    createService(ClassXManagementService, 'ClassXManagementService')
      .getClassXs({
        teacherIds: [props.userX?.teacherId ?? 0],
        includeAssignments: true,
        includeKnowledgeAndSkills: true,
      })
      .then(response => {
        setClassXs(response.classXs);

        const newAssignments: IAssignment[] = [];
        for (const classX of response.classXs ?? {}) {
          for (const assignment of classX.assignments ?? []) {
            newAssignments.push(assignment);
          }
        }
        setAssignments(newAssignments);
      })
      .catch(global.setError);
  }, []);

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
      assignment.knowledgeAndSkills = (assignmentEks.getValue() ?? []).slice();

      saveAssignmentTimeout.current = setTimeout(() => {
        saveAssignment();
      }, 1000);
    }
  }, [classX, name, shortDescr, assignmentEks.getValue(), longDescrHtml]);

  // Changes that affect labels in the select assignment dropdown.
  useEffect(() => {
    setAssignments(assignments != null ? [...assignments] : []);
  }, [classX, name, shortDescr]);

  useEffect(() => {
    if (assignment == null) {
      setProjectDefinitions(null);
      setProjectDefinition(null);
      setSortedAssignmentEks([]);
      return;
    }
    const newClassXEksIds = new Set(
      (assignment?.classX?.knowledgeAndSkills ?? []).map(eks => eks.id ?? 0)
    );
    setClassXEksIds(newClassXEksIds);
    setSortedAssignmentEks(
      sortedEks
        .slice()
        .sort(
          (a, b) =>
            (newClassXEksIds.has(a.id ?? 0) ? 0 : 1) -
              (newClassXEksIds.has(b.id ?? 0) ? 0 : 1) ||
            KNOWLEDGE_AND_SKILL_SORTER(a, b)
        )
    );
    createService(AssignmentManagementService, 'AssignmentManagementService')
      .getAssignments({
        assignmentIds: [assignment.id!],
        includeProjectDefinitions: true,
      })
      .then(response => {
        const definitions = response.assignments.flatMap(
          a => a.projectDefinitions ?? []
        );
        setProjectDefinitions(definitions);
        setProjectDefinition(null);
        for (const definition of definitions) {
          if (definition.selected) {
            setProjectDefinition(definition);
            break;
          }
        }
      })
      .catch(global.setError);
  }, [assignment, sortedEks]);

  useEffect(() => {
    if (projectDefinition?.inputs == null) {
      setCategories([]);
      return;
    }
    setCategories(projectDefinition.inputs);
  }, [projectDefinition]);

  function setAssignment(newAssignment: IAssignment | null) {
    if (assignment != null) {
      saveAssignment();
    }

    fnSetAssignment(newAssignment);
    setClassX(newAssignment?.classX ?? null);
    setName(newAssignment?.name ?? '');
    setShortDescr(newAssignment?.shortDescr ?? '');
    setLongDescrHtml(newAssignment?.longDescrHtml ?? '');
    assignmentEks.setValue(newAssignment?.knowledgeAndSkills ?? []);
  }

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

  if (!userX) {
    return <></>;
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
            options={(assignments?.slice() ?? []).sort(ASSIGNMENT_SORTER)}
            onChange={(ignore, value) => setAssignment(value)}
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
            options={classXs.slice().sort(CLASS_X_SORTER)}
            onChange={(ignore, value) => setClassX(value)}
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
          <Autocomplete
            {...assignmentEks.autocompleteParams()}
            multiple
            autoHighlight
            disableCloseOnSelect
            options={sortedAssignmentEks}
            isOptionEqualToValue={(option, value) => option.id === value.id}
            renderInput={params => (
              <TextField
                label="Select knowledge and skills"
                autoComplete="off"
                {...assignmentEks.textFieldParams(params)}
              />
            )}
            groupBy={eks =>
              classXEksIds.has(eks.id ?? 0)
                ? 'Class Knowledge and Skills'
                : 'Other Knowledge and Skills'
            }
            renderOption={(props, option, {selected}) => (
              <li {...props} key={option.id}>
                <Checkbox style={{marginRight: 8}} checked={selected} />
                {option.name}
              </li>
            )}
            getOptionLabel={option =>
              option.name + ': ' + (option?.shortDescr ?? 'undefined')
            }
            renderTags={(tagValue, getTagProps) =>
              tagValue.map((option, index) => (
                <Chip
                  label={option.name}
                  size="small"
                  variant="outlined"
                  {...addClassName(
                    getTagProps({index}),
                    'teacher-edit-classes-chips'
                  )}
                />
              ))
            }
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
            options={(projectDefinitions?.slice() ?? []).sort(
              PROJECT_DEFINITION_SORTER
            )}
            onChange={(ignore, value) => setProjectDefinition(value ?? null)}
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
