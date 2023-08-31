import './PostPaper.scss';

import {pl_types} from '../../generated/protobuf-js';
import IProjectPost = pl_types.IProjectPost;
import {toLong} from '../misc';
import ReactQuill from 'react-quill';
import {AccountCircle} from '@mui/icons-material';

export function PostPaper(props: {post: IProjectPost}) {
  return (
    <>
      <div className="global-flex-column post-paper-post">
        <div
          className="global-flex-row"
          style={{justifyContent: 'space-between'}}
        >
          <div
            className="global-flex-row"
            style={{alignItems: 'center', gap: '0.5em'}}
          >
            <div>
              <AccountCircle />
            </div>
            <div>
              <span className="post-paper-name">
                {props.post?.userX?.firstName ?? ''}&nbsp;
                {props.post?.userX?.lastName ?? ''}
              </span>
              <br />
              <span className="post-paper-title">
                {props.post?.name ?? 'Untitled'}&nbsp;
              </span>
            </div>
          </div>
          <div className="post-paper-date">
            {new Date(
              toLong(props.post?.postTimeMs ?? 0).toNumber()
            ).toLocaleDateString()}
          </div>
        </div>
        <div className="post-paper-content">
          <ReactQuill
            theme="snow"
            className="global-react-quill"
            value={props.post?.longDescrHtml ?? ''}
            preserveWhitespace={true}
            modules={{toolbar: false}}
            style={{padding: 0}}
          />
        </div>
      </div>
    </>
  );
}
