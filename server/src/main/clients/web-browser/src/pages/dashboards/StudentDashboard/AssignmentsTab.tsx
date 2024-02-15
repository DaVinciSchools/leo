import './StudentDashboard.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalStateProvider/GlobalStateProvider';

export function AssignmentsTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserXLogin(
    'You must be a student to view this dashboard.',
    userX => userX.isAdminX || userX.isStudent
  );

  if (!userX) {
    return <></>;
  }

  return <>TODO: Display assignment information.</>;
}
