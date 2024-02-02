import {FormFields} from '../form_utils/forms';
import {Checkbox, Grid, TextField} from '@mui/material';
import '../global.scss';
import {
  class_x_management_service,
  district_management,
  pl_types,
  school_management,
  user_x_management,
} from 'pl-pb';
import {useContext, useEffect, useState} from 'react';
import {createService} from '../protos';
import {CLASS_X_SORTER, DISTRICT_SORTER, SCHOOL_SORTER} from '../sorters';
import {GlobalStateContext} from '../GlobalState';
import {spread} from '../tags';
import {AccountCircle, Email, Lock} from '@mui/icons-material';
import {DistrictAutocomplete} from '../common_fields/DistrictAutocomplete';
import {MultiSchoolAutocomplete} from '../common_fields/MultiSchoolAutocomplete';
import {MultiClassXAutocomplete} from '../common_fields/MultiClassXAutocomplete';
import {DeepReadOnly} from '../misc';
import IClassX = pl_types.IClassX;
import IUserX = pl_types.IUserX;
import ISchool = pl_types.ISchool;
import IDistrict = pl_types.IDistrict;
import DistrictManagementService = district_management.DistrictManagementService;
import SchoolManagementService = school_management.SchoolManagementService;
import ClassXManagementService = class_x_management_service.ClassXManagementService;
import UserXManagementService = user_x_management.UserXManagementService;

export interface EditorProfile extends IUserX {
  district: IDistrict | null;
  schools: readonly ISchool[];
  classXs: readonly IClassX[];

  districtStudentId: number;
  studentGrade: number;

  currentPassword: string;
  newPassword: string;
  verifyNewPassword: string;
}

