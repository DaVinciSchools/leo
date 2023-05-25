import './MyProjects.scss';
import {useEffect, useState} from 'react';
import {
  createService,
  pl_types,
  project_management,
} from '../../../libs/protos';
import IProject = pl_types.IProject;
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {ProjectCard} from '../../../libs/ProjectCard/ProjectCard';
import ProjectManagementService = project_management.ProjectManagementService;
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';

export function MyProjects() {
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const [projects, setProjects] = useState<IProject[]>([]);

  useEffect(() => {
    const service = createService(
      ProjectManagementService,
      'ProjectManagementService'
    );
    service.getProjects({}).then(response => setProjects(response.projects));
  }, []);

  return (
    <>
      <DefaultPage title="My Projects">
        <div>
          {projects.map(project => (
            <ProjectCard key={project.id} project={project} />
          ))}
        </div>
      </DefaultPage>
    </>
  );
}
