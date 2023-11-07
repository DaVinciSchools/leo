import './Overview.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {useContext} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function Overview() {
  const global = useContext(GlobalStateContext);
  const userX = global.requireUserX(
    'You must be signed in to view the overview page.'
  );

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Projects" />
    </>
  );
}
