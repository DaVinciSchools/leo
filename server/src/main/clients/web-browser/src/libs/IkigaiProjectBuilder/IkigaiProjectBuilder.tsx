import '../global.scss';
import './IkigaiProjectBuilder.scss';

import {
  Autocomplete,
  Button,
  Checkbox,
  Chip,
  Modal,
  PaperProps,
  TextField,
} from '@mui/material';
import {CSSProperties, ReactNode, useEffect, useRef, useState} from 'react';
import {Close} from '@mui/icons-material';
import {Ikigai, VISIBLE_ALPHA} from '../../Ikigai/Ikigai';
import {TitledPaper} from '../TitledPaper/TitledPaper';
import {pl_types} from 'pl-pb';
import {OPTION_SORTER, TEXT_SORTER} from '../sorters';
import {addClassName} from '../tags';
import Markdown from 'react-markdown';
import {deepClone, DeepReadOnly} from '../misc';
import {getCategoryId, ProjectInput} from '../ProjectBuilder/ProjectBuilder';
import IOption = pl_types.ProjectInputCategory.IOption;
import ValueType = pl_types.ProjectInputCategory.ValueType;

const MODAL_STYLE: Partial<CSSProperties> = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '65%',
  height: '65%',
};

const MODAL_PAPER_PROPS: PaperProps = {
  elevation: 24,
};

