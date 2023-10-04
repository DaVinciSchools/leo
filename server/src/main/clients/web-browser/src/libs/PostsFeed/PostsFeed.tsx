import {Post} from '../Post/Post';
import {pl_types, post_service} from '../../generated/protobuf-js';
import IProjectPost = pl_types.IProjectPost;
import {useContext, useEffect, useRef, useState} from 'react';
import {createService} from '../protos';
import PostService = post_service.PostService;
import {GlobalStateContext} from '../GlobalState';
import IGetProjectPostsRequest = post_service.IGetProjectPostsRequest;
import {DeepReadonly, replaceInPlace} from '../misc';

export function PostsFeed(props: {
  posts?: DeepReadonly<IProjectPost[]>;
  request?: DeepReadonly<IGetProjectPostsRequest>;
}) {
  const global = useContext(GlobalStateContext);

  const [posts, setPosts] = useState<DeepReadonly<IProjectPost>[]>([]);
  // This request is expensive. So, verify that the request has changed before issuing it again.
  const lastGetProjectsPostRequest = useRef('');

  useEffect(() => {
    if (props.posts != null) {
      lastGetProjectsPostRequest.current = '';
      setPosts([...(props.posts ?? [])]);
    } else if (props.request != null) {
      const newGetProjectsPostRequest = JSON.stringify(props.request);
      if (lastGetProjectsPostRequest.current !== newGetProjectsPostRequest) {
        lastGetProjectsPostRequest.current = newGetProjectsPostRequest;
        createService(PostService, 'PostService')
          .getProjectPosts(Object.assign(props.request))
          .then(response => {
            if (
              lastGetProjectsPostRequest.current === newGetProjectsPostRequest
            ) {
              setPosts(response.projectPosts ?? []);
            }
          })
          .catch(global.setError);
      }
    }
  }, [props.posts, props.request]);

  return (
    <>
      <div className="post-feed-posts">
        {posts.map(post => (
          <Post
            key={post.id ?? 0}
            post={post}
            postUpdated={(post, refresh) => {
              replaceInPlace(posts, post, p => p.id);
              if (refresh) {
                setPosts([...posts]);
              }
            }}
          />
        ))}
      </div>
    </>
  );
}
