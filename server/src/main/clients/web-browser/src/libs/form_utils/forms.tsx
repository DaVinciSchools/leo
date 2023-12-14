/* eslint-disable @typescript-eslint/no-explicit-any */

import {AutocompleteValue, InputAdornment} from '@mui/material';
import {CheckboxProps} from '@mui/material/Checkbox/Checkbox';
import {
  DetailedHTMLProps,
  FormHTMLAttributes,
  ReactElement,
  RefObject,
  SyntheticEvent,
  useEffect,
  useRef,
  useState,
} from 'react';
import {OutlinedTextFieldProps} from '@mui/material/TextField/TextField';
import {Visibility, VisibilityOff} from '@mui/icons-material';
import {ReactQuillProps} from 'react-quill';
import {DeepWritable} from '../misc';

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
const PASSWORDS_DO_NOT_MATCH = 'Passwords do not match.';

export interface FormFieldMetadata<
  T,
  Multiple extends boolean | undefined = false,
  FreeSolo extends boolean | undefined = false,
  MultipleT = AutocompleteValue<T, Multiple, false, FreeSolo>
> {
  isMultiple: Multiple;
  isBoolean: T extends boolean ? true : false;
  isFreeSolo: FreeSolo;
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
  isAutocomplete?: boolean;
  onChange?: (
    formField: FormField<T, Multiple, FreeSolo, MultipleT>,
    formFields: FormFields
  ) => void;
  disabled?: boolean;
}

export interface IFormAutocompleteParams<
  T,
  Multiple extends boolean | undefined = false,
  FreeSolo extends boolean | undefined = false
> {
  value: AutocompleteValue<DeepWritable<T>, Multiple, false, FreeSolo>;
  onChange: (
    event: SyntheticEvent,
    value: AutocompleteValue<DeepWritable<T>, Multiple, false, FreeSolo>
  ) => void;
  disabled: boolean;
  multiple: Multiple;
}

export interface FormField<
  T,
  Multiple extends boolean | undefined = false,
  FreeSolo extends boolean | undefined = false,
  MultipleT = AutocompleteValue<T, Multiple, false, FreeSolo>
> {
  readonly name: string;
  readonly multiple: Multiple;

  getValue: () => MultipleT;
  setValue: (value: MultipleT) => void;

  readonly stringValue: string;
  setStringValue: (value: string) => void;

  readonly error: string;
  setError: (message: string) => void;

  autocompleteParams(): IFormAutocompleteParams<T, Multiple, FreeSolo>;

  textFieldParams: (
    params?: Partial<OutlinedTextFieldProps>
  ) => OutlinedTextFieldProps;

  checkboxParams: (params?: CheckboxProps) => CheckboxProps;

  quillParams: () => ReactQuillProps;
}

interface InternalFormField<
  T,
  Multiple extends boolean | undefined = false,
  FreeSolo extends boolean | undefined = false,
  MultipleT = AutocompleteValue<T, Multiple, false, FreeSolo>
> extends FormField<T, Multiple, FreeSolo, MultipleT> {
  fieldRef: RefObject<HTMLDivElement | HTMLButtonElement>;
  fieldMetadata: FormFieldMetadata<T, Multiple, FreeSolo, MultipleT>;

  reset: () => void;

  optionallyEvaluateField: () => void;
  verifyOkOrSetError: (finalCheck: boolean) => boolean;
  calculateError: (finalCheck: boolean) => string | undefined;
}

export type FormFieldsMetadata<
  T,
  Multiple extends boolean | undefined = false,
  FreeSolo extends boolean | undefined = false,
  MultipleT = AutocompleteValue<T, Multiple, false, FreeSolo>
> = {
  onChange?: (
    formField: FormField<T, Multiple, FreeSolo, MultipleT>,
    formFields: FormFields
  ) => void;
  disabled?: boolean;
};

export interface FormFields {
  useStringFormField(
    name: string,
    fieldMetadata?: Partial<FormFieldMetadata<string>>
  ): FormField<string>;

  useNumberFormField(
    name: string,
    fieldMetadata?: Partial<FormFieldMetadata<number>>
  ): FormField<number>;

  useBooleanFormField(
    name: string,
    fieldMetadata?: Partial<FormFieldMetadata<boolean>>
  ): FormField<boolean>;

