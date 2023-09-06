import './SelectMultipleFromList.scss';
import {ReactElement, useEffect, useState} from 'react';

export enum MultipleDisplay {
  SCROLLING_LIST,
  CHECK_BOXES,
}

export function SelectMultipleFromList<K, V>(props: {
  display: MultipleDisplay;
  id: string;
  values: Array<V> | Map<K, V>;
  selectedKeys: Set<K>;
  getKey: (value?: V) => K;
  stringToKey: (key: string) => K;
  compareValues: (a: V, b: V) => number;
  renderValue: (key: K) => ReactElement;
  onSelect: (key: Set<K>) => void;
}): ReactElement {
  const [sortedValues, setSortedValues] = useState(new Array<V>());

  useEffect(
    () =>
      setSortedValues(
        Array.from(props.values.values()).slice().sort(props.compareValues)
      ),
    [props.values]
  );

  useEffect(() => {
    props.onSelect(props.selectedKeys);
  }, [props.selectedKeys]);

  function AddRemoveKey(key: K): Set<K> {
    const newSet = new Set(props.selectedKeys);
    if (newSet.has(key)) {
      newSet.delete(key);
    } else {
      newSet.add(key);
    }
    return newSet;
  }

  switch (props.display) {
    case MultipleDisplay.SCROLLING_LIST:
      return (
        <div style={{width: '100%'}}>
          {sortedValues.map(value => {
            const key = props.getKey(value);
            return (
              <div
                key={String(key)}
                onSelect={() => props.onSelect(AddRemoveKey(key))}
                className={
                  props.selectedKeys.has(key) ? 'selected' : 'not-selected'
                }
              >
                {props.renderValue(key)}
              </div>
            );
          })}
        </div>
      );
    case MultipleDisplay.CHECK_BOXES:
      return (
        <>
          {sortedValues.map(value => {
            const key = props.getKey(value);
            return (
              <div className="checkbox-span" key={String(key)}>
                <input
                  id={props.id + String(key)}
                  type="checkbox"
                  name={props.id}
                  value={String(key)}
                  onChange={() => props.onSelect(AddRemoveKey(key))}
                  checked={props.selectedKeys.has(key)}
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
