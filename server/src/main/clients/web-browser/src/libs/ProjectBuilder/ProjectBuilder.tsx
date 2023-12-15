import './ProjectBuilder.scss';

import Modal from '@mui/material/Modal';
import {
  Backdrop,
  Box,
  Button,
  FormControlLabel,
  Radio,
  RadioGroup,
  Step,
  StepButton,
  Stepper,
} from '@mui/material';
import {
  CSSProperties,
  ReactNode,
  useContext,
  useEffect,
  useRef,
  useState,
} from 'react';
import {GlobalStateContext} from '../GlobalState';
import {IkigaiProjectBuilder} from '../IkigaiProjectBuilder/IkigaiProjectBuilder';
import {IkigaiProjectConfigurer} from '../IkigaiProjectConfigurer/IkigaiProjectConfigurer';
import {ModalLoginForm} from '../LoginForm/ModalLoginForm';
import {ModalRegistrationForm} from '../RegistrationForm/ModalRegistrationForm';
import {createService} from '../protos';
import {login} from '../authentication';
import {pl_types, project_management, user_x_management} from 'pl-pb';
import {useNavigate} from 'react-router';
import {ProjectsAutocomplete} from '../common_fields/ProjectsAutocomplete';
import {
  deepClone,
  DeepReadOnly,
  deepWritable,
  replaceInReadOnly,
} from '../misc';
import {PROJECT_SORTER} from '../sorters';
import {FormField, useFormFields} from '../form_utils/forms';
import ProjectManagementService = project_management.ProjectManagementService;
import UserXManagementService = user_x_management.UserXManagementService;
import IProject = pl_types.IProject;
import ExistingProjectUseType = pl_types.ProjectDefinition.ExistingProjectUseType;

enum State {
  GETTING_STARTED,
  EXISTING_PROJECT,
  PROJECT_DETAILS,
  REGISTER,
  CONGRATULATIONS,
}

const STATE_LABELS = new Map<State, string>([
  [State.GETTING_STARTED, 'Getting Started'],
  [State.EXISTING_PROJECT, 'Existing Project'],
  [State.PROJECT_DETAILS, 'Project Details'],
  [State.REGISTER, 'Log in / Register'],
  [State.CONGRATULATIONS, 'Congratulations!'],
]);

export interface ProjectInput {
  input: pl_types.IProjectInputValue;
  selected: boolean;
  highlightHue: number;
}

export function getCategoryId(
  category?: DeepReadOnly<pl_types.IProjectInputCategory | null>
) {
  return category?.id ?? -(category?.typeId ?? 0);
}

