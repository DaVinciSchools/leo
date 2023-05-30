import './DefaultPage.scss';
import {Outlet} from 'react-router';
import {Layout, Menu} from 'antd';
import {
  AppstoreOutlined,
  DesktopOutlined,
  HomeOutlined,
  RocketOutlined,
  SettingOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {getCurrentUser, sendToLogin} from '../authentication';
import {Link} from 'react-router-dom';
const Footer = Layout.Footer;
import {useState} from 'react';

const {Header, Sider, Content} = Layout;

enum MenuKeys {
  ADMIN,
  ALL_PROJECTS,
  DASHBOARD,
  EDIT_DISTRICTS,
  EDIT_SCHOOLS,
  EDIT_USERS,
  HOME,
  IKIGAI_BUILDER,
  INTERNSHIPS,
  MY_ACCOUNT,
  MY_PROJECTS,
  OVERVIEW,
  PROJECTS,
}

export function DefaultPageNav() {
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const [collapsed, setCollapsed] = useState(false);

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
              <Menu mode="inline" className="top-menu">
                <Menu.Item key={MenuKeys.HOME} icon={<HomeOutlined />}>
                  Home
                </Menu.Item>
                <Menu.Item key={MenuKeys.DASHBOARD} icon={<AppstoreOutlined />}>
                  Dashboard
                </Menu.Item>
                <Menu.SubMenu
                  key={MenuKeys.PROJECTS}
                  icon={<RocketOutlined />}
                  title="Projects"
                >
                  <Menu.Item key={MenuKeys.OVERVIEW}>
                    <Link to="/projects/overview">Overview</Link>
                  </Menu.Item>
                  <Menu.Item key={MenuKeys.MY_PROJECTS}>
                    <Link to="/projects/my-projects">My Projects</Link>
                  </Menu.Item>
                  <Menu.Item key={MenuKeys.IKIGAI_BUILDER}>
                    <Link to="/projects/ikigai-builder">Ikigai Builder</Link>
                  </Menu.Item>
                  <Menu.Item key={MenuKeys.ALL_PROJECTS}>
                    <Link to="/projects/all-projects">All Projects</Link>
                  </Menu.Item>
                </Menu.SubMenu>
                <Menu.Item
                  key={MenuKeys.INTERNSHIPS}
                  icon={<DesktopOutlined />}
                >
                  Internships
                </Menu.Item>
                <Menu.SubMenu
                  key={MenuKeys.ADMIN}
                  icon={<SettingOutlined />}
                  title="Admin"
                  style={{
                    display: user != null && user.isAdmin ? 'block' : 'none',
                  }}
                >
                  <Menu.Item key={MenuKeys.EDIT_DISTRICTS}>
                    <Link to="/profiles/edit-districts">Edit Districts</Link>
                  </Menu.Item>
                  <Menu.Item key={MenuKeys.EDIT_SCHOOLS}>
                    <Link to="/profiles/edit-schools">Edit Schools</Link>
                  </Menu.Item>
                  <Menu.Item key={MenuKeys.EDIT_USERS}>
                    <Link to="/profiles/edit-users">Edit Users</Link>
                  </Menu.Item>
                </Menu.SubMenu>
              </Menu>
            </Content>
            <Footer>
              <Menu mode="inline" className="top-menu">
                <Menu.Item key={MenuKeys.MY_ACCOUNT} icon={<UserOutlined />}>
                  <Link to="/users/my-account">My Account</Link>
                </Menu.Item>
              </Menu>
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
