import './StudentDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {OverviewTab} from './OverviewTab';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

enum TabValue {
  OVERVIEW,
}

export function StudentDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be an administrator to view this dashboard.',
    userX => userX.isAdminX || userX.isStudent
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title={(userX?.isAdminX ? 'Student ' : '') + 'Dashboard'}>
        <TabbedSwiper
          tabs={[
            {
              key: TabValue.OVERVIEW,
              label: 'Overview',
              content: <OverviewTab />,
            },
          ]}
        />
      </DefaultPage>
    </>
  );
}
