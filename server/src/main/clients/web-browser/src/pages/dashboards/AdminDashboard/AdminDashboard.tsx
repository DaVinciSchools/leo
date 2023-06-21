import './AdminDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';

export function AdminDashboard() {
  const user = getCurrentUser();
  if (user == null || !user.isAdmin) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage title="Admin Dashboard">TODO</DefaultPage>
    </>
  );
}
