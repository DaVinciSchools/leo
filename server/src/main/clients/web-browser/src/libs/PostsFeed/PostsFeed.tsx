import {ProjectPost} from '../ProjectPost/ProjectPost';
import {pl_types} from '../../generated/protobuf-js';
import IProjectPost = pl_types.IProjectPost;
import {useEffect, useState} from 'react';
import {DeepReadonly, replaceInPlace} from '../misc';

interface PostMetadata {
  post: IProjectPost;
}

export function PostsFeed(props: {posts: DeepReadonly<IProjectPost[]>}) {
  const [posts, setPosts] = useState<DeepReadonly<PostMetadata[]>>([]);

  useEffect(() => {
    const existingMetadata = new Map(posts.map(m => [m.post.id, m]));
    setPosts(props.posts.map(post => existingMetadata.get(post.id) ?? {post}));
  }, [props.posts]);

  return (
    <>
      <div className="post-feed-posts">
        {posts?.map((post, index) => {
          console.log(
            index,
            post.post?.id,
            posts.map(e => e.post?.id)
          );
          return (
            <ProjectPost
              key={post.post?.id ?? 0}
              post={post.post}
              onUpdatePost={updatedPost =>
                setPosts(
                  replaceInArray(posts, {post: updatedPost}, e => e.post?.id)
                )
              }
              // saveStatus={saveStatus}
              // editingCommentId={editingCommentId}
              // setEditingCommentId={id => {
              //   autoSave.forceDelayedAction(() => setEditingCommentId(id));
              // }}
              // addComment={() => {
              //   autoSave.forceDelayedAction(() =>
              //     createService(PostService, 'PostService')
              //       .upsertProjectPostComment({
              //         projectPostComment: {
              //           longDescrHtml: '',
              //           projectPost: post.post,
              //         },
              //       })
              //       .then(response => {
              //         post.post.comments = [
              //           response.projectPostComment!,
              //           ...(post.post.comments ?? []),
              //         ];
              //         setPosts([...posts]);
              //         setEditingCommentId(
              //           response.projectPostComment?.id ?? undefined
              //         );
              //       })
              //       .catch(global.setError)
              //   );
              // }}
              // setCommentToSave={comment => {
              //   autoSave.trigger();
              //   setCommentToSave(comment);
              // }}
              // deleteCommentId={id => {
              //   autoSave.forceDelayedAction(() => {
              //     setEditingCommentId(undefined);
              //     createService(PostService, 'PostService')
              //       .deleteProjectPostComment({
              //         projectPostCommentId: id,
              //       })
              //       .then(() => {
              //         const index = post.post?.comments?.findIndex(
              //           comment => comment.id === id
              //         );
              //         if (index != null && index >= 0) {
              //           post.post?.comments?.splice(index, 1);
              //           setPosts([...posts]);
              //         }
              //       })
              //       .catch(global.setError);
              //   });
              // }}
            />
          );
        })}
      </div>
    </>
  );
}
