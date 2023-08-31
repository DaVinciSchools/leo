import {pl_types} from '../../generated/protobuf-js';
import IProjectPost = pl_types.IProjectPost;

export function PostPaper(props: {post: IProjectPost}) {
  return <>{props.post?.name}</>;
}
