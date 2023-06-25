import './TitledPaper.scss';

import {Box, Paper, PaperProps} from '@mui/material';
import {PropsWithChildren, ReactNode} from 'react';
import {
  persistedGridDraggable,
  persistedGridNotDraggable,
} from '../PersistedReactGridLayout/PersistedReactGridLayout';

export function TitledPaper(
  props: PropsWithChildren<{
    title: ReactNode;
    icon?: ReactNode;
    highlightColor: string;
    draggable?: boolean;
    paperProps?: PaperProps;
  }>
) {
  return (
    <>
      <Paper
        square
        className="titled-paper"
        elevation={props.paperProps?.elevation ?? 1}
        style={{
          borderBottom: `${props.highlightColor} solid 2px`,
          display: 'flex',
          flexFlow: 'column nowrap',
          height: '100%',
          overflow: 'hidden',
          width: '100%',
        }}
        {...(props.paperProps ?? {})}
      >
        <Box
          paddingX={1}
          paddingY={0.5}
          className={'titled-paper-title ' + persistedGridDraggable}
          style={{
            cursor: props.draggable === true ? 'move' : undefined,
            background: `
              linear-gradient(90deg, transparent 0px, #fffffff2 0px),
              ${props.highlightColor}`,
          }}
        >
          <span>{props.title}</span>
          <span style={{color: `${props.highlightColor}`}}>
            {props.icon ?? ''}
          </span>
        </Box>
        <Box paddingX={1} paddingY={0.5} className={persistedGridNotDraggable}>
          <div style={{width: '100%', height: '100%'}}>{props.children}</div>
        </Box>
      </Paper>
    </>
  );
}
