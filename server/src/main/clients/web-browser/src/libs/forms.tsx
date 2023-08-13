/* eslint-disable @typescript-eslint/no-explicit-any */

import {AutocompleteRenderInputParams, InputAdornment} from '@mui/material';
import {CheckboxProps} from '@mui/material/Checkbox/Checkbox';
import {
  DetailedHTMLProps,
  FormHTMLAttributes,
  ReactElement,
  RefObject,
  useEffect,
  useRef,
  useState,
} from 'react';
import {OutlinedTextFieldProps} from '@mui/material/TextField/TextField';
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
const PASSWORDS_DONT_MATCH = 'Passwords do not match.';

export interface FormFieldMetadata {
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
  isAutocomplete?: {
    isMultiple?: boolean;
  };
}

export interface FormField<T> {
  readonly name: string;

  getValue: () => T | undefined;
  setValue: (value: T | undefined) => void;

  readonly error: string;
  setError: (message: string) => void;

  autocompleteParams(): {
    value: T;
    onChange: (event: React.SyntheticEvent, value: T) => void;
  };

  textFieldParams: (
    params?: AutocompleteRenderInputParams
  ) => OutlinedTextFieldProps;

  checkboxParams: (params?: CheckboxProps) => CheckboxProps;
}

interface InternalFormField<T> extends FormField<T> {
  fieldRef: RefObject<HTMLDivElement | HTMLButtonElement>;
  fieldMetadata?: FormFieldMetadata;

  reset: () => void;

  optionallyEvaluateField: () => void;
  verifyOkOrSetError: (finalCheck: boolean) => boolean;
  calculateError: (finalCheck: boolean) => string | undefined;
}

export type FormFieldsMetadata = {
  onChange?: (formFields: FormFields, formField: FormField<any>) => void;
};

interface FormFields {
  useStringFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata
  ): FormField<string>;

  useNumberFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata
  ): FormField<number>;

  useBooleanFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata
  ): FormField<boolean>;

  useAutocompleteFormField<T>(
    name: string,
    fieldMetadata?: FormFieldMetadata
  ): FormField<T>;

  params(): DetailedHTMLProps<
    FormHTMLAttributes<HTMLFormElement>,
    HTMLFormElement
  >;

  reset(): void;

  verifyOk(finalCheck: boolean): boolean;

  setValuesObject(values: {} | null | undefined): void;

  getValuesObject(): {
    [p: string]: any;
  };

  getValuesURLSearchParams(): URLSearchParams;
}

