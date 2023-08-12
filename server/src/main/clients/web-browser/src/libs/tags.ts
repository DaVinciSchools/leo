import {RegularBreakpoints} from '@mui/material/Grid/Grid';

export function addClassName(
  tagProps: {className?: string},
  additionalClassNames: string
) {
  const newTagProps = {...tagProps};
  newTagProps.className = (
    additionalClassNames +
    ' ' +
    (newTagProps.className ?? '')
  ).trim();
  return newTagProps;
}

export function spread(breakpoints: RegularBreakpoints) {
  const newBreakpoints = {...breakpoints};
  newBreakpoints.xs =
    breakpoints.xs ??
    breakpoints.sm ??
    breakpoints.md ??
    breakpoints.lg ??
    breakpoints.xl;
  newBreakpoints.sm =
    breakpoints.sm ?? breakpoints.md ?? breakpoints.lg ?? breakpoints.xl;
  newBreakpoints.md = breakpoints.md ?? breakpoints.lg ?? breakpoints.xl;
  newBreakpoints.lg = breakpoints.lg ?? breakpoints.xl;
  newBreakpoints.xl = breakpoints.xl;
  return newBreakpoints;
}
