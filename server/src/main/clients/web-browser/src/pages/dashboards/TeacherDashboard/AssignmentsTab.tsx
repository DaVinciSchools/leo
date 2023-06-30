import './TeacherDashboard.scss';
import 'react-quill/dist/quill.snow.css';

import {Autocomplete, Grid, TextField} from '@mui/material';
import {useEffect, useRef, useState} from 'react';
import {pl_types} from '../../../generated/protobuf-js';
import IClassX = pl_types.IClassX;
import IAssignment = pl_types.IAssignment;
import ReactQuill, {Value} from 'react-quill';

export function AssignmentsTab() {
  const [classXs, setClassXs] = useState<IClassX[]>([]);
  const [assignments, setAssignments] = useState<IAssignment[] | undefined>();

  const saveAssignmentTimeout = useRef<NodeJS.Timeout>();
  // Used to track the assignment to save asynchronously.
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
      assignmentRef.current = assignment ?? undefined;
      assignment.classX = classX;
      assignment.name = name;
      assignment.shortDescr = shortDescr;
      assignment.longDescr = String(longDescr);

      saveAssignmentTimeout.current = setTimeout(() => {
        saveAssignment();
      }, 2000);
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
      console.log(
        'Saving assignment: ' + JSON.stringify(assignmentRef.current)
      );
    }
  }

  useEffect(() => {
    setTimeout(() => {
      setClassXs(
        [
          {id: 2, name: 'Class B'},
          {id: 1, name: 'Class A'},
          {id: 3, name: 'Class C'},
        ].sort(classXSorter)
      );
      setAssignments(
        [
          {id: 3, name: 'Assignment 3', classX: {id: 2, name: 'Class B'}},
          {id: 2, name: 'Assignment 2', classX: {id: 1, name: 'Class A'}},
          {id: 4, name: 'Assignment 4', classX: {id: 3, name: 'Class C'}},
          {id: 1, name: 'Assignment 1', classX: {id: 1, name: 'Class A'}},
        ].sort(assignmentSorter)
      );
    }, 2000);
  }, []);

  return (
    <>
      <Grid container paddingY={2} spacing={2} columns={{xs: 6, md: 12}}>
        <Grid item xs={12}>
          <div className="section-label" style={{marginTop: '0'}}>
            Select Assignment:
          </div>
        </Grid>
        <Grid item xs={12}>
          <Autocomplete
            id="assignment"
            value={assignment}
            autoHighlight
            options={(assignments ?? []).sort(assignmentSorter)}
            onChange={(e, value) => setAssignment(value)}
            isOptionEqualToValue={(option, value) => option.id === value.id}
            groupBy={assignment => assignment?.classX?.name ?? '[Unassigned]'}
            getOptionLabel={assignment =>
              assignment.name! +
              (assignment.shortDescr != null && assignment.shortDescr !== ''
                ? '  (' + assignment.shortDescr + ')'
                : '')
            }
            disabled={assignments == null}
            size="small"
            fullWidth={true}
            renderInput={params => (
              <TextField
                {...params}
                label={
                  assignments == null
                    ? 'Loading Assignments...'
                    : 'Select Assignment'
                }
              />
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <div className="section-label">Edit Assignment:</div>
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
            disabled={assignment == null}
            size="small"
            fullWidth={true}
            renderInput={params => (
              <TextField
                {...params}
                label={assignments == null ? 'Loading Classes...' : 'Class'}
              />
            )}
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
        <Grid item xs={8}>
          <ReactQuill
            theme="snow"
            preserveWhitespace={true}
            value={longDescr}
            onChange={setLongDescr}
            onBlur={saveAssignment}
            readOnly={assignment == null}
          />
        </Grid>
        <Grid item xs={4}>
          Ikigai Project Definition Editor
        </Grid>
      </Grid>
    </>
  );
}
