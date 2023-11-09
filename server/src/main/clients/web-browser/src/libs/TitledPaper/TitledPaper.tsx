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
  DRAGGABLE_CLASS_NAME,
  NOT_DRAGGABLE_CLASS_NAME,
} from '../PersistedReactGridLayout/PersistedReactGridLayout';
import {Property} from 'csstype';

export const TitledPaper = forwardRef(
  (
    props: PropsWithChildren<{
      title: ReactNode;
      icon?: ReactNode;
      headerColor?: string;
      highlightColor: string;
      draggableCursorType?: Property.Cursor;
      paperProps?: PaperProps;
      titleStyle?: Partial<CSSProperties>;
      bodyStyle?: Partial<CSSProperties>;
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
            },
            props.titleStyle ?? {}
          )}
          {...(props.paperProps ?? {})}
          ref={ref}
          tabIndex={props.tabIndex}
        >
          <Box
            paddingX={1}
            paddingY={0.5}
            className={'titled-paper-title ' + DRAGGABLE_CLASS_NAME}
            style={{
              cursor: props.draggableCursorType,
              background:
                props.headerColor ??
                `
              linear-gradient(90deg, transparent 0px, #fffffff2 0px),
              ${props.highlightColor}`,
            }}
          >
            <span>{props.title}</span>
            <span
              className={NOT_DRAGGABLE_CLASS_NAME}
              style={{
                color: props.highlightColor,
                display: props.icon ? 'block' : 'none',
              }}
            >
              {props.icon}
            </span>
          </Box>
          <Box
            paddingX={1}
            paddingY={0.5}
            className={'titled-paper-body ' + NOT_DRAGGABLE_CLASS_NAME}
            style={props.bodyStyle}
          >
            {props.children}
          </Box>
        </Paper>
      </>
    );
  }
);
