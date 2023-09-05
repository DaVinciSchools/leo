import './PostCommentInFeed.scss';

import {pl_types} from '../../generated/protobuf-js';
import IProjectPostComment = pl_types.IProjectPostComment;
import ReactQuill from 'react-quill';
import {PostHeader} from '../PostHeader/PostHeader';

export function PostCommentInFeed(props: {comment: IProjectPostComment}) {
  return (
    <>
      <PostHeader
        userX={props.comment?.userX ?? {}}
        postTimeMs={props.comment?.postTimeMs}
        isComment={true}
      />
      <div className="post-comment-in-feed-content">
        <ReactQuill
          theme="snow"
          className="global-react-quill"
          value={props.comment?.longDescrHtml ?? ''}
          preserveWhitespace={true}
          modules={{toolbar: false}}
          style={{padding: '0.5em 0'}}
          readOnly={true}
        />
      </div>
    </>
  );
}
