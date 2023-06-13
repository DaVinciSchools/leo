const MESSAGE_FIELD = 'message' as keyof unknown;

export function errorMessage(e: unknown) {
  if (e != null) {
    if (typeof e === 'object' && 'message' in e) {
      return '' + e[MESSAGE_FIELD];
    }
  }
  return '' + e;
}
