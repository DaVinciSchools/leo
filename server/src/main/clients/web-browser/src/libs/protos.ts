import {Message, Method, rpc, RPCImpl, RPCImplCallback} from 'protobufjs';
import {addXsrfHeader} from './authentication';
import {error_service} from '../generated/protobuf-js';
import {REPORT_ERROR_REQUEST_PROP_NAME} from './HandleError/HandleError';
import IReportErrorRequest = error_service.IReportErrorRequest;

export * from '../generated/protobuf-js';

const memoizedServices: Map<string, unknown> = new Map();

export function createService<Service>(
  serviceClass: {
    create: (
      rpcImpl: RPCImpl,
      requestDelimited?: boolean,
      responseDelimited?: boolean
    ) => Service;
  },
  serviceName: string
): Service {
  let service = memoizedServices.get(serviceName) as Service | undefined;
  if (service === undefined) {
    service = createNewService(serviceClass, serviceName);
    memoizedServices.set(serviceName, service);
  }
  return service;
}

function createNewService<Service>(
  serviceClass: {
    create: (
      rpcImpl: RPCImpl,
      requestDelimited?: boolean,
      responseDelimited?: boolean
    ) => Service;
  },
  serviceName: string
): Service {
  const pathPrefix = `/api/protos/${serviceName}/`;

  const rpcImpl: RPCImpl = function (
    method: Method | rpc.ServiceMethod<Message<{}>, Message<{}>>,
    requestData: Uint8Array,
    callback: RPCImplCallback
  ) {
    const currentUrl = new URL(window.location.href);
    const fullUrl = currentUrl.origin + pathPrefix + method.name;
    let fetchResponse: Response | undefined;
    let fetchResponseBody: Uint8Array | undefined;
    fetch(fullUrl, {
      body: requestData,
      cache: 'no-cache',
      credentials: 'include',
      headers: addXsrfHeader({
        'Content-Type': 'application/x-protobuf',
        'Response-Type': 'application/x-protobuf',
      }),
      method: 'POST',
      mode: 'same-origin',
      redirect: 'follow',
    })
      .then((response: Response) => {
        fetchResponse = response;
        if (
          !response.ok ||
          !(response.headers.get('Content-Type') ?? '').startsWith(
            'application/x-protobuf'
          )
        ) {
          const error = new Error();
          error.name = '';
          error.message = '';
          error.stack = '';
          throw error;
        }
        return response;
      })
      .then(response => response.arrayBuffer())
      .then(buffer => (fetchResponseBody = new Uint8Array(buffer)))
      .then(bytes => callback(null, bytes))
      .catch(reason => {
        if (!(reason instanceof Error)) {
          reason = new Error(JSON.stringify(reason));
        }

        if (fetchResponse) {
          const headers: string[] = [];
          fetchResponse.headers.forEach((value, key) =>
            headers.push(key + ' = ' + JSON.stringify(value))
          );

          const error = new Error(
            `From: ${window.location.href}
  To: ${fullUrl}
 Via: ${fetchResponse.url}
Headers:
${headers.sort().join('\n')}` +
              (reason.message && '\nOriginal Message: ' + reason.message)
          );

          error.name =
            (reason.name ? reason.name + ': ' : '') +
            `HTTP ${fetchResponse.status}${
              fetchResponse.statusText && '- ' + fetchResponse.statusText
            }`;

          error.stack = (reason.stack ?? '')
            .replace(reason.name ?? '', '[name]')
            .replace(reason.message ?? '', '[message]');

          reason = error;
        }

        const reportErrorRequest: IReportErrorRequest = {
          name: reason.name,
          message: reason.message,
          stack: (reason.stack ?? '')
            .replace(reason.name ?? '', '[name]')
            .replace(reason.message ?? '', '[message]'),
          url: window.location.href,
          request: {
            url: fullUrl,
            body: requestData,
          },
          response: {
            status: fetchResponse?.status,
            statusText: fetchResponse?.statusText,
            url: fetchResponse?.url,
            body: fetchResponseBody,
          },
        };
        (reason as {} as {[key: string]: IReportErrorRequest})[
          REPORT_ERROR_REQUEST_PROP_NAME
        ] = reportErrorRequest;
        callback(reason);
      });
  };

  return serviceClass.create(rpcImpl, false, false);
}
