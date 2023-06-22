import {getCurrentUser, sendToLogin} from '../../libs/authentication';
import {useNavigate} from 'react-router';
import {useEffect} from 'react';

export function RedirectToDashboard() {
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const navigate = useNavigate();
  useEffect(() => {
    if (user.isAdmin) {
      navigate('/dashboards/admin-dashboard.html');
    } else if (user.isTeacher) {
      navigate('/dashboards/teacher-dashboard.html');
    } else {
      navigate('/dashboards/student-dashboard.html');
    }
  });

  return <></>;
}
