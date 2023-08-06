import './Overview.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {sendToLogin} from '../../../libs/authentication';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function Overview() {
  const global = useContext(GlobalStateContext);
  if (!global.user) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage title="Projects" />
    </>
  );
}
