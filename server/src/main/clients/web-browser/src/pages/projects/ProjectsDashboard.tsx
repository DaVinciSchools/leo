import './ProjectsDashboard.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../libs/GlobalStateProvider/GlobalStateProvider';
import {DefaultPage} from '../../libs/DefaultPage/DefaultPage';
import {TabbedPanel} from '../../libs/TabbedPanel/TabbedPanel';
import {OverviewTab} from './OverviewTab';
import {ProjectBuilderTab} from './ProjectBuilderTab';
import {AllProjectsTab} from './AllProjectsTab';

export enum ProjectsTab {
  OVERVIEW,
  PROJECT_BUILDER,
  ALL_PROJECTS,
}

export function ProjectsDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserXLogin(
    'You must be logged in to view this dashboard.'
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Projects">
        <div style={{height: '100%'}}>
          <TabbedPanel
            tabKeyEnum={ProjectsTab}
            defaultTabKey={ProjectsTab.OVERVIEW}
            tabs={[
              {
                key: ProjectsTab.OVERVIEW,
                label: 'Overview',
                content: <OverviewTab />,
              },
              {
                key: ProjectsTab.PROJECT_BUILDER,
                label: 'Builder',
                content: <ProjectBuilderTab />,
              },
              {
                key: ProjectsTab.ALL_PROJECTS,
                label: 'All Projects',
                content: <AllProjectsTab />,
              },
            ]}
          />
        </div>
      </DefaultPage>
    </>
  );
}