export function ProjectBuilder(
  props: DeepReadOnly<{
    noCategoriesText: ReactNode;
    style?: Partial<CSSProperties>;
  }>
) {
  const global = useContext(GlobalStateContext);
  const userX = global.optionalUserX();
  const navigate = useNavigate();
  const hiddenForm = useFormFields();

  const [projectInputId, setProjectInputId] = useState<number | undefined>();
  const [demoInputs, setDemoInputs] = useState<DeepReadOnly<ProjectInput[]>>(
    []
  );
  const demoInputsLoaded = useRef(false);
  const [allInputs, internalSetAllInputs] = useState<
    DeepReadOnly<ProjectInput[]>
  >([]);

  const [loginUserXVisible, setLoginUserXVisible] = useState(false);
  const [registerUserXVisible, setRegisterUserXVisible] = useState(false);
  const [accountAlreadyExistsVisible, setAccountAlreadyExistsVisible] =
    useState(false);

  const [selectExistingProject, setSelectExistingProject] = useState(false);
  const [sortedExistingProjects, setSortedExistingProjects] = useState<
    DeepReadOnly<IProject[]>
  >([]);
  const selectedExistingProject =
    hiddenForm.useAutocompleteFormField<IProject | null>('project', {
      onChange: selectedExistingProjectUpdated,
    });
  const [selectedExistingProjectUseType, setSelectedExistingProjectUseType] =
    useState<ExistingProjectUseType | null>(null);

  // All states. But, filter out REGISTER depending on whether a user is already logged in.
  const steps = Object.values(State)
    .filter(i => typeof i === 'number')
    .filter(i => selectExistingProject || i !== State.EXISTING_PROJECT)
    .filter(i => !userX?.isAuthenticated || i !== State.REGISTER)
    .map(i => i as State);
  const [activeStep, internalSetActiveStep] = useState(State.GETTING_STARTED);

  function setAllInputs(newInputs: DeepReadOnly<ProjectInput[]>) {
    // Color selected inputs.
    const newAllInputs: DeepReadOnly<ProjectInput>[] = [];
    const selectedInputsCount = newInputs.filter(i => i.selected).length;
    let selectedCountIndex = 0;
    newInputs.forEach(i => {
      if (i.selected) {
        const newI = deepClone(i);
        newI.highlightHue = selectedCountIndex * (360 / selectedInputsCount);
        newAllInputs.push(newI);
        ++selectedCountIndex;
      } else {
        newAllInputs.push(i);
      }
    });
    internalSetAllInputs(newAllInputs);
  }

  function setInput(newInput: DeepReadOnly<ProjectInput>) {
    setAllInputs(
      replaceInReadOnly(allInputs, newInput, i =>
        getCategoryId(i.input?.category)
      )
    );
  }

  function setActiveStep(step: State) {
    if (step === State.GETTING_STARTED) {
      setSelectExistingProject(false);
      selectedExistingProject.setValue(null);
      setSelectedExistingProjectUseType(null);
    }
    if (step === State.PROJECT_DETAILS && selectedExistingProject) {
      switch (selectedExistingProjectUseType) {
        case ExistingProjectUseType.USE_CONFIGURATION:
          loadAndSetInputs(selectedExistingProject.getValue());
          break;
        case ExistingProjectUseType.MORE_LIKE_THIS:
          loadAndSetInputs(selectedExistingProject.getValue());
          break;
        case ExistingProjectUseType.SUB_PROJECTS:
          loadAndSetInputs();
      }
    }
    internalSetActiveStep(step);
  }

  function loadUserXProjects() {
    if (!userX?.isAuthenticated) {
      setSelectExistingProject(false);
      setSortedExistingProjects([]);
      selectedExistingProject.setValue(null);
      setSelectedExistingProjectUseType(null);
    } else {
      createService(ProjectManagementService, 'ProjectManagementService')
        .getProjects({
          userXIds: [userX?.id ?? 0],
          includeAssignment: true,
          includeInputs: true,
          includeInputOptions: true,
        })
        .then(response => {
          setSelectExistingProject(false);
          setSortedExistingProjects(response.projects.sort(PROJECT_SORTER));
          selectedExistingProject.setValue(null);
          setSelectedExistingProjectUseType(null);
        })
        .catch(global.setError);
    }
  }

  function loadAndSetInputs(project?: DeepReadOnly<IProject | null>) {
    if (!demoInputsLoaded.current) {
      demoInputsLoaded.current = true;
      createService(ProjectManagementService, 'ProjectManagementService')
        .getProjectDefinitionCategoryTypes({includeDemos: true})
        .then(response => {
          const inputCategories = response.inputCategories.map((c, index) => ({
            input: {
              category: c,
              freeTexts: [],
              selectedIds: [],
            },
            selected: index < 4 && !project,
            highlightHue: 0,
          }));
          setDemoInputs(inputCategories);
        })
        .catch(global.setError);
      return;
    } else if (demoInputs.length === 0) {
      return;
    }

    setAllInputs(demoInputs);
  }

  function selectedExistingProjectUpdated(
    selectedExistingProject: FormField<IProject | null>
  ) {
    loadAndSetInputs(selectedExistingProject.getValue());
  }

  function startGeneratingProjects() {
    createService(ProjectManagementService, 'ProjectManagementService')
      .generateProjects({
        definition: {
          inputs: deepWritable(
            allInputs
              .filter(i => i.selected)
              .map(i => i.input)
              .filter(i => i != null)
          ),
          existingProject:
            selectedExistingProject.getValue()?.id != null
              ? {
                  id: selectedExistingProject.getValue()?.id ?? undefined,
                }
              : undefined,
          existingProjectUseType: selectedExistingProjectUseType ?? undefined,
        },
      })
      .then(response => {
        setProjectInputId(response.projectInputId ?? undefined);
        setActiveStep(activeStep + 1);
      })
      .catch(global.setError);
  }

  function onLoggedIn() {
    setLoginUserXVisible(false);
    createService(ProjectManagementService, 'ProjectManagementService')
      .registerAnonymousProjects({projectInputId})
      .then(() => {
        // The act of logging in should advance the step due to the removal of the REGISTER step.
        setProjectInputId(undefined);
      })
      .catch(global.setError);
  }

  function onRegisterUserX(request: user_x_management.IRegisterUserXRequest) {
    createService(UserXManagementService, 'UserXManagementService')
      .registerUserX(request)
      .then(response => {
        if (response.accountAlreadyExists) {
          setAccountAlreadyExistsVisible(true);
        } else {
          login(
            global,
            request.emailAddress ?? '',
            request.password ?? '',
            () => {
              createService(
                ProjectManagementService,
                'ProjectManagementService'
              )
                .registerAnonymousProjects({projectInputId})
                .then(() => {
                  // The act of logging in should advance the step due to the removal of the REGISTER step.
                  setProjectInputId(undefined);
                })
                .catch(global.setError);
            },
            () => {
              global.setError('Failed to log registered user in.');
            },
            global.setError
          );
        }
      })
      .catch(global.setError);
  }

  function onSeeProjects() {
    navigate('/projects/all-projects.html');
  }

  useEffect(() => {
    loadAndSetInputs();
  }, [demoInputs, selectedExistingProject.getValue()]);

  useEffect(() => loadUserXProjects(), [userX]);

  return (
    <>
      <div className="project-builder-page-layout" style={props.style}>
        <Box className="project-builder-stepper" paddingY={3}>
          <Stepper activeStep={activeStep}>
            {steps.map((value, index) => (
              <Step
                key={value}
                completed={index < activeStep}
                onClick={() => {
                  if (index < activeStep) {
                    setActiveStep(index);
                  }
                }}
              >
                <StepButton>
                  <div className="project-builder-stepper-buttons">
                    {STATE_LABELS.get(value)}
                  </div>
                </StepButton>
              </Step>
            ))}
          </Stepper>
        </Box>
        <div className="project-builder-content">
          {steps[activeStep] === State.GETTING_STARTED && (
            <div className="project-builder-getting-started">
              <Box padding={1}>
                <img
                  src="/images/buildingProjects/designed-rocket-launching.png"
                  height="200px"
                />
              </Box>
              <div className="project-builder-getting-started-title">
                Choose how you want to start creating your project
              </div>
              <Box padding={1}>
                You can start with an existing project,
                <br />
                select a favorite from the Ikigai Project Builder,
                <br />
                or create a project from scratch.
              </Box>
              <Box
                padding={1}
                className="project-builder-getting-started-buttons"
              >
                <Button
                  variant="contained"
                  className="project-builder-button"
                  disabled={!userX?.isAuthenticated}
                  onClick={() => {
                    setSelectExistingProject(true);
                    setActiveStep(activeStep + 1);
                  }}
                >
                  Start with an
                  <br />
                  existing project
                </Button>
                <Button
                  variant="contained"
                  className="project-builder-button"
                  disabled={true}
                >
                  Create for
                  <br />
                  an assignment
                </Button>
                <Button
                  variant="contained"
                  className="project-builder-button"
                  onClick={() => setActiveStep(activeStep + 1)}
                >
                  Create a custom
                  <br />
                  ikigai project
                </Button>
                <Button
                  variant="contained"
                  className="project-builder-button"
                  disabled={true}
                >
                  Manually create
                  <br />a new project
                </Button>
              </Box>
            </div>
          )}
          {steps[activeStep] === State.EXISTING_PROJECT && (
            <Box className="project-builder-existing-project">
              <div
                className="global-flex-column"
                style={{
                  alignItems: 'center',
                  justifyContent: 'space-evenly',
                  height: '100%',
                }}
              >
                <div
                  className="global-flex-column"
                  style={{alignItems: 'center'}}
                >
                  <div className="project-builder-existing-project-title">
                    Select the project to use as a starting point
                  </div>
                  <div
                    style={{width: '70%', paddingTop: '1em'}}
                    className="global-flex-row"
                  >
                    <ProjectsAutocomplete
                      // TODO: make ProjectsAutocomplete take a DeepReadOnly.
                      sortedProjects={
                        deepWritable(sortedExistingProjects) ?? []
                      }
                      formField={selectedExistingProject}
                      style={{width: '100%'}}
                    />
                  </div>
                </div>
                <div
                  className="global-flex-column"
                  style={{alignItems: 'center'}}
                >
                  <div className="project-builder-existing-project-title">
                    Select how to use this project
                  </div>
                  <div>
                    <RadioGroup
                      defaultValue=""
                      name="radio-buttons-group"
                      value={selectedExistingProjectUseType}
                      onChange={event => {
                        setSelectedExistingProjectUseType(
                          parseInt(event.target.value) as ExistingProjectUseType
                        );
                      }}
                    >
                      <FormControlLabel
                        value={ExistingProjectUseType.USE_CONFIGURATION}
                        control={<Radio />}
                        style={{padding: '1em'}}
                        label={
                          <>
                            <div style={{fontWeight: 'bold'}}>
                              Start with this project's configuration
                            </div>
                            <div>
                              Start with the selected project's configuration.
                              Then modify it, if desired, before generating new
                              projects. New projects{' '}
                              <u>will likely be unrelated</u> to this project.
                            </div>
                          </>
                        }
                      />
                      <FormControlLabel
                        value={ExistingProjectUseType.MORE_LIKE_THIS}
                        control={<Radio />}
                        style={{padding: '1em'}}
                        label={
                          <>
                            <div style={{fontWeight: 'bold'}}>
                              Generate projects that are similar to this project
                            </div>
                            <div>
                              Start with the selected project's configuration.
                              Then modify it, if desired, before generating new
                              projects. New projects <u>should be similar</u> to
                              this project.
                            </div>
                          </>
                        }
                      />
                      <FormControlLabel
                        value={ExistingProjectUseType.SUB_PROJECTS}
                        control={<Radio />}
                        style={{padding: '1em'}}
                        label={
                          <>
                            <div style={{fontWeight: 'bold'}}>
                              Generate subprojects for this project
                            </div>
                            <div>
                              Start with the selected project's configuration.
                              Then modify it, if desired, before generating new
                              subprojects. New subprojects{' '}
                              <u>will be a part of</u> this larger project. But,
                              they will focus on achieving the modified
                              configuration's goals.
                            </div>
                          </>
                        }
                      />
                    </RadioGroup>
                  </div>
                </div>
                <div
                  className="global-flex-row"
                  style={{justifyContent: 'space-evenly'}}
                >
                  <Button
                    variant="contained"
                    className="project-builder-button"
                    onClick={() => setActiveStep(State.GETTING_STARTED)}
                  >
                    <div style={{padding: '0.7em 0'}}>&lt;&nbsp;&nbsp;Back</div>
                  </Button>
                  <Button
                    variant="contained"
                    className="project-builder-button"
                    disabled={
                      !selectedExistingProject ||
                      selectedExistingProjectUseType == null
                    }
                    onClick={() => setActiveStep(activeStep + 1)}
                  >
                    <div style={{padding: '0.7em 0'}}>
                      Continue&nbsp;&nbsp;&gt;
                    </div>
                  </Button>
                </div>
              </div>
            </Box>
          )}
          {steps[activeStep] === State.PROJECT_DETAILS && (
            <>
              <Box className="project-builder-project-details">
                <div
                  className="project-builder-project-details-title"
                  style={{
                    gridColumn: 1,
                    gridRow: 1,
                  }}
                >
                  Ikigai Project Builder Configuration
                </div>
                <div
                  className="project-builder-project-details-description"
                  style={{
                    gridColumn: 1,
                    gridRow: 2,
                  }}
                >
                  Select categories below to include in the Ikigai Project
                  Builder diagram on the right. You can also drag them up and
                  down to reorder them.
                </div>
                <div
                  className="project-builder-project-details-widget"
                  style={{
                    gridColumn: 1,
                    gridRow: 3,
                  }}
                >
                  <IkigaiProjectConfigurer
                    inputs={allInputs}
                    setInputs={setAllInputs}
                  />
                </div>
                <div
                  className="project-builder-project-details-title project-builder-project-details-ikigai-builder"
                  style={{
                    gridColumn: 2,
                    gridRow: 1,
                  }}
                >
                  Ikigai Project Builder
                </div>
                <div
                  className="project-builder-project-details-description project-builder-project-details-ikigai-builder"
                  style={{
                    gridColumn: 2,
                    gridRow: 2,
                  }}
                >
                  Select categories on the left to include in the Ikigai Project
                  Builder below. Click on the circles to indicate what sorts of
                  projects you wish to generate. After they are all filled,
                  click the <i>SPIN</i> button that appears.
                </div>
                <div
                  style={{
                    gridColumn: 2,
                    gridRow: 3,
                  }}
                  className="project-builder-project-details-widget project-builder-project-details-ikigai-builder global-flex-column"
                >
                  {selectedExistingProjectUseType ===
                  ExistingProjectUseType.USE_CONFIGURATION ? (
                    <div className="project-builder-project-details-project-configuration">
                      <div style={{fontWeight: 'bold'}}>
                        Using an existing project as the configuration for this
                        one.
                      </div>
                      The initial configuration below is from the{' '}
                      <u>
                        {selectedExistingProject?.name ?? '[Unknown Project]'}
                      </u>{' '}
                      project. Modify it, if desired, before generating new
                      projects. The new projects <u>will likely be unrelated</u>{' '}
                      to that project.
                    </div>
                  ) : selectedExistingProjectUseType ===
                    ExistingProjectUseType.MORE_LIKE_THIS ? (
                    <div className="project-builder-project-details-project-configuration">
                      <div style={{fontWeight: 'bold'}}>
                        Using an existing project to create similar projects.
                      </div>
                      The initial configuration below is from the{' '}
                      <u>
                        {selectedExistingProject?.name ?? '[Unknown Project]'}
                      </u>{' '}
                      project. Modify it, if desired, before generating new
                      projects. The new projects <u>should be similar</u> to
                      that project.
                    </div>
                  ) : selectedExistingProjectUseType ===
                    ExistingProjectUseType.SUB_PROJECTS ? (
                    <div className="project-builder-project-details-project-configuration">
                      <div style={{fontWeight: 'bold'}}>
                        Using an existing project to create subprojects.
                      </div>
                      The initial configuration below is from the{' '}
                      <u>
                        {selectedExistingProject?.name ?? '[Unknown Project]'}
                      </u>{' '}
                      project. Modify it, if desired, before generating new
                      subprojects. The new subprojects <u>will be a part of</u>{' '}
                      that larger project. But, they will focus on achieving
                      these modified configuration's goals.
                    </div>
                  ) : (
                    <></>
                  )}
                  <IkigaiProjectBuilder
                    inputs={allInputs.filter(i => i.selected)}
                    setInput={setInput}
                    noInputsText={props.noCategoriesText}
                    inputDiameter={(width, height) =>
                      (Math.min(width, height) / 2) * 0.95
                    }
                    distanceToInputCenter={(width, height) =>
                      (Math.min(width, height) / 4) * 0.85
                    }
                    enabled={true}
                    onSpinClick={startGeneratingProjects}
                  />
                </div>
              </Box>
            </>
          )}
          {steps[activeStep] === State.REGISTER && (
            <Box className="project-builder-register">
              <Box padding={1} className="project-builder-register-title">
                Using the power of AI to create new and unique projects
                <br />
                tailored to your individual preferences!
              </Box>
              <Box
                padding={1}
                className="project-builder-register-instructions"
              >
                To explore the resulting projects, please log in or register
                below.
              </Box>
              <Box padding={1} className="project-builder-register-buttons">
                <Button
                  variant="contained"
                  className="project-builder-button"
                  onClick={() => setLoginUserXVisible(true)}
                >
                  Log in with an existing account
                </Button>
                <Button
                  variant="contained"
                  className="project-builder-button"
                  onClick={() => setRegisterUserXVisible(true)}
                >
                  Register for a new account
                </Button>
              </Box>
              <div />
              <ModalLoginForm
                open={loginUserXVisible}
                onLoggedIn={onLoggedIn}
                onCancel={() => setLoginUserXVisible(false)}
              />
              <ModalRegistrationForm
                open={registerUserXVisible}
                onRegisterUserX={onRegisterUserX}
                onCancel={() => setRegisterUserXVisible(false)}
              />
              <Modal
                open={accountAlreadyExistsVisible}
                slots={{backdrop: Backdrop}}
                slotProps={{
                  backdrop: {
                    timeout: 500,
                  },
                }}
              >
                <div className="project-builder-register-modal">
                  The email address you entered is already associated with an
                  account. Do you want to register with a different email
                  address? Or, log in using the existing account?
                  <div className="project-builder-register-buttons">
                    <Button
                      variant="contained"
                      className="project-builder-button"
                      onClick={() => {
                        setAccountAlreadyExistsVisible(false);
                      }}
                    >
                      Register with a different email
                    </Button>
                    <Button
                      variant="contained"
                      className="project-builder-button"
                      onClick={() => {
                        setAccountAlreadyExistsVisible(false);
                        setRegisterUserXVisible(false);
                        setLoginUserXVisible(true);
                      }}
                    >
                      Login using the existing account
                    </Button>
                  </div>
                </div>
              </Modal>
            </Box>
          )}
          {steps[activeStep] === State.CONGRATULATIONS && (
            <div className="project-builder-congratulations">
              Congratulations! Let's take you to your projects!
              <Box className="project-builder-congratulations-main">
                <Box padding={1}>
                  <img src="/images/landing/kids-jumping.png" height="200px" />
                </Box>
              </Box>
              <Box
                padding={1}
                className="project-builder-congratulations-buttons"
              >
                <Button
                  variant="contained"
                  className="project-builder-button"
                  onClick={onSeeProjects}
                >
                  Go see my projects!
                </Button>
              </Box>
            </div>
          )}
        </div>
      </div>
    </>
  );
}
