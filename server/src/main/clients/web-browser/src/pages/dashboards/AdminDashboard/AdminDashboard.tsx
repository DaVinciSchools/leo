import './AdminDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {sendToLogin} from '../../../libs/authentication';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function AdminDashboard() {
  const global = useContext(GlobalStateContext);
  if (!global.user?.isAdmin) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage title="Admin Dashboard">TODO</DefaultPage>
    </>
  );
}
