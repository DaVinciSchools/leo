/* eslint-disable @typescript-eslint/no-explicit-any */

import {
  AutocompleteProps,
  AutocompleteValue,
  InputAdornment,
} from '@mui/material';
import {CheckboxProps} from '@mui/material/Checkbox/Checkbox';
import {
  DetailedHTMLProps,
  ElementType,
  FormHTMLAttributes,
  ReactElement,
  RefObject,
  useEffect,
  useRef,
  useState,
} from 'react';
import {OutlinedTextFieldProps} from '@mui/material/TextField/TextField';
import {Visibility, VisibilityOff} from '@mui/icons-material';
import {HtmlEditorProps} from '../HtmlEditor/HtmlEditor';
import {ChipTypeMap} from '@mui/material/Chip';
import {deepClone, DeepReadOnly} from '../misc';

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

export const DEFAULT_PAGE_SIZE = 10;

export interface FormFieldMetadata<
  T,
  Multiple extends boolean | undefined = false,
  DisableClearable extends boolean | undefined = false,
  FreeSolo extends boolean | undefined = false,
  ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
> {
  isPassword?: {
    skipPasswordCheck?: boolean;
  };
  isInteger?: {
    min?: number;
    max?: number;
  };
  isAlwaysEditing?: boolean;
  isBoolean?: boolean;
  isEmail?: boolean;
  isZipCode?: boolean;
  startIcon?: ReactElement;
  maxLength?: number;
  isAutocomplete?: {
    isMultiple?: Multiple;
    isFreeSolo?: FreeSolo;
  };
  onChange?: (
    formField: FormField<
      T,
      Multiple,
      DisableClearable,
      FreeSolo,
      ChipComponent
    >,
    formFields: FormFields
  ) => void;
  disabled?: boolean;
}

export interface FormField<
  T,
  Multiple extends boolean | undefined = false,
  DisableClearable extends boolean | undefined = false,
  FreeSolo extends boolean | undefined = false,
  ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
> {
  readonly name: string;
  readonly disabled: boolean;
  readonly multiple: Multiple;
  readonly freeSolo: FreeSolo;

  getValue: () => Multiple extends true ? T[] : T | undefined;
  setValue: (value: Multiple extends true ? T[] : T | undefined) => void;

  readonly stringValue: string;
  setStringValue: (value: string) => void;

  readonly error: string;
  setError: (message: string) => void;

  reset: () => void;

  getTextFieldParams: (
    params?: Partial<OutlinedTextFieldProps>
  ) => Partial<OutlinedTextFieldProps>;

  getHtmlEditorParams: (
    params?: Partial<HtmlEditorProps>
  ) => Partial<HtmlEditorProps>;

  getCheckboxParams: (
    params?: Partial<CheckboxProps>
  ) => Partial<CheckboxProps>;

  getAutocompleteParams: (
    params?: Partial<
      AutocompleteProps<T, Multiple, DisableClearable, FreeSolo, ChipComponent>
    >
  ) => Partial<
    AutocompleteProps<T, Multiple, DisableClearable, FreeSolo, ChipComponent>
  >;
}

interface InternalFormField<
  T,
  Multiple extends boolean | undefined = false,
  DisableClearable extends boolean | undefined = false,
  FreeSolo extends boolean | undefined = false,
  ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
> extends FormField<T, Multiple, DisableClearable, FreeSolo, ChipComponent> {
  fieldRef: RefObject<HTMLDivElement | HTMLButtonElement | undefined>;

  fieldMetadata?:
    | FormFieldMetadata<T, Multiple, DisableClearable, FreeSolo, ChipComponent>
    | undefined;

  reset: () => void;

  optionallyEvaluateField: () => void;
  verifyOkOrSetError: (finalCheck: boolean) => boolean;
  calculateError: (finalCheck: boolean) => string | undefined;
}

export type FormFieldsMetadata = {
  onChange?: (
    formField: FormField<any, any, any, any, any>,
    formFields: FormFields
  ) => void;
  disabled?: boolean;
};

