import './AdminXDashboard.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function AdminXDashboard() {
  const global = useContext(GlobalStateContext);
  if (!global.requireUserX(userX => userX?.isAdminX)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Admin Dashboard">
        <ul>
          <li>
            <a
              href="https://analytics.google.com/analytics/web/?utm_source=marketingplatform.google.com&utm_medium=et&utm_campaign=marketingplatform.google.com%2Fabout%2Fanalytics%2F#/p398130888/reports/intelligenthome"
              target="_blank"
            >
              Google Analytics
            </a>
          </li>
          <li>
            <a
              href="https://trends.google.com/trends/explore?q=project%20leo&date=now%201-d&geo=US&hl=en"
              target="_blank"
            >
              Google Trends
            </a>
          </li>
        </ul>
      </DefaultPage>
    </>
  );
}
