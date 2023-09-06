import {Autocomplete, InputLabelProps, TextField} from '@mui/material';
import {FormField} from '../forms';
import {pl_types} from '../../generated/protobuf-js';

import IDistrict = pl_types.IDistrict;

export function DistrictAutocomplete(props: {
  sortedDistricts: readonly IDistrict[];
  formField: FormField<IDistrict | null>;
  InputLabelProps?: Partial<InputLabelProps>;
  placeholder?: (hasOptions: boolean) => string;
}) {
  return (
    <Autocomplete
      {...props.formField.autocompleteParams()}
      autoHighlight
      options={props.sortedDistricts}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      renderOption={(props, option) => (
        <li {...props} key={option.id}>
          <span
            style={{
              textOverflow: 'ellipsis',
              whiteSpace: 'nowrap',
            }}
          >
            {option.name ?? 'Unnamed District'}
          </span>
        </li>
      )}
      renderInput={params => (
        <TextField
          {...props.formField.textFieldParams(params)}
          label="District"
          size="small"
          InputLabelProps={props.InputLabelProps}
          placeholder={
            props.placeholder
              ? props.placeholder(props.sortedDistricts.length > 0)
              : undefined
          }
        />
      )}
      getOptionLabel={option => option.name ?? 'Unnamed District'}
    />
  );
}
