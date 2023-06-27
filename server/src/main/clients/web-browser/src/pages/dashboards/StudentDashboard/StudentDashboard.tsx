import './StudentDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {PersistedReactGridLayout} from '../../../libs/PersistedReactGridLayout/PersistedReactGridLayout';
import {TitledPaper} from '../../../libs/TitledPaper/TitledPaper';
import RocketOutlinedIcon from '@mui/icons-material/RocketOutlined';
import BadgeOutlinedIcon from '@mui/icons-material/BadgeOutlined';
import FavoriteBorderOutlinedIcon from '@mui/icons-material/FavoriteBorderOutlined';
import NotificationsTwoToneIcon from '@mui/icons-material/NotificationsTwoTone';
import SentimentSatisfiedOutlinedIcon from '@mui/icons-material/SentimentSatisfiedOutlined';
import PostAddOutlinedIcon from '@mui/icons-material/PostAddOutlined';

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
              content: (
                <PersistedReactGridLayout
                  id="StudentDashboard_Overview"
                  cols={9}
                  rows={9}
                  gap={{x: 20, y: 20}}
                  padding={{x: 0, y: 20}}
                  panels={[
                    {
                      id: 'my_projects',
                      panel: (
                        <TitledPaper
                          title="My Projects"
                          icon={<RocketOutlinedIcon />}
                          highlightColor="orange"
                          draggable={true}
                        >
                          TODO
                        </TitledPaper>
                      ),
                      layout: {x: 0, y: 0, w: 3, h: 3},
                    },
                    {
                      id: 'my_careers',
                      panel: (
                        <TitledPaper
                          title="My Careers"
                          icon={<BadgeOutlinedIcon />}
                          highlightColor="blue"
                          draggable={true}
                        >
                          TODO
                        </TitledPaper>
                      ),
                      layout: {x: 3, y: 0, w: 2, h: 3},
                    },
                    {
                      id: 'my_passions',
                      panel: (
                        <TitledPaper
                          title="My Passions"
                          icon={<FavoriteBorderOutlinedIcon />}
                          highlightColor="red"
                          draggable={true}
                        >
                          TODO
                        </TitledPaper>
                      ),
                      layout: {x: 5, y: 0, w: 2, h: 3},
                    },
                    {
                      id: 'my_motivations',
                      panel: (
                        <TitledPaper
                          title="My Motivations"
                          icon={<SentimentSatisfiedOutlinedIcon />}
                          highlightColor="yellow"
                          draggable={true}
                        >
                          TODO
                        </TitledPaper>
                      ),
                      layout: {x: 7, y: 0, w: 2, h: 3},
                    },
                    {
                      id: 'timeline',
                      panel: (
                        <TitledPaper
                          title="Timeline"
                          icon={<PostAddOutlinedIcon />}
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
          ]}
        />
      </DefaultPage>
    </>
  );
}
