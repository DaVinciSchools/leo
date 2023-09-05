import './PostInFeed.scss';

import {pl_types} from '../../generated/protobuf-js';
import IProjectPost = pl_types.IProjectPost;
import ReactQuill from 'react-quill';
import {AddCommentOutlined, CommentOutlined} from '@mui/icons-material';
import {PostCommentInFeed} from '../PostCommentInFeed/PostCommentInFeed';
import {PostHeader} from '../PostHeader/PostHeader';
import {useContext, useEffect, useState} from 'react';
import {GlobalStateContext} from '../GlobalState';

export function PostInFeed(props: {
  post: IProjectPost;
  saveStatus: string;
  editingCommentId: number | null;
  setEditingCommentId: (id: number | null) => void;
  addComment: () => void;
  setCommentToSave: (comment: IProjectPost) => void;
}) {
  const global = useContext(GlobalStateContext);

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
          <CommentOutlined
            onClick={() => setShowComments(!showComments)}
            style={{cursor: 'pointer'}}
          />
          {props.post?.comments?.length ?? 0}
          <div>
            <AddCommentOutlined
              onClick={props.addComment}
              style={{cursor: 'pointer'}}
            />
          </div>
        </div>
        <div style={{display: showComments ? undefined : 'none'}}>
          {props.post?.comments?.map(comment => (
            <PostCommentInFeed
              key={comment.id}
              comment={comment}
              isEditing={comment.id === props.editingCommentId}
              editIconClicked={
                global.userX?.isAdminX ||
                global.userX?.isTeacher ||
                global.userX?.userXId === comment.userX?.userXId
                  ? () => {
                      if (comment.id === props.editingCommentId) {
                        props.setEditingCommentId(null);
                      } else {
                        props.setEditingCommentId(comment.id ?? 0);
                      }
                    }
                  : undefined
              }
              addIconClicked={() => {
                props.addComment();
              }}
              saveStatus={
                comment.id === props.editingCommentId ? props.saveStatus : ''
              }
              commentUpdated={value => {
                comment.longDescrHtml = value;
                props.setCommentToSave(comment);
              }}
            />
          ))}
        </div>
      </div>
    </>
  );
}
