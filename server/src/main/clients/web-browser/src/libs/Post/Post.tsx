import './Post.scss';

import {pl_types, post_service} from 'pl-pb';
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
  DeepReadOnly,
  deepWritable,
  formatAsTag,
  getHighlightStyle,
  isTextEmpty,
  removeInPlace,
  replaceInPlace,
  textOrEmpty,
  toLong,
} from '../misc';
import {EditableReactQuill} from '../EditableReactQuill/EditableReactQuill';
import {PostHeader} from './PostHeader';
import {GlobalStateContext} from '../GlobalState';
import {
  PROJECT_POST_COMMENT_SORTER,
  TAG_SORTER,
  USER_X_SORTER,
} from '../sorters';
import {
  FormControl,
  InputLabel,
  LinearProgress,
  MenuItem,
  Select,
} from '@mui/material';
import {linearProgressClasses} from '@mui/material/LinearProgress';
import {createService} from '../protos';
import {styled} from '@mui/material/styles';
import IProjectPostRating = pl_types.IProjectPostRating;
import IProjectPost = pl_types.IProjectPost;
import IUserX = pl_types.IUserX;
import IProjectPostComment = pl_types.IProjectPostComment;
import ITag = pl_types.ITag;
import PostService = post_service.PostService;
import IProjectPostRatingCategory = pl_types.IProjectPostRatingCategory;
import ProjectPostRating = pl_types.ProjectPostRating;

const BorderLinearProgress = styled(LinearProgress)(({theme}) => ({
  height: 10,
  borderRadius: 5,
  [`&.${linearProgressClasses.colorPrimary}`]: {
    backgroundColor:
      theme.palette.grey[theme.palette.mode === 'light' ? 200 : 800],
  },
  [`& .${linearProgressClasses.bar}`]: {
    borderRadius: 5,
    backgroundColor: theme.palette.mode === 'light' ? '#1a90ff' : '#308fe8',
  },
}));

interface RatingColumn {
  userX: IUserX;
  ratingType: ProjectPostRating.RatingType;
}

interface RatingKey {
  ratingColumn: RatingColumn;
  projectInputFulfillmentId: number;
}

function toRatingColumn(
  rating: DeepReadOnly<IProjectPostRating>
): RatingColumn {
  return {
    userX: rating.userX ?? {},
    ratingType:
      rating.ratingType ?? ProjectPostRating.RatingType.UNSET_RATING_TYPE,
  };
}

function ratingToRatingKey(
  rating: DeepReadOnly<IProjectPostRating>
): RatingKey {
  return {
    ratingColumn: toRatingColumn(rating),
    projectInputFulfillmentId: rating.projectInputFulfillmentId ?? 0,
  };
}

function cellToRatingKey(
  column: RatingColumn,
  category: IProjectPostRatingCategory
): RatingKey {
  return {
    ratingColumn: column,
    projectInputFulfillmentId: category.projectInputFulfillmentId ?? 0,
  };
}

const RATING_COLUMN_SORTER = (a: RatingColumn, b: RatingColumn) =>
  USER_X_SORTER(a.userX, b.userX) ||
  (a.ratingType ?? ProjectPostRating.RatingType.UNSET_RATING_TYPE) -
    (b.ratingType ?? ProjectPostRating.RatingType.UNSET_RATING_TYPE);

const RATING_CATEGORY_SORTER = (
  a: pl_types.IProjectPostRatingCategory,
  b: pl_types.IProjectPostRatingCategory
) =>
  (a.category ?? '').localeCompare(b.category ?? '') ||
  (a.value ?? '').localeCompare(b.value ?? '');

export interface PostHighlights {
  getUserXHue?: (userX: DeepReadOnly<IUserX>) => number | undefined;
}

