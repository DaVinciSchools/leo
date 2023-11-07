import './DefaultPage.scss';

import {
  BellOutlined,
  QuestionCircleOutlined,
  SmileTwoTone,
} from '@ant-design/icons';
import {GlobalStateContext} from '../GlobalState';
import {Link} from 'react-router-dom';
import {Popover} from 'antd';
import {CSSProperties, PropsWithChildren, ReactNode, useContext} from 'react';

export function DefaultPage(
  props: PropsWithChildren<{
    title: ReactNode;
    bodyStyle?: CSSProperties;
  }>
) {
  const userX = useContext(GlobalStateContext).requireUserX();

  const avatarPanel =
    userX != null ? (
      <div className="avatar-panel">
        <div className="avatar-icon">
          <SmileTwoTone />
        </div>
        <div className="name">
          <span style={{whiteSpace: 'nowrap'}}>
            {userX?.firstName ?? ''} {userX?.lastName ?? ''}
          </span>
        </div>
        <div className="email">{userX?.emailAddress ?? ''}</div>
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
          <div className="page-title">{props.title}</div>
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
                {userX?.firstName ?? ''} {userX?.lastName ?? ''}
              </div>
            </Popover>
          </div>
        </div>
        <div className="page-body" style={props.bodyStyle}>
          {props.children}
        </div>
      </div>
    </>
  );
}
