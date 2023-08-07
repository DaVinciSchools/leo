import './Overview.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function Overview() {
  const global = useContext(GlobalStateContext);
  if (!global.requireUser(user => user?.isAuthenticated)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Projects" />
    </>
  );
}
