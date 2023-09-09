import {FormFields} from '../form_utils/forms';
import {Checkbox, Grid, TextField} from '@mui/material';
import '../global.scss';
import {
  class_x_management_service,
  district_management,
  pl_types,
  school_management,
  user_x_management,
} from '../../generated/protobuf-js';
import IClassX = pl_types.IClassX;
import IUserX = pl_types.IUserX;
import ISchool = pl_types.ISchool;
import IDistrict = pl_types.IDistrict;
import {useContext, useEffect, useState} from 'react';
import DistrictManagementService = district_management.DistrictManagementService;
import {createService} from '../protos';
import {CLASS_X_SORTER, DISTRICT_SORTER, SCHOOL_SORTER} from '../sorters';
import {GlobalStateContext} from '../GlobalState';
import SchoolManagementService = school_management.SchoolManagementService;
import ClassXManagementService = class_x_management_service.ClassXManagementService;
import {spread} from '../tags';
import {AccountCircle, Email, Lock} from '@mui/icons-material';
import {DistrictAutocomplete} from '../common_fields/DistrictAutocomplete';
import {MultiSchoolAutocomplete} from '../common_fields/MultiSchoolAutocomplete';
import {MultiClassXAutocomplete} from '../common_fields/MultiClassXAutocomplete';
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

  const [sortedDistricts, setSortedDistricts] = useState<readonly IDistrict[]>(
    []
  );
  const [sortedSchools, setSortedSchools] = useState<readonly ISchool[]>([]);
  const [sortedClassXs, setSortedClassXs] = useState<readonly IClassX[]>([]);

  props.profileForm.useNumberFormField('userXId');

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
      disabled: global?.userX?.isAdminX !== true,
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
      })
      .then(response => {
        setSortedClassXs(response.classXs.sort(CLASS_X_SORTER));
      })
      .catch(global.setError);
  }

  const profileDistrict =
    props.profileForm.useAutocompleteFormField<IDistrict | null>('district', {
      isAutocomplete: {},
      onChange: refreshClassXs,
      disabled: global?.userX?.isAdminX !== true,
    });
  const profileSchools = props.profileForm.useAutocompleteFormField<
    readonly ISchool[]
  >('schools', {
    isAutocomplete: {
      isMultiple: true,
    },
    onChange: refreshClassXs,
  });
  const profileClassXs = props.profileForm.useAutocompleteFormField<
    readonly IClassX[]
  >('classXs', {
    isAutocomplete: {
      isMultiple: true,
    },
  });
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
        .getUserXDetails({
          userXId: props.userXId,
          includeSchools: true,
          includeAllAvailableClassXs: true,
        })
        .then(response => {
          // TODO: Get rid of this hack.
          props.profileForm.setValuesObject({
            ...(response.userX?.userX ?? {}),
            ...(response.userX ?? {}),
          });

          profileSchools.setValue(response?.userX?.schools ?? []);
          profileClassXs.setValue(
            (response?.userX?.classXs ?? []).filter(e => e?.enrolled === true)
          );
        })
        .catch(global.setError);
    }
  }, [props.userXId]);

  if (!global.requireUserX(userX => userX?.isAuthenticated)) {
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
              {...profileFirstName.textFieldParams()}
            />
          </Grid>
          <Grid item {...spread({sm: 12, md: 6})}>
            <TextField
              required
              autoComplete="family-name"
              label="Last Name"
              InputLabelProps={{shrink: true}}
              {...profileLastName.textFieldParams()}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              autoComplete="email"
              label="Email Address"
              InputLabelProps={{shrink: true}}
              {...profileEmailAddress.textFieldParams()}
            />
          </Grid>
          <Grid item xs={12} className="global-section-heading">
            <div className="global-section-title">Password</div>
          </Grid>
          <Grid
            item
            xs={12}
            display={global?.userX?.isAdminX ? 'none' : undefined}
          >
            <TextField
              autoComplete="current-password"
              label="Current Password"
              InputLabelProps={{shrink: true}}
              {...profileCurrentPassword.textFieldParams()}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              autoComplete="new-password"
              label="New Password"
              InputLabelProps={{shrink: true}}
              {...profileNewPassword.textFieldParams()}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              autoComplete="new-password"
              label="Re-enter New Password"
              InputLabelProps={{shrink: true}}
              {...profileVerifyNewPassword.textFieldParams()}
            />
          </Grid>
          <Grid item xs={12} className="global-section-heading">
            <div className="global-section-title">Academic Information</div>
          </Grid>
          <Grid item xs={12}>
            <DistrictAutocomplete
              sortedDistricts={sortedDistricts}
              formField={profileDistrict}
              InputLabelProps={{shrink: true}}
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
                global?.userX?.isAdminX === true
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
            display={global?.userX?.isAdminX ? undefined : 'none'}
          >
            <div className="global-section-title">Roles</div>
          </Grid>
          <Grid
            item
            xs={12}
            display={global?.userX?.isAdminX ? undefined : 'none'}
          >
            <Checkbox {...profileIsAdminX.checkboxParams()} />
            Is Administrator
          </Grid>
          <Grid
            item
            xs={12}
            display={global?.userX?.isAdminX ? undefined : 'none'}
          >
            <Checkbox {...profileIsTeacher.checkboxParams()} />
            Is Teacher
          </Grid>
          <Grid
            item
            xs={12}
            display={global?.userX?.isAdminX ? undefined : 'none'}
          >
            <Checkbox {...profileIsStudent.checkboxParams()} />
            Is Student
          </Grid>
        </Grid>
      </div>
    </>
  );
}
