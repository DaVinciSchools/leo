import './TeacherDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {TabbedPanel} from '../../../libs/TabbedPanel/TabbedPanel';
import {PostsTab} from './PostsTab';
import {useContext, useEffect} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {OverviewTab} from './OverviewTab';
import {AssignmentsTab} from './AssignmentsTab';
import {StudentsTab} from './StudentsTab';
import {ClassesTab} from './ClassesTab';
import {useFormFields} from '../../../libs/form_utils/forms';
import {defaultEducationFilters} from '../../../libs/EducationFilter/EducationFilters';

enum TeacherTab {
  ASSIGNMENTS,
  CLASSES,
  OVERVIEW,
  POSTS,
  STUDENTS,
}

export function TeacherDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be a teacher to view this dashboard.',
    userX => userX.isAdminX || userX.isTeacher
  );

  const filterForm = useFormFields();
  const educationFilters = defaultEducationFilters(userX, filterForm);

  useEffect(() => {
    if (!userX) {
      filterForm.setValuesObject({});
      return;
    }
  }, [userX]);

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title={(userX?.isAdminX ? 'Teacher ' : '') + 'Dashboard'}>
        <div style={{height: '100%'}}>
          <TabbedPanel
            tabKeyEnum={TeacherTab}
            defaultTabKey={TeacherTab.OVERVIEW}
            tabs={[
              {
                key: TeacherTab.OVERVIEW,
                label: 'Overview',
                content: <OverviewTab />,
              },
              {
                key: TeacherTab.POSTS,
                label: 'Posts',
                content: <PostsTab educationFilters={educationFilters} />,
              },
              {
                key: TeacherTab.CLASSES,
                label: 'Classes',
                content: <ClassesTab />,
              },
              {
                key: TeacherTab.ASSIGNMENTS,
                label: 'Assignments',
                content: (
                  <AssignmentsTab
                    educationFilters={educationFilters}
                    assignmentsFilter={educationFilters.assignmentsFilter}
                  />
                ),
              },
              {
                key: TeacherTab.STUDENTS,
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
