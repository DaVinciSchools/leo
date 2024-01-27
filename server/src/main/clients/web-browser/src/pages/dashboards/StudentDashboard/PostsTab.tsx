import '../../../libs/global.scss';
import './StudentDashboard.scss';
import {useContext, useEffect, useState} from 'react';
import {pl_types, user_x_management} from 'pl-pb';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {useFormFields} from '../../../libs/form_utils/forms';
import {FULL_USER_X_SORTER} from '../../../libs/sorters';
import {deepReadOnly, DeepReadOnly, getUniqueHues} from '../../../libs/misc';
import {PostsFeed} from '../../../libs/PostsFeed/PostsFeed';
import {Grid} from '@mui/material';
import {EducationFilters} from '../../../libs/EducationFilter/EducationFilters';
import IClassX = pl_types.IClassX;
import ISchool = pl_types.ISchool;
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import IAssignment = pl_types.IAssignment;

export function PostsTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be a teacher to view posts.',
    userX => userX.isAdminX || userX.isStudent
  );

  const filterForm = useFormFields();

  const schoolFilter = filterForm.useMultipleAutocompleteFormField<
    DeepReadOnly<ISchool>
  >('schoolFilter', {disabled: !userX});
  const classXFilter = filterForm.useMultipleAutocompleteFormField<
    DeepReadOnly<IClassX>
  >('classXFilter', {disabled: !schoolFilter.getValue()});
  const assignmentFilter = filterForm.useMultipleAutocompleteFormField<
    DeepReadOnly<IAssignment>
  >('assignmentFilter', {disabled: !schoolFilter.getValue()});
  const userXFilter = filterForm.useMultipleAutocompleteFormField<
    DeepReadOnly<IFullUserXDetails>
  >('userXFilter', {disabled: !schoolFilter.getValue()});

  const highlightedUserXs = filterForm.useMultipleAutocompleteFormField<
    DeepReadOnly<IFullUserXDetails>
  >('highlightUserXs', {
    onChange: formField => {
      internal_setHighlightedUserXHues(
        getUniqueHues(
          highlightedUserXHues,
          formField
            .getValue()
            .slice()
            .sort(FULL_USER_X_SORTER)
            .map(userX => userX.userX?.id ?? 0)
        )
      );
    },
    disabled: !schoolFilter.getValue(),
  });
  const [highlightedUserXHues, internal_setHighlightedUserXHues] = useState(
    deepReadOnly(new Map<number, number>())
  );

  useEffect(() => {
    if (!userX) {
      filterForm.setValuesObject({});
      internal_setHighlightedUserXHues(new Map());
      return;
    }
  }, [userX]);

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <Grid container spacing={2} padding={2}>
        <Grid item xs={3}>
          <EducationFilters
            label="Filter Posts"
            classXsField={classXFilter}
            assignmentsField={assignmentFilter}
            userXsField={userXFilter}
            highlightLabel="Highlight Posts"
            highlightUserXsField={highlightedUserXs}
            renderUserXsStyle={userX =>
              highlightedUserXHues.has(userX.userX?.id ?? 0)
                ? {
                    backgroundColor: `hsl(${
                      highlightedUserXHues.get(userX.userX?.id ?? 0) ?? 0
                    }, 100%, 80%)`,
                  }
                : {}
            }
          />
        </Grid>
        <Grid item xs={9}>
          <PostsFeed
            request={{
              schoolIds: schoolFilter.getValue()?.map(e => e.id ?? 0),
              classXIds: classXFilter.getValue()?.map(e => e.id ?? 0),
              userXIds: userXFilter.getValue().map(e => e.userX?.id ?? 0),
              includeProjects: true,
              includeComments: true,
              includeTags: true,
              beingEdited: false,
            }}
            paged
            postHighlights={{
              getUserXHue: userX => highlightedUserXHues.get(userX?.id ?? 0),
            }}
          />
        </Grid>
      </Grid>
    </>
  );
}
