import {useContext} from 'react';
import {GlobalStateContext} from '../../libs/GlobalStateProvider/GlobalStateProvider';

export function OverviewTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserXLogin(
    'You must be logged in to view this dashboard.'
  );

  if (!userX) {
    return <></>;
  }

  return <>TODO: Place an overview of project status here.</>;
}
