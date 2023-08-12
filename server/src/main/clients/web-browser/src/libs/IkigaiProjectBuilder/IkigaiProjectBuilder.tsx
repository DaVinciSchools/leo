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
import {pl_types} from '../../generated/protobuf-js';

import IOption = pl_types.ProjectInputCategory.IOption;
import ValueType = pl_types.ProjectInputCategory.ValueType;
import {OPTION_SORTER, TEXT_SORTER} from '../sorters';
import {getInputField} from '../forms';
import {addClassName} from '../tags';

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

interface CategoryElement {
  category: pl_types.IProjectInputValue;
  htmlId: string;
  hue: number;
  stringValues: string[];
  optionValues: IOption[];
}

function FreeTextInput(props: {
  id: string;
  hue: number;
  title: string;
  description: string;
  hint: string;
  placeholder: string;
  options: IOption[];
  values: string[];
  onValuesUpdated: (values: string[]) => void;
  maxNumberOfValues: number;
}) {
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedValues, setSelectedValues] = useState<string[]>([]);
  const [optionValues, setOptionValues] = useState<string[]>([]);
  const inputRef = useRef<HTMLInputElement>();

  useEffect(
    () =>
      setOptionValues(
        props.options.map(e => e?.name ?? 'undefined').sort(TEXT_SORTER)
      ),
    [props.options]
  );

  function onClick() {
    setSelectedValues([...props.values.sort(TEXT_SORTER)]);
    setModalOpen(true);
  }

  function onCancel() {
    setModalOpen(false);
  }

  function onSave() {
    setModalOpen(false);
    const unenteredValue = getInputField(
      getInputField(inputRef.current)
    )?.value?.trim();
    if (unenteredValue != null && unenteredValue !== '') {
      selectedValues.push(unenteredValue);
    }
    props.onValuesUpdated([
      ...new Set(selectedValues.map(e => e.trim()).filter(e => e.length > 0)),
    ]);
  }

  return (
    <>
      <div
        id={props.id}
        onClick={onClick}
        className="ikigai-project-builder-panel"
      >
        <div className="ikigai-project-builder-title">{props.title}</div>
        {props.values.length === 0 && (
          <div className="ikigai-project-builder-hint">{props.hint}</div>
        )}
        <div>
          {props.values.length > 0 &&
            props.values.sort(TEXT_SORTER).map(value => (
              <Chip
                key={value}
                label={value}
                className="ikigai-project-builder-chip-values"
                size="small"
                variant="outlined"
                onDelete={() => {
                  props.onValuesUpdated(props.values.filter(e => e !== value));
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
          title={props.title}
          headerColor={`hsla(${props.hue}, 100%, 75%, ${VISIBLE_ALPHA})`}
          highlightColor={`hsla(${props.hue}, 100%, 75%, 100%)`}
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
              <div style={{paddingBottom: '1em'}}>{props.description}</div>
              <Autocomplete
                ref={inputRef}
                multiple
                freeSolo
                autoFocus
                value={selectedValues}
                renderInput={params => (
                  <TextField
                    {...params}
                    label={props.placeholder}
                    variant="standard"
                  />
                )}
                options={optionValues}
                renderOption={(props, option) => <li {...props}>{option}</li>}
                onChange={(e, options) => {
                  setSelectedValues(options.sort(TEXT_SORTER));
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

function DropdownSelectInput(props: {
  id: string;
  hue: number;
  title: string;
  description: string;
  hint: string;
  placeholder: string;
  options: IOption[];
  values: IOption[];
  onValuesUpdated: (values: IOption[]) => void;
  maxNumberOfValues: number;
}) {
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedValues, setSelectedValues] = useState<IOption[]>([]);

  function onClick() {
    setSelectedValues([...props.values.sort(OPTION_SORTER)]);
    setModalOpen(true);
  }

  function onCancel() {
    setModalOpen(false);
  }

  function onSave() {
    setModalOpen(false);
    props.onValuesUpdated([...selectedValues]);
  }

  return (
    <>
      <div
        id={props.id}
        onClick={onClick}
        className="ikigai-project-builder-panel"
      >
        <div className="ikigai-project-builder-title">{props.title}</div>
        {props.values.length === 0 && (
          <span className="ikigai-project-builder-hint">{props.hint}</span>
        )}
        <div>
          {props.values.length > 0 &&
            props.values.sort(OPTION_SORTER).map(value => (
              <Chip
                key={value.name ?? 'undefined'}
                label={value.name ?? 'undefined'}
                className="ikigai-project-builder-chip-values"
                size="small"
                variant="outlined"
                onDelete={() => {
                  props.onValuesUpdated(
                    props.values.filter(e => e.id !== value.id)
                  );
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
          title={props.title}
          headerColor={`hsla(${props.hue}, 100%, 75%, ${VISIBLE_ALPHA})`}
          highlightColor={`hsla(${props.hue}, 100%, 75%, 100%)`}
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
              <div style={{paddingBottom: '1em'}}>{props.description}</div>
              <Autocomplete
                multiple
                autoHighlight
                autoFocus
                disableCloseOnSelect
                value={selectedValues}
                groupBy={option => option.category ?? ''}
                renderInput={params => (
                  <TextField
                    {...params}
                    label={props.placeholder}
                    variant="standard"
                  />
                )}
                options={props.options.sort(OPTION_SORTER)}
                renderOption={(props, option, {selected}) => (
                  <li {...props}>
                    <Checkbox style={{marginRight: 8}} checked={selected} />
                    {option?.name ?? 'undefined'}:&nbsp;
                    <i>{option?.shortDescr ?? 'undefined'}</i>
                  </li>
                )}
                getOptionLabel={option =>
                  (option?.name ?? 'undefined') +
                  ' ' +
                  (option?.shortDescr ?? 'undefined')
                }
                onChange={(e, options) => {
                  setSelectedValues(options.sort(OPTION_SORTER));
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

export function IkigaiProjectBuilder(props: {
  id: string;
  categoryStyle?: Partial<CSSProperties>;
  categories: pl_types.IProjectInputValue[];
  noCategoriesText?: ReactNode;
  categoryDiameter: (width: number, height: number) => number;
  distanceToCategoryCenter: (width: number, height: number) => number;
  enabled: boolean;
  onSpinClick: (configuration: pl_types.IProjectInputValue[]) => void;
  style?: Partial<CSSProperties>;
}) {
  const [categoryElements, setCategoryElements] = useState<CategoryElement[]>(
    []
  );

  useEffect(() => {
    const newCategoryElements: CategoryElement[] = [];
    for (let i = 0; i < props.categories.length; ++i) {
      const category = props.categories[i];
      const optionsIndex = new Map(
        (category.category?.options ?? []).map(e => [e.id, e])
      );
      newCategoryElements.push({
        category: category,
        htmlId:
          props.id +
          '.' +
          (category?.id ?? 'undefined') +
          '.' +
          (category?.category?.id ?? 'undefined') +
          '.' +
          (category?.category?.typeId ?? 'undefined'),
        hue: i * (360 / props.categories.length),
        stringValues: category.freeTexts ?? [],
        optionValues:
          (category.selectedIds
            ?.map(k => optionsIndex.get(k))
            ?.filter(e => e != null) as IOption[]) ?? [],
      });
    }
    setCategoryElements(newCategoryElements);
  }, [props.categories]);

  return (
    <>
      <div style={props.style ?? {}} className="ikigai-project-builder">
        {(props?.categories?.length ?? 0) > 0 && (
          <Ikigai
            id={props.id}
            categoryDiameter={(width, height) =>
              (Math.min(width, height) / 2) * 0.95
            }
            distanceToCategoryCenter={(width, height) =>
              (Math.min(width, height) / 4) * 0.85
            }
            radians={0}
            enabled={true}
            processing={false}
            categoryElementIds={categoryElements.map(e => e.htmlId)}
            showSpinButton={
              categoryElements.length > 0 &&
              categoryElements.every(
                e => e.stringValues.length > 0 || e.optionValues.length > 0
              )
            }
            onSpinClick={() =>
              props.onSpinClick(
                categoryElements.map(c => {
                  c.category.freeTexts = [...(c.stringValues ?? [])];
                  c.category.selectedIds = (c.optionValues ?? []).map(
                    o => o.id!
                  );
                  return c.category;
                })
              )
            }
            radiansOffset={0}
          >
            {categoryElements.map(e => {
              switch (e.category?.category?.valueType ?? ValueType.UNSET) {
                case pl_types.ProjectInputCategory.ValueType.FREE_TEXT:
                  return (
                    <FreeTextInput
                      id={e.htmlId}
                      key={e.htmlId}
                      hue={e.hue}
                      title={e.category?.category?.name ?? 'undefined'}
                      description={
                        e.category?.category?.inputDescr ?? 'undefined'
                      }
                      hint={e.category?.category?.hint ?? 'undefined'}
                      placeholder={
                        e.category?.category?.placeholder ?? 'undefined'
                      }
                      onValuesUpdated={values => {
                        e.stringValues = [...values];
                        setCategoryElements([...categoryElements]);
                      }}
                      maxNumberOfValues={
                        e.category?.category?.maxNumValues ?? 1
                      }
                      values={e.stringValues}
                      options={e.category?.category?.options ?? []}
                    />
                  );
                default:
                  return (
                    <DropdownSelectInput
                      id={e.htmlId}
                      key={e.htmlId}
                      hue={e.hue}
                      title={e.category?.category?.name ?? 'undefined'}
                      description={
                        e.category?.category?.inputDescr ?? 'undefined'
                      }
                      hint={e.category?.category?.hint ?? 'undefined'}
                      placeholder={
                        e.category?.category?.placeholder ?? 'undefined'
                      }
                      onValuesUpdated={values => {
                        e.optionValues = [...values];
                        setCategoryElements([...categoryElements]);
                      }}
                      maxNumberOfValues={
                        e.category?.category?.maxNumValues ?? 1
                      }
                      values={e.optionValues}
                      options={e.category?.category?.options ?? []}
                    />
                  );
              }
            })}
          </Ikigai>
        )}
        {(props?.categories?.length ?? 0) === 0 && (
          <div className="ikigai-project-builder-no-categories">
            <span>
              {props.noCategoriesText ??
                'There are no categories for the Ikigai diagram.'}
            </span>
          </div>
        )}
      </div>
    </>
  );
}
