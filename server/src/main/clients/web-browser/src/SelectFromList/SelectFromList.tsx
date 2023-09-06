import './SelectFromList.scss';
import {ChangeEvent, ReactElement, useEffect, useState} from 'react';

export enum Display {
  DROP_DOWN,
  SCROLLING_LIST,
  RADIO_BUTTONS,
}

export function SelectFromList<K, V>(props: {
  display: Display;
  id: string;
  values: Array<V> | Map<K, V>;
  selectedKey: K;
  getKey: (value?: V) => K;
  stringToKey: (key: string) => K;
  compareValues: (a: V, b: V) => number;
  renderValue: (key: K) => ReactElement;
  onSelect: (key: K) => void;
}): ReactElement {
  const emptyKey = props.getKey();
  const [sortedValues, setSortedValues] = useState(new Array<V>());

  useEffect(
    () =>
      setSortedValues(
        Array.from(props.values.values()).slice().sort(props.compareValues)
      ),
    [props.values]
  );

  useEffect(() => {
    props.onSelect(props.selectedKey);
  }, [props.selectedKey]);

  switch (props.display) {
    case Display.DROP_DOWN:
      return (
        <select
          value={String(props.selectedKey)}
          onChange={(e: ChangeEvent<HTMLSelectElement>) => {
            props.onSelect(props.stringToKey(e.target.value));
          }}
        >
          <option key={String(emptyKey)} value={String(emptyKey)}>
            {props.renderValue(emptyKey)}
          </option>
          {sortedValues.map(value => {
            const key = props.getKey(value);
            return (
              <option key={String(key)} value={String(key)}>
                {props.renderValue(key)}
              </option>
            );
          })}
        </select>
      );
    case Display.SCROLLING_LIST:
      return (
        <div style={{width: '100%'}}>
          <div
            key={String(emptyKey)}
            onSelect={() => props.onSelect(emptyKey)}
            className={
              props.selectedKey === emptyKey ? 'selected' : 'not-selected'
            }
          >
            {props.renderValue(emptyKey)}
          </div>
          {sortedValues.map(value => {
            const key = props.getKey(value);
            return (
              <div
                key={String(key)}
                onSelect={() => props.onSelect(key)}
                className={
                  props.selectedKey === key ? 'selected' : 'not-selected'
                }
              >
                {props.renderValue(key)}
              </div>
            );
          })}
        </div>
      );
    case Display.RADIO_BUTTONS:
      return (
        <>
          <div className="radio-span">
            <input
              id={props.id + String(emptyKey)}
              type="radio"
              name={props.id}
              value={String(emptyKey)}
              onChange={() => props.onSelect(emptyKey)}
              checked={props.selectedKey === emptyKey}
            />
            <label htmlFor={props.id + String(emptyKey)}>
              {props.renderValue(emptyKey)}
            </label>
          </div>
          {sortedValues.map(value => {
            const key = props.getKey(value);
            return (
              <div className="radio-span" key={String(key)}>
                <input
                  id={props.id + String(key)}
                  type="radio"
                  name={props.id}
                  value={String(key)}
                  onChange={() => props.onSelect(key)}
                  checked={props.selectedKey === key}
                />
                <label htmlFor={props.id + String(key)}>
                  {props.renderValue(key)}
                </label>
              </div>
            );
          })}
        </>
      );
  }
}
