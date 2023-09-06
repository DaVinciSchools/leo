import './ProjectPage.scss';
import 'react-quill/dist/quill.bubble.css';

import {Button, Card, Form, Input} from 'antd';
import {ChangeEvent, useState} from 'react';
import {CloseCircleTwoTone} from '@ant-design/icons';
import {pl_types} from '../../generated/protobuf-js';

import IProject = pl_types.IProject;
import IProjectPost = pl_types.IProjectPost;
import ReactQuill, {Value} from 'react-quill';

export function ProjectPage(props: {
  id: number;
  name: string;
  shortDescr: string;
  longDescrHtml: string;
  milestones: readonly pl_types.Project.IMilestone[];
  posts: readonly IProjectPost[] | undefined;
  updateProject: (update: IProject) => void;
  onSubmitPost: (name: string, message: string) => void;
  onDeletePost: (post: IProjectPost) => void;
  editable: boolean;
}) {
  const [name, setName] = useState('');
  const [longDescrHtml, setLongDescrHtml] = useState<Value>('');

  function submitPost(values: {name: string; message: string}) {
    props.onSubmitPost(values.name, values.message);
  }

  return (
    <>
      <div className="project-page">
        <div className="title">{props.name}</div>
        <div className="short-descr">
          <span className="label">Description:</span>
          <div className="description">{props.shortDescr}</div>
        </div>
        <div className="long-descr">
          <span className="label">Details:</span>
          <ReactQuill
            theme={'bubble'}
            preserveWhitespace={true}
            value={props.longDescrHtml ?? ''}
            readOnly={true}
            className="long-descr-html"
          />
        </div>
        <div>
          {props.milestones?.length > 0 && (
            <>
              <span className="label">Milestones:</span>
              <ol>
                {props.milestones.map(milestone => (
                  <li key={milestone.id!}>
                    <div className="milestone">{milestone.name}</div>
                    {(milestone.steps?.length ?? 0) > 0 && (
                      <ol type="A">
                        {milestone.steps!.map(step => (
                          <li key={step.id!}>{step.name}</li>
                        ))}
                      </ol>
                    )}
                  </li>
                ))}
              </ol>
            </>
          )}
        </div>
        {props.posts != null &&
          props.posts.map(post => (
            <>
              <Card
                title={
                  <>
                    {post.name ?? ''}
                    <div className="post-user-x-info">
                      {post.userX?.lastName ?? ''},&nbsp;
                      {post.userX?.firstName ?? ''}
                    </div>
                  </>
                }
                extra={
                  <CloseCircleTwoTone
                    twoToneColor="red"
                    onClick={() => props.onDeletePost(post)}
                  />
                }
              >
                <ReactQuill
                  theme={'bubble'}
                  preserveWhitespace={true}
                  value={post?.longDescrHtml ?? ''}
                  readOnly={true}
                />
              </Card>
            </>
          ))}
        {props.posts != null && props.editable && (
          <>
            <div className="title">Add Post</div>
            <Form onFinish={submitPost}>
              <Form.Item name="title" style={{margin: '0 0'}}>
                <Input
                  placeholder="Title"
                  value={name}
                  maxLength={255}
                  onChange={(e: ChangeEvent<HTMLInputElement>) =>
                    setName(e.target.value)
                  }
                />
              </Form.Item>
              <Form.Item name="message" style={{margin: '0 0'}}>
                <ReactQuill
                  theme="snow"
                  preserveWhitespace={true}
                  value={longDescrHtml}
                  onChange={setLongDescrHtml}
                />
              </Form.Item>
              <Form.Item style={{textAlign: 'end'}}>
                <Button type="primary" htmlType="submit">
                  Submit
                </Button>
              </Form.Item>
            </Form>
          </>
        )}
      </div>
    </>
  );
}
