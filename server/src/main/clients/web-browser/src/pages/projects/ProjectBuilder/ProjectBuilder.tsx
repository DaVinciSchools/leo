import './ProjectBuilder.scss';

import {useContext} from 'react';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {ProjectBuilder as MainProjectBuilder} from '../../../libs/ProjectBuilder/ProjectBuilder';

export function ProjectBuilder() {
  const global = useContext(GlobalStateContext);
  if (!global.requireUser(user => user?.isAuthenticated)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Project Builder" bodyStyle={{paddingTop: 0}}>
        <MainProjectBuilder noCategoriesText="There are no categories for the project builder." />
      </DefaultPage>
    </>
  );
}
