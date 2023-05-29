import './IkigaiBuilder.scss';
import {Input, Layout, Modal} from 'antd';
import {Ikigai} from '../../../Ikigai/Ikigai';
import {ChangeEvent, useEffect, useState} from 'react';
import {
  createService,
  pl_types,
  project_management,
} from '../../../libs/protos';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {MinusCircleOutlined, PlusCircleOutlined} from '@ant-design/icons';
import ProjectManagementService = project_management.ProjectManagementService;
import {useNavigate} from 'react-router';
import ISelectionOption = pl_types.ProjectInputCategory.IOption;

const {Content} = Layout;

function FreeTextInput(props: {
  id: string;
  shortTitle: string;
  hint?: string;
  placeholder: string;
  values: string[];
  onValuesUpdated: (values: string[]) => void;
  maxNumberOfValues: number;
}) {
  const [modalOpen, setModalOpen] = useState(false);
  const [editingValues, setEditingValues] = useState<string[]>([]);

  function onClick() {
    const valuesCopy = [...props.values];
    if (valuesCopy.length === 0) {
      valuesCopy.push('');
    }
    setEditingValues(valuesCopy);
    setModalOpen(true);
  }

  function onOk() {
    setModalOpen(false);
    props.onValuesUpdated(editingValues.filter(value => value.length > 0));
  }

  function onCancel() {
    setModalOpen(false);
  }

  function setEditingValue(index: number, value: string) {
    const valuesCopy = [...editingValues];
    valuesCopy[index] = value;
    setEditingValues(valuesCopy);
  }

  function removeEditingValue(index: number) {
    const valuesCopy = [...editingValues];
    valuesCopy.splice(index, 1);
    if (valuesCopy.length === 0) {
      valuesCopy.push('');
    }
    setEditingValues(valuesCopy);
  }

  function addEditingValue() {
    setEditingValues([...editingValues, '']);
  }

  return (
    <>
      <div id={props.id} onClick={onClick}>
        <div className="panel">
          <div className="title">{props.shortTitle}</div>
          {props.values.length > 0 ? (
            <div
              className="values"
              style={{
                textAlign: 'left',
                width: 'fit-content',
                textOverflow: 'ellipsis',
              }}
            >
              {props.values.map((value, index) => (
                <span
                  key={index}
                  style={{
                    paddingLeft: '0.5em',
                    whiteSpace: 'nowrap',
                  }}
                >
                  - {value}
                  <br />
                </span>
              ))}
            </div>
          ) : (
            <div className="hint">
              {props.hint != null ? props.hint : 'Select to modify.'}
            </div>
          )}
        </div>
      </div>
      <Modal
        title={props.shortTitle}
        open={modalOpen}
        closable={true}
        onOk={onOk}
        onCancel={onCancel}
      >
        {editingValues.map((value, index) => (
          <div key={index} className="line-item">
            <Input
              placeholder={props.placeholder}
              maxLength={255}
              onChange={(e: ChangeEvent<HTMLInputElement>) => {
                setEditingValue(index, e.target.value);
              }}
              value={value}
            />
            <MinusCircleOutlined
              style={{
                visibility: editingValues.length > 1 ? 'visible' : 'hidden',
              }}
              onClick={() => removeEditingValue(index)}
            />
          </div>
        ))}
        <PlusCircleOutlined
          className="add-line-item"
          onClick={() => addEditingValue()}
          style={{
            display:
              editingValues.length < props.maxNumberOfValues
                ? 'inline-block'
                : 'none',
          }}
        />
      </Modal>
    </>
  );
}