  useAutocompleteFormField<
    T,
    Multiple extends boolean | undefined = false,
    FreeSolo extends boolean | undefined = false,
    MultipleT = AutocompleteValue<T, Multiple, false, FreeSolo>
  >(
    name: string,
    fieldMetadata?: Partial<FormFieldMetadata<T, Multiple, FreeSolo, MultipleT>>
  ): FormField<T, Multiple, FreeSolo, MultipleT>;

  formParams(): DetailedHTMLProps<
    FormHTMLAttributes<HTMLFormElement>,
    HTMLFormElement
  >;

  reset(): void;

  verifyOk(finalCheck: boolean): boolean;

  setValuesObject(values: {} | null | undefined): void;

  getValuesObject<O extends {}>(
    includeNulls?: boolean,
    startWithObject?: O | null | undefined
  ): O;

  getValuesURLSearchParams(): URLSearchParams;
}

export function useFormFields(
  formFieldsMetadata?: FormFieldsMetadata<any, any, any, any>
): FormFields {
  const fields: Map<string, InternalFormField<any, any, any, any>> = new Map();
  const formRef = useRef<HTMLFormElement>(null);
  const [showPasswords, setShowPasswords] = useState(false);
  const [evaluateAllFields, setEvaluateAllFields] = useState(false);

  function useStringFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata<string>
  ): FormField<string> {
    fieldMetadata = {...fieldMetadata} as FormFieldMetadata<string>;
    if (
      fieldMetadata.isInteger ||
      fieldMetadata.isBoolean ||
      fieldMetadata.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata for ${name} does not indicate a string: ${JSON.stringify(
          fieldMetadata
        )}`
      );
    }
    return useFormField<string>(name, fieldMetadata ?? {});
  }

  function useNumberFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata<number>
  ): FormField<number> {
    fieldMetadata = {...fieldMetadata} as FormFieldMetadata<number>;
    fieldMetadata.isInteger = fieldMetadata.isInteger ?? {};
    if (
      !fieldMetadata.isInteger ||
      fieldMetadata.isBoolean ||
      fieldMetadata.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata for ${name} does not indicate a number: ${JSON.stringify(
          fieldMetadata
        )}`
      );
    }
    return useFormField<number>(name, fieldMetadata ?? {});
  }

  function useBooleanFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata<boolean>
  ): FormField<boolean> {
    fieldMetadata = {...fieldMetadata} as FormFieldMetadata<boolean>;
    fieldMetadata.isBoolean = fieldMetadata.isBoolean ?? true;
    if (
      fieldMetadata.isInteger ||
      !fieldMetadata.isBoolean ||
      fieldMetadata.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata for ${name} does not indicate a boolean: ${JSON.stringify(
          fieldMetadata
        )}`
      );
    }
    return useFormField<boolean>(name, fieldMetadata);
  }

  function useAutocompleteFormField<
    T,
    Multiple extends boolean | undefined = false,
    FreeSolo extends boolean | undefined = false,
    MultipleT = AutocompleteValue<T, Multiple, false, FreeSolo>
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<T, Multiple, FreeSolo, MultipleT>
  ): FormField<T, Multiple, FreeSolo, MultipleT> {
    fieldMetadata = {...fieldMetadata} as FormFieldMetadata<
      T,
      Multiple,
      FreeSolo,
      MultipleT
    >;
    fieldMetadata.isAutocomplete = fieldMetadata.isAutocomplete ?? true;
    if (
      fieldMetadata.isInteger ||
      fieldMetadata.isBoolean ||
      !fieldMetadata.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata for ${name} does not indicate an autocomplete: ${JSON.stringify(
          fieldMetadata
        )}`
      );
    }
    return useFormField<T, Multiple, FreeSolo, MultipleT>(name, fieldMetadata);
  }

  function useFormField<
    T,
    Multiple extends boolean | undefined = false,
    FreeSolo extends boolean | undefined = false,
    MultipleT = AutocompleteValue<T, Multiple, false, FreeSolo>
  >(
    name: string,
    fieldMetadata: FormFieldMetadata<T, Multiple, FreeSolo, MultipleT>
  ): FormField<T, Multiple, FreeSolo, MultipleT> {
    const [stringValue, setStringValue] = useState('');
    const [autocompleteValue, setAutocompleteValue] = useState(
      (fieldMetadata.isMultiple ? [] : null) as MultipleT
    );

    const [error, setError] = useState('');
    const [evaluateField, setEvaluateField] = useState(false);
    const fieldRef = useRef<HTMLDivElement | HTMLButtonElement>(null);

    useEffect(() => {
      fieldMetadata.onChange?.(formField, formFields);
      formFieldsMetadata?.onChange?.(formField, formFields);
    }, [stringValue, autocompleteValue]);

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
      if (fieldMetadata.isBoolean) {
        setStringValue('off');
      } else {
        setStringValue('');
      }
      if (fieldMetadata.isMultiple) {
        setAutocompleteValue([] as MultipleT);
      } else {
        setAutocompleteValue(null as MultipleT);
      }
    }

    function getType() {
      return fieldMetadata.isPassword && !showPasswords
        ? 'password'
        : fieldMetadata.isEmail
        ? 'email'
        : fieldMetadata.isInteger != null
        ? 'number'
        : fieldMetadata.isBoolean
        ? 'checkbox'
        : 'text';
    }

    function textFieldParams(
      params?: Partial<OutlinedTextFieldProps>
    ): OutlinedTextFieldProps {
      return {
        ...(params ?? {}),
        value: stringValue,
        type: getType(),
        variant: 'outlined',
        fullWidth: true,
        size: 'small',
        name: name,
        ref: fieldRef as RefObject<HTMLDivElement>,
        helperText: error,
        error: !!error,
        onChange: e => {
          setStringValue(e.target.value);
          params?.onChange?.(e);
        },
        onBlur: e => {
          setStringValue(e.target.value);
          setEvaluateField(true);
        },
        InputProps: {
          ...(params?.InputProps ?? {}),
          startAdornment: fieldMetadata.startIcon ? (
            <InputAdornment position="start" style={{cursor: 'not-allowed'}}>
              {fieldMetadata.startIcon}
            </InputAdornment>
          ) : (
            params?.InputProps?.startAdornment
          ),
          endAdornment: fieldMetadata.isPassword ? (
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
            fieldMetadata.isPassword &&
            fieldMetadata.isPassword?.skipPasswordCheck !== true
              ? MIN_PASSWORD_LENGTH
              : undefined,
          maxLength:
            fieldMetadata.maxLength ??
            (fieldMetadata.isZipCode
              ? MAX_ZIP_CODE_LENGTH
              : fieldMetadata.isPassword &&
                fieldMetadata.isPassword?.skipPasswordCheck !== true
              ? MAX_PASSWORD_LENGTH
              : undefined),
          min: fieldMetadata.isInteger?.min,
          max: fieldMetadata.isInteger?.max,
        },
        disabled:
          formFieldsMetadata?.disabled === true ||
          fieldMetadata.disabled === true,
      } as OutlinedTextFieldProps;
    }

    function checkboxParams(): CheckboxProps {
      return {
        checked: stringValue === 'on',
        size: 'small',
        name: name,
        ref: fieldRef as RefObject<HTMLButtonElement>,
        onChange: e => {
          setStringValue(e.target.checked ? 'on' : 'off');
        },
        disabled:
          formFieldsMetadata?.disabled === true ||
          fieldMetadata.disabled === true,
      };
    }

    function autocompleteParams() {
      return {
        multiple: fieldMetadata.isMultiple,
        freeSolo: fieldMetadata.isFreeSolo,
        disableCloseOnSelect: fieldMetadata.isMultiple,
        // The value must be modifiable, even though it doesn't modify anything.
        value: autocompleteValue as AutocompleteValue<
          DeepWritable<T>,
          Multiple,
          false,
          FreeSolo
        >,
        onChange: (
          _: SyntheticEvent,
          value: AutocompleteValue<DeepWritable<T>, Multiple, false, FreeSolo>
        ) => {
          setAutocompleteValue(value as MultipleT);
        },
        disabled:
          formFieldsMetadata?.disabled === true ||
          fieldMetadata.disabled === true,
      } as IFormAutocompleteParams<T, Multiple, FreeSolo>;
    }

    function quillParams(): ReactQuillProps {
      return {
        value: stringValue,
        onChange: (value: string) => {
          setStringValue(value);
        },
        preserveWhitespace: true,
        readOnly:
          formFieldsMetadata?.disabled === true ||
          fieldMetadata.disabled === true,
      };
    }

    function getValue(): MultipleT {
      if (fieldMetadata.isAutocomplete) {
        return autocompleteValue as MultipleT;
      }

      const type = getType();
      const trimmedValue = stringValue?.trim() ?? '';

      if (type === 'checkbox' || fieldMetadata.isBoolean) {
        return (trimmedValue === 'on') as MultipleT;
      }

      if (['email', 'password', 'text', 'textarea'].includes(type)) {
        return trimmedValue as MultipleT;
      }

      if (type === 'number') {
        if (NUMBER_PATTERN.exec(trimmedValue)) {
          return (
            INTEGER_PATTERN.exec(trimmedValue)
              ? parseInt(trimmedValue)
              : parseFloat(trimmedValue)
          ) as MultipleT;
        }
        return 0 as MultipleT;
      }

      throw new Error(`Input type '${type}' is not recognized.`);
    }

    function setValue(value: MultipleT) {
      if (fieldMetadata.isAutocomplete) {
        setStringValue('');
        setAutocompleteValue(value as MultipleT);
      } else if (fieldMetadata.isBoolean) {
        setStringValue(value ? 'on' : 'off');
      } else {
        setStringValue(value == null ? '' : String(value));
      }
    }

    function verifyOkOrSetError(finalCheck: boolean): boolean {
      const error = calculateError(finalCheck);
      if (error) {
        setError(error);
      } else if (
        fieldMetadata.isPassword &&
        fieldMetadata.isPassword?.skipPasswordCheck !== true &&
        getUnmatchedPasswords(finalCheck).length > 0
      ) {
        setError(PASSWORDS_DO_NOT_MATCH);
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

      if (
        (input.type === 'password' || fieldMetadata.isPassword) &&
        fieldMetadata.isPassword?.skipPasswordCheck !== true &&
        stringValue !== '' &&
        !PASSWORD_PATTERN.exec(stringValue)
      ) {
        return PASSWORD_ERROR_MESSAGE;
      }

      if (finalCheck) {
        if (input.required && stringValue.trim() === '') {
          return 'This field is required.';
        }
        if (
          input.minLength > 0 &&
          stringValue.length < input.minLength &&
          stringValue !== ''
        ) {
          return `This field must have at least ${input.minLength} character(s).`;
        }
        if (
          input.maxLength >= 0 &&
          stringValue.length > input.maxLength &&
          stringValue !== ''
        ) {
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
          fieldMetadata.isInteger &&
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
        fieldMetadata.isZipCode &&
        ZIP_CODE_PATTERN.exec(stringValue) == null
      ) {
        return 'This field must be 5 digits optionally followed by a dash and 4 digits.';
      }
      return;
    }

    const formField: InternalFormField<T, Multiple, FreeSolo, MultipleT> = {
      name,
      multiple: fieldMetadata.isMultiple,

      getValue,
      setValue,

      stringValue,
      setStringValue,

      error,
      setError,

      autocompleteParams,
      textFieldParams,
      checkboxParams,
      quillParams,

      fieldRef,
      fieldMetadata,

      reset,

      optionallyEvaluateField,
      verifyOkOrSetError,
      calculateError,
    };

    fields.set(name, formField);

    return formField;
  }

  function formParams(): DetailedHTMLProps<
    FormHTMLAttributes<HTMLFormElement>,
    HTMLFormElement
  > {
    return {ref: formRef};
  }

  function reset() {
    fields.forEach(f => f.reset());
  }

  function getUnmatchedPasswords(finalCheck: boolean) {
    const passwordFields: InternalFormField<string>[] = [];
    const passwordInputs: (
      | HTMLInputElement
      | HTMLTextAreaElement
      | undefined
    )[] = [];

    for (const field of fields.values()) {
      if (
        field.fieldMetadata.isPassword != null &&
        field?.fieldMetadata.isPassword?.skipPasswordCheck !== true
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

    const passwordFields = getUnmatchedPasswords(finalCheck);
    if (passwordFields.length > 0) {
      errorField = passwordFields[0];
      passwordFields.forEach(f => f.setError(PASSWORDS_DO_NOT_MATCH));
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
      }
    });
  }

  function getValuesObject<O extends {}>(
    includeNulls?: boolean,
    startWithObject?: O | null | undefined
  ): O {
    const object = (startWithObject ?? {}) as {[key: string]: any};
    fields.forEach(f => {
      const value = f.getValue();
      if (value != null || includeNulls === true) {
        object[f.name] = value;
      }
    });
    return object as O;
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
    formParams,
    reset,
    verifyOk,
    setValuesObject,
    getValuesObject,
    getValuesURLSearchParams,
  };
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

export function filterAutocompleteFormField<T, ID>(
  formField: FormField<T, true>,
  toId: (idFunction: T) => ID,
  options: T[]
) {
  const idOptions = new Set(options.map(o => toId(o)));
  const filteredOptions =
    formField.getValue()?.filter?.(o => idOptions.has(toId(o))) ?? [];
  formField.setValue(filteredOptions);
}
