import './MyAccount.scss';
import {SignupForm} from '../../../libs/SignupForm/SignupForm';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';

export function MyAccount() {
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  return (
    <>
      <DefaultPage title="My Account">
        <SignupForm />
      </DefaultPage>
    </>
  );
}
