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
import {ProjectPage} from '../../../libs/ProjectPage/ProjectPage';
import {
  HandleError,
  HandleErrorType,
} from '../../../libs/HandleError/HandleError';

export function AllProjects() {
  const [handleError, setHandleError] = useState<HandleErrorType>();
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const [projects, setProjects] = useState<IProject[]>([]);
  const [showProjectDetails, setShowProjectDetails] = useState(false);
  const [projectDetails, setProjectDetails] = useState<IProject | undefined>();

  const service = createService(
    ProjectManagementService,
    'ProjectManagementService'
  );

  useEffect(() => {
    service
      .getProjects({userXId: user!.userXId})
      .then(response => {
        response.projects.sort((a, b) => (b.id ?? 0) - (a.id ?? 0));
        setProjects(response.projects);
      })
      .catch(setHandleError);
  }, []);

  function updateProject(project: IProject, modifications: IProject) {
    service
      .updateProject({id: project.id!, modifications: modifications})
      .catch(setHandleError);

    const newProjects = [...projects];
    Object.assign(project, modifications);
    setProjects(newProjects);
  }

  function showModal(project: pl_types.IProject) {
    setProjectDetails(undefined);
    setShowProjectDetails(true);
    service
      .getProjectDetails({projectId: project.id})
      .then(response => setProjectDetails(response.project!))
      .catch(reason => {
        setShowProjectDetails(false);
        setHandleError({error: reason, reload: false});
      });
  }

  return (
    <>
      <HandleError error={handleError} setError={setHandleError} />
      <DefaultPage title="All Projects">
        <div>
          {projects.map(project => (
            <ProjectCard
              id={project.id!}
              key={project.id!}
              name={project.name!}
              shortDescr={project.shortDescr!}
              longDescrHtml={project.longDescrHtml!}
              active={project.active ?? false}
              favorite={project.favorite ?? false}
              thumbsState={project.thumbsState ?? ThumbsState.UNSET}
              thumbsStateReason={project.thumbsStateReason ?? ''}
              showDetails={() => showModal(project)}
              updateProject={modifications =>
                updateProject(project, modifications)
              }
            />
          ))}
        </div>
        <Modal
          open={showProjectDetails}
          onOk={() => setShowProjectDetails(false)}
          onCancel={() => setShowProjectDetails(false)}
          cancelButtonProps={{style: {display: 'none'}}}
          centered
          style={{margin: '5%', minWidth: '60%'}}
        >
          {projectDetails != null && (
            <ProjectPage
              id={projectDetails!.id!}
              key={projectDetails!.id!}
              name={projectDetails!.name!}
              shortDescr={projectDetails!.shortDescr!}
              longDescrHtml={projectDetails!.longDescrHtml!}
              milestones={projectDetails!.milestones!}
              updateProject={modifications =>
                updateProject(projectDetails!, modifications)
              }
              onDeletePost={() => {}}
              onSubmitPost={() => {}}
              posts={[]}
              editable={false}
            />
          )}
        </Modal>
      </DefaultPage>
    </>
  );
}
