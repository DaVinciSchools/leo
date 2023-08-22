import './TeacherDashboard.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
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
  if (!global.requireUserX(userX => userX?.isAdminX || userX?.isTeacher)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage
        title={(global.userX?.isAdminX ? 'Teacher ' : '') + 'Dashboard'}
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
                content: <AssignmentsTab userX={global.userX} />,
              },
            ]}
          />
        </div>
      </DefaultPage>
    </>
  );
}
