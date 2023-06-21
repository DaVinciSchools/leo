import './DefaultPage.scss';

import {getCurrentUser} from '../authentication';
import {
  BellOutlined,
  QuestionCircleOutlined,
  SmileTwoTone,
} from '@ant-design/icons';
import {PropsWithChildren, ReactNode} from 'react';
import {Popover} from 'antd';
import {Link} from 'react-router-dom';

export function DefaultPage(props: PropsWithChildren<{title: ReactNode}>) {
  const user = getCurrentUser();

  const avatarPanel =
    user != null ? (
      <div className="avatar-panel">
        <div className="avatar-icon">
          <SmileTwoTone />
        </div>
        <div className="name">
          <span style={{whiteSpace: 'nowrap'}}>
            {user?.firstName} {user?.lastName}
          </span>
        </div>
        <div className="email">{user?.emailAddress}</div>
        <div className="section-div" />
        <div className="menu">
          <Link to="/users/my-account.html">My Account</Link>
        </div>
        <div className="menu">
          <Link to="/users/logout.html">Log out</Link>
        </div>
      </div>
    ) : (
      <></>
    );

  return (
    <>
      <div className="page">
        <div className="page-header">
          <div className="page-account">
            <QuestionCircleOutlined />
            <BellOutlined />
            <Popover
              content={avatarPanel}
              placement="bottomRight"
              trigger="click"
            >
              <div className="page-account">
                <span className="avatar-icon">
                  <SmileTwoTone />
                </span>
                {user?.firstName ?? ''} {user?.lastName ?? ''}
              </div>
            </Popover>
          </div>
        </div>
        <div className="page-title">{props.title}</div>
        <div className="page-body">{props.children}</div>
      </div>
    </>
  );
}
