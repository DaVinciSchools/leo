import './Logout.scss';
import {addXsrfInputField} from '../../../libs/authentication';
import {useContext, useEffect, useRef} from 'react';
import {Link} from 'react-router-dom';
import {GlobalStateContext} from '../../../libs/GlobalState';

export function Logout() {
  const global = useContext(GlobalStateContext);

  const formRef = useRef<HTMLFormElement>(null);

  useEffect(() => {
    if (formRef.current != null) {
      global.setUser(undefined);
      formRef.current.requestSubmit();
    }
  }, [formRef.current]);

  return (
    <>
      <div className="space-filler" />
      <div className="logo">
        <Link to="/">
          <img src="/images/logo-orange-on-white.svg" />
        </Link>
      </div>
      <div className="space-filler" />
      <div className="logout">Logging out...</div>
      <form action="/api/logout.html" method="POST" ref={formRef}>
        {addXsrfInputField()}
      </form>
    </>
  );
}
