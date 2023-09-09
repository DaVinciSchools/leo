import './RegistrationForm.scss';

import {AccountCircle, Comment, Email, Lock} from '@mui/icons-material';
import {Autocomplete, Button, Grid, TextField} from '@mui/material';
import {FormEvent} from 'react';
import {useFormFields} from '../form_utils/forms';
import {user_x_management} from '../../generated/protobuf-js';

import IRegisterUserXRequest = user_x_management.IRegisterUserXRequest;

export function RegistrationForm(props: {
  onRegisterUserX: (registerUserXRequest: IRegisterUserXRequest) => void;
  onCancel: () => void;
}) {
  const formFields = useFormFields();
  const firstName = formFields.useStringFormField('firstName', {
    startIcon: <AccountCircle />,
    maxLength: 255,
  });
  const lastName = formFields.useStringFormField('lastName', {
    startIcon: <AccountCircle />,
    maxLength: 255,
  });
  const emailAddress = formFields.useStringFormField('emailAddress', {
    startIcon: <Email />,
    maxLength: 254,
    isEmail: true,
  });
  const password = formFields.useStringFormField('password', {
    isPassword: {},
    startIcon: <Lock />,
  });
  const verifyPassword = formFields.useStringFormField('verifyPassword', {
    isPassword: {},
    startIcon: <Lock />,
  });
  const profession = formFields.useAutocompleteFormField<string | null>(
    'profession',
    {
      maxLength: 255,
      isAutocomplete: {
        isFreeSolo: true,
      },
    }
  );
  const reasonForInterest = formFields.useStringFormField('reasonForInterest', {
    startIcon: <Comment />,
    maxLength: 8192,
  });
  const districtName = formFields.useStringFormField('districtName', {
    maxLength: 255,
  });
  const schoolName = formFields.useStringFormField('schoolName', {
    maxLength: 255,
  });
  const addressLine1 = formFields.useStringFormField('addressLine_1', {
    maxLength: 255,
  });
  const addressLine2 = formFields.useStringFormField('addressLine_2', {
    maxLength: 255,
  });
  const city = formFields.useStringFormField('city', {maxLength: 20});
  const state = formFields.useStringFormField('state', {maxLength: 2});
  const zipCode = formFields.useStringFormField('zipCode', {isZipCode: true});
  const numTeachers = formFields.useNumberFormField('numTeachers', {
    isInteger: {min: 0},
  });
  const numStudents = formFields.useNumberFormField('numStudents', {
    isInteger: {min: 0},
  });

  function onFormSubmit(e: FormEvent<HTMLFormElement>) {
    // Prevent the form from reloading the page.
    e.preventDefault();

    if (!formFields.verifyOk(true)) {
      return;
    }

    props.onRegisterUserX(
      formFields.getValuesObject() as IRegisterUserXRequest
    );
  }

  return (
    <form onSubmit={onFormSubmit} noValidate>
      <div className="registration-form-logo">
        <div />
        <img src="/images/logo-orange-on-white.svg" />
      </div>
      <Grid container spacing={2}>
        <Grid item xs={6}>
          <TextField
            required
            autoFocus
            autoComplete="given-name"
            label="First Name"
            {...firstName.textFieldParams()}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            autoComplete="family-name"
            label="Last Name"
            {...lastName.textFieldParams()}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            autoComplete="email"
            label="Email Address"
            {...emailAddress.textFieldParams()}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            autoComplete="current-password"
            label="Password"
            {...password.textFieldParams()}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            autoComplete="new-password"
            label="Verify Password"
            {...verifyPassword.textFieldParams()}
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
                {...profession.textFieldParams(params)}
              />
            )}
            {...profession.autocompleteParams()}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            multiline
            rows={3}
            label="Let us know why you're interested."
            {...reasonForInterest.textFieldParams()}
          />
        </Grid>
        <Grid item xs={12} />
        <Grid item xs={12}>
          <TextField
            label="District Name"
            {...districtName.textFieldParams()}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField label="School Name" {...schoolName.textFieldParams()} />
        </Grid>
        <Grid item xs={12}>
          <TextField
            label="Address Line 1"
            {...addressLine1.textFieldParams()}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            label="Address Line 2"
            {...addressLine2.textFieldParams()}
          />
        </Grid>
        <Grid item xs={4}>
          <TextField label="City" {...city.textFieldParams()} />
        </Grid>
        <Grid item xs={4}>
          <TextField label="State" {...state.textFieldParams()} />
        </Grid>
        <Grid item xs={4}>
          <TextField label="Zip Code" {...zipCode.textFieldParams()} />
        </Grid>
        <Grid item xs={12} />
        <Grid item xs={6}>
          <TextField
            label="Number of Educators"
            {...numTeachers.textFieldParams()}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            label="Number of Students"
            {...numStudents.textFieldParams()}
          />
        </Grid>
        <Grid item xs={12} />
        <Grid item xs={12} className="registration-form-buttons">
          <Button
            variant="contained"
            className="project-builder-button"
            type="submit"
          >
            Register and Continue
          </Button>
          <Button
            variant="contained"
            className="project-builder-button"
            onClick={props.onCancel}
          >
            Cancel
          </Button>
        </Grid>
      </Grid>
    </form>
  );
}
