import {
  Autocomplete,
  Button,
  Checkbox,
  Chip,
  Modal,
  TextField,
} from '@mui/material';
import {useEffect, useState} from 'react';
import {Close} from '@mui/icons-material';
import {VISIBLE_ALPHA} from '../../Ikigai/Ikigai';
import {TitledPaper} from '../TitledPaper/TitledPaper';
import {OPTION_SORTER} from '../sorters';
import {addClassName} from '../tags';
import Markdown from 'react-markdown';
import {deepClone, deepReadOnly, DeepReadOnly} from '../misc';
import {ProjectInput} from '../ProjectBuilder/ProjectBuilder';
import {MODAL_PAPER_PROPS, MODAL_STYLE} from './IkigaiProjectBuilder';
import {pl_types} from 'pl-pb';
import IOption = pl_types.ProjectInputCategory.IOption;

export function DropdownSelectInputModal(
  props: DeepReadOnly<{
    open: boolean;
    input: ProjectInput;
    onClickClose: (input: ProjectInput) => void;
    setInput: (input: DeepReadOnly<ProjectInput>) => void;
  }>
) {
  const [values, setValues] = useState<DeepReadOnly<Set<IOption>>>(new Set());
  const [options, setOptions] = useState<DeepReadOnly<IOption[]>>([]);
  const optionsById = deepReadOnly(
    new Map(options.map(option => [option.id, option]))
  );

  function resetValuesAndOptions() {
    setValues(
      new Set(
        (props.input?.input?.selectedIds ?? [])
          .map(id => optionsById.get(id))
          .filter(option => option != null)
          .map(option => option!)
      )
    );
    setOptions(
      [...(props.input?.input?.category?.options ?? [])].sort(OPTION_SORTER)
    );
  }

  function onCancel() {
    resetValuesAndOptions();

    props.onClickClose(props.input);
  }

  function save(newValues: DeepReadOnly<IOption[]>) {
    const newInput = deepClone(props.input);

    // Normalize new values.
    newInput.input.selectedIds = newValues
      .map(value => value.id)
      .filter(id => id != null && id !== 0)
      .map(id => id!);

    props.setInput(newInput);
    props.onClickClose(props.input);
  }

  function onSave() {
    save([...values.values()]);
  }

  useEffect(() => {
    resetValuesAndOptions();
  }, [props.input]);

  return (
    <>
      <Modal
        open={props.open}
        onClose={onCancel}
        className="ikigai-project-builder"
      >
        <TitledPaper
          title={
            <Markdown className="global-markdown">
              {props.input.input?.category?.name ?? ''}
            </Markdown>
          }
          headerColor={`hsla(${props.input.highlightHue}, 100%, 75%, ${VISIBLE_ALPHA})`}
          highlightColor={`hsla(${props.input.highlightHue}, 100%, 75%, 100%)`}
          titleStyle={MODAL_STYLE}
          paperProps={MODAL_PAPER_PROPS}
          icon={
            <span style={{color: 'black'}}>
              <Close onClick={onCancel} />
            </span>
          }
        >
          <div className="ikigai-project-builder-modal-content">
            <div>
              <div style={{paddingBottom: '1em'}}>
                <Markdown className="global-markdown">
                  {props.input.input?.category?.inputDescr ?? ''}
                </Markdown>
              </div>
              <Autocomplete
                multiple
                autoHighlight
                autoFocus
                disableCloseOnSelect
                value={[...values.values()].sort(OPTION_SORTER)}
                groupBy={option => option.category ?? ''}
                renderInput={params => (
                  <TextField
                    {...params}
                    label={props.input.input?.category?.name ?? ''}
                    placeholder={props.input.input?.category?.placeholder ?? ''}
                    variant="standard"
                  />
                )}
                options={options}
                renderOption={(props, option, {selected}) => (
                  <li {...props}>
                    <Checkbox style={{marginRight: 8}} checked={selected} />
                    {option?.name ?? 'undefined'}
                    {option?.shortDescr && (
                      <>
                        :&nbsp;
                        <i>{option?.shortDescr ?? 'undefined'}</i>
                      </>
                    )}
                  </li>
                )}
                getOptionLabel={option =>
                  (option?.name ?? 'undefined') +
                  ' ' +
                  (option?.shortDescr ?? 'undefined')
                }
                onChange={(e, options) => {
                  setValues(new Set(options));
                }}
                renderTags={(tagValue, getTagProps) =>
                  tagValue.map((option, index) => (
                    <Chip
                      label={option.name ?? 'undefined'}
                      size="small"
                      variant="outlined"
                      {...addClassName(
                        getTagProps({index}),
                        'ikigai-project-builder-chip-values'
                      )}
                    />
                  ))
                }
              />
            </div>
            <div className="ikigai-project-builder-modal-buttons">
              <Button onClick={onSave} color="secondary" variant="contained">
                Save
              </Button>
              <Button onClick={onCancel} color="secondary">
                Cancel
              </Button>
            </div>
          </div>
        </TitledPaper>
      </Modal>
    </>
  );
}
