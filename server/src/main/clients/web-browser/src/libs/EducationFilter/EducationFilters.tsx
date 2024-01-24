import '../global.scss';

import {DeepReadOnly} from '../misc';
import {FormField} from '../form_utils/forms';
import {pl_types, user_x_management} from 'pl-pb';
import {DynamicSchoolAutocomplete} from '../common_fields/DynamicSchoolAutocomplete';
import {CSSProperties, useContext} from 'react';
import {GlobalStateContext} from '../GlobalState';
import {DynamicClassXAutocomplete} from '../common_fields/DynamicClassXAutocomplete';
import {DynamicAssignmentAutocomplete} from '../common_fields/DynamicAssignmentAutocomplete';
import {DynamicUserXAutocomplete} from '../common_fields/DynamicUserXAutocomplete';
import {Grid} from '@mui/material';
import {DynamicDistrictAutocomplete} from '../common_fields/DynamicDistrictAutocomplete';
import IAssignment = pl_types.IAssignment;
import ISchool = pl_types.ISchool;
import IClassX = pl_types.IClassX;
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import IDistrict = pl_types.IDistrict;

// TODO: cache and share results so that multiple instances are not hitting the database over and over again.
export function EducationFilters(
  props: DeepReadOnly<{
    label: string;
    districtField?: FormField<DeepReadOnly<IDistrict>>;
    schoolsField?: FormField<DeepReadOnly<ISchool>, true>;
    classXsField?: FormField<DeepReadOnly<IClassX>, true>;
    assignmentsField?: FormField<DeepReadOnly<IAssignment>, true>;
    userXsField?: FormField<DeepReadOnly<IFullUserXDetails>, true>;

    highlightLabel?: string;
    highlightUserXsField?: FormField<DeepReadOnly<IFullUserXDetails>, true>;

    renderUserXsStyle?: (
      userX: DeepReadOnly<IFullUserXDetails>
    ) => CSSProperties;
  }>
) {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be logged in to use EducationFilters.'
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <div className="global-flex-column">
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <div className="global-section-heading" style={{paddingTop: 0}}>
              <div className="global-section-title">{props.label}</div>
            </div>
          </Grid>
          {props.districtField && (
            <Grid item xs={12}>
              <DynamicDistrictAutocomplete
                label={'Filter by District'}
                districtField={props.districtField}
              />
            </Grid>
          )}
          {props.schoolsField && (
            <Grid item xs={12}>
              <DynamicSchoolAutocomplete
                label={'Filter by School'}
                baseRequest={{
                  districtId:
                    props.districtField?.getValue?.()?.id ?? userX.districtId,
                }}
                schoolField={props.schoolsField}
              />
            </Grid>
          )}
          {props.classXsField && (
            <Grid item xs={12}>
              <DynamicClassXAutocomplete
                label={'Filter by Class'}
                baseRequest={{
                  schoolIds: props.schoolsField
                    ?.getValue?.()
                    ?.map?.(s => s.id ?? 0),
                  teacherIds: userX.isTeacher
                    ? [userX.teacherId ?? 0]
                    : undefined,
                  studentIds: userX.isStudent
                    ? [userX.studentId ?? 0]
                    : undefined,
                  includeSchool: true,
                }}
                classXField={props.classXsField}
              />
            </Grid>
          )}
          {props.assignmentsField && (
            <Grid item xs={12}>
              <DynamicAssignmentAutocomplete
                label={'Filter by Assignment'}
                baseRequest={{
                  teacherId: userX.teacherId,
                  studentId: userX.studentId,
                  schoolIds: props.schoolsField
                    ?.getValue?.()
                    ?.map?.(s => s.id ?? 0),
                  classXIds: props.classXsField
                    ?.getValue?.()
                    ?.map?.(c => c.id ?? 0),
                }}
                assignmentField={props.assignmentsField}
              />
            </Grid>
          )}
          {props.userXsField && (
            <Grid item xs={12}>
              <DynamicUserXAutocomplete
                label={'Filter by User'}
                baseRequest={{
                  inDistrictIds: [userX.districtId ?? 0],
                  inSchoolIds: props.schoolsField
                    ?.getValue?.()
                    ?.map?.(s => s.id ?? 0),
                  inClassXIds: props.classXsField
                    ?.getValue?.()
                    ?.map?.(s => s.id ?? 0),
                }}
                userXField={props.userXsField}
                renderTagStyle={props.renderUserXsStyle}
              />
            </Grid>
          )}
          {props.highlightLabel && (
            <>
              <Grid item xs={12}>
                <div className="global-section-heading">
                  <div className="global-section-title">
                    {props.highlightLabel}
                  </div>
                </div>
              </Grid>
              {props.highlightUserXsField && (
                <Grid item xs={12}>
                  <DynamicUserXAutocomplete
                    label={'Highlight by User'}
                    baseRequest={{
                      inDistrictIds: [userX.districtId ?? 0],
                      inSchoolIds: props.schoolsField
                        ?.getValue?.()
                        ?.map?.(s => s.id ?? 0),
                      inClassXIds: props.classXsField
                        ?.getValue?.()
                        ?.map?.(s => s.id ?? 0),
                    }}
                    userXField={props.highlightUserXsField}
                    renderTagStyle={props.renderUserXsStyle}
                  />
                </Grid>
              )}
            </>
          )}
        </Grid>
      </div>
    </>
  );
}
