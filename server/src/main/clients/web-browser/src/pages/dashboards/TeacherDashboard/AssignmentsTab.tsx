import './TeacherDashboard.scss';

import {Autocomplete, Grid, TextField} from '@mui/material';
import {useEffect, useState} from 'react';
import {pl_types} from '../../../generated/protobuf-js';
import IClassX = pl_types.IClassX;
import IAssignment = pl_types.IAssignment;

export function AssignmentsTab() {
  const [classXs, setClassXs] = useState<IClassX[] | undefined>();
  const [assignments, setAssignments] = useState<IAssignment[] | undefined>();

  const [classX, setClassX] = useState<IClassX | null>(null);
  const [assignment, setAssignment] = useState<IAssignment | null>(null);

  useEffect(() => {
    setTimeout(() => {
      const sortedAssignments = [
        {id: 3, name: 'Assignment 3', classX: {id: 2, name: 'Class B'}},
        {id: 2, name: 'Assignment 2', classX: {id: 1, name: 'Class A'}},
        {id: 4, name: 'Assignment 4', classX: {id: 3, name: 'Class C'}},
        {id: 1, name: 'Assignment 1', classX: {id: 1, name: 'Class A'}},
      ].sort(
        (a, b) =>
          a.classX!.name!.localeCompare(b.classX!.name!) ||
          a.name!.localeCompare(b.name!)
      );

      const sortedUniqueClassXs: IClassX[] = [];
      sortedAssignments.forEach(assignment => {
        if (
          sortedUniqueClassXs[sortedUniqueClassXs.length - 1]?.id !==
          assignment.classX!.id!
        ) {
          sortedUniqueClassXs.push(assignment.classX!);
        }
      });

      setClassXs(sortedUniqueClassXs);
      setAssignments(sortedAssignments);
    }, 2000);
  });

  return (
    <>
      <Grid container paddingY={2} spacing={2} columns={{xs: 6, md: 12}}>
        <Grid item xs={6}>
          <Autocomplete
            id="class"
            value={classX}
            autoHighlight
            options={classXs ?? []}
            onChange={(e, value) => {
              setClassX(value);
              setAssignment(null);
            }}
            getOptionLabel={classX => classX.name!}
            isOptionEqualToValue={(option, value) => option.id === value.id}
            disabled={assignments == null}
            size="small"
            fullWidth={true}
            renderInput={params => (
              <TextField
                {...params}
                label={
                  assignments == null ? 'Loading Classes...' : 'Select Class'
                }
              />
            )}
          />
        </Grid>
        <Grid item xs={6}>
          <Autocomplete
            id="assignment"
            value={assignment}
            autoHighlight
            options={assignments ?? []}
            onChange={(e, value) => {
              setClassX(value?.classX ?? null);
              setAssignment(value);
            }}
            isOptionEqualToValue={(option, value) => option.id === value.id}
            filterOptions={options =>
              classX != null
                ? options.filter(value => value.classX!.id! === classX.id!)
                : options
            }
            groupBy={assignment => assignment.classX!.name!}
            getOptionLabel={assignment => assignment.name!}
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
      </Grid>
    </>
  );
}
