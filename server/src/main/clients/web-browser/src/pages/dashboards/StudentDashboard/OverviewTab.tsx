import './StudentDashboard.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function OverviewTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be a student to view this dashboard.',
    userX => userX.isAdminX || userX.isStudent
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      TODO: Create a summary of classes and assignment statuses. Include
      upcoming deadlines with overall completion.
    </>
  );
}
