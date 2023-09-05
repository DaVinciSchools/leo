import './PostHeader.scss';

import {pl_types} from '../../generated/protobuf-js';
import {AccountCircle} from '@mui/icons-material';
import IUserX = pl_types.IUserX;
import IProject = pl_types.IProject;
import {toLong} from '../misc';
import Long from 'long';

export function PostHeader(props: {
  userX: IUserX;
  project?: IProject;
  postTitle?: string;
  postTimeMs?: Long | null;
  isComment?: boolean;
}) {
  return (
    <>
      <div className="global-flex-row post-header" style={{gap: '0.5em'}}>
        <span className="post-header-avatar">
          <AccountCircle />
        </span>
        <div className="global-flex-column">
          <div
            className="global-flex-row"
            style={{
              gap: '0.5em',
              justifyContent: 'space-between',
            }}
          >
            <div className="global-flex-row" style={{alignItems: 'baseline'}}>
              <div className="post-header-name">
                {props.userX.firstName ?? ''}&nbsp;
                {props.userX.lastName ?? ''}
              </div>
              <div className="post-header-project-name">
                {props.project?.name ?? ''}
              </div>
              <div
                className="post-header-date"
                style={{display: props.postTimeMs != null ? undefined : 'none'}}
              >
                {new Date(
                  toLong(props.postTimeMs ?? 0)?.toNumber()
                ).toLocaleDateString()}
              </div>
            </div>
          </div>
          {!props.isComment && (
            <div className="post-header-title">
              {props.postTitle ?? 'Untitled'}
              &nbsp;
            </div>
          )}
        </div>
      </div>
    </>
  );
}
