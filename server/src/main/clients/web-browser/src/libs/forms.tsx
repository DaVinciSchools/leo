/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/no-unused-vars */

import {
  AutocompleteProps,
  AutocompleteRenderInputParams,
  AutocompleteValue,
  InputAdornment,
} from '@mui/material';
import {OutlinedTextFieldProps} from '@mui/material/TextField/TextField';
import {
  DetailedHTMLProps,
  FormHTMLAttributes,
  ReactElement,
  RefObject,
  useEffect,
  useRef,
  useState,
} from 'react';
import {Visibility, VisibilityOff} from '@mui/icons-material';
import {CheckboxProps} from '@mui/material/Checkbox/Checkbox';
import * as React from 'react';
import {ChipTypeMap} from '@mui/material/Chip';

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
  isBoolean?: boolean;
  isEmail?: boolean;
  isZipCode?: boolean;
  startIcon?: ReactElement;
  maxLength?: number;
  isAutocomplete?: boolean;
  isMultiple?: boolean;
}

export type FormFieldsMetadata = {
  onChange?: (formFields: FormFields, formField: FormField<any>) => void;
};

export function useFormFields(formFieldsMetadata?: FormFieldsMetadata) {
  return useState(FormFields.useFormFields(formFieldsMetadata))[0];
}

export interface FormField<T> {
  name: string;
  fieldRef: RefObject<HTMLDivElement | HTMLButtonElement>;
  fieldMetadata?: FieldMetadata;

  getValue: () => T | undefined;
  setValue: (value: T | undefined) => void;

  error: string;
  setError: (message: string) => void;

  autocompleteParams<
    DisableClearable extends boolean | undefined = false,
    FreeSolo extends boolean | undefined = false,
    ChipComponent extends React.ElementType = ChipTypeMap['defaultComponent']
  >(): Partial<
    AutocompleteProps<
      T extends (infer R)[] ? R : T,
      T extends (infer R)[] ? true : false,
      DisableClearable,
      FreeSolo,
      ChipComponent
    >
  >;

  textFieldParams: (
    params?: AutocompleteRenderInputParams
  ) => OutlinedTextFieldProps;

  checkboxParams: (params?: CheckboxProps) => CheckboxProps;
}

