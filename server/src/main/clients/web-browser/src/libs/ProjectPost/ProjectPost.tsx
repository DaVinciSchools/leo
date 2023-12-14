import './ProjectPost.scss';

import {pl_types, post_service} from '../../generated/protobuf-js';
import {
  AccountCircle,
  AddCommentTwoTone,
  CommentTwoTone,
  North,
  South,
} from '@mui/icons-material';
import {useContext, useEffect, useState} from 'react';
import {
  DeepReadonly,
  DeepWritable,
  formatAsTag,
  isHtmlEmpty,
  isTextEmpty,
  textOrEmpty,
  toLong,
} from '../misc';
import {EditableReactQuill} from '../EditableReactQuill/EditableReactQuill';
import {ProjectPostHeader} from './ProjectPostHeader';
import {FieldState, useFormFields} from '../form_utils/forms';
import {GlobalStateContext} from '../GlobalState';
import {TextField} from '@mui/material';
import IProjectPost = pl_types.IProjectPost;
import IProjectPostComment = pl_types.IProjectPostComment;
import {MultiTagAutocomplete} from '../common_fields/MultiTagAutocomplete';
import {createService} from '../protos';
import PostService = post_service.PostService;
import {Space} from '../tags';
import {useDelayedAction} from '../delayed_action';

