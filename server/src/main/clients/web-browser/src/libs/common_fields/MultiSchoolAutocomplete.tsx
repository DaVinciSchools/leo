import '../global.scss';

import {
  Autocomplete,
  Checkbox,
  Chip,
  InputLabelProps,
  TextField,
} from '@mui/material';
import {FormField} from '../forms';
import {pl_types} from '../../generated/protobuf-js';
import ISchool = pl_types.ISchool;
import {addClassName} from '../tags';

export function MultiSchoolAutocomplete(props: {
  sortedSchools: readonly ISchool[];
  formField: FormField<readonly ISchool[]>;
  InputLabelProps?: Partial<InputLabelProps>;
  placeholder?: (hasOptions: boolean) => string;
}) {
  return (
    <Autocomplete
      {...props.formField.autocompleteParams()}
      multiple
      autoHighlight
      disableCloseOnSelect
      options={props.sortedSchools}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      renderOption={(params, option, {selected}) => (
        <li {...params} key={option?.id ?? 0}>
          <Checkbox style={{marginRight: 8}} checked={selected} />
          <span
            style={{
              textOverflow: 'ellipsis',
              whiteSpace: 'nowrap',
            }}
          >
            {option.name ?? 'Unnamed School'}
            {option.nickname ? (
              <>
                &nbsp;<i>({option.nickname})</i>
              </>
            ) : (
              <></>
            )}
          </span>
        </li>
      )}
      getOptionLabel={option => {
        return (option.name ?? 'Unnamed School') + ' (' + option.nickname + ')';
      }}
      renderInput={params => (
        <TextField
          {...props.formField.textFieldParams(params)}
          label="Schools"
          InputLabelProps={props.InputLabelProps}
          placeholder={
            props.placeholder
              ? props.placeholder(props.sortedSchools.length > 0)
              : undefined
          }
        />
      )}
      renderTags={(schools: readonly ISchool[], getTagProps) =>
        schools.map((option, index) => (
          <Chip
            {...addClassName(getTagProps({index}), 'global-tags')}
            label={option.nickname ?? option.name ?? 'Unnamed School'}
            title={
              (option.name ?? 'Unnamed School') +
              (option.nickname ? ' (' + option.nickname + ')' : '')
            }
            size="small"
            variant="outlined"
          />
        ))
      }
    />
  );
}
