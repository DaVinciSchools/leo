import './TitledPaper.scss';

import {Box, Paper, PaperProps} from '@mui/material';
import {
  CSSProperties,
  ForwardedRef,
  forwardRef,
  PropsWithChildren,
  ReactNode,
} from 'react';
import {
  persistedGridDraggable,
  persistedGridNotDraggable,
} from '../PersistedReactGridLayout/PersistedReactGridLayout';

export const TitledPaper = forwardRef(
  (
    props: PropsWithChildren<{
      title: ReactNode;
      icon?: ReactNode;
      headerColor?: string;
      highlightColor: string;
      draggable?: boolean;
      paperProps?: PaperProps;
      style?: Partial<CSSProperties>;
      tabIndex?: number;
    }>,
    ref: ForwardedRef<HTMLDivElement>
  ) => {
    return (
      <>
        <Paper
          square
          className="titled-paper"
          elevation={props.paperProps?.elevation ?? 1}
          style={Object.assign(
            {},
            {
              borderBottom: `${props.highlightColor} solid 2px`,
              display: 'flex',
              flexFlow: 'column nowrap',
              height: '100%',
              overflow: 'hidden',
              width: '100%',
            },
            props.style ?? {}
          )}
          {...(props.paperProps ?? {})}
          ref={ref}
          tabIndex={props.tabIndex}
        >
          <Box
            paddingX={1}
            paddingY={0.5}
            className={'titled-paper-title ' + persistedGridDraggable}
            style={{
              cursor: props.draggable === true ? 'move' : undefined,
              background:
                props.headerColor ??
                `
              linear-gradient(90deg, transparent 0px, #fffffff2 0px),
              ${props.highlightColor}`,
              boxSizing: 'border-box',
            }}
          >
            <span>{props.title}</span>
            <span style={{color: `${props.highlightColor}`}}>
              {props.icon ?? ''}
            </span>
          </Box>
          <Box
            paddingX={1}
            paddingY={0.5}
            className={persistedGridNotDraggable}
            style={{width: '100%', height: '100%', boxSizing: 'border-box'}}
          >
            {props.children}
          </Box>
        </Paper>
      </>
    );
  }
);
