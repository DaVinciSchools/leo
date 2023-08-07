import './AdminDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function AdminDashboard() {
  const global = useContext(GlobalStateContext);
  if (!global.requireUser(user => user?.isAdmin)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Admin Dashboard">TODO</DefaultPage>
    </>
  );
}
