import './Logout.scss';
import {addXsrfInputField, logout} from '../../../libs/authentication';
import {useEffect, useRef} from 'react';
import {Link} from 'react-router-dom';

export function Logout() {
  const formRef = useRef<HTMLFormElement>(null);

  useEffect(() => {
    if (formRef.current != null) {
      logout();
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
      <form action="/api/logout" method="POST" ref={formRef}>
        {addXsrfInputField()}
      </form>
    </>
  );
}
