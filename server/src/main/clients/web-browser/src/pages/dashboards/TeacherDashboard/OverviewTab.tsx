import './TeacherDashboard.scss';

import DescriptionTwoToneIcon from '@mui/icons-material/DescriptionTwoTone';
import ForumIcon from '@mui/icons-material/Forum';
import NotificationsTwoToneIcon from '@mui/icons-material/NotificationsTwoTone';
import QueryStatsIcon from '@mui/icons-material/QueryStats';
import {TitledPaper} from '../../../libs/TitledPaper/TitledPaper';
import {PersistedReactGridLayout} from '../../../libs/PersistedReactGridLayout/PersistedReactGridLayout';

export function OverviewTab() {
  return (
    <>
      <PersistedReactGridLayout
        id="TeacherDashboard_Overview"
        cols={9}
        rows={9}
        gap={{x: 20, y: 20}}
        padding={{x: 3, y: 20}}
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
                icon={<ForumIcon />}
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
    </>
  );
}
