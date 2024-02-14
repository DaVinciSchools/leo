import './Post.scss';

import {pl_types} from 'pl-pb';
import {
  EditTwoTone,
  PsychologyTwoTone,
  RemoveCircleTwoTone,
} from '@mui/icons-material';
import Long from 'long';
import IUserX = pl_types.IUserX;

export function PostHeader(props: {
  userX: IUserX | null | undefined;
  saveStatus?: string | null | undefined;
  postTimeMs?: Long | null | undefined;
  readOnly?: boolean | undefined;

  editIconClicked?: () => void;
  deleteIconClicked?: () => void;
  aiIconClicked?: () => void;
}) {
  return (
    <>
      <div className="post-in-feed-header">
        <div className="post-in-feed-user-name">
          {props.userX?.firstName ?? ''}&nbsp;
          {props.userX?.lastName ?? ''}
        </div>
        <EditTwoTone
          className="global-two-tone-pencil-color"
          onClick={props.editIconClicked}
          cursor="pointer"
          style={{display: props.editIconClicked ? undefined : 'none'}}
        />
        <div className="post-in-feed-save-status">
          &nbsp;{props.saveStatus}&nbsp;
        </div>
        <div className="post-in-feed-date">
          {new Date(props.postTimeMs?.toNumber() ?? 0).toLocaleDateString()}
        </div>
        {!props.readOnly && (
          <RemoveCircleTwoTone
            className="global-two-tone-delete-color"
            onClick={props.deleteIconClicked}
            cursor="pointer"
            style={{display: props.deleteIconClicked ? undefined : 'none'}}
          />
        )}
        {props.aiIconClicked && (
          <PsychologyTwoTone
            className="global-two-tone-ai-color"
            onClick={props.aiIconClicked}
            cursor="pointer"
          />
        )}
      </div>
    </>
  );
}
