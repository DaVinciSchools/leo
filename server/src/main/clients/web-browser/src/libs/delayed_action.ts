/* eslint-disable @typescript-eslint/no-explicit-any */

import {useRef} from 'react';
import PromiseQueue from './PromiseQueue';

enum DelayedState {
  NONE,
  EXECUTING_DELAYED_ACTION,
}

export interface DelayedAction {
  trigger: () => void;
  forceDelayedAction: (onFinished: () => Promise<any> | void) => void;
}

export function useDelayedAction(
  onAction: () => void,
  onDelayed: () => Promise<any> | void,
  delayMs: number
) {
  const delayTimeout = useRef<NodeJS.Timeout>();
  const state = useRef(DelayedState.NONE);
  const actionTriggered = useRef(false);
  const promiseQueue = useRef<PromiseQueue>(new PromiseQueue());

  function trigger() {
    doTrigger();
  }

  function forceDelayedAction(onFinish: () => void) {
    doTrigger(onFinish);
  }

  function doTrigger(onFinish?: () => Promise<any> | void) {
    try {
      onAction();
    } catch (err) {
      // Ignore any errors.
    }

    if (!onFinish && state.current !== DelayedState.NONE) {
      actionTriggered.current = true;
      return;
    }

    clearTimeout(delayTimeout.current);
    delayTimeout.current = undefined;

    const newPromise = () =>
      Promise.resolve()
        .then(() => {
          state.current = DelayedState.EXECUTING_DELAYED_ACTION;
          actionTriggered.current = false;
          onDelayed();
        })
        .finally(async () => {
          if (onFinish) {
            return onFinish();
          }
        })
        .finally(() => {
          state.current = DelayedState.NONE;
          if (actionTriggered.current) {
            doTrigger();
          }
        });

    if (onFinish) {
      promiseQueue.current.enqueue(newPromise);
    } else {
      delayTimeout.current = setTimeout(() => {
        promiseQueue.current.enqueue(newPromise);
      }, delayMs);
    }
  }

  return {
    trigger,
    forceDelayedAction,
  };
}
