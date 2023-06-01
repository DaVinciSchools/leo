import './ProjectPage.scss';

import {Button, Card, Form, Input} from 'antd';
import {pl_types} from '../../generated/protobuf-js';
import IProject = pl_types.IProject;
import IProjectPost = pl_types.IProjectPost;
import {ChangeEvent, useState} from 'react';
import {CloseCircleTwoTone} from '@ant-design/icons';

export function ProjectPage(props: {
  id: number;
  name: string;
  shortDescr: string;
  longDescr: string;
  posts: IProjectPost[] | undefined;
  updateProject: (update: IProject) => void;
  onSubmitPost: (title: string, message: string) => void;
  onDeletePost: (post: IProjectPost) => void;
  editable: boolean;
}) {
  const [title, setTitle] = useState('');
  const [message, setMessage] = useState('');

  function submitPost(values: {title: string; message: string}) {
    props.onSubmitPost(values.title, values.message);
  }

  return (
    <>
      <div className="project-page">
        <div className="title">{props.name}</div>
        <div className="short-descr">
          <span className="label">Description:</span> {props.shortDescr}
        </div>
        <div className="long-descr">
          <span className="label">Details:</span>
          {props.longDescr
            .split('\n')
            .map(line => line.split('\r').map(line => <div>{line}</div>))}
        </div>
        {props.posts != null &&
          props.posts.map(post => (
            <>
              <Card
                title={
                  <>
                    {post.title ?? ''}
                    <div className="post-user-info">
                      {post.user?.lastName ?? ''}, {post.user?.firstName ?? ''}
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
                {post?.message ?? ''}
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
                  value={title}
                  maxLength={255}
                  onChange={(e: ChangeEvent<HTMLInputElement>) =>
                    setTitle(e.target.value)
                  }
                />
              </Form.Item>
              <Form.Item name="message" style={{margin: '0 0'}}>
                <Input.TextArea
                  placeholder="Message"
                  rows={5}
                  value={message}
                  onChange={(e: ChangeEvent<HTMLTextAreaElement>) =>
                    setMessage(e.target.value)
                  }
                />
              </Form.Item>
              <Form.Item style={{textAlign: 'end', margin: '0 0'}}>
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
