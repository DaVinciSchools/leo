import {
  Autocomplete,
  Checkbox,
  InputLabelProps,
  TextField,
} from '@mui/material';
import {FormField} from '../form_utils/forms';
import {pl_types} from 'pl-pb';
import {useEffect, useState} from 'react';
import {DeepReadOnly, deepWritable} from '../misc';
import IProject = pl_types.IProject;

export function ProjectsAutocomplete<
  Multiple extends boolean | undefined = false
>(
  props: DeepReadOnly<{
    sortedProjects: IProject[];
    formField:
      | FormField<IProject, Multiple>
      | FormField<DeepReadOnly<IProject>, Multiple>;
    InputLabelProps?: Partial<InputLabelProps>;
    placeholder?: (hasOptions: boolean) => string;
  }>
) {
  const [hasMultipleClassXs, setHasMultipleClassXs] = useState(false);

  useEffect(() => {
    setHasMultipleClassXs(
      new Set(props.sortedProjects.map(e => e?.assignment?.classX?.id)).size > 1
    );
  }, [props.sortedProjects]);

  return (
    <Autocomplete
      {...props.formField.autocompleteParams()}
      autoHighlight
      options={deepWritable(props.sortedProjects)}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      groupBy={
        hasMultipleClassXs
          ? option => option?.assignment?.classX?.name ?? 'No Class Selected'
          : undefined
      }
      renderOption={(props2, option, {selected}) => (
        <li {...props2} key={option.id}>
          {props.formField.multiple ? (
            <Checkbox style={{marginRight: 8}} checked={selected} />
          ) : (
            <></>
          )}
          <span
            style={{
              textOverflow: 'ellipsis',
              whiteSpace: 'nowrap',
            }}
          >
            {option.name}:&nbsp;<i>{option.shortDescr}</i>
          </span>
        </li>
      )}
      // getOptionLabel={option => option.name + ': ' + option.shortDescr}
      renderInput={params => (
        <TextField
          {...props.formField.textFieldParams(params)}
          label="Projects"
          InputLabelProps={props.InputLabelProps as InputLabelProps}
          placeholder={
            props.placeholder
              ? props.placeholder(props.sortedProjects.length > 0)
              : undefined
          }
        />
      )}
    />
  );
}
