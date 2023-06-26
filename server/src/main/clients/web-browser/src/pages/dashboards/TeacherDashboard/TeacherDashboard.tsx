import './TeacherDashboard.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import DescriptionTwoToneIcon from '@mui/icons-material/DescriptionTwoTone';
import NotificationsTwoToneIcon from '@mui/icons-material/NotificationsTwoTone';
import QueryStatsIcon from '@mui/icons-material/QueryStats';
import {TitledPaper} from '../../../libs/TitledPaper/TitledPaper';
import {PersistedReactGridLayout} from '../../../libs/PersistedReactGridLayout/PersistedReactGridLayout';

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
        <div style={{height: '100%'}}>
          <TabbedSwiper
            tabs={[
              {
                key: TabValue.OVERVIEW,
                label: 'Overview',
                content: (
                  <PersistedReactGridLayout
                    id="TeacherDashboard_Overview"
                    cols={9}
                    rows={9}
                    gap={{x: 10, y: 10}}
                    padding={{x: 10, y: 10}}
                    panels={[
                      {
                        id: 'assignments',
                        panel: (
                          <TitledPaper
                            title="Assignments"
                            icon={<DescriptionTwoToneIcon />}
                            highlightColor="orange"
                            draggable={true}
                          >
                            TODO
                          </TitledPaper>
                        ),
                        layout: {x: 0, y: 0, w: 5, h: 3},
                      },
                      {
                        id: 'assignment_stats',
                        panel: (
                          <TitledPaper
                            title="Assignment Stats"
                            icon={<QueryStatsIcon />}
                            highlightColor="blue"
                            draggable={true}
                          >
                            TODO
                          </TitledPaper>
                        ),
                        layout: {x: 5, y: 0, w: 4, h: 3},
                      },
                      {
                        id: 'timeline',
                        panel: (
                          <TitledPaper
                            title="Timeline"
                            icon={<NotificationsTwoToneIcon />}
                            highlightColor="black"
                            draggable={true}
                          >
                            TODO
                          </TitledPaper>
                        ),
                        layout: {x: 0, y: 3, w: 5, h: 6},
                      },
                      {
                        id: 'notifications',
                        panel: (
                          <TitledPaper
                            title="Notifications"
                            icon={<NotificationsTwoToneIcon />}
                            highlightColor="green"
                            draggable={true}
                          >
                            TODO
                          </TitledPaper>
                        ),
                        layout: {x: 5, y: 3, w: 4, h: 6},
                      },
                    ]}
                  />
                ),
              },
              {
                key: TabValue.ASSIGNMENTS,
                label: 'Assignments',
                content: (
                  <>
                    <div style={{overflow: 'clip'}}>TODO</div>
                  </>
                ),
              },
            ]}
          />
        </div>
      </DefaultPage>
    </>
  );
}
