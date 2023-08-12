import './TeacherEditClasses.scss';
import '../../../libs/IkigaiProjectBuilder/IkigaiProjectBuilder.scss';

import {
  Autocomplete,
  Button,
  Checkbox,
  Chip,
  FormControlLabel,
  Grid,
  TextField,
} from '@mui/material';
import {CLASS_SORTER, KNOWLEDGE_AND_SKILL_SORTER} from '../../../libs/sorters';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {
  class_management_service,
  pl_types,
  project_management,
} from '../../../generated/protobuf-js';
import {createService} from '../../../libs/protos';
import {useContext, useEffect, useState} from 'react';
import {useFormFields} from '../../../libs/forms';
import {addClassName, spread} from '../../../libs/tags';
import {Add, Clear} from '@mui/icons-material';
import {StandardModal} from '../../../libs/StandardModal/StandardModal';
import ClassManagementService = class_management_service.ClassManagementService;
import IClassX = pl_types.IClassX;
import ProjectManagementService = project_management.ProjectManagementService;
import IKnowledgeAndSkill = pl_types.IKnowledgeAndSkill;
import Type = pl_types.KnowledgeAndSkill.Type;

export function TeacherEditClasses() {
  const global = useContext(GlobalStateContext);

  const [classes, setClasses] = useState<IClassX[]>([]);
  const [selectedClass, setSelectedClass] = useState<IClassX | null>(null);
  const [sortedKnowledgeAndSkills, setSortedKnowledgeAndSkills] = useState<
    IKnowledgeAndSkill[]
  >([]);
  const [
    sortedSelectedKnowledgeAndSkills,
    setSortedSelectedKnowledgeAndSkills,
  ] = useState<IKnowledgeAndSkill[]>([]);

  const formFields = useFormFields();
  const name = formFields.useStringFormField('name', {maxLength: 255});
  const number = formFields.useStringFormField('number', {maxLength: 16});
  const eks = formFields.useStringFormField('eks', {
    maxLength: 255,
  });
  const period = formFields.useStringFormField('period', {maxLength: 16});
  const grade = formFields.useStringFormField('grade', {maxLength: 16});
  const shortDescr = formFields.useStringFormField('shortDescr', {
    maxLength: 255,
  });
  const longDescr = formFields.useStringFormField('longDescr', {
    maxLength: 255,
  });

  const [showNewEks, setShowNewEks] = useState<boolean>(false);
  const eksFormFields = useFormFields();
  const eksName = eksFormFields.useStringFormField('name', {maxLength: 255});
  const eksShortDescr = eksFormFields.useStringFormField('short_descr', {
    maxLength: 255,
  });
  const eksGlobal = eksFormFields.useBooleanFormField('global', {
    isBoolean: true,
  });

  useEffect(() => {
    createService(ProjectManagementService, 'ProjectManagementService')
      .getKnowledgeAndSkills({types: [Type.EKS]})
      .then(response =>
        setSortedKnowledgeAndSkills(
          response.knowledgeAndSkills.sort(KNOWLEDGE_AND_SKILL_SORTER)
        )
      )
      .catch(global.setError);
  }, []);

  useEffect(() => {
    if (global.user == null) {
      setClasses([]);
      setSelectedClass(null);
      return;
    }
    createService(ClassManagementService, 'ClassManagementService')
      .getClasses({teacherId: global.user.teacherId})
      .then(response => setClasses(response.classes))
      .catch(global.setError);
  }, [global.user]);

  useEffect(() => {
    if (showNewEks) {
      eksFormFields.reset();
    }
  }, [showNewEks]);

  if (!global.requireUser(user => user?.isTeacher || user?.isAdmin)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Edit Classes">
        <Grid container spacing={2}>
          <Grid item {...spread({sm: 12, md: 7})}>
            <Autocomplete
              autoHighlight
              autoFocus
              value={selectedClass}
              options={classes.sort(CLASS_SORTER)}
              isOptionEqualToValue={(option, value) => option.id === value.id}
              //eslint-disable-next-line @typescript-eslint/no-unused-vars
              renderOption={(props, option, {selected}) => (
                <li {...props}>
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
              onChange={(e, option) => {
                setSelectedClass(option);
                formFields.setValuesObject(option);
              }}
            />
          </Grid>
          <Grid
            item
            {...spread({sm: 12, md: 5})}
            className="teacher-edit-classes-expand-buttons"
          >
            <Button variant="contained" startIcon={<Add />}>
              New Class
            </Button>
            <Button
              variant="contained"
              color="warning"
              disabled={selectedClass == null}
              startIcon={<Clear />}
            >
              Delete Class
            </Button>
          </Grid>
        </Grid>
        <form style={{paddingTop: '2em'}}>
          <Grid container spacing={2}>
            <Grid item {...spread({sm: 12, md: 4})}>
              <TextField
                required
                label="Class ID"
                {...number.textFieldParams()}
              />
            </Grid>
            <Grid item {...spread({sm: 12, md: 8})}>
              <TextField
                required
                label="Class Name"
                {...name.textFieldParams()}
              />
            </Grid>
            <Grid item {...spread({sm: 12, md: 9.5})}>
              <Autocomplete
                multiple
                autoHighlight
                disableCloseOnSelect
                value={sortedSelectedKnowledgeAndSkills}
                options={sortedKnowledgeAndSkills}
                isOptionEqualToValue={(option, value) => option.id === value.id}
                renderInput={params => (
                  <TextField
                    label="Select knowledge and skills"
                    autoComplete="off"
                    {...eks.textFieldParams(params)}
                  />
                )}
                renderOption={(props, option, {selected}) => (
                  <li {...props}>
                    <Checkbox style={{marginRight: 8}} checked={selected} />
                    {option.name}:&nbsp;
                    <i>{option.shortDescr ?? 'undefined'}</i>
                  </li>
                )}
                getOptionLabel={option =>
                  option.name + ': ' + (option?.shortDescr ?? 'undefined')
                }
                onChange={(e, options) => {
                  setSortedSelectedKnowledgeAndSkills([
                    ...options.sort(KNOWLEDGE_AND_SKILL_SORTER),
                  ]);
                }}
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
              />
            </Grid>
            <Grid item {...spread({sm: 12, md: 2.5})}>
              <Button
                variant="contained"
                className="teacher-edit-classes-expand-buttons"
                startIcon={<Add />}
                onClick={() => setShowNewEks(true)}
              >
                New EKS
              </Button>
            </Grid>
            <Grid item {...spread({sm: 12, md: 6})}>
              <TextField label="Period" {...period.textFieldParams()} />
            </Grid>
            <Grid item {...spread({sm: 12, md: 6})}>
              <TextField label="Grade" {...grade.textFieldParams()} />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="Short Description"
                {...shortDescr.textFieldParams()}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="Long Description"
                {...longDescr.textFieldParams()}
              />
            </Grid>
          </Grid>
        </form>
        <StandardModal
          open={showNewEks}
          title="Create a New EKS"
          onClose={() => setShowNewEks(false)}
          okText="Add EKS"
          enableOk={eksFormFields.isTentativelyOkToSubmit()}
          onOk={() => {
            if (!eksFormFields.verifyOk(true)) {
              return;
            }

            setShowNewEks(false);

            createService(ProjectManagementService, 'ProjectManagementService')
              .addKnowledgeAndSkill({
                knowledgeAndSkill: {
                  name: eksName.getTypedValue()!,
                  shortDescr: eksShortDescr.getTypedValue()!,
                  global: eksGlobal.getTypedValue()!,
                  type: Type.EKS,
                },
              })
              .then(response => {
                // Add the knowledge and skill to the list and select it.
                setSortedKnowledgeAndSkills(
                  [
                    ...sortedKnowledgeAndSkills,
                    response.knowledgeAndSkill!,
                  ].sort(KNOWLEDGE_AND_SKILL_SORTER)
                );
                setSortedSelectedKnowledgeAndSkills(
                  [
                    ...sortedSelectedKnowledgeAndSkills,
                    response.knowledgeAndSkill!,
                  ].sort(KNOWLEDGE_AND_SKILL_SORTER)
                );
              })
              .catch(global.setError);
          }}
        >
          <Grid container spacing={2}>
            <Grid item xs={12}>
              A name should be a one or two word summary to uniquely identify
              it.
              <TextField
                required
                label="Name"
                InputLabelProps={{shrink: true}}
                placeholder="e.g., Camera Types"
                style={{marginTop: '1em'}}
                {...eksName.textFieldParams()}
              />
            </Grid>
            <Grid item xs={12}>
              A description should:
              <ul style={{marginTop: 0}}>
                <li>
                  Follow the statement: "After completing this course, a student
                  can..."
                </li>
                <li>
                  Address a <i>single</i> capability. I.e., it probably
                  shouldn't include "and" or a comma.
                </li>
              </ul>
              <TextField
                required
                multiline
                minRows={3}
                label="Description"
                placeholder="e.g., Differentiate between camera types for specific types of videography."
                InputLabelProps={{shrink: true}}
                {...eksShortDescr.textFieldParams()}
              />
            </Grid>
            <Grid item xs={12}>
              <FormControlLabel
                label="Visible to other teachers"
                control={<Checkbox {...eksGlobal.checkboxParams()} />}
              />
            </Grid>
          </Grid>
        </StandardModal>
      </DefaultPage>
    </>
  );
}
