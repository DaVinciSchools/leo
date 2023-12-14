import {FormField} from '../form_utils/forms';
import {Autocomplete, Checkbox, Chip, TextField} from '@mui/material';
import {forwardRef, Ref} from 'react';

export const MultiTagAutocomplete = forwardRef(
  (
    props: {
      sortedTags: readonly string[];
      formField: FormField<readonly string[]>;
      placeholder?: (hasOptions: boolean) => string;
      label?: string;
    },
    ref: Ref<string>
  ) => {
    return (
      <Autocomplete
        {...props.formField.autocompleteParams()}
        ref={ref}
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
                ? props.placeholder(
                    (props.formField.getValue() ?? []).length > 0
                  )
                : undefined
            }
            label={props.label}
          />
        )}
        renderTags={(tags, getTagProps) =>
          tags.map((option, index) => (
            <Chip
              {...getTagProps({index})}
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
);
