import './TeacherDashboard.scss';

import NotificationsTwoToneIcon from '@mui/icons-material/NotificationsTwoTone';
import {TitledPaper} from '../../../libs/TitledPaper/TitledPaper';
import {PersistedReactGridLayout} from '../../../libs/PersistedReactGridLayout/PersistedReactGridLayout';
import {useContext, useEffect, useState} from 'react';
import {
  class_x_management_service,
  pl_types,
  post_service,
  school_management,
  user_x_management,
} from '../../../generated/protobuf-js';
import IClassX = pl_types.IClassX;
import {GlobalStateContext} from '../../../libs/GlobalState';
import {
  filterAutocompleteFormField,
  useFormFields,
} from '../../../libs/form_utils/forms';
import SchoolManagementService = school_management.SchoolManagementService;
import {createService} from '../../../libs/protos';
import ISchool = pl_types.ISchool;
import {MultiSchoolAutocomplete} from '../../../libs/common_fields/MultiSchoolAutocomplete';
import {SCHOOL_SORTER} from '../../../libs/sorters';
import {MultiClassXAutocomplete} from '../../../libs/common_fields/MultiClassXAutocomplete';
import ClassXManagementService = class_x_management_service.ClassXManagementService;
import PostService = post_service.PostService;
import IProjectPost = pl_types.IProjectPost;
import {FilterAltTwoTone} from '@mui/icons-material';
import {PostsFeed} from '../../../libs/PostsFeed/PostsFeed';
import {DeepReadonly} from '../../../libs/misc';
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import {Button} from '@mui/material';
import {SearchForUserXModal} from '../../../libs/SearchForUserX/SearchForUserXModal';

export function OverviewTab() {
  const global = useContext(GlobalStateContext);

  const [projectPosts, setProjectPosts] = useState<readonly IProjectPost[]>([]);

  const filterForm = useFormFields();

  // Maintain school filters.

  const [schoolOptions, setSchoolOptions] = useState<readonly ISchool[]>([]);
  const schoolFilter = filterForm.useAutocompleteFormField<readonly ISchool[]>(
    'schoolFilter',
    {
      isAutocomplete: {
        isMultiple: true,
      },
      disabled: !global.userX,
    }
  );

  useEffect(() => {
    if (!global.userX) {
      setSchoolOptions([]);
      setClassXOptions([]);
      filterForm.setValuesObject({});
      return;
    }

    createService(SchoolManagementService, 'SchoolManagementService')
      .getSchools({districtId: global.userX.districtId})
      .then(response => {
        const options = response.schools.sort(SCHOOL_SORTER);
        setSchoolOptions(options);
        filterAutocompleteFormField(schoolFilter, school => school.id, options);
      })
      .catch(global.setError);
  }, [global.userX]);

  // Maintain classX filters.

  const [classXOptions, setClassXOptions] = useState<readonly IClassX[]>([]);
  const classXFilter = filterForm.useAutocompleteFormField<readonly IClassX[]>(
    'classXFilter',
    {
      isAutocomplete: {
        isMultiple: true,
      },
    }
  );

  useEffect(() => {
    if ((schoolFilter.getValue()?.length ?? 0) === 0) {
      setClassXOptions([]);
      return;
    }

    createService(ClassXManagementService, 'ClassXManagementService')
      .getClassXs({schoolIds: schoolFilter.getValue()?.map?.(e => e.id ?? 0)})
      .then(response => {
        const options = response.classXs;
        setClassXOptions(options);
        filterAutocompleteFormField(classXFilter, classX => classX.id, options);
      })
      .catch(global.setError);
  }, [schoolFilter.getValue()]);

  // Maintain userX filters.

  const [userXFilter, setUserXFilter] = useState<
    DeepReadonly<IFullUserXDetails>[]
  >([]);
  const [showSearchForStudent, setShowSearchForStudent] =
    useState<boolean>(false);

  // Update project posts.

  useEffect(() => {
    createService(PostService, 'PostService')
      .getProjectPosts({
        schoolIds: schoolFilter.getValue()?.map?.(e => e.id ?? 0),
        classXIds: classXFilter.getValue()?.map?.(e => e.id ?? 0),
        userXIds: userXFilter.map(e => e.userX?.id ?? 0),
        includeProjects: true,
        includeComments: true,
        includeTags: true,
        beingEdited: false,
      })
      .then(response => {
        setProjectPosts(response.projectPosts);
      })
      .catch(global.setError);
  }, [schoolFilter.getValue(), classXFilter.getValue(), userXFilter]);

  if (!global.requireUserX(userX => userX?.isAdminX || userX?.isTeacher)) {
    return <></>;
  }

  return (
    <>
      <PersistedReactGridLayout
        id="TeacherDashboard_Overview"
        cols={12}
        rows={12}
        gap={{x: 20, y: 20}}
        padding={{x: 3, y: 20}}
        panels={[
          {
            id: 'filter_posts',
            panel: (
              <TitledPaper
                title="Filter Posts"
                icon={<FilterAltTwoTone />}
                highlightColor="orange"
                draggableCursorType="move"
              >
                <div
                  className="global-flex-column"
                  style={{marginTop: '0.5em', gap: '1em'}}
                >
                  <MultiSchoolAutocomplete
                    sortedSchools={schoolOptions}
                    formField={schoolFilter}
                    placeholder={() => 'Filter by School'}
                    InputLabelProps={{shrink: true}}
                  />
                  <MultiClassXAutocomplete
                    sortedClassXs={classXOptions}
                    formField={classXFilter}
                    placeholder={hasOptions =>
                      hasOptions ? 'Filter by Class' : 'Select Schools'
                    }
                    InputLabelProps={{shrink: true}}
                  />
                  <Button
                    className="global-button"
                    onClick={() => {
                      setShowSearchForStudent(true);
                    }}
                  >
                    Select Student
                    {userXFilter.map(userX => (
                      <>
                        <br />
                        {userX.userX?.emailAddress ?? ''}
                      </>
                    ))}
                  </Button>
                </div>
              </TitledPaper>
            ),
            layout: {x: 8, y: 0, w: 4, h: 6},
          },
          {
            id: 'posts',
            panel: <PostsFeed posts={projectPosts} />,
            layout: {x: 0, y: 0, w: 8, h: 12},
          },
          {
            id: 'notifications',
            panel: (
              <TitledPaper
                title="Notifications"
                icon={<NotificationsTwoToneIcon />}
                highlightColor="green"
                draggableCursorType="move"
              >
                TODO
              </TitledPaper>
            ),
            layout: {x: 8, y: 6, w: 4, h: 6},
          },
        ]}
      />
      <SearchForUserXModal
        showSearchBox={showSearchForStudent}
        title={'Search for Student'}
        onSelect={userX => {
          setShowSearchForStudent(false);
          setUserXFilter(userX ? [userX] : []);
        }}
        baseRequest={{
          includeStudents: true,
          schoolIds: schoolOptions.map(e => e.id ?? 0),
          classXIds: classXOptions.map(e => e.id ?? 0),
        }}
      />
    </>
  );
}
