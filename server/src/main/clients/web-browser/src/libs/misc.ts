/* eslint-disable @typescript-eslint/no-explicit-any */

import {Long as PbLong} from 'protobufjs';
import Long from 'long';
import {CSSProperties} from 'react';
import {Base64} from 'js-base64';

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

export function replaceOrAddInDeepReadOnly<T>(
  values: DeepReadOnly<T[]>,
  newValue: DeepReadOnly<T>,
  toKey: (value: DeepReadOnly<T>) => any
): DeepReadOnly<T>[] {
  const key = toKey(newValue);
  const index = values.findIndex(value => toKey(value) === key);
  const newValues = values.slice();
  if (index === -1) {
    newValues.push(newValue);
    return newValues;
  } else {
    newValues[index] = newValue;
    return newValues;
  }
}

export function removeInDeepReadOnly<T>(
  values: DeepReadOnly<T[]>,
  oldValue: DeepReadOnly<T>,
  toKey: (value: DeepReadOnly<T>) => any
): DeepReadOnly<T>[] {
  const key = toKey(oldValue);
  const index = values.findIndex(value => toKey(value) === key);
  if (index !== -1) {
    const newValues = values.slice();
    newValues.splice(index, 1);
    return newValues;
  }
  return values.slice();
}

export function modifyInDeepReadOnly<T, K>(
  values: DeepReadOnly<T[]>,
  oldValue: DeepReadOnly<T>,
  processFn: (value: DeepWritable<T>) => void,
  toKey: (value: DeepReadOnly<T>) => K
): DeepReadOnly<T>[] {
  const oldKey = toKey(oldValue);
  const index = values.findIndex(value => toKey(value) === oldKey);
  if (index !== -1) {
    const newValues = values.slice();
    const newValue = deepClone(newValues[index]);
    processFn(newValue);
    newValues[index] = deepReadOnly(newValue);
    return newValues;
  }
  return values.slice();
}

export function replaceInDeepReadOnly<T, K>(
  values: DeepReadOnly<T[]>,
  newValue: DeepReadOnly<T>,
  toKey: (value: DeepReadOnly<T>) => K
): DeepReadOnly<T>[] {
  const key = toKey(newValue);
  const index = values.findIndex(value => toKey(value) === key);
  if (index !== -1) {
    const newValues = values.slice();
    newValues[index] = newValue;
    return newValues;
  } else {
    return values.slice();
  }
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

export function deepReadOnly<T>(value: T | DeepWritable<T>) {
  return value as DeepReadOnly<T>;
}

export function deepClone<T>(
  value: DeepReadOnly<T>,
  update?: (value: DeepWritable<T>) => void
) {
  const clone = structuredClone(value as DeepWritable<T>);
  update?.(clone);
  return clone;
}

// Proto types are mutable by default. So, to store a DeepReadOnly value in a proto
// we need to make it DeepWritable first. Use this rather than deepWritable() to
// document that we are converting it just to store in a proto.
export function writableForProto<T>(value: T | DeepReadOnly<T>) {
  return value as DeepWritable<T>;
}

export function getHighlightStyle(
  hue: number | null | undefined
): Partial<CSSProperties> {
  if (hue != null) {
    return {
      background: `hsla(${hue}, 100%, 50%, 25%)`,
      border: `hsla(${hue}, 100%, 33%, 100%) 2px solid`,
      borderRadius: '4px',
      padding: '4px',
    };
  }
  return {};
}

export function getUniqueHues<ID>(
  existingHuesById: DeepReadOnly<Map<ID, number>>,
  allIds: DeepReadOnly<ID[]>
) {
  const newHuesById = new Map(allIds.map(id => [id, existingHuesById.get(id)]));
  const usedHues = new Set(
    [...newHuesById.values()].filter(hue => hue != null)
  );

  let maxSteps2 = 1;
  let step = 0;
  let hueDelta = 512;
  let hue = 0;

  for (const id of allIds.slice()) {
    if (newHuesById.get(id) != null) {
      continue;
    }

    do {
      hue = (hueDelta / 2 + step++ * hueDelta) % 256;
      if (step >= maxSteps2 / 2) {
        step = 0;
        hueDelta /= 2;
        maxSteps2 *= 2;
      }
    } while (usedHues.has(hue));
    newHuesById.set(id, hue);
    usedHues.add(hue);
  }

  return newHuesById as Map<DeepReadOnly<ID>, number>;
}

export function genericJsonReplacer(_: string, value: unknown): any {
  if (value instanceof Error) {
    const plainObject = {};
    for (const propName in value) {
      (plainObject as any)[propName] = (value as any)[propName];
    }
    if (value.stack != null) {
      (plainObject as any)['stack'] = value.stack
        .replace(value.name ?? '', '[name]')
        .replace(value.message ?? '', '[message]');
    }
    return plainObject;
  } else if (value instanceof Headers) {
    const plainObject: any = {};
    value.forEach((value, key) => {
      plainObject[key] = value;
    });
    return plainObject;
  } else if (value instanceof Uint8Array) {
    return 'base64:' + Base64.fromUint8Array(value);
  } else if (typeof value === 'object' && value !== null) {
    const plainObject = {};
    for (const propName in value) {
      (plainObject as any)[propName] = (value as any)[propName];
    }
    return plainObject;
  }
  return value;
}
