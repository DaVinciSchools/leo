import {useNavigate} from 'react-router';
import {useContext, useEffect} from 'react';
import {GlobalStateContext} from '../../libs/GlobalState';
import {TAB_PARAM_NAME} from '../../libs/TabbedPanel/TabbedPanel';
import {ProjectsTab} from '../projects/ProjectsDashboard';

export function RedirectToDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX();

  useEffect(() => {
    if (userX != null) {
      const navigate = useNavigate();
      if (userX?.isAdminX) {
        navigate('/dashboards/admin-dashboard.html');
      } else if (userX?.isTeacher) {
        navigate('/dashboards/teacher-dashboard.html');
      } else if (userX?.isStudent) {
        navigate('/dashboards/student-dashboard.html');
      } else {
        navigate(
          `/projects/projects.html?${TAB_PARAM_NAME}=${
            ProjectsTab[ProjectsTab.ALL_PROJECTS]
          }`
        );
      }
    }
  });

  return <>Redirecting...</>;
}
