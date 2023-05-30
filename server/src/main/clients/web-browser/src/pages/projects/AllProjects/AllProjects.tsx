import './AllProjects.scss';
import {useEffect, useState} from 'react';
import {
  createService,
  pl_types,
  project_management,
} from '../../../libs/protos';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {ProjectCard} from '../../../libs/ProjectCard/ProjectCard';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import IProject = pl_types.IProject;
import ProjectManagementService = project_management.ProjectManagementService;
import ThumbsState = pl_types.Project.ThumbsState;
import {Modal} from 'antd';

export function AllProjects() {
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const [projects, setProjects] = useState<IProject[]>([]);
  const [detailedProject, setDetailedProject] = useState<
    IProject | undefined
  >();

  const service = createService(
    ProjectManagementService,
    'ProjectManagementService'
  );

  useEffect(() => {
    service.getProjects({userXId: user!.id}).then(response => {
      setProjects(response.projects);
    });
  }, []);

  function updateProject(project: IProject, modifications: IProject) {
    service.updateProject({id: project.id!, modifications: modifications});

    const newProjects = [...projects];
    Object.assign(project, modifications);
    setProjects(newProjects);
  }

  function showModal(project: pl_types.IProject) {
    setDetailedProject(project);
  }

  return (
    <>
      <DefaultPage title="All Projects">
        <div>
          {projects.map(project => (
            <ProjectCard
              id={project.id!}
              key={project.id!}
              name={project.name!}
              shortDescr={project.shortDescr!}
              longDescr={project.longDescr!}
              active={project.active ?? false}
              favorite={project.favorite ?? false}
              thumbsState={project.thumbsState ?? ThumbsState.UNSET}
              showDetails={() => showModal(project)}
              updateProject={modifications =>
                updateProject(project, modifications)
              }
            />
          ))}
        </div>
        <Modal
          open={detailedProject != null}
          onOk={() => setDetailedProject(undefined)}
          onCancel={() => setDetailedProject(undefined)}
        >
          TODO
        </Modal>
      </DefaultPage>
    </>
  );
}
