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
import {usernamePasswordLogin} from '../authentication';
import {pl_types, project_management, user_x_management} from 'pl-pb';
import {useNavigate} from 'react-router';
import {ProjectsAutocomplete} from '../common_fields/ProjectsAutocomplete';
import {
  deepClone,
  deepReadOnly,
  DeepReadOnly,
  deepWritable,
  replaceInDeepReadOnly,
  writableForProto,
} from '../misc';
import {PROJECT_SORTER} from '../sorters';
import {useFormFields} from '../form_utils/forms';
import {ProjectsTab} from '../../pages/projects/ProjectsDashboard';
import {TAB_PARAM_NAME} from '../TabbedPanel/TabbedPanel';
import ProjectManagementService = project_management.ProjectManagementService;
import UserXManagementService = user_x_management.UserXManagementService;
import IProject = pl_types.IProject;
import ExistingProjectUseType = pl_types.ProjectDefinition.ExistingProjectUseType;
import FreeTextInputModal from '../IkigaiProjectBuilder/FreeTextInputModal';
import ValueType = pl_types.ProjectInputCategory.ValueType;
import {DropdownSelectInputModal} from '../IkigaiProjectBuilder/DropdownSelectInputModal';

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
  input: DeepReadOnly<pl_types.IProjectInputValue>;
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
  const [openCategoryModalId, setOpenCategoryModalId] = useState<
    number | undefined | null
  >();

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
    hiddenForm.useSingleAutocompleteFormField<DeepReadOnly<IProject>>(
      'project'
    );
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
      replaceInDeepReadOnly(allInputs, newInput, i =>
        getCategoryId(i.input?.category)
      )
    );
  }

  function setActiveStep(step: State) {
    if (step === State.GETTING_STARTED) {
      setSelectExistingProject(false);
      selectedExistingProject.setValue(undefined);
      setSelectedExistingProjectUseType(null);
    }
    if (step === State.PROJECT_DETAILS && selectedExistingProject.getValue()) {
      loadAndSetInputs();
    }
    internalSetActiveStep(step);
  }

  function loadUserXProjects() {
    if (!userX?.isAuthenticated) {
      setSelectExistingProject(false);
      setSortedExistingProjects([]);
      selectedExistingProject.setValue(undefined);
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
          selectedExistingProject.setValue(undefined);
          setSelectedExistingProjectUseType(null);
        })
        .catch(global.setError);
    }
  }

  function loadAndSetInputs() {
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
            selected: index < 4,
            highlightHue: 0,
          }));
          setDemoInputs(inputCategories);
        })
        .catch(global.setError);
      return;
    } else if (demoInputs.length === 0) {
      return;
    }

    // Place the project's inputs at the beginning of the inputs list.
    if (selectedExistingProject.getValue() != null) {
      // We will exclude the demoInputs that are going to be replaced by the project's inputs.
      const demoInputIdsToFilterOut = new Set(
        (
          selectedExistingProject.getValue()!.projectDefinition?.inputs ?? []
        ).flatMap(i => [
          getCategoryId(i?.category),
          -(i?.category?.typeId ?? 0),
        ])
      );

      // Exclude the demoInputs and mark the remaining ones as unselected.
      const newDemoInputs = demoInputs
        .filter(
          i => !demoInputIdsToFilterOut.has(getCategoryId(i.input?.category))
        )
        .map(i => {
          if (i.selected) {
            const newI = deepClone(i);
            newI.selected = false;
            return deepReadOnly(newI);
          }
          return i;
        });

      // Create entries for the project's inputs and mark them as selected.
      const newProjectInputs = (
        selectedExistingProject.getValue()!.projectDefinition?.inputs ?? []
      ).map(
        i =>
          ({
            input: i,
            selected: true,
            highlightHue: 0,
          } as ProjectInput)
      );

      setAllInputs([...newProjectInputs, ...newDemoInputs]);
    } else {
      setAllInputs(demoInputs);
    }
  }

  function startGeneratingProjects() {
    createService(ProjectManagementService, 'ProjectManagementService')
      .generateProjects({
        definition: {
          inputs: writableForProto(
            allInputs
              .filter(i => i.selected)
              .map(i => i.input)
              .filter(i => i != null)
          ),
          existingProject: writableForProto(selectedExistingProject.getValue()),
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
          usernamePasswordLogin(
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
    navigate(
      `/projects/projects.html?${TAB_PARAM_NAME}=${
        ProjectsTab[ProjectsTab.ALL_PROJECTS]
      }`
    );
  }

  function handleClickEditInputValues(input: ProjectInput) {
    setOpenCategoryModalId(getCategoryId(input.input.category));
  }

  function handleModalClose() {
    setOpenCategoryModalId(null);
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
                Let's get started!
              </div>
              <Box
                padding={1}
                className="project-builder-getting-started-buttons"
              >
                {userX?.isDemo && (
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
                )}
                {/* <Button
                  variant="contained"
                  className="project-builder-button"
                  disabled={true}
                >
                  Create for
                  <br />
                  an assignment
                </Button> */}
                <Button
                  variant="contained"
                  className="project-builder-button"
                  onClick={() => setActiveStep(activeStep + 1)}
                >
                  Create a custom
                  <br />
                  Ikigai project
                </Button>
                {/* <Button
                  variant="contained"
                  className="project-builder-button"
                  disabled={true}
                >
                  Manually create
                  <br />a new project
                </Button> */}
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
                              Generate sub-projects for this project
                            </div>
                            <div>
                              Use the selected project as a larger project from
                              which to create sub-projects. New sub-projects{' '}
                              <u>will be a part of</u> the larger, selected
                              project. But, they will focus on achieving
                              modified goals in the new configuration, below.
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
                      !selectedExistingProject.getValue() ||
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
              {allInputs?.map(input => {
                switch (
                  input.input.category?.valueType ??
                  ValueType.UNSET_VALUE_TYPE
                ) {
                  case pl_types.ProjectInputCategory.ValueType.FREE_TEXT:
                    return (
                      <FreeTextInputModal
                        open={
                          openCategoryModalId ===
                          getCategoryId(input.input.category)
                        }
                        key={getCategoryId(input?.input?.category)}
                        input={input}
                        setInput={setInput}
                        onClickClose={handleModalClose}
                      />
                    );
                  default:
                    return (
                      <DropdownSelectInputModal
                        open={
                          openCategoryModalId ===
                          getCategoryId(input.input.category)
                        }
                        key={getCategoryId(input?.input?.category)}
                        input={input}
                        setInput={setInput}
                        onClickClose={handleModalClose}
                      />
                    );
                }
              })}
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
                  Project Leo generates student project ideas from three key
                  information sources. The first two, contributed by students,
                  guide the project suggestions made by Project Leo. The third,
                  'Assignment Topic,' is predetermined by the teacher.
                </div>
                <div
                  className="project-builder-project-details-widget"
                  style={{
                    gridColumn: 1,
                    gridRow: 3,
                    overflow: 'scroll',
                    position: 'relative',
                  }}
                >
                  <IkigaiProjectConfigurer
                    inputs={allInputs}
                    setInputs={setAllInputs}
                    onClickEditInputValues={handleClickEditInputValues}
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
                        Using an existing project as the initial configuration
                        for this one.
                      </div>
                      The initial configuration below is from the{' '}
                      <u>
                        {selectedExistingProject?.getValue()?.name ??
                          '[Unknown Project]'}
                      </u>{' '}
                      project. Modify it, if desired, before generating new
                      projects. The new projects <u>will likely be unrelated</u>{' '}
                      to that project.
                    </div>
                  ) : selectedExistingProjectUseType ===
                    ExistingProjectUseType.MORE_LIKE_THIS ? (
                    <div className="project-builder-project-details-project-configuration">
                      <div style={{fontWeight: 'bold'}}>
                        Using an existing project to create more projects like
                        it.
                      </div>
                      The initial configuration below is from the{' '}
                      <u>
                        {selectedExistingProject?.getValue()?.name ??
                          '[Unknown Project]'}
                      </u>{' '}
                      project. Modify it, if desired, before generating new
                      projects. The new projects <u>should be similar</u> to
                      that project.
                    </div>
                  ) : selectedExistingProjectUseType ===
                    ExistingProjectUseType.SUB_PROJECTS ? (
                    <div className="project-builder-project-details-project-configuration">
                      <div style={{fontWeight: 'bold'}}>
                        Using an existing project to create sub-projects.
                      </div>
                      The{' '}
                      <u>
                        {selectedExistingProject?.getValue()?.name ??
                          '[Unknown Project]'}
                      </u>{' '}
                      project will be a "parent" project. The configuration
                      below will be used to generate sub-projects for it. The
                      sub-projects <u>will be a part of</u> that larger project.
                      But, they will focus on achieving the goals in the new
                      configuration below.
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
                    onInputClick={handleClickEditInputValues}
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
                onSuccess={onLoggedIn}
                onFailure={() => {
                  // Do nothing. Allow the user to retry.
                }}
                onCancel={() => setLoginUserXVisible(false)}
                onError={global.setError}
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
