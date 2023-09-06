import {Autocomplete, TextField} from '@mui/material';
import {FormField} from '../forms';
import {pl_types} from '../../generated/protobuf-js';
import {useEffect, useState} from 'react';

import IAssignment = pl_types.IAssignment;

export function AssignmentAutocomplete(props: {
  sortedAssignments: readonly IAssignment[];
  formField: FormField<IAssignment | null>;
}) {
  const [hasMultipleClasses, setHasMultipleClasses] = useState(true);

  useEffect(() => {
    setHasMultipleClasses(
      new Set(props.sortedAssignments.map(e => e?.classX?.id)).size > 1
    );
  }, [props.sortedAssignments]);
  return (
    <Autocomplete
      autoHighlight
      options={props.sortedAssignments}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      groupBy={
        hasMultipleClasses
          ? option => option?.classX?.name ?? 'No Class Selected'
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
            {option.name ?? 'Unnamed Assignment'}
          </span>
        </li>
      )}
      renderInput={params => (
        <TextField
          label="Select Assignment"
          size="small"
          {...props.formField.textFieldParams(params)}
        />
      )}
      getOptionLabel={option => option.name ?? 'Unnamed Assignment'}
      {...props.formField.autocompleteParams()}
    />
  );
}
