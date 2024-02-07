import {useContext} from 'react';
import {GlobalStateContext} from '../../libs/GlobalState';
import {ProjectBuilder} from '../../libs/ProjectBuilder/ProjectBuilder';

export function ProjectBuilderTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be logged in to view this dashboard.'
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <ProjectBuilder noCategoriesText="There are no categories for the project builder." />
    </>
  );
}
