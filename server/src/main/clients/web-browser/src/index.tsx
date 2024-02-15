import './index.scss';
import React, {PropsWithChildren, StrictMode, useEffect} from 'react';
import ReactDOM from 'react-dom/client';
import {
  createBrowserRouter,
  RouterProvider,
  useLocation,
} from 'react-router-dom';
import reportWebVitals from './reportWebVitals';
import {createTheme, ThemeProvider} from '@mui/material/styles';

import {Root} from './pages/Root';
import {Login} from './pages/user_xs/Login/Login';
import {EditDistricts} from './pages/profiles/EditDistricts/EditDistricts';
import {EditSchools} from './pages/profiles/EditSchools/EditSchools';
import {DefaultPageNav} from './libs/DefaultPage/DefaultPageNav';
import {MyAccount} from './pages/user_xs/MyAccount/MyAccount';
import {PrivacyPolicy} from './pages/docs/PrivacyPolicy';
import {Logout} from './pages/user_xs/Logout/Logout';
import {Accounts} from './pages/admin_x/Accounts/Accounts';
import {StudentDashboard} from './pages/dashboards/StudentDashboard/StudentDashboard';
import {TeacherDashboard} from './pages/dashboards/TeacherDashboard/TeacherDashboard';
import {AdminXDashboard} from './pages/dashboards/AdminXDashboard/AdminXDashboard';
import {RedirectToDashboard} from './pages/dashboards/RedirectToDashboard';
import {DemoProjectBuilder} from './pages/demos/DemoProjectBuilder/DemoProjectBuilder';
import {MechanicalEngineering} from './pages/schools/dvs/MechanicalEngineering';
import {SchoolsIndex} from './pages/schools/SchoolsIndex';
import {MetricType} from 'web-vitals';
import {ClassXManagement} from './pages/class_x_management/ClassXManagement';
import {ProjectsDashboard} from './pages/projects/ProjectsDashboard';
import {GlobalStateProvider} from './libs/GlobalStateProvider/GlobalStateProvider';

function GaTags(props: PropsWithChildren<{title: string}>) {
  const location = useLocation();

  useEffect(() => {
    if ('gtag' in window && typeof window.gtag === 'function') {
      document.title = 'Project Leo: ' + props.title;
      window.gtag('event', 'page_view', {
        page_path: location.pathname + location.search,
      });
    }
  }, [location]);

  return <>{props.children}</>;
}

function sendToGoogleAnalytics({name, delta, value, id}: MetricType) {
  if ('gtag' in window && typeof window.gtag === 'function') {
    window.gtag('event', name, {
      // Built-in params:
      value: delta, // Use `delta` so the value can be summed.
      // Custom params:
      metric_id: id, // Needed to aggregate events.
      metric_value: value, // Optional.
      metric_delta: delta, // Optional.
    });
  }
}

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const router = createBrowserRouter([
  {
    path: '/users/login.html',
    element: (
      <GaTags title="Login">
        <Login />
      </GaTags>
    ),
  },
  {
    path: '/users/logout.html',
    element: (
      <GaTags title="Logout">
        <Logout />
      </GaTags>
    ),
  },
  {
    path: '/admin',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'accounts.html',
        element: (
          <GaTags title="Account Administration">
            <Accounts />
          </GaTags>
        ),
      },
    ],
  },
  {
    path: '/class-management',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'class-management.html',
        element: (
          <GaTags title="Class Management">
            <ClassXManagement />
          </GaTags>
        ),
      },
    ],
  },
  {
    path: '/demos',
    children: [
      {
        path: 'project-builder.html',
        element: (
          <GaTags title="Demo Project Builder">
            <DemoProjectBuilder />
          </GaTags>
        ),
      },
    ],
  },
  {
    path: '/dashboards/redirect.html',
    element: <RedirectToDashboard />,
  },
  {
    path: '/dashboards',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'admin-dashboard.html',
        element: (
          <GaTags title="Admin Dashboard">
            <AdminXDashboard />
          </GaTags>
        ),
      },
      {
        path: 'student-dashboard.html',
        element: (
          <GaTags title="Student Dashboard">
            <StudentDashboard />
          </GaTags>
        ),
      },
      {
        path: 'teacher-dashboard.html',
        element: (
          <GaTags title="Teacher Dashboard">
            <TeacherDashboard />
          </GaTags>
        ),
      },
    ],
  },
  {
    path: '/users',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'my-account.html',
        element: (
          <GaTags title="My Account">
            <MyAccount />
          </GaTags>
        ),
      },
    ],
  },
  {
    path: '/docs',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'privacy-policy.html',
        element: (
          <GaTags title="Privacy Policy">
            <PrivacyPolicy />
          </GaTags>
        ),
      },
    ],
  },
  {
    path: '/profiles',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'edit-districts.html',
        element: (
          <GaTags title="Edit Districts">
            <EditDistricts />
          </GaTags>
        ),
      },
      {
        path: 'edit-schools.html',
        element: (
          <GaTags title="Edit Schools">
            <EditSchools />
          </GaTags>
        ),
      },
    ],
  },
  {
    path: '/projects',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'projects.html',
        element: (
          <GaTags title="Projects Dashboard">
            <ProjectsDashboard />
          </GaTags>
        ),
      },
    ],
  },
  {
    path: '/schools',
    children: [
      {
        path: 'index.html',
        element: (
          <GaTags title="Schools">
            <SchoolsIndex />
          </GaTags>
        ),
      },
      {
        path: 'dvs',
        children: [
          {
            path: 'mechanical-engineering.html',
            element: (
              <GaTags title="School of Mechanical Engineering">
                <MechanicalEngineering />
              </GaTags>
            ),
          },
        ],
      },
    ],
  },
  {
    path: '',
    children: [
      {
        path: '',
        element: (
          <GaTags title="Engaging Students. Empowering Teachers.">
            <Root />
          </GaTags>
        ),
      },
      {
        path: '/',
        element: (
          <GaTags title="Engaging Students. Empowering Teachers.">
            <Root />
          </GaTags>
        ),
      },
    ],
  },
]);

const theme = createTheme();

root.render(
  <StrictMode>
    <ThemeProvider theme={theme}>
      <GlobalStateProvider>
        <RouterProvider router={router} />
      </GlobalStateProvider>
    </ThemeProvider>
  </StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals(sendToGoogleAnalytics);
