import './StudentDashboard.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function ClassesTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be a student to view this dashboard.',
    userX => userX.isAdminX || userX.isStudent
  );

  if (!userX) {
    return <></>;
  }

  return <>TODO: Display class information.</>;
}
