import {ReactElement, RefObject} from 'react';
import {OutlinedTextFieldProps} from '@mui/material/TextField/TextField';
import {AutocompleteRenderInputParams, InputAdornment} from '@mui/material';
import {Visibility, VisibilityOff} from '@mui/icons-material';

const MAX_ZIP_CODE_LENGTH = 10;
const MIN_PASSWORD_LENGTH = 8;
const MAX_PASSWORD_LENGTH = 50;
export const PASSWORD_PATTERN = RegExp(
  `^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{${MIN_PASSWORD_LENGTH},${MAX_PASSWORD_LENGTH}}$`
);
export const PASSWORD_ERROR_MESSAGE =
  'Passwords must have 8+ characters, a number, and a lower and upper case letter.';
export const NUMBER_PATTERN = RegExp('^-?[0-9.]+([eE][+-][0-9]+)?');
export const INTEGER_PATTERN = RegExp('^-?[0-9]+');
export const EMAIL_PATTERN = RegExp('^[^@]+@[^@]+\\.[^@]{3,}$');
export const ZIP_CODE_PATTERN = RegExp('^[0-9]{5}(-[0-9]{4})?$');

export interface FieldMetadata {
  isPassword?: {
    showPasswords: boolean;
    setShowPasswords: (showPasswords: boolean) => void;
    skipPasswordCheck?: boolean;
  };
  isInteger?: {
    min?: number;
    max?: number;
  };
  isEmail?: boolean;
  isZipCode?: boolean;
  startIcon?: ReactElement;
  maxLength?: number;
  params?: AutocompleteRenderInputParams;
}

interface FormField {
  name: string;
  fieldRef: RefObject<HTMLDivElement>;
  setError: (message: string) => void;
  error: string;
  fieldMetadata?: FieldMetadata;
  checkAndSet: (finalCheck: boolean) => boolean;
}

export class FormFields {
  readonly #fields: Map<string, FormField> = new Map();
  readonly #formRef: RefObject<HTMLFormElement>;

  constructor(formRef: RefObject<HTMLFormElement>) {
    this.#formRef = formRef;
  }

  resetErrors() {
    this.#fields.forEach(f => f.setError(''));
  }

