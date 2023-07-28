import './IkigaiBuilder.scss';
import {Input, Layout, Modal} from 'antd';
import {Ikigai} from '../../../Ikigai/Ikigai';
import {ChangeEvent, useEffect, useState} from 'react';
import {
  assignment_management,
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
import {
  HandleError,
  HandleErrorType,
} from '../../../libs/HandleError/HandleError';
import IAssignment = pl_types.IAssignment;
import AssignmentManagementService = assignment_management.AssignmentManagementService;
import {Autocomplete, TextField} from '@mui/material';
import IClassX = pl_types.IClassX;

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
  const [handleError, setHandleError] = useState<HandleErrorType>();
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
      <HandleError error={handleError} setError={setHandleError} />
      <div
        id={props.id}
        onClick={onClick}
        style={{overflow: 'hidden', width: 0, height: 0}}
      >
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
      <div
        id={props.id}
        onClick={onClick}
        style={{overflow: 'hidden', width: 0, height: 0}}
      >
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
                    {value.name!} - <i>{value.shortDescr!}</i>
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
  const [handleError, setHandleError] = useState<HandleErrorType>();
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const classXSorter = (a: IClassX, b: IClassX) =>
    (a?.name ?? '').localeCompare(b?.name ?? '');
  const assignmentSorter = (a: IAssignment, b: IAssignment) =>
    classXSorter(a?.classX ?? {}, b?.classX ?? {}) ||
    (a?.name ?? '').localeCompare(b?.name ?? '');

  const [processing, setProcessing] = useState(false);
  const navigate = useNavigate();

  const [assignments, setAssignments] = useState<IAssignment[] | null>(null);
  const [assignment, setAssignment] = useState<IAssignment | null>(null);
  const [projectDefinition, setProjectDefinition] =
    useState<pl_types.IProjectDefinition | null>(null);
  const [categories, setCategories] = useState<Category[]>([]);
  const [categoryValues, setCategoryValues] = useState<(string | number)[][]>(
    []
  );

  useEffect(() => {
    createService(AssignmentManagementService, 'AssignmentManagementService')
      .getAssignments({
        teacherId: user.isStudent ? undefined : user.teacherId,
        studentId: user.isStudent ? user.studentId : undefined,
      })
      .then(response => {
        setAssignments(response.assignments);
      })
      .catch(setHandleError);
  }, []);

  useEffect(() => {
    if (assignment == null) {
      setProjectDefinition(null);
      return;
    }
    createService(ProjectManagementService, 'ProjectManagementService')
      .getAssignmentProjectDefinition({assignmentId: assignment?.id})
      .then(response => setProjectDefinition(response.definition ?? null))
      .catch(setHandleError);
  }, [assignment]);

  useEffect(() => {
    if (projectDefinition == null) {
      setCategories([]);
      setCategoryValues([]);
      return;
    }
    setCategories(
      projectDefinition.inputs!.map<Category>(input => ({
        htmlId: `ikigaiCategory.${assignment?.id}.${projectDefinition.id}.${input.id}.${input.category?.id}`,
        input: input,
      }))
    );
    setCategoryValues([...projectDefinition.inputs!.map(() => [])]);
  }, [projectDefinition]);

  function onSpinClick() {
    setProcessing(true);

    createService(ProjectManagementService, 'ProjectManagementService')
      .generateProjects({
        definition: projectDefinition,
        assignmentId: assignment?.id,
      })
      .then(() => navigate('/projects/all-projects.html'))
      .catch(setHandleError);
  }

  function updateCategoryValues(index: number, values: (string | number)[]) {
    const newCategoryValues = [...categoryValues];
    newCategoryValues[index] = [...values];
    setCategoryValues(newCategoryValues);
  }

  return (
    <>
      <HandleError error={handleError} setError={setHandleError} />
      <DefaultPage
        title={
          <>
            <div
              style={{
                display: 'flex',
                flexFlow: 'row nowrap',
                gap: '1em',
                alignItems: 'center',
              }}
            >
              <span style={{whiteSpace: 'nowrap'}}>Ikigai Project Builder</span>
              <Autocomplete
                id="assignment"
                value={assignment}
                autoHighlight
                options={(assignments ?? []).sort(assignmentSorter)}
                onChange={(e, value) => setAssignment(value)}
                getOptionLabel={assignment =>
                  assignment?.name ?? '(Unnamed Assignment)'
                }
                isOptionEqualToValue={(option, value) =>
                  option?.id === value?.id
                }
                groupBy={assignment =>
                  assignment?.classX?.name ?? '(Unnamed Class)'
                }
                disabled={assignments == null}
                size="small"
                fullWidth={true}
                renderOption={(props, option) => {
                  return (
                    <li {...props} key={option.id}>
                      {option?.name ?? '(Unnamed Assignment)'}
                    </li>
                  );
                }}
                renderInput={params => (
                  <TextField {...params} label="Select Assignment" />
                )}
                loading={assignments == null}
                loadingText="Loading Assignments..."
              />
            </div>
          </>
        }
      >
        <Layout style={{height: '100%'}}>
          {categories.length === 0 && (
            <Content className="select-assignment">
              Select an assignment above...
            </Content>
          )}
          <Content style={{padding: '0.5em'}}>
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
              showSpinButton={
                categoryValues.length > 0 &&
                categoryValues.every(v => v.length > 0)
              }
              onSpinClick={onSpinClick}
              radiansOffset={0}
            >
              {categories.map((category, index) => {
                switch (category.input.category!.valueType!) {
                  case pl_types.ProjectInputCategory.ValueType.FREE_TEXT:
                    return (
                      <FreeTextInput
                        id={category.htmlId}
                        key={category.input.category!.id!}
                        shortTitle={category.input.category!.name!}
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
                        id={category.htmlId}
                        key={category.input.category!.id!}
                        shortTitle={category.input.category!.name!}
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
            Using the power of AI to create new and unique projects tailored to
            your individual preferences! Please be patient, this can take a few
            minutes.
          </div>
        </Modal>
      </DefaultPage>
    </>
  );
}
