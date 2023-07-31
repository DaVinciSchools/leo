import './ProjectBuilder.scss';

import {
  Box,
  Button,
  Step,
  StepButton,
  Stepper,
  Typography,
} from '@mui/material';
import {CSSProperties, ReactNode, useEffect, useState} from 'react';
import {HandleError, HandleErrorType} from '../HandleError/HandleError';
import {IkigaiProjectBuilder} from '../IkigaiProjectBuilder/IkigaiProjectBuilder';
import {
  pl_types,
  project_management,
  user_management,
} from '../../generated/protobuf-js';
import {createService} from '../protos';
import ProjectManagementService = project_management.ProjectManagementService;
import {IkigaiProjectConfigurer} from '../IkigaiProjectConfigurer/IkigaiProjectConfigurer';
import {RegistrationForm} from './RegistrationForm/RegistrationForm';
import IRegisterUserRequest = user_management.IRegisterUserRequest;

enum State {
  GETTING_STARTED,
  PROJECT_DETAILS,
  REGISTER,
  REVIEW,
}

const STATE_LABELS = new Map<State, string>([
  [State.GETTING_STARTED, 'Getting Started'],
  [State.PROJECT_DETAILS, 'Project Details'],
  [State.REGISTER, 'Register'],
  [State.REVIEW, 'Review'],
]);

export function ProjectBuilder(props: {
  demo?: boolean;
  noCategoriesText: ReactNode;
  style?: Partial<CSSProperties>;
}) {
  const [handleError, setHandleError] = useState<HandleErrorType>();
  const [allInputValues, setAllInputValues] = useState<
    pl_types.IProjectInputValue[]
  >([]);
  const [inputValues, setInputValues] = useState<pl_types.IProjectInputValue[]>(
    []
  );
  const [userId, setUserId] = useState(-1);

  // All states. But, filter out REGISTER/REVIEW depending on demo mode.
  const steps: State[] = Object.values(State)
    .filter(i => typeof i === 'number')
    .map(i => i as State)
    .filter(i => (props.demo ? i !== State.REVIEW : i !== State.REGISTER));
  const [activeStep, setActiveStep] = useState(State.GETTING_STARTED);

  useEffect(() => {
    if (props.demo && allInputValues.length === 0) {
      createService(ProjectManagementService, 'ProjectManagementService')
        .getProjectDefinitionCategoryTypes({includeDemos: true})
        .then(response => {
          setAllInputValues(
            response.inputCategories.map(c => ({
              category: c,
              freeTexts: [],
              selectedIds: [],
            }))
          );
        })
        .catch(setHandleError);
    }
  }, [props.demo]);

  function startGeneratingProjects(values: pl_types.IProjectInputValue[]) {
    createService(ProjectManagementService, 'ProjectManagementService')
      .generateAnonymousProjects({inputValues: values})
      .then(response => {
        setUserId(response.userId!);
        setActiveStep(activeStep + 1);
      })
      .catch(setHandleError);
  }

  function onRegisterUser(request: IRegisterUserRequest) {
    request.userId = userId;

    // TODO
    console.log(request);
  }

  return (
    <>
      <HandleError error={handleError} setError={setHandleError} />
      <div className="project-builder-page-layout" style={props.style}>
        <Box className="project-builder-header">
          <Typography variant="h4">
            <Box padding={1}>Create a new project</Box>
          </Typography>
        </Box>
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
                  <Typography variant="h5">
                    {STATE_LABELS.get(value)}
                  </Typography>
                </StepButton>
              </Step>
            ))}
          </Stepper>
        </Box>
        <div className="project-builder-content">
          {steps[activeStep] === State.GETTING_STARTED && (
            <div className="project-builder-getting-started">
              <Box className="project-builder-getting-started-main">
                <Box padding={1}>
                  <img
                    src="/images/buildingProjects/designed-rocket-launching.png"
                    height="200px"
                  />
                </Box>
                <Box padding={1}>
                  <Typography variant="h4">
                    Choose how you want to start creating your project
                  </Typography>
                </Box>
                <Box padding={1}>
                  <Typography variant="h5">
                    You can start with an existing project,
                    <br />
                    select a favorite from the Ikigai Project Builder,
                    <br />
                    or create a project from scratch.
                  </Typography>
                </Box>
              </Box>
              <Box
                padding={1}
                className="project-builder-getting-started-buttons"
              >
                <Button
                  variant="contained"
                  className="project-builder-button"
                  disabled={props.demo}
                >
                  Start with an
                  <br />
                  existing project
                </Button>
                <Button
                  variant="contained"
                  className="project-builder-button"
                  onClick={() => setActiveStep(activeStep + 1)}
                >
                  Use the Ikigai
                  <br />
                  Project Builder
                </Button>
                <Button
                  variant="contained"
                  className="project-builder-button"
                  disabled={props.demo}
                >
                  Create a project
                  <br />
                  from scratch
                </Button>
              </Box>
            </div>
          )}
          {steps[activeStep] === State.PROJECT_DETAILS && (
            <Box className="project-builder-project-details">
              <div
                className="project-builder-project-details-column"
                style={{width: '30%'}}
              >
                <div className="project-builder-project-details-column-title">
                  Ikigai Project Builder Configuration
                </div>
                <div>
                  Select categories below to include in the Ikigai Project
                  Builder diagram on the right. You can also drag them up and
                  down to reorder them.
                </div>
                <IkigaiProjectConfigurer
                  allCategories={allInputValues}
                  setSelectedCategories={setInputValues}
                />
              </div>
              <div
                className="project-builder-project-details-column"
                style={{width: '60%'}}
              >
                <div className="project-builder-project-details-column-title">
                  Ikigai Project Builder
                </div>
                <div>
                  Select categories on the left to include in the Ikigai Project
                  Builder below. Click on the circles to indicate what sorts of
                  projects you wish to generate. After they are all filled,
                  click the <i>SPIN</i> button that appears.
                </div>
                <IkigaiProjectBuilder
                  id="ikigai-builder"
                  categories={inputValues}
                  noCategoriesText={props.noCategoriesText}
                  categoryDiameter={(width, height) =>
                    Math.min(width, height) / 2
                  }
                  distanceToCategoryCenter={(width, height) =>
                    (Math.min(width, height) / 2) * 0.45
                  }
                  enabled={true}
                  onSpinClick={(
                    configuration: pl_types.IProjectInputValue[]
                  ) => {
                    startGeneratingProjects(configuration);
                  }}
                />
              </div>
            </Box>
          )}
          {steps[activeStep] === State.REGISTER && (
            <Box className="project-builder-register">
              <div>
                <Box padding={1} className="project-builder-register-title">
                  <Typography variant="h4">
                    Using the power of AI to create new and unique projects
                    <br />
                    tailored to your individual preferences!
                  </Typography>
                </Box>
                <Box
                  padding={1}
                  className="project-builder-register-instructions"
                >
                  To explore the resulting projects, please register below.
                </Box>
              </div>
              <Box paddingY={3} className="project-builder-register-form">
                <RegistrationForm onRegisterUser={onRegisterUser} />
              </Box>
            </Box>
          )}
        </div>
        <Box className="project-builder-footer" style={{width: '100%'}}>
          <Typography variant="h4">
            <Box padding={1}>&nbsp;</Box>
          </Typography>
        </Box>
      </div>
    </>
  );
}
