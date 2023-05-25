import './Overview.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';

export function Overview() {
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage title="Projects" />
    </>
  );
}
