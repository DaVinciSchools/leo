import './Post.scss';

import {pl_types} from '../../generated/protobuf-js';
import IProjectPost = pl_types.IProjectPost;
import {
  AccountCircle,
  North,
  South,
  AddCommentTwoTone,
  CommentTwoTone,
} from '@mui/icons-material';
import {useEffect, useState} from 'react';
import {formatAsTag, isTextEmpty, textOrEmpty, toLong} from '../misc';
import IProjectPostComment = pl_types.IProjectPostComment;
import {EditableReactQuill} from '../EditableReactQuill/EditableReactQuill';
import {PostHeader} from './PostHeader';

export function Post(props: {
  post: IProjectPost;
  saveStatus: string;

  addComment: () => void;
  editingCommentId?: number;
  setEditingCommentId: (id?: number) => void;
  setCommentToSave: (comment: IProjectPostComment) => void;
  deleteCommentId?: (id: number) => void;

  editingPostId?: number;
}) {
  const [showComments, setShowComments] = useState<boolean>(false);

  useEffect(() => {
    for (const comment of props.post?.comments ?? []) {
      if (comment.id === props.editingCommentId) {
        setShowComments(true);
        break;
      }
    }
  }, [props.editingCommentId]);

  return (
    <>
      <div className="post-in-feed">
        <div className="global-flex-row">
          <AccountCircle className="post-in-feed-avatar" />
          <div className="global-flex-column" style={{flexGrow: 1, gap: 0}}>
            <PostHeader
              userX={props.post?.userX}
              postTimeMs={toLong(props.post?.postTimeMs ?? 0)}
            />
            <div className="post-in-feed-title">
              {textOrEmpty(props.post?.name, 'Untitled Post')}
            </div>
            <div className="post-in-feed-project-name">
              Project:&nbsp;
              {textOrEmpty(props.post?.project?.name, 'Untitled Project')}
            </div>
          </div>
        </div>
        <div className="post-in-feed-content">
          <EditableReactQuill
            value={props.post.longDescrHtml}
            placeholder={
              <span className="post-in-feed-empty-post">No Post Content</span>
            }
            editing={props.editingPostId === props.post.id}
          />
        </div>
        <div
          className="post-in-feed-tags"
          style={{
            display: (props.post?.tags?.length ?? 0) > 0 ? undefined : 'none',
          }}
        >
          {[...new Set(props.post?.tags?.map(tag => formatAsTag(tag.text)))]
            .sort()
            .map(tag => (
              <span key={tag}>{tag}&nbsp;&nbsp;</span>
            ))}
        </div>
        <div
          style={{
            display: isTextEmpty(props.post?.desiredFeedback)
              ? 'none'
              : undefined,
          }}
        >
          <span className="post-in-feed-feedback-label">
            Feedback I'm looking for:&nbsp;
          </span>
          {textOrEmpty(props.post?.desiredFeedback, 'No Feedback Desired')}
        </div>
        <div className="post-in-feed-footer">
          <div
            className="post-in-feed-footer-group"
            onClick={() => setShowComments(!showComments)}
            style={{cursor: 'pointer'}}
          >
            {showComments ? <South /> : <North />}
            <CommentTwoTone className="global-two-tone-chat-color" />
            <span>{props.post?.comments?.length ?? 0}</span>
          </div>
          <div
            className="post-in-feed-footer-group"
            onClick={() => {
              setShowComments(true);
              props.addComment();
            }}
            style={{cursor: 'pointer'}}
          >
            <AddCommentTwoTone className="global-two-tone-chat-color" />
          </div>
        </div>
        <div
          className="post-in-feed-comments"
          style={{display: showComments ? undefined : 'none'}}
        >
          {props.post?.comments?.length === 0 && (
            <span className="post-in-feed-empty-post">No Comments</span>
          )}
          {props.post?.comments?.map(comment => (
            <div key={comment.id ?? 0} className="post-in-feed-comment">
              <AccountCircle className="post-in-feed-avatar" />
              <div className="global-flex-column" style={{gap: 0}}>
                <PostHeader
                  userX={comment?.userX}
                  postTimeMs={toLong(comment?.postTimeMs ?? 0)}
                  saveStatus={
                    props.editingCommentId === comment.id
                      ? props.saveStatus
                      : ''
                  }
                  editIconClicked={() =>
                    props.setEditingCommentId(
                      props.editingCommentId !== comment.id
                        ? comment.id ?? undefined
                        : undefined
                    )
                  }
                  deleteIconClicked={
                    props.deleteCommentId
                      ? () => props.deleteCommentId?.(comment.id ?? 0)
                      : undefined
                  }
                />
                <EditableReactQuill
                  value={comment.longDescrHtml}
                  placeholder={
                    <span className="post-in-feed-empty-post">
                      No Comment Content
                    </span>
                  }
                  editing={props.editingCommentId === comment.id}
                  onBlur={() => {
                    props.setEditingCommentId(undefined);
                  }}
                  onChange={value => {
                    comment.longDescrHtml = value;
                    props.setCommentToSave(comment);
                  }}
                  editingStyle={{paddingTop: '1rem'}}
                />
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}
