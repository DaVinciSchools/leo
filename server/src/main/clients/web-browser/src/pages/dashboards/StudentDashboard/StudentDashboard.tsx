import './StudentDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';

export function StudentDashboard() {
  const user = getCurrentUser();
  if (user == null || (!user.isStudent && !user.isAdmin)) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage title={(user.isAdmin ? 'Student ' : '') + 'Dashboard'}>
        TODO
      </DefaultPage>
    </>
  );
}
