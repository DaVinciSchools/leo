import {useLocation} from 'react-router-dom';
import {useEffect, useState} from 'react';
import {DeepReadOnly} from './misc';

export function useURLSearchParam<K, D>(
  paramName: string,
  defaultValue: DeepReadOnly<K | D>,
  toParamString?: (value: DeepReadOnly<K | D>) => string | null | undefined,
  fromParamString?: (
    paramString: string
  ) => DeepReadOnly<K | D> | null | undefined
): [DeepReadOnly<K | D>, (newValue: DeepReadOnly<K | D>) => void] {
  const [useValue, setUseValue] = useState(
    getValueFromSearch(new URL(window.location.toString()))
  );

  function getValueFromSearch(url: DeepReadOnly<URL>) {
    const paramString = url.searchParams.get(paramName);
    return (
      paramString == null
        ? defaultValue
        : (fromParamString ?? String)(paramString) ?? defaultValue
    ) as DeepReadOnly<K | D>;
  }

  function setStateFromValue(
    newValue: DeepReadOnly<K | D>,
    url: DeepReadOnly<URL>
  ) {
    const paramValue =
      newValue == null
        ? undefined
        : (toParamString ?? String)(newValue) ?? undefined;

    const oldSearchParams = new URLSearchParams(url.searchParams);
    const urlSearchParams = new URLSearchParams(url.searchParams);
    if (paramValue == null) {
      urlSearchParams.delete(paramName);
    } else {
      urlSearchParams.set(paramName, paramValue);
    }

    oldSearchParams.sort();
    urlSearchParams.sort();
    if (urlSearchParams.toString() === oldSearchParams.toString()) {
      return;
    }

    const newUrl = new URL(url);
    newUrl.search = urlSearchParams.toString();
    window.history.pushState(undefined, '', newUrl.toString());
  }

  function setValue(newValue: DeepReadOnly<K | D>, url?: DeepReadOnly<URL>) {
    if (useValue === newValue) {
      return;
    }

    setUseValue(newValue);
    setStateFromValue(newValue, url ?? new URL(window.location.toString()));
  }

  const location = useLocation();
  useEffect(() => {
    const url = new URL(window.location.toString());
    setValue(getValueFromSearch(url), url);
  }, [window.history.state, location.search]);

  return [useValue, setValue];
}
