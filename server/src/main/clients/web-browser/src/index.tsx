import './index.scss';
import React from 'react';
import ReactDOM from 'react-dom/client';
import {createBrowserRouter, RouterProvider} from 'react-router-dom';
import reportWebVitals from './reportWebVitals';

import {Root} from './pages/Root';
import {EditUsers} from './pages/profiles/EditUsers/EditUsers';
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

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const router = createBrowserRouter([
  {
    path: '/users/login',
    element: <Login />,
  },
  {
    path: '/users/logout',
    element: <Logout />,
  },
  {
    path: '/users',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'my-account',
        element: <MyAccount />,
      },
    ],
  },
  {
    path: '/docs',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'privacy-policy',
        element: <PrivacyPolicy />,
      },
    ],
  },
  {
    path: '/profiles',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'edit-districts',
        element: <EditDistricts />,
      },
      {
        path: 'edit-schools',
        element: <EditSchools />,
      },
      {
        path: 'edit-users',
        element: <EditUsers />,
      },
    ],
  },
  {
    path: '/projects',
    element: <DefaultPageNav />,
    children: [
      {
        path: 'overview',
        element: <Overview />,
      },
      {
        path: 'ikigai-builder',
        element: <IkigaiBuilder />,
      },
      {
        path: 'my-projects',
        element: <MyProjects />,
      },
      {
        path: 'all-projects',
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
