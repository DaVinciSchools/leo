import './DemoProjectBuilder.scss';

import {ProjectBuilder} from '../../../libs/ProjectBuilder/ProjectBuilder';

export function DemoProjectBuilder() {
  return (
    <>
      <ProjectBuilder
        demo={true}
        noCategoriesText={'Select categories on the left'}
      />
    </>
  );
}
