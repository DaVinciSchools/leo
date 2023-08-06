import './DefaultPage.scss';

import {
  BellOutlined,
  QuestionCircleOutlined,
  SmileTwoTone,
} from '@ant-design/icons';
import {PropsWithChildren, ReactNode, useContext} from 'react';
import {Popover} from 'antd';
import {Link} from 'react-router-dom';
import {GlobalStateContext} from '../GlobalState';

export function DefaultPage(props: PropsWithChildren<{title: ReactNode}>) {
  const global = useContext(GlobalStateContext);

  const avatarPanel =
    global.user != null ? (
      <div className="avatar-panel">
        <div className="avatar-icon">
          <SmileTwoTone />
        </div>
        <div className="name">
          <span style={{whiteSpace: 'nowrap'}}>
            {global.user?.firstName ?? ''} {global.user?.lastName ?? ''}
          </span>
        </div>
        <div className="email">{global.user?.emailAddress ?? ''}</div>
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
                {global.user?.firstName ?? ''} {global.user?.lastName ?? ''}
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
