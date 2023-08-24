import './MyProjects.scss';
import '../../../global.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {createService} from '../../../libs/protos';
import {
  assignment_management,
  pl_types,
  project_management,
} from '../../../generated/protobuf-js';
import {useContext, useEffect, useState} from 'react';

import IProject = pl_types.IProject;
import ProjectManagementService = project_management.ProjectManagementService;
import {useDelayedAction} from '../../../libs/delayed_action';
import {replaceInPlace} from '../../../libs/misc';
import {ASSIGNMENT_SORTER, PROJECT_SORTER} from '../../../libs/sorters';
import {useFormFields} from '../../../libs/forms';
import {ProjectsAutocomplete} from '../../../libs/common_fields/ProjectsAutocomplete';
import IAssignment = pl_types.IAssignment;
import AssignmentManagementService = assignment_management.AssignmentManagementService;
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {ProjectEditor} from '../../../libs/ProjectEditor/ProjectEditor';

enum TabValue {
  OVERVIEW,
  EDIT_PROJECT,
}

export function MyProjects() {
  const global = useContext(GlobalStateContext);

  const [sortedProjects, setSortedProjects] = useState<IProject[]>([]);
  const [selectedProject, setSelectedProject] = useState<IProject | null>(null);
  const [sortedAssignments, setSortedAssignments] = useState<IAssignment[]>([]);

  const projectForm = useFormFields({
    onChange: () => autoSave.trigger(),
    disabled: selectedProject == null,
  });
  const [projectSaveStatus, setProjectSaveStatus] = useState<string>('');

  const hiddenForm = useFormFields({
    onChange: () => {
      setSelectedProject(hiddenProject.getValue() ?? null);
    },
  });
  const hiddenProject = hiddenForm.useAutocompleteFormField<IProject | null>(
    'project'
  );

  const autoSave = useDelayedAction(
    () => {
      setProjectSaveStatus('Modified');
      if (selectedProject != null) {
        const newProject = projectForm.getValuesObject(true, selectedProject);
        setSortedProjects(
          replaceInPlace([...sortedProjects], newProject, e => e?.id).sort(
            PROJECT_SORTER
          )
        );
      }
    },
    () => {
      setProjectSaveStatus('Saving...');
      if (selectedProject != null && projectForm.verifyOk(true)) {
        return createService(
          ProjectManagementService,
          'ProjectManagementService'
        )
          .updateProject({
            project: projectForm.getValuesObject(true, selectedProject),
          })
          .then(() => {
            setProjectSaveStatus('Saved');
          })
          .catch(global.setError);
      } else {
        setProjectSaveStatus('Invalid values, Not saved');
      }
      return;
    },
    1500
  );

  useEffect(() => {
    createService(ProjectManagementService, 'ProjectManagementService')
      .getProjects({userXId: global.userX?.userXId, activeOnly: true})
      .then(response => {
        setSortedProjects(response.projects.sort(PROJECT_SORTER));
        setSelectedProject(null);
      })
      .catch(global.setError);
    createService(AssignmentManagementService, 'AssignmentManagementService')
      .getAssignments({
        teacherId: global.userX?.teacherId,
        studentId: global.userX?.studentId,
      })
      .then(response => {
        setSortedAssignments(response.assignments.sort(ASSIGNMENT_SORTER));
      });
  }, [global.userX]);

  useEffect(() => {
    projectForm.setValuesObject(selectedProject ?? {});
  }, [selectedProject]);

  if (!global.requireUserX(userX => userX?.isAuthenticated)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="My Projects">
        <div
          className="global-flex-row"
          style={{gap: '1em', alignItems: 'center', justifyContent: 'stretch'}}
        >
          <span style={{fontWeight: 'bold', whiteSpace: 'nowrap'}}>
            Select Project
          </span>
          <ProjectsAutocomplete
            sortedProjects={sortedProjects}
            formField={hiddenProject}
            style={{width: '100%'}}
          />
          <span style={{whiteSpace: 'nowrap'}}>{projectSaveStatus}</span>
        </div>
        <div style={{height: '100%'}}>
          <TabbedSwiper
            tabs={[
              {
                key: TabValue.OVERVIEW,
                label: 'Overview',
                content: (
                  <>
                    <div className="global-flex-column">TODO</div>
                  </>
                ),
              },
              {
                key: TabValue.EDIT_PROJECT,
                label: 'Edit Project',
                content: (
                  <>
                    <div className="global-flex-column">
                      <ProjectEditor
                        projectForm={projectForm}
                        sortedAssignments={sortedAssignments}
                      />
                    </div>
                  </>
                ),
              },
            ]}
          />
        </div>
      </DefaultPage>
    </>
  );
}