export interface FormFields {
  useStringFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata<string>
  ): FormField<string>;

  useNumberFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata<number>
  ): FormField<number>;

  useBooleanFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata<boolean>
  ): FormField<boolean>;

  useAutocompleteFormField<
    T,
    Multiple extends boolean | undefined = false,
    DisableClearable extends boolean | undefined = false,
    FreeSolo extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    multiple: Multiple,
    freeSolo: FreeSolo,
    fieldMetadata?: FormFieldMetadata<
      T,
      Multiple,
      DisableClearable,
      FreeSolo,
      ChipComponent
    >
  ): FormField<T, Multiple, DisableClearable, FreeSolo, ChipComponent>;

  useSingleAutocompleteFormField<
    T,
    DisableClearable extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<
      T,
      false,
      DisableClearable,
      false,
      ChipComponent
    >
  ): FormField<T, false, DisableClearable, false, ChipComponent>;

  useSingleFreeSoloAutocompleteFormField<
    DisableClearable extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<
      string,
      false,
      DisableClearable,
      true,
      ChipComponent
    >
  ): FormField<string, false, DisableClearable, true, ChipComponent>;

  useMultipleAutocompleteFormField<
    T,
    DisableClearable extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<
      T,
      true,
      DisableClearable,
      false,
      ChipComponent
    >
  ): FormField<T, true, DisableClearable, false, ChipComponent>;

  useMultipleFreeSoloAutocompleteFormField<
    T,
    DisableClearable extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<
      T,
      true,
      DisableClearable,
      true,
      ChipComponent
    >
  ): FormField<T, true, DisableClearable, true, ChipComponent>;

  getFormParams(): DetailedHTMLProps<
    FormHTMLAttributes<HTMLFormElement>,
    HTMLFormElement
  >;

  reset(): void;

  verifyOk(finalCheck: boolean): boolean;

  setValuesObject(values: {} | null | undefined): void;

  getValuesObject<O extends {}>(
    includeNulls?: boolean,
    startWithObject?: DeepReadOnly<O> | null | undefined
  ): O;

  getValuesURLSearchParams(): URLSearchParams;
}

