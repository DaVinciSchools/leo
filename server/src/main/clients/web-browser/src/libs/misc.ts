/* eslint-disable @typescript-eslint/no-explicit-any */

import {Long as PbLong} from 'protobufjs';
import Long from 'long';

export function toLong(value: PbLong | number) {
  if (typeof value === 'number') {
    return Long.fromNumber(value);
  } else {
    return Long.fromValue({
      low: value.low,
      high: value.high,
      unsigned: value.unsigned,
    });
  }
}

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

export type Writable<T> = {-readonly [P in keyof T]: T[P]};
