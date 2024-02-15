/* eslint-disable @typescript-eslint/no-explicit-any */

import '../global.scss';

import {DeepReadOnly, removeInDeepReadOnly} from '../misc';
import * as React from 'react';
import {
  CSSProperties,
  HTMLAttributes,
  ReactNode,
  useContext,
  useEffect,
  useRef,
  useState,
} from 'react';
import {GlobalStateContext} from '../GlobalStateProvider/GlobalStateProvider';
import {
  AutocompleteRenderGetTagProps,
  AutocompleteRenderOptionState,
} from '@mui/material/Autocomplete/Autocomplete';
import {useDelayedAction} from '../delayed_action';
import {FormField} from '../form_utils/forms';
import {Autocomplete, Checkbox, Chip, TextField} from '@mui/material';

// TODO: This is high to force a scrollbar. Seems brittle that way.
const PAGE_SIZE = 20;

export function DynamicLoadAutocomplete<
  T,
  Multiple extends boolean | undefined = false
>(
  props: DeepReadOnly<{
    formField: FormField<DeepReadOnly<T>, Multiple>;
    label: string;
    getId: (value: DeepReadOnly<T>) => number;
    getOptionLabel: (option: DeepReadOnly<T>) => string;
    loadMoreOptions: (
      page: number,
      pageSize: number,
      searchText: string
    ) => Promise<DeepReadOnly<T[]>>;
    context?: any;

    placeholder?: string;
    renderOption?: (
      props: HTMLAttributes<HTMLLIElement>,
      option: DeepReadOnly<T>,
      state: AutocompleteRenderOptionState
    ) => ReactNode;
    renderTags?: (
      value: DeepReadOnly<T[]>,
      getTagProps: AutocompleteRenderGetTagProps
    ) => ReactNode;
    renderTagLabel?: (option: DeepReadOnly<T>) => ReactNode;
    renderTagStyle?: (
      option?: DeepReadOnly<T> | null | undefined
    ) => CSSProperties | undefined;
    groupBy?: (option: DeepReadOnly<T>) => string;
  }>
) {
  const global = useContext(GlobalStateContext);

  const allOptionsRef = useRef<(DeepReadOnly<T> | undefined)[]>([]);
  const allOptionsIterationRef = useRef(0);

  const [dropdownOptions, setDropdownOptions] = useState<DeepReadOnly<T[]>>([]);
  const [noMoreResults, setNoMoreResults] = useState(false);

  const delayedLoadMoreOptions = useDelayedAction(
    () => {},
    () => loadMoreOptions(noMoreResults),
    500
  );

  useEffect(() => {
    allOptionsRef.current = [];
    allOptionsIterationRef.current++;
    setNoMoreResults(false);
    loadMoreOptions(false);
  }, [props.formField.stringValue, JSON.stringify(props.context ?? '')]);

  function loadMoreOptions(noMoreResults: boolean) {
    if (noMoreResults) {
      return;
    }

    const currentIteration = allOptionsIterationRef.current;
    const page = allOptionsRef.current.length / PAGE_SIZE;
    allOptionsRef.current.splice(
      page * PAGE_SIZE,
      PAGE_SIZE,
      ...(Array.from({length: PAGE_SIZE}).fill(undefined) as Array<
        DeepReadOnly<T> | undefined
      >)
    );

    props
      .loadMoreOptions(page, PAGE_SIZE, props.formField.stringValue)
      .then(newOptions => {
        if (allOptionsIterationRef.current !== currentIteration) {
          return;
        }
        allOptionsRef.current.splice(
          page * PAGE_SIZE,
          newOptions.length,
          ...(newOptions as (DeepReadOnly<T> | undefined)[])
        );
        const seenIds = new Set<number>();
        const filteredDropdownOptions = allOptionsRef.current.filter(option => {
          if (option != null && !seenIds.has(props.getId(option))) {
            seenIds.add(props.getId(option));
            return true;
          }
          return false;
        });
        setDropdownOptions(filteredDropdownOptions as DeepReadOnly<T[]>);
        if (newOptions.length < PAGE_SIZE) {
          setNoMoreResults(true);
        }
      })
      .catch(global.setError);
  }

  return (
    <Autocomplete<DeepReadOnly<T>, Multiple, false, false>
      {...props.formField.getAutocompleteParams()}
      autoHighlight
      disableCloseOnSelect={props.formField.multiple}
      openOnFocus={true}
      size="small"
      options={dropdownOptions}
      isOptionEqualToValue={(option, value) =>
        props.getId(option) === props.getId(value)
      }
      getOptionLabel={option =>
        option ? props.getOptionLabel(option) ?? '' : ''
      }
      groupBy={props.groupBy}
      renderOption={
        props.renderOption
          ? props.renderOption
          : (params, option, {selected}) => (
              <li
                {...params}
                className={
                  props.formField.multiple
                    ? 'global-flex-row'
                    : params.className
                }
                style={{alignItems: 'center', cursor: 'pointer'}}
              >
                {props.formField.multiple && (
                  <Checkbox style={{marginRight: 8}} checked={selected} />
                )}
                <span
                  style={{
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap',
                  }}
                >
                  {props.getOptionLabel(option)}
                </span>
              </li>
            )
      }
      renderTags={
        props.renderTags
          ? props.renderTags
          : !props.formField.multiple
          ? undefined
          : (options, getTagProps) =>
              options.map(option => (
                <Chip
                  {...getTagProps}
                  key={props.getId(option)}
                  label={
                    option
                      ? props.renderTagLabel?.(option) ??
                        props.getOptionLabel(option)
                      : ''
                  }
                  size="small"
                  onDelete={() => {
                    props.formField.setValue(
                      removeInDeepReadOnly(
                        props.formField.getValue() as DeepReadOnly<T[]>,
                        option,
                        props.getId
                      ) as Multiple extends true
                        ? DeepReadOnly<T>[]
                        : DeepReadOnly<T> | undefined
                    );
                  }}
                  style={option ? props.renderTagStyle?.(option) ?? {} : {}}
                />
              ))
      }
      renderInput={params => (
        <TextField
          {...props.formField.getTextFieldParams(params)}
          label={props.label}
          placeholder={props.placeholder}
          InputLabelProps={{shrink: true}}
        />
      )}
      filterOptions={(options, state) => {
        return options.filter(option =>
          props
            .getOptionLabel(option)
            .toLowerCase()
            .includes(state.inputValue.toLowerCase())
        );
      }}
      ListboxProps={{
        onScroll: e => {
          const listboxNode = e.currentTarget;
          if (
            listboxNode.scrollTop + listboxNode.clientHeight >=
            listboxNode.scrollHeight
          ) {
            delayedLoadMoreOptions.trigger();
          }
        },
      }}
    />
  );
}