export function useFormFields(
  formFieldsMetadata?: FormFieldsMetadata
): FormFields {
  const fields: Map<string, InternalFormField<any>> = new Map();
  const formRef = useRef<HTMLFormElement>(null);
  const [showPasswords, setShowPasswords] = useState(false);
  const [evaluateAllFields, setEvaluateAllFields] = useState(false);

  function useStringFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata
  ): FormField<string> {
    if (
      fieldMetadata?.isInteger ||
      fieldMetadata?.isBoolean ||
      fieldMetadata?.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata for ${name} does not indicate a string: ${JSON.stringify(
          fieldMetadata ?? {}
        )}`
      );
    }
    return useFormField<string>(name, fieldMetadata);
  }

  function useNumberFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata
  ): FormField<number> {
    fieldMetadata = {...fieldMetadata};
    fieldMetadata.isInteger = fieldMetadata.isInteger ?? {};
    if (
      !fieldMetadata?.isInteger ||
      fieldMetadata?.isBoolean ||
      fieldMetadata?.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata for ${name} does not indicate a number: ${JSON.stringify(
          fieldMetadata ?? {}
        )}`
      );
    }
    return useFormField<number>(name, fieldMetadata);
  }

  function useBooleanFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata
  ): FormField<boolean> {
    fieldMetadata = {...fieldMetadata};
    fieldMetadata.isBoolean = true;
    if (
      fieldMetadata?.isInteger ||
      !fieldMetadata?.isBoolean ||
      fieldMetadata?.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata for ${name} does not indicate a boolean: ${JSON.stringify(
          fieldMetadata ?? {}
        )}`
      );
    }
    return useFormField<boolean>(name, fieldMetadata);
  }

  function useAutocompleteFormField<T>(
    name: string,
    fieldMetadata?: FormFieldMetadata
  ): FormField<T> {
    fieldMetadata = {...fieldMetadata};
    fieldMetadata.isAutocomplete = fieldMetadata.isAutocomplete ?? {};
    if (
      fieldMetadata?.isInteger ||
      fieldMetadata?.isBoolean ||
      !fieldMetadata?.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata for ${name} does not indicate a string: ${JSON.stringify(
          fieldMetadata ?? {}
        )}`
      );
    }
    return useFormField<T>(name, fieldMetadata);
  }

  function useFormField<T>(
    name: string,
    fieldMetadata?: FormFieldMetadata
  ): FormField<T> {
    const [stringValue, setStringValue] = useState('');
    const [autocompleteValue, setAutocompleteValue] = useState<T>(
      fieldMetadata?.isAutocomplete?.isMultiple ? ([] as T) : ('' as T)
    );
    const [error, setError] = useState('');
    const [evaluateField, setEvaluateField] = useState(false);
    const fieldRef = useRef<HTMLDivElement | HTMLButtonElement>(null);

    useEffect(() => {
      fields.forEach(f => f.optionallyEvaluateField());
    }, [
      stringValue,
      autocompleteValue,
      fieldRef.current,
      evaluateAllFields,
      evaluateField,
    ]);

    function optionallyEvaluateField() {
      if (evaluateAllFields || evaluateField) {
        setError('');
        verifyOkOrSetError(true);
      }
    }

    function reset() {
      setError('');
      setEvaluateField(false);
      setStringValue('');
      if (fieldMetadata?.isAutocomplete?.isMultiple) {
        setAutocompleteValue([] as T);
      }
    }

    function textFieldParams(
      params?: AutocompleteRenderInputParams
    ): OutlinedTextFieldProps {
      return {
        ...(params ?? {}),
        value: stringValue,
        type:
          fieldMetadata?.isPassword && !showPasswords
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
          setStringValue(e.target.value);
        },
        onBlur: e => {
          setEvaluateField(true);
          setStringValue(e.target.value);
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
            showPasswords ? (
              <Visibility
                onClick={() => setShowPasswords(false)}
                style={{cursor: 'pointer'}}
              />
            ) : (
              <VisibilityOff
                onClick={() => setShowPasswords(true)}
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
      } as OutlinedTextFieldProps;
    }

    function checkboxParams(): CheckboxProps {
      return {
        checked: stringValue === 'true',
        size: 'small',
        name: name,
        ref: fieldRef as RefObject<HTMLButtonElement>,
        onChange: e => {
          setStringValue(e.target.checked ? 'true' : '');
        },
      };
    }

    function autocompleteParams() {
      return {
        value: autocompleteValue,
        onChange: (e: React.SyntheticEvent, value: T) => {
          if (fieldMetadata?.isAutocomplete?.isMultiple) {
            setAutocompleteValue(value as T);
          } else {
            setStringValue(String(value ?? ''));
          }
        },
      };
    }

    function getValue() {
      if (fieldMetadata?.isAutocomplete?.isMultiple) {
        return autocompleteValue ?? ([] as T);
      }

      const input = getInputField(fieldRef.current);
      if (input == null) {
        // We don't have a value to process.
        return;
      }

      if (stringValue !== input.value) {
        console.warn(
          `Field value is out of sync from getValue(): '${stringValue}' !== '${input.value}'.`
        );
        setStringValue(input.value);
        return;
      }

      const trimmedValue = stringValue.trim();
      if (trimmedValue === '') {
        return;
      }

      if (['email', 'password', 'text', 'textarea'].includes(input.type)) {
        return trimmedValue as T;
      }

      if (input.type === 'number') {
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
        return (trimmedValue === 'true') as T;
      }

      throw new Error(`Input type '${input.type}' is not recognized.`);
    }

    function setValue(value: T | undefined) {
      if (fieldMetadata?.isAutocomplete?.isMultiple) {
        setAutocompleteValue(value ?? ([] as T));
      }
    }

    function verifyOkOrSetError(finalCheck: boolean): boolean {
      const error = calculateError(finalCheck);
      if (error) {
        setError(error);
      } else if (
        fieldMetadata?.isPassword &&
        verifyPasswordsMatch(finalCheck).length > 0
      ) {
        setError(PASSWORDS_DONT_MATCH);
      }
      return !error;
    }

    function calculateError(finalCheck: boolean): string | undefined {
      const input = getInputField(fieldRef.current);
      if (input == null) {
        // We don't have a field to check yet.
        return;
      }

      if (
        ![
          'checkbox',
          'email',
          'number',
          'password',
          'text',
          'textarea',
        ].includes(input.type)
      ) {
        throw new Error(`Input type '${input.type}' is not recognized.`);
      }

      if (stringValue !== input.value) {
        console.warn(
          `Field value is out of sync from calculateError(): '${stringValue}' !== '${input.value}'.`
        );
        setStringValue(input.value);
        return;
      }

      if (
        (input.type === 'password' || fieldMetadata?.isPassword) &&
        fieldMetadata?.isPassword?.skipPasswordCheck !== true &&
        !PASSWORD_PATTERN.exec(stringValue)
      ) {
        return PASSWORD_ERROR_MESSAGE;
      }

      if (finalCheck) {
        if (input.required && stringValue.trim() === '') {
          return 'This field is required.';
        }
        if (input.minLength > 0 && stringValue.length < input.minLength) {
          return `This field must have at least ${input.minLength} character(s).`;
        }
        if (input.maxLength >= 0 && stringValue.length > input.maxLength) {
          return `This field must have less than ${input.maxLength} character(s).`;
        }
      }

      if (stringValue.trim() === '' && (!input.required || !finalCheck)) {
        return;
      }

      if (input.type === 'number') {
        if (NUMBER_PATTERN.exec(stringValue) == null) {
          return 'This field must be a number.';
        }
        if (
          fieldMetadata?.isInteger &&
          INTEGER_PATTERN.exec(stringValue) == null
        ) {
          return 'This field must be an integer.';
        }
        const value = getValue() as number;
        if (
          'min' in input &&
          input.min.length > 0 &&
          (INTEGER_PATTERN.exec(input.min) != null
            ? parseInt(input.min)
            : parseFloat(input.min)) > value
        ) {
          return `This field must be greater than or equal to ${input.min}.`;
        }
        if (
          'max' in input &&
          input.max.length > 0 &&
          (INTEGER_PATTERN.exec(input.max) != null
            ? parseInt(input.max)
            : parseFloat(input.max)) < value
        ) {
          return `This field must be less than or equal to ${input.max}.`;
        }
      }
      if (input.type === 'email' && EMAIL_PATTERN.exec(stringValue) == null) {
        return 'This field is not a valid e-mail address.';
      }
      if (
        fieldMetadata?.isZipCode &&
        ZIP_CODE_PATTERN.exec(stringValue) == null
      ) {
        return 'This field must be 5 digits optionally followed by a dash and 4 digits.';
      }
      return;
    }

    const formField: InternalFormField<T> = {
      name,

      getValue,
      setValue,

      error,
      setError,

      autocompleteParams,
      textFieldParams,
      checkboxParams,

      fieldRef,
      fieldMetadata,

      reset,

      optionallyEvaluateField,
      verifyOkOrSetError,
      calculateError,
    };

    fields.set(name, formField);

    useEffect(() => {
      formChanged(formField);
    }, [stringValue]);

    useEffect(() => {
      formChanged(formField);
    }, [autocompleteValue]);

    return formField;
  }

  function params(): DetailedHTMLProps<
    FormHTMLAttributes<HTMLFormElement>,
    HTMLFormElement
  > {
    return {ref: formRef};
  }

  function reset() {
    setValuesObject({});
  }

  function verifyPasswordsMatch(finalCheck: boolean) {
    const passwordFields: InternalFormField<string>[] = [];
    const passwordInputs: (
      | HTMLInputElement
      | HTMLTextAreaElement
      | undefined
    )[] = [];

    for (const field of fields.values()) {
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
      return passwordFields;
    }

    return [];
  }

  function verifyOk(finalCheck: boolean) {
    fields.forEach(f => f.setError(''));
    setEvaluateAllFields(evaluateAllFields || finalCheck);

    let errorField: InternalFormField<any> | undefined;

    const passwordFields = verifyPasswordsMatch(finalCheck);
    if (passwordFields.length > 0) {
      errorField = passwordFields[0];
      passwordFields.forEach(f => f.setError(PASSWORDS_DONT_MATCH));
    }

    fields.forEach(f => {
      if (!f.verifyOkOrSetError(finalCheck)) {
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

  function setValuesObject(values: {} | null | undefined) {
    setEvaluateAllFields(false);
    fields.forEach(f => f.reset());

    const valuesMap = new Map(Object.entries(values ?? {}));

    fields.forEach(f => {
      if (valuesMap.has(f.name)) {
        f.setValue(valuesMap.get(f.name));
      } else {
        f.setValue(f.fieldMetadata?.isAutocomplete?.isMultiple ? [] : '');
      }
    });
  }

  function getValuesObject() {
    const valuesMap = new Map<string, any>();

    fields.forEach(f => {
      const value = f.getValue();
      if (value != null) {
        valuesMap.set(f.name, value);
      }
    });

    return Object.fromEntries(valuesMap.entries());
  }

  function getValuesURLSearchParams() {
    const params = new URLSearchParams();

    fields.forEach(f => {
      const value = f.getValue();
      if (value == null || value.trim() === '') {
        return;
      }
      params.append(f.name, value);
    });

    return params;
  }

  const formFields: FormFields = {
    useStringFormField,
    useNumberFormField,
    useBooleanFormField,
    useAutocompleteFormField,
    params,
    reset,
    verifyOk,
    setValuesObject,
    getValuesObject,
    getValuesURLSearchParams,
  };

  function formChanged(formField: FormField<any>) {
    if (formFieldsMetadata && formFieldsMetadata.onChange) {
      formFieldsMetadata.onChange(formFields, formField);
    }
  }

  return formFields;
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
