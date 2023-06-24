import './TeacherDashboard.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import ClassTwoToneIcon from '@mui/icons-material/ClassTwoTone';
import DescriptionTwoToneIcon from '@mui/icons-material/DescriptionTwoTone';
import NotificationsTwoToneIcon from '@mui/icons-material/NotificationsTwoTone';
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
                    cols={12}
                    rows={12}
                    gap={{x: 10, y: 10}}
                    padding={{x: 10, y: 10}}
                    panels={[
                      {
                        id: 'Classes',
                        panel: (
                          <TitledPaper
                            title="Classes"
                            icon={<ClassTwoToneIcon />}
                            highlightColor="red"
                            draggable={true}
                          >
                            TODO
                          </TitledPaper>
                        ),
                        layout: {x: 0, y: 0, w: 4, h: 4},
                      },
                      {
                        id: 'Assignments',
                        panel: (
                          <TitledPaper
                            title="Assignments"
                            icon={<DescriptionTwoToneIcon />}
                            highlightColor="blue"
                            draggable={true}
                          >
                            TODO
                          </TitledPaper>
                        ),
                        layout: {x: 4, y: 0, w: 4, h: 4},
                      },
                      {
                        id: 'Notifications',
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
                        layout: {x: 8, y: 0, w: 4, h: 4},
                      },
                      {
                        id: 'Extra',
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
                        layout: {x: 0, y: 4, w: 12, h: 8},
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
