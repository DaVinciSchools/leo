import '../../../libs/global.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {DeepReadOnly} from '../../../libs/misc';
import {PostsFeed} from '../../../libs/PostsFeed/PostsFeed';
import {Grid} from '@mui/material';
import {EducationFilters} from '../../../libs/EducationFilter/EducationFilters';

export function PostsTab(
  props: DeepReadOnly<{educationFilters: EducationFilters}>
) {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be a student to view posts.',
    userX => userX.isAdminX || userX.isStudent
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <Grid container spacing={2} padding={2}>
        <Grid item xs={3}>
          <EducationFilters
            label="Filter Posts"
            highlightLabel="Highlight Posts"
            educationFilters={props.educationFilters}
          />
        </Grid>
        <Grid item xs={9}>
          <PostsFeed
            request={{
              schoolIds: props.educationFilters.schoolsFilter
                ?.getValue?.()
                ?.map(e => e.id ?? 0),
              classXIds: props.educationFilters.classXsFilter
                ?.getValue?.()
                ?.map(e => e.id ?? 0),
              userXIds: props.educationFilters.userXsFilter
                ?.getValue?.()
                ?.map(e => e.userX?.id ?? 0),
              includeProjects: true,
              includeComments: true,
              includeTags: true,
              beingEdited: false,
            }}
            paged
            getUserXHighlightStyle={
              props.educationFilters.getUserXHighlightStyle
            }
          />
        </Grid>
      </Grid>
    </>
  );
}
