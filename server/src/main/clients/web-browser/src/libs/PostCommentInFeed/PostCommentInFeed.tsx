import {pl_types} from '../../generated/protobuf-js';
import IProjectPostComment = pl_types.IProjectPostComment;
import {AccountCircle} from '@mui/icons-material';

export function PostCommentInFeed(props: {comment: IProjectPostComment}) {
  return (
    <>
      <div className="post-comment-in-feed-header">
        <span className="post-comment-in-feed-avatar">
          <AccountCircle />
        </span>
        <span className="post-comment-in-feed-name">
          {props.comment?.userX?.firstName ?? ''}&nbsp;
          {props.comment?.userX?.lastName ?? ''}
        </span>
      </div>
      <div className="post-comment-in-feed-content">
        {props.comment?.longDescrHtml}
      </div>
    </>
  );
}
