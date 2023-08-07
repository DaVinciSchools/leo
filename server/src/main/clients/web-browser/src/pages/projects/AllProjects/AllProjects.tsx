import './AllProjects.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {Modal} from 'antd';
import {ProjectCard} from '../../../libs/ProjectCard/ProjectCard';
import {ProjectPage} from '../../../libs/ProjectPage/ProjectPage';
import {createService} from '../../../libs/protos';
import {pl_types, project_management} from '../../../generated/protobuf-js';
import {sendToLogin} from '../../../libs/authentication';
import {useContext, useEffect, useState} from 'react';

import IProject = pl_types.IProject;
import ProjectManagementService = project_management.ProjectManagementService;
import ThumbsState = pl_types.Project.ThumbsState;

export function AllProjects() {
  const global = useContext(GlobalStateContext);
  if (!global.user == null) {
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
      .getProjects({userXId: global.user!.userXId})
      .then(response => {
        response.projects.sort((a, b) => (b.id ?? 0) - (a.id ?? 0));
        setProjects(response.projects);
      })
      .catch(global.setError);
  }, []);

  function updateProject(project: IProject, modifications: IProject) {
    service
      .updateProject({id: project.id!, modifications: modifications})
      .catch(global.setError);

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
        global.setError({error: reason, reload: false});
      });
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
