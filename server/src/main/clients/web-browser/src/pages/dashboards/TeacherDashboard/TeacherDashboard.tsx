import './TeacherDashboard.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {PostsTab} from './PostsTab';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {OverviewTab} from './OverviewTab';
import {AssignmentsTab} from './AssignmentsTab';
import {StudentsTab} from './StudentsTab';
import {ClassesTab} from './ClassesTab';

enum TabValue {
  ASSIGNMENTS,
  CLASSES,
  OVERVIEW,
  POSTS,
  STUDENTS,
}

export function TeacherDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be a teacher to view this dashboard.',
    userX => userX.isAdminX || userX.isTeacher
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title={(userX?.isAdminX ? 'Teacher ' : '') + 'Dashboard'}>
        <div style={{height: '100%'}}>
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
                key: TabValue.CLASSES,
                label: 'Classes',
                content: <ClassesTab />,
              },
              {
                key: TabValue.ASSIGNMENTS,
                label: 'Assignments',
                content: <AssignmentsTab />,
              },
              {
                key: TabValue.STUDENTS,
                label: 'Students',
                content: <StudentsTab />,
              },
            ]}
          />
        </div>
      </DefaultPage>
    </>
  );
}