export class FormFields {
  readonly #fields: Map<string, FormField<any>> = new Map();
  #formFieldsMetadata?: FormFieldsMetadata;
  #formRef?: RefObject<HTMLFormElement>;
  #showPasswords = false;
  #setShowPasswords: (showPasswords: boolean) => void = () => {
    throw new Error(
      'Use FormFields.useFormFields() to create a FormFields object.'
    );
  };

  static useFormFields(formFieldsMetadata?: FormFieldsMetadata) {
    const formFields = useState(new FormFields())[0];
    formFields.#formFieldsMetadata = formFieldsMetadata;
    formFields.#formRef = useRef<HTMLFormElement>(null);
    [formFields.#showPasswords, formFields.#setShowPasswords] = useState(false);
    return formFields;
  }

  params(): DetailedHTMLProps<
    FormHTMLAttributes<HTMLFormElement>,
    HTMLFormElement
  > {
    return {ref: this.#formRef};
  }

  #formChanged(formField: FormField<any>) {
    if (this.#formFieldsMetadata && this.#formFieldsMetadata.onChange) {
      this.#formFieldsMetadata.onChange(this, formField);
    }
  }

  useStringFormField(
    name: string,
    fieldMetadata?: FieldMetadata
  ): FormField<string> {
    if (
      fieldMetadata?.isInteger ||
      fieldMetadata?.isBoolean ||
      fieldMetadata?.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata does not indicate a string: ${JSON.stringify(
          fieldMetadata ?? {}
        )}`
      );
    }
    return this.#useFormField<string>(name, fieldMetadata);
  }

  useNumberFormField(
    name: string,
    fieldMetadata?: FieldMetadata
  ): FormField<number> {
    if (
      !fieldMetadata?.isInteger ||
      fieldMetadata?.isBoolean ||
      fieldMetadata?.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata does not indicate a number: ${JSON.stringify(
          fieldMetadata ?? {}
        )}`
      );
    }
    return this.#useFormField<number>(name, fieldMetadata);
  }

  useBooleanFormField(
    name: string,
    fieldMetadata?: FieldMetadata
  ): FormField<boolean> {
    if (
      fieldMetadata?.isInteger ||
      !fieldMetadata?.isBoolean ||
      fieldMetadata?.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata does not indicate a boolean: ${JSON.stringify(
          fieldMetadata ?? {}
        )}`
      );
    }
    return this.#useFormField<boolean>(name, fieldMetadata);
  }

  useMultiFormField<T>(
    name: string,
    fieldMetadata?: FieldMetadata
  ): FormField<T> {
    if (
      fieldMetadata?.isInteger ||
      fieldMetadata?.isBoolean ||
      !fieldMetadata?.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata does not indicate a multi field: ${JSON.stringify(
          fieldMetadata ?? {}
        )}`
      );
    }
    return this.#useFormField<T>(name, fieldMetadata);
  }

  #useFormField<T>(name: string, fieldMetadata?: FieldMetadata): FormField<T> {
    const [stringValue, setStringValue] = useState('');
    const [autocompleteValue, setAutocompleteValue] = useState<T | null>(
      fieldMetadata?.isMultiple ? ([] as T) : null
    );
    const [error, setError] = useState('');
    const fieldRef = useRef<HTMLDivElement | HTMLButtonElement>(null);

    const formField: FormField<T> = {
      name,
      fieldRef,
      error,
      setError,
      fieldMetadata,

      textFieldParams: (
        params?: AutocompleteRenderInputParams
      ): OutlinedTextFieldProps => {
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
          ref: fieldRef as RefObject<HTMLDivElement>,
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

      checkboxParams(): CheckboxProps {
        return {
          checked: stringValue === 'true',
          size: 'small',
          name: name,
          ref: fieldRef as RefObject<HTMLButtonElement>,
          onChange: e => {
            setError('');
            setStringValue(e.target.checked ? 'true' : '');
          },
        };
      },

      autocompleteParams<
        DisableClearable extends boolean | undefined = false,
        FreeSolo extends boolean | undefined = false,
        ChipComponent extends React.ElementType = ChipTypeMap['defaultComponent']
      >(): Partial<
        AutocompleteProps<
          T extends (infer R)[] ? R : T,
          T extends (infer R)[] ? true : false,
          DisableClearable,
          FreeSolo,
          ChipComponent
        >
      > {
        type ValueType = AutocompleteValue<
          T extends (infer R)[] ? R : T,
          T extends (infer R)[] ? true : false,
          DisableClearable,
          FreeSolo
        >;
        return {
          value: autocompleteValue as ValueType,
          onChange: (e, value: ValueType) => {
            setAutocompleteValue(
              (Array.isArray(value) ? [...value] : value) as T
            );
          },
        };
      },

      getValue: () => {
        if (fieldMetadata?.isAutocomplete) {
          return autocompleteValue ?? ([] as T);
        }

        const input = getInputField(fieldRef.current);
        if (input == null || (stringValue ?? '') === '') {
          // We don't have a value to process.
          return;
        }

        if (['email', 'password', 'text', 'textarea'].includes(input.type)) {
          const trimmedValue = stringValue.trim();
          return trimmedValue.length === 0 ? undefined : (trimmedValue as T);
        }

        if (input.type === 'number') {
          const trimmedValue = stringValue.trim();
          if (NUMBER_PATTERN.exec(trimmedValue)) {
            return (
              INTEGER_PATTERN.exec(trimmedValue)
                ? parseInt(trimmedValue)
                : parseFloat(trimmedValue)
            ) as T;
          }
          return;
        }

        if (input.type === 'checkbox') {
          return (stringValue === 'true') as T;
        }

        throw new Error(`Input type '${input.type}' is not recognized.`);
      },

      setValue: (value: T | undefined) => {
        if (fieldMetadata?.isAutocomplete) {
          setAutocompleteValue(value ?? ([] as T));
        } else {
          setStringValue(String(value ?? ''));
        }
      },
    };

    this.#fields.set(name, formField as FormField<any>);

    useEffect(() => {
      this.#formChanged(formField);
    }, [stringValue]);
    useEffect(() => {
      this.#formChanged(formField);
    }, [autocompleteValue]);

    return formField;
  }

  reset() {
    this.#fields.forEach(f => f.setError(''));
    this.setValuesObject({});
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
      if (valuesMap.has(f.name)) {
        f.setValue(valuesMap.get(f.name));
      } else {
        f.setValue(f.fieldMetadata?.isMultiple ? [] : '');
      }
    });
  }

  getValuesObject() {
    const valuesMap = new Map<string, any>();

    this.#fields.forEach(f => {
      const value = f.getValue();
      if (value != null) {
        valuesMap.set(f.name, value);
      }
    });

    return Object.fromEntries(valuesMap.entries());
  }

  getValuesURLSearchParams() {
    const params = new URLSearchParams();

    this.#fields.forEach(f => {
      const value = f.getValue();
      if (value == null || value.trim() === '') {
        return;
      }
      params.append(f.name, value);
    });

    return params;
  }

  isTentativelyOkToSubmit() {
    let okToSubmit = true;
    this.#fields.forEach(f => {
      okToSubmit &&= !f.error;
      if (okToSubmit && getInputField(f.fieldRef.current)?.required) {
        okToSubmit &&= f.getValue() != null;
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

function checkFieldForErrorsAndSet(
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

function checkFieldForErrors(
  input: HTMLInputElement | HTMLTextAreaElement | undefined,
  finalCheck: boolean,
  extraErrorChecks?: FieldMetadata
): string | undefined {
  if (input == null) {
    // We don't have a field to check yet.
    return;
  }

  if (
    !['checkbox', 'email', 'number', 'password', 'text', 'textarea'].includes(
      input.type
    )
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
