import {Link} from 'react-router-dom';
import './Layout.scss';

function Card({
  title,
  imageSrc,
  description,
  path,
}: {
  title: string;
  imageSrc: string;
  description: string | string[];
  path: string;
}) {
  return (
    <div className="card">
      <img src={imageSrc} alt={title} />
      <h3>
        <Link to={path}>{title}</Link>
      </h3>
      {(typeof description === 'string' ? [description] : description).map(
        e => (
          <p>{e}</p>
        )
      )}
    </div>
  );
}

function classWrapper(className: string) {
  return ({children}: {children: JSX.Element | JSX.Element[] | null}) => {
    return <div className={className}>{children}</div>;
  };
}

const HeaderSidebar = classWrapper('layout-header-sidebar');

// function Header();

export {Card, HeaderSidebar};
