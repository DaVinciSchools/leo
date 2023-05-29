import './ProjectCard.scss';

import {Card} from 'antd';
import {
  CheckCircleOutlined,
  CheckCircleTwoTone,
  DislikeOutlined,
  DislikeTwoTone,
  LikeOutlined,
  LikeTwoTone,
} from '@ant-design/icons';
import {pl_types} from '../../generated/protobuf-js';
import {ReactNode} from 'react';
import IProject = pl_types.IProject;
import ThumbsState = pl_types.Project.ThumbsState;

export function ProjectCard(props: {
  id: number;
  name: string;
  shortDescr: string;
  longDescr: string;
  active: boolean;
  favorite: boolean;
  thumbsState: ThumbsState;
  updateProject: (update: IProject) => void;
  showDetails: () => void;
}) {
  const actions: ReactNode[] = [
    <>
      {props.thumbsState === ThumbsState.THUMBS_UP ? (
        <LikeTwoTone
          twoToneColor="#0000cc"
          onClick={() => props.updateProject({thumbsState: ThumbsState.UNSET})}
        />
      ) : (
        <LikeOutlined
          onClick={() =>
            props.updateProject({thumbsState: ThumbsState.THUMBS_UP})
          }
        />
      )}
    </>,
    <>
      {props.thumbsState === ThumbsState.THUMBS_DOWN ? (
        <DislikeTwoTone
          twoToneColor="#0000cc"
          onClick={() => props.updateProject({thumbsState: ThumbsState.UNSET})}
        />
      ) : (
        <DislikeOutlined
          onClick={() =>
            props.updateProject({
              favorite: false,
              thumbsState: ThumbsState.THUMBS_DOWN,
            })
          }
        />
      )}
    </>,
    <>
      {props.active ? (
        <CheckCircleTwoTone
          twoToneColor="#00b500"
          onClick={() =>
            props.updateProject({
              active: false,
            })
          }
        />
      ) : (
        <CheckCircleOutlined
          onClick={() =>
            props.updateProject({
              active: true,
            })
          }
        />
      )}
    </>,
  ];

  return (
    <>
      <Card
        id={props.id.toString()}
        title={props.name}
        extra={
          <span className="details-link" onClick={props.showDetails}>
            Details
          </span>
        }
        actions={actions}
        className={
          props.active
            ? 'active'
            : props.favorite
            ? 'favorite'
            : props.thumbsState === ThumbsState.THUMBS_UP
            ? 'like'
            : props.thumbsState === ThumbsState.THUMBS_DOWN
            ? 'dislike'
            : undefined
        }
      >
        {props.shortDescr}
      </Card>
    </>
  );
}
