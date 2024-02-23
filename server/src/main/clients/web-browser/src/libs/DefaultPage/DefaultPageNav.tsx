import {GlobalStateContext} from '../GlobalState';
import {Link} from 'react-router-dom';
import {Outlet, To, useLocation} from 'react-router';
import {useContext, useState} from 'react';
import AppBar from '@mui/material/AppBar';
import Drawer from '@mui/material/Drawer';
import IconButton from '@mui/material/IconButton';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MenuIcon from '@mui/icons-material/Menu';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import {styled} from 'styled-components';
import {
  ExpandLess,
  ExpandMore,
  Home,
  Person,
  Rocket,
  Settings,
} from '@mui/icons-material';
import {Collapse, Divider} from '@mui/material';
import {AvatarNavMenu} from './AvatarNavMenu';

const drawerWidth = 220;

const Wrapper = styled.div`
  display: flex;
`;

const StyledAppBar = styled(AppBar)`
  background: white;
  border-bottom: 1px solid ${props => props.theme.palette.grey[300]};
  color: ${props => props.theme.palette.warning.main};

  ${props => props.theme.breakpoints.up('sm')} {
    width: calc(100% - ${drawerWidth}px);
    margin-left: ${drawerWidth}px;
    color: black;
  }
`;

const StyledLogoLink = styled(Link)`
  display: block;
  padding: ${props => props.theme.spacing(2)};

  img {
    max-width: 100%;
  }
`;

const Nav = styled.nav`
  ${props => props.theme.breakpoints.up('sm')} {
    width: ${drawerWidth}px;
    flex-shrink: 0;
  }
`;

const Main = styled.main`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  flex-grow: 1;
  padding-left: ${props => props.theme.spacing(4)};
  padding-right: ${props => props.theme.spacing(4)};

  ${props => props.theme.breakpoints.up('sm')} {
    width: calc(100% - ${drawerWidth}px);
  }
`;

interface NavItem {
  label: string;
  to?: To;
  icon?: React.ReactNode;
  nestedItems?: NavItem[];
  divider?: boolean;
}
interface NavItemProps extends NavItem {
  currentPathname: string;
  onClick: () => void;
}

const StyledListItemIcon = styled(ListItemIcon)`
  min-width: 24px;
  margin-right: 16px;
`;

function NavItem({
  label,
  icon,
  to,
  nestedItems,
  divider,
  onClick,
  currentPathname,
}: NavItemProps) {
  const [open, setOpen] = useState(false);
  const nestedListItems =
    nestedItems?.map(item => {
      return (
        <NavItem
          key={item.label}
          onClick={onClick}
          currentPathname={currentPathname}
          {...item}
        />
      );
    }) ?? [];
  const nestedList = nestedItems ? (
    <Collapse in={open} timeout="auto" unmountOnExit>
      <List component="div" disablePadding>
        {nestedListItems}
      </List>
    </Collapse>
  ) : undefined;

  return (
    <>
      {divider && <Divider />}
      <ListItemButton
        key={label}
        onClick={() => {
          if (nestedList) {
            setOpen(!open);
          }
          onClick();
        }}
        component={to ? Link : 'div'}
        to={to}
        selected={currentPathname === to}
      >
        <StyledListItemIcon>{icon}</StyledListItemIcon>
        <ListItemText primary={label} />
        {nestedList && (open ? <ExpandLess /> : <ExpandMore />)}
      </ListItemButton>
      {nestedList}
    </>
  );
}

export function DefaultPageNav() {
  const global = useContext(GlobalStateContext);
  const location = useLocation();
  const userX = global.optionalUserX();

  const [mobileOpen, setMobileOpen] = useState(false);
  const [isClosing, setIsClosing] = useState(false);

  const navItems: (NavItem & {
    include: boolean;
  })[] = [
    {
      label: 'Admin Dashboard',
      icon: <Home />,
      include: !!userX?.isAdminX,
      to: '/dashboards/admin-dashboard.html',
    },
    {
      label: 'Student Dashboard',
      icon: <Home />,
      include: !!userX?.isAdminX || !!userX?.isStudent,
      to: '/dashboards/student-dashboard.html',
    },
    {
      label: 'Teacher Dashboard',
      icon: <Home />,
      include: !!userX?.isAdminX || !!userX?.isTeacher,
      to: '/dashboards/teacher-dashboard.html',
    },
    {
      label: 'Class Management',
      icon: <Person />,
      include: !!userX?.isAdminX || !!userX?.isTeacher,
      to: '/class-management/class-management.html',
    },
    {
      label: 'Projects',
      icon: <Rocket />,
      to: '/projects/projects.html',
      include: true,
    },
    {
      label: 'Administration',
      icon: <Settings />,
      include: !!userX?.isAdminX,
      divider: true,
      nestedItems: [
        {
          label: 'Accounts',
          to: '/admin/accounts.html',
          include: true,
        },
        {
          label: 'Schools',
          to: '/profiles/edit-schools.html',
          include: true,
        },
        {
          label: 'Districts',
          to: '/profiles/edit-districts.html',
          include: true,
        },
      ],
    },
  ].filter(item => item.include);

  const handleDrawerClose = () => {
    setIsClosing(true);
    setMobileOpen(false);
  };

  const handleDrawerTransitionEnd = () => {
    setIsClosing(false);
  };

  const handleDrawerToggle = () => {
    if (!isClosing) {
      setMobileOpen(!mobileOpen);
    }
  };

  const drawer = (
    <div>
      <StyledLogoLink to="/">
        <img src="/images/logo-orange-on-white.svg" />
      </StyledLogoLink>
      <List>
        {navItems.map(item => (
          <NavItem
            key={item.label}
            currentPathname={location.pathname}
            onClick={handleDrawerClose}
            {...item}
          />
        ))}
      </List>
    </div>
  );

  return (
    <Wrapper>
      <StyledAppBar position="fixed" elevation={0}>
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            sx={{mr: 2, display: {sm: 'none'}}}
          >
            <MenuIcon />
          </IconButton>
          {/* TODO Move page title here from DefaultPage */}
          <Typography variant="h6" noWrap component="div" sx={{flexGrow: 1}}>
            {' '}
          </Typography>
          <AvatarNavMenu />
        </Toolbar>
      </StyledAppBar>
      <Nav>
        {/* The implementation can be swapped with js to avoid SEO duplication of links. */}
        <Drawer
          variant="temporary"
          open={mobileOpen}
          onTransitionEnd={handleDrawerTransitionEnd}
          onClose={handleDrawerClose}
          ModalProps={{
            keepMounted: true, // Better open performance on mobile.
          }}
          sx={{
            display: {xs: 'block', sm: 'none'},
            '& .MuiDrawer-paper': {boxSizing: 'border-box', width: drawerWidth},
          }}
        >
          {drawer}
        </Drawer>
        <Drawer
          variant="permanent"
          sx={{
            display: {xs: 'none', sm: 'block'},
            '& .MuiDrawer-paper': {boxSizing: 'border-box', width: drawerWidth},
          }}
          open
        >
          {drawer}
        </Drawer>
      </Nav>
      <Main>
        <Toolbar />
        <Outlet />
      </Main>
    </Wrapper>
  );
}
