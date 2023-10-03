import './Post.scss';

import {pl_types, post_service} from '../../generated/protobuf-js';
import {
  AccountCircle,
  AddCommentTwoTone,
  AssessmentTwoTone,
  CommentTwoTone,
  North,
  South,
} from '@mui/icons-material';
import {useContext, useEffect, useState} from 'react';
import {
  DeepReadonly,
  DeepWritable,
  formatAsTag,
  isTextEmpty,
  textOrEmpty,
  toLong,
} from '../misc';
import {EditableReactQuill} from '../EditableReactQuill/EditableReactQuill';
import {PostHeader} from './PostHeader';
import {GlobalStateContext} from '../GlobalState';
import {
  KNOWLEDGE_AND_SKILL_SORTER,
  PROJECT_POST_COMMENT_SORTER,
  USER_X_SORTER,
} from '../sorters';
import {FormControl, InputLabel, MenuItem, Select} from '@mui/material';
import {createService} from '../protos';
import IProjectPost = pl_types.IProjectPost;
import IProjectPostComment = pl_types.IProjectPostComment;
import IUserX = pl_types.IUserX;
import PostService = post_service.PostService;

interface RatingKey {
  readonly userXId: number;
  readonly ratingType: pl_types.ProjectPostRating.RatingType;
  readonly knowledgeAndSkillId: number;
}

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
  const global = useContext(GlobalStateContext);

  const [assignmentKs, setAssignmentKs] = useState<
    readonly pl_types.IKnowledgeAndSkill[]
  >([]);
  const [ratingUsers, setRatingUsers] = useState<readonly IUserX[]>([]);
  const [ratings, setRatings] = useState<
    DeepReadonly<
      Map</* JSON.stringify(RatingKey)= */ string, pl_types.IProjectPostRating>
    >
  >(new Map());

  const [showComments, setShowComments] = useState<boolean>(false);
  const [showRatings, setShowRatings] = useState<boolean>(false);

  useEffect(() => {
    for (const comment of props.post?.comments ?? []) {
      if (comment.id === props.editingCommentId) {
        setShowComments(true);
        break;
      }
    }
  }, [props.editingCommentId]);

  useEffect(() => {
    if (props.post.ratings == null) {
      setRatings(new Map());
      return;
    }

    setRatings(
      new Map(
        (props.post?.ratings ?? []).map(rating => [
          JSON.stringify({
            userXId: rating.userX?.id ?? 0,
            ratingType: rating.ratingType,
            knowledgeAndSkillId: rating.knowledgeAndSkill?.id ?? 0,
          } as RatingKey),
          rating,
        ])
      )
    );
    setAssignmentKs(
      (props.post?.project?.assignment?.knowledgeAndSkills ?? [])
        .slice()
        .sort(KNOWLEDGE_AND_SKILL_SORTER)
    );

    const users = new Map(
      props.post?.ratings?.map(rating => [
        rating.userX?.id ?? 0,
        rating.userX ?? {},
      ])
    );
    users.delete(global.userX?.id ?? 0);
    setRatingUsers([
      global.userX ?? {},
      ...[...users.values()].sort(USER_X_SORTER),
    ]);
  }, [props.post]);

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
          <div
            className="post-in-feed-footer-group"
            onClick={() => {
              setShowRatings(!showRatings);
            }}
            style={{
              cursor: 'pointer',
              display:
                global.userX?.isAdminX || global.userX?.isTeacher
                  ? undefined
                  : 'none',
            }}
          >
            {showRatings ? <South /> : <North />}
            <AssessmentTwoTone className="global-two-tone-rating-color" />
            <span>{props.post?.ratings?.length ?? 0}</span>
          </div>
        </div>
        <div
          className="post-in-feed-ratings"
          style={{display: showRatings ? undefined : 'none'}}
        >
          <table width="fit-content">
            <thead>
              <tr>
                <th key={-1}>Skill</th>
                {ratingUsers.map(userX => (
                  <th key={userX?.id ?? 0}>
                    {userX?.lastName ?? ''},&nbsp;
                    {(userX?.firstName ?? '').charAt(0)}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {assignmentKs.map(ks => (
                <tr key={ks.id ?? 0}>
                  <th
                    key={-1}
                    scope="row"
                    style={{
                      textAlign: 'right',
                    }}
                  >
                    {ks.name}
                  </th>
                  {ratingUsers.map(userX => (
                    <td key={userX.id ?? 0}>
                      <FormControl fullWidth>
                        <InputLabel id="demo-simple-select-label" size="small">
                          Rating
                        </InputLabel>
                        <Select
                          labelId="demo-simple-select-label"
                          id="demo-simple-select"
                          size="small"
                          value={
                            ratings.get(
                              JSON.stringify({
                                userXId: userX.id ?? 0,
                                ratingType:
                                  pl_types.ProjectPostRating.RatingType
                                    .INITIAL_1_TO_5,
                                knowledgeAndSkillId: ks.id ?? 0,
                              } as RatingKey)
                            )?.rating ?? ''
                          }
                          label="Rating"
                          onChange={e => {
                            const value =
                              e.target.value === ''
                                ? 0
                                : parseInt(String(e.target.value));
                            const key = JSON.stringify({
                              userXId: userX.id ?? 0,
                              ratingType:
                                pl_types.ProjectPostRating.RatingType
                                  .INITIAL_1_TO_5,
                              knowledgeAndSkillId: ks.id ?? 0,
                            } as RatingKey);

                            console.log('props', props);
                            let oldRating = ratings.get(key);
                            if (oldRating == null) {
                              oldRating = {
                                userX: {id: userX.id},
                                rating: value,
                                ratingType:
                                  pl_types.ProjectPostRating.RatingType
                                    .INITIAL_1_TO_5,
                                knowledgeAndSkill: {id: ks.id},
                                projectPost: {id: props.post.id},
                              };
                            }
                            console.log('oldRating', oldRating);
                            const newRating = Object.assign(
                              {},
                              DeepWritable(oldRating),
                              {
                                rating: value,
                              }
                            );
                            console.log('newRating', newRating);
                            createService(PostService, 'PostService')
                              .upsertProjectPostRating({
                                projectPostRating: newRating,
                              })
                              .then(response => {
                                newRating.id = response.id ?? 0;
                                setRatings(
                                  new Map(ratings).set(key, newRating)
                                );
                              })
                              .catch(global.setError);
                          }}
                        >
                          <MenuItem value={1}>1 - No Evidence</MenuItem>
                          <MenuItem value={2}>2 - Attempted</MenuItem>
                          <MenuItem value={3}>3 - Emerging</MenuItem>
                          <MenuItem value={4}>4 - Proficient</MenuItem>
                          <MenuItem value={5}>5 - Advanced</MenuItem>
                        </Select>
                      </FormControl>
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div
          className="post-in-feed-comments"
          style={{display: showComments ? undefined : 'none'}}
        >
          {props.post?.comments?.length === 0 && (
            <span className="post-in-feed-empty-post">No Comments</span>
          )}
          {props.post?.comments
            ?.sort(PROJECT_POST_COMMENT_SORTER)
            ?.map(comment => (
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
