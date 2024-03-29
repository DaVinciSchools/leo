import '../global.scss';

import {AssignmentAutocomplete} from '../common_fields/AssignmentAutocomplete';
import {TextField} from '@mui/material';
import {FormFields} from '../form_utils/forms';
import {pl_types} from 'pl-pb';
import {HtmlEditor} from '../HtmlEditor/HtmlEditor';
import {DeepReadOnly} from '../misc';
import IAssignment = pl_types.IAssignment;

export function ProjectEditor(props: {
  projectForm: FormFields;
  sortedAssignments: readonly IAssignment[];
}) {
  const projectName = props.projectForm.useStringFormField('name', {
    maxLength: 255,
  });
  const projectAssignment =
    props.projectForm.useSingleAutocompleteFormField<DeepReadOnly<IAssignment>>(
      'assignment'
    );
  const projectShortDescr = props.projectForm.useStringFormField('shortDescr', {
    maxLength: 65535,
  });
  const projectLongDescrHtml = props.projectForm.useStringFormField(
    'longDescrHtml',
    {
      maxLength: 65535,
      isAlwaysEditing: true,
    }
  );

  return (
    <>
      <div className="global-flex-column">
        <div className="global-form-label">Select Assignment</div>
        <AssignmentAutocomplete
          sortedAssignments={props.sortedAssignments}
          formField={projectAssignment}
        />
        <div className="global-form-label">Title</div>
        <TextField required {...projectName.getTextFieldParams()} />
        <div className="global-form-label">Description</div>
        <TextField required {...projectShortDescr.getTextFieldParams()} />
        <div className="global-form-label">Details</div>
        <HtmlEditor {...projectLongDescrHtml.getHtmlEditorParams()} />
      </div>
    </>
  );
}
