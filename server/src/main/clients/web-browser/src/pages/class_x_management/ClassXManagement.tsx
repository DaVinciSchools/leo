import './ClassXManagement.scss';

import {useContext} from 'react';
import {GlobalStateContext} from '../../libs/GlobalState';
import {DefaultPage} from '../../libs/DefaultPage/DefaultPage';
import {TabbedSwiper} from '../../libs/TabbedSwiper/TabbedSwiper';
import {OverviewTab} from './OverviewTab';
import {EditClassXsTab} from './EditClassXsTab';
import {EditAssignmentsTab} from './EditAssignmentsTab';

enum TabValue {
  OVERVIEW,
  CLASS,
  ASSIGNMENTS,
}

export function ClassXManagement() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be a teacher to administer classes.',
    userX => userX.isAdminX || userX.isTeacher
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Class Management">
        <div style={{height: '100%'}}>
          <TabbedSwiper
            tabs={[
              {
                key: TabValue.OVERVIEW,
                label: 'Overview',
                content: <OverviewTab />,
              },
              {
                key: TabValue.CLASS,
                label: 'Edit Classes',
                content: <EditClassXsTab />,
              },
              {
                key: TabValue.ASSIGNMENTS,
                label: 'Edit Assignments',
                content: <EditAssignmentsTab />,
              },
            ]}
          />
        </div>
      </DefaultPage>
    </>
  );
}
