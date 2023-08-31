import {FormFields} from '../forms';
import {TextField} from '@mui/material';
import ReactQuill from 'react-quill';
import {MultiTagAutocomplete} from '../common_fields/MultiTagAutocomplete';

export function PostEditor(props: {
  sortedTags: string[];
  postForm: FormFields;
}) {
  const postName = props.postForm.useStringFormField('name', {
    maxLength: 255,
  });
  const postTags = props.postForm.useAutocompleteFormField<string[]>('tags', {
    maxLength: 32,
    isAutocomplete: {isMultiple: true, isFreeSolo: true},
  });
  const postLongDescrHtml = props.postForm.useStringFormField('longDescrHtml', {
    maxLength: 65535,
    // TODO: isHtml: {isRequired: true}.
  });
  const postDesiredFeedback = props.postForm.useStringFormField(
    'desiredFeedback',
    {
      maxLength: 65535,
    }
  );

  return (
    <>
      <div className="global-flex-column">
        <div className="global-form-label">Title</div>
        <TextField
          placeholder="Enter a name for this post."
          // TODO: required - Right now it pulls the focus all the time.
          {...postName.textFieldParams()}
        />
        <div className="global-form-label">Tags</div>
        <MultiTagAutocomplete
          formField={postTags}
          sortedTags={props.sortedTags}
          placeholder={() => 'Select tags or enter new ones then press ENTER'}
        />
        <div className="global-form-label">Details</div>
        <ReactQuill
          theme="snow"
          className="global-react-quill"
          placeholder="Enter a description for this post."
          {...postLongDescrHtml.quillParams()}
        />
        <div className="global-form-label">Feedback Desired</div>
        <TextField
          multiline
          minRows={3}
          placeholder="Enter what kind of feedback you want."
          {...postDesiredFeedback.textFieldParams()}
        />
      </div>
    </>
  );
}
