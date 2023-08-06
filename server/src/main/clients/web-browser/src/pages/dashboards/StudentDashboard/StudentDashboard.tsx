import './StudentDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {sendToLogin} from '../../../libs/authentication';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {OverviewTab} from './OverviewTab';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

enum TabValue {
  OVERVIEW,
}

export function StudentDashboard() {
  const global = useContext(GlobalStateContext);
  if (!global.user?.isStudent && !global.user?.isAdmin) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage
        title={(global.user?.isAdmin ? 'Student ' : '') + 'Dashboard'}
      >
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
