import '../global.scss';

import {
  Autocomplete,
  Checkbox,
  Chip,
  InputLabelProps,
  TextField,
} from '@mui/material';
import {FormField} from '../form_utils/forms';
import {pl_types} from 'pl-pb';
import {addClassName} from '../tags';
import {DeepReadOnly} from '../misc';
import {useEffect, useState} from 'react';
import ISchool = pl_types.ISchool;

export function MultiSchoolAutocomplete(
  props: DeepReadOnly<{
    sortedSchools: ISchool[];
    formField: FormField<ISchool | DeepReadOnly<ISchool>, true>;
    InputLabelProps?: Partial<InputLabelProps>;
    placeholder?: (hasOptions: boolean) => string;
  }>
) {
  const [hasMultipleDistricts, setHasMultipleDistricts] = useState(false);

  useEffect(() => {
    setHasMultipleDistricts(
      new Set(props.sortedSchools.map(e => e?.district?.id)).size > 1
    );
  }, [props.sortedSchools]);

  return (
    <Autocomplete
      {...props.formField.autocompleteParams()}
      autoHighlight
      disableCloseOnSelect
      options={props.sortedSchools}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      groupBy={
        hasMultipleDistricts
          ? option => option?.district?.name ?? 'Unnamed District'
          : undefined
      }
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
        return (
          (option.name ?? 'Unnamed School') +
          ' (' +
          (option.nickname ?? '') +
          ')'
        );
      }}
      renderInput={params => (
        <TextField
          {...props.formField.textFieldParams(params)}
          label="Schools"
          InputLabelProps={props.InputLabelProps as InputLabelProps}
          placeholder={
            props.placeholder
              ? props.placeholder(props.sortedSchools.length > 0)
              : undefined
          }
        />
      )}
      renderTags={(schools, getTagProps) =>
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
