import './StudentDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {OverviewTab} from './OverviewTab';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {PostsTab} from './PostsTab';
import {ClassesTab} from './ClassesTab';
import {AssignmentsTab} from './AssignmentsTab';
import {CreatePostTab} from './CreatePostTab';

enum TabValue {
  ASSIGNMENTS,
  CLASSES,
  CREATE_POST,
  OVERVIEW,
  POSTS,
}

export function StudentDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be a student to view this dashboard.',
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
            {
              key: TabValue.POSTS,
              label: 'Posts',
              content: <PostsTab />,
            },
            {
              key: TabValue.CREATE_POST,
              label: 'Create Post',
              content: <CreatePostTab />,
            },
            {
              key: TabValue.CLASSES,
              label: 'Classes',
              content: <ClassesTab />,
            },
            {
              key: TabValue.ASSIGNMENTS,
              label: 'Assignments',
              content: <AssignmentsTab />,
            },
          ]}
        />
      </DefaultPage>
    </>
  );
}
