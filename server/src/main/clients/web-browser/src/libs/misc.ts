/* eslint-disable @typescript-eslint/no-explicit-any */

export function replaceOrAddInPlace<T>(
  values: T[],
  newValue: T,
  toKey: (value: T) => any
): T[] {
  const key = toKey(newValue);
  const index = values.findIndex(value => toKey(value) === key);
  if (index === -1) {
    values.push(newValue);
  } else {
    values[index] = newValue;
  }
  return values;
}

export function replaceInPlace<T>(
  values: T[],
  newValue: T,
  toKey: (value: T) => any
): T[] {
  const key = toKey(newValue);
  const index = values.findIndex(value => toKey(value) === key);
  if (index !== -1) {
    values[index] = newValue;
  }
  return values;
}
