import './ClassXManagement.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../libs/GlobalState';

export function OverviewTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be a teacher to administer classes.',
    userX => userX.isAdminX || userX.isTeacher
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      TODO: Create a summary overview of classes and assignments for the purpose
      of editing them.
    </>
  );
}
