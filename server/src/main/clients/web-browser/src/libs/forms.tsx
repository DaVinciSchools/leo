import {AutocompleteRenderInputParams, InputAdornment} from '@mui/material';
import {OutlinedTextFieldProps} from '@mui/material/TextField/TextField';
import {ReactElement, RefObject, useRef, useState} from 'react';
import {Visibility, VisibilityOff} from '@mui/icons-material';

const MAX_ZIP_CODE_LENGTH = 10;
const MIN_PASSWORD_LENGTH = 8;
const MAX_PASSWORD_LENGTH = 50;

const PASSWORD_PATTERN = RegExp(
  `^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{${MIN_PASSWORD_LENGTH},${MAX_PASSWORD_LENGTH}}$`
);
const NUMBER_PATTERN = RegExp('^-?[0-9.]+([eE][+-][0-9]+)?');
const INTEGER_PATTERN = RegExp('^-?[0-9]+');
const EMAIL_PATTERN = RegExp('^[^@]+@[^@]+\\.[^@]{2,}$');
const ZIP_CODE_PATTERN = RegExp('^[0-9]{5}(-[0-9]{4})?$');

const PASSWORD_ERROR_MESSAGE =
  'Passwords must have 8+ characters, a number, and a lower and upper case letter.';

export interface FieldMetadata {
  isPassword?: {
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
}

export interface FormField<T extends number | string> {
  readonly name: string;
  readonly fieldRef: RefObject<HTMLDivElement>;
  readonly fieldMetadata?: FieldMetadata;

  readonly stringValue: string;
  readonly setStringValue: (value: string) => void;

  readonly error: string;
  readonly setError: (message: string) => void;

  readonly getTypedValue: () => T | undefined;

  readonly params: (
    params?: AutocompleteRenderInputParams
  ) => OutlinedTextFieldProps;
}

export interface FormFields {
  readonly useNumberFormField: (
    name: string,
    fieldMetadata?: FieldMetadata
  ) => FormField<number>;

  readonly useStringFormField: (
    name: string,
    fieldMetadata?: FieldMetadata
  ) => FormField<string>;

  readonly setValuesObject: (values: {} | null | undefined) => void;

  readonly getValuesObject: () => {};
  readonly getValuesURLSearchParams: () => URLSearchParams;

  readonly verifyOk: (finalCheck: boolean) => boolean;
  readonly isTentativelyOkToSubmit: () => boolean;
}

export function useFormFields() {
  return FormFieldsImpl.useFormFields();
}

class FormFieldsImpl implements FormFields {
  //eslint-disable-next-line @typescript-eslint/no-explicit-any
  readonly #fields: Map<string, FormField<any>> = new Map();
  #showPasswords = false;
  #setShowPasswords: (showPasswords: boolean) => void = () => {
    throw new Error(
      'Use FormFields.useFormFields() to create a FormFields object.'
    );
  };

