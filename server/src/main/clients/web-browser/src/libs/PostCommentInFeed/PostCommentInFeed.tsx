import './PostCommentInFeed.scss';

import {pl_types} from '../../generated/protobuf-js';
import IProjectPostComment = pl_types.IProjectPostComment;
import ReactQuill from 'react-quill';
import {PostHeader} from '../PostHeader/PostHeader';

export function PostCommentInFeed(props: {
  comment: IProjectPostComment;
  isEditing: boolean;
  editIconClicked?: () => void;
  addIconClicked?: () => void;
  commentUpdated?: (comment: string) => void;
  saveStatus?: string;
}) {
  return (
    <>
      <PostHeader
        userX={props.comment?.userX ?? {}}
        postTimeMs={props.comment?.postTimeMs}
        isComment={true}
        editIconClicked={props.editIconClicked}
        saveStatus={props.saveStatus}
      />
      <div style={{display: props.isEditing ? 'none' : undefined}}>
        <ReactQuill
          theme="snow"
          className="post-comment-in-feed-content global-react-quill global-react-quill-read-only"
          value={props.comment?.longDescrHtml ?? ''}
          preserveWhitespace={true}
          style={{padding: '0.5em 0'}}
          modules={{toolbar: false}}
          readOnly={true}
        />
      </div>
      <div style={{display: props.isEditing ? undefined : 'none'}}>
        <ReactQuill
          theme="snow"
          className="global-react-quill"
          value={props.comment?.longDescrHtml ?? ''}
          preserveWhitespace={true}
          onChange={props.commentUpdated}
        />
      </div>
    </>
  );
}
