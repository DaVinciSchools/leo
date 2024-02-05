import {Autocomplete, Button, Chip, Modal, TextField} from '@mui/material';
import {useEffect, useRef, useState} from 'react';
import {Close} from '@mui/icons-material';
import {VISIBLE_ALPHA} from '../../Ikigai/Ikigai';
import {TitledPaper} from '../TitledPaper/TitledPaper';
import {TEXT_SORTER} from '../sorters';
import {addClassName} from '../tags';
import Markdown from 'react-markdown';
import {deepClone, DeepReadOnly} from '../misc';
import {ProjectInput} from '../ProjectBuilder/ProjectBuilder';
import {getInputField} from '../form_utils/forms';
import {MODAL_STYLE, MODAL_PAPER_PROPS} from './IkigaiProjectBuilder';

export default function FreeTextInputModal(
  props: DeepReadOnly<{
    input: ProjectInput;
    open: boolean;
    onClickClose: (input: ProjectInput) => void;
    setInput: (input: DeepReadOnly<ProjectInput>) => void;
  }>
) {
  const [values, setValues] = useState<DeepReadOnly<Set<string>>>(new Set());
  const [options, setOptions] = useState<DeepReadOnly<string[]>>([]);
  const inputRef = useRef<HTMLInputElement>();

  function resetValuesAndOptions() {
    setValues(new Set(props.input?.input?.freeTexts ?? []));
    setOptions(
      [...(props.input?.input?.category?.options ?? [])]
        .map(option => option.name ?? '[Unknown Option]')
        .sort(TEXT_SORTER)
    );
  }

  function onCancel() {
    resetValuesAndOptions();

    props.onClickClose(props.input);
  }

  function save(newValues: DeepReadOnly<string[]>) {
    const newInput = deepClone(props.input);

    // Normalize new values.
    newInput.input.freeTexts = newValues
      .map(value => value.trim())
      .filter(value => value.length > 0)
      .sort(TEXT_SORTER);

    props.setInput(newInput);
    props.onClickClose(props.input);
  }

  function onSave() {
    // // Create a new input with the new text values.
    const newValues = [...values.values()];
    // Enter what was left over in the input field.
    const unenteredValue = getInputField(inputRef.current)?.value;
    if (unenteredValue) {
      newValues.push(unenteredValue);
    }
    save(newValues);
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
                ref={inputRef}
                multiple
                freeSolo
                autoFocus
                // TODO: Make this handle DeepReadOnly.
                value={[...values.values()].sort(TEXT_SORTER)}
                renderInput={params => (
                  <TextField
                    {...params}
                    label={props.input.input?.category?.name ?? ''}
                    placeholder={props.input.input?.category?.placeholder ?? ''}
                    variant="standard"
                  />
                )}
                options={options}
                renderOption={(props, option) => <li {...props}>{option}</li>}
                onChange={(_, options) => {
                  setValues(new Set(options.map(e => e.trim())));
                }}
                renderTags={(tagValue, getTagProps) =>
                  tagValue.map((option, index) => (
                    <Chip
                      label={option}
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
              <Button onClick={onSave}>Save</Button>
              <Button onClick={onCancel}>Cancel</Button>
            </div>
          </div>
        </TitledPaper>
      </Modal>
    </>
  );
}
