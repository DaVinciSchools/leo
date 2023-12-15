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
import {deepClone, deepReadOnly, DeepReadOnly} from '../misc';
import {getCategoryId, ProjectInput} from '../ProjectBuilder/ProjectBuilder';
import {getInputField} from '../form_utils/forms';
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

  function onClick() {
    setModalOpen(true);
    resetValuesAndOptions();
  }

  function onCancel() {
    setModalOpen(false);
    resetValuesAndOptions();
  }

  function save(newValues: DeepReadOnly<string[]>) {
    const newInput = deepClone(props.input);

    // Normalize new values.
    newInput.input.freeTexts = newValues
      .map(value => value.trim())
      .filter(value => value.length > 0)
      .sort(TEXT_SORTER);

    props.setInput(newInput);
  }

  function onSave() {
    setModalOpen(false);

    // Create a new input with the new text values.
    const newValues = [...values.values()];

    // Enter what was left over in the input field.
    const unenteredValue = getInputField(inputRef.current)?.value;
    if (unenteredValue) {
      newValues.push(unenteredValue);
    }

    save(newValues);
  }

  function deleteValue(value: string) {
    save([...values.values()].filter(v => v !== value));
  }

  useEffect(() => {
    resetValuesAndOptions();
  }, [props.input]);

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
        {values.size === 0 && (
          <div className="ikigai-project-builder-hint">
            <Markdown className="global-markdown">
              {props.input.input?.category?.hint ?? 'Enter a value'}
            </Markdown>
          </div>
        )}
        <div>
          {(props.input?.input?.freeTexts?.length ?? 0) > 0 &&
            [...(props.input?.input?.freeTexts ?? [])]
              .sort(TEXT_SORTER)
              .map(value => (
                <Chip
                  key={value}
                  label={value}
                  className="ikigai-project-builder-chip-values"
                  size="small"
                  variant="outlined"
                  onDelete={() => deleteValue(value)}
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

function DropdownSelectInput(
  props: DeepReadOnly<{
    input: ProjectInput;
    setInput: (input: DeepReadOnly<ProjectInput>) => void;
  }>
) {
  const [modalOpen, setModalOpen] = useState(false);
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

  function onClick() {
    setModalOpen(true);
    resetValuesAndOptions();
  }

  function onCancel() {
    setModalOpen(false);
    resetValuesAndOptions();
  }

  function save(newValues: DeepReadOnly<IOption[]>) {
    const newInput = deepClone(props.input);

    // Normalize new values.
    newInput.input.selectedIds = newValues
      .map(value => value.id)
      .filter(id => id != null && id !== 0)
      .map(id => id!);

    props.setInput(newInput);
  }

  function onSave() {
    setModalOpen(false);
    save([...values.values()]);
  }

  function deleteValue(value: IOption) {
    save([...values.values()].filter(v => v.id !== value.id));
  }

  useEffect(() => {
    resetValuesAndOptions();
  }, [props.input]);

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
        {values.size === 0 && (
          <div className="ikigai-project-builder-hint">
            <Markdown className="global-markdown">
              {props.input.input?.category?.hint ?? 'Enter a value'}
            </Markdown>
          </div>
        )}
        <div>
          {(props.input?.input?.selectedIds?.length ?? 0) > 0 &&
            [...(props.input?.input?.selectedIds ?? [])]
              .map(id => optionsById.get(id))
              .filter(option => option != null)
              .map(option => option!)
              .sort(OPTION_SORTER)
              .map(option => (
                <Chip
                  key={option.id}
                  label={option.name}
                  className="ikigai-project-builder-chip-values"
                  size="small"
                  variant="outlined"
                  onDelete={() => deleteValue(option)}
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
            id="ikigai-project-builder"
            categoryDiameter={(width, height) =>
              (Math.min(width, height) / 2) * 0.95
            }
            distanceToCategoryCenter={(width, height) =>
              (Math.min(width, height) / 4) * 0.85
            }
            radians={0}
            enabled={true}
            processing={false}
            categoryElementIds={props?.inputs?.map(input =>
              String(getCategoryId(input?.input?.category))
            )}
            showSpinButton={
              props?.inputs?.length > 0 &&
              props?.inputs?.every(
                input =>
                  (input?.input?.freeTexts?.length ?? 0) > 0 ||
                  (input?.input?.selectedIds?.length ?? 0) > 0
              )
            }
            onSpinClick={() => props.onSpinClick(props.inputs)}
            radiansOffset={0}
          >
            {props.inputs?.map(input => {
              switch (
                input?.input?.category?.valueType ??
                ValueType.UNSET_VALUE_TYPE
              ) {
                case pl_types.ProjectInputCategory.ValueType.FREE_TEXT:
                  return (
                    <FreeTextInput
                      key={getCategoryId(input?.input?.category)}
                      input={input}
                      setInput={props.setInput}
                    />
                  );
                default:
                  return (
                    <DropdownSelectInput
                      key={getCategoryId(input?.input?.category)}
                      input={input}
                      setInput={props.setInput}
                    />
                  );
              }
            })}
          </Ikigai>
        )}
        {((props?.inputs ?? []).length ?? 0) === 0 && (
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
