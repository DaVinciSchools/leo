import {FormField} from '../form_utils/forms';
import {Autocomplete, Checkbox, Chip, TextField} from '@mui/material';
import {addClassName} from '../tags';

export function MultiTagAutocomplete(props: {
  sortedTags: readonly string[];
  formField: FormField<string, true, false, true>;
  placeholder?: (hasOptions: boolean) => string;
}) {
  return (
    <Autocomplete
      {...props.formField.getAutocompleteParams()}
      disableCloseOnSelect
      options={props.sortedTags}
      renderOption={(params, option, {selected}) => (
        <li {...params} key={option}>
          <Checkbox style={{marginRight: 8}} checked={selected} />
          <span
            style={{
              textOverflow: 'ellipsis',
              whiteSpace: 'nowrap',
            }}
          >
            {option}
          </span>
        </li>
      )}
      renderInput={params => (
        <TextField
          {...props.formField.getTextFieldParams(params)}
          placeholder={
            props.placeholder
              ? props.placeholder(props.sortedTags.length > 0)
              : undefined
          }
        />
      )}
      renderTags={(tags, getTagProps) =>
        tags.map((option, index) => (
          <Chip
            {...addClassName(getTagProps({index}), 'global-tags')}
            label={option}
            title={option}
            size="small"
            variant="outlined"
          />
        ))
      }
    />
  );
}
