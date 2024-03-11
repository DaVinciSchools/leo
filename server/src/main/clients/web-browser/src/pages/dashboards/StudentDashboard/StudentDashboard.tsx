import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {TabbedPanel} from '../../../libs/TabbedPanel/TabbedPanel';
import {useContext, useEffect} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {PostsTab} from './PostsTab';
import {CreatePostTab} from './CreatePostTab';
import {defaultEducationFilters} from '../../../libs/EducationFilter/EducationFilters';
import {useFormFields} from '../../../libs/form_utils/forms';

enum StudentTab {
  // ASSIGNMENTS,
  // CLASSES,
  CREATE_POST,
  // OVERVIEW,
  POSTS,
}

export function StudentDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be a student to view this dashboard.',
    userX => userX.isAdminX || userX.isStudent
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
      <DefaultPage title={(userX?.isAdminX ? 'Student ' : '') + 'Dashboard'}>
        <TabbedPanel
          tabKeyEnum={StudentTab}
          defaultTabKey={StudentTab.POSTS}
          tabs={[
            // {
            //   key: StudentTab.OVERVIEW,
            //   label: 'Overview',
            //   content: <OverviewTab />,
            // },
            {
              key: StudentTab.POSTS,
              label: 'Posts',
              content: <PostsTab educationFilters={educationFilters} />,
            },
            {
              key: StudentTab.CREATE_POST,
              label: 'Create Post',
              content: <CreatePostTab />,
            },
            // {
            //   key: StudentTab.CLASSES,
            //   label: 'Classes',
            //   content: <ClassesTab />,
            // },
            // {
            //   key: StudentTab.ASSIGNMENTS,
            //   label: 'Assignments',
            //   content: <AssignmentsTab />,
            // },
          ]}
        />
      </DefaultPage>
    </>
  );
}