export function ProfileEditor(props: {
  userXId: number | null;
  profileForm: FormFields;
  profileSaveStatus: string;
}) {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX('You must be logged in to edit a profile.');

  const [sortedDistricts, setSortedDistricts] = useState<
    DeepReadOnly<IDistrict[]>
  >([]);
  const [sortedSchools, setSortedSchools] = useState<readonly ISchool[]>([]);
  const [sortedClassXs, setSortedClassXs] = useState<readonly IClassX[]>([]);

  props.profileForm.useNumberFormField('id');

  const profileFirstName = props.profileForm.useStringFormField('firstName', {
    maxLength: 255,
    startIcon: <AccountCircle />,
  });
  const profileLastName = props.profileForm.useStringFormField('lastName', {
    maxLength: 255,
    startIcon: <AccountCircle />,
  });
  const profileEmailAddress = props.profileForm.useStringFormField(
    'emailAddress',
    {
      maxLength: 254,
      isEmail: true,
      startIcon: <Email />,
      disabled: userX?.isAdminX !== true,
    }
  );

  const profileCurrentPassword = props.profileForm.useStringFormField(
    'currentPassword',
    {
      startIcon: <Lock />,
      isPassword: {skipPasswordCheck: true},
    }
  );
  const profileNewPassword = props.profileForm.useStringFormField(
    'newPassword',
    {
      startIcon: <Lock />,
      isPassword: {},
    }
  );
  const profileVerifyNewPassword = props.profileForm.useStringFormField(
    'verifyPassword',
    {
      startIcon: <Lock />,
      isPassword: {},
    }
  );

  function refreshClassXs() {
    if (profileSchools.getValue()?.length === 0) {
      setSortedClassXs([]);
      return;
    }

    createService(ClassXManagementService, 'ClassXManagementService')
      .getClassXs({
        schoolIds: profileSchools.getValue()?.map(e => e.id ?? 0) ?? [],
        includeSchool: true,
      })
      .then(response => {
        setSortedClassXs(response.classXs.sort(CLASS_X_SORTER));
      })
      .catch(global.setError);
  }

  const profileDistrict = props.profileForm.useSingleAutocompleteFormField<
    DeepReadOnly<IDistrict>
  >('district', {
    onChange: refreshClassXs,
    disabled: userX?.isAdminX !== true,
  });
  const profileSchools = props.profileForm.useMultipleAutocompleteFormField<
    DeepReadOnly<ISchool>
  >('schools', {
    onChange: refreshClassXs,
  });
  const profileClassXs =
    props.profileForm.useMultipleAutocompleteFormField<DeepReadOnly<IClassX>>(
      'classXs'
    );
  const profileIsAdminX = props.profileForm.useBooleanFormField('isAdminX');
  const profileIsTeacher = props.profileForm.useBooleanFormField('isTeacher');
  const profileIsStudent = props.profileForm.useBooleanFormField('isStudent');

  useEffect(() => {
    createService(DistrictManagementService, 'DistrictManagementService')
      .getDistricts({})
      .then(response =>
        setSortedDistricts(response.districts.sort(DISTRICT_SORTER))
      )
      .catch(global.setError);
  }, []);

  useEffect(() => {
    if (profileDistrict.getValue()?.id == null) {
      setSortedSchools([]);
    } else {
      createService(SchoolManagementService, 'SchoolManagementService')
        .getSchools({districtId: profileDistrict.getValue()?.id})
        .then(response =>
          setSortedSchools(response.schools.sort(SCHOOL_SORTER))
        )
        .catch(global.setError);
    }
  }, [profileDistrict.getValue()?.id]);

  useEffect(() => {
    if (props.userXId == null) {
      props.profileForm.reset();
    } else {
      createService(UserXManagementService, 'UserXManagementService')
        .getUserXs({
          inUserXIds: [props.userXId],
          includeSchools: true,
          includeClassXs: true,
        })
        .then(response => {
          const userX = response.userXs?.[0];

          if (userX == null) {
            props.profileForm.reset();
            return;
          }

          // TODO: Get rid of this hack.
          props.profileForm.setValuesObject({
            ...(userX?.userX ?? {}),
            ...(userX ?? {}),
          });

          profileSchools.setValue(userX?.schools ?? []);
          profileClassXs.setValue(userX?.classXs ?? []);
        })
        .catch(global.setError);
    }
  }, [props.userXId]);

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <div className="global-flex-column">
        <Grid container spacing={2}>
          <Grid
            item
            xs={12}
            className="global-section-heading"
            style={{paddingTop: 0}}
          >
            <div className="global-section-title">Basic Information</div>
            <div className="global-section-links">
              {props.profileSaveStatus}
            </div>
          </Grid>
          <Grid item {...spread({sm: 12, md: 6})}>
            <TextField
              required
              autoComplete="given-name"
              label="First Name"
              InputLabelProps={{shrink: true}}
              {...profileFirstName.getTextFieldParams()}
            />
          </Grid>
          <Grid item {...spread({sm: 12, md: 6})}>
            <TextField
              required
              autoComplete="family-name"
              label="Last Name"
              InputLabelProps={{shrink: true}}
              {...profileLastName.getTextFieldParams()}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              autoComplete="email"
              label="Email Address"
              InputLabelProps={{shrink: true}}
              {...profileEmailAddress.getTextFieldParams()}
            />
          </Grid>
          <Grid item xs={12} className="global-section-heading">
            <div className="global-section-title">Password</div>
          </Grid>
          <Grid item xs={12} display={userX?.isAdminX ? 'none' : undefined}>
            <TextField
              autoComplete="current-password"
              label="Current Password"
              InputLabelProps={{shrink: true}}
              {...profileCurrentPassword.getTextFieldParams()}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              autoComplete="new-password"
              label="New Password"
              InputLabelProps={{shrink: true}}
              {...profileNewPassword.getTextFieldParams()}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              autoComplete="new-password"
              label="Re-enter New Password"
              InputLabelProps={{shrink: true}}
              {...profileVerifyNewPassword.getTextFieldParams()}
            />
          </Grid>
          <Grid item xs={12} className="global-section-heading">
            <div className="global-section-title">Academic Information</div>
          </Grid>
          <Grid item xs={12}>
            <DistrictAutocomplete
              sortedDistricts={sortedDistricts}
              formField={profileDistrict}
              placeholder={hasOptions =>
                hasOptions ? 'Select a district' : 'Districts are loading...'
              }
            />
          </Grid>
          <Grid item xs={12}>
            <MultiSchoolAutocomplete
              sortedSchools={sortedSchools}
              formField={profileSchools}
              InputLabelProps={{shrink: true}}
              placeholder={() =>
                userX?.isAdminX === true
                  ? 'Select a district to select from its available schools'
                  : 'Select schools from the district'
              }
            />
          </Grid>
          <Grid item xs={12}>
            <MultiClassXAutocomplete
              sortedClassXs={sortedClassXs}
              formField={profileClassXs}
              InputLabelProps={{shrink: true}}
              placeholder={() =>
                'Select schools to select from their available classes'
              }
            />
          </Grid>
          <Grid
            item
            xs={12}
            className="global-section-heading"
            display={userX?.isAdminX ? undefined : 'none'}
          >
            <div className="global-section-title">Roles</div>
          </Grid>
          <Grid item xs={12} display={userX?.isAdminX ? undefined : 'none'}>
            <Checkbox {...profileIsAdminX.getCheckboxParams()} />
            Is Administrator
          </Grid>
          <Grid item xs={12} display={userX?.isAdminX ? undefined : 'none'}>
            <Checkbox {...profileIsTeacher.getCheckboxParams()} />
            Is Teacher
          </Grid>
          <Grid item xs={12} display={userX?.isAdminX ? undefined : 'none'}>
            <Checkbox {...profileIsStudent.getCheckboxParams()} />
            Is Student
          </Grid>
        </Grid>
      </div>
    </>
  );
}
