import './TeacherDashboard.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';

enum TabValue {
  ASSIGNMENTS,
  OVERVIEW,
}

export function TeacherDashboard() {
  const user = getCurrentUser();
  if (user == null || (!user.isTeacher && !user.isAdmin)) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage title={(user.isAdmin ? 'Teacher ' : '') + 'Dashboard'}>
        <TabbedSwiper
          tabs={[
            {
              key: TabValue.OVERVIEW,
              label: 'Overview',
              content: 'Overview',
            },
            {
              key: TabValue.ASSIGNMENTS,
              label: 'Assignments',
              content: 'Assignments',
            },
          ]}
          tabsProps={{
            style: {borderBottom: 'rgba(5, 5, 5, 0.06) solid 2px'},
          }}
        />
      </DefaultPage>
    </>
  );
}