function DropdownSelectInput(props: {
  id: string;
  shortTitle: string;
  hint?: string;
  placeholder: string;
  options: Map<number, ISelectionOption>;
  values: number[];
  onValuesUpdated: (values: number[]) => void;
  maxNumberOfValues: number;
}) {
  const [modalOpen, setModalOpen] = useState(false);
  const [editingValues, setEditingValues] = useState<number[]>([]);

  function onClick() {
    const valuesCopy = [...props.values];
    if (valuesCopy.length === 0) {
      valuesCopy.push(-1);
    }
    setEditingValues(valuesCopy);
    setModalOpen(true);
  }

  function onOk() {
    setModalOpen(false);
    props.onValuesUpdated(
      editingValues
        .map(id => props.options.get(id))
        .filter(value => value != null)
        .map(value => value!.id!)
    );
  }

  function onCancel() {
    setModalOpen(false);
  }

  function setEditingValue(index: number, value: number) {
    const valuesCopy = [...editingValues];
    valuesCopy[index] = value;
    setEditingValues(valuesCopy);
  }

  function removeEditingValue(index: number) {
    const valuesCopy = [...editingValues];
    valuesCopy.splice(index, 1);
    if (valuesCopy.length === 0) {
      valuesCopy.push(-1);
    }
    setEditingValues(valuesCopy);
  }

  function addEditingValue() {
    setEditingValues([...editingValues, -1]);
  }

  return (
    <>
      <div id={props.id} onClick={onClick}>
        <div className="panel">
          <div className="title">{props.shortTitle}</div>
          <div className="body">
            {props.values.length > 0 ? (
              <div>
                <div style={{textAlign: 'left'}}>
                  {props.values.map(value => (
                    <span key={value} style={{whiteSpace: 'nowrap'}}>
                      - {props.options.get(value)?.name ?? '[error]'} <br />
                    </span>
                  ))}
                </div>
              </div>
            ) : (
              <div className="hint">
                {props.hint != null ? props.hint : 'Select to modify.'}
              </div>
            )}
          </div>
        </div>
      </div>
      <Modal
        title={props.shortTitle}
        open={modalOpen}
        closable={true}
        onOk={onOk}
        onCancel={onCancel}
      >
        {editingValues.map((id, index) => (
          <div key={index} className="line-item">
            <select
              value={id}
              onChange={(e: ChangeEvent<HTMLSelectElement>) => {
                setEditingValue(index, Number.parseInt(e.target.value));
              }}
            >
              <option key={-1} value={-1}>
                {props.placeholder}
              </option>
              {Array.from(props.options).map(([key, value]) => (
                <option key={key} value={key}>
                  <>
                    {value.name!} - <i>{value.description!}</i>
                  </>
                </option>
              ))}
            </select>
            <MinusCircleOutlined
              style={{
                visibility: editingValues.length > 1 ? 'visible' : 'hidden',
              }}
              onClick={() => removeEditingValue(index)}
            />
          </div>
        ))}
        <PlusCircleOutlined
          className="add-line-item"
          onClick={() => addEditingValue()}
          style={{
            display:
              editingValues.length < props.maxNumberOfValues
                ? 'inline-block'
                : 'none',
          }}
        />
      </Modal>
    </>
  );
}

type Category = {
  htmlId: string;
  input: pl_types.IProjectInputValue;
};

