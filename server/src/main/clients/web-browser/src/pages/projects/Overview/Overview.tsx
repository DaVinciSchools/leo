import './Overview.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function Overview() {
  const global = useContext(GlobalStateContext);
  if (!global.requireUserX(userX => userX?.isAuthenticated)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Projects" />
    </>
  );
}
