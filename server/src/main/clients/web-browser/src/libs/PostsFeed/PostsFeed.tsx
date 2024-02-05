import {Post} from '../Post/Post';
import {pl_types, post_service} from 'pl-pb';
import {CSSProperties, useContext, useEffect, useRef, useState} from 'react';
import {createService} from '../protos';
import {GlobalStateContext} from '../GlobalState';
import {DeepReadOnly, replaceInDeepReadOnly} from '../misc';
import IProjectPost = pl_types.IProjectPost;
import PostService = post_service.PostService;
import IGetProjectPostsRequest = post_service.IGetProjectPostsRequest;
import IUserX = pl_types.IUserX;

const PAGE_SIZE = 25;

export function PostsFeed(
  props: DeepReadOnly<{
    posts?: IProjectPost[];
    request?: IGetProjectPostsRequest;
    paged?: boolean;
    getUserXHighlightStyle?: (
      userX?: DeepReadOnly<IUserX> | null | undefined
    ) => CSSProperties | undefined;
  }>
) {
  const global = useContext(GlobalStateContext);
  const [posts, setPosts] = useState<DeepReadOnly<IProjectPost[]>>([]);
  // This request is expensive. So, verify that the request has changed before issuing it again.
  const lastGetProjectsPostRequest = useRef('');
  const [pageToLoad, setPageToLoad] = useState(0);
  const pagesLoaded = useRef(new Set<number>());
  const [allPagesLoaded, setAllPagesLoaded] = useState(false);

  useEffect(() => {
    if (props.posts != null) {
      lastGetProjectsPostRequest.current = '';
      setPosts([...(props.posts ?? [])]);
    } else if (props.request != null) {
      const newGetProjectsPostRequest = JSON.stringify(props.request);
      if (lastGetProjectsPostRequest.current !== newGetProjectsPostRequest) {
        lastGetProjectsPostRequest.current = newGetProjectsPostRequest;
        if (props.paged) {
          setPosts([]);
          setPageToLoad(0);
          pagesLoaded.current.clear();
          setAllPagesLoaded(false);
          loadNextPage([], 0);
        } else {
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
    }
  }, [props.posts, props.request]);

  function loadNextPage(
    posts: DeepReadOnly<IProjectPost[]>,
    pageToLoad: number
  ) {
    console.assert(props.request);
    if (!pagesLoaded.current.has(pageToLoad)) {
      pagesLoaded.current.add(pageToLoad);
      setPageToLoad(page => page + 1);
      createService(PostService, 'PostService')
        .getProjectPosts(
          Object.assign({}, props.request, {
            page: pageToLoad,
            pageSize: PAGE_SIZE,
          } as IGetProjectPostsRequest)
        )
        .then(response => {
          setPosts([...posts, ...response.projectPosts]);
          setAllPagesLoaded(response.projectPosts.length < PAGE_SIZE);
        })
        .catch(global.setError);
    }
  }

  console.assert(
    props.posts || props.request,
    'PostsFeed: requires posts or request.'
  );

  return (
    <>
      <div className="post-feed-posts">
        {posts.map(post => (
          <Post
            key={post.id ?? 0}
            post={post}
            postUpdated={post => {
              setPosts(replaceInDeepReadOnly(posts, post, p => p.id));
            }}
            showComments={props.request?.includeComments}
            showRatings={props.request?.includeRatings}
            getUserXHighlightStyle={props.getUserXHighlightStyle}
          />
        ))}
        <div
          className="post-feed-load-more"
          style={{
            display: props.paged && posts.length > 0 ? undefined : 'none',
          }}
        >
          {!allPagesLoaded && (
            <button onClick={() => loadNextPage(posts, pageToLoad)}>
              Load More Posts
            </button>
          )}
        </div>
      </div>
    </>
  );
}
