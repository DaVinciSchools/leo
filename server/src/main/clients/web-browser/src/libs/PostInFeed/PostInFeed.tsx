import './PostInFeed.scss';

import {pl_types} from '../../generated/protobuf-js';
import IProjectPost = pl_types.IProjectPost;
import ReactQuill from 'react-quill';
import {ChatOutlined} from '@mui/icons-material';
import {PostCommentInFeed} from '../PostCommentInFeed/PostCommentInFeed';
import {PostHeader} from '../PostHeader/PostHeader';

export function PostInFeed(props: {
  post: IProjectPost;
  showComments?: boolean;
  chatIconClicked: () => void;
}) {
  return (
    <>
      <div className="global-flex-column post-in-feed-post">
        <PostHeader
          userX={props.post?.userX ?? {}}
          project={props.post?.project ?? {}}
          postTitle={props.post?.name ?? ''}
          postTimeMs={props.post?.postTimeMs}
          isComment={false}
        />
        <div className="post-in-feed-content">
          <ReactQuill
            theme="snow"
            className="global-react-quill global-react-quill-read-only"
            value={
              (props.post?.longDescrHtml?.trim()?.length ?? 0) > 0 &&
              props.post?.longDescrHtml !== '<p><br></p>'
                ? props.post?.longDescrHtml ?? ''
                : 'No Content'
            }
            preserveWhitespace={true}
            modules={{toolbar: false}}
            style={{padding: 0}}
            readOnly={true}
          />
        </div>
        <div className="global-flex-row" style={{gap: '0.5em'}}>
          <ChatOutlined
            onClick={props.chatIconClicked}
            style={{cursor: 'pointer'}}
          />
          {props.post?.comments?.length ?? 0}
        </div>
        <div style={{display: props.showComments ? undefined : 'none'}}>
          {props.post?.comments?.map(comment => (
            <PostCommentInFeed key={comment.id} comment={comment} />
          ))}
        </div>
      </div>
    </>
  );
}
