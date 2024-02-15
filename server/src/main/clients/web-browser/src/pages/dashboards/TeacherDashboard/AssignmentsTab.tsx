import './TeacherDashboard.scss';
import {useContext, useEffect, useState} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalStateProvider/GlobalStateProvider';
import {DeepReadOnly} from '../../../libs/misc';
import {EducationFilters} from '../../../libs/EducationFilter/EducationFilters';
import {Grid} from '@mui/material';
import {assignment_management, pl_types} from 'pl-pb';
import {createService} from '../../../libs/protos';
import {FormField} from '../../../libs/form_utils/forms';
import IAssignment = pl_types.IAssignment;
import AssignmentManagementService = assignment_management.AssignmentManagementService;

export function AssignmentsTab(
  props: DeepReadOnly<{
    educationFilters: EducationFilters;
    assignmentsFilter?: FormField<DeepReadOnly<IAssignment>, true>;
  }>
) {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserXLogin(
    'You must be a teacher to view this dashboard.',
    userX => userX.isAdminX || userX.isTeacher
  );

  const [assignment, setAssignment] = useState<DeepReadOnly<IAssignment>>();

  useEffect(() => {
    const assignments = props.assignmentsFilter?.getValue?.() ?? [];
    if (assignments.length === 1) {
      createService(AssignmentManagementService, 'AssignmentManagementService')
        .getAssignments({
          assignmentIds: assignments.map(a => a.id ?? 0),
        })
        .then(response => setAssignment(response.assignments?.[0]));
    } else {
      setAssignment(undefined);
    }
  }, [props.assignmentsFilter?.getValue?.()]);

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <Grid container spacing={2} padding={2}>
        <Grid item xs={3}>
          <EducationFilters
            label="Filter Posts"
            highlightLabel="Highlight Posts"
            educationFilters={props.educationFilters}
          />
        </Grid>
        <Grid item xs={9}>
          {!assignment ? (
            <div
              className="global-flex-column"
              style={{
                alignItems: 'center',
                justifyContent: 'center',
                height: '100%',
              }}
            >
              Please select a single assignment on the left.
            </div>
          ) : (
            <>
              <div>{JSON.stringify(assignment)}</div>
            </>
          )}
        </Grid>
      </Grid>
    </>
  );
}
