import {Checkbox, FormControlLabel, Grid, TextField} from '@mui/material';
import {StandardModal} from '../StandardModal/StandardModal';
import {pl_types} from '../../generated/protobuf-js';
import {useEffect} from 'react';
import {useFormFields} from '../forms';

import IKnowledgeAndSkill = pl_types.IKnowledgeAndSkill;

export function KnowledgeAndSkillModal(props: {
  open: boolean;
  title: string;
  okText: string;
  onClose: () => void;
  onOk: (knowledgeAndSkill: IKnowledgeAndSkill) => void;
  value?: IKnowledgeAndSkill;
}) {
  const mainForm = useFormFields();
  const nameField = mainForm.useStringFormField('name', {maxLength: 255});
  const shortDescrField = mainForm.useStringFormField('shortDescr', {
    maxLength: 255,
  });
  const globalField = mainForm.useBooleanFormField('global', {
    isBoolean: true,
  });

  useEffect(() => {
    mainForm.setValuesObject(props.value);
  }, [props.value]);

  return (
    <StandardModal
      open={props.open}
      title={props.title}
      onClose={props.onClose}
      okText={props.okText}
      onOk={() => {
        if (!mainForm.verifyOk(true)) {
          return;
        }
        props.onOk(mainForm.getValuesObject(true, {...props.value}));
      }}
    >
      <form style={{paddingTop: '2em'}} {...mainForm.formParams()}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            A name should be a one or two word summary to uniquely identify it.
            <TextField
              required
              label="Name"
              InputLabelProps={{shrink: true}}
              placeholder="e.g., Camera Types"
              style={{marginTop: '1em'}}
              {...nameField.textFieldParams()}
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
                Address a <i>single</i> capability. I.e., it probably shouldn't
                include "and" or a comma.
              </li>
            </ul>
            <TextField
              required
              multiline
              minRows={3}
              label="Description"
              placeholder="e.g., Differentiate between camera types for specific types of videography."
              InputLabelProps={{shrink: true}}
              {...shortDescrField.textFieldParams()}
            />
          </Grid>
          <Grid item xs={12}>
            <FormControlLabel
              label="Visible to other teachers"
              control={<Checkbox {...globalField.checkboxParams()} />}
            />
          </Grid>
        </Grid>
      </form>
    </StandardModal>
  );
}