  static useFormFields() {
    const formFields = useState(new FormFieldsImpl())[0];
    [formFields.#showPasswords, formFields.#setShowPasswords] = useState(false);
    return formFields;
  }

  useStringFormField(
    name: string,
    fieldMetadata?: FieldMetadata
  ): FormField<string> {
    if (fieldMetadata?.isInteger) {
      throw new Error(
        'useStringFormField requires FieldMetadata.isInteger to be unset.'
      );
    }
    return this.#useFormField<string>(name, fieldMetadata);
  }

  useNumberFormField(
    name: string,
    fieldMetadata?: FieldMetadata
  ): FormField<number> {
    if (!fieldMetadata?.isInteger) {
      throw new Error(
        'useNumberFormField requires FieldMetadata.isInteger to be set.'
      );
    }
    return this.#useFormField<number>(name, fieldMetadata);
  }

  #useFormField<T extends number | string>(
    name: string,
    fieldMetadata?: FieldMetadata
  ): FormField<T> {
    const [stringValue, setStringValue] = useState('');
    const [error, setError] = useState('');
    const fieldRef = useRef<HTMLDivElement>(null);

    const formField: FormField<T> = {
      name,
      stringValue,
      setStringValue,
      fieldRef,
      error,
      setError,
      fieldMetadata,

      params: (params?: AutocompleteRenderInputParams) => {
        const checkErrorFn = (finalCheck: boolean) =>
          checkFieldForErrorsAndSet(
            setError,
            getInputField(fieldRef.current),
            finalCheck,
            fieldMetadata
          );

        return {
          ...(params ?? {}),
          value: stringValue,
          type:
            fieldMetadata?.isPassword && !this.#showPasswords
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
          onChange: e => {
            setError('');
            setStringValue(e.target.value);
            checkErrorFn(true);
          },
          onBlur: () => {
            setError('');
            checkErrorFn(true);
          },
          InputProps: {
            ...(params?.InputProps ?? {}),
            startAdornment: fieldMetadata?.startIcon ? (
              <InputAdornment position="start" style={{cursor: 'not-allowed'}}>
                {fieldMetadata.startIcon}
              </InputAdornment>
            ) : (
              params?.InputProps?.startAdornment
            ),
            endAdornment: fieldMetadata?.isPassword ? (
              this.#showPasswords ? (
                <Visibility
                  onClick={() => this.#setShowPasswords(false)}
                  style={{cursor: 'pointer'}}
                />
              ) : (
                <VisibilityOff
                  onClick={() => this.#setShowPasswords(true)}
                  style={{cursor: 'pointer'}}
                />
              )
            ) : (
              params?.InputProps?.endAdornment
            ),
          },
          inputProps: {
            ...(params?.inputProps ?? {}),
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
      },

      getTypedValue: () => {
        const input = getInputField(fieldRef.current);
        if (input == null || stringValue == null || stringValue.length === 0) {
          // We don't have a value to process.
          return;
        }

        if (['email', 'password', 'text', 'textarea'].includes(input.type)) {
          const trimmedValue = stringValue.trim();
          return trimmedValue.length === 0 ? undefined : (trimmedValue as T);
        }

        if (input.type === 'number') {
          if (NUMBER_PATTERN.exec(input.value)) {
            return (
              INTEGER_PATTERN.exec(input.value)
                ? parseInt(input.value)
                : parseFloat(input.value)
            ) as T;
          }
          return;
        }

        throw new Error(`Input type '${input.type}' is not recognized.`);
      },
    };

    //eslint-disable-next-line @typescript-eslint/no-explicit-any
    this.#fields.set(name, formField as FormField<any>);

    return formField;
  }

  verifyOk(finalCheck: boolean) {
    this.#fields.forEach(f => f.setError(''));
    let errorField;

    const passwordFields: FormField<string>[] = [];
    const passwordInputs: (
      | HTMLInputElement
      | HTMLTextAreaElement
      | undefined
    )[] = [];

    //eslint-disable-next-line @typescript-eslint/no-unused-vars
    for (const [name, field] of this.#fields) {
      if (
        field.fieldMetadata?.isPassword != null &&
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
      (finalCheck && passwordsSet.size > 1) ||
      passwordsSetNonBlank.size > 1
    ) {
      passwordFields.forEach(f => f.setError('Passwords do not match.'));
      errorField = passwordFields[0];
    }

    this.#fields.forEach(f => {
      if (
        checkFieldForErrorsAndSet(
          f.setError,
          getInputField(f.fieldRef.current),
          finalCheck,
          f.fieldMetadata
        )
      ) {
        errorField = f;
      }
    });

    if (errorField) {
      const input = getInputField(errorField.fieldRef.current);
      if (input != null) {
        input.scrollIntoView(true);
        input.focus();
      }
    }

    return !errorField;
  }

  #checkPasswords(finalCheck: boolean) {
    let error = false;

    this.#fields.forEach(f => {
      if (
        f.fieldMetadata?.isPassword &&
        f.fieldMetadata?.isPassword?.skipPasswordCheck !== true
      ) {
        if (
          checkFieldForErrorsAndSet(
            f.setError,
            getInputField(f.fieldRef.current),
            finalCheck,
            f.fieldMetadata
          )
        ) {
          error = true;
        }
      }
    });

    return error;
  }

  setValuesObject(values: {} | null | undefined) {
    this.#fields.forEach(f => f.setError(''));
    const valuesMap = new Map(Object.entries(values ?? {}));

    this.#fields.forEach(f => {
      f.setStringValue(
        valuesMap.get(f.name) == null ? '' : String(valuesMap.get(f.name))
      );
    });
  }

  getValuesObject() {
    const values: {[key: string]: string | number} = {};

    this.#fields.forEach(f => {
      const value = f.getTypedValue();
      if (value != null) {
        values[f.name] = value;
      }
    });

    return values;
  }

  getValuesURLSearchParams() {
    const params = new URLSearchParams();

    this.#fields.forEach(f => {
      if (f.stringValue == null || f.stringValue.trim() === '') {
        return;
      }
      params.append(f.name, f.stringValue);
    });

    return params;
  }

  isTentativelyOkToSubmit() {
    let okToSubmit = true;
    this.#fields.forEach(f => {
      okToSubmit &&= !f.error;
      if (okToSubmit && getInputField(f.fieldRef.current)?.required) {
        okToSubmit &&= !!f.stringValue;
      }
    });
    return okToSubmit;
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
