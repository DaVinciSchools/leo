import './TeacherDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';

export function TeacherDashboard() {
  const user = getCurrentUser();
  if (user == null || (!user.isTeacher && !user.isAdmin)) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage title={(user.isAdmin ? 'Teacher ' : '') + 'Dashboard'}>
        TODO
      </DefaultPage>
    </>
  );
}
