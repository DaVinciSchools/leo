import {Message, Method, rpc, RPCImpl, RPCImplCallback} from 'protobufjs';
import {REPORT_ERROR_REQUEST_PROP_NAME} from './HandleError/HandleError';
import {addXsrfHeader} from './authentication';
import {error_service} from 'pl-pb';
import {genericJsonReplacer} from './misc';
import IReportErrorRequest = error_service.IReportErrorRequest;

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
        if (!response.ok) {
          throw new Error('Unexpected status code: ' + response.status);
        }
        if (
          !(response.headers.get('Content-Type') ?? '').startsWith(
            'application/x-protobuf'
          )
        ) {
          throw new Error(
            'Unexpected content type: ' + response.headers.get('Content-Type')
          );
        }
        return response;
      })
      .then(response => response.arrayBuffer())
      .then(buffer => (fetchResponseBody = new Uint8Array(buffer)))
      .then(bytes => callback(null, bytes))
      .catch(error => {
        if (!(error instanceof Error)) {
          error = new Error(
            'Unexpected error: ' + JSON.stringify(error, genericJsonReplacer)
          );
        }

        (error as {} as {[key: string]: IReportErrorRequest})[
          REPORT_ERROR_REQUEST_PROP_NAME
        ] = {
          name: error.name,
          error: JSON.stringify(error, genericJsonReplacer, 2),
          sourceUrl: window.location.href,
          requestBody: requestData,
          requestUrl: fullUrl,
          response: JSON.stringify(fetchResponse, genericJsonReplacer, 2),
          responseUrl: fetchResponse?.url ?? 'Unknown',
          responseBody: fetchResponseBody,
        };

        callback(error);
      });
  };

  return serviceClass.create(rpcImpl, false, false);
}
