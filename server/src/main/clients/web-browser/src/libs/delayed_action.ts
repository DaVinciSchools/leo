/* eslint-disable @typescript-eslint/no-explicit-any */

import {MutableRefObject, useRef, useState} from 'react';
import PromiseQueue from './PromiseQueue';

enum DelayedState {
  NONE,
  EXECUTING_DELAYED_ACTION,
}

export interface DelayedAction {
  trigger: () => void;
  forceDelayedAction: (onFinished: () => void) => void;
}

export function useDelayedAction(
  onAction: () => void,
  onDelayed: () => Promise<any> | void,
  delayMs: number
) {
  return DelayedActionImpl.useDelayedAction(onAction, onDelayed, delayMs);
}

class DelayedActionImpl implements DelayedAction {
  onAction?: () => void;
  onDelayed?: () => Promise<any> | void;
  delayMs?: number;
  delayTimeout?: MutableRefObject<NodeJS.Timeout | undefined>;
  state?: MutableRefObject<DelayedState>;
  actionTriggered?: MutableRefObject<boolean>;
  promiseQueue?: MutableRefObject<PromiseQueue>;

  static useDelayedAction<T>(
    onAction: () => void,
    onDelayed: () => Promise<T> | void,
    delayMs: number
  ): DelayedAction {
    const delayedAction = useState(new DelayedActionImpl())[0];
    delayedAction.onAction = onAction;
    delayedAction.onDelayed = onDelayed;
    delayedAction.delayMs = delayMs;
    delayedAction.delayTimeout = useRef<NodeJS.Timeout>();
    delayedAction.state = useRef(DelayedState.NONE);
    delayedAction.actionTriggered = useRef(false);
    delayedAction.promiseQueue = useRef<PromiseQueue>(new PromiseQueue());
    return delayedAction;
  }

  timedOut(retrigger: boolean) {
    this.state!.current = DelayedState.EXECUTING_DELAYED_ACTION;
    this.actionTriggered!.current = false;

    this.promiseQueue!.current.enqueue(() =>
      new Promise<void>(resolve => resolve())
        .then(() => this.onDelayed!())
        .finally(() => {
          this.state!.current = DelayedState.NONE;
          if (this.actionTriggered!.current) {
            this.actionTriggered!.current = false;
            if (retrigger) {
              this.trigger();
            }
          }
        })
    );
  }

  trigger() {
    this.onAction!();

    if (this.state!.current !== DelayedState.NONE) {
      this.actionTriggered!.current = true;
      return;
    }

    clearTimeout(this.delayTimeout!.current);
    this.delayTimeout!.current = undefined;

    this.delayTimeout!.current = setTimeout(() => {
      this.timedOut(true);
    }, this.delayMs);
  }

  forceDelayedAction(onFinished: () => void) {
    this.onAction!();

    clearTimeout(this.delayTimeout!.current);
    this.delayTimeout!.current = undefined;

    this.promiseQueue!.current.enqueue(() =>
      new Promise<void>(resolve => resolve())
        .then(() => this.timedOut(false))
        .finally(() => onFinished())
    );
  }
}
