import {FormField} from '../forms';
import {Autocomplete, Checkbox, Chip, TextField} from '@mui/material';
import {addClassName} from '../tags';

export function MultiTagAutocomplete(props: {
  sortedTags: string[];
  formField: FormField<string[]>;
  placeholder?: (hasOptions: boolean) => string;
}) {
  return (
    <Autocomplete
      {...props.formField.autocompleteParams()}
      multiple
      disableCloseOnSelect
      freeSolo
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
          {...props.formField.textFieldParams(params)}
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
