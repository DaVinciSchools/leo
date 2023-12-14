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
import {DeepReadOnly, deepWritable} from '../misc';
import IClassX = pl_types.IClassX;

export function MultiClassXAutocomplete(
  props: DeepReadOnly<{
    sortedClassXs: IClassX[];
    formField: FormField<IClassX, true>;
    InputLabelProps?: Partial<InputLabelProps>;
    placeholder?: (hasOptions: boolean) => string;
  }>
) {
  const [hasMultipleSchools, setHasMultipleSchools] = useState(false);

  useEffect(() => {
    setHasMultipleSchools(
      new Set(props.sortedClassXs.map(e => e?.school?.id)).size > 1
    );
  }, [props.sortedClassXs]);

  return (
    <Autocomplete
      {...props.formField.autocompleteParams()}
      autoHighlight
      options={deepWritable(props.sortedClassXs)}
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
      getOptionLabel={option =>
        option.number + ' ' + (option.name ?? 'Unnamed School')
      }
      renderInput={params => (
        <TextField
          {...props.formField.textFieldParams(params)}
          label="Classes"
          InputLabelProps={props.InputLabelProps as InputLabelProps}
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
              (option.number != null ? option.number + ': ' : '') +
              (option.name ?? 'Unnamed Class')
            }
            size="small"
            variant="outlined"
          />
        ))
      }
    />
  );
}
