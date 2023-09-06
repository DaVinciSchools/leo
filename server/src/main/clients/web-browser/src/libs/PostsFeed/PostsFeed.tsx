import {PostInFeed} from '../PostInFeed/PostInFeed';
import {pl_types, post_service} from '../../generated/protobuf-js';
import IProjectPost = pl_types.IProjectPost;
import {useContext, useEffect, useState} from 'react';
import {useDelayedAction} from '../delayed_action';
import IProjectPostComment = pl_types.IProjectPostComment;
import {createService} from '../protos';
import PostService = post_service.PostService;
import {GlobalStateContext} from '../GlobalState';

interface PostMetadata {
  post: IProjectPost;
}

export function PostsFeed(props: {posts: readonly IProjectPost[]}) {
  const global = useContext(GlobalStateContext);

  const [posts, setPosts] = useState<readonly PostMetadata[]>([]);

  const [saveStatus, setSaveStatus] = useState<string>('');
  const [editingCommentId, setEditingCommentId] = useState<number | null>(null);
  const [commentToSave, setCommentToSave] =
    useState<IProjectPostComment | null>(null);

  const autoSave = useDelayedAction(
    () => setSaveStatus('Modified'),
    () => {
      setSaveStatus('Saving...');
      const commentToSaveLocal = commentToSave;
      if (commentToSaveLocal != null) {
        createService(PostService, 'PostService')
          .upsertProjectPostComment({
            projectPostComment: commentToSaveLocal,
          })
          .then(() => {
            setSaveStatus('');
            setCommentToSave(null);
          })
          .catch(global.setError);
      } else {
        setSaveStatus('');
      }
    },
    1500
  );

  useEffect(() => {
    const existingMetadata = new Map(posts.map(m => [m.post.id, m]));
    setPosts(props.posts.map(post => existingMetadata.get(post.id) ?? {post}));
  }, [props.posts]);

  return (
    <>
      <div className="post-feed-posts">
        {posts?.map(post => (
          <PostInFeed
            key={post.post?.id ?? 0}
            post={post.post}
            saveStatus={saveStatus}
            editingCommentId={editingCommentId}
            setEditingCommentId={id => {
              autoSave.forceDelayedAction(() => setEditingCommentId(id));
            }}
            addComment={() => {
              autoSave.forceDelayedAction(() =>
                createService(PostService, 'PostService')
                  .upsertProjectPostComment({
                    projectPostComment: {
                      longDescrHtml: '',
                      projectPost: post.post,
                    },
                  })
                  .then(response => {
                    post.post.comments = [
                      response.projectPostComment!,
                      ...(post.post.comments ?? []).slice(),
                    ];
                    setPosts([...posts]);
                    setEditingCommentId(response.projectPostComment?.id ?? 0);
                  })
                  .catch(global.setError)
              );
            }}
            setCommentToSave={comment => {
              autoSave.trigger();
              setCommentToSave(comment);
            }}
          />
        ))}
      </div>
    </>
  );
}
