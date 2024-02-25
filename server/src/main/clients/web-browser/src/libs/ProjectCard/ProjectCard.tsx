import {
  Alert,
  Button,
  Card,
  CardContent,
  IconButton,
  Link,
  TextField,
  ToggleButton,
  ToggleButtonGroup,
  Typography,
} from '@mui/material';
import {useEffect} from 'react';
import {pl_types} from 'pl-pb';
import {
  DataObject,
  InfoOutlined,
  ThumbDownAlt,
  ThumbUpAlt,
} from '@mui/icons-material';
import {styled} from 'styled-components';
import {useForm} from 'react-hook-form';
import IProject = pl_types.IProject;
import ThumbsState = pl_types.Project.ThumbsState;

const ActivateButton = styled(Button)`
  margin-left: auto;
`;

const StyledAlert = styled(Alert)`
  margin: ${props => props.theme.spacing(1)} 0;
`;

const Form = styled.form`
  display: flex;
  gap: ${props => props.theme.spacing(1)};
  align-items: flex-start;
  width: 100%;
`;

const CardHeader = styled.div`
  display: flex;
  align-items: flex-start;
  gap: ${props => props.theme.spacing(1)};

  h2 {
    flex-grow: 1;
  }
`;

const CardFooter = styled.div`
  background: ${props => props.theme.palette.grey[100]};
  display: flex;

  padding: ${props => props.theme.spacing(2)};
  gap: ${props => props.theme.spacing(1)};
`;

interface FeedbackForm {
  feedback: string;
}

export function ProjectCard(props: {
  id: number;
  name: string;
  shortDescr: string;
  longDescrHtml: string;
  active: boolean;
  favorite: boolean;
  thumbsState: ThumbsState;
  thumbsStateReason: string;
  updateProject: (update: IProject) => void;
  showDetails: () => void;
  showAiPrompt?: () => void;
}) {
  const {
    register,
    formState: {isDirty, isSubmitSuccessful},
    handleSubmit,
    reset,
    setFocus,
    getValues,
  } = useForm<FeedbackForm>({
    defaultValues: {
      feedback: props.thumbsStateReason,
    },
  });

  useEffect(() => {
    reset({}, {keepValues: true});
  }, [isSubmitSuccessful]);

  const onSubmit = handleSubmit(data => {
    props.updateProject({
      thumbsStateReason: data.feedback,
    });
  });

  const helperText = props.active
    ? 'Why did you choose this project?'
    : props.thumbsState === ThumbsState.THUMBS_UP
      ? 'Why do you like this project?'
      : props.thumbsState === ThumbsState.THUMBS_DOWN
        ? 'Why do you dislike this project?'
        : 'What do you think about this project?';

  return (
    <>
      <Card variant="outlined">
        <CardContent>
          <CardHeader>
            <Typography variant="h5" component="h2" gutterBottom>
              {props.name}
            </Typography>
            <IconButton
              onClick={() => {
                props.showDetails();
              }}
              size="small"
            >
              <InfoOutlined />
            </IconButton>
            {!!props.showAiPrompt && (
              <IconButton
                onClick={() => {
                  props.showAiPrompt?.();
                }}
                size="small"
              >
                <DataObject />
              </IconButton>
            )}
            <ActivateButton
              onClick={() => {
                props.updateProject({
                  active: !props.active,
                  ...(!props.active
                    ? {thumbsState: ThumbsState.THUMBS_UP}
                    : {}),
                });
                if (!getValues().feedback) {
                  setFocus('feedback');
                }
              }}
              variant={props.active ? 'text' : 'contained'}
              color={props.active ? 'warning' : 'primary'}
            >
              {props.active ? 'Deactivate' : 'Activate'}
            </ActivateButton>
          </CardHeader>
          {props.active && (
            <StyledAlert severity="success">
              This project is activated.
            </StyledAlert>
          )}
          <Typography paragraph>{props.shortDescr}.</Typography>
          <Typography paragraph>
            <Link
              component="button"
              color="secondary"
              onClick={() => {
                props.showDetails();
              }}
            >
              Read more &gt;&gt;
            </Link>
          </Typography>
        </CardContent>
        <CardFooter>
          <ToggleButtonGroup
            value={props.thumbsState}
            size="small"
            color={
              props.thumbsState === ThumbsState.THUMBS_UP
                ? 'secondary'
                : 'error'
            }
            exclusive
            onChange={(event, newValue) => {
              props.updateProject({
                thumbsState: newValue ?? ThumbsState.UNSET_THUMBS_STATE,
              });
              if (!getValues().feedback) {
                setFocus('feedback');
              }
            }}
          >
            <ToggleButton value={ThumbsState.THUMBS_UP}>
              <ThumbUpAlt fontSize="small" sx={{mr: 1}} /> Like
            </ToggleButton>
            <ToggleButton value={ThumbsState.THUMBS_DOWN}>
              <ThumbDownAlt fontSize="small" sx={{mr: 1}} /> Dislike
            </ToggleButton>
          </ToggleButtonGroup>
          <Form onSubmit={onSubmit}>
            <TextField
              label="Feedback"
              variant="outlined"
              size="small"
              InputLabelProps={{
                shrink: true,
              }}
              fullWidth
              placeholder={helperText}
              {...register('feedback')}
            />
            <Button type="submit" disabled={!isDirty} variant="outlined">
              Submit
            </Button>
          </Form>
        </CardFooter>
      </Card>
    </>
  );
}
