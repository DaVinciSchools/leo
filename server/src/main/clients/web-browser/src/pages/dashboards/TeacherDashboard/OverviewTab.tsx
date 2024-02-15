import './TeacherDashboard.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalStateProvider/GlobalStateProvider';

export function OverviewTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserXLogin(
    'You must be a teacher to view this dashboard.',
    userX => userX.isAdminX || userX.isTeacher
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