export function Post(
  props: DeepReadOnly<{
    post: DeepReadOnly<IProjectPost>;
    postUpdated: (post: DeepReadOnly<IProjectPost>, refresh: boolean) => void;
    showComments?: boolean | null | undefined;
    showRatings?: boolean | null | undefined;
    postHighlights?: DeepReadOnly<PostHighlights>;
  }>
) {
  const userX = useContext(GlobalStateContext).requireUserX(
    'You must be logged in to view posts.'
  );
  const global = useContext(GlobalStateContext);
  const hasHighlightedComment = props.post.comments
    ?.map(c => props.postHighlights?.getUserXHue?.(c.userX ?? {}) != null)
    .includes(true);

  const [sortedRatingColumns, setSortedRatingColumns] = useState<
    DeepReadOnly<RatingColumn[]>
  >([]);
  const [sortedRatingCategories, setSortedRatingCategories] = useState<
    DeepReadOnly<IProjectPostRatingCategory[]>
  >([]);
  const [sortedTags, setSortedTags] = useState<DeepReadOnly<ITag[]>>([]);
  const [sortedComments, setSortedComments] = useState<
    DeepReadOnly<IProjectPostComment>[]
  >([]);

  const [ratings, setRatings] = useState<
    DeepReadOnly<
      Map</* JSON.stringify(RatingKey)= */ string, IProjectPostRating>
    >
  >(new Map());

  const [commentBeingEdited, setCommentBeingEdited] = useState<
    DeepReadOnly<IProjectPostComment> | undefined
  >(undefined);
  const [newCommentContent, setNewCommentContent] = useState('');

  const [expandComments, setExpandComments] = useState<boolean>(false);
  const [expandRatings, setExpandRatings] = useState<boolean>(false);

  useEffect(() => {
    setSortedRatingColumns(
      [
        ...new Set(
          (props.post.ratings ?? []).map(rating =>
            JSON.stringify(toRatingColumn(rating))
          )
        ),
      ]
        .map(jsonRatingColumn => JSON.parse(jsonRatingColumn))
        .sort(RATING_COLUMN_SORTER)
    );
    setSortedRatingCategories(
      (props.post?.ratingCategories ?? []).slice().sort(RATING_CATEGORY_SORTER)
    );
    setRatings(
      new Map(
        (props.post.ratings ?? []).map(rating => [
          JSON.stringify(ratingToRatingKey(rating)),
          rating,
        ])
      )
    );

    setSortedTags((props.post?.tags ?? []).slice().sort(TAG_SORTER));

    setSortedComments(
      (props.post?.comments ?? []).slice().sort(PROJECT_POST_COMMENT_SORTER)
    );
  }, [props.post]);

  function saveCommentBeingEdited(then?: () => void) {
    if (commentBeingEdited == null) {
      then && then();
      return;
    }

    const newComment = Object.assign({}, deepWritable(commentBeingEdited), {
      longDescrHtml: newCommentContent,
    } as IProjectPostComment);
    setCommentBeingEdited(undefined);

    replaceInPlace(sortedComments, newComment, c => c.id);
    setSortedComments([...sortedComments]);

    props.postUpdated(
      Object.assign({}, props.post, {
        comments: sortedComments,
      } as IProjectPost),
      false
    );

    createService(PostService, 'PostService')
      .upsertProjectPostComment({
        projectPostComment: newComment,
      })
      .then(() => {
        then && then();
      })
      .catch(global.setError);
  }

  function addComment() {
    setExpandComments(true);
    saveCommentBeingEdited(() => {
      createService(PostService, 'PostService')
        .upsertProjectPostComment({
          projectPostComment: {
            longDescrHtml: '',
            projectPost: {id: props.post.id},
          },
        })
        .then(response => {
          const newSortedComments = [
            response.projectPostComment!,
            ...sortedComments,
          ];
          setSortedComments(newSortedComments);

          props.postUpdated(
            Object.assign({}, props.post, {
              comments: newSortedComments,
            } as IProjectPost),
            false
          );

          saveCommentBeingEdited(() => {
            setCommentBeingEdited(newSortedComments[0]);
            setNewCommentContent(newSortedComments[0].longDescrHtml ?? '');
          });
        })
        .catch(global.setError);
    });
  }

  function deleteComment(comment: DeepReadOnly<IProjectPostComment>) {
    saveCommentBeingEdited();

    const newSortedComments = removeInPlace(
      sortedComments.slice(),
      comment,
      c => c.id
    );
    setSortedComments(newSortedComments);

    props.postUpdated(
      Object.assign({}, props.post, {
        comments: newSortedComments,
      } as IProjectPost),
      false
    );

    createService(PostService, 'PostService')
      .deleteProjectPostComment({
        projectPostCommentId: comment.id ?? 0,
      })
      .catch(global.setError);
  }

  return (
    <>
      <div className="post-in-feed">
        <div
          className="global-flex-row"
          style={getHighlightStyle(
            props.postHighlights?.getUserXHue?.(props.post?.userX ?? {})
          )}
        >
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
          />
        </div>
        <div
          className="post-in-feed-tags"
          style={{
            display: (sortedTags.length ?? 0) > 0 ? undefined : 'none',
          }}
        >
          {sortedTags.map(tag => (
            <span key={tag.text}>{formatAsTag(tag.text)}&nbsp;&nbsp;</span>
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
        <div
          className="post-in-feed-footer"
          style={{
            display:
              props.showComments || props.showRatings ? undefined : 'none',
          }}
        >
          {props.showComments && (
            <>
              <div
                className="post-in-feed-footer-group"
                onClick={() => setExpandComments(!expandComments)}
                style={{cursor: 'pointer'}}
              >
                {expandComments ? <South /> : <North />}
                <CommentTwoTone className="global-two-tone-chat-color" />
                <span>{sortedComments.length ?? 0}</span>
              </div>
              <div
                className="post-in-feed-footer-group"
                onClick={addComment}
                style={{cursor: 'pointer'}}
              >
                <AddCommentTwoTone className="global-two-tone-chat-color" />
              </div>
            </>
          )}
          {props.showRatings && (
            <>
              <div
                className="post-in-feed-footer-group"
                onClick={() => {
                  setExpandRatings(!expandRatings);
                }}
                style={{
                  cursor: 'pointer',
                  display:
                    userX?.isAdminX || userX?.isTeacher ? undefined : 'none',
                }}
              >
                {expandRatings ? <South /> : <North />}
                <AssessmentTwoTone className="global-two-tone-rating-color" />
                <span>{props.post?.ratings?.length ?? 0}</span>
              </div>
            </>
          )}
        </div>
        {expandRatings && (
          <div className="post-in-feed-ratings">
            <table width="fit-content">
              <thead>
                <tr>
                  <th key={-2}></th>
                  <th key={-1}></th>
                  {sortedRatingColumns.map(column => (
                    <th key={column.userX?.id ?? 0}>
                      {userX?.lastName ?? ''},&nbsp;
                      {(userX?.firstName ?? '').charAt(0)}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {sortedRatingCategories.map(ratingCategory => (
                  <tr key={ratingCategory.projectInputFulfillmentId ?? 0}>
                    <th
                      key={-2}
                      scope="row"
                      style={{
                        textAlign: 'right',
                      }}
                    >
                      {ratingCategory.category}
                    </th>
                    <th
                      key={-1}
                      scope="row"
                      style={{
                        textAlign: 'right',
                      }}
                    >
                      {ratingCategory.value}
                    </th>
                    {sortedRatingColumns.map(ratingColumn => (
                      <td key={ratingColumn.userX?.id ?? 0}>
                        <FormControl fullWidth>
                          {ratingColumn.ratingType ===
                            ProjectPostRating.RatingType.INITIAL_1_TO_5 && (
                            <>
                              <InputLabel
                                id="demo-simple-select-label"
                                size="small"
                              >
                                Rating
                              </InputLabel>
                              <Select
                                labelId="demo-simple-select-label"
                                id="demo-simple-select"
                                size="small"
                                disabled={
                                  (ratingColumn.userX?.id ?? 0) !==
                                  (userX?.id ?? 0)
                                }
                                value={
                                  ratings.get(
                                    JSON.stringify(
                                      cellToRatingKey(
                                        ratingColumn,
                                        ratingCategory
                                      )
                                    )
                                  )?.rating ?? 0
                                }
                                label="Rating"
                                onChange={e => {
                                  const ratingKey = cellToRatingKey(
                                    ratingColumn,
                                    ratingCategory
                                  );
                                  const value =
                                    e.target.value === ''
                                      ? 0
                                      : parseInt(String(e.target.value));
                                  let oldRating = ratings.get(
                                    JSON.stringify(ratingKey)
                                  );
                                  if (oldRating == null) {
                                    oldRating = {
                                      userX: ratingColumn.userX,
                                      rating: value,
                                      ratingType: ratingColumn.ratingType,
                                      projectInputFulfillmentId:
                                        ratingCategory.projectInputFulfillmentId,
                                    };
                                  }
                                  const newRating = Object.assign(
                                    {},
                                    deepWritable(oldRating),
                                    {
                                      rating: value,
                                    }
                                  );
                                  createService(PostService, 'PostService')
                                    .upsertProjectPostRating({
                                      projectPostRating: newRating,
                                    })
                                    .then(response => {
                                      newRating.id = response.id ?? 0;
                                      props.postUpdated(
                                        Object.assign({}, props.post, {
                                          ratings: Array.from(ratings.values()),
                                        } as IProjectPost),
                                        false
                                      );
                                      setRatings(
                                        new Map(ratings).set(
                                          JSON.stringify(ratingKey),
                                          newRating
                                        )
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
                            </>
                          )}
                          {ratingColumn.ratingType ===
                            ProjectPostRating.RatingType.GOAL_COMPLETE_PCT && (
                            <BorderLinearProgress
                              variant="determinate"
                              value={
                                ratings.get(
                                  JSON.stringify(
                                    cellToRatingKey(
                                      ratingColumn,
                                      ratingCategory
                                    )
                                  )
                                )?.rating ?? 0
                              }
                              style={{height: '1em', verticalAlign: 'bottom'}}
                            />
                          )}{' '}
                        </FormControl>
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
        {(expandComments || hasHighlightedComment) && (
          <div className="post-in-feed-comments">
            {sortedComments.length === 0 && (
              <span className="post-in-feed-empty-post">No Comments</span>
            )}
            {sortedComments.map(comment => (
              <div key={comment.id ?? 0} className="post-in-feed-comment">
                <AccountCircle className="post-in-feed-avatar" />
                <div className="global-flex-column" style={{gap: 0}}>
                  <PostHeader
                    userX={comment?.userX}
                    postTimeMs={toLong(comment?.postTimeMs ?? 0)}
                    deleteIconClicked={() => deleteComment(comment)}
                  />
                  <EditableReactQuill
                    value={
                      comment.id === commentBeingEdited?.id
                        ? newCommentContent
                        : comment.longDescrHtml
                    }
                    placeholder={
                      <span className="post-in-feed-empty-post">
                        No Comment Content
                      </span>
                    }
                    editing={comment.id === commentBeingEdited?.id}
                    onClick={() => {
                      if (comment.id !== commentBeingEdited?.id) {
                        saveCommentBeingEdited(() => {
                          setCommentBeingEdited(comment);
                          setNewCommentContent(comment.longDescrHtml ?? '');
                        });
                      }
                    }}
                    onBlur={() => {
                      saveCommentBeingEdited();
                    }}
                    onChange={value => {
                      if (comment.id === commentBeingEdited?.id) {
                        setNewCommentContent(value);
                      }
                    }}
                    editingStyle={{
                      marginTop: '1rem',
                    }}
                  />
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  );
}
