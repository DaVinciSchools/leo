/* eslint-disable  @typescript-eslint/no-explicit-any */

interface PromiseQueueEntry {
  promiseProvider: () => Promise<any>;
  token: any | undefined;
}

export default class PromiseQueue {
  private promiseQueue: PromiseQueueEntry[] = [];

  private runningPromise = false;
  private lastToken: any | undefined;

  enqueue(promiseProvider: () => Promise<any>, token?: any) {
    this.promiseQueue.push({promiseProvider, token});
    this.dequeue();
  }

  private dequeue() {
    if (this.runningPromise) {
      return false;
    }

    let entry: PromiseQueueEntry | undefined;
    do {
      entry = this.promiseQueue.shift();
      if (!entry) {
        return false;
      }
    } while (
      entry.token != null &&
      this.lastToken != null &&
      entry.token === this.lastToken
    );
    this.lastToken = entry.token;

    try {
      this.runningPromise = true;
      entry.promiseProvider().finally(() => {
        this.runningPromise = false;
        this.dequeue();
      });
    } catch (err) {
      console.log(err);
      this.runningPromise = false;
      this.dequeue();
    }

    return true;
  }
}
