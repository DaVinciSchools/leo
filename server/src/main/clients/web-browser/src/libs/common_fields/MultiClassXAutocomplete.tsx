import '../global.scss';
import {pl_types} from 'pl-pb';
import {
  Autocomplete,
  Checkbox,
  Chip,
  InputLabelProps,
  TextField,
} from '@mui/material';
import {useEffect, useState} from 'react';
import {FormField} from '../form_utils/forms';
import {addClassName} from '../tags';
import {DeepReadOnly} from '../misc';
import IClassX = pl_types.IClassX;

export function MultiClassXAutocomplete(props: {
  sortedClassXs: readonly IClassX[];
  formField: FormField<DeepReadOnly<IClassX>, true>;
  InputLabelProps?: Partial<InputLabelProps>;
  placeholder?: (hasOptions: boolean) => string;
}) {
  const [hasMultipleSchools, setHasMultipleSchools] = useState(true);

  useEffect(() => {
    setHasMultipleSchools(
      new Set(props.sortedClassXs.map(e => e?.school?.id)).size > 1
    );
  }, [props.sortedClassXs]);

  return (
    <Autocomplete
      autoHighlight
      disableCloseOnSelect
      options={props.sortedClassXs.slice()}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      groupBy={
        hasMultipleSchools
          ? option => option?.school?.name ?? 'Unnamed School'
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
            {option.name ?? 'Unnamed Class'}
            {option.number ? (
              <>
                &nbsp;<i>({option.number})</i>
              </>
            ) : (
              <></>
            )}
          </span>
        </li>
      )}
      getOptionLabel={option => {
        return option.number + ' ' + (option.name ?? 'Unnamed School');
      }}
      renderInput={params => (
        <TextField
          {...props.formField.getTextFieldParams(params)}
          label="Classes"
          InputLabelProps={props.InputLabelProps}
          placeholder={
            props.placeholder
              ? props.placeholder(props.sortedClassXs.length > 0)
              : undefined
          }
        />
      )}
      renderTags={(classXs, getTagProps) =>
        classXs.map((option, index) => (
          <Chip
            {...addClassName(getTagProps({index}), 'global-tags')}
            label={option.number ?? option.name ?? 'Unnamed Class'}
            title={
              (option.number ? option.number + ': ' : '') +
              (option.name ?? 'Unnamed Class')
            }
            size="small"
            variant="outlined"
          />
        ))
      }
      {...props.formField.getAutocompleteParams()}
    />
  );
}
