import '../global.scss';

import {deepReadOnly, DeepReadOnly, getUniqueHues} from '../misc';
import {FormField, FormFields} from '../form_utils/forms';
import {pl_types, user_x_management} from 'pl-pb';
import {DynamicSchoolAutocomplete} from '../common_fields/DynamicSchoolAutocomplete';
import {CSSProperties, useContext, useState} from 'react';
import {GlobalStateContext} from '../GlobalState';
import {DynamicClassXAutocomplete} from '../common_fields/DynamicClassXAutocomplete';
import {DynamicAssignmentAutocomplete} from '../common_fields/DynamicAssignmentAutocomplete';
import {DynamicUserXAutocomplete} from '../common_fields/DynamicUserXAutocomplete';
import {Grid} from '@mui/material';
import {DynamicDistrictAutocomplete} from '../common_fields/DynamicDistrictAutocomplete';
import {FULL_USER_X_SORTER} from '../sorters';
import IAssignment = pl_types.IAssignment;
import ISchool = pl_types.ISchool;
import IClassX = pl_types.IClassX;
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import IDistrict = pl_types.IDistrict;
import IUserX = pl_types.IUserX;

export type EducationFilters = {
  districtFilter?: FormField<DeepReadOnly<IDistrict>>;
  schoolsFilter?: FormField<DeepReadOnly<ISchool>, true>;
  classXsFilter?: FormField<DeepReadOnly<IClassX>, true>;
  assignmentsFilter?: FormField<DeepReadOnly<IAssignment>, true>;
  userXsFilter?: FormField<DeepReadOnly<IFullUserXDetails>, true>;

  highlightUserXsField?: FormField<DeepReadOnly<IFullUserXDetails>, true>;
  getUserXHighlightStyle?: (
    userX?: DeepReadOnly<IUserX> | null | undefined
  ) => CSSProperties | undefined;
};

export function EducationFilters(
  props: DeepReadOnly<{
    label: string;
    highlightLabel?: string;
    educationFilters: EducationFilters;
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
          {props.educationFilters.districtFilter && (
            <Grid item xs={12}>
              <DynamicDistrictAutocomplete
                label={'Filter by District'}
                districtField={props.educationFilters.districtFilter}
              />
            </Grid>
          )}
          {props.educationFilters.schoolsFilter && (
            <Grid item xs={12}>
              <DynamicSchoolAutocomplete
                label={'Filter by School'}
                baseRequest={{
                  districtId:
                    props.educationFilters.districtFilter?.getValue?.()?.id ??
                    userX.districtId,
                }}
                schoolField={props.educationFilters.schoolsFilter}
              />
            </Grid>
          )}
          {props.educationFilters.classXsFilter && (
            <Grid item xs={12}>
              <DynamicClassXAutocomplete
                label={'Filter by Class'}
                baseRequest={{
                  schoolIds: props.educationFilters.schoolsFilter
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
                classXField={props.educationFilters.classXsFilter}
              />
            </Grid>
          )}
          {props.educationFilters.assignmentsFilter && (
            <Grid item xs={12}>
              <DynamicAssignmentAutocomplete
                label={'Filter by Assignment'}
                baseRequest={{
                  teacherId: userX.teacherId,
                  studentId: userX.studentId,
                  schoolIds: props.educationFilters.schoolsFilter
                    ?.getValue?.()
                    ?.map?.(s => s.id ?? 0),
                  classXIds: props.educationFilters.classXsFilter
                    ?.getValue?.()
                    ?.map?.(c => c.id ?? 0),
                }}
                assignmentField={props.educationFilters.assignmentsFilter}
              />
            </Grid>
          )}
          {props.educationFilters.userXsFilter && (
            <Grid item xs={12}>
              <DynamicUserXAutocomplete
                label={'Filter by User'}
                baseRequest={{
                  inDistrictIds: [userX.districtId ?? 0],
                  inSchoolIds: props.educationFilters.schoolsFilter
                    ?.getValue?.()
                    ?.map?.(s => s.id ?? 0),
                  inClassXIds: props.educationFilters.classXsFilter
                    ?.getValue?.()
                    ?.map?.(s => s.id ?? 0),
                }}
                userXField={props.educationFilters.userXsFilter}
                renderTagStyle={fullUserX =>
                  props.educationFilters.getUserXHighlightStyle?.(
                    fullUserX?.userX
                  )
                }
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
              {props.educationFilters.highlightUserXsField && (
                <Grid item xs={12}>
                  <DynamicUserXAutocomplete
                    label={'Highlight by User'}
                    baseRequest={{
                      inDistrictIds: [userX.districtId ?? 0],
                      inSchoolIds: props.educationFilters.schoolsFilter
                        ?.getValue?.()
                        ?.map?.(s => s.id ?? 0),
                      inClassXIds: props.educationFilters.classXsFilter
                        ?.getValue?.()
                        ?.map?.(s => s.id ?? 0),
                    }}
                    userXField={props.educationFilters.highlightUserXsField}
                    renderTagStyle={fullUserX =>
                      props.educationFilters.getUserXHighlightStyle?.(
                        fullUserX?.userX
                      )
                    }
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

export function defaultEducationFilters(
  userX: IUserX | null | undefined,
  filterForm: FormFields
) {
  const districtFilter =
    filterForm.useSingleAutocompleteFormField<DeepReadOnly<IDistrict>>(
      'districtFilter'
    );
  const schoolFilter =
    filterForm.useMultipleAutocompleteFormField<DeepReadOnly<ISchool>>(
      'schoolFilter'
    );
  const classXFilter =
    filterForm.useMultipleAutocompleteFormField<DeepReadOnly<IClassX>>(
      'classXFilter'
    );
  const assignmentFilter =
    filterForm.useMultipleAutocompleteFormField<DeepReadOnly<IAssignment>>(
      'assignmentFilter'
    );
  const userXFilter =
    filterForm.useMultipleAutocompleteFormField<
      DeepReadOnly<IFullUserXDetails>
    >('userXFilter');

  const [highlightedUserXHues, internal_setHighlightedUserXHues] = useState(
    deepReadOnly(new Map<number, number>())
  );
  const highlightedUserXs = filterForm.useMultipleAutocompleteFormField<
    DeepReadOnly<IFullUserXDetails>
  >('highlightUserXs', {
    onChange: formField => {
      internal_setHighlightedUserXHues(
        getUniqueHues(
          highlightedUserXHues,
          formField
            .getValue()
            .slice()
            .sort(FULL_USER_X_SORTER)
            .map(userX => userX.userX?.id ?? 0)
        )
      );
    },
  });

  return deepReadOnly<EducationFilters>({
    districtFilter: userX?.isAdminX ? districtFilter : undefined,
    schoolsFilter: userX?.isAdminX ? schoolFilter : undefined,
    classXsFilter: classXFilter,
    assignmentsFilter: assignmentFilter,
    userXsFilter: userXFilter,

    highlightUserXsField: highlightedUserXs,

    getUserXHighlightStyle: userX =>
      highlightedUserXHues.has(userX?.id ?? 0)
        ? {
            backgroundColor: `hsl(${
              highlightedUserXHues.get(userX?.id ?? 0) ?? 0
            }, 100%, 80%)`,
          }
        : undefined,
  });
}