  checkAndSet(finalCheck: boolean) {
    let error = false;

    const passwordFields: FormField[] = [];
    const passwordInputs: (
      | HTMLInputElement
      | HTMLTextAreaElement
      | undefined
    )[] = [];

    //eslint-disable-next-line @typescript-eslint/no-unused-vars
    for (const [name, field] of this.#fields) {
      if (
        field.fieldMetadata?.isPassword &&
        field?.fieldMetadata?.isPassword?.skipPasswordCheck !== true
      ) {
        passwordFields.push(field);
        passwordInputs.push(getInputField(field.fieldRef.current));
      }
    }
    const passwords = passwordInputs.map(i => i?.value);
    const passwordsSet = new Set(passwords);
    const passwordsSetNonBlank = new Set(passwordsSet);

    if (
      (finalCheck && passwordsSet.size > 0) ||
      passwordsSetNonBlank.size > 1
    ) {
      passwordFields.forEach(f => f.setError('Passwords do not match.'));
      error = true;
    }

    this.#fields.forEach(f => {
      if (f.checkAndSet(finalCheck)) {
        error = true;
      }
    });

    return error;
  }

  showPasswords(showPasswords: boolean) {
    this.#fields.forEach(f => {
      if (f.fieldMetadata?.isPassword) {
        const input = getInputField(f.fieldRef.current);
        if (input != null && input instanceof HTMLInputElement) {
          input.type = showPasswords ? 'text' : 'password';
        }
      }
    });
  }

  checkPasswords(finalCheck: boolean) {
    let error = false;

    this.#fields.forEach(f => {
      if (
        f.fieldMetadata?.isPassword &&
        f.fieldMetadata?.isPassword?.skipPasswordCheck !== true
      ) {
        if (f.checkAndSet(finalCheck)) {
          error = true;
        }
      }
    });

    return error;
  }

  registerProps(
    name: string,
    fieldRef: React.RefObject<HTMLDivElement>,
    error: string,
    setError: (message: string) => void,
    fieldMetadata?: FieldMetadata
  ): OutlinedTextFieldProps {
    const checkErrorFn = (finalCheck: boolean) =>
      checkFieldForErrorsAndSet(
        setError,
        getInputField(fieldRef.current),
        finalCheck,
        fieldMetadata
      );

    const checkAndSet = (finalCheck: boolean) => {
      if (checkErrorFn(finalCheck)) {
        const input = getInputField(fieldRef.current);
        if (input != null) {
          input.scrollIntoView(true);
          input.focus();
        }
        return true;
      }
      return false;
    };

    this.#fields.set(name, {
      name,
      fieldRef,
      setError,
      error,
      fieldMetadata,
      checkAndSet,
    });

    return {
      ...(fieldMetadata?.params ?? {}),
      type:
        fieldMetadata?.isPassword?.showPasswords === false
          ? 'password'
          : fieldMetadata?.isEmail
          ? 'email'
          : fieldMetadata?.isInteger != null
          ? 'number'
          : 'text',
      variant: 'outlined',
      fullWidth: true,
      size: 'small',
      name: name,
      ref: fieldRef,
      helperText: error,
      error: !!error,
      onChange: () => {
        setError('');
        checkErrorFn(true);
      },
      onBlur: () => {
        setError('');
        checkErrorFn(true);
      },
      InputProps: {
        ...fieldMetadata?.params?.InputProps,
        startAdornment: fieldMetadata?.startIcon ? (
          <InputAdornment position="start" style={{cursor: 'not-allowed'}}>
            {fieldMetadata.startIcon}
          </InputAdornment>
        ) : undefined,
        endAdornment: fieldMetadata?.isPassword ? (
          fieldMetadata.isPassword?.showPasswords ? (
            <Visibility
              onClick={() => fieldMetadata?.isPassword?.setShowPasswords(false)}
              style={{cursor: 'pointer'}}
            />
          ) : (
            <VisibilityOff
              onClick={() => fieldMetadata?.isPassword?.setShowPasswords(true)}
              style={{cursor: 'pointer'}}
            />
          )
        ) : undefined,
      },
      inputProps: {
        ...fieldMetadata?.params?.inputProps,
        minLength:
          fieldMetadata?.isPassword &&
          fieldMetadata?.isPassword?.skipPasswordCheck !== true
            ? MIN_PASSWORD_LENGTH
            : undefined,
        maxLength:
          fieldMetadata?.maxLength ??
          (fieldMetadata?.isZipCode
            ? MAX_ZIP_CODE_LENGTH
            : fieldMetadata?.isPassword &&
              fieldMetadata?.isPassword?.skipPasswordCheck !== true
            ? MAX_PASSWORD_LENGTH
            : undefined),
        min: fieldMetadata?.isInteger?.min,
        max: fieldMetadata?.isInteger?.max,
      },
    };
  }
}

// We have to do this complex search because MUI input refs do not refer
// to the actual input element, but rather some parent DIV.
export function getInputField(
  field: HTMLElement | null | undefined
): HTMLInputElement | HTMLTextAreaElement | undefined {
  if (field == null) {
    // We don't have a field to search under yet.
    return;
  }

  let input: HTMLInputElement | HTMLTextAreaElement | undefined;

  if (['INPUT', 'TEXTAREA'].includes(field.tagName)) {
    return field as HTMLInputElement | HTMLTextAreaElement;
  }

  inputSearch: for (const tagName of ['INPUT', 'TEXTAREA']) {
    const inputs = field.getElementsByTagName(tagName);
    for (let i = 0; i < inputs.length; ++i) {
      // MUI will create multiple input elements for an TextField. We only
      // want the visible one.
      if (getComputedStyle(inputs.item(i)!).visibility === 'hidden') {
        continue;
      }
      input = inputs.item(i)! as HTMLInputElement | HTMLTextAreaElement;
      break inputSearch;
    }
  }

  return input;
}

export function getAllInputFields(
  form: HTMLFormElement | undefined
): (HTMLInputElement | HTMLTextAreaElement)[] {
  if (form == null) {
    // We don't have a form to search under yet.
    return [];
  }

  const inputFields: (HTMLInputElement | HTMLTextAreaElement)[] = [];

  const inputs = form.getElementsByTagName('INPUT');
  for (let i = 0; i < inputs.length; ++i) {
    inputFields.push(inputs.item(i) as HTMLInputElement);
  }
  const textAreas = form.getElementsByTagName('TEXTAREA');
  for (let i = 0; i < textAreas.length; ++i) {
    inputFields.push(textAreas.item(i) as HTMLTextAreaElement);
  }

  return inputFields.filter(
    i =>
      // MUI will create multiple input elements for an TextField. We only
      // want the visible one.
      getComputedStyle(i).visibility !== 'hidden'
  );
}

