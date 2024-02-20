import './Post.scss';

import {pl_types, post_service} from 'pl-pb';
import {
  AddCommentTwoTone,
  AssessmentTwoTone,
  CommentTwoTone,
  North,
  South,
} from '@mui/icons-material';
import {CSSProperties, useContext, useEffect, useState} from 'react';
import {
  deepClone,
  DeepReadOnly,
  formatAsTag,
  isTextEmpty,
  removeInDeepReadOnly,
  replaceInDeepReadOnly,
  textOrEmpty,
  toLong,
} from '../misc';
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
  Modal,
  Select,
} from '@mui/material';
import {linearProgressClasses} from '@mui/material/LinearProgress';
import {createService} from '../protos';
import {styled} from '@mui/material/styles';
import {HtmlEditor} from '../HtmlEditor/HtmlEditor';
import {UserXAvatar} from '../UserXAvatar/UserXAvatar';
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

export function Post(
  props: DeepReadOnly<{
    post: IProjectPost;
    postUpdated: (post: DeepReadOnly<IProjectPost>) => void;
    showComments?: boolean | null | undefined;
    showRatings?: boolean | null | undefined;
    getUserXHighlightStyle?: (
      userX?: DeepReadOnly<IUserX> | null | undefined
    ) => CSSProperties | undefined;
  }>
) {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX('You must be logged in to view posts.');
  const hasHighlightedComment = props.post.comments
    ?.map(c => props.getUserXHighlightStyle?.(c.userX) != null)
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

  const [expandComments, setExpandComments] = useState<boolean>(true);
  const [expandRatings, setExpandRatings] = useState<boolean>(false);
  const [showAiPrompt, setShowAiPrompt] =
    useState<DeepReadOnly<IProjectPostComment>>();

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

  function saveCommentBeingEdited(): Promise<IProjectPostComment | undefined> {
    if (commentBeingEdited == null) {
      return Promise.resolve(undefined);
    }

    const newComment = deepClone(
      commentBeingEdited,
      c => (c.longDescrHtml = newCommentContent)
    );

    return createService(PostService, 'PostService')
      .upsertProjectPostComment({
        projectPostComment: newComment,
      })
      .then(() => {
        const newSortedComments = replaceInDeepReadOnly(
          sortedComments,
          newComment,
          c => c.id
        );

        setSortedComments(newSortedComments);
        setCommentBeingEdited(undefined);
        props.postUpdated(
          deepClone(
            props.post,
            p => (p.comments = deepClone(newSortedComments))
          )
        );

        return newComment;
      });
  }

  function addComment() {
    setExpandComments(true);
    saveCommentBeingEdited()
      .then(() =>
        createService(PostService, 'PostService').upsertProjectPostComment({
          projectPostComment: {
            longDescrHtml: '',
            projectPost: {id: props.post.id},
          },
        })
      )
      .then(response => {
        const newSortedComments = [
          response.projectPostComment!,
          ...sortedComments,
        ];

        setSortedComments(newSortedComments);
        setCommentBeingEdited(newSortedComments[0]);
        setNewCommentContent('');
        props.postUpdated(
          deepClone(
            props.post,
            p => (p.comments = deepClone(newSortedComments))
          )
        );
      })
      .catch(global.setError);
  }

  function deleteComment(comment: DeepReadOnly<IProjectPostComment>) {
    saveCommentBeingEdited();

    const newSortedComments = removeInDeepReadOnly(
      sortedComments,
      comment,
      c => c.id
    );
    setSortedComments(newSortedComments);

    props.postUpdated(
      deepClone(props.post, p => (p.comments = deepClone(newSortedComments)))
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
        <div className="global-flex-row">
          <UserXAvatar userX={props.post?.userX} size="3rem" />
          <div
            className="global-flex-column"
            style={{
              flexGrow: 1,
              gap: 0,
              ...props.getUserXHighlightStyle?.(props.post?.userX),
            }}
          >
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
          <HtmlEditor
            id={props.post.id?.toString() ?? ''}
            value={props.post.longDescrHtml}
            placeholder="No Post Content"
            readOnly={true}
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
                      {column.userX?.lastName ?? ''},&nbsp;
                      {(column.userX?.firstName ?? '').charAt(0)}
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
                                  const newRating = deepClone(
                                    ratings.get(JSON.stringify(ratingKey)) ??
                                      {},
                                    r => {
                                      r.userX = ratingColumn.userX;
                                      r.rating = value;
                                      r.ratingType = ratingColumn.ratingType;
                                      r.projectInputFulfillmentId =
                                        ratingCategory.projectInputFulfillmentId;
                                    }
                                  );
                                  createService(PostService, 'PostService')
                                    .upsertProjectPostRating({
                                      projectPostRating: newRating,
                                    })
                                    .then(response => {
                                      newRating.id = response.id ?? 0;
                                      const newRatings = new Map(ratings).set(
                                        JSON.stringify(ratingKey),
                                        newRating
                                      );
                                      setRatings(newRatings);
                                      props.postUpdated(
                                        deepClone(
                                          props.post,
                                          p =>
                                            (p.ratings = deepClone(
                                              Array.from(newRatings.values())
                                            ))
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
            {sortedComments.map(comment => {
              const readOnly =
                comment.userX?.id !== userX?.id &&
                !userX?.isTeacher &&
                !userX?.isAdminX;
              return (
                <div key={comment.id ?? 0} className="post-in-feed-comment">
                  <UserXAvatar userX={comment?.userX} />
                  <div className="global-flex-column" style={{gap: 0}}>
                    <div style={props?.getUserXHighlightStyle?.(comment.userX)}>
                      <PostHeader
                        userX={comment?.userX}
                        postTimeMs={toLong(comment?.postTimeMs ?? 0)}
                        deleteIconClicked={() => deleteComment(comment)}
                        readOnly={readOnly}
                        aiIconClicked={
                          userX?.viewAiPrompts && comment.aiPrompt
                            ? () => setShowAiPrompt(comment)
                            : undefined
                        }
                      />
                    </div>
                    <HtmlEditor
                      id={comment.id?.toString() ?? ''}
                      value={
                        comment.id === commentBeingEdited?.id
                          ? newCommentContent
                          : comment.longDescrHtml
                      }
                      readOnly={readOnly}
                      placeholder="No comment. Click to edit."
                      editingPlaceholder="Type your comment here..."
                      startEditing={() => {
                        if (comment.id !== commentBeingEdited?.id) {
                          saveCommentBeingEdited()
                            .then(() => {
                              setCommentBeingEdited(comment);
                              setNewCommentContent(comment.longDescrHtml ?? '');
                            })
                            .catch(global.setError);
                        }
                      }}
                      finishEditing={() => {
                        saveCommentBeingEdited().catch(global.setError);
                      }}
                      onChange={value => {
                        if (comment.id === commentBeingEdited?.id) {
                          setNewCommentContent(value);
                        }
                      }}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
      {showAiPrompt && (
        <Modal open={!!showAiPrompt} onClose={() => setShowAiPrompt(undefined)}>
          <textarea
            style={{
              position: 'absolute',
              top: '50%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
              width: '90%',
              height: '90%',
              overflow: 'auto',
              padding: '2em',
            }}
          >
            {showAiPrompt.aiPrompt + '\n\n-----\n\n' + showAiPrompt.aiResponse}
          </textarea>
        </Modal>
      )}
    </>
  );
}
