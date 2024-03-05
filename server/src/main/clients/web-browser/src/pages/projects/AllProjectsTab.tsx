import {GlobalStateContext} from '../../libs/GlobalState';
import {Alert, Button, Dialog, Paper} from '@mui/material';
import {ProjectCard} from '../../libs/ProjectCard/ProjectCard';
import {ProjectPage} from '../../libs/ProjectPage/ProjectPage';
import {createService} from '../../libs/protos';
import {pl_types, project_management} from 'pl-pb';
import {useContext, useEffect, useState} from 'react';
import {styled} from 'styled-components';
import {
  PROJECT_DEFINITION_SORTER,
  REVERSE_DATE_THEN_PROJECT_SORTER,
} from '../../libs/sorters';
import IProject = pl_types.IProject;
import ProjectManagementService = project_management.ProjectManagementService;
import ThumbsState = pl_types.Project.ThumbsState;
import IProjectDefinition = pl_types.IProjectDefinition;
import State = pl_types.ProjectDefinition.State;

const StyledDialog = styled(Paper)`
  padding: ${props => props.theme.spacing(4)};
`;

const DialogActionBar = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
`;

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${props => props.theme.spacing(4)};
  margin: ${props => props.theme.spacing(4)} 0;
`;

export function AllProjectsTab() {
  const global = useContext(GlobalStateContext);
  const userX = global.useUserX(
    'You must be logged in to view this dashboard.'
  );

  const [projects, setProjects] = useState<readonly IProject[]>([]);
  const [unsuccessfulProjects, setUnsuccessfulProjects] = useState<
    readonly IProjectDefinition[]
  >([]);
  const [projectDetails, setProjectDetails] = useState<IProject>();
  const [aiResponse, setAiResponse] = useState<string | null | undefined>();

  useEffect(() => {
    if (userX == null) {
      setProjects([]);
      setUnsuccessfulProjects([]);
      return;
    }

    createService(ProjectManagementService, 'ProjectManagementService')
      .getProjects({
        userXIds: [userX.id ?? 0],
        includeAssignment: true,
        includeInactive: true,
      })
      .then(response => {
        setProjects(response.projects.sort(REVERSE_DATE_THEN_PROJECT_SORTER));
      })
      .catch(global.setError);

    createService(ProjectManagementService, 'ProjectManagementService')
      .getProjectInputs({
        userXIds: [userX.id ?? 0],
        includeProcessing: true,
      })
      .then(response => {
        setUnsuccessfulProjects(
          (response.projects ?? []).sort(PROJECT_DEFINITION_SORTER)
        );
      })
      .catch(global.setError);
  }, [userX]);

  function updateProject(project: IProject, modifications: IProject) {
    Object.assign(project, modifications);

    createService(ProjectManagementService, 'ProjectManagementService')
      .updateProject({project})
      .catch(global.setError);

    setProjects([...projects]);
  }

  function showProjectDetails(project: pl_types.IProject) {
    createService(ProjectManagementService, 'ProjectManagementService')
      .getProjects({
        projectIds: [project.id ?? 0],
        includeInactive: true,
        includeMilestones: true,
      })
      .then(response => setProjectDetails(response.projects[0]!))
      .catch(reason => {
        global.setError({error: reason, reload: false});
      });
  }

  function showAiResponse(project: pl_types.IProject) {
    setAiResponse(project.projectDefinition?.aiResponse);
  }

  if (!userX) {
    return <></>;
  }

  return (
    <>
      <Wrapper>
        {unsuccessfulProjects
          .filter(e => e.state === State.FAILED)
          .map(definition => (
            <Alert key={definition.id} color="error">
              Generating projects for this Ikigai configuration failed. If the
              problem persists, please contact the administrator.
            </Alert>
          ))}
        {unsuccessfulProjects
          .filter(e => e.state === State.PROCESSING)
          .map(definition => (
            <Alert key={definition.id} severity="info">
              Projects are still being generated for this Ikigai configuration.
              Please refresh the page to check for updates.
            </Alert>
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
            thumbsState={project.thumbsState ?? ThumbsState.UNSET_THUMBS_STATE}
            thumbsStateReason={project.thumbsStateReason ?? ''}
            showDetails={() => showProjectDetails(project)}
            updateProject={modifications =>
              updateProject(project, modifications)
            }
            showAiPrompt={() => {
              showAiResponse(project);
            }}
          />
        ))}
      </Wrapper>
      <Dialog
        open={projectDetails != null}
        onClose={() => setProjectDetails(undefined)}
        PaperComponent={StyledDialog}
        maxWidth="lg"
      >
        {projectDetails != null && (
          <>
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
            <DialogActionBar>
              <Button
                variant="contained"
                onClick={() => setProjectDetails(undefined)}
              >
                Ok
              </Button>
            </DialogActionBar>
          </>
        )}
      </Dialog>
      <Dialog
        open={!!aiResponse}
        onClose={() => setAiResponse(null)}
        PaperComponent={StyledDialog}
      >
        {aiResponse}
      </Dialog>
    </>
  );
}
