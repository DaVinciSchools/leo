import './TitledPaper.scss';

import {Box, Paper, PaperProps} from '@mui/material';
import {PropsWithChildren, ReactNode} from 'react';

export function TitledPaper(
  props: PropsWithChildren<
    {
      title: ReactNode;
      icon?: ReactNode;
      highlightColor: string;
      draggable?: boolean;
    } & PaperProps
  >
) {
  return (
    <>
      <Paper
        square
        className="titled-paper"
        style={{
          borderBottom: `${props.highlightColor} solid 2px`,
          display: 'flex',
          flexFlow: 'column nowrap',
          height: '100%',
          overflow: 'hidden',
          width: '100%',
        }}
        {...props}
      >
        <Box
          padding={0.5}
          paddingX={1}
          className="titled-paper-title"
          style={{
            cursor: props.draggable === true ? 'move' : undefined,
          }}
        >
          <span>{props.title}</span>
          <span style={{color: props.highlightColor}}>{props.icon ?? ''}</span>
        </Box>
        <Box padding={0.5} paddingX={1}>
          {props.children}
        </Box>
      </Paper>
    </>
  );
}
