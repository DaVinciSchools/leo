import {Message, Method, rpc, RPCImpl, RPCImplCallback} from 'protobufjs';
import {addXsrfHeader} from './authentication';

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
    fetch(currentUrl.origin + pathPrefix + method.name, {
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
      .then((response: Response) => checkResponse(response))
      .then((response: Response) => response.arrayBuffer())
      .then((buffer: ArrayBuffer) => new Uint8Array(buffer))
      .then((array: Uint8Array) => callback(null, array))
      .catch((error: unknown) => callback(new Error(JSON.stringify(error))));
  };

  return serviceClass.create(rpcImpl, false, false);
}

function checkResponse(response: Response): Response {
  if (!response.ok) {
    throw new Error(`HTTP ${response.status} - ${response.statusText}`);
  } else if (!response.body) {
    throw new Error(`HTTP ${response.status} - Response is missing its body`);
  }
  return response;
}
