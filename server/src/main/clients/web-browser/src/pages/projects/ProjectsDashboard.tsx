import './ProjectsDashboard.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../libs/GlobalState';
import {DefaultPage} from '../../libs/DefaultPage/DefaultPage';
import {TabbedPanel} from '../../libs/TabbedPanel/TabbedPanel';
import {ProjectBuilderTab} from './ProjectBuilderTab';
import {AllProjectsTab} from './AllProjectsTab';

export enum ProjectsTab {
  // OVERVIEW,
  PROJECT_BUILDER,
  ALL_PROJECTS,
}

export function ProjectsDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be logged in to view this dashboard.'
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Projects">
        <TabbedPanel
          tabKeyEnum={ProjectsTab}
          defaultTabKey={ProjectsTab.PROJECT_BUILDER}
          tabs={[
            // {
            //   key: ProjectsTab.OVERVIEW,
            //   label: 'Overview',
            //   content: <OverviewTab />,
            // },
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
      </DefaultPage>
    </>
  );
}
