import './ProjectsDashboard.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../libs/GlobalState';
import {DefaultPage} from '../../libs/DefaultPage/DefaultPage';
import {TabbedSwiper} from '../../libs/TabbedSwiper/TabbedSwiper';
import {OverviewTab} from './OverviewTab';
import {ProjectBuilderTab} from './ProjectBuilderTab';
import {AllProjectsTab} from './AllProjectsTab';

enum TabValue {
  OVERVIEW,
  PROJECT_BUILDER,
  ALL_PROJECTS,
}

export function ProjectsDashboard() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be logged in to view this dashboard.'
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Projects">
        <div style={{height: '100%'}}>
          <TabbedSwiper
            tabs={[
              {
                key: TabValue.OVERVIEW,
                label: 'Overview',
                content: <OverviewTab />,
              },
              {
                key: TabValue.PROJECT_BUILDER,
                label: 'Builder',
                content: <ProjectBuilderTab />,
              },
              {
                key: TabValue.ALL_PROJECTS,
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
