import './ClassXManagement.scss';
import {useContext} from 'react';
import {GlobalStateContext} from '../../libs/GlobalStateProvider/GlobalStateProvider';

export function OverviewTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserXLogin(
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
