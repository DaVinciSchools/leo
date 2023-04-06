import {Link} from 'react-router-dom';
import './Nav.scss';
import {Outlet} from 'react-router';

export function Nav() {
  return (
    <>
      <header>
        <Link
          to="/"
          className="header-section header-section-left header-title nav-link"
        >
          <img src="/logo-white-orange.svg" />
          <div id="site-title">PROJECT LEO</div>
        </Link>
        <div className="header-section header-section-center">
          <Link to="" className="nav-link">
            About
          </Link>
          <Link to="" className="nav-link">
            Our Mission
          </Link>
          <Link to="" className="nav-link">
            Projects
          </Link>
          <Link to="" className="nav-link">
            Blog
          </Link>
        </div>
        <div className="header-section header-section-right">
          <Link to="">
            <button className="light">Login</button>
          </Link>
          <Link to="">
            <button className="primary">Sign up</button>
          </Link>
        </div>
      </header>
      <main>
        <Outlet />
      </main>
      <footer>&nbsp;</footer>
    </>
  );
}
