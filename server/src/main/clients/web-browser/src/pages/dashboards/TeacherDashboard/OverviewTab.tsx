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
} from '../../../generated/protobuf-js';
import IClassX = pl_types.IClassX;
import {GlobalStateContext} from '../../../libs/GlobalState';
import {filterAutocompleteFormField, useFormFields} from '../../../libs/forms';
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

export function OverviewTab() {
  const global = useContext(GlobalStateContext);

  const [schoolOptions, setSchoolOptions] = useState<ISchool[]>([]);
  const [classXOptions, setClassXOptions] = useState<IClassX[]>([]);
  const [projectPosts, setProjectPosts] = useState<IProjectPost[]>([]);

  const filterForm = useFormFields();

  // Maintain school filters.

  const schoolFilter = filterForm.useAutocompleteFormField<ISchool[]>(
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

  const classXFilter = filterForm.useAutocompleteFormField<IClassX[]>(
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

  // Update project posts.

  useEffect(() => {
    createService(PostService, 'PostService')
      .getProjectPosts({
        schoolIds: schoolFilter.getValue()?.map?.(e => e.id ?? 0),
        classXIds: classXFilter.getValue()?.map?.(e => e.id ?? 0),
        includeProjects: true,
        includeComments: true,
        beingEdited: false,
      })
      .then(response => {
        setProjectPosts(response.projectPosts);
      })
      .catch(global.setError);
  }, [schoolFilter.getValue(), classXFilter.getValue()]);

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
                </div>
              </TitledPaper>
            ),
            layout: {x: 8, y: 0, w: 4, h: 5},
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
            layout: {x: 8, y: 5, w: 4, h: 7},
          },
        ]}
      />
    </>
  );
}