export function ProjectPost(props: {
  post: DeepReadonly<IProjectPost>;
  onUpdatePost: (post: DeepReadonly<IProjectPost>) => void;
}) {
  const global = useContext(GlobalStateContext);

  const [saveStatus, setSaveStatus] = useState<
    '' | 'Modified' | 'Saving...' | 'Save error!'
  >('');
  const autoSave = useDelayedAction(
    () => setSaveStatus('Modified'),
    () => {
      setSaveStatus('Saving...');
      const post = Object.assign({}, props.post, postForm.getValuesObject());
      createService(PostService, 'PostService')
        .upsertProjectPost({
          projectPost: DeepWritable(post),
        })
        .then(() => {
          setSaveStatus('');
          props.onUpdatePost(post);
        })
        .catch(global.setError);
    },
    1500
  );

  // ---------- ProjectPost ----------

  const postForm = useFormFields({
    fieldState: FieldState.READ_ONLY,
    onChange: () => autoSave.trigger(),
  });
  const postName = postForm.useStringFormField('name', {
    maxLength: 255,
  });
  const postTags = postForm.useAutocompleteFormField<readonly string[]>(
    'tags',
    {
      maxLength: 32,
      isAutocomplete: {isMultiple: true, isFreeSolo: true},
    }
  );
  const postLongDescrHtml = postForm.useStringFormField('longDescrHtml', {
    maxLength: 65535,
  });
  const postDesiredFeedback = postForm.useStringFormField('desiredFeedback', {
    maxLength: 65535,
  });

  // ---------- Comments ----------

  const [showComments, setShowComments] = useState<boolean>(false);
  const [comments, setComments] = useState<DeepReadonly<IProjectPostComment[]>>(
    []
  );
  const [commentIdBeingEdited, setCommentIdBeingEdited] = useState<number>();

  const commentForm = useFormFields({});
  const commentLongDescrHtml = commentForm.useStringFormField('longDescrHtml', {
    fieldState: FieldState.READ_ONLY,
  });

  // ---------- Effects ----------

  useEffect(() => {
    postForm.setValuesObject(props.post);
    commentForm.reset();
    setComments(props.post?.comments ?? []);
    setCommentIdBeingEdited(undefined);
  }, [props.post]);

  return (
    <>
      <div className="project-post">
        <div className="global-flex-row">
          <AccountCircle className="project-post-avatar" />
          <div className="global-flex-column" style={{flexGrow: 1, gap: 0}}>
            <ProjectPostHeader
              userX={props.post?.userX}
              saveStatus={saveStatus}
              postTimeMs={toLong(props.post?.postTimeMs ?? 0)}
            />
            <div className="project-post-project-name">
              {textOrEmpty(props.post?.project?.name, 'Untitled Project')}
            </div>
            {postName.inlineEditableField(
              () => (
                <TextField
                  {...postName.textFieldParams()}
                  label="Title"
                  placeholder="Enter a title for this post."
                  style={{marginTop: '1rem'}}
                />
              ),
              () =>
                isTextEmpty(postName.getValue()) ? (
                  <div
                    className="project-post-set-comment"
                    style={{marginTop: '0.5em'}}
                  >
                    Set Title
                  </div>
                ) : (
                  <div className="project-post-title">
                    {postName.getValue()}
                  </div>
                )
            )}
          </div>
        </div>
        <div className="project-post-content ">
          {postLongDescrHtml.inlineEditableField(editing => {
            if (!editing && isHtmlEmpty(postLongDescrHtml.getValue())) {
              return (
                <div className="project-post-set-comment">Set Description</div>
              );
            }
            return (
              <EditableReactQuill
                {...postLongDescrHtml.quillParams()}
                placeholder="Enter a description for this post."
                editing={editing}
              />
            );
          })}
        </div>
        <div className="project-post-tags">
          {postTags.inlineEditableField(
            () => (
              <>
                <MultiTagAutocomplete
                  sortedTags={[
                    ...new Set(
                      props.post?.tags?.map(tag => formatAsTag(tag.text))
                    ),
                  ].sort()}
                  placeholder={hasOptions =>
                    hasOptions ? '' : 'Enter tags for this post.'
                  }
                  formField={postTags}
                  label="Tags"
                />
              </>
            ),
            () => (
              <>
                {(postTags.getValue()?.length ?? 0) > 0 ? (
                  (postTags.getValue()?.slice?.() ?? []).sort().map(tag => (
                    <span key={tag}>
                      {tag}
                      <Space />
                      <Space />
                    </span>
                  ))
                ) : (
                  <div className="project-post-set-comment">Set Tags</div>
                )}
              </>
            )
          )}
        </div>
        <div>
          {postDesiredFeedback.inlineEditableField(
            () => (
              <TextField
                placeholder="Ask for feedback you want."
                label="Feedback Desired"
                {...postDesiredFeedback.textFieldParams()}
              />
            ),
            () => (
              <>
                <div className="project-post-feedback">
                  <span className="project-post-feedback-label">
                    Feedback I'm looking for:
                    <Space />
                  </span>
                  {isTextEmpty(postDesiredFeedback.getValue()) ? (
                    <>
                      <div
                        className="project-post-set-comment"
                        style={{marginTop: '0.5em'}}
                      >
                        Set Feedback Desired
                      </div>
                    </>
                  ) : (
                    <>{postDesiredFeedback.getValue()}</>
                  )}
                </div>
              </>
            )
          )}
        </div>
        <div className="project-post-footer">
          <div
            className="project-post-footer-group"
            onClick={() => setShowComments(e => !e)}
            style={{cursor: 'pointer'}}
          >
            {showComments ? <South /> : <North />}
            <CommentTwoTone className="global-two-tone-chat-color" />
            <span>{comments.length ?? 0}</span>
          </div>
          <div
            className="project-post-footer-group"
            onClick={() => {
              createService(PostService, 'PostService')
                .upsertProjectPostComment({
                  projectPostComment: {
                    projectPost: DeepWritable(props.post),
                    userX: global.userX,
                    longDescrHtml: '',
                  },
                })
                .then(response => {
                  setComments(e => [response.projectPostComment!, ...e]);
                  setShowComments(true);
                })
                .catch(global.setError);
            }}
            style={{cursor: 'pointer'}}
          >
            <AddCommentTwoTone className="global-two-tone-chat-color" />
          </div>
        </div>
        <div
          className="project-post-comments"
          style={{display: showComments ? undefined : 'none'}}
        >
          {comments.length === 0 && (
            <span className="project-post-comments-empty">No Comments</span>
          )}
          {comments.map(comment => (
            <div key={comment.id ?? 0} className="project-post-comment">
              <AccountCircle className="project-post-avatar" />
              <div className="global-flex-column" style={{gap: 0}}>
                <ProjectPostHeader
                  userX={comment?.userX}
                  postTimeMs={toLong(comment?.postTimeMs ?? 0)}
                  deleteIconClicked={() => {
                    createService(PostService, 'PostService')
                      .deleteProjectPostComment({
                        projectPostCommentId: comment.id ?? 0,
                      })
                      .then(response => {
                        setComments(e => e.filter(f => f.id === comment.id));
                        setShowComments(true);
                      })
                      .catch(global.setError);
                  }}
                />
                {/*<EditableReactQuill*/}
                {/*  className="project-post-empty-post"*/}
                {/*  placeholder="No comment content."*/}
                {/*  editing={comment.id === commentIdBeingEdited}*/}
                {/*  editingStyle={{paddingTop: '1rem'}}*/}
                {/*  value={comment.longDescrHtml ?? ''}*/}
                {/*  onChange={value => (comment.longDescrHtml = value)}*/}
                {/*  onFocus={() =>*/}
                {/*    setCommentIdBeingEdited(comment.id ?? undefined)*/}
                {/*  }*/}
                {/*  onBlur={() => {*/}
                {/*    setCommentIdBeingEdited(undefined);*/}
                {/*  }}*/}
                {/*/>*/}
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}
