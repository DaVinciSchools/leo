import './ProjectBuilder.scss';

import {
  Box,
  Button,
  Step,
  StepButton,
  Stepper,
  Typography,
} from '@mui/material';
import {CSSProperties, useState} from 'react';
import {HandleError, HandleErrorType} from '../HandleError/HandleError';

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
  style?: Partial<CSSProperties>;
}) {
  const [handleError, setHandleError] = useState<HandleErrorType>();

  // All states. But, filter out REGISTER/REVIEW depending on demo mode.
  const steps: State[] = Object.values(State)
    .filter(i => typeof i === 'number')
    .map(i => i as State)
    .filter(i => (props.demo ? i !== State.REVIEW : i !== State.REGISTER));
  const [activeStep, setActiveStep] = useState(State.GETTING_STARTED);

  return (
    <>
      <HandleError error={handleError} setError={setHandleError} />
      <div
        style={Object.assign(
          {},
          {
            alignItems: 'center',
            display: 'flex',
            flexFlow: 'column nowrap',
            height: '100%',
            justifyContent: 'space-between',
            width: '100%',
          },
          props.style
        )}
      >
        <Box width="100%">
          <Box className="title" style={{borderTop: 0}}>
            <Typography variant="h4">
              <Box padding={1}>Create a new project</Box>
            </Typography>
          </Box>
          <Box className="stepper" paddingY={3}>
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
        </Box>
        <Box className="main-section">
          {steps[activeStep] === State.GETTING_STARTED && (
            <>
              <img
                src="/images/buildingProjects/designed-rocket-launching.png"
                height="200px"
              />
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
            </>
          )}
        </Box>
        {steps[activeStep] === State.GETTING_STARTED && (
          <Box padding={1} className="buttons">
            <Button
              variant="contained"
              className="button"
              disabled={props.demo}
            >
              Start with an existing project
            </Button>
            <Button
              variant="contained"
              className="button"
              onClick={() => setActiveStep(1)}
            >
              Use the Ikigai Project Builder
            </Button>
            <Button
              variant="contained"
              className="button"
              disabled={props.demo}
            >
              Create a project from scratch
            </Button>
          </Box>
        )}
        <Box className="title" style={{borderBottom: 0}}>
          <Typography variant="h4">
            <Box padding={1}>&nbsp;</Box>
          </Typography>
        </Box>
      </div>
    </>
  );
}
