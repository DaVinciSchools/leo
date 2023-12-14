import {FormField} from '../form_utils/forms';
import {Autocomplete, Checkbox, Chip, TextField} from '@mui/material';
import {addClassName} from '../tags';
import {DeepReadOnly} from '../misc';

export function MultiTagAutocomplete(
  props: DeepReadOnly<{
    sortedTags: string[];
    formField: FormField<string, true, true>;
    placeholder?: (hasOptions: boolean) => string;
  }>
) {
  return (
    <Autocomplete
      {...props.formField.autocompleteParams()}
      freeSolo
      autoHighlight
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
          {...props.formField.textFieldParams(params)}
          label="Tags"
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
