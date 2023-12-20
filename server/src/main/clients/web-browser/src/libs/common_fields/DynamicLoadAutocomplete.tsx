import '../global.scss';
import {Autocomplete, Checkbox, Chip, TextField} from '@mui/material';
import {DeepReadOnly} from '../misc';
import * as React from 'react';
import {
  HTMLAttributes,
  ReactNode,
  useContext,
  useEffect,
  useRef,
  useState,
} from 'react';
import {GlobalStateContext} from '../GlobalState';
import {
  AutocompleteRenderGetTagProps,
  AutocompleteRenderOptionState,
} from '@mui/material/Autocomplete/Autocomplete';
import {useDelayedAction} from '../delayed_action';

// TODO: This is high to force a scrollbar. Seems brittle that way.
const PAGE_SIZE = 20;

export function DynamicLoadAutocomplete<T>(
  props: DeepReadOnly<{
    label: string;
    placeholder?: string;

    getId: (value: DeepReadOnly<T>) => number;
    values: T[];
    onChange: (values: DeepReadOnly<T[]>) => void;

    loadMoreOptions: (
      page: number,
      pageSize: number,
      searchText: string
    ) => Promise<DeepReadOnly<T[]>>;

    getOptionLabel: (option: DeepReadOnly<T>) => string;
    groupBy?: (option: DeepReadOnly<T>) => string;
    renderOption?: (
      props: HTMLAttributes<HTMLLIElement>,
      option: DeepReadOnly<T>,
      state: AutocompleteRenderOptionState
    ) => ReactNode;
    renderTags?: (
      value: DeepReadOnly<T[]>,
      getTagProps: AutocompleteRenderGetTagProps
    ) => ReactNode;
  }>
) {
  const global = useContext(GlobalStateContext);

  const allOptionsRef = useRef<(DeepReadOnly<T> | undefined)[]>([]);
  const allOptionsIterationRef = useRef(0);

  const [dropdownOptions, setDropdownOptions] = useState<DeepReadOnly<T[]>>([]);
  const [searchText, setSearchText] = useState('');
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
  }, [searchText]);

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
      .loadMoreOptions(page, PAGE_SIZE, searchText)
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
    <Autocomplete<DeepReadOnly<T>, true, false, false>
      multiple
      disableClearable={false}
      freeSolo={false}
      autoHighlight
      disableCloseOnSelect
      openOnFocus={true}
      size="small"
      options={dropdownOptions}
      // 'value' requires a mutable array.
      value={props.values as DeepReadOnly<T>[]}
      isOptionEqualToValue={(option, value) =>
        props.getId(option) === props.getId(value)
      }
      onChange={(_, values) => {
        props.onChange(values);
      }}
      onInputChange={(_, value) => setSearchText(value)}
      inputValue={searchText}
      getOptionLabel={option => props.getOptionLabel(option) ?? ''}
      groupBy={props.groupBy ? props.groupBy : undefined}
      renderOption={
        props.renderOption
          ? props.renderOption
          : (props2, option, state) => (
              <li {...props2} key={props.getId(option)}>
                <Checkbox style={{marginRight: 8}} checked={state.selected} />
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
          : (options, getTagProps) =>
              options.map(option => (
                <Chip
                  {...getTagProps}
                  key={props.getId(option)}
                  label={props.getOptionLabel(option)}
                  size="small"
                  onDelete={() => {
                    props.onChange(
                      props.values
                        .slice()
                        .filter(
                          option2 =>
                            props.getId(option) !== props.getId(option2)
                        )
                    );
                  }}
                />
              ))
      }
      renderInput={params => (
        <TextField
          {...params}
          label={props.label}
          placeholder={props.placeholder}
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
