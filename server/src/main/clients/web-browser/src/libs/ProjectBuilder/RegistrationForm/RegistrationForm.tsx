import {Autocomplete, Button, Grid, TextField} from '@mui/material';
import {AccountCircle, Comment, Email} from '@mui/icons-material';
import {FormEvent, useRef, useState} from 'react';
import {user_management} from '../../../generated/protobuf-js';
import IRegisterUserRequest = user_management.IRegisterUserRequest;
import {convertFormValuesToObject, FormFields} from '../../forms';

export function RegistrationForm(props: {
  onRegisterUser: (registerUserRequest: IRegisterUserRequest) => void;
}) {
  const formRef = useRef<HTMLFormElement>(null);
  const formFields = useState(new FormFields(formRef))[0];
  const [showPasswords, setShowPasswords] = useState(false);

  const firstNameRef = useRef<HTMLInputElement>(null);
  const lastNameRef = useRef<HTMLDivElement>(null);
  const emailAddressRef = useRef<HTMLDivElement>(null);
  const passwordRef = useRef<HTMLDivElement>(null);
  const verifyPasswordRef = useRef<HTMLDivElement>(null);
  const professionRef = useRef<HTMLDivElement>(null);
  const reasonForInterestRef = useRef<HTMLDivElement>(null);
  const districtNameRef = useRef<HTMLDivElement>(null);
  const schoolNameRef = useRef<HTMLDivElement>(null);
  const addressLine1Ref = useRef<HTMLDivElement>(null);
  const addressLine2Ref = useRef<HTMLDivElement>(null);
  const cityRef = useRef<HTMLDivElement>(null);
  const stateRef = useRef<HTMLDivElement>(null);
  const zipCodeRef = useRef<HTMLDivElement>(null);
  const numTeachersRef = useRef<HTMLDivElement>(null);
  const numStudentsRef = useRef<HTMLDivElement>(null);

  const [firstNameError, setFirstNameError] = useState('');
  const [lastNameError, setLastNameError] = useState('');
  const [emailAddressError, setEmailAddressError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [verifyPasswordError, setVerifyPasswordError] = useState('');
  const [professionError, setProfessionError] = useState('');
  const [reasonForInterestError, setReasonForInterestError] = useState('');
  const [districtNameError, setDistrictNameError] = useState('');
  const [schoolNameError, setSchoolNameError] = useState('');
  const [addressLine1Error, setAddressLine1Error] = useState('');
  const [addressLine2Error, setAddressLine2Error] = useState('');
  const [cityError, setCityError] = useState('');
  const [stateError, setStateError] = useState('');
  const [zipCodeError, setZipCodeError] = useState('');
  const [numTeachersError, setNumTeachersError] = useState('');
  const [numStudentsError, setNumStudentsError] = useState('');

  function onFormSubmit(e: FormEvent<HTMLFormElement>) {
    // Prevent the form from reloading the page.
    e.preventDefault();

    formFields.resetErrors();
    if (formFields.checkAndSet(true)) {
      return;
    }

    props.onRegisterUser(
      convertFormValuesToObject(formRef.current) as IRegisterUserRequest
    );
  }

  return (
    <form ref={formRef} onSubmit={onFormSubmit} noValidate>
      <Grid container spacing={2}>
        <Grid item xs={6}>
          <TextField
            required
            autoFocus
            autoComplete="given-name"
            label="First Name"
            {...formFields.registerProps(
              'firstName',
              firstNameRef,
              firstNameError,
              setFirstNameError,
              {
                startIcon: <AccountCircle />,
                maxLength: 255,
              }
            )}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            autoComplete="family-name"
            label="Last Name"
            {...formFields.registerProps(
              'lastName',
              lastNameRef,
              lastNameError,
              setLastNameError,
              {
                startIcon: <AccountCircle />,
                maxLength: 255,
              }
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            autoComplete="email"
            label="Email Address"
            {...formFields.registerProps(
              'emailAddress',
              emailAddressRef,
              emailAddressError,
              setEmailAddressError,
              {
                startIcon: <Email />,
                maxLength: 254,
                isEmail: true,
              }
            )}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            autoComplete="current-password"
            label="Password"
            {...formFields.registerProps(
              'password',
              passwordRef,
              passwordError,
              setPasswordError,
              {
                isPassword: {
                  showPasswords,
                  setShowPasswords,
                },
              }
            )}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            autoComplete="new-password"
            label="Verify Password"
            {...formFields.registerProps(
              'verifyPassword',
              verifyPasswordRef,
              verifyPasswordError,
              setVerifyPasswordError,
              {
                isPassword: {
                  showPasswords,
                  setShowPasswords,
                },
              }
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <Autocomplete
            freeSolo
            autoComplete
            options={[
              'K-12 District Leader/Administrator',
              'K-12 School Principal/Administrator',
              'K-12 Educator',
              'K-12 Student',
            ]}
            renderInput={params => (
              <TextField
                required
                autoComplete="organization-title"
                label="Profession"
                {...formFields.registerProps(
                  'profession',
                  professionRef,
                  professionError,
                  setProfessionError,
                  {
                    maxLength: 255,
                    params,
                  }
                )}
              />
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            multiline
            rows={3}
            label="Let us know why you're interested."
            {...formFields.registerProps(
              'reasonForInterest',
              reasonForInterestRef,
              reasonForInterestError,
              setReasonForInterestError,
              {
                startIcon: <Comment />,
                maxLength: 8192,
              }
            )}
          />
        </Grid>
        <Grid item xs={12} />
        <Grid item xs={12}>
          <TextField
            label="District Name"
            {...formFields.registerProps(
              'districtName',
              districtNameRef,
              districtNameError,
              setDistrictNameError,
              {maxLength: 255}
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            label="School Name"
            {...formFields.registerProps(
              'schoolName',
              schoolNameRef,
              schoolNameError,
              setSchoolNameError,
              {maxLength: 255}
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            label="Address Line 1"
            {...formFields.registerProps(
              'addressLine_1',
              addressLine1Ref,
              addressLine1Error,
              setAddressLine1Error,
              {maxLength: 255}
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            label="Address Line 2"
            {...formFields.registerProps(
              'addressLine_2',
              addressLine2Ref,
              addressLine2Error,
              setAddressLine2Error,
              {maxLength: 255}
            )}
          />
        </Grid>
        <Grid item xs={4}>
          <TextField
            label="City"
            {...formFields.registerProps(
              'city',
              cityRef,
              cityError,
              setCityError,
              {maxLength: 20}
            )}
          />
        </Grid>
        <Grid item xs={4}>
          <TextField
            label="State"
            {...formFields.registerProps(
              'state',
              stateRef,
              stateError,
              setStateError,
              {maxLength: 2}
            )}
          />
        </Grid>
        <Grid item xs={4}>
          <TextField
            label="Zip Code"
            {...formFields.registerProps(
              'zipCode',
              zipCodeRef,
              zipCodeError,
              setZipCodeError,
              {isZipCode: true}
            )}
          />
        </Grid>
        <Grid item xs={12} />
        <Grid item xs={6}>
          <TextField
            label="Number of Educators"
            {...formFields.registerProps(
              'numTeachers',
              numTeachersRef,
              numTeachersError,
              setNumTeachersError,
              {isInteger: {min: 0}}
            )}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            label="Number of Students"
            {...formFields.registerProps(
              'numStudents',
              numStudentsRef,
              numStudentsError,
              setNumStudentsError,
              {isInteger: {min: 0}}
            )}
          />
        </Grid>
        <Grid item xs={12} />
        <Grid item xs={12} style={{textAlign: 'right'}}>
          <Button
            variant="contained"
            className="project-builder-button"
            type="submit"
          >
            Register and Continue
          </Button>
        </Grid>
      </Grid>
    </form>
  );
}
