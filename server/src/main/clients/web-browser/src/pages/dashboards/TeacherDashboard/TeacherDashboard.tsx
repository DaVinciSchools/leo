import './TeacherDashboard.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {sendToLogin} from '../../../libs/authentication';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {OverviewTab} from './OverviewTab';
import {AssignmentsTab} from './AssignmentsTab';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

enum TabValue {
  ASSIGNMENTS,
  OVERVIEW,
}

export function TeacherDashboard() {
  const global = useContext(GlobalStateContext);
  if (!global.user?.isTeacher && !global.user?.isAdmin) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage
        title={(global.user?.isAdmin ? 'Teacher ' : '') + 'Dashboard'}
      >
        <div style={{height: '100%'}}>
          <TabbedSwiper
            tabs={[
              {
                key: TabValue.OVERVIEW,
                label: 'Overview',
                content: <OverviewTab />,
              },
              {
                key: TabValue.ASSIGNMENTS,
                label: 'Assignments',
                content: <AssignmentsTab user={global.user} />,
              },
            ]}
          />
        </div>
      </DefaultPage>
    </>
  );
}
