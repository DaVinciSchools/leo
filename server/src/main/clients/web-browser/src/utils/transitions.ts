export function lateTransition(
  untilFraction: number
): (fraction: number) => number {
  return (fraction: number): number => {
    if (fraction < untilFraction) {
      return 0;
    } else {
      return (fraction - untilFraction) / (1 - untilFraction);
    }
  };
}

export function earlyTransition(
  untilFraction: number
): (fraction: number) => number {
  return (fraction: number): number => {
    if (fraction < untilFraction) {
      return (fraction - (1 - untilFraction)) / untilFraction;
    } else {
      return 1;
    }
  };
}

export function overshootTransition(
  expandToFraction: number,
  shrinkAfterFraction: number
): (fraction: number) => number {
  return (fraction: number) => {
    if (fraction < shrinkAfterFraction) {
      return (fraction / shrinkAfterFraction) * expandToFraction;
    } else {
      return (
        expandToFraction -
        ((expandToFraction - 1) * (fraction - shrinkAfterFraction)) /
          (1 - shrinkAfterFraction)
      );
    }
  };
}

export function doTransition(
  durationMs: number,
  ...transitions: {
    setFn: (value: number) => void;
    begin: number;
    end: number;
    fractionFn?: (fractionComplete: number) => number;
  }[]
): Promise<void> {
  const startTime = Date.now();
  const endTime = startTime + durationMs;

  if (durationMs === 0) {
    for (const transition of transitions) {
      transition.setFn(transition.end);
    }
    return Promise.resolve();
  }

  // Initialize values.
  for (const transition of transitions) {
    transition.setFn(transition.begin);
  }

  return new Promise<void>(resolve => {
    const doTransitions = () => {
      // Calculate the fraction we are between startTime and startTime + durationMs
      const now = Date.now();
      const fractionComplete = (now - startTime) / durationMs;

      // Set all transition values to the fraction between start and end values.
      if (now < endTime) {
        for (const transition of transitions) {
          const adjustedFractionComplete = transition.fractionFn
            ? transition.fractionFn(fractionComplete)
            : fractionComplete;
          transition.setFn(
            transition.begin +
              (transition.end - transition.begin) * adjustedFractionComplete
          );
        }

        // Keep doing this until fractionComplete >= 1.
        setTimeout(doTransitions, /* 60fps */ 16);
        return;
      }

      // We've finished duration. So, finialize all the values to the end value.
      for (const transition of transitions) {
        transition.setFn(transition.end);
      }
      resolve();
    };

    // Start the transition loop.
    doTransitions();
  });
}
