import './ProjectCard.scss';

import {Card, Form, Input, InputRef} from 'antd';
import {
  CheckCircleOutlined,
  CheckCircleTwoTone,
  DislikeOutlined,
  DislikeTwoTone,
  LikeOutlined,
  LikeTwoTone,
} from '@ant-design/icons';
import {ReactNode, useContext, useRef, useState} from 'react';
import {pl_types} from 'pl-pb';
import {PsychologyTwoTone} from '@mui/icons-material';
import {GlobalStateContext} from '../GlobalState';
import {Modal} from '@mui/material';
import IProject = pl_types.IProject;
import ThumbsState = pl_types.Project.ThumbsState;

export function ProjectCard(props: {
  id: number;
  name: string;
  shortDescr: string;
  longDescrHtml: string;
  active: boolean;
  favorite: boolean;
  thumbsState: ThumbsState;
  thumbsStateReason: string;
  updateProject: (update: IProject) => void;
  showDetails: () => void;
  aiPrompt?: string | null | undefined;
  aiResponse?: string | null | undefined;
}) {
  const global = useContext(GlobalStateContext);
  const userX = global.optionalUserX();

  const [showAiPrompt, setShowAiPrompt] = useState(false);
  const thumbsQueryRef = useRef<InputRef>(null);
  const actions: readonly ReactNode[] = [
    <>
      {props.thumbsState === ThumbsState.THUMBS_UP ? (
        <LikeTwoTone
          twoToneColor="#0000cc"
          onClick={() =>
            props.updateProject({thumbsState: ThumbsState.UNSET_THUMBS_STATE})
          }
        />
      ) : (
        <LikeOutlined
          onClick={() => {
            props.updateProject({thumbsState: ThumbsState.THUMBS_UP});
            if (thumbsQueryRef.current != null) {
              thumbsQueryRef.current.focus();
            }
          }}
        />
      )}
    </>,
    <>
      {props.thumbsState === ThumbsState.THUMBS_DOWN ? (
        <DislikeTwoTone
          twoToneColor="#0000cc"
          onClick={() =>
            props.updateProject({thumbsState: ThumbsState.UNSET_THUMBS_STATE})
          }
        />
      ) : (
        <DislikeOutlined
          onClick={() => {
            props.updateProject({
              favorite: false,
              thumbsState: ThumbsState.THUMBS_DOWN,
            });
            if (thumbsQueryRef.current != null) {
              thumbsQueryRef.current.focus();
            }
          }}
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
          onClick={() => {
            props.updateProject({
              active: true,
            });
            if (thumbsQueryRef.current != null) {
              thumbsQueryRef.current.focus();
            }
          }}
        />
      )}
    </>,
  ];

  const [thumbsStateReason, setThumbsStateReason] = useState(
    props.thumbsStateReason
  );

  return (
    <>
      <Card
        id={props.id.toString()}
        title={props.name}
        extra={
          <>
            <span className="details-link" onClick={props.showDetails}>
              Details
            </span>
            {!!userX?.viewAiPrompts && !!props.aiPrompt && (
              <PsychologyTwoTone
                className="global-two-tone-ai-color"
                onClick={() => setShowAiPrompt(true)}
                cursor="pointer"
              />
            )}
          </>
        }
        actions={actions.slice()}
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
        <Form>
          <Input
            ref={thumbsQueryRef}
            type="text"
            placeholder={
              props.active
                ? 'Why did you choose this project?'
                : props.thumbsState === ThumbsState.THUMBS_UP
                  ? 'Why do you like this project?'
                  : props.thumbsState === ThumbsState.THUMBS_DOWN
                    ? 'Why do you dislike this project?'
                    : 'What do you think about this project?'
            }
            name="thumbs_reason"
            value={thumbsStateReason}
            onChange={e => setThumbsStateReason(e.target.value)}
            onBlur={() =>
              props.updateProject({
                thumbsStateReason: thumbsStateReason,
              })
            }
          />
        </Form>
      </Card>
      {showAiPrompt && (
        <Modal open={showAiPrompt} onClose={() => setShowAiPrompt(false)}>
          <textarea
            style={{
              position: 'absolute',
              top: '50%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
              width: '90%',
              height: '90%',
              overflow: 'auto',
              padding: '2em',
            }}
          >
            {(props.aiPrompt ?? '') +
              '\n\n-----\n\n' +
              (props.aiResponse ?? '')}
          </textarea>
        </Modal>
      )}
    </>
  );
}
