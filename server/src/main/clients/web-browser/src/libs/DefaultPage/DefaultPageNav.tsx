import './DefaultPage.scss';

import {
  BookOutlined,
  HomeOutlined,
  RocketOutlined,
  SettingOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {GlobalStateContext} from '../GlobalStateProvider/GlobalStateProvider';
import {Layout, Menu, MenuItemProps, MenuProps} from 'antd';
import {Link} from 'react-router-dom';
import {Outlet, useNavigate} from 'react-router';
import {useContext, useState} from 'react';

const Footer = Layout.Footer;
const {Header, Sider, Content} = Layout;

enum MenuKeys {
  ADMIN,
  ADMIN_ACCOUNTS,
  ADMIN_DISTRICTS,
  ADMIN_SCHOOLS,
  CLASS_MANAGEMENT,
  DASHBOARD_ADMIN,
  DASHBOARD_STUDENT,
  DASHBOARD_TEACHER,
  MY_ACCOUNT,
  PORTFOLIOS,
  PROJECTS,
}

export function DefaultPageNav() {
  const global = useContext(GlobalStateContext);
  const userX = global.getUserX();
  const navigate = useNavigate();
  const [collapsed, setCollapsed] = useState(false);

  const topMenuItems: MenuProps['items'] = [
    {
      label: 'Admin Dashboard',
      key: MenuKeys.DASHBOARD_ADMIN,
      icon: <HomeOutlined />,
      style: {
        display: userX?.isAdminX ? 'block' : 'none',
      },
    },
    {
      label: (userX?.isAdminX ? 'Student ' : '') + 'Dashboard',
      key: MenuKeys.DASHBOARD_STUDENT,
      icon: <HomeOutlined />,
      style: {
        display: userX?.isAdminX || userX?.isStudent ? 'block' : 'none',
      },
    },
    {
      label: (userX?.isAdminX ? 'Teacher ' : '') + 'Dashboard',
      key: MenuKeys.DASHBOARD_TEACHER,
      icon: <HomeOutlined />,
      style: {
        display: userX?.isAdminX || userX?.isTeacher ? 'block' : 'none',
      },
    },
    {
      label: 'Class Management',
      key: MenuKeys.CLASS_MANAGEMENT,
      icon: <UserOutlined />,
      style: {
        display: userX?.isAdminX || userX?.isTeacher ? 'block' : 'none',
      },
    },
    {
      label: 'Projects',
      key: MenuKeys.PROJECTS,
      icon: <RocketOutlined />,
    },
    {
      label: 'Portfolios',
      key: MenuKeys.PORTFOLIOS,
      icon: <BookOutlined />,
      style: {
        display: userX?.isAdminX || userX?.isStudent ? 'block' : 'none',
      },
    },
    {
      label: 'Administration',
      key: MenuKeys.ADMIN,
      icon: <SettingOutlined />,
      style: {
        display: userX?.isAdminX ? 'block' : 'none',
      },
      children: [
        {
          label: 'Accounts',
          key: MenuKeys.ADMIN_ACCOUNTS,
        },
        {label: 'Schools', key: MenuKeys.ADMIN_SCHOOLS},
        {
          label: 'Districts',
          key: MenuKeys.ADMIN_DISTRICTS,
        },
      ],
    },
  ];

  const bottomMenuItems: MenuProps['items'] = [
    {
      label: 'My Account',
      key: MenuKeys.MY_ACCOUNT,
      icon: <UserOutlined />,
    },
  ];

  const menuItemClicked: MenuItemProps['onClick'] = ({key}) => {
    switch (Number(key)) {
      case MenuKeys.ADMIN: {
        // not selectable.
        break;
      }
      case MenuKeys.ADMIN_ACCOUNTS: {
        navigate('/admin/accounts.html');
        break;
      }
      case MenuKeys.ADMIN_DISTRICTS: {
        navigate('/profiles/edit-districts.html');
        break;
      }
      case MenuKeys.ADMIN_SCHOOLS: {
        navigate('/profiles/edit-schools.html');
        break;
      }
      case MenuKeys.CLASS_MANAGEMENT: {
        navigate('/class-management/class-management.html');
        break;
      }
      case MenuKeys.DASHBOARD_ADMIN: {
        navigate('/dashboards/admin-dashboard.html');
        break;
      }
      case MenuKeys.DASHBOARD_STUDENT: {
        navigate('/dashboards/student-dashboard.html');
        break;
      }
      case MenuKeys.DASHBOARD_TEACHER: {
        navigate('/dashboards/teacher-dashboard.html');
        break;
      }
      case MenuKeys.MY_ACCOUNT: {
        navigate('/users/my-account.html');
        break;
      }
      case MenuKeys.PROJECTS: {
        navigate('/projects/projects.html');
        break;
      }
    }
  };

  return (
    <>
      <Layout>
        <Sider
          collapsible
          collapsed={collapsed}
          onCollapse={() => setCollapsed(!collapsed)}
          className="left-menu"
        >
          <Layout>
            <Header>
              <Link to="/">
                <img
                  src={
                    collapsed
                      ? '/images/logo-orange-on-white-circles.svg'
                      : '/images/logo-orange-on-white.svg'
                  }
                  alt="Project Leo Logo | Go Home"
                />
              </Link>
            </Header>
            <Content>
              <Menu
                mode="vertical"
                className="top-menu"
                items={topMenuItems}
                onClick={menuItemClicked}
              />
            </Content>
            <Footer>
              <Menu
                mode="vertical"
                className="bottom-menu"
                items={bottomMenuItems}
                onClick={menuItemClicked}
              />
            </Footer>
          </Layout>
        </Sider>
        <Content>
          <Outlet />
        </Content>
      </Layout>
    </>
  );
}