export function useFormFields(
  formFieldsMetadata?: FormFieldsMetadata
): FormFields {
  const fields: Map<
    string,
    InternalFormField<any, any, any, any, any>
  > = new Map();
  const formRef = useRef<HTMLFormElement>(null);
  const [showPasswords, setShowPasswords] = useState(false);
  const [evaluateAllFields, setEvaluateAllFields] = useState(false);

  function useStringFormField(
    name: string,
    fieldMetadata?: FormFieldMetadata<string>
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
    fieldMetadata?: FormFieldMetadata<number>
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
    fieldMetadata?: FormFieldMetadata<boolean>
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

  function useAutocompleteFormField<
    T,
    Multiple extends boolean | undefined = false,
    DisableClearable extends boolean | undefined = false,
    FreeSolo extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    multiple: Multiple,
    freeSolo: FreeSolo,
    fieldMetadata?: FormFieldMetadata<
      T,
      Multiple,
      DisableClearable,
      FreeSolo,
      ChipComponent
    >
  ): FormField<T, Multiple, DisableClearable, FreeSolo, ChipComponent> {
    fieldMetadata = {...fieldMetadata};
    fieldMetadata.isAutocomplete = fieldMetadata.isAutocomplete ?? {};
    fieldMetadata.isAutocomplete.isMultiple = multiple;
    fieldMetadata.isAutocomplete.isFreeSolo = freeSolo;

    if (
      fieldMetadata?.isInteger ||
      fieldMetadata?.isBoolean ||
      !fieldMetadata?.isAutocomplete
    ) {
      throw new Error(
        `fieldMetadata for ${name} does not indicate an autocomplete: ${JSON.stringify(
          fieldMetadata ?? {}
        )}`
      );
    }
    return useFormField<T, Multiple, DisableClearable, FreeSolo, ChipComponent>(
      name,
      fieldMetadata
    );
  }

  function useSingleAutocompleteFormField<
    T,
    DisableClearable extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<
      T,
      false,
      DisableClearable,
      false,
      ChipComponent
    >
  ): FormField<T, false, DisableClearable, false, ChipComponent> {
    return useAutocompleteFormField(name, false, false, fieldMetadata);
  }

  function useSingleFreeSoloAutocompleteFormField<
    DisableClearable extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<
      string,
      false,
      DisableClearable,
      true,
      ChipComponent
    >
  ): FormField<string, false, DisableClearable, true, ChipComponent> {
    return useAutocompleteFormField(name, false, true, fieldMetadata);
  }

  function useMultipleAutocompleteFormField<
    T,
    DisableClearable extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<
      T,
      true,
      DisableClearable,
      false,
      ChipComponent
    >
  ): FormField<T, true, DisableClearable, false, ChipComponent> {
    return useAutocompleteFormField(name, true, false, fieldMetadata);
  }

  function useMultipleFreeSoloAutocompleteFormField<
    T,
    DisableClearable extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<
      T,
      true,
      DisableClearable,
      true,
      ChipComponent
    >
  ): FormField<T, true, DisableClearable, true, ChipComponent> {
    return useAutocompleteFormField(name, true, true, fieldMetadata);
  }

  function useFormField<
    T,
    Multiple extends boolean | undefined = false,
    DisableClearable extends boolean | undefined = false,
    FreeSolo extends boolean | undefined = false,
    ChipComponent extends ElementType = ChipTypeMap['defaultComponent']
  >(
    name: string,
    fieldMetadata?: FormFieldMetadata<
      T,
      Multiple,
      DisableClearable,
      FreeSolo,
      ChipComponent
    >
  ): FormField<T, Multiple, DisableClearable, FreeSolo, ChipComponent> {
    const [stringValue, setStringValue] = useState('');
    const [autocompleteValue, setAutocompleteValue] = useState<
      Multiple extends true ? T[] : T | undefined
    >(
      (fieldMetadata?.isAutocomplete?.isMultiple
        ? []
        : undefined) as Multiple extends true ? T[] : T | undefined
    );
    const [error, setError] = useState('');
    const [evaluateField, setEvaluateField] = useState(false);
    const fieldRef = useRef<HTMLDivElement | HTMLButtonElement>();

    useEffect(() => {
      fieldMetadata?.onChange?.(formField, formFields);
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
      setStringValue('');
      if (fieldMetadata?.isAutocomplete) {
        if (fieldMetadata?.isAutocomplete?.isMultiple) {
          setAutocompleteValue(
            [] as Multiple extends true ? T[] : T | undefined
          );
        } else {
          setAutocompleteValue(
            undefined as Multiple extends true ? T[] : T | undefined
          );
        }
      } else if (fieldMetadata?.isBoolean) {
        setStringValue('off');
      }
    }

    function getType() {
      return fieldMetadata?.isPassword && !showPasswords
        ? 'password'
        : fieldMetadata?.isEmail
        ? 'email'
        : fieldMetadata?.isInteger != null
        ? 'number'
        : fieldMetadata?.isBoolean
        ? 'checkbox'
        : 'text';
    }

    function getTextFieldParams(
      params?: Partial<OutlinedTextFieldProps>
    ): Partial<OutlinedTextFieldProps> {
      return {
        ...(params ?? {}),
        type: getType(),
        variant: 'outlined',
        fullWidth: true,
        size: 'small',
        name: name,
        ref: fieldRef as RefObject<HTMLDivElement>,
        helperText: error,
        error: !!error,
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
          ...{
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
            onBlur: e => {
              setEvaluateField(true);
              setStringValue(e.target.value);
              params?.inputProps?.onBlur?.(e);
            },
            spellCheck: true,
          },
        },
        disabled:
          formFieldsMetadata?.disabled === true ||
          fieldMetadata?.disabled === true,
        ...(fieldMetadata?.isAutocomplete != null
          ? {}
          : {
              value: stringValue,
              onChange: e => {
                setEvaluateField(true);
                setStringValue(e.target.value);
              },
            }),
      } as OutlinedTextFieldProps;
    }

    function getCheckboxParams(): CheckboxProps {
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
          fieldMetadata?.disabled === true,
      };
    }

    function getAutocompleteParams(): Partial<
      AutocompleteProps<T, Multiple, DisableClearable, FreeSolo, ChipComponent>
    > {
      return {
        multiple: (fieldMetadata?.isAutocomplete?.isMultiple ??
          false) as Multiple,
        freeSolo: (fieldMetadata?.isAutocomplete?.isMultiple ??
          false) as FreeSolo,
        value:
          // We have to set a null value if undefined in order to indicate
          // that it's still a controlled value. See the docs.
          (autocompleteValue ??
            (fieldMetadata?.isAutocomplete?.isMultiple
              ? []
              : null)) as AutocompleteValue<
            T,
            Multiple,
            DisableClearable,
            FreeSolo
          >,
        onChange: (_, value) => {
          setEvaluateField(true);
          setAutocompleteValue(
            value as Multiple extends true ? T[] : T | undefined
          );
        },
        disabled:
          formFieldsMetadata?.disabled === true ||
          fieldMetadata?.disabled === true,
        spellCheck: true,
        inputValue: stringValue,
        onInputChange: (_, value) => {
          setEvaluateField(true);
          setStringValue(value);
        },
      };
    }

    function getHtmlEditorParams(): HtmlEditorProps {
      return {
        id: name,
        value: stringValue,
        onChange: (value: string) => {
          setStringValue(value);
        },
        alwaysShowEditor: fieldMetadata?.isAlwaysEditing,
        readOnly: !!formFieldsMetadata?.disabled || !!fieldMetadata?.disabled,
      };
    }

    function getValue(): Multiple extends true ? T[] : T | undefined {
      if (fieldMetadata?.isAutocomplete) {
        if (fieldMetadata?.isAutocomplete?.isMultiple) {
          return (autocompleteValue ?? []) as Multiple extends true
            ? T[]
            : T | undefined;
        } else if (fieldMetadata?.isAutocomplete?.isFreeSolo) {
          return (
            stringValue != null ? stringValue : undefined
          ) as Multiple extends true ? T[] : T | undefined;
        } else {
          return (
            autocompleteValue != null ? autocompleteValue : undefined
          ) as Multiple extends true ? T[] : T | undefined;
        }
      }

      const trimmedValue = stringValue.trim();
      if (trimmedValue === '') {
        return undefined as Multiple extends true ? T[] : T | undefined;
      }

      const type = getType();

      if (['email', 'password', 'text', 'textarea'].includes(type)) {
        return trimmedValue as Multiple extends true ? T[] : T | undefined;
      }

      if (type === 'number') {
        if (NUMBER_PATTERN.exec(trimmedValue)) {
          return (
            INTEGER_PATTERN.exec(trimmedValue)
              ? parseInt(trimmedValue)
              : parseFloat(trimmedValue)
          ) as Multiple extends true ? T[] : T | undefined;
        }
        return undefined as Multiple extends true ? T[] : T | undefined;
      }

      if (type === 'checkbox') {
        return (trimmedValue === 'on') as Multiple extends true
          ? T[]
          : T | undefined;
      }

      throw new Error(`Input type '${type}' is not recognized.`);
    }

    function setValue(value: Multiple extends true ? T[] : T | undefined) {
      if (fieldMetadata?.isAutocomplete) {
        if (fieldMetadata?.isAutocomplete?.isMultiple) {
          setAutocompleteValue(value);
        } else if (!fieldMetadata?.isAutocomplete?.isFreeSolo) {
          setAutocompleteValue(value);
        } else if (value != null) {
          setStringValue(String(value));
        } else {
          setStringValue('');
        }
      } else {
        if (fieldMetadata?.isBoolean) {
          setStringValue(value ? 'on' : 'off');
        } else if (value != null) {
          setStringValue(String(value));
        } else {
          setStringValue('');
        }
      }
    }

    function verifyOkOrSetError(finalCheck: boolean): boolean {
      const error = calculateError(finalCheck);
      if (error) {
        setError(error);
      } else if (
        fieldMetadata?.isPassword &&
        fieldMetadata?.isPassword?.skipPasswordCheck !== true &&
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
        (input.type === 'password' || fieldMetadata?.isPassword) &&
        fieldMetadata?.isPassword?.skipPasswordCheck !== true &&
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

    const formField: InternalFormField<
      T,
      Multiple,
      DisableClearable,
      FreeSolo,
      ChipComponent
    > = {
      name,
      disabled: !!formFieldsMetadata?.disabled || !!fieldMetadata?.disabled,
      multiple: (fieldMetadata?.isAutocomplete?.isMultiple ??
        false) as Multiple,
      freeSolo: (fieldMetadata?.isAutocomplete?.isFreeSolo ??
        false) as FreeSolo,

      getValue,
      setValue,

      stringValue,
      setStringValue,

      error,
      setError,

      getAutocompleteParams,
      getTextFieldParams,
      getCheckboxParams,
      getHtmlEditorParams,

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
      const input = getInputField(errorField.fieldRef?.current);
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
    startWithObject?: DeepReadOnly<O> | null | undefined
  ): O {
    const object = deepClone(
      startWithObject != null ? startWithObject : {}
    ) as {[key: string]: any};
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
    useSingleAutocompleteFormField,
    useSingleFreeSoloAutocompleteFormField,
    useMultipleAutocompleteFormField,
    useMultipleFreeSoloAutocompleteFormField,
    getFormParams: formParams,
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
      // MUI will create multiple input elements for a TextField. We only
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
  formField: FormField<DeepReadOnly<T>, true>,
  toId: (idFunction: DeepReadOnly<T>) => ID,
  options: DeepReadOnly<T[]>
) {
  const idOptions = new Set(options.map(toId));
  const filteredOptions =
    formField.getValue()?.filter?.(t => idOptions.has(toId(t))) ?? [];
  formField.setValue(filteredOptions);
}
