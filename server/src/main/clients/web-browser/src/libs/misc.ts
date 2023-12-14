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

export function replaceInNewArray<T>(
  values: readonly T[] | DeepReadonly<T>[],
  newValue: T | DeepReadonly<T>,
  toKey: (value: T | DeepReadonly<T>) => any
): DeepReadonly<T[]> {
  const key = toKey(newValue);
  const index = values.findIndex(value => toKey(value) === key);
  if (index === -1) {
    return DeepReadonly(values);
    values[index] = newValue;
  }
  return values;
}

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

export type DeepReadonly<T> = T extends void | undefined
  ? undefined
  : T extends undefined | null | boolean | string | number | Function
  ? T
  : T extends Array<infer E>
  ? ReadonlyArray<DeepReadonly<E>>
  : T extends Map<infer K, infer V>
  ? ReadonlyMap<DeepReadonly<K>, DeepReadonly<V>>
  : T extends Set<infer E>
  ? ReadonlySet<DeepReadonly<E>>
  : {readonly [K in keyof T]: DeepReadonly<T[K]>};

export type Writable<T> = {-readonly [P in keyof T]: T[P]};

export type DeepWritable<T> = T extends void | undefined
  ? undefined
  : T extends undefined | null | boolean | string | number | Function
  ? T
  : T extends ReadonlyArray<infer E>
  ? Array<DeepWritable<E>>
  : T extends ReadonlyMap<infer K, infer V>
  ? Map<DeepWritable<K>, DeepWritable<V>>
  : T extends ReadonlySet<infer E>
  ? Set<DeepWritable<E>>
  : {-readonly [K in keyof T]: DeepWritable<T[K]>};

export function DeepWritable<T>(value: T | DeepReadonly<T>): DeepWritable<T> {
  return value as DeepWritable<T>;
}
