import './AllProjects.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {Modal} from 'antd';
import {ProjectCard} from '../../../libs/ProjectCard/ProjectCard';
import {ProjectPage} from '../../../libs/ProjectPage/ProjectPage';
import {createService} from '../../../libs/protos';
import {pl_types, project_management} from '../../../generated/protobuf-js';
import {useContext, useEffect, useState} from 'react';
import {
  PROJECT_DEFINITION_SORTER,
  REVERSE_DATE_THEN_PROJECT_SORTER,
} from '../../../libs/sorters';
import {TitledPaper} from '../../../libs/TitledPaper/TitledPaper';
import IProject = pl_types.IProject;
import ProjectManagementService = project_management.ProjectManagementService;
import ThumbsState = pl_types.Project.ThumbsState;
import IProjectDefinition = pl_types.IProjectDefinition;
import State = pl_types.ProjectDefinition.State;

export function AllProjects() {
  const global = useContext(GlobalStateContext);

  const [projects, setProjects] = useState<IProject[]>([]);
  const [unsuccessfulProjects, setUnsuccessfulProjects] = useState<
    IProjectDefinition[]
  >([]);

  const [projectDetails, setProjectDetails] = useState<IProject | undefined>();

  useEffect(() => {
    if (global.user != null) {
      createService(ProjectManagementService, 'ProjectManagementService')
        .getProjects({userXId: global.user!.userXId, includeUnsuccessful: true})
        .then(response => {
          setProjects(response.projects.sort(REVERSE_DATE_THEN_PROJECT_SORTER));
          setUnsuccessfulProjects(
            (response.unsuccessfulInputs ?? []).sort(PROJECT_DEFINITION_SORTER)
          );
        })
        .catch(global.setError);
    } else {
      setProjects([]);
      setUnsuccessfulProjects([]);
    }
  }, [global.user]);

  if (!global.requireUser(user => user?.isAuthenticated)) {
    return <></>;
  }

  function updateProject(project: IProject, modifications: IProject) {
    createService(ProjectManagementService, 'ProjectManagementService')
      .updateProject({id: project.id!, modifications: modifications})
      .catch(global.setError);

    const newProjects = [...projects];
    Object.assign(project, modifications);
    setProjects(newProjects);
  }

  function showProjectDetails(project: pl_types.IProject) {
    createService(ProjectManagementService, 'ProjectManagementService')
      .getProjectDetails({projectId: project.id})
      .then(response => setProjectDetails(response.project!))
      .catch(reason => {
        global.setError({error: reason, reload: false});
      });
  }

  return (
    <>
      <DefaultPage title="All Projects">
        <div>
          {unsuccessfulProjects
            .filter(e => e.state === State.FAILED)
            .map(definition => (
              <TitledPaper
                title="Project Generation Failed"
                highlightColor="red"
                key={definition.id}
              >
                Generating projects for this Ikigai configuration failed. If the
                problem persists, please contact the administrator.
              </TitledPaper>
            ))}
          {unsuccessfulProjects
            .filter(e => e.state === State.PROCESSING)
            .map(definition => (
              <TitledPaper
                title="Project Generation In Progress"
                highlightColor="lightgreen"
                key={definition.id}
              >
                Projects are still being generated for this Ikigai
                configuration. Please refresh the page to check for updates.
              </TitledPaper>
            ))}
          {projects.map(project => (
            <ProjectCard
              id={project.id!}
              key={project.id!}
              name={project.name ?? 'undefined'}
              shortDescr={project.shortDescr ?? 'undefined'}
              longDescrHtml={project.longDescrHtml ?? 'undefined'}
              active={project.active ?? false}
              favorite={project.favorite ?? false}
              thumbsState={project.thumbsState ?? ThumbsState.UNSET}
              thumbsStateReason={project.thumbsStateReason ?? ''}
              showDetails={() => showProjectDetails(project)}
              updateProject={modifications =>
                updateProject(project, modifications)
              }
            />
          ))}
        </div>
        <Modal
          open={projectDetails != null}
          onOk={() => setProjectDetails(undefined)}
          onCancel={() => setProjectDetails(undefined)}
          cancelButtonProps={{style: {display: 'none'}}}
          centered
          style={{margin: '5%', minWidth: '60%'}}
        >
          {projectDetails != null && (
            <ProjectPage
              id={projectDetails.id!}
              key={projectDetails.id!}
              name={projectDetails.name ?? 'undefined'}
              shortDescr={projectDetails.shortDescr ?? 'undefined'}
              longDescrHtml={projectDetails.longDescrHtml ?? 'undefined'}
              milestones={projectDetails.milestones ?? []}
              updateProject={modifications =>
                updateProject(projectDetails, modifications)
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