function FreeTextInput(
  props: DeepReadOnly<{
    input: ProjectInput;
    setInput: (input: DeepReadOnly<ProjectInput>) => void;
  }>
) {
  const [modalOpen, setModalOpen] = useState(false);
  const [values, setValues] = useState<DeepReadOnly<string[]>>([]);
  const [options, setOptions] = useState<DeepReadOnly<string[]>>([]);
  const inputRef = useRef<HTMLInputElement>();

  function resetSelectedValues() {
    setValues((props.input?.input?.freeTexts ?? []).slice().sort(TEXT_SORTER));
  }

  function onClick() {
    resetSelectedValues();
    setModalOpen(true);
  }

  function onCancel() {
    setModalOpen(false);
    resetSelectedValues();
  }

  function onSave() {
    setModalOpen(false);
    saveValues(values);
  }

  function saveValues(newValues: DeepReadOnly<string[]>) {
    setValues(newValues);
    const newInput = deepClone(props.input);
    newInput.input.freeTexts = [...newValues];
    props.setInput(newInput);
  }

  useEffect(() => {
    setOptions(
      props.input?.input?.category?.options
        ?.map(e => e.name ?? '')
        ?.sort(TEXT_SORTER) ?? []
    );
    resetSelectedValues();
  }, [props.input?.input?.category?.options]);

  return (
    <>
      <div
        id={getCategoryId(props.input.input?.category).toString()}
        onClick={onClick}
        className="ikigai-project-builder-panel"
      >
        <div className="ikigai-project-builder-title">
          <Markdown className="global-markdown">
            {props.input.input?.category?.name ?? ''}
          </Markdown>
        </div>
        {(props.input.input?.freeTexts?.length ?? 0) === 0 && (
          <div className="ikigai-project-builder-hint">
            <Markdown className="global-markdown">
              {props.input.input?.category?.hint ?? 'Enter a value'}
            </Markdown>
          </div>
        )}
        <div>
          {(props.input?.input?.freeTexts ?? [])
            .slice()
            .sort(TEXT_SORTER)
            .map(value => (
              <Chip
                key={value}
                label={value}
                className="ikigai-project-builder-chip-values"
                size="small"
                variant="outlined"
                onDelete={() => saveValues(values.filter(e => e !== value))}
              />
            ))}
        </div>
      </div>
      <Modal
        open={modalOpen}
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
                value={values.slice()}
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
                  setValues(options.slice().sort(TEXT_SORTER));
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

function DropdownSelectInput(
  props: DeepReadOnly<{
    input: ProjectInput;
    setInput: (input: DeepReadOnly<ProjectInput>) => void;
  }>
) {
  const [modalOpen, setModalOpen] = useState(false);
  const [options, setOptions] = useState<DeepReadOnly<IOption[]>>([]);
  const [optionsById, setOptionsById] = useState(new Map<number, IOption>());
  const [selectedOptions, setSelectedOptions] = useState<
    DeepReadOnly<IOption[]>
  >([]);

  function resetSelectedOptions() {
    setSelectedOptions([
      ...(props.input?.input?.selectedIds
        ?.map(i => optionsById.get(i)!)
        ?.filter(v => v != null) ?? []),
    ]);
  }

  function onClick() {
    resetSelectedOptions();
    setModalOpen(true);
  }

  function onCancel() {
    setModalOpen(false);
    resetSelectedOptions();
  }

  function onSave() {
    setModalOpen(false);
    saveValues(selectedOptions);
  }

  function saveValues(newOptions: DeepReadOnly<IOption[]>) {
    setSelectedOptions(newOptions);
    const newInput = deepClone(props.input);
    newInput.input.selectedIds = [...newOptions.map(v => v.id!)];
    props.setInput(newInput);
  }

  useEffect(() => {
    setOptions(
      props.input?.input?.category?.options?.slice()?.sort(OPTION_SORTER) ?? []
    );
    setOptionsById(
      new Map(props.input?.input?.category?.options?.map(o => [o.id!, o]) ?? [])
    );
    resetSelectedOptions();
  }, [props.input?.input?.category?.options]);

  return (
    <>
      <div
        id={getCategoryId(props.input.input?.category).toString()}
        onClick={onClick}
        className="ikigai-project-builder-panel"
      >
        <div className="ikigai-project-builder-title">
          <Markdown className="global-markdown">
            {props.input.input?.category?.name ?? ''}
          </Markdown>
        </div>
        {(props.input.input?.selectedIds?.length ?? 0) === 0 && (
          <span className="ikigai-project-builder-hint">
            <Markdown className="global-markdown">
              {props.input.input?.category?.hint ?? 'Enter a value'}
            </Markdown>
          </span>
        )}
        <div>
          {(props.input?.input?.selectedIds ?? [])
            .map(i => optionsById.get(i))
            .filter(v => v != null)
            // A little help for the null checks.
            .map(v => v!)
            .sort(OPTION_SORTER)
            .map(value => (
              <Chip
                key={value.name ?? ''}
                label={value.name ?? ''}
                className="ikigai-project-builder-chip-values"
                size="small"
                variant="outlined"
                onDelete={() => {
                  saveValues(selectedOptions.filter(e => e.id !== value.id));
                }}
              />
            ))}
        </div>
      </div>
      <Modal
        open={modalOpen}
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
                value={selectedOptions.slice()}
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
                    {option.name ?? ''}
                    {option.shortDescr && (
                      <>
                        :&nbsp;
                        <i>{option.shortDescr ?? ''}</i>
                      </>
                    )}
                  </li>
                )}
                getOptionLabel={option =>
                  (option.name ?? '') + ' ' + (option.shortDescr ?? '')
                }
                onChange={(_, options) => {
                  setSelectedOptions(options.slice().sort(OPTION_SORTER));
                }}
                renderTags={tagValue =>
                  tagValue.map(option => (
                    <Chip
                      key={option.id ?? 0}
                      label={option.name ?? ''}
                      className="ikigai-project-builder-chip-values"
                      size="small"
                      variant="outlined"
                      onDelete={() =>
                        saveValues(
                          selectedOptions.filter(e => e.id !== option.id)
                        )
                      }
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

export function IkigaiProjectBuilder(
  props: DeepReadOnly<{
    inputs: ProjectInput[];
    setInput: (input: DeepReadOnly<ProjectInput>) => void;
    noInputsText?: ReactNode;
    inputDiameter: (width: number, height: number) => number;
    distanceToInputCenter: (width: number, height: number) => number;
    enabled: boolean;
    onSpinClick: (inputs: DeepReadOnly<ProjectInput[]>) => void;
    style?: Partial<CSSProperties>;
  }>
) {
  return (
    <>
      <div style={props.style ?? {}} className="ikigai-project-builder">
        {(props?.inputs?.length ?? 0) > 0 && (
          <Ikigai
            id="ikigai"
            categoryDiameter={props.inputDiameter}
            distanceToCategoryCenter={props.distanceToInputCenter}
            radians={0}
            enabled={true}
            processing={false}
            categoryElementIds={props.inputs.map(i =>
              getCategoryId(i.input?.category).toString()
            )}
            showSpinButton={
              props.inputs.length > 0 &&
              props.inputs.every(
                e =>
                  (e.input?.selectedIds?.length ?? 0) > 0 ||
                  (e.input?.freeTexts?.length ?? 0) > 0
              )
            }
            onSpinClick={() => props.onSpinClick(props.inputs)}
            radiansOffset={0}
          >
            {props.inputs.map(i => {
              switch (
                i.input?.category?.valueType ??
                ValueType.UNSET_VALUE_TYPE
              ) {
                case pl_types.ProjectInputCategory.ValueType.FREE_TEXT:
                  return (
                    <FreeTextInput
                      key={getCategoryId(i.input?.category)}
                      input={i}
                      setInput={props.setInput}
                    />
                  );
                default:
                  return (
                    <DropdownSelectInput
                      key={getCategoryId(i.input?.category)}
                      input={i}
                      setInput={props.setInput}
                    />
                  );
              }
            })}
          </Ikigai>
        )}
        {(props?.inputs?.length ?? 0) === 0 && (
          <div className="ikigai-project-builder-no-categories">
            <span>
              {props.noInputsText ??
                'There are no categories for the Ikigai diagram.'}
            </span>
          </div>
        )}
      </div>
    </>
  );
}
