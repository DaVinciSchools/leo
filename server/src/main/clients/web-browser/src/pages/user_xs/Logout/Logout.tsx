import '../Login.scss';
import {GlobalStateContext} from '../../../libs/GlobalStateProvider/GlobalStateProvider';
import {useContext, useEffect} from 'react';
import {useNavigate} from 'react-router';

export function Logout() {
  const global = useContext(GlobalStateContext);
  const navigate = useNavigate();

  useEffect(() => {
    global.logout().finally(() => navigate('/'));
  }, []);

  return (
    <>
      <div className="space-filler" />
      <div className="logo">
        <img src="/images/logo-orange-on-white.svg" />
      </div>
      <div className="space-filler" />
      <div className="logout">Logging out...</div>
    </>
  );
}