export function IkigaiBuilder() {
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const [processing, setProcessing] = useState(false);
  const navigate = useNavigate();

  const [projectDefinition, setProjectDefinition] = useState<
    pl_types.IProjectDefinition | undefined
  >();
  const [categories, setCategories] = useState<Category[]>([]);
  const [categoryValues, setCategoryValues] = useState<(string | number)[][]>(
    []
  );
  useEffect(() => {
    const service = createService(
      ProjectManagementService,
      'ProjectManagementService'
    );
    service
      .getProjectDefinition({})
      .then(response => setProjectDefinition(response.definition!));
  }, []);
  useEffect(() => {
    if (projectDefinition != null) {
      setCategories(
        projectDefinition.inputs!.map<Category>((input, index) => ({
          htmlId: `ikigaiCategory${index}`,
          input: input,
        }))
      );
      setCategoryValues([...projectDefinition.inputs!.map(() => [])]);
    } else {
      setCategories([]);
      setCategoryValues([]);
    }
  }, [projectDefinition]);

  // function getLovesRelatedSuggestions() {
  //   const partialTextOpenAiPromptService = createService(
  //     PartialTextOpenAiPromptService,
  //     'PartialTextOpenAiPromptService'
  //   );
  //   setLovesGetRelatedSuggestionsEnabled(false);
  //   partialTextOpenAiPromptService
  //     .getSuggestions({
  //       partialText: modalLovesValue,
  //       prompt: Prompt.SUGGEST_THINGS_YOU_LOVE,
  //       userXId: user!.userXId!,
  //     })
  //     .then(response => setLovesSuggestions(response.suggestions))
  //     .catch(() => setLovesSuggestions([]))
  //     .finally(() => setLovesGetRelatedSuggestionsEnabled(true));
  // }

  function onSpinClick() {
    setProcessing(true);

    const service = createService(
      ProjectManagementService,
      'ProjectManagementService'
    );

    service
      .generateProjects({definition: projectDefinition})
      .finally(() => navigate('/projects/my-projects'));
  }

  function updateCategoryValues(index: number, values: (string | number)[]) {
    const newCategoryValues = [...categoryValues];
    newCategoryValues[index] = [...values];
    setCategoryValues(newCategoryValues);
  }

  return (
    <>
      <DefaultPage title="Ikigai Project Builder">
        <Layout style={{height: '100%'}}>
          <Content style={{borderRight: '#F0781F solid 1px', padding: '0.5em'}}>
            <Ikigai
              id="ikigai-builder"
              categoryDiameter={(width, height) => Math.min(width, height) / 2}
              distanceToCategoryCenter={(width, height) =>
                (Math.min(width, height) / 2) * 0.45
              }
              radians={0}
              enabled={!processing}
              processing={processing}
              categoryElementIds={categories.map(c => c.htmlId)}
              showSpinButton={categoryValues.every(v => v.length > 0)}
              onSpinClick={onSpinClick}
              radiansOffset={0}
            >
              {categories.map((category, index) => {
                switch (category.input.category!.valueType!) {
                  case pl_types.ProjectInputCategory.ValueType.FREE_TEXT:
                    return (
                      <FreeTextInput
                        id={`${category.htmlId}`}
                        key={category.input.category!.id!}
                        shortTitle={category.input.category!.title!}
                        hint={category.input.category!.hint!}
                        placeholder={category.input.category!.placeholder!}
                        values={categoryValues[index] as string[]}
                        onValuesUpdated={values => {
                          updateCategoryValues(index, values);
                          category.input.freeTexts = [...values];
                        }}
                        maxNumberOfValues={
                          category.input.category!.maxNumValues!
                        }
                      />
                    );
                  default:
                    return (
                      <DropdownSelectInput
                        id={`${category.htmlId}`}
                        key={category.input.category!.id!}
                        shortTitle={category.input.category!.title!}
                        hint={category.input.category!.hint!}
                        placeholder={category.input.category!.placeholder!}
                        values={categoryValues[index] as number[]}
                        onValuesUpdated={values => {
                          updateCategoryValues(index, values);
                          category.input.selectedIds = [...values];
                        }}
                        maxNumberOfValues={
                          category.input.category!.maxNumValues!
                        }
                        options={
                          new Map(
                            category.input.category!.options!.map(i => [
                              i.id!,
                              i,
                            ])
                          )
                        }
                      />
                    );
                }
              })}
            </Ikigai>
          </Content>
        </Layout>
        <Modal
          centered
          width="50%"
          open={processing}
          closable={false}
          footer={null}
        >
          <div
            style={{
              textAlign: 'center',
              width: '100%',
            }}
          >
            Finding great projects! Please wait. This can take a few minutes.
          </div>
        </Modal>
      </DefaultPage>
    </>
  );
}
