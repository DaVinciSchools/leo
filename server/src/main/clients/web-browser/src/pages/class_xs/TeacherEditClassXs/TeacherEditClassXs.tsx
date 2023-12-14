import './TeacherEditClassXs.scss';
import '../../../libs/IkigaiProjectBuilder/IkigaiProjectBuilder.scss';

import {Add, Clear, Edit} from '@mui/icons-material';
import {
  Autocomplete,
  Button,
  Checkbox,
  Chip,
  Grid,
  TextField,
} from '@mui/material';
import {
  CLASS_X_SORTER,
  KNOWLEDGE_AND_SKILL_SORTER,
  SCHOOL_SORTER,
} from '../../../libs/sorters';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {addClassName, spread} from '../../../libs/tags';
import {
  class_x_management_service,
  pl_types,
  project_management,
  school_management,
} from 'pl-pb';
import {createService} from '../../../libs/protos';
import {useContext, useEffect, useState} from 'react';
import {useDelayedAction} from '../../../libs/delayed_action';
import {useFormFields} from '../../../libs/form_utils/forms';
import {KnowledgeAndSkillModal} from '../../../libs/KnowledgeAndSkillModal/KnowledgeAndSkillModal';
import {replaceInPlace, replaceOrAddInPlace} from '../../../libs/misc';
import ClassXManagementService = class_x_management_service.ClassXManagementService;
import IClassX = pl_types.IClassX;
import IKnowledgeAndSkill = pl_types.IKnowledgeAndSkill;
import ProjectManagementService = project_management.ProjectManagementService;
import Type = pl_types.KnowledgeAndSkill.Type;
import ISchool = pl_types.ISchool;
import SchoolManagementService = school_management.SchoolManagementService;

