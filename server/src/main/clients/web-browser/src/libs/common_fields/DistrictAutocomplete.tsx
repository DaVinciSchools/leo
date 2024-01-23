import {Autocomplete, TextField} from '@mui/material';
import {FormField} from '../form_utils/forms';
import {pl_types} from 'pl-pb';
import {DeepReadOnly} from '../misc';
import IDistrict = pl_types.IDistrict;

export function DistrictAutocomplete(
  props: DeepReadOnly<{
    sortedDistricts: IDistrict[];
    formField: FormField<DeepReadOnly<IDistrict>>;
    placeholder?: (hasOptions: boolean) => string;
  }>
) {
  return (
    <Autocomplete
      {...props.formField.getAutocompleteParams()}
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
          {...props.formField.getTextFieldParams(params)}
          label="District"
          size="small"
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
