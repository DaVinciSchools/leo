export const MIN_PASSWORD_LENGTH = 8;
export const MAX_PASSWORD_LENGTH = 50;

export const PASSWORD_PATTERN = RegExp(
  `^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{${MIN_PASSWORD_LENGTH},${MAX_PASSWORD_LENGTH}}$`
);
export const PASSWORD_ERROR_MESSAGE =
  'Passwords must have 8+ characters, a number, and a lower and upper case letter.';
export const NUMBER_PATTERN = RegExp('^-?[0-9.]+([eE][+-][0-9]+)?');
export const INTEGER_PATTERN = RegExp('^-?[0-9]+');
export const EMAIL_PATTERN = RegExp('^[^@]+@[^@]+\\.[^@]{3,}$');
export const ZIP_CODE_PATTERN = RegExp('^[0-9]{5}(-[0-9]{4})?$');

export interface ExtraErrorChecks {
  isPassword?: boolean;
  isInteger?: boolean;
  isZipCode?: boolean;
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
  extraErrorChecks?: ExtraErrorChecks
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
  extraErrorChecks?: ExtraErrorChecks
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
    PASSWORD_PATTERN.exec(input.value) == null
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
  if (form == null) {
    return {};
  }

  return Object.fromEntries(
    getAllInputFields(form)
      .map(input => [input.name, getInputValue(input)])
      //eslint-disable-next-line @typescript-eslint/no-unused-vars
      .filter(([k, v]) => v !== undefined)
  );
}
