import {useNavigate} from 'react-router';
import {useContext, useEffect} from 'react';
import {GlobalStateContext} from '../../libs/GlobalState';

export function RedirectToDashboard() {
  const global = useContext(GlobalStateContext);
  if (!global.requireUser(user => user?.isAuthenticated)) {
    return <></>;
  }

  const navigate = useNavigate();
  useEffect(() => {
    if (global.user?.isAdmin) {
      navigate('/dashboards/admin-dashboard.html');
    } else if (global.user?.isTeacher) {
      navigate('/dashboards/teacher-dashboard.html');
    } else if (global.user?.isStudent) {
      navigate('/dashboards/student-dashboard.html');
    } else if (global.user?.isAuthenticated) {
      navigate('/projects/all-projects.html');
    }
  });

  return <></>;
}
