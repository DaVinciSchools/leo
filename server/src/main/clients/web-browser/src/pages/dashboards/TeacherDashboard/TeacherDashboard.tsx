import './TeacherDashboard.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {Grid} from '@mui/material';
import ClassTwoToneIcon from '@mui/icons-material/ClassTwoTone';
import DescriptionTwoToneIcon from '@mui/icons-material/DescriptionTwoTone';
import NotificationsTwoToneIcon from '@mui/icons-material/NotificationsTwoTone';
import {TitledPaper} from '../../../libs/TitledBox/TitledPaper';

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
              content: (
                <>
                  <Grid container spacing={1} paddingX={1} paddingY={1}>
                    <Grid item xs={4}>
                      <TitledPaper
                        title="Classes"
                        icon={<ClassTwoToneIcon />}
                        highlightColor="red"
                      >
                        TODO
                      </TitledPaper>
                    </Grid>
                    <Grid item xs={8}>
                      <TitledPaper
                        title="Assignments"
                        icon={<DescriptionTwoToneIcon />}
                        highlightColor="blue"
                      >
                        TODO
                      </TitledPaper>
                    </Grid>
                    <Grid item xs={4}>
                      <TitledPaper
                        title="Notifications"
                        icon={<NotificationsTwoToneIcon />}
                        highlightColor="green"
                      >
                        TODO
                      </TitledPaper>
                    </Grid>
                  </Grid>
                </>
              ),
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
