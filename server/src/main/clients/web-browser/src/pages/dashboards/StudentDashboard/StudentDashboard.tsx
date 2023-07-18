import './StudentDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {OverviewTab} from './OverviewTab';

enum TabValue {
  OVERVIEW,
}

export function StudentDashboard() {
  const user = getCurrentUser();
  if (user == null || (!user.isStudent && !user.isAdmin)) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage title={(user.isAdmin ? 'Student ' : '') + 'Dashboard'}>
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
