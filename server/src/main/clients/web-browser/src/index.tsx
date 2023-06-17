import './index.scss';
import React from 'react';
import ReactDOM from 'react-dom/client';
import {createBrowserRouter, RouterProvider} from 'react-router-dom';
import reportWebVitals from './reportWebVitals';

import {Root} from './pages/Root';
import {Login} from './pages/users/Login/Login';
import {EditDistricts} from './pages/profiles/EditDistricts/EditDistricts';
import {EditSchools} from './pages/profiles/EditSchools/EditSchools';
import {DefaultPageNav} from './libs/DefaultPage/DefaultPageNav';
import {IkigaiBuilder} from './pages/projects/IkigaiBuilder/IkigaiBuilder';
import {MyProjects} from './pages/projects/MyProjects/MyProjects';
import {MyAccount} from './pages/users/MyAccount/MyAccount';
import {PrivacyPolicy} from './pages/docs/PrivacyPolicy';
import {Overview} from './pages/projects/Overview/Overview';
import {Logout} from './pages/users/Logout/Logout';
import {AllProjects} from './pages/projects/AllProjects/AllProjects';
import {Accounts} from './pages/admin/Accounts/Accounts';
import {StudentDashboard} from './pages/dashboards/StudentDashboard/StudentDashboard';
import {TeacherDashboard} from './pages/dashboards/TeacherDashboard/TeacherDashboard';
import {AdminDashboard} from './pages/dashboards/AdminDashboard/AdminDashboard';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const router = createBrowserRouter([
  {
    path: '/users/login.html',
    element: <Login />,
  },
  {
    path: '/users/logout.html',
    element: <Logout />,
  },
  {
    path: '/admin',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'accounts.html',
        element: <Accounts />,
      },
    ],
  },
  {
    path: '/dashboards',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'admin-dashboard.html',
        element: <AdminDashboard />,
      },
      {
        path: 'student-dashboard.html',
        element: <StudentDashboard />,
      },
      {
        path: 'teacher-dashboard.html',
        element: <TeacherDashboard />,
      },
    ],
  },
  {
    path: '/users',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'my-account.html',
        element: <MyAccount />,
      },
    ],
  },
  {
    path: '/docs',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'privacy-policy.html',
        element: <PrivacyPolicy />,
      },
    ],
  },
  {
    path: '/profiles',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'edit-districts.html',
        element: <EditDistricts />,
      },
      {
        path: 'edit-schools.html',
        element: <EditSchools />,
      },
    ],
  },
  {
    path: '/projects',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'overview.html',
        element: <Overview />,
      },
      {
        path: 'ikigai-builder.html',
        element: <IkigaiBuilder />,
      },
      {
        path: 'my-projects.html',
        element: <MyProjects />,
      },
      {
        path: 'all-projects.html',
        element: <AllProjects />,
      },
    ],
  },
  {
    path: '',
    children: [
      {
        path: '',
        element: <Root />,
      },
      {
        path: '/',
        element: <Root />,
      },
    ],
  },
]);

root.render(
  <React.StrictMode>
    <>
      <RouterProvider router={router} />
    </>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
