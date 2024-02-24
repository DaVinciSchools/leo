import './ProjectCard.scss';

import {
  Button,
  Card,
  CardActions,
  CardContent,
  Dialog,
  Divider,
  IconButton,
  Paper,
  TextField,
  ToggleButton,
  ToggleButtonGroup,
  Typography,
} from '@mui/material';
import {useContext, useEffect, useRef, useState} from 'react';
import {pl_types} from 'pl-pb';
import {DataObject, Info, ThumbDownAlt, ThumbUpAlt} from '@mui/icons-material';
import {GlobalStateContext} from '../GlobalState';
import {styled} from 'styled-components';
import {SubmitHandler, useForm} from 'react-hook-form';
import IProject = pl_types.IProject;
import ThumbsState = pl_types.Project.ThumbsState;

const ActivateButton = styled(Button)`
  margin-left: auto;
`;

const Form = styled.form`
  display: flex;
  gap: ${props => props.theme.spacing(2)};
  align-items: flex-start;
`;

const CardHeader = styled.div`
  display: flex;
  align-items: flex-start;

  h2 {
    flex-grow: 1;
  }
`;

const StyledDialog = styled(Paper)`
  padding: ${props => props.theme.spacing(4)};
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
  aiPrompt?: string | null | undefined;
  aiResponse?: string | null | undefined;
}) {
  const [showAiPrompt, setShowAiPrompt] = useState(false);
  const global = useContext(GlobalStateContext);
  const userX = global.optionalUserX();

  const {
    register,
    formState: {isDirty, isSubmitSuccessful},
    handleSubmit,
    reset,
    setFocus,
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
              <Info />
            </IconButton>
            {!!userX?.viewAiPrompts && !!props.aiPrompt && (
              <IconButton
                onClick={() => {
                  setShowAiPrompt(true);
                }}
                size="small"
              >
                <DataObject />
              </IconButton>
            )}
          </CardHeader>
          <Typography paragraph>{props.shortDescr}</Typography>
          <Form onSubmit={onSubmit}>
            <TextField
              label="Feedback"
              variant="outlined"
              size="small"
              fullWidth
              InputLabelProps={{
                shrink: true,
              }}
              placeholder={helperText}
              {...register('feedback')}
            />
            <Button type="submit" disabled={!isDirty} variant="outlined">
              Submit
            </Button>
          </Form>
        </CardContent>
        <CardActions disableSpacing>
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
              setFocus('feedback');
            }}
          >
            <ToggleButton value={ThumbsState.THUMBS_UP}>
              <ThumbUpAlt fontSize="small" sx={{mr: 1}} /> Like
            </ToggleButton>
            <ToggleButton value={ThumbsState.THUMBS_DOWN}>
              <ThumbDownAlt fontSize="small" sx={{mr: 1}} /> Dislike
            </ToggleButton>
          </ToggleButtonGroup>
          <ActivateButton
            onClick={() => {
              props.updateProject({
                active: !props.active,
              });
              setFocus('feedback');
            }}
            variant={props.active ? 'text' : 'contained'}
          >
            {props.active ? 'Deactivate' : 'Activate'}
          </ActivateButton>
        </CardActions>
      </Card>
      <Dialog
        open={showAiPrompt}
        onClose={() => setShowAiPrompt(false)}
        PaperComponent={StyledDialog}
      >
        {props.aiResponse}
      </Dialog>
    </>
  );
}
