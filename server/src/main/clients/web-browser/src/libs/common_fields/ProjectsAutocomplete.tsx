import {Autocomplete, TextField} from '@mui/material';
import {FormField} from '../form_utils/forms';
import {pl_types} from '../../generated/protobuf-js';
import {CSSProperties, useEffect, useState} from 'react';

import IProject = pl_types.IProject;

export function ProjectsAutocomplete(props: {
  sortedProjects: readonly IProject[];
  formField: FormField<IProject | null>;
  style?: CSSProperties;
}) {
  const [hasMultipleProjects, setHasMultipleProjects] = useState(true);

  useEffect(() => {
    setHasMultipleProjects(
      new Set(props.sortedProjects.map(e => e?.assignment?.classX?.id)).size > 1
    );
  }, [props.sortedProjects]);
  return (
    <Autocomplete
      fullWidth
      autoHighlight
      options={props.sortedProjects}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      groupBy={
        hasMultipleProjects
          ? option => option?.assignment?.classX?.name ?? 'No Class Selected'
          : undefined
      }
      renderOption={(props, option) => (
        <li {...props} key={option.id}>
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
      renderInput={params => (
        <TextField
          label="Select Project"
          size="small"
          {...props.formField.textFieldParams(params)}
          style={props.style}
        />
      )}
      getOptionLabel={option => option.name + ': ' + option.shortDescr}
      {...props.formField.autocompleteParams()}
    />
  );
}
