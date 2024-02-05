import './TeacherDashboard.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function StudentsTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be a teacher to view this dashboard.',
    userX => userX.isAdminX || userX.isTeacher
  );

  if (!userX) {
    return <></>;
  }

  return <>TODO: Create a navigable view of student details.</>;
}
