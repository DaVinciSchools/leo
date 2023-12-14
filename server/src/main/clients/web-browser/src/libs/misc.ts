/* eslint-disable @typescript-eslint/no-explicit-any */

import {Long as PbLong} from 'protobufjs';
import Long from 'long';

export function isObject(value: any) {
  return value != null && typeof value === 'object';
}

export function asObject(value: any) {
  return isObject(value) ? value : {};
}

export function toLong(value: DeepReadOnly<PbLong> | number) {
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

export function removeInPlace<T>(
  values: T[],
  oldValue: T,
  toKey: (value: T) => any
): T[] {
  const key = toKey(oldValue);
  const index = values.findIndex(value => toKey(value) === key);
  if (index !== -1) {
    values.splice(index, 1);
  }
  return values;
}

export function cloneAndReplace<T, K>(
  values: DeepReadOnly<T[]>,
  replaceValue: DeepReadOnly<T>,
  toKey: (value: DeepReadOnly<T>) => K
): DeepReadOnly<T[]> {
  const key = toKey(replaceValue);
  const index = values.findIndex(value => toKey(value) === key);
  if (index !== -1) {
    return values.slice().splice(index, 1, replaceValue);
  }
  return values;
}

export type Writable<T> = {-readonly [P in keyof T]: T[P]};

export function isTextEmpty(text: string | null | undefined) {
  return text == null || text.trim() === '';
}

export function isHtmlEmpty(html: string | null | undefined) {
  return isTextEmpty(html) || html?.trim() === '<p><br></p>';
}

export function textOrEmpty(
  text: string | null | undefined,
  emptyText: string
) {
  return isTextEmpty(text) ? emptyText : text!;
}

export function htmlOrEmpty(
  html: string | null | undefined,
  emptyHtml: string
) {
  return isHtmlEmpty(html) ? emptyHtml : html!;
}

export function formatAsTag(text: string | null | undefined) {
  return '#' + (text ?? 'tag').replace(/^#/, '');
}

export type DeepWritable<T> = T extends
  | undefined
  | void
  | null
  | boolean
  | string
  | number
  | Function
  ? T
  : T extends ReadonlyArray<infer E>
  ? Array<DeepWritable<E>>
  : T extends ReadonlyMap<infer K, infer V>
  ? Map<DeepWritable<K>, DeepWritable<V>>
  : T extends ReadonlySet<infer E>
  ? Set<DeepWritable<E>>
  : {-readonly [K in keyof T]: DeepWritable<T[K]>};

export function deepWritable<T>(value: T | DeepReadOnly<T>): DeepWritable<T> {
  return value as DeepWritable<T>;
}

export type DeepReadOnly<T> = T extends
  | undefined
  | void
  | null
  | boolean
  | string
  | number
  | Function
  ? T
  : T extends Array<infer E>
  ? ReadonlyArray<DeepReadOnly<E>>
  : T extends Map<infer K, infer V>
  ? ReadonlyMap<DeepReadOnly<K>, DeepReadOnly<V>>
  : T extends Set<infer E>
  ? ReadonlySet<DeepReadOnly<E>>
  : {readonly [K in keyof T]: DeepReadOnly<T[K]>};

export function deepReadOnly<T>(value: T | DeepWritable<T>): DeepReadOnly<T> {
  return value as DeepReadOnly<T>;
}
