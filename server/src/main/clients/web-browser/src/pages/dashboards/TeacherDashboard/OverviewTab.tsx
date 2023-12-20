import './TeacherDashboard.scss';
import {TitledPaper} from '../../../libs/TitledPaper/TitledPaper';
import {PersistedReactGridLayout} from '../../../libs/PersistedReactGridLayout/PersistedReactGridLayout';
import {useContext, useEffect, useState} from 'react';
import {
  class_x_management_service,
  pl_types,
  school_management,
  user_x_management,
} from 'pl-pb';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {
  filterAutocompleteFormField,
  useFormFields,
} from '../../../libs/form_utils/forms';
import {createService} from '../../../libs/protos';
import {MultiSchoolAutocomplete} from '../../../libs/common_fields/MultiSchoolAutocomplete';
import {FULL_USER_X_SORTER, SCHOOL_SORTER} from '../../../libs/sorters';
import {MultiClassXAutocomplete} from '../../../libs/common_fields/MultiClassXAutocomplete';
import {FilterAltTwoTone, SearchTwoTone} from '@mui/icons-material';
import {
  deepReadOnly,
  DeepReadOnly,
  getHighlightStyle,
  getUniqueHues,
} from '../../../libs/misc';
import {SelectUserXsAutocomplete} from '../../../libs/common_fields/SelectUserXsAutocomplete';
import {PostsFeed} from '../../../libs/PostsFeed/PostsFeed';
import {Chip} from '@mui/material';
import IClassX = pl_types.IClassX;
import SchoolManagementService = school_management.SchoolManagementService;
import ISchool = pl_types.ISchool;
import ClassXManagementService = class_x_management_service.ClassXManagementService;
import IFullUserXDetails = user_x_management.IFullUserXDetails;

export function OverviewTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be a teacher to view this overview.',
    userX => userX.isAdminX || userX.isTeacher
  );

  // Filter parameters.

  const filterForm = useFormFields();

  const [schoolOptions, setSchoolOptions] = useState<DeepReadOnly<ISchool[]>>(
    []
  );
  const schoolFilter = filterForm.useAutocompleteFormField<
    DeepReadOnly<ISchool[]>
  >('schoolFilter', {
    isAutocomplete: {
      isMultiple: true,
    },
    disabled: !userX,
  });

  const [classXOptions, setClassXOptions] = useState<readonly IClassX[]>([]);
  const classXFilter = filterForm.useAutocompleteFormField<readonly IClassX[]>(
    'classXFilter',
    {
      isAutocomplete: {
        isMultiple: true,
      },
    }
  );

  const [filteredUserXs, setFilteredUserXs] = useState<
    DeepReadOnly<IFullUserXDetails[]>
  >([]);

  // Highlight parameters.

  const [highlightedUserXs, internalSetHighlightedUserXs] = useState<
    DeepReadOnly<IFullUserXDetails[]>
  >([]);
  const [highlightedUserXHues, setHighlightedUserXHues] = useState(
    deepReadOnly(new Map<number, number>())
  );

  useEffect(() => {
    if (!userX) {
      setSchoolOptions([]);
      setClassXOptions([]);
      filterForm.setValuesObject({});
      setFilteredUserXs([]);
      setHighlightedUserXs([]);
      setHighlightedUserXHues(new Map());
      return;
    }

    createService(SchoolManagementService, 'SchoolManagementService')
      .getSchools({districtId: userX.districtId})
      .then(response => {
        const options = response.schools.sort(SCHOOL_SORTER);
        setSchoolOptions(options);
        filterAutocompleteFormField(schoolFilter, school => school.id, options);
      })
      .catch(global.setError);
  }, [userX]);

  // Maintain classX filters.

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
  }, [userX, schoolFilter.getValue()]);

  function setHighlightedUserXs(userXs: DeepReadOnly<IFullUserXDetails[]>) {
    const sortedUserXs = userXs.slice().sort(FULL_USER_X_SORTER);
    const newHighlightedUserXHues = getUniqueHues(
      highlightedUserXHues,
      sortedUserXs.map(userX => userX.userX?.id ?? 0)
    );
    internalSetHighlightedUserXs(sortedUserXs);
    setHighlightedUserXHues(newHighlightedUserXHues);
  }

  if (!userX) {
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
                      hasOptions ? 'Filter by Class' : 'Select Schools First'
                    }
                    InputLabelProps={{shrink: true}}
                  />
                  <SelectUserXsAutocomplete
                    label="Filter by Student"
                    baseRequest={{
                      studentsOnly: true,
                      inSchoolIds: schoolFilter
                        .getValue()
                        ?.map(school => school.id ?? 0),
                      inClassXIds: classXFilter
                        .getValue()
                        ?.map(classX => classX.id ?? 0),
                    }}
                    values={filteredUserXs}
                    onChange={setFilteredUserXs}
                  />
                </div>
              </TitledPaper>
            ),
            layout: {x: 8, y: 0, w: 4, h: 4},
          },
          {
            id: 'highlight_posts',
            panel: (
              <TitledPaper
                title="Highlight Posts"
                icon={<SearchTwoTone />}
                highlightColor="blue"
                draggableCursorType="move"
              >
                <div
                  className="global-flex-column"
                  style={{marginTop: '0.5em', gap: '1em'}}
                >
                  <SelectUserXsAutocomplete
                    label="Students to Highlight"
                    baseRequest={{
                      studentsOnly: true,
                      inSchoolIds: schoolFilter
                        .getValue()
                        ?.map(school => school.id ?? 0),
                      inClassXIds: classXFilter
                        .getValue()
                        ?.map(classX => classX.id ?? 0),
                    }}
                    values={highlightedUserXs}
                    onChange={setHighlightedUserXs}
                    renderTags={(options, getTagProps) => (
                      <>
                        {options.map(option => (
                          <Chip
                            {...getTagProps}
                            key={option.userX?.id ?? 0}
                            label={`${option.userX?.lastName ?? ''}, ${
                              option.userX?.firstName ?? ''
                            }`}
                            size="small"
                            onDelete={() => {
                              setHighlightedUserXs(
                                highlightedUserXs
                                  .slice()
                                  .filter(
                                    userX =>
                                      userX?.userX?.id !== option?.userX?.id
                                  )
                              );
                            }}
                            style={getHighlightStyle(
                              highlightedUserXHues.get(option.userX?.id ?? 0)
                            )}
                          />
                        ))}
                      </>
                    )}
                  />
                </div>
              </TitledPaper>
            ),
            layout: {x: 8, y: 4, w: 4, h: 8},
          },
          {
            id: 'posts',
            panel: (
              <PostsFeed
                request={{
                  schoolIds: schoolFilter.getValue()?.map(e => e.id ?? 0),
                  classXIds: classXFilter.getValue()?.map(e => e.id ?? 0),
                  userXIds: filteredUserXs.map(e => e.userX?.id ?? 0),
                  includeProjects: true,
                  includeComments: true,
                  includeTags: true,
                  includeRatings: true,
                  beingEdited: false,
                }}
                paged={true}
                postHighlights={{
                  getUserXHue: userX =>
                    highlightedUserXHues.get(userX?.id ?? 0),
                }}
              />
            ),
            layout: {x: 0, y: 0, w: 8, h: 12},
          },
        ]}
      />
    </>
  );
}