export function checkFieldForErrorsAndSet(
  setError: (error: string) => void,
  input: HTMLInputElement | HTMLTextAreaElement | undefined,
  finalCheck: boolean,
  extraErrorChecks?: FieldMetadata
): string | undefined {
  const error = checkFieldForErrors(input, finalCheck, extraErrorChecks);
  if (error) {
    setError(error);
  }
  return error;
}

export function checkFieldForErrors(
  input: HTMLInputElement | HTMLTextAreaElement | undefined,
  finalCheck: boolean,
  extraErrorChecks?: FieldMetadata
): string | undefined {
  if (input == null) {
    // We don't have a field to check yet.
    return;
  }

  if (
    !['email', 'number', 'password', 'text', 'textarea'].includes(input.type)
  ) {
    throw new Error(`Input type '${input.type}' is not recognized.`);
  }

  if (finalCheck) {
    if (input.required && input.value.trim().length === 0) {
      return 'This field is required.';
    }
    if (input.minLength > 0 && input.value.trim().length < input.minLength) {
      return `This field must have at least ${input.minLength} character(s).`;
    }
    if (input.maxLength >= 0 && input.value.trim().length > input.maxLength) {
      return `This field must have less than ${input.maxLength} character(s).`;
    }
  }

  if (input.value.length === 0 && (!finalCheck || !input.required)) {
    return;
  }

  if (input.type === 'number' && finalCheck && input.value.length > 0) {
    if (NUMBER_PATTERN.exec(input.value) == null) {
      return 'This field must be a number.';
    }
    if (
      extraErrorChecks?.isInteger &&
      INTEGER_PATTERN.exec(input.value) == null
    ) {
      return 'This field must be an integer.';
    }
    if (
      'min' in input &&
      input.min.length > 0 &&
      (INTEGER_PATTERN.exec(input.min) != null
        ? parseInt(input.min)
        : parseFloat(input.min)) > input.valueAsNumber
    ) {
      return `This field must be greater than or equal to ${input.min}.`;
    }
    if (
      'max' in input &&
      input.max.length > 0 &&
      (INTEGER_PATTERN.exec(input.max) != null
        ? parseInt(input.max)
        : parseFloat(input.max)) < input.valueAsNumber
    ) {
      return `This field must be less than or equal to ${input.max}.`;
    }
  }
  if (input.type === 'email' && EMAIL_PATTERN.exec(input.value) == null) {
    return 'This field is not a valid e-mail address.';
  }
  if (
    (input.type === 'password' || extraErrorChecks?.isPassword) &&
    extraErrorChecks?.isPassword?.skipPasswordCheck !== true &&
    !PASSWORD_PATTERN.exec(input.value)
  ) {
    return PASSWORD_ERROR_MESSAGE;
  }
  if (
    extraErrorChecks?.isZipCode &&
    ZIP_CODE_PATTERN.exec(input.value) == null
  ) {
    return 'This field must be 5 digits optionally followed by a dash and 4 digits.';
  }
  return;
}

export function getInputValue(
  input: HTMLInputElement | HTMLTextAreaElement | undefined
) {
  if (input == null) {
    // We don't have a field to extract a value from yet.
    return;
  }

  if (['email', 'password', 'text', 'textarea'].includes(input.type)) {
    if (input.value.length === 0) {
      return;
    }
    return input.value.trim();
  }

  if (input.type === 'number') {
    if (
      input.value.length === 0 ||
      ('valueAsNumber' in input && isNaN(input.valueAsNumber))
    ) {
      return;
    }
    if (NUMBER_PATTERN.exec(input.value)) {
      return INTEGER_PATTERN.exec(input.value)
        ? parseInt(input.value)
        : parseFloat(input.value);
    }
    throw new Error(`Input value '${input.value}' is not a number.`);
  }

  throw new Error(`Input type '${input.type}' is not recognized.`);
}

export function convertFormValuesToObject(
  form: HTMLFormElement | null | undefined
) {
  if (!form) {
    return {};
  }

  return Object.fromEntries(
    getAllInputFields(form)
      .map(input => [input.name, getInputValue(input)])
      //eslint-disable-next-line @typescript-eslint/no-unused-vars
      .filter(([k, v]) => v !== undefined)
  );
}

export function convertFormValuesToURLSearchParams(
  form: HTMLFormElement | null | undefined
) {
  const params = new URLSearchParams();

  if (!form) {
    return params;
  }

  getAllInputFields(form)
    .map(input => [input.name, getInputValue(input)])
    //eslint-disable-next-line @typescript-eslint/no-unused-vars
    .filter(([k, v]) => v !== undefined)
    .forEach(([k, v]) => params.append(String(k), String(v)));
  return params;
}
