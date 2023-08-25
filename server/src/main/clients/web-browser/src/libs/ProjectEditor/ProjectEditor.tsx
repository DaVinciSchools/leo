import '../global.scss';

import {AssignmentAutocomplete} from '../common_fields/AssignmentsAutocomplete';
import {TextField} from '@mui/material';
import {FormFields} from '../forms';
import {pl_types} from '../../generated/protobuf-js';
import IAssignment = pl_types.IAssignment;
import ReactQuill from 'react-quill';

export function ProjectEditor(props: {
  projectForm: FormFields;
  sortedAssignments: IAssignment[];
}) {
  const projectName = props.projectForm.useStringFormField('name', {
    maxLength: 255,
  });
  const projectAssignment =
    props.projectForm.useAutocompleteFormField<IAssignment | null>(
      'assignment',
      {
        isAutocomplete: {},
      }
    );
  const projectShortDescr = props.projectForm.useStringFormField('shortDescr', {
    maxLength: 65535,
  });
  const projectLongDescrHtml = props.projectForm.useStringFormField(
    'longDescrHtml',
    {
      maxLength: 65535,
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
        <TextField required {...projectName.textFieldParams()} />
        <div className="global-form-label">Description</div>
        <TextField required {...projectShortDescr.textFieldParams()} />
        <div className="global-form-label">Details</div>
        <ReactQuill
          theme="snow"
          className="global-react-quill"
          {...projectLongDescrHtml.quillParams()}
        />
      </div>
    </>
  );
}
