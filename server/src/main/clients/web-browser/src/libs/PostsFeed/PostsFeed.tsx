import {PostInFeed} from '../PostInFeed/PostInFeed';
import {pl_types} from '../../generated/protobuf-js';
import IProjectPost = pl_types.IProjectPost;
import {useEffect, useState} from 'react';

interface PostMetadata {
  post: IProjectPost;
  showComments?: boolean;
}

export function PostsFeed(props: {posts: IProjectPost[]}) {
  const [posts, setPosts] = useState<PostMetadata[]>([]);

  useEffect(() => {
    const existingMetadata = new Map(posts.map(m => [m.post.id, m]));
    setPosts(props.posts.map(post => existingMetadata.get(post.id) ?? {post}));
  }, [props.posts]);

  function touchPosts(callback: () => void) {
    callback();
    setPosts([...posts]);
  }

  return (
    <>
      <div className="post-feed-posts">
        {posts?.map(post => (
          <PostInFeed
            key={post.post?.id ?? 0}
            post={post.post}
            showComments={post.showComments}
            chatIconClicked={() =>
              touchPosts(() => (post.showComments = !post.showComments))
            }
          />
        ))}
      </div>
    </>
  );
}