export function TeacherEditClassXs() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be a teacher for this overview.',
    userX => userX.isAdminX || userX.isTeacher
  );

  const [sortedClasses, setSortedClasses] = useState<readonly IClassX[]>([]);
  const [haveMultipleSchools, setHaveMultipleSchools] = useState(false);
  const [selectedClass, setSelectedClass] = useState<IClassX | null>(null);
  const [sortedKnowledgeAndSkills, setSortedKnowledgeAndSkills] = useState<
    readonly IKnowledgeAndSkill[]
  >([]);
  const [sortedSchools, setSortedSchools] = useState<readonly ISchool[]>([]);
  const [classSaveStatus, setClassSaveStatus] = useState<string>('');

  // --- AutoSave ---
  const autoSave = useDelayedAction(
    () => {
      setClassSaveStatus('Modified');
      if (selectedClass?.id != null) {
        const newClass = classFormFields.getValuesObject(true, selectedClass);
        setSortedClasses(
          replaceInPlace(sortedClasses.slice(), newClass, e => e?.id).sort(
            CLASS_X_SORTER
          )
        );
      }
    },
    () => {
      setClassSaveStatus('Saving...');
      if (selectedClass?.id != null && classFormFields.verifyOk(true)) {
        return createService(ClassXManagementService, 'ClassXManagementService')
          .upsertClassX({
            classX: classFormFields.getValuesObject(true, selectedClass),
          })
          .then(() => {
            setClassSaveStatus('Saved');
          })
          .catch(global.setError);
      } else {
        setClassSaveStatus('Invalid values, Not saved');
      }
      return;
    },
    1500
  );

  // --- Class Form ---

  const classFormFields = useFormFields({
    onChange: () => autoSave.trigger(),
    disabled: selectedClass == null,
  });
  const classSchool = classFormFields.useAutocompleteFormField<ISchool | null>(
    'school'
  );
  const className = classFormFields.useStringFormField('name', {
    maxLength: 255,
  });
  const classNumber = classFormFields.useStringFormField('number', {
    maxLength: 255,
  });
  const classEks = classFormFields.useAutocompleteFormField<
    IKnowledgeAndSkill,
    true
  >('knowledgeAndSkills');
  const classPeriod = classFormFields.useStringFormField('period', {
    maxLength: 16,
  });
  const classGrade = classFormFields.useStringFormField('grade', {
    maxLength: 16,
  });
  const classShortDescr = classFormFields.useStringFormField('shortDescr', {
    maxLength: 255,
  });
  const classLongDescrHtml = classFormFields.useStringFormField(
    'longDescrHtml',
    {
      maxLength: 255,
    }
  );

  // --- New / Edit EKS Form ---

  const [editKs, setEditKs] = useState<IKnowledgeAndSkill | undefined>();
  const [editKsOp, setEditKsOp] = useState<'ADD' | 'UPDATE'>('ADD');

  // --- Effects ---

  useEffect(() => {
    if (userX == null) {
      setSelectedClass(null);
      setHaveMultipleSchools(false);
      setSortedSchools([]);
      setSortedClasses([]);
      setSortedKnowledgeAndSkills([]);
      return;
    }
    createService(SchoolManagementService, 'SchoolManagementService')
      .getSchools({districtId: userX.districtId})
      .then(response => setSortedSchools(response.schools.sort(SCHOOL_SORTER)))
      .catch(global.setError);
    createService(ClassXManagementService, 'ClassXManagementService')
      .getClassXs({
        teacherIds: [userX.teacherId ?? 0],
        includeKnowledgeAndSkills: true,
        includeSchool: true,
      })
      .then(response => {
        setSortedClasses(response.classXs.sort(CLASS_X_SORTER));
        setHaveMultipleSchools(false);
      })
      .catch(global.setError);
    createService(ProjectManagementService, 'ProjectManagementService')
      .getKnowledgeAndSkills({types: [Type.EKS]})
      .then(response =>
        setSortedKnowledgeAndSkills(
          response.knowledgeAndSkills.sort(KNOWLEDGE_AND_SKILL_SORTER)
        )
      )
      .catch(global.setError);
  }, [userX]);

  useEffect(() => {
    setHaveMultipleSchools(
      new Set(sortedClasses.map(e => e.school?.id)).size > 1
    );
  }, [sortedClasses]);

  useEffect(() => {
    classFormFields.setValuesObject(selectedClass ?? {});
  }, [selectedClass]);

  function createNewClass() {
    if (!userX) {
      return;
    }

    createService(ClassXManagementService, 'ClassXManagementService')
      .upsertClassX({
        classX: {
          name: 'New Class',
          number: (userX.lastName ?? 'CLASS').toUpperCase() + ' 101',
        },
      })
      .then(response => {
        setSortedClasses(
          [...sortedClasses, response.classX!].sort(CLASS_X_SORTER)
        );
        setSelectedClass(response.classX!);
      })
      .catch(global.setError);
  }

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Edit Classes">
        <Grid container spacing={2}>
          <Grid item xs={12} className="section-heading">
            <div className="section-title">Select Class:</div>
          </Grid>
          <Grid item {...spread({sm: 12, md: 7})}>
            <Autocomplete
              autoHighlight
              autoFocus
              value={selectedClass}
              options={sortedClasses}
              isOptionEqualToValue={(option, value) => option.id === value.id}
              groupBy={
                !haveMultipleSchools
                  ? undefined
                  : option => option.school?.name ?? 'No School Selected'
              }
              renderOption={(props, option) => (
                <li {...props} key={option.id}>
                  {option.number}:&nbsp;
                  <i>{option.name}</i>
                </li>
              )}
              renderInput={params => (
                <TextField
                  {...params}
                  label="Select class"
                  size="small"
                  variant="outlined"
                />
              )}
              getOptionLabel={option =>
                (option?.number ?? 'undefined') +
                ': ' +
                (option?.name ?? 'undefined')
              }
              onChange={(ignore, option) => {
                autoSave.forceDelayedAction(() => {
                  setSelectedClass(option);
                });
              }}
            />
          </Grid>
          <Grid
            item
            {...spread({sm: 12, md: 5})}
            className="teacher-edit-classes-expand-buttons"
          >
            <Button
              variant="contained"
              startIcon={<Add />}
              onClick={createNewClass}
            >
              New Class
            </Button>
            <Button
              variant="contained"
              color="warning"
              disabled={selectedClass == null}
              startIcon={<Clear />}
              onClick={() => window.alert('Not implemented yet.')}
            >
              Delete Class (TODO)
            </Button>
          </Grid>
        </Grid>
        <form style={{paddingTop: '2em'}} {...classFormFields.formParams()}>
          <Grid container spacing={2}>
            <Grid item xs={12} className="section-heading">
              <div className="section-title">Edit Class:</div>
              <div className="section-links">
                <div
                  style={{display: selectedClass == null ? 'none' : undefined}}
                >
                  {classSaveStatus}
                </div>
              </div>
            </Grid>
            <Grid item xs={12}>
              <Autocomplete
                autoHighlight
                options={sortedSchools}
                isOptionEqualToValue={(option, value) => option.id === value.id}
                renderInput={params => (
                  <TextField
                    label="Select school"
                    {...classSchool.textFieldParams(params)}
                  />
                )}
                renderOption={(props, option) => (
                  <li {...props} key={option.id}>
                    {option.name ?? 'School is Unnamed'}
                  </li>
                )}
                getOptionLabel={option => option?.name ?? 'School is Unnamed'}
                {...classSchool.autocompleteParams()}
              />
            </Grid>
            <Grid item {...spread({sm: 12, md: 4})}>
              <TextField
                required
                label="Class ID"
                {...classNumber.textFieldParams()}
              />
            </Grid>
            <Grid item {...spread({sm: 12, md: 8})}>
              <TextField
                required
                label="Class Name"
                {...className.textFieldParams()}
              />
            </Grid>
            <Grid item {...spread({sm: 12, md: 9.5})}>
              <Autocomplete
                autoHighlight
                disableCloseOnSelect
                options={sortedKnowledgeAndSkills}
                isOptionEqualToValue={(option, value) => option.id === value.id}
                renderInput={params => (
                  <TextField
                    label="Select knowledge and skills"
                    autoComplete="off"
                    {...classEks.textFieldParams(params)}
                  />
                )}
                renderOption={(props, option, {selected}) => (
                  <li {...props} key={option.id}>
                    <Checkbox style={{marginRight: 8}} checked={selected} />
                    {option.name}:&nbsp;
                    <i>{option.shortDescr ?? 'undefined'}</i>{' '}
                    {((option.userX?.id != null &&
                      option.userX?.id === userX.id) ||
                      userX.isAdminX) && (
                      <>
                        &nbsp;
                        <Edit
                          onClick={e => {
                            setEditKsOp('UPDATE');
                            setEditKs(option);
                            e.stopPropagation();
                          }}
                        />
                      </>
                    )}
                  </li>
                )}
                getOptionLabel={option =>
                  option.name + ': ' + (option?.shortDescr ?? 'undefined')
                }
                renderTags={(tagValue, getTagProps) =>
                  tagValue.map((option, index) => (
                    <Chip
                      label={option.name}
                      size="small"
                      variant="outlined"
                      {...addClassName(
                        getTagProps({index}),
                        'teacher-edit-classes-chips'
                      )}
                    />
                  ))
                }
                {...classEks.autocompleteParams()}
              />
            </Grid>
            <Grid item {...spread({sm: 12, md: 2.5})}>
              <Button
                variant="contained"
                className="teacher-edit-classes-expand-buttons"
                startIcon={<Add />}
                onClick={() => {
                  setEditKsOp('ADD');
                  setEditKs({type: Type.EKS, global: true});
                }}
                disabled={selectedClass == null}
              >
                New EKS
              </Button>
            </Grid>
            <Grid item {...spread({sm: 12, md: 6})}>
              <TextField label="Period" {...classPeriod.textFieldParams()} />
            </Grid>
            <Grid item {...spread({sm: 12, md: 6})}>
              <TextField label="Grade" {...classGrade.textFieldParams()} />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="Short Description"
                {...classShortDescr.textFieldParams()}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="Long Description"
                {...classLongDescrHtml.textFieldParams()}
              />
            </Grid>
          </Grid>
        </form>
        <KnowledgeAndSkillModal
          open={editKs != null}
          value={editKs}
          title={
            editKsOp === 'ADD'
              ? 'Create a New Knowledge and Skill'
              : 'Edit Knowledge and Skill'
          }
          okText={editKsOp === 'ADD' ? 'Add EKS' : 'Update EKS'}
          onClose={() => setEditKs(undefined)}
          onOk={ks => {
            createService(ProjectManagementService, 'ProjectManagementService')
              .upsertKnowledgeAndSkill({knowledgeAndSkill: ks})
              .then(response => {
                setSortedKnowledgeAndSkills(
                  replaceOrAddInPlace(
                    sortedKnowledgeAndSkills.slice(),
                    response.knowledgeAndSkill!,
                    e => e.id
                  ).sort(KNOWLEDGE_AND_SKILL_SORTER)
                );
                if (editKsOp === 'ADD') {
                  // Add the knowledge and skill to the class.
                  classEks.setValue(
                    replaceOrAddInPlace(
                      classEks.getValue()?.slice() ?? [],
                      response.knowledgeAndSkill!,
                      e => e.id
                    ).sort(KNOWLEDGE_AND_SKILL_SORTER)
                  );
                } else {
                  classEks.setValue(
                    replaceInPlace(
                      classEks.getValue()?.slice() ?? [],
                      response.knowledgeAndSkill!,
                      e => e.id
                    ).sort(KNOWLEDGE_AND_SKILL_SORTER)
                  );
                }
              })
              .catch(global.setError)
              .finally(() => setEditKs(undefined));
          }}
        />
      </DefaultPage>
    </>
  );
}
