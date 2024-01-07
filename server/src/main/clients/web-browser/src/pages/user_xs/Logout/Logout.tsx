import '../Login.scss';
import {logout} from '../../../libs/authentication';
import {useContext, useEffect} from 'react';
import {Link} from 'react-router-dom';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {useNavigate} from 'react-router';

export function Logout() {
  const global = useContext(GlobalStateContext);
  const navigate = useNavigate();

  useEffect(() => {
    logout(global).finally(() => navigate('/'));
  }, []);

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
    </>
  );
}
