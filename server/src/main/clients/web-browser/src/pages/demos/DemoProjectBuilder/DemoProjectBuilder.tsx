import './DemoProjectBuilder.scss';

import {Link} from 'react-router-dom';
import {ProjectBuilder} from '../../../libs/ProjectBuilder/ProjectBuilder';

export function DemoProjectBuilder() {
  return (
    <>
      <div className="demo-project-builder">
        <div className="demo-project-builder-project-title">
          <Link to="/">
            <img
              src="/images/logo-orange-on-white.svg"
              alt="Project Leo Logo | Go Home"
              style={{maxHeight: '33px'}}
            />
          </Link>
          <span>Create New Projects</span>
        </div>
        <div className="demo-project-builder-project-builder">
          <ProjectBuilder noCategoriesText={'Select categories on the left'} />
        </div>
      </div>
    </>
  );
}
