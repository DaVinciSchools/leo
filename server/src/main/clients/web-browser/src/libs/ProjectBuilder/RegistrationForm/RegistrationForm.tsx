import {
  Autocomplete,
  Button,
  Grid,
  InputAdornment,
  TextField,
} from '@mui/material';
import {
  AccountCircle,
  Email,
  LockPerson,
  QuestionMark,
  Visibility,
  VisibilityOff,
} from '@mui/icons-material';
import {FormEvent, RefObject, useRef, useState} from 'react';
import {user_management} from '../../../generated/protobuf-js';
import IRegisterUserRequest = user_management.IRegisterUserRequest;
import {
  checkFieldForErrors,
  convertFormValuesToMap,
  ExtraErrorChecks,
  getInputField,
  getInputValue,
  MAX_PASSWORD_LENGTH,
  MIN_PASSWORD_LENGTH,
} from '../../forms';
import {OutlinedTextFieldProps} from '@mui/material/TextField/TextField';

export function RegistrationForm(props: {
  onRegisterUser: (registerUserRequest: IRegisterUserRequest) => void;
}) {
  const formRef = useRef<HTMLFormElement>(null);
  const formFieldErrorCheckers = useRef(
    new Map<string, (finalCheck: boolean) => boolean>()
  );

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

  function registrationFieldProps(
    name: string,
    ref: RefObject<HTMLDivElement>,
    setError: (message: string) => void,
    error: string,
    extraErrorChecks?: ExtraErrorChecks
  ): OutlinedTextFieldProps {
    formFieldErrorCheckers.current.set(name, finalCheck => {
      const error = checkFieldForErrors(
        getInputField(ref.current),
        finalCheck,
        extraErrorChecks
      );
      if (error != null) {
        setError(error);
        ref.current?.scrollIntoView(true);
        getInputField(ref.current)?.focus();
        return true;
      } else {
        setError('');
        return false;
      }
    });

    return {
      variant: 'outlined',
      fullWidth: true,
      size: 'small',
      name: name,
      ref: ref,
      helperText: error,
      error: !!error,
      onChange: () =>
        setError(
          checkFieldForErrors(
            getInputField(ref.current),
            true,
            extraErrorChecks
          ) ?? ''
        ),
      onBlur: () =>
        setError(
          checkFieldForErrors(
            getInputField(ref.current),
            true,
            extraErrorChecks
          ) ?? ''
        ),
    };
  }

  function checkFormForErrors(finalCheck: boolean) {
    let error = false;

    const password = getInputValue(getInputField(passwordRef.current));
    const verifyPassword = getInputValue(
      getInputField(verifyPasswordRef.current)
    );
    if (finalCheck || (password != null && verifyPassword != null)) {
      if (password !== verifyPassword) {
        error = true;
        setPasswordError('Passwords do not match.');
        setVerifyPasswordError('Passwords do not match.');
      }
    }

    formFieldErrorCheckers.current.forEach(fn => {
      if (fn(finalCheck)) {
        error = true;
      }
    });

    return error;
  }

  function onFormSubmit(e: FormEvent<HTMLFormElement>) {
    // Prevent the form from reloading the page.
    e.preventDefault();

    if (checkFormForErrors(true)) {
      return;
    }

    const request = convertFormValuesToMap(
      formRef.current
    ) as IRegisterUserRequest;

    props.onRegisterUser(request);
  }

  return (
    <form ref={formRef} onSubmit={onFormSubmit} noValidate>
      <Grid container spacing={2}>
        <Grid item xs={6}>
          <TextField
            required
            autoFocus
            type="text"
            autoComplete="given-name"
            label="First Name"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <AccountCircle />
                </InputAdornment>
              ),
            }}
            inputProps={{maxLength: 255}}
            {...registrationFieldProps(
              'firstName',
              firstNameRef,
              setFirstNameError,
              firstNameError
            )}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            type="text"
            autoComplete="family-name"
            label="Last Name"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <AccountCircle />
                </InputAdornment>
              ),
            }}
            inputProps={{maxLength: 255}}
            {...registrationFieldProps(
              'lastName',
              lastNameRef,
              setLastNameError,
              lastNameError
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            type="email"
            autoComplete="email"
            label="Email Address"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <Email />
                </InputAdornment>
              ),
            }}
            inputProps={{maxLength: 254}}
            {...registrationFieldProps(
              'emailAddress',
              emailAddressRef,
              setEmailAddressError,
              emailAddressError
            )}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            type={showPasswords ? 'text' : 'password'}
            autoComplete="current-password"
            label="Password"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <LockPerson />
                </InputAdornment>
              ),
              endAdornment: (
                <InputAdornment position="end">
                  {showPasswords ? (
                    <Visibility onClick={() => setShowPasswords(false)} />
                  ) : (
                    <VisibilityOff onClick={() => setShowPasswords(true)} />
                  )}
                </InputAdornment>
              ),
            }}
            inputProps={{
              minLength: MIN_PASSWORD_LENGTH,
              maxLength: MAX_PASSWORD_LENGTH,
            }}
            {...registrationFieldProps(
              'password',
              passwordRef,
              setPasswordError,
              passwordError,
              {isPassword: true}
            )}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            type={showPasswords ? 'text' : 'password'}
            autoComplete="new-password"
            label="Verify Password"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <LockPerson />
                </InputAdornment>
              ),
              endAdornment: (
                <InputAdornment position="end">
                  {showPasswords ? (
                    <Visibility onClick={() => setShowPasswords(false)} />
                  ) : (
                    <VisibilityOff onClick={() => setShowPasswords(true)} />
                  )}
                </InputAdornment>
              ),
            }}
            inputProps={{
              minLength: MIN_PASSWORD_LENGTH,
              maxLength: MAX_PASSWORD_LENGTH,
            }}
            {...registrationFieldProps(
              'verifyPassword',
              verifyPasswordRef,
              setVerifyPasswordError,
              verifyPasswordError,
              {isPassword: true}
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
            renderInput={params => {
              const newParams = {...params};
              params.inputProps = params.inputProps ?? {};
              params.inputProps.maxLength = 255;
              return (
                <TextField
                  required
                  type="text"
                  autoComplete="organization-title"
                  label="Profession"
                  {...newParams}
                  {...registrationFieldProps(
                    'profession',
                    professionRef,
                    setProfessionError,
                    professionError
                  )}
                />
              );
            }}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            multiline
            rows={3}
            type="text"
            label="Let us know why you're interested."
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <QuestionMark />
                </InputAdornment>
              ),
            }}
            inputProps={{maxLength: 8192}}
            {...registrationFieldProps(
              'reasonForInterest',
              reasonForInterestRef,
              setReasonForInterestError,
              reasonForInterestError
            )}
          />
        </Grid>
        <Grid item xs={12} />
        <Grid item xs={12}>
          <TextField
            type="text"
            label="District Name"
            inputProps={{maxLength: 255}}
            {...registrationFieldProps(
              'districtName',
              districtNameRef,
              setDistrictNameError,
              districtNameError
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            type="text"
            label="School Name"
            inputProps={{maxLength: 255}}
            {...registrationFieldProps(
              'schoolName',
              schoolNameRef,
              setSchoolNameError,
              schoolNameError
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            type="text"
            label="Address Line 1"
            inputProps={{maxLength: 255}}
            {...registrationFieldProps(
              'addressLine_1',
              addressLine1Ref,
              setAddressLine1Error,
              addressLine1Error
            )}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            type="text"
            label="Address Line 2"
            inputProps={{maxLength: 255}}
            {...registrationFieldProps(
              'addressLine_2',
              addressLine2Ref,
              setAddressLine2Error,
              addressLine2Error
            )}
          />
        </Grid>
        <Grid item xs={4}>
          <TextField
            type="text"
            label="City"
            inputProps={{maxLength: 20}}
            {...registrationFieldProps(
              'city',
              cityRef,
              setCityError,
              cityError
            )}
          />
        </Grid>
        <Grid item xs={4}>
          <TextField
            type="text"
            label="State"
            inputProps={{maxLength: 2}}
            {...registrationFieldProps(
              'state',
              stateRef,
              setStateError,
              stateError
            )}
          />
        </Grid>
        <Grid item xs={4}>
          <TextField
            type="text"
            label="Zip Code"
            inputProps={{maxLength: 10}}
            {...registrationFieldProps(
              'zipCode',
              zipCodeRef,
              setZipCodeError,
              zipCodeError,
              {isZipCode: true}
            )}
          />
        </Grid>
        <Grid item xs={12} />
        <Grid item xs={6}>
          <TextField
            type="number"
            label="Number of Educators"
            inputProps={{min: '0'}}
            {...registrationFieldProps(
              'numTeachers',
              numTeachersRef,
              setNumTeachersError,
              numTeachersError
            )}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            type="number"
            label="Number of Students"
            inputProps={{min: '0'}}
            {...registrationFieldProps(
              'numStudents',
              numStudentsRef,
              setNumStudentsError,
              numStudentsError
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
            Show Me the Projects!
          </Button>
        </Grid>
      </Grid>
    </form>
  );
}
