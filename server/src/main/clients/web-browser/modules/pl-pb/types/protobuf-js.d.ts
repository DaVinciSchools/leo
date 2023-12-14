import * as $protobuf from "protobufjs";
import Long = require("long");
/** Namespace assignment_management. */
export namespace assignment_management {

    /** Represents an AssignmentManagementService */
    class AssignmentManagementService extends $protobuf.rpc.Service {

        /**
         * Constructs a new AssignmentManagementService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new AssignmentManagementService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): AssignmentManagementService;

        /**
         * Calls GetAssignments.
         * @param request GetAssignmentsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetAssignmentsResponse
         */
        public getAssignments(request: assignment_management.IGetAssignmentsRequest, callback: assignment_management.AssignmentManagementService.GetAssignmentsCallback): void;

        /**
         * Calls GetAssignments.
         * @param request GetAssignmentsRequest message or plain object
         * @returns Promise
         */
        public getAssignments(request: assignment_management.IGetAssignmentsRequest): Promise<assignment_management.GetAssignmentsResponse>;

        /**
         * Calls CreateAssignment.
         * @param request CreateAssignmentRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and CreateAssignmentResponse
         */
        public createAssignment(request: assignment_management.ICreateAssignmentRequest, callback: assignment_management.AssignmentManagementService.CreateAssignmentCallback): void;

        /**
         * Calls CreateAssignment.
         * @param request CreateAssignmentRequest message or plain object
         * @returns Promise
         */
        public createAssignment(request: assignment_management.ICreateAssignmentRequest): Promise<assignment_management.CreateAssignmentResponse>;

        /**
         * Calls SaveAssignment.
         * @param request SaveAssignmentRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and SaveAssignmentResponse
         */
        public saveAssignment(request: assignment_management.ISaveAssignmentRequest, callback: assignment_management.AssignmentManagementService.SaveAssignmentCallback): void;

        /**
         * Calls SaveAssignment.
         * @param request SaveAssignmentRequest message or plain object
         * @returns Promise
         */
        public saveAssignment(request: assignment_management.ISaveAssignmentRequest): Promise<assignment_management.SaveAssignmentResponse>;

        /**
         * Calls DeleteAssignment.
         * @param request DeleteAssignmentRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and DeleteAssignmentResponse
         */
        public deleteAssignment(request: assignment_management.IDeleteAssignmentRequest, callback: assignment_management.AssignmentManagementService.DeleteAssignmentCallback): void;

        /**
         * Calls DeleteAssignment.
         * @param request DeleteAssignmentRequest message or plain object
         * @returns Promise
         */
        public deleteAssignment(request: assignment_management.IDeleteAssignmentRequest): Promise<assignment_management.DeleteAssignmentResponse>;
    }

    namespace AssignmentManagementService {

        /**
         * Callback as used by {@link assignment_management.AssignmentManagementService#getAssignments}.
         * @param error Error, if any
         * @param [response] GetAssignmentsResponse
         */
        type GetAssignmentsCallback = (error: (Error|null), response?: assignment_management.GetAssignmentsResponse) => void;

        /**
         * Callback as used by {@link assignment_management.AssignmentManagementService#createAssignment}.
         * @param error Error, if any
         * @param [response] CreateAssignmentResponse
         */
        type CreateAssignmentCallback = (error: (Error|null), response?: assignment_management.CreateAssignmentResponse) => void;

        /**
         * Callback as used by {@link assignment_management.AssignmentManagementService#saveAssignment}.
         * @param error Error, if any
         * @param [response] SaveAssignmentResponse
         */
        type SaveAssignmentCallback = (error: (Error|null), response?: assignment_management.SaveAssignmentResponse) => void;

        /**
         * Callback as used by {@link assignment_management.AssignmentManagementService#deleteAssignment}.
         * @param error Error, if any
         * @param [response] DeleteAssignmentResponse
         */
        type DeleteAssignmentCallback = (error: (Error|null), response?: assignment_management.DeleteAssignmentResponse) => void;
    }

    /** Properties of a GetAssignmentsRequest. */
    interface IGetAssignmentsRequest {

        /** GetAssignmentsRequest teacherId */
        teacherId?: (number|null);

        /** GetAssignmentsRequest studentId */
        studentId?: (number|null);

        /** GetAssignmentsRequest assignmentIds */
        assignmentIds?: (number[]|null);

        /** GetAssignmentsRequest includeProjectDefinitions */
        includeProjectDefinitions?: (boolean|null);
    }

    /** Represents a GetAssignmentsRequest. */
    class GetAssignmentsRequest implements IGetAssignmentsRequest {

        /**
         * Constructs a new GetAssignmentsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: assignment_management.IGetAssignmentsRequest);

        /** GetAssignmentsRequest teacherId. */
        public teacherId?: (number|null);

        /** GetAssignmentsRequest studentId. */
        public studentId?: (number|null);

        /** GetAssignmentsRequest assignmentIds. */
        public assignmentIds: number[];

        /** GetAssignmentsRequest includeProjectDefinitions. */
        public includeProjectDefinitions?: (boolean|null);

        /** GetAssignmentsRequest _teacherId. */
        public _teacherId?: "teacherId";

        /** GetAssignmentsRequest _studentId. */
        public _studentId?: "studentId";

        /** GetAssignmentsRequest _includeProjectDefinitions. */
        public _includeProjectDefinitions?: "includeProjectDefinitions";

        /**
         * Creates a new GetAssignmentsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetAssignmentsRequest instance
         */
        public static create(properties?: assignment_management.IGetAssignmentsRequest): assignment_management.GetAssignmentsRequest;

        /**
         * Encodes the specified GetAssignmentsRequest message. Does not implicitly {@link assignment_management.GetAssignmentsRequest.verify|verify} messages.
         * @param message GetAssignmentsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: assignment_management.IGetAssignmentsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetAssignmentsRequest message, length delimited. Does not implicitly {@link assignment_management.GetAssignmentsRequest.verify|verify} messages.
         * @param message GetAssignmentsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: assignment_management.IGetAssignmentsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetAssignmentsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetAssignmentsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): assignment_management.GetAssignmentsRequest;

        /**
         * Decodes a GetAssignmentsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetAssignmentsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): assignment_management.GetAssignmentsRequest;

        /**
         * Verifies a GetAssignmentsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetAssignmentsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetAssignmentsRequest
         */
        public static fromObject(object: { [k: string]: any }): assignment_management.GetAssignmentsRequest;

        /**
         * Creates a plain object from a GetAssignmentsRequest message. Also converts values to other types if specified.
         * @param message GetAssignmentsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: assignment_management.GetAssignmentsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetAssignmentsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetAssignmentsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetAssignmentsResponse. */
    interface IGetAssignmentsResponse {

        /** GetAssignmentsResponse classXs */
        classXs?: (pl_types.IClassX[]|null);

        /** GetAssignmentsResponse assignments */
        assignments?: (pl_types.IAssignment[]|null);
    }

    /** Represents a GetAssignmentsResponse. */
    class GetAssignmentsResponse implements IGetAssignmentsResponse {

        /**
         * Constructs a new GetAssignmentsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: assignment_management.IGetAssignmentsResponse);

        /** GetAssignmentsResponse classXs. */
        public classXs: pl_types.IClassX[];

        /** GetAssignmentsResponse assignments. */
        public assignments: pl_types.IAssignment[];

        /**
         * Creates a new GetAssignmentsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetAssignmentsResponse instance
         */
        public static create(properties?: assignment_management.IGetAssignmentsResponse): assignment_management.GetAssignmentsResponse;

        /**
         * Encodes the specified GetAssignmentsResponse message. Does not implicitly {@link assignment_management.GetAssignmentsResponse.verify|verify} messages.
         * @param message GetAssignmentsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: assignment_management.IGetAssignmentsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetAssignmentsResponse message, length delimited. Does not implicitly {@link assignment_management.GetAssignmentsResponse.verify|verify} messages.
         * @param message GetAssignmentsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: assignment_management.IGetAssignmentsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetAssignmentsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetAssignmentsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): assignment_management.GetAssignmentsResponse;

        /**
         * Decodes a GetAssignmentsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetAssignmentsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): assignment_management.GetAssignmentsResponse;

        /**
         * Verifies a GetAssignmentsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetAssignmentsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetAssignmentsResponse
         */
        public static fromObject(object: { [k: string]: any }): assignment_management.GetAssignmentsResponse;

        /**
         * Creates a plain object from a GetAssignmentsResponse message. Also converts values to other types if specified.
         * @param message GetAssignmentsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: assignment_management.GetAssignmentsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetAssignmentsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetAssignmentsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a CreateAssignmentRequest. */
    interface ICreateAssignmentRequest {

        /** CreateAssignmentRequest classXId */
        classXId?: (number|null);
    }

    /** Represents a CreateAssignmentRequest. */
    class CreateAssignmentRequest implements ICreateAssignmentRequest {

        /**
         * Constructs a new CreateAssignmentRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: assignment_management.ICreateAssignmentRequest);

        /** CreateAssignmentRequest classXId. */
        public classXId?: (number|null);

        /** CreateAssignmentRequest _classXId. */
        public _classXId?: "classXId";

        /**
         * Creates a new CreateAssignmentRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns CreateAssignmentRequest instance
         */
        public static create(properties?: assignment_management.ICreateAssignmentRequest): assignment_management.CreateAssignmentRequest;

        /**
         * Encodes the specified CreateAssignmentRequest message. Does not implicitly {@link assignment_management.CreateAssignmentRequest.verify|verify} messages.
         * @param message CreateAssignmentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: assignment_management.ICreateAssignmentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified CreateAssignmentRequest message, length delimited. Does not implicitly {@link assignment_management.CreateAssignmentRequest.verify|verify} messages.
         * @param message CreateAssignmentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: assignment_management.ICreateAssignmentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a CreateAssignmentRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns CreateAssignmentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): assignment_management.CreateAssignmentRequest;

        /**
         * Decodes a CreateAssignmentRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns CreateAssignmentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): assignment_management.CreateAssignmentRequest;

        /**
         * Verifies a CreateAssignmentRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a CreateAssignmentRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns CreateAssignmentRequest
         */
        public static fromObject(object: { [k: string]: any }): assignment_management.CreateAssignmentRequest;

        /**
         * Creates a plain object from a CreateAssignmentRequest message. Also converts values to other types if specified.
         * @param message CreateAssignmentRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: assignment_management.CreateAssignmentRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this CreateAssignmentRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for CreateAssignmentRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a CreateAssignmentResponse. */
    interface ICreateAssignmentResponse {

        /** CreateAssignmentResponse assignment */
        assignment?: (pl_types.IAssignment|null);
    }

    /** Represents a CreateAssignmentResponse. */
    class CreateAssignmentResponse implements ICreateAssignmentResponse {

        /**
         * Constructs a new CreateAssignmentResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: assignment_management.ICreateAssignmentResponse);

        /** CreateAssignmentResponse assignment. */
        public assignment?: (pl_types.IAssignment|null);

        /** CreateAssignmentResponse _assignment. */
        public _assignment?: "assignment";

        /**
         * Creates a new CreateAssignmentResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns CreateAssignmentResponse instance
         */
        public static create(properties?: assignment_management.ICreateAssignmentResponse): assignment_management.CreateAssignmentResponse;

        /**
         * Encodes the specified CreateAssignmentResponse message. Does not implicitly {@link assignment_management.CreateAssignmentResponse.verify|verify} messages.
         * @param message CreateAssignmentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: assignment_management.ICreateAssignmentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified CreateAssignmentResponse message, length delimited. Does not implicitly {@link assignment_management.CreateAssignmentResponse.verify|verify} messages.
         * @param message CreateAssignmentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: assignment_management.ICreateAssignmentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a CreateAssignmentResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns CreateAssignmentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): assignment_management.CreateAssignmentResponse;

        /**
         * Decodes a CreateAssignmentResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns CreateAssignmentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): assignment_management.CreateAssignmentResponse;

        /**
         * Verifies a CreateAssignmentResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a CreateAssignmentResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns CreateAssignmentResponse
         */
        public static fromObject(object: { [k: string]: any }): assignment_management.CreateAssignmentResponse;

        /**
         * Creates a plain object from a CreateAssignmentResponse message. Also converts values to other types if specified.
         * @param message CreateAssignmentResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: assignment_management.CreateAssignmentResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this CreateAssignmentResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for CreateAssignmentResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a SaveAssignmentRequest. */
    interface ISaveAssignmentRequest {

        /** SaveAssignmentRequest assignment */
        assignment?: (pl_types.IAssignment|null);
    }

    /** Represents a SaveAssignmentRequest. */
    class SaveAssignmentRequest implements ISaveAssignmentRequest {

        /**
         * Constructs a new SaveAssignmentRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: assignment_management.ISaveAssignmentRequest);

        /** SaveAssignmentRequest assignment. */
        public assignment?: (pl_types.IAssignment|null);

        /** SaveAssignmentRequest _assignment. */
        public _assignment?: "assignment";

        /**
         * Creates a new SaveAssignmentRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns SaveAssignmentRequest instance
         */
        public static create(properties?: assignment_management.ISaveAssignmentRequest): assignment_management.SaveAssignmentRequest;

        /**
         * Encodes the specified SaveAssignmentRequest message. Does not implicitly {@link assignment_management.SaveAssignmentRequest.verify|verify} messages.
         * @param message SaveAssignmentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: assignment_management.ISaveAssignmentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified SaveAssignmentRequest message, length delimited. Does not implicitly {@link assignment_management.SaveAssignmentRequest.verify|verify} messages.
         * @param message SaveAssignmentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: assignment_management.ISaveAssignmentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a SaveAssignmentRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns SaveAssignmentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): assignment_management.SaveAssignmentRequest;

        /**
         * Decodes a SaveAssignmentRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns SaveAssignmentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): assignment_management.SaveAssignmentRequest;

        /**
         * Verifies a SaveAssignmentRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a SaveAssignmentRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns SaveAssignmentRequest
         */
        public static fromObject(object: { [k: string]: any }): assignment_management.SaveAssignmentRequest;

        /**
         * Creates a plain object from a SaveAssignmentRequest message. Also converts values to other types if specified.
         * @param message SaveAssignmentRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: assignment_management.SaveAssignmentRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this SaveAssignmentRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for SaveAssignmentRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a SaveAssignmentResponse. */
    interface ISaveAssignmentResponse {
    }

    /** Represents a SaveAssignmentResponse. */
    class SaveAssignmentResponse implements ISaveAssignmentResponse {

        /**
         * Constructs a new SaveAssignmentResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: assignment_management.ISaveAssignmentResponse);

        /**
         * Creates a new SaveAssignmentResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns SaveAssignmentResponse instance
         */
        public static create(properties?: assignment_management.ISaveAssignmentResponse): assignment_management.SaveAssignmentResponse;

        /**
         * Encodes the specified SaveAssignmentResponse message. Does not implicitly {@link assignment_management.SaveAssignmentResponse.verify|verify} messages.
         * @param message SaveAssignmentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: assignment_management.ISaveAssignmentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified SaveAssignmentResponse message, length delimited. Does not implicitly {@link assignment_management.SaveAssignmentResponse.verify|verify} messages.
         * @param message SaveAssignmentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: assignment_management.ISaveAssignmentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a SaveAssignmentResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns SaveAssignmentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): assignment_management.SaveAssignmentResponse;

        /**
         * Decodes a SaveAssignmentResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns SaveAssignmentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): assignment_management.SaveAssignmentResponse;

        /**
         * Verifies a SaveAssignmentResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a SaveAssignmentResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns SaveAssignmentResponse
         */
        public static fromObject(object: { [k: string]: any }): assignment_management.SaveAssignmentResponse;

        /**
         * Creates a plain object from a SaveAssignmentResponse message. Also converts values to other types if specified.
         * @param message SaveAssignmentResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: assignment_management.SaveAssignmentResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this SaveAssignmentResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for SaveAssignmentResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a DeleteAssignmentRequest. */
    interface IDeleteAssignmentRequest {

        /** DeleteAssignmentRequest assignmentId */
        assignmentId?: (number|null);
    }

    /** Represents a DeleteAssignmentRequest. */
    class DeleteAssignmentRequest implements IDeleteAssignmentRequest {

        /**
         * Constructs a new DeleteAssignmentRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: assignment_management.IDeleteAssignmentRequest);

        /** DeleteAssignmentRequest assignmentId. */
        public assignmentId?: (number|null);

        /** DeleteAssignmentRequest _assignmentId. */
        public _assignmentId?: "assignmentId";

        /**
         * Creates a new DeleteAssignmentRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns DeleteAssignmentRequest instance
         */
        public static create(properties?: assignment_management.IDeleteAssignmentRequest): assignment_management.DeleteAssignmentRequest;

        /**
         * Encodes the specified DeleteAssignmentRequest message. Does not implicitly {@link assignment_management.DeleteAssignmentRequest.verify|verify} messages.
         * @param message DeleteAssignmentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: assignment_management.IDeleteAssignmentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified DeleteAssignmentRequest message, length delimited. Does not implicitly {@link assignment_management.DeleteAssignmentRequest.verify|verify} messages.
         * @param message DeleteAssignmentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: assignment_management.IDeleteAssignmentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a DeleteAssignmentRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns DeleteAssignmentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): assignment_management.DeleteAssignmentRequest;

        /**
         * Decodes a DeleteAssignmentRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns DeleteAssignmentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): assignment_management.DeleteAssignmentRequest;

        /**
         * Verifies a DeleteAssignmentRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a DeleteAssignmentRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns DeleteAssignmentRequest
         */
        public static fromObject(object: { [k: string]: any }): assignment_management.DeleteAssignmentRequest;

        /**
         * Creates a plain object from a DeleteAssignmentRequest message. Also converts values to other types if specified.
         * @param message DeleteAssignmentRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: assignment_management.DeleteAssignmentRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this DeleteAssignmentRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for DeleteAssignmentRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a DeleteAssignmentResponse. */
    interface IDeleteAssignmentResponse {
    }

    /** Represents a DeleteAssignmentResponse. */
    class DeleteAssignmentResponse implements IDeleteAssignmentResponse {

        /**
         * Constructs a new DeleteAssignmentResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: assignment_management.IDeleteAssignmentResponse);

        /**
         * Creates a new DeleteAssignmentResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns DeleteAssignmentResponse instance
         */
        public static create(properties?: assignment_management.IDeleteAssignmentResponse): assignment_management.DeleteAssignmentResponse;

        /**
         * Encodes the specified DeleteAssignmentResponse message. Does not implicitly {@link assignment_management.DeleteAssignmentResponse.verify|verify} messages.
         * @param message DeleteAssignmentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: assignment_management.IDeleteAssignmentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified DeleteAssignmentResponse message, length delimited. Does not implicitly {@link assignment_management.DeleteAssignmentResponse.verify|verify} messages.
         * @param message DeleteAssignmentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: assignment_management.IDeleteAssignmentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a DeleteAssignmentResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns DeleteAssignmentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): assignment_management.DeleteAssignmentResponse;

        /**
         * Decodes a DeleteAssignmentResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns DeleteAssignmentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): assignment_management.DeleteAssignmentResponse;

        /**
         * Verifies a DeleteAssignmentResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a DeleteAssignmentResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns DeleteAssignmentResponse
         */
        public static fromObject(object: { [k: string]: any }): assignment_management.DeleteAssignmentResponse;

        /**
         * Creates a plain object from a DeleteAssignmentResponse message. Also converts values to other types if specified.
         * @param message DeleteAssignmentResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: assignment_management.DeleteAssignmentResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this DeleteAssignmentResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for DeleteAssignmentResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace pl_types. */
export namespace pl_types {

    /** Properties of an Assignment. */
    interface IAssignment {

        /** Assignment id */
        id?: (number|null);

        /** Assignment name */
        name?: (string|null);

        /** Assignment nickname */
        nickname?: (string|null);

        /** Assignment shortDescr */
        shortDescr?: (string|null);

        /** Assignment longDescrHtml */
        longDescrHtml?: (string|null);

        /** Assignment classX */
        classX?: (pl_types.IClassX|null);

        /** Assignment knowledgeAndSkills */
        knowledgeAndSkills?: (pl_types.IKnowledgeAndSkill[]|null);

        /** Assignment projectDefinitions */
        projectDefinitions?: (pl_types.IProjectDefinition[]|null);
    }

    /** Represents an Assignment. */
    class Assignment implements IAssignment {

        /**
         * Constructs a new Assignment.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IAssignment);

        /** Assignment id. */
        public id?: (number|null);

        /** Assignment name. */
        public name?: (string|null);

        /** Assignment nickname. */
        public nickname?: (string|null);

        /** Assignment shortDescr. */
        public shortDescr?: (string|null);

        /** Assignment longDescrHtml. */
        public longDescrHtml?: (string|null);

        /** Assignment classX. */
        public classX?: (pl_types.IClassX|null);

        /** Assignment knowledgeAndSkills. */
        public knowledgeAndSkills: pl_types.IKnowledgeAndSkill[];

        /** Assignment projectDefinitions. */
        public projectDefinitions: pl_types.IProjectDefinition[];

        /** Assignment _id. */
        public _id?: "id";

        /** Assignment _name. */
        public _name?: "name";

        /** Assignment _nickname. */
        public _nickname?: "nickname";

        /** Assignment _shortDescr. */
        public _shortDescr?: "shortDescr";

        /** Assignment _longDescrHtml. */
        public _longDescrHtml?: "longDescrHtml";

        /** Assignment _classX. */
        public _classX?: "classX";

        /**
         * Creates a new Assignment instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Assignment instance
         */
        public static create(properties?: pl_types.IAssignment): pl_types.Assignment;

        /**
         * Encodes the specified Assignment message. Does not implicitly {@link pl_types.Assignment.verify|verify} messages.
         * @param message Assignment message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IAssignment, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Assignment message, length delimited. Does not implicitly {@link pl_types.Assignment.verify|verify} messages.
         * @param message Assignment message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IAssignment, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an Assignment message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Assignment
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.Assignment;

        /**
         * Decodes an Assignment message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Assignment
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.Assignment;

        /**
         * Verifies an Assignment message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an Assignment message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Assignment
         */
        public static fromObject(object: { [k: string]: any }): pl_types.Assignment;

        /**
         * Creates a plain object from an Assignment message. Also converts values to other types if specified.
         * @param message Assignment
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.Assignment, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Assignment to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Assignment
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ClassX. */
    interface IClassX {

        /** ClassX id */
        id?: (number|null);

        /** ClassX school */
        school?: (pl_types.ISchool|null);

        /** ClassX name */
        name?: (string|null);

        /** ClassX number */
        number?: (string|null);

        /** ClassX period */
        period?: (string|null);

        /** ClassX grade */
        grade?: (string|null);

        /** ClassX shortDescr */
        shortDescr?: (string|null);

        /** ClassX longDescrHtml */
        longDescrHtml?: (string|null);

        /** ClassX assignments */
        assignments?: (pl_types.IAssignment[]|null);

        /** ClassX knowledgeAndSkills */
        knowledgeAndSkills?: (pl_types.IKnowledgeAndSkill[]|null);
    }

    /** Represents a ClassX. */
    class ClassX implements IClassX {

        /**
         * Constructs a new ClassX.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IClassX);

        /** ClassX id. */
        public id?: (number|null);

        /** ClassX school. */
        public school?: (pl_types.ISchool|null);

        /** ClassX name. */
        public name?: (string|null);

        /** ClassX number. */
        public number?: (string|null);

        /** ClassX period. */
        public period?: (string|null);

        /** ClassX grade. */
        public grade?: (string|null);

        /** ClassX shortDescr. */
        public shortDescr?: (string|null);

        /** ClassX longDescrHtml. */
        public longDescrHtml?: (string|null);

        /** ClassX assignments. */
        public assignments: pl_types.IAssignment[];

        /** ClassX knowledgeAndSkills. */
        public knowledgeAndSkills: pl_types.IKnowledgeAndSkill[];

        /** ClassX _id. */
        public _id?: "id";

        /** ClassX _school. */
        public _school?: "school";

        /** ClassX _name. */
        public _name?: "name";

        /** ClassX _number. */
        public _number?: "number";

        /** ClassX _period. */
        public _period?: "period";

        /** ClassX _grade. */
        public _grade?: "grade";

        /** ClassX _shortDescr. */
        public _shortDescr?: "shortDescr";

        /** ClassX _longDescrHtml. */
        public _longDescrHtml?: "longDescrHtml";

        /**
         * Creates a new ClassX instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ClassX instance
         */
        public static create(properties?: pl_types.IClassX): pl_types.ClassX;

        /**
         * Encodes the specified ClassX message. Does not implicitly {@link pl_types.ClassX.verify|verify} messages.
         * @param message ClassX message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IClassX, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ClassX message, length delimited. Does not implicitly {@link pl_types.ClassX.verify|verify} messages.
         * @param message ClassX message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IClassX, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ClassX message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ClassX
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.ClassX;

        /**
         * Decodes a ClassX message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ClassX
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.ClassX;

        /**
         * Verifies a ClassX message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ClassX message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ClassX
         */
        public static fromObject(object: { [k: string]: any }): pl_types.ClassX;

        /**
         * Creates a plain object from a ClassX message. Also converts values to other types if specified.
         * @param message ClassX
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.ClassX, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ClassX to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ClassX
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a District. */
    interface IDistrict {

        /** District id */
        id?: (number|null);

        /** District name */
        name?: (string|null);
    }

    /** Represents a District. */
    class District implements IDistrict {

        /**
         * Constructs a new District.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IDistrict);

        /** District id. */
        public id?: (number|null);

        /** District name. */
        public name?: (string|null);

        /** District _id. */
        public _id?: "id";

        /** District _name. */
        public _name?: "name";

        /**
         * Creates a new District instance using the specified properties.
         * @param [properties] Properties to set
         * @returns District instance
         */
        public static create(properties?: pl_types.IDistrict): pl_types.District;

        /**
         * Encodes the specified District message. Does not implicitly {@link pl_types.District.verify|verify} messages.
         * @param message District message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IDistrict, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified District message, length delimited. Does not implicitly {@link pl_types.District.verify|verify} messages.
         * @param message District message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IDistrict, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a District message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns District
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.District;

        /**
         * Decodes a District message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns District
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.District;

        /**
         * Verifies a District message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a District message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns District
         */
        public static fromObject(object: { [k: string]: any }): pl_types.District;

        /**
         * Creates a plain object from a District message. Also converts values to other types if specified.
         * @param message District
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.District, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this District to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for District
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a KnowledgeAndSkill. */
    interface IKnowledgeAndSkill {

        /** KnowledgeAndSkill id */
        id?: (number|null);

        /** KnowledgeAndSkill type */
        type?: (pl_types.KnowledgeAndSkill.Type|null);

        /** KnowledgeAndSkill name */
        name?: (string|null);

        /** KnowledgeAndSkill category */
        category?: (string|null);

        /** KnowledgeAndSkill shortDescr */
        shortDescr?: (string|null);

        /** KnowledgeAndSkill longDescrHtml */
        longDescrHtml?: (string|null);

        /** KnowledgeAndSkill global */
        global?: (boolean|null);

        /** KnowledgeAndSkill userX */
        userX?: (pl_types.IUserX|null);
    }

    /** Represents a KnowledgeAndSkill. */
    class KnowledgeAndSkill implements IKnowledgeAndSkill {

        /**
         * Constructs a new KnowledgeAndSkill.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IKnowledgeAndSkill);

        /** KnowledgeAndSkill id. */
        public id?: (number|null);

        /** KnowledgeAndSkill type. */
        public type?: (pl_types.KnowledgeAndSkill.Type|null);

        /** KnowledgeAndSkill name. */
        public name?: (string|null);

        /** KnowledgeAndSkill category. */
        public category?: (string|null);

        /** KnowledgeAndSkill shortDescr. */
        public shortDescr?: (string|null);

        /** KnowledgeAndSkill longDescrHtml. */
        public longDescrHtml?: (string|null);

        /** KnowledgeAndSkill global. */
        public global?: (boolean|null);

        /** KnowledgeAndSkill userX. */
        public userX?: (pl_types.IUserX|null);

        /** KnowledgeAndSkill _id. */
        public _id?: "id";

        /** KnowledgeAndSkill _type. */
        public _type?: "type";

        /** KnowledgeAndSkill _name. */
        public _name?: "name";

        /** KnowledgeAndSkill _category. */
        public _category?: "category";

        /** KnowledgeAndSkill _shortDescr. */
        public _shortDescr?: "shortDescr";

        /** KnowledgeAndSkill _longDescrHtml. */
        public _longDescrHtml?: "longDescrHtml";

        /** KnowledgeAndSkill _global. */
        public _global?: "global";

        /** KnowledgeAndSkill _userX. */
        public _userX?: "userX";

        /**
         * Creates a new KnowledgeAndSkill instance using the specified properties.
         * @param [properties] Properties to set
         * @returns KnowledgeAndSkill instance
         */
        public static create(properties?: pl_types.IKnowledgeAndSkill): pl_types.KnowledgeAndSkill;

        /**
         * Encodes the specified KnowledgeAndSkill message. Does not implicitly {@link pl_types.KnowledgeAndSkill.verify|verify} messages.
         * @param message KnowledgeAndSkill message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IKnowledgeAndSkill, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified KnowledgeAndSkill message, length delimited. Does not implicitly {@link pl_types.KnowledgeAndSkill.verify|verify} messages.
         * @param message KnowledgeAndSkill message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IKnowledgeAndSkill, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a KnowledgeAndSkill message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns KnowledgeAndSkill
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.KnowledgeAndSkill;

        /**
         * Decodes a KnowledgeAndSkill message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns KnowledgeAndSkill
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.KnowledgeAndSkill;

        /**
         * Verifies a KnowledgeAndSkill message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a KnowledgeAndSkill message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns KnowledgeAndSkill
         */
        public static fromObject(object: { [k: string]: any }): pl_types.KnowledgeAndSkill;

        /**
         * Creates a plain object from a KnowledgeAndSkill message. Also converts values to other types if specified.
         * @param message KnowledgeAndSkill
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.KnowledgeAndSkill, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this KnowledgeAndSkill to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for KnowledgeAndSkill
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace KnowledgeAndSkill {

        /** Type enum. */
        enum Type {
            UNSET_TYPE = 0,
            EKS = 1,
            XQ_COMPETENCY = 2,
            CTE = 3
        }
    }

    /** Properties of a Project. */
    interface IProject {

        /** Project id */
        id?: (number|null);

        /** Project name */
        name?: (string|null);

        /** Project shortDescr */
        shortDescr?: (string|null);

        /** Project longDescrHtml */
        longDescrHtml?: (string|null);

        /** Project favorite */
        favorite?: (boolean|null);

        /** Project thumbsState */
        thumbsState?: (pl_types.Project.ThumbsState|null);

        /** Project thumbsStateReason */
        thumbsStateReason?: (string|null);

        /** Project archived */
        archived?: (boolean|null);

        /** Project active */
        active?: (boolean|null);

        /** Project assignment */
        assignment?: (pl_types.IAssignment|null);

        /** Project projectDefinition */
        projectDefinition?: (pl_types.IProjectDefinition|null);

        /** Project milestones */
        milestones?: (pl_types.Project.IMilestone[]|null);
    }

    /** Represents a Project. */
    class Project implements IProject {

        /**
         * Constructs a new Project.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IProject);

        /** Project id. */
        public id?: (number|null);

        /** Project name. */
        public name?: (string|null);

        /** Project shortDescr. */
        public shortDescr?: (string|null);

        /** Project longDescrHtml. */
        public longDescrHtml?: (string|null);

        /** Project favorite. */
        public favorite?: (boolean|null);

        /** Project thumbsState. */
        public thumbsState?: (pl_types.Project.ThumbsState|null);

        /** Project thumbsStateReason. */
        public thumbsStateReason?: (string|null);

        /** Project archived. */
        public archived?: (boolean|null);

        /** Project active. */
        public active?: (boolean|null);

        /** Project assignment. */
        public assignment?: (pl_types.IAssignment|null);

        /** Project projectDefinition. */
        public projectDefinition?: (pl_types.IProjectDefinition|null);

        /** Project milestones. */
        public milestones: pl_types.Project.IMilestone[];

        /** Project _id. */
        public _id?: "id";

        /** Project _name. */
        public _name?: "name";

        /** Project _shortDescr. */
        public _shortDescr?: "shortDescr";

        /** Project _longDescrHtml. */
        public _longDescrHtml?: "longDescrHtml";

        /** Project _favorite. */
        public _favorite?: "favorite";

        /** Project _thumbsState. */
        public _thumbsState?: "thumbsState";

        /** Project _thumbsStateReason. */
        public _thumbsStateReason?: "thumbsStateReason";

        /** Project _archived. */
        public _archived?: "archived";

        /** Project _active. */
        public _active?: "active";

        /** Project _assignment. */
        public _assignment?: "assignment";

        /** Project _projectDefinition. */
        public _projectDefinition?: "projectDefinition";

        /**
         * Creates a new Project instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Project instance
         */
        public static create(properties?: pl_types.IProject): pl_types.Project;

        /**
         * Encodes the specified Project message. Does not implicitly {@link pl_types.Project.verify|verify} messages.
         * @param message Project message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IProject, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Project message, length delimited. Does not implicitly {@link pl_types.Project.verify|verify} messages.
         * @param message Project message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IProject, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Project message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Project
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.Project;

        /**
         * Decodes a Project message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Project
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.Project;

        /**
         * Verifies a Project message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Project message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Project
         */
        public static fromObject(object: { [k: string]: any }): pl_types.Project;

        /**
         * Creates a plain object from a Project message. Also converts values to other types if specified.
         * @param message Project
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.Project, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Project to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Project
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace Project {

        /** ThumbsState enum. */
        enum ThumbsState {
            UNSET_THUMBS_STATE = 0,
            THUMBS_UP = 1,
            THUMBS_DOWN = 2
        }

        /** Properties of a Milestone. */
        interface IMilestone {

            /** Milestone id */
            id?: (number|null);

            /** Milestone name */
            name?: (string|null);

            /** Milestone steps */
            steps?: (pl_types.Project.Milestone.IStep[]|null);
        }

        /** Represents a Milestone. */
        class Milestone implements IMilestone {

            /**
             * Constructs a new Milestone.
             * @param [properties] Properties to set
             */
            constructor(properties?: pl_types.Project.IMilestone);

            /** Milestone id. */
            public id?: (number|null);

            /** Milestone name. */
            public name?: (string|null);

            /** Milestone steps. */
            public steps: pl_types.Project.Milestone.IStep[];

            /** Milestone _id. */
            public _id?: "id";

            /** Milestone _name. */
            public _name?: "name";

            /**
             * Creates a new Milestone instance using the specified properties.
             * @param [properties] Properties to set
             * @returns Milestone instance
             */
            public static create(properties?: pl_types.Project.IMilestone): pl_types.Project.Milestone;

            /**
             * Encodes the specified Milestone message. Does not implicitly {@link pl_types.Project.Milestone.verify|verify} messages.
             * @param message Milestone message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encode(message: pl_types.Project.IMilestone, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Encodes the specified Milestone message, length delimited. Does not implicitly {@link pl_types.Project.Milestone.verify|verify} messages.
             * @param message Milestone message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encodeDelimited(message: pl_types.Project.IMilestone, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Decodes a Milestone message from the specified reader or buffer.
             * @param reader Reader or buffer to decode from
             * @param [length] Message length if known beforehand
             * @returns Milestone
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.Project.Milestone;

            /**
             * Decodes a Milestone message from the specified reader or buffer, length delimited.
             * @param reader Reader or buffer to decode from
             * @returns Milestone
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.Project.Milestone;

            /**
             * Verifies a Milestone message.
             * @param message Plain object to verify
             * @returns `null` if valid, otherwise the reason why it is not
             */
            public static verify(message: { [k: string]: any }): (string|null);

            /**
             * Creates a Milestone message from a plain object. Also converts values to their respective internal types.
             * @param object Plain object
             * @returns Milestone
             */
            public static fromObject(object: { [k: string]: any }): pl_types.Project.Milestone;

            /**
             * Creates a plain object from a Milestone message. Also converts values to other types if specified.
             * @param message Milestone
             * @param [options] Conversion options
             * @returns Plain object
             */
            public static toObject(message: pl_types.Project.Milestone, options?: $protobuf.IConversionOptions): { [k: string]: any };

            /**
             * Converts this Milestone to JSON.
             * @returns JSON object
             */
            public toJSON(): { [k: string]: any };

            /**
             * Gets the default type url for Milestone
             * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
             * @returns The default type url
             */
            public static getTypeUrl(typeUrlPrefix?: string): string;
        }

        namespace Milestone {

            /** Properties of a Step. */
            interface IStep {

                /** Step id */
                id?: (number|null);

                /** Step name */
                name?: (string|null);
            }

            /** Represents a Step. */
            class Step implements IStep {

                /**
                 * Constructs a new Step.
                 * @param [properties] Properties to set
                 */
                constructor(properties?: pl_types.Project.Milestone.IStep);

                /** Step id. */
                public id?: (number|null);

                /** Step name. */
                public name?: (string|null);

                /** Step _id. */
                public _id?: "id";

                /** Step _name. */
                public _name?: "name";

                /**
                 * Creates a new Step instance using the specified properties.
                 * @param [properties] Properties to set
                 * @returns Step instance
                 */
                public static create(properties?: pl_types.Project.Milestone.IStep): pl_types.Project.Milestone.Step;

                /**
                 * Encodes the specified Step message. Does not implicitly {@link pl_types.Project.Milestone.Step.verify|verify} messages.
                 * @param message Step message or plain object to encode
                 * @param [writer] Writer to encode to
                 * @returns Writer
                 */
                public static encode(message: pl_types.Project.Milestone.IStep, writer?: $protobuf.Writer): $protobuf.Writer;

                /**
                 * Encodes the specified Step message, length delimited. Does not implicitly {@link pl_types.Project.Milestone.Step.verify|verify} messages.
                 * @param message Step message or plain object to encode
                 * @param [writer] Writer to encode to
                 * @returns Writer
                 */
                public static encodeDelimited(message: pl_types.Project.Milestone.IStep, writer?: $protobuf.Writer): $protobuf.Writer;

                /**
                 * Decodes a Step message from the specified reader or buffer.
                 * @param reader Reader or buffer to decode from
                 * @param [length] Message length if known beforehand
                 * @returns Step
                 * @throws {Error} If the payload is not a reader or valid buffer
                 * @throws {$protobuf.util.ProtocolError} If required fields are missing
                 */
                public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.Project.Milestone.Step;

                /**
                 * Decodes a Step message from the specified reader or buffer, length delimited.
                 * @param reader Reader or buffer to decode from
                 * @returns Step
                 * @throws {Error} If the payload is not a reader or valid buffer
                 * @throws {$protobuf.util.ProtocolError} If required fields are missing
                 */
                public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.Project.Milestone.Step;

                /**
                 * Verifies a Step message.
                 * @param message Plain object to verify
                 * @returns `null` if valid, otherwise the reason why it is not
                 */
                public static verify(message: { [k: string]: any }): (string|null);

                /**
                 * Creates a Step message from a plain object. Also converts values to their respective internal types.
                 * @param object Plain object
                 * @returns Step
                 */
                public static fromObject(object: { [k: string]: any }): pl_types.Project.Milestone.Step;

                /**
                 * Creates a plain object from a Step message. Also converts values to other types if specified.
                 * @param message Step
                 * @param [options] Conversion options
                 * @returns Plain object
                 */
                public static toObject(message: pl_types.Project.Milestone.Step, options?: $protobuf.IConversionOptions): { [k: string]: any };

                /**
                 * Converts this Step to JSON.
                 * @returns JSON object
                 */
                public toJSON(): { [k: string]: any };

                /**
                 * Gets the default type url for Step
                 * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                 * @returns The default type url
                 */
                public static getTypeUrl(typeUrlPrefix?: string): string;
            }
        }
    }

    /** Properties of a School. */
    interface ISchool {

        /** School id */
        id?: (number|null);

        /** School district */
        district?: (pl_types.IDistrict|null);

        /** School name */
        name?: (string|null);

        /** School nickname */
        nickname?: (string|null);

        /** School address */
        address?: (string|null);
    }

    /** Represents a School. */
    class School implements ISchool {

        /**
         * Constructs a new School.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.ISchool);

        /** School id. */
        public id?: (number|null);

        /** School district. */
        public district?: (pl_types.IDistrict|null);

        /** School name. */
        public name?: (string|null);

        /** School nickname. */
        public nickname?: (string|null);

        /** School address. */
        public address?: (string|null);

        /** School _id. */
        public _id?: "id";

        /** School _district. */
        public _district?: "district";

        /** School _name. */
        public _name?: "name";

        /** School _nickname. */
        public _nickname?: "nickname";

        /** School _address. */
        public _address?: "address";

        /**
         * Creates a new School instance using the specified properties.
         * @param [properties] Properties to set
         * @returns School instance
         */
        public static create(properties?: pl_types.ISchool): pl_types.School;

        /**
         * Encodes the specified School message. Does not implicitly {@link pl_types.School.verify|verify} messages.
         * @param message School message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.ISchool, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified School message, length delimited. Does not implicitly {@link pl_types.School.verify|verify} messages.
         * @param message School message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.ISchool, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a School message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns School
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.School;

        /**
         * Decodes a School message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns School
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.School;

        /**
         * Verifies a School message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a School message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns School
         */
        public static fromObject(object: { [k: string]: any }): pl_types.School;

        /**
         * Creates a plain object from a School message. Also converts values to other types if specified.
         * @param message School
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.School, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this School to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for School
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a UserX. */
    interface IUserX {

        /** UserX id */
        id?: (number|null);

        /** UserX districtId */
        districtId?: (number|null);

        /** UserX firstName */
        firstName?: (string|null);

        /** UserX lastName */
        lastName?: (string|null);

        /** UserX emailAddress */
        emailAddress?: (string|null);

        /** UserX isAdminX */
        isAdminX?: (boolean|null);

        /** UserX adminXId */
        adminXId?: (number|null);

        /** UserX isTeacher */
        isTeacher?: (boolean|null);

        /** UserX teacherId */
        teacherId?: (number|null);

        /** UserX isStudent */
        isStudent?: (boolean|null);

        /** UserX studentId */
        studentId?: (number|null);

        /** UserX isDemo */
        isDemo?: (boolean|null);

        /** UserX isAuthenticated */
        isAuthenticated?: (boolean|null);
    }

    /** Represents a UserX. */
    class UserX implements IUserX {

        /**
         * Constructs a new UserX.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IUserX);

        /** UserX id. */
        public id?: (number|null);

        /** UserX districtId. */
        public districtId?: (number|null);

        /** UserX firstName. */
        public firstName?: (string|null);

        /** UserX lastName. */
        public lastName?: (string|null);

        /** UserX emailAddress. */
        public emailAddress?: (string|null);

        /** UserX isAdminX. */
        public isAdminX?: (boolean|null);

        /** UserX adminXId. */
        public adminXId?: (number|null);

        /** UserX isTeacher. */
        public isTeacher?: (boolean|null);

        /** UserX teacherId. */
        public teacherId?: (number|null);

        /** UserX isStudent. */
        public isStudent?: (boolean|null);

        /** UserX studentId. */
        public studentId?: (number|null);

        /** UserX isDemo. */
        public isDemo?: (boolean|null);

        /** UserX isAuthenticated. */
        public isAuthenticated?: (boolean|null);

        /** UserX _id. */
        public _id?: "id";

        /** UserX _districtId. */
        public _districtId?: "districtId";

        /** UserX _firstName. */
        public _firstName?: "firstName";

        /** UserX _lastName. */
        public _lastName?: "lastName";

        /** UserX _emailAddress. */
        public _emailAddress?: "emailAddress";

        /** UserX _isAdminX. */
        public _isAdminX?: "isAdminX";

        /** UserX _adminXId. */
        public _adminXId?: "adminXId";

        /** UserX _isTeacher. */
        public _isTeacher?: "isTeacher";

        /** UserX _teacherId. */
        public _teacherId?: "teacherId";

        /** UserX _isStudent. */
        public _isStudent?: "isStudent";

        /** UserX _studentId. */
        public _studentId?: "studentId";

        /** UserX _isDemo. */
        public _isDemo?: "isDemo";

        /** UserX _isAuthenticated. */
        public _isAuthenticated?: "isAuthenticated";

        /**
         * Creates a new UserX instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UserX instance
         */
        public static create(properties?: pl_types.IUserX): pl_types.UserX;

        /**
         * Encodes the specified UserX message. Does not implicitly {@link pl_types.UserX.verify|verify} messages.
         * @param message UserX message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IUserX, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UserX message, length delimited. Does not implicitly {@link pl_types.UserX.verify|verify} messages.
         * @param message UserX message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IUserX, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a UserX message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UserX
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.UserX;

        /**
         * Decodes a UserX message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UserX
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.UserX;

        /**
         * Verifies a UserX message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a UserX message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UserX
         */
        public static fromObject(object: { [k: string]: any }): pl_types.UserX;

        /**
         * Creates a plain object from a UserX message. Also converts values to other types if specified.
         * @param message UserX
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.UserX, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UserX to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UserX
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ProjectDefinition. */
    interface IProjectDefinition {

        /** ProjectDefinition id */
        id?: (number|null);

        /** ProjectDefinition name */
        name?: (string|null);

        /** ProjectDefinition inputId */
        inputId?: (number|null);

        /** ProjectDefinition template */
        template?: (boolean|null);

        /** ProjectDefinition selected */
        selected?: (boolean|null);

        /** ProjectDefinition state */
        state?: (pl_types.ProjectDefinition.State|null);

        /** ProjectDefinition existingProject */
        existingProject?: (pl_types.IProject|null);

        /** ProjectDefinition existingProjectUseType */
        existingProjectUseType?: (pl_types.ProjectDefinition.ExistingProjectUseType|null);

        /** ProjectDefinition inputs */
        inputs?: (pl_types.IProjectInputValue[]|null);

        /** ProjectDefinition assignment */
        assignment?: (pl_types.IAssignment|null);
    }

    /** Represents a ProjectDefinition. */
    class ProjectDefinition implements IProjectDefinition {

        /**
         * Constructs a new ProjectDefinition.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IProjectDefinition);

        /** ProjectDefinition id. */
        public id?: (number|null);

        /** ProjectDefinition name. */
        public name?: (string|null);

        /** ProjectDefinition inputId. */
        public inputId?: (number|null);

        /** ProjectDefinition template. */
        public template?: (boolean|null);

        /** ProjectDefinition selected. */
        public selected?: (boolean|null);

        /** ProjectDefinition state. */
        public state?: (pl_types.ProjectDefinition.State|null);

        /** ProjectDefinition existingProject. */
        public existingProject?: (pl_types.IProject|null);

        /** ProjectDefinition existingProjectUseType. */
        public existingProjectUseType?: (pl_types.ProjectDefinition.ExistingProjectUseType|null);

        /** ProjectDefinition inputs. */
        public inputs: pl_types.IProjectInputValue[];

        /** ProjectDefinition assignment. */
        public assignment?: (pl_types.IAssignment|null);

        /** ProjectDefinition _id. */
        public _id?: "id";

        /** ProjectDefinition _name. */
        public _name?: "name";

        /** ProjectDefinition _inputId. */
        public _inputId?: "inputId";

        /** ProjectDefinition _template. */
        public _template?: "template";

        /** ProjectDefinition _selected. */
        public _selected?: "selected";

        /** ProjectDefinition _state. */
        public _state?: "state";

        /** ProjectDefinition _existingProject. */
        public _existingProject?: "existingProject";

        /** ProjectDefinition _existingProjectUseType. */
        public _existingProjectUseType?: "existingProjectUseType";

        /** ProjectDefinition _assignment. */
        public _assignment?: "assignment";

        /**
         * Creates a new ProjectDefinition instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ProjectDefinition instance
         */
        public static create(properties?: pl_types.IProjectDefinition): pl_types.ProjectDefinition;

        /**
         * Encodes the specified ProjectDefinition message. Does not implicitly {@link pl_types.ProjectDefinition.verify|verify} messages.
         * @param message ProjectDefinition message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IProjectDefinition, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ProjectDefinition message, length delimited. Does not implicitly {@link pl_types.ProjectDefinition.verify|verify} messages.
         * @param message ProjectDefinition message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IProjectDefinition, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ProjectDefinition message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ProjectDefinition
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.ProjectDefinition;

        /**
         * Decodes a ProjectDefinition message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ProjectDefinition
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.ProjectDefinition;

        /**
         * Verifies a ProjectDefinition message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ProjectDefinition message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ProjectDefinition
         */
        public static fromObject(object: { [k: string]: any }): pl_types.ProjectDefinition;

        /**
         * Creates a plain object from a ProjectDefinition message. Also converts values to other types if specified.
         * @param message ProjectDefinition
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.ProjectDefinition, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ProjectDefinition to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ProjectDefinition
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace ProjectDefinition {

        /** State enum. */
        enum State {
            UNSET_STATE = 0,
            PROCESSING = 1,
            COMPLETED = 2,
            FAILED = 3
        }

        /** ExistingProjectUseType enum. */
        enum ExistingProjectUseType {
            UNSET_EXISTING_PROJECT_CONFIGURATION_TYPE = 0,
            USE_CONFIGURATION = 1,
            MORE_LIKE_THIS = 2,
            SUB_PROJECTS = 3
        }
    }

    /** Properties of a ProjectInputValue. */
    interface IProjectInputValue {

        /** ProjectInputValue category */
        category?: (pl_types.IProjectInputCategory|null);

        /** ProjectInputValue freeTexts */
        freeTexts?: (string[]|null);

        /** ProjectInputValue selectedIds */
        selectedIds?: (number[]|null);
    }

    /** Represents a ProjectInputValue. */
    class ProjectInputValue implements IProjectInputValue {

        /**
         * Constructs a new ProjectInputValue.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IProjectInputValue);

        /** ProjectInputValue category. */
        public category?: (pl_types.IProjectInputCategory|null);

        /** ProjectInputValue freeTexts. */
        public freeTexts: string[];

        /** ProjectInputValue selectedIds. */
        public selectedIds: number[];

        /** ProjectInputValue _category. */
        public _category?: "category";

        /**
         * Creates a new ProjectInputValue instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ProjectInputValue instance
         */
        public static create(properties?: pl_types.IProjectInputValue): pl_types.ProjectInputValue;

        /**
         * Encodes the specified ProjectInputValue message. Does not implicitly {@link pl_types.ProjectInputValue.verify|verify} messages.
         * @param message ProjectInputValue message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IProjectInputValue, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ProjectInputValue message, length delimited. Does not implicitly {@link pl_types.ProjectInputValue.verify|verify} messages.
         * @param message ProjectInputValue message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IProjectInputValue, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ProjectInputValue message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ProjectInputValue
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.ProjectInputValue;

        /**
         * Decodes a ProjectInputValue message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ProjectInputValue
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.ProjectInputValue;

        /**
         * Verifies a ProjectInputValue message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ProjectInputValue message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ProjectInputValue
         */
        public static fromObject(object: { [k: string]: any }): pl_types.ProjectInputValue;

        /**
         * Creates a plain object from a ProjectInputValue message. Also converts values to other types if specified.
         * @param message ProjectInputValue
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.ProjectInputValue, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ProjectInputValue to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ProjectInputValue
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ProjectInputCategory. */
    interface IProjectInputCategory {

        /** ProjectInputCategory id */
        id?: (number|null);

        /** ProjectInputCategory typeId */
        typeId?: (number|null);

        /** ProjectInputCategory name */
        name?: (string|null);

        /** ProjectInputCategory shortDescr */
        shortDescr?: (string|null);

        /** ProjectInputCategory inputDescr */
        inputDescr?: (string|null);

        /** ProjectInputCategory hint */
        hint?: (string|null);

        /** ProjectInputCategory placeholder */
        placeholder?: (string|null);

        /** ProjectInputCategory valueType */
        valueType?: (pl_types.ProjectInputCategory.ValueType|null);

        /** ProjectInputCategory maxNumValues */
        maxNumValues?: (number|null);

        /** ProjectInputCategory options */
        options?: (pl_types.ProjectInputCategory.IOption[]|null);
    }

    /** Represents a ProjectInputCategory. */
    class ProjectInputCategory implements IProjectInputCategory {

        /**
         * Constructs a new ProjectInputCategory.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IProjectInputCategory);

        /** ProjectInputCategory id. */
        public id?: (number|null);

        /** ProjectInputCategory typeId. */
        public typeId?: (number|null);

        /** ProjectInputCategory name. */
        public name?: (string|null);

        /** ProjectInputCategory shortDescr. */
        public shortDescr?: (string|null);

        /** ProjectInputCategory inputDescr. */
        public inputDescr?: (string|null);

        /** ProjectInputCategory hint. */
        public hint?: (string|null);

        /** ProjectInputCategory placeholder. */
        public placeholder?: (string|null);

        /** ProjectInputCategory valueType. */
        public valueType?: (pl_types.ProjectInputCategory.ValueType|null);

        /** ProjectInputCategory maxNumValues. */
        public maxNumValues?: (number|null);

        /** ProjectInputCategory options. */
        public options: pl_types.ProjectInputCategory.IOption[];

        /** ProjectInputCategory _id. */
        public _id?: "id";

        /** ProjectInputCategory _typeId. */
        public _typeId?: "typeId";

        /** ProjectInputCategory _name. */
        public _name?: "name";

        /** ProjectInputCategory _shortDescr. */
        public _shortDescr?: "shortDescr";

        /** ProjectInputCategory _inputDescr. */
        public _inputDescr?: "inputDescr";

        /** ProjectInputCategory _hint. */
        public _hint?: "hint";

        /** ProjectInputCategory _placeholder. */
        public _placeholder?: "placeholder";

        /** ProjectInputCategory _valueType. */
        public _valueType?: "valueType";

        /** ProjectInputCategory _maxNumValues. */
        public _maxNumValues?: "maxNumValues";

        /**
         * Creates a new ProjectInputCategory instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ProjectInputCategory instance
         */
        public static create(properties?: pl_types.IProjectInputCategory): pl_types.ProjectInputCategory;

        /**
         * Encodes the specified ProjectInputCategory message. Does not implicitly {@link pl_types.ProjectInputCategory.verify|verify} messages.
         * @param message ProjectInputCategory message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IProjectInputCategory, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ProjectInputCategory message, length delimited. Does not implicitly {@link pl_types.ProjectInputCategory.verify|verify} messages.
         * @param message ProjectInputCategory message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IProjectInputCategory, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ProjectInputCategory message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ProjectInputCategory
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.ProjectInputCategory;

        /**
         * Decodes a ProjectInputCategory message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ProjectInputCategory
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.ProjectInputCategory;

        /**
         * Verifies a ProjectInputCategory message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ProjectInputCategory message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ProjectInputCategory
         */
        public static fromObject(object: { [k: string]: any }): pl_types.ProjectInputCategory;

        /**
         * Creates a plain object from a ProjectInputCategory message. Also converts values to other types if specified.
         * @param message ProjectInputCategory
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.ProjectInputCategory, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ProjectInputCategory to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ProjectInputCategory
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace ProjectInputCategory {

        /** ValueType enum. */
        enum ValueType {
            UNSET_VALUE_TYPE = 0,
            FREE_TEXT = 1,
            EKS = 2,
            XQ_COMPETENCY = 3,
            MOTIVATION = 4,
            CTE = 5
        }

        /** Properties of an Option. */
        interface IOption {

            /** Option id */
            id?: (number|null);

            /** Option name */
            name?: (string|null);

            /** Option category */
            category?: (string|null);

            /** Option shortDescr */
            shortDescr?: (string|null);

            /** Option userX */
            userX?: (pl_types.IUserX|null);
        }

        /** Represents an Option. */
        class Option implements IOption {

            /**
             * Constructs a new Option.
             * @param [properties] Properties to set
             */
            constructor(properties?: pl_types.ProjectInputCategory.IOption);

            /** Option id. */
            public id?: (number|null);

            /** Option name. */
            public name?: (string|null);

            /** Option category. */
            public category?: (string|null);

            /** Option shortDescr. */
            public shortDescr?: (string|null);

            /** Option userX. */
            public userX?: (pl_types.IUserX|null);

            /** Option _id. */
            public _id?: "id";

            /** Option _name. */
            public _name?: "name";

            /** Option _category. */
            public _category?: "category";

            /** Option _shortDescr. */
            public _shortDescr?: "shortDescr";

            /** Option _userX. */
            public _userX?: "userX";

            /**
             * Creates a new Option instance using the specified properties.
             * @param [properties] Properties to set
             * @returns Option instance
             */
            public static create(properties?: pl_types.ProjectInputCategory.IOption): pl_types.ProjectInputCategory.Option;

            /**
             * Encodes the specified Option message. Does not implicitly {@link pl_types.ProjectInputCategory.Option.verify|verify} messages.
             * @param message Option message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encode(message: pl_types.ProjectInputCategory.IOption, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Encodes the specified Option message, length delimited. Does not implicitly {@link pl_types.ProjectInputCategory.Option.verify|verify} messages.
             * @param message Option message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encodeDelimited(message: pl_types.ProjectInputCategory.IOption, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Decodes an Option message from the specified reader or buffer.
             * @param reader Reader or buffer to decode from
             * @param [length] Message length if known beforehand
             * @returns Option
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.ProjectInputCategory.Option;

            /**
             * Decodes an Option message from the specified reader or buffer, length delimited.
             * @param reader Reader or buffer to decode from
             * @returns Option
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.ProjectInputCategory.Option;

            /**
             * Verifies an Option message.
             * @param message Plain object to verify
             * @returns `null` if valid, otherwise the reason why it is not
             */
            public static verify(message: { [k: string]: any }): (string|null);

            /**
             * Creates an Option message from a plain object. Also converts values to their respective internal types.
             * @param object Plain object
             * @returns Option
             */
            public static fromObject(object: { [k: string]: any }): pl_types.ProjectInputCategory.Option;

            /**
             * Creates a plain object from an Option message. Also converts values to other types if specified.
             * @param message Option
             * @param [options] Conversion options
             * @returns Plain object
             */
            public static toObject(message: pl_types.ProjectInputCategory.Option, options?: $protobuf.IConversionOptions): { [k: string]: any };

            /**
             * Converts this Option to JSON.
             * @returns JSON object
             */
            public toJSON(): { [k: string]: any };

            /**
             * Gets the default type url for Option
             * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
             * @returns The default type url
             */
            public static getTypeUrl(typeUrlPrefix?: string): string;
        }
    }

    /** Properties of a Tag. */
    interface ITag {

        /** Tag text */
        text?: (string|null);

        /** Tag userXId */
        userXId?: (number|null);
    }

    /** Represents a Tag. */
    class Tag implements ITag {

        /**
         * Constructs a new Tag.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.ITag);

        /** Tag text. */
        public text?: (string|null);

        /** Tag userXId. */
        public userXId?: (number|null);

        /** Tag _text. */
        public _text?: "text";

        /** Tag _userXId. */
        public _userXId?: "userXId";

        /**
         * Creates a new Tag instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Tag instance
         */
        public static create(properties?: pl_types.ITag): pl_types.Tag;

        /**
         * Encodes the specified Tag message. Does not implicitly {@link pl_types.Tag.verify|verify} messages.
         * @param message Tag message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.ITag, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Tag message, length delimited. Does not implicitly {@link pl_types.Tag.verify|verify} messages.
         * @param message Tag message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.ITag, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Tag message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Tag
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.Tag;

        /**
         * Decodes a Tag message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Tag
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.Tag;

        /**
         * Verifies a Tag message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Tag message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Tag
         */
        public static fromObject(object: { [k: string]: any }): pl_types.Tag;

        /**
         * Creates a plain object from a Tag message. Also converts values to other types if specified.
         * @param message Tag
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.Tag, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Tag to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Tag
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ProjectPostComment. */
    interface IProjectPostComment {

        /** ProjectPostComment id */
        id?: (number|null);

        /** ProjectPostComment userX */
        userX?: (pl_types.IUserX|null);

        /** ProjectPostComment projectPost */
        projectPost?: (pl_types.IProjectPost|null);

        /** ProjectPostComment longDescrHtml */
        longDescrHtml?: (string|null);

        /** ProjectPostComment postTimeMs */
        postTimeMs?: (Long|null);

        /** ProjectPostComment beingEdited */
        beingEdited?: (boolean|null);
    }

    /** Represents a ProjectPostComment. */
    class ProjectPostComment implements IProjectPostComment {

        /**
         * Constructs a new ProjectPostComment.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IProjectPostComment);

        /** ProjectPostComment id. */
        public id?: (number|null);

        /** ProjectPostComment userX. */
        public userX?: (pl_types.IUserX|null);

        /** ProjectPostComment projectPost. */
        public projectPost?: (pl_types.IProjectPost|null);

        /** ProjectPostComment longDescrHtml. */
        public longDescrHtml?: (string|null);

        /** ProjectPostComment postTimeMs. */
        public postTimeMs?: (Long|null);

        /** ProjectPostComment beingEdited. */
        public beingEdited?: (boolean|null);

        /** ProjectPostComment _id. */
        public _id?: "id";

        /** ProjectPostComment _userX. */
        public _userX?: "userX";

        /** ProjectPostComment _projectPost. */
        public _projectPost?: "projectPost";

        /** ProjectPostComment _longDescrHtml. */
        public _longDescrHtml?: "longDescrHtml";

        /** ProjectPostComment _postTimeMs. */
        public _postTimeMs?: "postTimeMs";

        /** ProjectPostComment _beingEdited. */
        public _beingEdited?: "beingEdited";

        /**
         * Creates a new ProjectPostComment instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ProjectPostComment instance
         */
        public static create(properties?: pl_types.IProjectPostComment): pl_types.ProjectPostComment;

        /**
         * Encodes the specified ProjectPostComment message. Does not implicitly {@link pl_types.ProjectPostComment.verify|verify} messages.
         * @param message ProjectPostComment message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IProjectPostComment, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ProjectPostComment message, length delimited. Does not implicitly {@link pl_types.ProjectPostComment.verify|verify} messages.
         * @param message ProjectPostComment message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IProjectPostComment, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ProjectPostComment message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ProjectPostComment
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.ProjectPostComment;

        /**
         * Decodes a ProjectPostComment message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ProjectPostComment
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.ProjectPostComment;

        /**
         * Verifies a ProjectPostComment message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ProjectPostComment message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ProjectPostComment
         */
        public static fromObject(object: { [k: string]: any }): pl_types.ProjectPostComment;

        /**
         * Creates a plain object from a ProjectPostComment message. Also converts values to other types if specified.
         * @param message ProjectPostComment
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.ProjectPostComment, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ProjectPostComment to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ProjectPostComment
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ProjectPostRatingCategory. */
    interface IProjectPostRatingCategory {

        /** ProjectPostRatingCategory projectInputFulfillmentId */
        projectInputFulfillmentId?: (number|null);

        /** ProjectPostRatingCategory category */
        category?: (string|null);

        /** ProjectPostRatingCategory value */
        value?: (string|null);

        /** ProjectPostRatingCategory valueType */
        valueType?: (pl_types.ProjectInputCategory.ValueType|null);
    }

    /** Represents a ProjectPostRatingCategory. */
    class ProjectPostRatingCategory implements IProjectPostRatingCategory {

        /**
         * Constructs a new ProjectPostRatingCategory.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IProjectPostRatingCategory);

        /** ProjectPostRatingCategory projectInputFulfillmentId. */
        public projectInputFulfillmentId?: (number|null);

        /** ProjectPostRatingCategory category. */
        public category?: (string|null);

        /** ProjectPostRatingCategory value. */
        public value?: (string|null);

        /** ProjectPostRatingCategory valueType. */
        public valueType?: (pl_types.ProjectInputCategory.ValueType|null);

        /** ProjectPostRatingCategory _projectInputFulfillmentId. */
        public _projectInputFulfillmentId?: "projectInputFulfillmentId";

        /** ProjectPostRatingCategory _category. */
        public _category?: "category";

        /** ProjectPostRatingCategory _value. */
        public _value?: "value";

        /** ProjectPostRatingCategory _valueType. */
        public _valueType?: "valueType";

        /**
         * Creates a new ProjectPostRatingCategory instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ProjectPostRatingCategory instance
         */
        public static create(properties?: pl_types.IProjectPostRatingCategory): pl_types.ProjectPostRatingCategory;

        /**
         * Encodes the specified ProjectPostRatingCategory message. Does not implicitly {@link pl_types.ProjectPostRatingCategory.verify|verify} messages.
         * @param message ProjectPostRatingCategory message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IProjectPostRatingCategory, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ProjectPostRatingCategory message, length delimited. Does not implicitly {@link pl_types.ProjectPostRatingCategory.verify|verify} messages.
         * @param message ProjectPostRatingCategory message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IProjectPostRatingCategory, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ProjectPostRatingCategory message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ProjectPostRatingCategory
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.ProjectPostRatingCategory;

        /**
         * Decodes a ProjectPostRatingCategory message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ProjectPostRatingCategory
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.ProjectPostRatingCategory;

        /**
         * Verifies a ProjectPostRatingCategory message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ProjectPostRatingCategory message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ProjectPostRatingCategory
         */
        public static fromObject(object: { [k: string]: any }): pl_types.ProjectPostRatingCategory;

        /**
         * Creates a plain object from a ProjectPostRatingCategory message. Also converts values to other types if specified.
         * @param message ProjectPostRatingCategory
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.ProjectPostRatingCategory, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ProjectPostRatingCategory to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ProjectPostRatingCategory
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ProjectPostRating. */
    interface IProjectPostRating {

        /** ProjectPostRating id */
        id?: (number|null);

        /** ProjectPostRating userX */
        userX?: (pl_types.IUserX|null);

        /** ProjectPostRating rating */
        rating?: (number|null);

        /** ProjectPostRating ratingType */
        ratingType?: (pl_types.ProjectPostRating.RatingType|null);

        /** ProjectPostRating projectPost */
        projectPost?: (pl_types.IProjectPost|null);

        /** ProjectPostRating knowledgeAndSkill */
        knowledgeAndSkill?: (pl_types.IKnowledgeAndSkill|null);

        /** ProjectPostRating projectInputFulfillmentId */
        projectInputFulfillmentId?: (number|null);
    }

    /** Represents a ProjectPostRating. */
    class ProjectPostRating implements IProjectPostRating {

        /**
         * Constructs a new ProjectPostRating.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IProjectPostRating);

        /** ProjectPostRating id. */
        public id?: (number|null);

        /** ProjectPostRating userX. */
        public userX?: (pl_types.IUserX|null);

        /** ProjectPostRating rating. */
        public rating?: (number|null);

        /** ProjectPostRating ratingType. */
        public ratingType?: (pl_types.ProjectPostRating.RatingType|null);

        /** ProjectPostRating projectPost. */
        public projectPost?: (pl_types.IProjectPost|null);

        /** ProjectPostRating knowledgeAndSkill. */
        public knowledgeAndSkill?: (pl_types.IKnowledgeAndSkill|null);

        /** ProjectPostRating projectInputFulfillmentId. */
        public projectInputFulfillmentId?: (number|null);

        /** ProjectPostRating _id. */
        public _id?: "id";

        /** ProjectPostRating _userX. */
        public _userX?: "userX";

        /** ProjectPostRating _rating. */
        public _rating?: "rating";

        /** ProjectPostRating _ratingType. */
        public _ratingType?: "ratingType";

        /** ProjectPostRating _projectPost. */
        public _projectPost?: "projectPost";

        /** ProjectPostRating _knowledgeAndSkill. */
        public _knowledgeAndSkill?: "knowledgeAndSkill";

        /** ProjectPostRating _projectInputFulfillmentId. */
        public _projectInputFulfillmentId?: "projectInputFulfillmentId";

        /**
         * Creates a new ProjectPostRating instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ProjectPostRating instance
         */
        public static create(properties?: pl_types.IProjectPostRating): pl_types.ProjectPostRating;

        /**
         * Encodes the specified ProjectPostRating message. Does not implicitly {@link pl_types.ProjectPostRating.verify|verify} messages.
         * @param message ProjectPostRating message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IProjectPostRating, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ProjectPostRating message, length delimited. Does not implicitly {@link pl_types.ProjectPostRating.verify|verify} messages.
         * @param message ProjectPostRating message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IProjectPostRating, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ProjectPostRating message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ProjectPostRating
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.ProjectPostRating;

        /**
         * Decodes a ProjectPostRating message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ProjectPostRating
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.ProjectPostRating;

        /**
         * Verifies a ProjectPostRating message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ProjectPostRating message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ProjectPostRating
         */
        public static fromObject(object: { [k: string]: any }): pl_types.ProjectPostRating;

        /**
         * Creates a plain object from a ProjectPostRating message. Also converts values to other types if specified.
         * @param message ProjectPostRating
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.ProjectPostRating, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ProjectPostRating to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ProjectPostRating
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace ProjectPostRating {

        /** RatingType enum. */
        enum RatingType {
            UNSET_RATING_TYPE = 0,
            INITIAL_1_TO_5 = 1,
            GOAL_COMPLETE_PCT = 2
        }
    }

    /** Properties of a ProjectPost. */
    interface IProjectPost {

        /** ProjectPost id */
        id?: (number|null);

        /** ProjectPost userX */
        userX?: (pl_types.IUserX|null);

        /** ProjectPost project */
        project?: (pl_types.IProject|null);

        /** ProjectPost name */
        name?: (string|null);

        /** ProjectPost longDescrHtml */
        longDescrHtml?: (string|null);

        /** ProjectPost desiredFeedback */
        desiredFeedback?: (string|null);

        /** ProjectPost tags */
        tags?: (pl_types.ITag[]|null);

        /** ProjectPost comments */
        comments?: (pl_types.IProjectPostComment[]|null);

        /** ProjectPost postTimeMs */
        postTimeMs?: (Long|null);

        /** ProjectPost beingEdited */
        beingEdited?: (boolean|null);

        /** ProjectPost ratingCategories */
        ratingCategories?: (pl_types.IProjectPostRatingCategory[]|null);

        /** ProjectPost ratings */
        ratings?: (pl_types.IProjectPostRating[]|null);
    }

    /** Represents a ProjectPost. */
    class ProjectPost implements IProjectPost {

        /**
         * Constructs a new ProjectPost.
         * @param [properties] Properties to set
         */
        constructor(properties?: pl_types.IProjectPost);

        /** ProjectPost id. */
        public id?: (number|null);

        /** ProjectPost userX. */
        public userX?: (pl_types.IUserX|null);

        /** ProjectPost project. */
        public project?: (pl_types.IProject|null);

        /** ProjectPost name. */
        public name?: (string|null);

        /** ProjectPost longDescrHtml. */
        public longDescrHtml?: (string|null);

        /** ProjectPost desiredFeedback. */
        public desiredFeedback?: (string|null);

        /** ProjectPost tags. */
        public tags: pl_types.ITag[];

        /** ProjectPost comments. */
        public comments: pl_types.IProjectPostComment[];

        /** ProjectPost postTimeMs. */
        public postTimeMs?: (Long|null);

        /** ProjectPost beingEdited. */
        public beingEdited?: (boolean|null);

        /** ProjectPost ratingCategories. */
        public ratingCategories: pl_types.IProjectPostRatingCategory[];

        /** ProjectPost ratings. */
        public ratings: pl_types.IProjectPostRating[];

        /** ProjectPost _id. */
        public _id?: "id";

        /** ProjectPost _userX. */
        public _userX?: "userX";

        /** ProjectPost _project. */
        public _project?: "project";

        /** ProjectPost _name. */
        public _name?: "name";

        /** ProjectPost _longDescrHtml. */
        public _longDescrHtml?: "longDescrHtml";

        /** ProjectPost _desiredFeedback. */
        public _desiredFeedback?: "desiredFeedback";

        /** ProjectPost _postTimeMs. */
        public _postTimeMs?: "postTimeMs";

        /** ProjectPost _beingEdited. */
        public _beingEdited?: "beingEdited";

        /**
         * Creates a new ProjectPost instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ProjectPost instance
         */
        public static create(properties?: pl_types.IProjectPost): pl_types.ProjectPost;

        /**
         * Encodes the specified ProjectPost message. Does not implicitly {@link pl_types.ProjectPost.verify|verify} messages.
         * @param message ProjectPost message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: pl_types.IProjectPost, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ProjectPost message, length delimited. Does not implicitly {@link pl_types.ProjectPost.verify|verify} messages.
         * @param message ProjectPost message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: pl_types.IProjectPost, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ProjectPost message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ProjectPost
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): pl_types.ProjectPost;

        /**
         * Decodes a ProjectPost message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ProjectPost
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): pl_types.ProjectPost;

        /**
         * Verifies a ProjectPost message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ProjectPost message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ProjectPost
         */
        public static fromObject(object: { [k: string]: any }): pl_types.ProjectPost;

        /**
         * Creates a plain object from a ProjectPost message. Also converts values to other types if specified.
         * @param message ProjectPost
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: pl_types.ProjectPost, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ProjectPost to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ProjectPost
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace class_x_management_service. */
export namespace class_x_management_service {

    /** Represents a ClassXManagementService */
    class ClassXManagementService extends $protobuf.rpc.Service {

        /**
         * Constructs a new ClassXManagementService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new ClassXManagementService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): ClassXManagementService;

        /**
         * Calls GetClassXs.
         * @param request GetClassXsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetClassXsResponse
         */
        public getClassXs(request: class_x_management_service.IGetClassXsRequest, callback: class_x_management_service.ClassXManagementService.GetClassXsCallback): void;

        /**
         * Calls GetClassXs.
         * @param request GetClassXsRequest message or plain object
         * @returns Promise
         */
        public getClassXs(request: class_x_management_service.IGetClassXsRequest): Promise<class_x_management_service.GetClassXsResponse>;

        /**
         * Calls UpsertClassX.
         * @param request UpsertClassXRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and UpsertClassXResponse
         */
        public upsertClassX(request: class_x_management_service.IUpsertClassXRequest, callback: class_x_management_service.ClassXManagementService.UpsertClassXCallback): void;

        /**
         * Calls UpsertClassX.
         * @param request UpsertClassXRequest message or plain object
         * @returns Promise
         */
        public upsertClassX(request: class_x_management_service.IUpsertClassXRequest): Promise<class_x_management_service.UpsertClassXResponse>;
    }

    namespace ClassXManagementService {

        /**
         * Callback as used by {@link class_x_management_service.ClassXManagementService#getClassXs}.
         * @param error Error, if any
         * @param [response] GetClassXsResponse
         */
        type GetClassXsCallback = (error: (Error|null), response?: class_x_management_service.GetClassXsResponse) => void;

        /**
         * Callback as used by {@link class_x_management_service.ClassXManagementService#upsertClassX}.
         * @param error Error, if any
         * @param [response] UpsertClassXResponse
         */
        type UpsertClassXCallback = (error: (Error|null), response?: class_x_management_service.UpsertClassXResponse) => void;
    }

    /** Properties of a GetClassXsRequest. */
    interface IGetClassXsRequest {

        /** GetClassXsRequest schoolIds */
        schoolIds?: (number[]|null);

        /** GetClassXsRequest classXIds */
        classXIds?: (number[]|null);

        /** GetClassXsRequest teacherIds */
        teacherIds?: (number[]|null);

        /** GetClassXsRequest studentIds */
        studentIds?: (number[]|null);

        /** GetClassXsRequest includeSchool */
        includeSchool?: (boolean|null);

        /** GetClassXsRequest includeAssignments */
        includeAssignments?: (boolean|null);

        /** GetClassXsRequest includeKnowledgeAndSkills */
        includeKnowledgeAndSkills?: (boolean|null);
    }

    /** Represents a GetClassXsRequest. */
    class GetClassXsRequest implements IGetClassXsRequest {

        /**
         * Constructs a new GetClassXsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: class_x_management_service.IGetClassXsRequest);

        /** GetClassXsRequest schoolIds. */
        public schoolIds: number[];

        /** GetClassXsRequest classXIds. */
        public classXIds: number[];

        /** GetClassXsRequest teacherIds. */
        public teacherIds: number[];

        /** GetClassXsRequest studentIds. */
        public studentIds: number[];

        /** GetClassXsRequest includeSchool. */
        public includeSchool?: (boolean|null);

        /** GetClassXsRequest includeAssignments. */
        public includeAssignments?: (boolean|null);

        /** GetClassXsRequest includeKnowledgeAndSkills. */
        public includeKnowledgeAndSkills?: (boolean|null);

        /** GetClassXsRequest _includeSchool. */
        public _includeSchool?: "includeSchool";

        /** GetClassXsRequest _includeAssignments. */
        public _includeAssignments?: "includeAssignments";

        /** GetClassXsRequest _includeKnowledgeAndSkills. */
        public _includeKnowledgeAndSkills?: "includeKnowledgeAndSkills";

        /**
         * Creates a new GetClassXsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetClassXsRequest instance
         */
        public static create(properties?: class_x_management_service.IGetClassXsRequest): class_x_management_service.GetClassXsRequest;

        /**
         * Encodes the specified GetClassXsRequest message. Does not implicitly {@link class_x_management_service.GetClassXsRequest.verify|verify} messages.
         * @param message GetClassXsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: class_x_management_service.IGetClassXsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetClassXsRequest message, length delimited. Does not implicitly {@link class_x_management_service.GetClassXsRequest.verify|verify} messages.
         * @param message GetClassXsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: class_x_management_service.IGetClassXsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetClassXsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetClassXsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): class_x_management_service.GetClassXsRequest;

        /**
         * Decodes a GetClassXsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetClassXsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): class_x_management_service.GetClassXsRequest;

        /**
         * Verifies a GetClassXsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetClassXsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetClassXsRequest
         */
        public static fromObject(object: { [k: string]: any }): class_x_management_service.GetClassXsRequest;

        /**
         * Creates a plain object from a GetClassXsRequest message. Also converts values to other types if specified.
         * @param message GetClassXsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: class_x_management_service.GetClassXsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetClassXsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetClassXsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetClassXsResponse. */
    interface IGetClassXsResponse {

        /** GetClassXsResponse classXs */
        classXs?: (pl_types.IClassX[]|null);
    }

    /** Represents a GetClassXsResponse. */
    class GetClassXsResponse implements IGetClassXsResponse {

        /**
         * Constructs a new GetClassXsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: class_x_management_service.IGetClassXsResponse);

        /** GetClassXsResponse classXs. */
        public classXs: pl_types.IClassX[];

        /**
         * Creates a new GetClassXsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetClassXsResponse instance
         */
        public static create(properties?: class_x_management_service.IGetClassXsResponse): class_x_management_service.GetClassXsResponse;

        /**
         * Encodes the specified GetClassXsResponse message. Does not implicitly {@link class_x_management_service.GetClassXsResponse.verify|verify} messages.
         * @param message GetClassXsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: class_x_management_service.IGetClassXsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetClassXsResponse message, length delimited. Does not implicitly {@link class_x_management_service.GetClassXsResponse.verify|verify} messages.
         * @param message GetClassXsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: class_x_management_service.IGetClassXsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetClassXsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetClassXsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): class_x_management_service.GetClassXsResponse;

        /**
         * Decodes a GetClassXsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetClassXsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): class_x_management_service.GetClassXsResponse;

        /**
         * Verifies a GetClassXsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetClassXsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetClassXsResponse
         */
        public static fromObject(object: { [k: string]: any }): class_x_management_service.GetClassXsResponse;

        /**
         * Creates a plain object from a GetClassXsResponse message. Also converts values to other types if specified.
         * @param message GetClassXsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: class_x_management_service.GetClassXsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetClassXsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetClassXsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertClassXRequest. */
    interface IUpsertClassXRequest {

        /** UpsertClassXRequest classX */
        classX?: (pl_types.IClassX|null);
    }

    /** Represents an UpsertClassXRequest. */
    class UpsertClassXRequest implements IUpsertClassXRequest {

        /**
         * Constructs a new UpsertClassXRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: class_x_management_service.IUpsertClassXRequest);

        /** UpsertClassXRequest classX. */
        public classX?: (pl_types.IClassX|null);

        /**
         * Creates a new UpsertClassXRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertClassXRequest instance
         */
        public static create(properties?: class_x_management_service.IUpsertClassXRequest): class_x_management_service.UpsertClassXRequest;

        /**
         * Encodes the specified UpsertClassXRequest message. Does not implicitly {@link class_x_management_service.UpsertClassXRequest.verify|verify} messages.
         * @param message UpsertClassXRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: class_x_management_service.IUpsertClassXRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertClassXRequest message, length delimited. Does not implicitly {@link class_x_management_service.UpsertClassXRequest.verify|verify} messages.
         * @param message UpsertClassXRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: class_x_management_service.IUpsertClassXRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertClassXRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertClassXRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): class_x_management_service.UpsertClassXRequest;

        /**
         * Decodes an UpsertClassXRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertClassXRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): class_x_management_service.UpsertClassXRequest;

        /**
         * Verifies an UpsertClassXRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertClassXRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertClassXRequest
         */
        public static fromObject(object: { [k: string]: any }): class_x_management_service.UpsertClassXRequest;

        /**
         * Creates a plain object from an UpsertClassXRequest message. Also converts values to other types if specified.
         * @param message UpsertClassXRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: class_x_management_service.UpsertClassXRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertClassXRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertClassXRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertClassXResponse. */
    interface IUpsertClassXResponse {

        /** UpsertClassXResponse classX */
        classX?: (pl_types.IClassX|null);
    }

    /** Represents an UpsertClassXResponse. */
    class UpsertClassXResponse implements IUpsertClassXResponse {

        /**
         * Constructs a new UpsertClassXResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: class_x_management_service.IUpsertClassXResponse);

        /** UpsertClassXResponse classX. */
        public classX?: (pl_types.IClassX|null);

        /**
         * Creates a new UpsertClassXResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertClassXResponse instance
         */
        public static create(properties?: class_x_management_service.IUpsertClassXResponse): class_x_management_service.UpsertClassXResponse;

        /**
         * Encodes the specified UpsertClassXResponse message. Does not implicitly {@link class_x_management_service.UpsertClassXResponse.verify|verify} messages.
         * @param message UpsertClassXResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: class_x_management_service.IUpsertClassXResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertClassXResponse message, length delimited. Does not implicitly {@link class_x_management_service.UpsertClassXResponse.verify|verify} messages.
         * @param message UpsertClassXResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: class_x_management_service.IUpsertClassXResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertClassXResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertClassXResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): class_x_management_service.UpsertClassXResponse;

        /**
         * Decodes an UpsertClassXResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertClassXResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): class_x_management_service.UpsertClassXResponse;

        /**
         * Verifies an UpsertClassXResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertClassXResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertClassXResponse
         */
        public static fromObject(object: { [k: string]: any }): class_x_management_service.UpsertClassXResponse;

        /**
         * Creates a plain object from an UpsertClassXResponse message. Also converts values to other types if specified.
         * @param message UpsertClassXResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: class_x_management_service.UpsertClassXResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertClassXResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertClassXResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace district_management. */
export namespace district_management {

    /** Represents a DistrictManagementService */
    class DistrictManagementService extends $protobuf.rpc.Service {

        /**
         * Constructs a new DistrictManagementService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new DistrictManagementService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): DistrictManagementService;

        /**
         * Calls GetDistricts.
         * @param request GetDistrictsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and DistrictInformationResponse
         */
        public getDistricts(request: district_management.IGetDistrictsRequest, callback: district_management.DistrictManagementService.GetDistrictsCallback): void;

        /**
         * Calls GetDistricts.
         * @param request GetDistrictsRequest message or plain object
         * @returns Promise
         */
        public getDistricts(request: district_management.IGetDistrictsRequest): Promise<district_management.DistrictInformationResponse>;

        /**
         * Calls AddDistrict.
         * @param request AddDistrictRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and DistrictInformationResponse
         */
        public addDistrict(request: district_management.IAddDistrictRequest, callback: district_management.DistrictManagementService.AddDistrictCallback): void;

        /**
         * Calls AddDistrict.
         * @param request AddDistrictRequest message or plain object
         * @returns Promise
         */
        public addDistrict(request: district_management.IAddDistrictRequest): Promise<district_management.DistrictInformationResponse>;

        /**
         * Calls UpdateDistrict.
         * @param request UpdateDistrictRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and DistrictInformationResponse
         */
        public updateDistrict(request: district_management.IUpdateDistrictRequest, callback: district_management.DistrictManagementService.UpdateDistrictCallback): void;

        /**
         * Calls UpdateDistrict.
         * @param request UpdateDistrictRequest message or plain object
         * @returns Promise
         */
        public updateDistrict(request: district_management.IUpdateDistrictRequest): Promise<district_management.DistrictInformationResponse>;

        /**
         * Calls RemoveDistrict.
         * @param request RemoveDistrictRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and DistrictInformationResponse
         */
        public removeDistrict(request: district_management.IRemoveDistrictRequest, callback: district_management.DistrictManagementService.RemoveDistrictCallback): void;

        /**
         * Calls RemoveDistrict.
         * @param request RemoveDistrictRequest message or plain object
         * @returns Promise
         */
        public removeDistrict(request: district_management.IRemoveDistrictRequest): Promise<district_management.DistrictInformationResponse>;
    }

    namespace DistrictManagementService {

        /**
         * Callback as used by {@link district_management.DistrictManagementService#getDistricts}.
         * @param error Error, if any
         * @param [response] DistrictInformationResponse
         */
        type GetDistrictsCallback = (error: (Error|null), response?: district_management.DistrictInformationResponse) => void;

        /**
         * Callback as used by {@link district_management.DistrictManagementService#addDistrict}.
         * @param error Error, if any
         * @param [response] DistrictInformationResponse
         */
        type AddDistrictCallback = (error: (Error|null), response?: district_management.DistrictInformationResponse) => void;

        /**
         * Callback as used by {@link district_management.DistrictManagementService#updateDistrict}.
         * @param error Error, if any
         * @param [response] DistrictInformationResponse
         */
        type UpdateDistrictCallback = (error: (Error|null), response?: district_management.DistrictInformationResponse) => void;

        /**
         * Callback as used by {@link district_management.DistrictManagementService#removeDistrict}.
         * @param error Error, if any
         * @param [response] DistrictInformationResponse
         */
        type RemoveDistrictCallback = (error: (Error|null), response?: district_management.DistrictInformationResponse) => void;
    }

    /** Properties of a GetDistrictsRequest. */
    interface IGetDistrictsRequest {
    }

    /** Represents a GetDistrictsRequest. */
    class GetDistrictsRequest implements IGetDistrictsRequest {

        /**
         * Constructs a new GetDistrictsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: district_management.IGetDistrictsRequest);

        /**
         * Creates a new GetDistrictsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetDistrictsRequest instance
         */
        public static create(properties?: district_management.IGetDistrictsRequest): district_management.GetDistrictsRequest;

        /**
         * Encodes the specified GetDistrictsRequest message. Does not implicitly {@link district_management.GetDistrictsRequest.verify|verify} messages.
         * @param message GetDistrictsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: district_management.IGetDistrictsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetDistrictsRequest message, length delimited. Does not implicitly {@link district_management.GetDistrictsRequest.verify|verify} messages.
         * @param message GetDistrictsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: district_management.IGetDistrictsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetDistrictsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetDistrictsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): district_management.GetDistrictsRequest;

        /**
         * Decodes a GetDistrictsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetDistrictsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): district_management.GetDistrictsRequest;

        /**
         * Verifies a GetDistrictsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetDistrictsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetDistrictsRequest
         */
        public static fromObject(object: { [k: string]: any }): district_management.GetDistrictsRequest;

        /**
         * Creates a plain object from a GetDistrictsRequest message. Also converts values to other types if specified.
         * @param message GetDistrictsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: district_management.GetDistrictsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetDistrictsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetDistrictsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an AddDistrictRequest. */
    interface IAddDistrictRequest {

        /** AddDistrictRequest district */
        district?: (pl_types.IDistrict|null);
    }

    /** Represents an AddDistrictRequest. */
    class AddDistrictRequest implements IAddDistrictRequest {

        /**
         * Constructs a new AddDistrictRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: district_management.IAddDistrictRequest);

        /** AddDistrictRequest district. */
        public district?: (pl_types.IDistrict|null);

        /** AddDistrictRequest _district. */
        public _district?: "district";

        /**
         * Creates a new AddDistrictRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns AddDistrictRequest instance
         */
        public static create(properties?: district_management.IAddDistrictRequest): district_management.AddDistrictRequest;

        /**
         * Encodes the specified AddDistrictRequest message. Does not implicitly {@link district_management.AddDistrictRequest.verify|verify} messages.
         * @param message AddDistrictRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: district_management.IAddDistrictRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified AddDistrictRequest message, length delimited. Does not implicitly {@link district_management.AddDistrictRequest.verify|verify} messages.
         * @param message AddDistrictRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: district_management.IAddDistrictRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an AddDistrictRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns AddDistrictRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): district_management.AddDistrictRequest;

        /**
         * Decodes an AddDistrictRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns AddDistrictRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): district_management.AddDistrictRequest;

        /**
         * Verifies an AddDistrictRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an AddDistrictRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns AddDistrictRequest
         */
        public static fromObject(object: { [k: string]: any }): district_management.AddDistrictRequest;

        /**
         * Creates a plain object from an AddDistrictRequest message. Also converts values to other types if specified.
         * @param message AddDistrictRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: district_management.AddDistrictRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this AddDistrictRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for AddDistrictRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpdateDistrictRequest. */
    interface IUpdateDistrictRequest {

        /** UpdateDistrictRequest district */
        district?: (pl_types.IDistrict|null);
    }

    /** Represents an UpdateDistrictRequest. */
    class UpdateDistrictRequest implements IUpdateDistrictRequest {

        /**
         * Constructs a new UpdateDistrictRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: district_management.IUpdateDistrictRequest);

        /** UpdateDistrictRequest district. */
        public district?: (pl_types.IDistrict|null);

        /** UpdateDistrictRequest _district. */
        public _district?: "district";

        /**
         * Creates a new UpdateDistrictRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpdateDistrictRequest instance
         */
        public static create(properties?: district_management.IUpdateDistrictRequest): district_management.UpdateDistrictRequest;

        /**
         * Encodes the specified UpdateDistrictRequest message. Does not implicitly {@link district_management.UpdateDistrictRequest.verify|verify} messages.
         * @param message UpdateDistrictRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: district_management.IUpdateDistrictRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpdateDistrictRequest message, length delimited. Does not implicitly {@link district_management.UpdateDistrictRequest.verify|verify} messages.
         * @param message UpdateDistrictRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: district_management.IUpdateDistrictRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpdateDistrictRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpdateDistrictRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): district_management.UpdateDistrictRequest;

        /**
         * Decodes an UpdateDistrictRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpdateDistrictRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): district_management.UpdateDistrictRequest;

        /**
         * Verifies an UpdateDistrictRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpdateDistrictRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpdateDistrictRequest
         */
        public static fromObject(object: { [k: string]: any }): district_management.UpdateDistrictRequest;

        /**
         * Creates a plain object from an UpdateDistrictRequest message. Also converts values to other types if specified.
         * @param message UpdateDistrictRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: district_management.UpdateDistrictRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpdateDistrictRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpdateDistrictRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a RemoveDistrictRequest. */
    interface IRemoveDistrictRequest {

        /** RemoveDistrictRequest districtId */
        districtId?: (number|null);
    }

    /** Represents a RemoveDistrictRequest. */
    class RemoveDistrictRequest implements IRemoveDistrictRequest {

        /**
         * Constructs a new RemoveDistrictRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: district_management.IRemoveDistrictRequest);

        /** RemoveDistrictRequest districtId. */
        public districtId?: (number|null);

        /** RemoveDistrictRequest _districtId. */
        public _districtId?: "districtId";

        /**
         * Creates a new RemoveDistrictRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns RemoveDistrictRequest instance
         */
        public static create(properties?: district_management.IRemoveDistrictRequest): district_management.RemoveDistrictRequest;

        /**
         * Encodes the specified RemoveDistrictRequest message. Does not implicitly {@link district_management.RemoveDistrictRequest.verify|verify} messages.
         * @param message RemoveDistrictRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: district_management.IRemoveDistrictRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified RemoveDistrictRequest message, length delimited. Does not implicitly {@link district_management.RemoveDistrictRequest.verify|verify} messages.
         * @param message RemoveDistrictRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: district_management.IRemoveDistrictRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a RemoveDistrictRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns RemoveDistrictRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): district_management.RemoveDistrictRequest;

        /**
         * Decodes a RemoveDistrictRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns RemoveDistrictRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): district_management.RemoveDistrictRequest;

        /**
         * Verifies a RemoveDistrictRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a RemoveDistrictRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns RemoveDistrictRequest
         */
        public static fromObject(object: { [k: string]: any }): district_management.RemoveDistrictRequest;

        /**
         * Creates a plain object from a RemoveDistrictRequest message. Also converts values to other types if specified.
         * @param message RemoveDistrictRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: district_management.RemoveDistrictRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this RemoveDistrictRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for RemoveDistrictRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a DistrictInformationResponse. */
    interface IDistrictInformationResponse {

        /** DistrictInformationResponse modifiedDistrictId */
        modifiedDistrictId?: (number|null);

        /** DistrictInformationResponse districts */
        districts?: (pl_types.IDistrict[]|null);
    }

    /** Represents a DistrictInformationResponse. */
    class DistrictInformationResponse implements IDistrictInformationResponse {

        /**
         * Constructs a new DistrictInformationResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: district_management.IDistrictInformationResponse);

        /** DistrictInformationResponse modifiedDistrictId. */
        public modifiedDistrictId?: (number|null);

        /** DistrictInformationResponse districts. */
        public districts: pl_types.IDistrict[];

        /** DistrictInformationResponse _modifiedDistrictId. */
        public _modifiedDistrictId?: "modifiedDistrictId";

        /**
         * Creates a new DistrictInformationResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns DistrictInformationResponse instance
         */
        public static create(properties?: district_management.IDistrictInformationResponse): district_management.DistrictInformationResponse;

        /**
         * Encodes the specified DistrictInformationResponse message. Does not implicitly {@link district_management.DistrictInformationResponse.verify|verify} messages.
         * @param message DistrictInformationResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: district_management.IDistrictInformationResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified DistrictInformationResponse message, length delimited. Does not implicitly {@link district_management.DistrictInformationResponse.verify|verify} messages.
         * @param message DistrictInformationResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: district_management.IDistrictInformationResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a DistrictInformationResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns DistrictInformationResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): district_management.DistrictInformationResponse;

        /**
         * Decodes a DistrictInformationResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns DistrictInformationResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): district_management.DistrictInformationResponse;

        /**
         * Verifies a DistrictInformationResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a DistrictInformationResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns DistrictInformationResponse
         */
        public static fromObject(object: { [k: string]: any }): district_management.DistrictInformationResponse;

        /**
         * Creates a plain object from a DistrictInformationResponse message. Also converts values to other types if specified.
         * @param message DistrictInformationResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: district_management.DistrictInformationResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this DistrictInformationResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for DistrictInformationResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace error_service. */
export namespace error_service {

    /** Represents an ErrorService */
    class ErrorService extends $protobuf.rpc.Service {

        /**
         * Constructs a new ErrorService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new ErrorService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): ErrorService;

        /**
         * Calls ReportError.
         * @param request ReportErrorRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and ReportErrorResponse
         */
        public reportError(request: error_service.IReportErrorRequest, callback: error_service.ErrorService.ReportErrorCallback): void;

        /**
         * Calls ReportError.
         * @param request ReportErrorRequest message or plain object
         * @returns Promise
         */
        public reportError(request: error_service.IReportErrorRequest): Promise<error_service.ReportErrorResponse>;
    }

    namespace ErrorService {

        /**
         * Callback as used by {@link error_service.ErrorService#reportError}.
         * @param error Error, if any
         * @param [response] ReportErrorResponse
         */
        type ReportErrorCallback = (error: (Error|null), response?: error_service.ReportErrorResponse) => void;
    }

    /** Properties of a ReportErrorRequest. */
    interface IReportErrorRequest {

        /** ReportErrorRequest name */
        name?: (string|null);

        /** ReportErrorRequest message */
        message?: (string|null);

        /** ReportErrorRequest stack */
        stack?: (string|null);

        /** ReportErrorRequest url */
        url?: (string|null);

        /** ReportErrorRequest request */
        request?: (error_service.ReportErrorRequest.IRequest|null);

        /** ReportErrorRequest response */
        response?: (error_service.ReportErrorRequest.IResponse|null);
    }

    /** Represents a ReportErrorRequest. */
    class ReportErrorRequest implements IReportErrorRequest {

        /**
         * Constructs a new ReportErrorRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: error_service.IReportErrorRequest);

        /** ReportErrorRequest name. */
        public name?: (string|null);

        /** ReportErrorRequest message. */
        public message?: (string|null);

        /** ReportErrorRequest stack. */
        public stack?: (string|null);

        /** ReportErrorRequest url. */
        public url?: (string|null);

        /** ReportErrorRequest request. */
        public request?: (error_service.ReportErrorRequest.IRequest|null);

        /** ReportErrorRequest response. */
        public response?: (error_service.ReportErrorRequest.IResponse|null);

        /** ReportErrorRequest _name. */
        public _name?: "name";

        /** ReportErrorRequest _message. */
        public _message?: "message";

        /** ReportErrorRequest _stack. */
        public _stack?: "stack";

        /** ReportErrorRequest _url. */
        public _url?: "url";

        /** ReportErrorRequest _request. */
        public _request?: "request";

        /** ReportErrorRequest _response. */
        public _response?: "response";

        /**
         * Creates a new ReportErrorRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ReportErrorRequest instance
         */
        public static create(properties?: error_service.IReportErrorRequest): error_service.ReportErrorRequest;

        /**
         * Encodes the specified ReportErrorRequest message. Does not implicitly {@link error_service.ReportErrorRequest.verify|verify} messages.
         * @param message ReportErrorRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: error_service.IReportErrorRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ReportErrorRequest message, length delimited. Does not implicitly {@link error_service.ReportErrorRequest.verify|verify} messages.
         * @param message ReportErrorRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: error_service.IReportErrorRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ReportErrorRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ReportErrorRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): error_service.ReportErrorRequest;

        /**
         * Decodes a ReportErrorRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ReportErrorRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): error_service.ReportErrorRequest;

        /**
         * Verifies a ReportErrorRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ReportErrorRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ReportErrorRequest
         */
        public static fromObject(object: { [k: string]: any }): error_service.ReportErrorRequest;

        /**
         * Creates a plain object from a ReportErrorRequest message. Also converts values to other types if specified.
         * @param message ReportErrorRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: error_service.ReportErrorRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ReportErrorRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ReportErrorRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace ReportErrorRequest {

        /** Properties of a Request. */
        interface IRequest {

            /** Request url */
            url?: (string|null);

            /** Request body */
            body?: (Uint8Array|null);
        }

        /** Represents a Request. */
        class Request implements IRequest {

            /**
             * Constructs a new Request.
             * @param [properties] Properties to set
             */
            constructor(properties?: error_service.ReportErrorRequest.IRequest);

            /** Request url. */
            public url?: (string|null);

            /** Request body. */
            public body?: (Uint8Array|null);

            /** Request _url. */
            public _url?: "url";

            /** Request _body. */
            public _body?: "body";

            /**
             * Creates a new Request instance using the specified properties.
             * @param [properties] Properties to set
             * @returns Request instance
             */
            public static create(properties?: error_service.ReportErrorRequest.IRequest): error_service.ReportErrorRequest.Request;

            /**
             * Encodes the specified Request message. Does not implicitly {@link error_service.ReportErrorRequest.Request.verify|verify} messages.
             * @param message Request message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encode(message: error_service.ReportErrorRequest.IRequest, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Encodes the specified Request message, length delimited. Does not implicitly {@link error_service.ReportErrorRequest.Request.verify|verify} messages.
             * @param message Request message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encodeDelimited(message: error_service.ReportErrorRequest.IRequest, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Decodes a Request message from the specified reader or buffer.
             * @param reader Reader or buffer to decode from
             * @param [length] Message length if known beforehand
             * @returns Request
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): error_service.ReportErrorRequest.Request;

            /**
             * Decodes a Request message from the specified reader or buffer, length delimited.
             * @param reader Reader or buffer to decode from
             * @returns Request
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): error_service.ReportErrorRequest.Request;

            /**
             * Verifies a Request message.
             * @param message Plain object to verify
             * @returns `null` if valid, otherwise the reason why it is not
             */
            public static verify(message: { [k: string]: any }): (string|null);

            /**
             * Creates a Request message from a plain object. Also converts values to their respective internal types.
             * @param object Plain object
             * @returns Request
             */
            public static fromObject(object: { [k: string]: any }): error_service.ReportErrorRequest.Request;

            /**
             * Creates a plain object from a Request message. Also converts values to other types if specified.
             * @param message Request
             * @param [options] Conversion options
             * @returns Plain object
             */
            public static toObject(message: error_service.ReportErrorRequest.Request, options?: $protobuf.IConversionOptions): { [k: string]: any };

            /**
             * Converts this Request to JSON.
             * @returns JSON object
             */
            public toJSON(): { [k: string]: any };

            /**
             * Gets the default type url for Request
             * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
             * @returns The default type url
             */
            public static getTypeUrl(typeUrlPrefix?: string): string;
        }

        /** Properties of a Response. */
        interface IResponse {

            /** Response status */
            status?: (number|null);

            /** Response statusText */
            statusText?: (string|null);

            /** Response url */
            url?: (string|null);

            /** Response body */
            body?: (Uint8Array|null);
        }

        /** Represents a Response. */
        class Response implements IResponse {

            /**
             * Constructs a new Response.
             * @param [properties] Properties to set
             */
            constructor(properties?: error_service.ReportErrorRequest.IResponse);

            /** Response status. */
            public status?: (number|null);

            /** Response statusText. */
            public statusText?: (string|null);

            /** Response url. */
            public url?: (string|null);

            /** Response body. */
            public body?: (Uint8Array|null);

            /** Response _status. */
            public _status?: "status";

            /** Response _statusText. */
            public _statusText?: "statusText";

            /** Response _url. */
            public _url?: "url";

            /** Response _body. */
            public _body?: "body";

            /**
             * Creates a new Response instance using the specified properties.
             * @param [properties] Properties to set
             * @returns Response instance
             */
            public static create(properties?: error_service.ReportErrorRequest.IResponse): error_service.ReportErrorRequest.Response;

            /**
             * Encodes the specified Response message. Does not implicitly {@link error_service.ReportErrorRequest.Response.verify|verify} messages.
             * @param message Response message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encode(message: error_service.ReportErrorRequest.IResponse, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Encodes the specified Response message, length delimited. Does not implicitly {@link error_service.ReportErrorRequest.Response.verify|verify} messages.
             * @param message Response message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encodeDelimited(message: error_service.ReportErrorRequest.IResponse, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Decodes a Response message from the specified reader or buffer.
             * @param reader Reader or buffer to decode from
             * @param [length] Message length if known beforehand
             * @returns Response
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): error_service.ReportErrorRequest.Response;

            /**
             * Decodes a Response message from the specified reader or buffer, length delimited.
             * @param reader Reader or buffer to decode from
             * @returns Response
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): error_service.ReportErrorRequest.Response;

            /**
             * Verifies a Response message.
             * @param message Plain object to verify
             * @returns `null` if valid, otherwise the reason why it is not
             */
            public static verify(message: { [k: string]: any }): (string|null);

            /**
             * Creates a Response message from a plain object. Also converts values to their respective internal types.
             * @param object Plain object
             * @returns Response
             */
            public static fromObject(object: { [k: string]: any }): error_service.ReportErrorRequest.Response;

            /**
             * Creates a plain object from a Response message. Also converts values to other types if specified.
             * @param message Response
             * @param [options] Conversion options
             * @returns Plain object
             */
            public static toObject(message: error_service.ReportErrorRequest.Response, options?: $protobuf.IConversionOptions): { [k: string]: any };

            /**
             * Converts this Response to JSON.
             * @returns JSON object
             */
            public toJSON(): { [k: string]: any };

            /**
             * Gets the default type url for Response
             * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
             * @returns The default type url
             */
            public static getTypeUrl(typeUrlPrefix?: string): string;
        }
    }

    /** Properties of a ReportErrorResponse. */
    interface IReportErrorResponse {

        /** ReportErrorResponse failureReason */
        failureReason?: (string|null);

        /** ReportErrorResponse issueLink */
        issueLink?: (string|null);
    }

    /** Represents a ReportErrorResponse. */
    class ReportErrorResponse implements IReportErrorResponse {

        /**
         * Constructs a new ReportErrorResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: error_service.IReportErrorResponse);

        /** ReportErrorResponse failureReason. */
        public failureReason?: (string|null);

        /** ReportErrorResponse issueLink. */
        public issueLink?: (string|null);

        /** ReportErrorResponse _failureReason. */
        public _failureReason?: "failureReason";

        /** ReportErrorResponse _issueLink. */
        public _issueLink?: "issueLink";

        /**
         * Creates a new ReportErrorResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ReportErrorResponse instance
         */
        public static create(properties?: error_service.IReportErrorResponse): error_service.ReportErrorResponse;

        /**
         * Encodes the specified ReportErrorResponse message. Does not implicitly {@link error_service.ReportErrorResponse.verify|verify} messages.
         * @param message ReportErrorResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: error_service.IReportErrorResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ReportErrorResponse message, length delimited. Does not implicitly {@link error_service.ReportErrorResponse.verify|verify} messages.
         * @param message ReportErrorResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: error_service.IReportErrorResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ReportErrorResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ReportErrorResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): error_service.ReportErrorResponse;

        /**
         * Decodes a ReportErrorResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ReportErrorResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): error_service.ReportErrorResponse;

        /**
         * Verifies a ReportErrorResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ReportErrorResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ReportErrorResponse
         */
        public static fromObject(object: { [k: string]: any }): error_service.ReportErrorResponse;

        /**
         * Creates a plain object from a ReportErrorResponse message. Also converts values to other types if specified.
         * @param message ReportErrorResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: error_service.ReportErrorResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ReportErrorResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ReportErrorResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace jira. */
export namespace jira {

    /** Properties of an ErrorResponse. */
    interface IErrorResponse {

        /** ErrorResponse errorMessages */
        errorMessages?: (string[]|null);

        /** ErrorResponse errors */
        errors?: ({ [k: string]: string }|null);

        /** ErrorResponse status */
        status?: (number|null);
    }

    /** Represents an ErrorResponse. */
    class ErrorResponse implements IErrorResponse {

        /**
         * Constructs a new ErrorResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IErrorResponse);

        /** ErrorResponse errorMessages. */
        public errorMessages: string[];

        /** ErrorResponse errors. */
        public errors: { [k: string]: string };

        /** ErrorResponse status. */
        public status?: (number|null);

        /** ErrorResponse _status. */
        public _status?: "status";

        /**
         * Creates a new ErrorResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ErrorResponse instance
         */
        public static create(properties?: jira.IErrorResponse): jira.ErrorResponse;

        /**
         * Encodes the specified ErrorResponse message. Does not implicitly {@link jira.ErrorResponse.verify|verify} messages.
         * @param message ErrorResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IErrorResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ErrorResponse message, length delimited. Does not implicitly {@link jira.ErrorResponse.verify|verify} messages.
         * @param message ErrorResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IErrorResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an ErrorResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ErrorResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.ErrorResponse;

        /**
         * Decodes an ErrorResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ErrorResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.ErrorResponse;

        /**
         * Verifies an ErrorResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an ErrorResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ErrorResponse
         */
        public static fromObject(object: { [k: string]: any }): jira.ErrorResponse;

        /**
         * Creates a plain object from an ErrorResponse message. Also converts values to other types if specified.
         * @param message ErrorResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.ErrorResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ErrorResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ErrorResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a LoginInfo. */
    interface ILoginInfo {

        /** LoginInfo failedLoginCount */
        failedLoginCount?: (number|null);

        /** LoginInfo loginCount */
        loginCount?: (number|null);

        /** LoginInfo lastFailedLoginTime */
        lastFailedLoginTime?: (string|null);

        /** LoginInfo previousLoginTime */
        previousLoginTime?: (string|null);
    }

    /** Represents a LoginInfo. */
    class LoginInfo implements ILoginInfo {

        /**
         * Constructs a new LoginInfo.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.ILoginInfo);

        /** LoginInfo failedLoginCount. */
        public failedLoginCount?: (number|null);

        /** LoginInfo loginCount. */
        public loginCount?: (number|null);

        /** LoginInfo lastFailedLoginTime. */
        public lastFailedLoginTime?: (string|null);

        /** LoginInfo previousLoginTime. */
        public previousLoginTime?: (string|null);

        /** LoginInfo _failedLoginCount. */
        public _failedLoginCount?: "failedLoginCount";

        /** LoginInfo _loginCount. */
        public _loginCount?: "loginCount";

        /** LoginInfo _lastFailedLoginTime. */
        public _lastFailedLoginTime?: "lastFailedLoginTime";

        /** LoginInfo _previousLoginTime. */
        public _previousLoginTime?: "previousLoginTime";

        /**
         * Creates a new LoginInfo instance using the specified properties.
         * @param [properties] Properties to set
         * @returns LoginInfo instance
         */
        public static create(properties?: jira.ILoginInfo): jira.LoginInfo;

        /**
         * Encodes the specified LoginInfo message. Does not implicitly {@link jira.LoginInfo.verify|verify} messages.
         * @param message LoginInfo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.ILoginInfo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified LoginInfo message, length delimited. Does not implicitly {@link jira.LoginInfo.verify|verify} messages.
         * @param message LoginInfo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.ILoginInfo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a LoginInfo message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns LoginInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.LoginInfo;

        /**
         * Decodes a LoginInfo message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns LoginInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.LoginInfo;

        /**
         * Verifies a LoginInfo message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a LoginInfo message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns LoginInfo
         */
        public static fromObject(object: { [k: string]: any }): jira.LoginInfo;

        /**
         * Creates a plain object from a LoginInfo message. Also converts values to other types if specified.
         * @param message LoginInfo
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.LoginInfo, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this LoginInfo to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for LoginInfo
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a CurrentUserResponse. */
    interface ICurrentUserResponse {

        /** CurrentUserResponse self */
        self?: (string|null);

        /** CurrentUserResponse name */
        name?: (string|null);

        /** CurrentUserResponse loginInfo */
        loginInfo?: (jira.ILoginInfo|null);
    }

    /** Represents a CurrentUserResponse. */
    class CurrentUserResponse implements ICurrentUserResponse {

        /**
         * Constructs a new CurrentUserResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.ICurrentUserResponse);

        /** CurrentUserResponse self. */
        public self?: (string|null);

        /** CurrentUserResponse name. */
        public name?: (string|null);

        /** CurrentUserResponse loginInfo. */
        public loginInfo?: (jira.ILoginInfo|null);

        /** CurrentUserResponse _self. */
        public _self?: "self";

        /** CurrentUserResponse _name. */
        public _name?: "name";

        /** CurrentUserResponse _loginInfo. */
        public _loginInfo?: "loginInfo";

        /**
         * Creates a new CurrentUserResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns CurrentUserResponse instance
         */
        public static create(properties?: jira.ICurrentUserResponse): jira.CurrentUserResponse;

        /**
         * Encodes the specified CurrentUserResponse message. Does not implicitly {@link jira.CurrentUserResponse.verify|verify} messages.
         * @param message CurrentUserResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.ICurrentUserResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified CurrentUserResponse message, length delimited. Does not implicitly {@link jira.CurrentUserResponse.verify|verify} messages.
         * @param message CurrentUserResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.ICurrentUserResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a CurrentUserResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns CurrentUserResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.CurrentUserResponse;

        /**
         * Decodes a CurrentUserResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns CurrentUserResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.CurrentUserResponse;

        /**
         * Verifies a CurrentUserResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a CurrentUserResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns CurrentUserResponse
         */
        public static fromObject(object: { [k: string]: any }): jira.CurrentUserResponse;

        /**
         * Creates a plain object from a CurrentUserResponse message. Also converts values to other types if specified.
         * @param message CurrentUserResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.CurrentUserResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this CurrentUserResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for CurrentUserResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a LoginRequest. */
    interface ILoginRequest {

        /** LoginRequest username */
        username?: (string|null);

        /** LoginRequest password */
        password?: (string|null);
    }

    /** Represents a LoginRequest. */
    class LoginRequest implements ILoginRequest {

        /**
         * Constructs a new LoginRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.ILoginRequest);

        /** LoginRequest username. */
        public username?: (string|null);

        /** LoginRequest password. */
        public password?: (string|null);

        /** LoginRequest _username. */
        public _username?: "username";

        /** LoginRequest _password. */
        public _password?: "password";

        /**
         * Creates a new LoginRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns LoginRequest instance
         */
        public static create(properties?: jira.ILoginRequest): jira.LoginRequest;

        /**
         * Encodes the specified LoginRequest message. Does not implicitly {@link jira.LoginRequest.verify|verify} messages.
         * @param message LoginRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.ILoginRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified LoginRequest message, length delimited. Does not implicitly {@link jira.LoginRequest.verify|verify} messages.
         * @param message LoginRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.ILoginRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a LoginRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns LoginRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.LoginRequest;

        /**
         * Decodes a LoginRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns LoginRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.LoginRequest;

        /**
         * Verifies a LoginRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a LoginRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns LoginRequest
         */
        public static fromObject(object: { [k: string]: any }): jira.LoginRequest;

        /**
         * Creates a plain object from a LoginRequest message. Also converts values to other types if specified.
         * @param message LoginRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.LoginRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this LoginRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for LoginRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a LoginResponse. */
    interface ILoginResponse {

        /** LoginResponse session */
        session?: (jira.LoginResponse.ISession|null);

        /** LoginResponse loginInfo */
        loginInfo?: (jira.ILoginInfo|null);
    }

    /** Represents a LoginResponse. */
    class LoginResponse implements ILoginResponse {

        /**
         * Constructs a new LoginResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.ILoginResponse);

        /** LoginResponse session. */
        public session?: (jira.LoginResponse.ISession|null);

        /** LoginResponse loginInfo. */
        public loginInfo?: (jira.ILoginInfo|null);

        /** LoginResponse _session. */
        public _session?: "session";

        /** LoginResponse _loginInfo. */
        public _loginInfo?: "loginInfo";

        /**
         * Creates a new LoginResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns LoginResponse instance
         */
        public static create(properties?: jira.ILoginResponse): jira.LoginResponse;

        /**
         * Encodes the specified LoginResponse message. Does not implicitly {@link jira.LoginResponse.verify|verify} messages.
         * @param message LoginResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.ILoginResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified LoginResponse message, length delimited. Does not implicitly {@link jira.LoginResponse.verify|verify} messages.
         * @param message LoginResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.ILoginResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a LoginResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns LoginResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.LoginResponse;

        /**
         * Decodes a LoginResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns LoginResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.LoginResponse;

        /**
         * Verifies a LoginResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a LoginResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns LoginResponse
         */
        public static fromObject(object: { [k: string]: any }): jira.LoginResponse;

        /**
         * Creates a plain object from a LoginResponse message. Also converts values to other types if specified.
         * @param message LoginResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.LoginResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this LoginResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for LoginResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace LoginResponse {

        /** Properties of a Session. */
        interface ISession {

            /** Session name */
            name?: (string|null);

            /** Session value */
            value?: (string|null);
        }

        /** Represents a Session. */
        class Session implements ISession {

            /**
             * Constructs a new Session.
             * @param [properties] Properties to set
             */
            constructor(properties?: jira.LoginResponse.ISession);

            /** Session name. */
            public name?: (string|null);

            /** Session value. */
            public value?: (string|null);

            /** Session _name. */
            public _name?: "name";

            /** Session _value. */
            public _value?: "value";

            /**
             * Creates a new Session instance using the specified properties.
             * @param [properties] Properties to set
             * @returns Session instance
             */
            public static create(properties?: jira.LoginResponse.ISession): jira.LoginResponse.Session;

            /**
             * Encodes the specified Session message. Does not implicitly {@link jira.LoginResponse.Session.verify|verify} messages.
             * @param message Session message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encode(message: jira.LoginResponse.ISession, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Encodes the specified Session message, length delimited. Does not implicitly {@link jira.LoginResponse.Session.verify|verify} messages.
             * @param message Session message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encodeDelimited(message: jira.LoginResponse.ISession, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Decodes a Session message from the specified reader or buffer.
             * @param reader Reader or buffer to decode from
             * @param [length] Message length if known beforehand
             * @returns Session
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.LoginResponse.Session;

            /**
             * Decodes a Session message from the specified reader or buffer, length delimited.
             * @param reader Reader or buffer to decode from
             * @returns Session
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.LoginResponse.Session;

            /**
             * Verifies a Session message.
             * @param message Plain object to verify
             * @returns `null` if valid, otherwise the reason why it is not
             */
            public static verify(message: { [k: string]: any }): (string|null);

            /**
             * Creates a Session message from a plain object. Also converts values to their respective internal types.
             * @param object Plain object
             * @returns Session
             */
            public static fromObject(object: { [k: string]: any }): jira.LoginResponse.Session;

            /**
             * Creates a plain object from a Session message. Also converts values to other types if specified.
             * @param message Session
             * @param [options] Conversion options
             * @returns Plain object
             */
            public static toObject(message: jira.LoginResponse.Session, options?: $protobuf.IConversionOptions): { [k: string]: any };

            /**
             * Converts this Session to JSON.
             * @returns JSON object
             */
            public toJSON(): { [k: string]: any };

            /**
             * Gets the default type url for Session
             * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
             * @returns The default type url
             */
            public static getTypeUrl(typeUrlPrefix?: string): string;
        }
    }

    /** Properties of a Parent. */
    interface IParent {

        /** Parent key */
        key?: (string|null);
    }

    /** Represents a Parent. */
    class Parent implements IParent {

        /**
         * Constructs a new Parent.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IParent);

        /** Parent key. */
        public key?: (string|null);

        /** Parent _key. */
        public _key?: "key";

        /**
         * Creates a new Parent instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Parent instance
         */
        public static create(properties?: jira.IParent): jira.Parent;

        /**
         * Encodes the specified Parent message. Does not implicitly {@link jira.Parent.verify|verify} messages.
         * @param message Parent message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IParent, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Parent message, length delimited. Does not implicitly {@link jira.Parent.verify|verify} messages.
         * @param message Parent message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IParent, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Parent message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Parent
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Parent;

        /**
         * Decodes a Parent message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Parent
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Parent;

        /**
         * Verifies a Parent message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Parent message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Parent
         */
        public static fromObject(object: { [k: string]: any }): jira.Parent;

        /**
         * Creates a plain object from a Parent message. Also converts values to other types if specified.
         * @param message Parent
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Parent, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Parent to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Parent
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a Worklog. */
    interface IWorklog {

        /** Worklog add */
        add?: (jira.Worklog.IAdd|null);
    }

    /** Represents a Worklog. */
    class Worklog implements IWorklog {

        /**
         * Constructs a new Worklog.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IWorklog);

        /** Worklog add. */
        public add?: (jira.Worklog.IAdd|null);

        /** Worklog _add. */
        public _add?: "add";

        /**
         * Creates a new Worklog instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Worklog instance
         */
        public static create(properties?: jira.IWorklog): jira.Worklog;

        /**
         * Encodes the specified Worklog message. Does not implicitly {@link jira.Worklog.verify|verify} messages.
         * @param message Worklog message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IWorklog, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Worklog message, length delimited. Does not implicitly {@link jira.Worklog.verify|verify} messages.
         * @param message Worklog message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IWorklog, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Worklog message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Worklog
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Worklog;

        /**
         * Decodes a Worklog message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Worklog
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Worklog;

        /**
         * Verifies a Worklog message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Worklog message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Worklog
         */
        public static fromObject(object: { [k: string]: any }): jira.Worklog;

        /**
         * Creates a plain object from a Worklog message. Also converts values to other types if specified.
         * @param message Worklog
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Worklog, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Worklog to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Worklog
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace Worklog {

        /** Properties of an Add. */
        interface IAdd {

            /** Add timeSpent */
            timeSpent?: (string|null);

            /** Add started */
            started?: (string|null);
        }

        /** Represents an Add. */
        class Add implements IAdd {

            /**
             * Constructs a new Add.
             * @param [properties] Properties to set
             */
            constructor(properties?: jira.Worklog.IAdd);

            /** Add timeSpent. */
            public timeSpent?: (string|null);

            /** Add started. */
            public started?: (string|null);

            /** Add _timeSpent. */
            public _timeSpent?: "timeSpent";

            /** Add _started. */
            public _started?: "started";

            /**
             * Creates a new Add instance using the specified properties.
             * @param [properties] Properties to set
             * @returns Add instance
             */
            public static create(properties?: jira.Worklog.IAdd): jira.Worklog.Add;

            /**
             * Encodes the specified Add message. Does not implicitly {@link jira.Worklog.Add.verify|verify} messages.
             * @param message Add message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encode(message: jira.Worklog.IAdd, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Encodes the specified Add message, length delimited. Does not implicitly {@link jira.Worklog.Add.verify|verify} messages.
             * @param message Add message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encodeDelimited(message: jira.Worklog.IAdd, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Decodes an Add message from the specified reader or buffer.
             * @param reader Reader or buffer to decode from
             * @param [length] Message length if known beforehand
             * @returns Add
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Worklog.Add;

            /**
             * Decodes an Add message from the specified reader or buffer, length delimited.
             * @param reader Reader or buffer to decode from
             * @returns Add
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Worklog.Add;

            /**
             * Verifies an Add message.
             * @param message Plain object to verify
             * @returns `null` if valid, otherwise the reason why it is not
             */
            public static verify(message: { [k: string]: any }): (string|null);

            /**
             * Creates an Add message from a plain object. Also converts values to their respective internal types.
             * @param object Plain object
             * @returns Add
             */
            public static fromObject(object: { [k: string]: any }): jira.Worklog.Add;

            /**
             * Creates a plain object from an Add message. Also converts values to other types if specified.
             * @param message Add
             * @param [options] Conversion options
             * @returns Plain object
             */
            public static toObject(message: jira.Worklog.Add, options?: $protobuf.IConversionOptions): { [k: string]: any };

            /**
             * Converts this Add to JSON.
             * @returns JSON object
             */
            public toJSON(): { [k: string]: any };

            /**
             * Gets the default type url for Add
             * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
             * @returns The default type url
             */
            public static getTypeUrl(typeUrlPrefix?: string): string;
        }
    }

    /** Properties of an Update. */
    interface IUpdate {

        /** Update worklog */
        worklog?: (jira.IWorklog[]|null);
    }

    /** Represents an Update. */
    class Update implements IUpdate {

        /**
         * Constructs a new Update.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IUpdate);

        /** Update worklog. */
        public worklog: jira.IWorklog[];

        /**
         * Creates a new Update instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Update instance
         */
        public static create(properties?: jira.IUpdate): jira.Update;

        /**
         * Encodes the specified Update message. Does not implicitly {@link jira.Update.verify|verify} messages.
         * @param message Update message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IUpdate, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Update message, length delimited. Does not implicitly {@link jira.Update.verify|verify} messages.
         * @param message Update message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IUpdate, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an Update message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Update
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Update;

        /**
         * Decodes an Update message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Update
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Update;

        /**
         * Verifies an Update message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an Update message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Update
         */
        public static fromObject(object: { [k: string]: any }): jira.Update;

        /**
         * Creates a plain object from an Update message. Also converts values to other types if specified.
         * @param message Update
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Update, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Update to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Update
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a Project. */
    interface IProject {

        /** Project id */
        id?: (string|null);

        /** Project key */
        key?: (string|null);
    }

    /** Represents a Project. */
    class Project implements IProject {

        /**
         * Constructs a new Project.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IProject);

        /** Project id. */
        public id?: (string|null);

        /** Project key. */
        public key?: (string|null);

        /** Project _id. */
        public _id?: "id";

        /** Project _key. */
        public _key?: "key";

        /**
         * Creates a new Project instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Project instance
         */
        public static create(properties?: jira.IProject): jira.Project;

        /**
         * Encodes the specified Project message. Does not implicitly {@link jira.Project.verify|verify} messages.
         * @param message Project message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IProject, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Project message, length delimited. Does not implicitly {@link jira.Project.verify|verify} messages.
         * @param message Project message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IProject, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Project message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Project
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Project;

        /**
         * Decodes a Project message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Project
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Project;

        /**
         * Verifies a Project message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Project message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Project
         */
        public static fromObject(object: { [k: string]: any }): jira.Project;

        /**
         * Creates a plain object from a Project message. Also converts values to other types if specified.
         * @param message Project
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Project, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Project to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Project
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an IssueType. */
    interface IIssueType {

        /** IssueType id */
        id?: (string|null);

        /** IssueType name */
        name?: (string|null);
    }

    /** Represents an IssueType. */
    class IssueType implements IIssueType {

        /**
         * Constructs a new IssueType.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IIssueType);

        /** IssueType id. */
        public id?: (string|null);

        /** IssueType name. */
        public name?: (string|null);

        /** IssueType _id. */
        public _id?: "id";

        /** IssueType _name. */
        public _name?: "name";

        /**
         * Creates a new IssueType instance using the specified properties.
         * @param [properties] Properties to set
         * @returns IssueType instance
         */
        public static create(properties?: jira.IIssueType): jira.IssueType;

        /**
         * Encodes the specified IssueType message. Does not implicitly {@link jira.IssueType.verify|verify} messages.
         * @param message IssueType message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IIssueType, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified IssueType message, length delimited. Does not implicitly {@link jira.IssueType.verify|verify} messages.
         * @param message IssueType message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IIssueType, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an IssueType message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns IssueType
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.IssueType;

        /**
         * Decodes an IssueType message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns IssueType
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.IssueType;

        /**
         * Verifies an IssueType message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an IssueType message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns IssueType
         */
        public static fromObject(object: { [k: string]: any }): jira.IssueType;

        /**
         * Creates a plain object from an IssueType message. Also converts values to other types if specified.
         * @param message IssueType
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.IssueType, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this IssueType to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for IssueType
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an Assignee. */
    interface IAssignee {

        /** Assignee id */
        id?: (string|null);
    }

    /** Represents an Assignee. */
    class Assignee implements IAssignee {

        /**
         * Constructs a new Assignee.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IAssignee);

        /** Assignee id. */
        public id?: (string|null);

        /** Assignee _id. */
        public _id?: "id";

        /**
         * Creates a new Assignee instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Assignee instance
         */
        public static create(properties?: jira.IAssignee): jira.Assignee;

        /**
         * Encodes the specified Assignee message. Does not implicitly {@link jira.Assignee.verify|verify} messages.
         * @param message Assignee message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IAssignee, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Assignee message, length delimited. Does not implicitly {@link jira.Assignee.verify|verify} messages.
         * @param message Assignee message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IAssignee, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an Assignee message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Assignee
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Assignee;

        /**
         * Decodes an Assignee message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Assignee
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Assignee;

        /**
         * Verifies an Assignee message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an Assignee message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Assignee
         */
        public static fromObject(object: { [k: string]: any }): jira.Assignee;

        /**
         * Creates a plain object from an Assignee message. Also converts values to other types if specified.
         * @param message Assignee
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Assignee, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Assignee to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Assignee
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a Reporter. */
    interface IReporter {

        /** Reporter id */
        id?: (string|null);
    }

    /** Represents a Reporter. */
    class Reporter implements IReporter {

        /**
         * Constructs a new Reporter.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IReporter);

        /** Reporter id. */
        public id?: (string|null);

        /** Reporter _id. */
        public _id?: "id";

        /**
         * Creates a new Reporter instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Reporter instance
         */
        public static create(properties?: jira.IReporter): jira.Reporter;

        /**
         * Encodes the specified Reporter message. Does not implicitly {@link jira.Reporter.verify|verify} messages.
         * @param message Reporter message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IReporter, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Reporter message, length delimited. Does not implicitly {@link jira.Reporter.verify|verify} messages.
         * @param message Reporter message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IReporter, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Reporter message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Reporter
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Reporter;

        /**
         * Decodes a Reporter message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Reporter
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Reporter;

        /**
         * Verifies a Reporter message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Reporter message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Reporter
         */
        public static fromObject(object: { [k: string]: any }): jira.Reporter;

        /**
         * Creates a plain object from a Reporter message. Also converts values to other types if specified.
         * @param message Reporter
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Reporter, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Reporter to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Reporter
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a Priority. */
    interface IPriority {

        /** Priority id */
        id?: (string|null);
    }

    /** Represents a Priority. */
    class Priority implements IPriority {

        /**
         * Constructs a new Priority.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IPriority);

        /** Priority id. */
        public id?: (string|null);

        /** Priority _id. */
        public _id?: "id";

        /**
         * Creates a new Priority instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Priority instance
         */
        public static create(properties?: jira.IPriority): jira.Priority;

        /**
         * Encodes the specified Priority message. Does not implicitly {@link jira.Priority.verify|verify} messages.
         * @param message Priority message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IPriority, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Priority message, length delimited. Does not implicitly {@link jira.Priority.verify|verify} messages.
         * @param message Priority message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IPriority, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Priority message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Priority
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Priority;

        /**
         * Decodes a Priority message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Priority
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Priority;

        /**
         * Verifies a Priority message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Priority message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Priority
         */
        public static fromObject(object: { [k: string]: any }): jira.Priority;

        /**
         * Creates a plain object from a Priority message. Also converts values to other types if specified.
         * @param message Priority
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Priority, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Priority to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Priority
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a TimeTracking. */
    interface ITimeTracking {

        /** TimeTracking originalEstimate */
        originalEstimate?: (string|null);

        /** TimeTracking remainingEstimate */
        remainingEstimate?: (string|null);
    }

    /** Represents a TimeTracking. */
    class TimeTracking implements ITimeTracking {

        /**
         * Constructs a new TimeTracking.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.ITimeTracking);

        /** TimeTracking originalEstimate. */
        public originalEstimate?: (string|null);

        /** TimeTracking remainingEstimate. */
        public remainingEstimate?: (string|null);

        /** TimeTracking _originalEstimate. */
        public _originalEstimate?: "originalEstimate";

        /** TimeTracking _remainingEstimate. */
        public _remainingEstimate?: "remainingEstimate";

        /**
         * Creates a new TimeTracking instance using the specified properties.
         * @param [properties] Properties to set
         * @returns TimeTracking instance
         */
        public static create(properties?: jira.ITimeTracking): jira.TimeTracking;

        /**
         * Encodes the specified TimeTracking message. Does not implicitly {@link jira.TimeTracking.verify|verify} messages.
         * @param message TimeTracking message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.ITimeTracking, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified TimeTracking message, length delimited. Does not implicitly {@link jira.TimeTracking.verify|verify} messages.
         * @param message TimeTracking message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.ITimeTracking, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a TimeTracking message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns TimeTracking
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.TimeTracking;

        /**
         * Decodes a TimeTracking message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns TimeTracking
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.TimeTracking;

        /**
         * Verifies a TimeTracking message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a TimeTracking message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns TimeTracking
         */
        public static fromObject(object: { [k: string]: any }): jira.TimeTracking;

        /**
         * Creates a plain object from a TimeTracking message. Also converts values to other types if specified.
         * @param message TimeTracking
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.TimeTracking, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this TimeTracking to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for TimeTracking
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a Security. */
    interface ISecurity {

        /** Security id */
        id?: (string|null);
    }

    /** Represents a Security. */
    class Security implements ISecurity {

        /**
         * Constructs a new Security.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.ISecurity);

        /** Security id. */
        public id?: (string|null);

        /** Security _id. */
        public _id?: "id";

        /**
         * Creates a new Security instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Security instance
         */
        public static create(properties?: jira.ISecurity): jira.Security;

        /**
         * Encodes the specified Security message. Does not implicitly {@link jira.Security.verify|verify} messages.
         * @param message Security message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.ISecurity, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Security message, length delimited. Does not implicitly {@link jira.Security.verify|verify} messages.
         * @param message Security message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.ISecurity, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Security message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Security
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Security;

        /**
         * Decodes a Security message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Security
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Security;

        /**
         * Verifies a Security message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Security message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Security
         */
        public static fromObject(object: { [k: string]: any }): jira.Security;

        /**
         * Creates a plain object from a Security message. Also converts values to other types if specified.
         * @param message Security
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Security, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Security to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Security
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a Version. */
    interface IVersion {

        /** Version id */
        id?: (string|null);
    }

    /** Represents a Version. */
    class Version implements IVersion {

        /**
         * Constructs a new Version.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IVersion);

        /** Version id. */
        public id?: (string|null);

        /** Version _id. */
        public _id?: "id";

        /**
         * Creates a new Version instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Version instance
         */
        public static create(properties?: jira.IVersion): jira.Version;

        /**
         * Encodes the specified Version message. Does not implicitly {@link jira.Version.verify|verify} messages.
         * @param message Version message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IVersion, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Version message, length delimited. Does not implicitly {@link jira.Version.verify|verify} messages.
         * @param message Version message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IVersion, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Version message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Version
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Version;

        /**
         * Decodes a Version message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Version
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Version;

        /**
         * Verifies a Version message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Version message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Version
         */
        public static fromObject(object: { [k: string]: any }): jira.Version;

        /**
         * Creates a plain object from a Version message. Also converts values to other types if specified.
         * @param message Version
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Version, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Version to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Version
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a Component. */
    interface IComponent {

        /** Component id */
        id?: (string|null);
    }

    /** Represents a Component. */
    class Component implements IComponent {

        /**
         * Constructs a new Component.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IComponent);

        /** Component id. */
        public id?: (string|null);

        /** Component _id. */
        public _id?: "id";

        /**
         * Creates a new Component instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Component instance
         */
        public static create(properties?: jira.IComponent): jira.Component;

        /**
         * Encodes the specified Component message. Does not implicitly {@link jira.Component.verify|verify} messages.
         * @param message Component message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IComponent, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Component message, length delimited. Does not implicitly {@link jira.Component.verify|verify} messages.
         * @param message Component message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IComponent, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a Component message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Component
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Component;

        /**
         * Decodes a Component message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Component
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Component;

        /**
         * Verifies a Component message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a Component message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Component
         */
        public static fromObject(object: { [k: string]: any }): jira.Component;

        /**
         * Creates a plain object from a Component message. Also converts values to other types if specified.
         * @param message Component
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Component, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Component to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Component
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an Error. */
    interface IError {

        /** Error priority */
        priority?: (string|null);
    }

    /** Represents an Error. */
    class Error implements IError {

        /**
         * Constructs a new Error.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IError);

        /** Error priority. */
        public priority?: (string|null);

        /** Error _priority. */
        public _priority?: "priority";

        /**
         * Creates a new Error instance using the specified properties.
         * @param [properties] Properties to set
         * @returns Error instance
         */
        public static create(properties?: jira.IError): jira.Error;

        /**
         * Encodes the specified Error message. Does not implicitly {@link jira.Error.verify|verify} messages.
         * @param message Error message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IError, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified Error message, length delimited. Does not implicitly {@link jira.Error.verify|verify} messages.
         * @param message Error message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IError, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an Error message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns Error
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.Error;

        /**
         * Decodes an Error message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns Error
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.Error;

        /**
         * Verifies an Error message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an Error message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns Error
         */
        public static fromObject(object: { [k: string]: any }): jira.Error;

        /**
         * Creates a plain object from an Error message. Also converts values to other types if specified.
         * @param message Error
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.Error, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this Error to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for Error
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a FindUsersAndGroupsRequest. */
    interface IFindUsersAndGroupsRequest {

        /** FindUsersAndGroupsRequest query */
        query?: (string|null);

        /** FindUsersAndGroupsRequest maxResults */
        maxResults?: (number|null);

        /** FindUsersAndGroupsRequest caseInsensitive */
        caseInsensitive?: (boolean|null);
    }

    /** Represents a FindUsersAndGroupsRequest. */
    class FindUsersAndGroupsRequest implements IFindUsersAndGroupsRequest {

        /**
         * Constructs a new FindUsersAndGroupsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IFindUsersAndGroupsRequest);

        /** FindUsersAndGroupsRequest query. */
        public query?: (string|null);

        /** FindUsersAndGroupsRequest maxResults. */
        public maxResults?: (number|null);

        /** FindUsersAndGroupsRequest caseInsensitive. */
        public caseInsensitive?: (boolean|null);

        /** FindUsersAndGroupsRequest _query. */
        public _query?: "query";

        /** FindUsersAndGroupsRequest _maxResults. */
        public _maxResults?: "maxResults";

        /** FindUsersAndGroupsRequest _caseInsensitive. */
        public _caseInsensitive?: "caseInsensitive";

        /**
         * Creates a new FindUsersAndGroupsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns FindUsersAndGroupsRequest instance
         */
        public static create(properties?: jira.IFindUsersAndGroupsRequest): jira.FindUsersAndGroupsRequest;

        /**
         * Encodes the specified FindUsersAndGroupsRequest message. Does not implicitly {@link jira.FindUsersAndGroupsRequest.verify|verify} messages.
         * @param message FindUsersAndGroupsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IFindUsersAndGroupsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified FindUsersAndGroupsRequest message, length delimited. Does not implicitly {@link jira.FindUsersAndGroupsRequest.verify|verify} messages.
         * @param message FindUsersAndGroupsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IFindUsersAndGroupsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a FindUsersAndGroupsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns FindUsersAndGroupsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.FindUsersAndGroupsRequest;

        /**
         * Decodes a FindUsersAndGroupsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns FindUsersAndGroupsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.FindUsersAndGroupsRequest;

        /**
         * Verifies a FindUsersAndGroupsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a FindUsersAndGroupsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns FindUsersAndGroupsRequest
         */
        public static fromObject(object: { [k: string]: any }): jira.FindUsersAndGroupsRequest;

        /**
         * Creates a plain object from a FindUsersAndGroupsRequest message. Also converts values to other types if specified.
         * @param message FindUsersAndGroupsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.FindUsersAndGroupsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this FindUsersAndGroupsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for FindUsersAndGroupsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a FindUsersAndGroupsResponse. */
    interface IFindUsersAndGroupsResponse {

        /** FindUsersAndGroupsResponse users */
        users?: (jira.FindUsersAndGroupsResponse.IFoundUsers|null);

        /** FindUsersAndGroupsResponse projectLeoError */
        projectLeoError?: (jira.IErrorResponse|null);
    }

    /** Represents a FindUsersAndGroupsResponse. */
    class FindUsersAndGroupsResponse implements IFindUsersAndGroupsResponse {

        /**
         * Constructs a new FindUsersAndGroupsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.IFindUsersAndGroupsResponse);

        /** FindUsersAndGroupsResponse users. */
        public users?: (jira.FindUsersAndGroupsResponse.IFoundUsers|null);

        /** FindUsersAndGroupsResponse projectLeoError. */
        public projectLeoError?: (jira.IErrorResponse|null);

        /** FindUsersAndGroupsResponse _users. */
        public _users?: "users";

        /** FindUsersAndGroupsResponse _projectLeoError. */
        public _projectLeoError?: "projectLeoError";

        /**
         * Creates a new FindUsersAndGroupsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns FindUsersAndGroupsResponse instance
         */
        public static create(properties?: jira.IFindUsersAndGroupsResponse): jira.FindUsersAndGroupsResponse;

        /**
         * Encodes the specified FindUsersAndGroupsResponse message. Does not implicitly {@link jira.FindUsersAndGroupsResponse.verify|verify} messages.
         * @param message FindUsersAndGroupsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.IFindUsersAndGroupsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified FindUsersAndGroupsResponse message, length delimited. Does not implicitly {@link jira.FindUsersAndGroupsResponse.verify|verify} messages.
         * @param message FindUsersAndGroupsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.IFindUsersAndGroupsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a FindUsersAndGroupsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns FindUsersAndGroupsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.FindUsersAndGroupsResponse;

        /**
         * Decodes a FindUsersAndGroupsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns FindUsersAndGroupsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.FindUsersAndGroupsResponse;

        /**
         * Verifies a FindUsersAndGroupsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a FindUsersAndGroupsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns FindUsersAndGroupsResponse
         */
        public static fromObject(object: { [k: string]: any }): jira.FindUsersAndGroupsResponse;

        /**
         * Creates a plain object from a FindUsersAndGroupsResponse message. Also converts values to other types if specified.
         * @param message FindUsersAndGroupsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.FindUsersAndGroupsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this FindUsersAndGroupsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for FindUsersAndGroupsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace FindUsersAndGroupsResponse {

        /** Properties of a FoundUsers. */
        interface IFoundUsers {

            /** FoundUsers users */
            users?: (jira.FindUsersAndGroupsResponse.FoundUsers.IUserPickerUser[]|null);
        }

        /** Represents a FoundUsers. */
        class FoundUsers implements IFoundUsers {

            /**
             * Constructs a new FoundUsers.
             * @param [properties] Properties to set
             */
            constructor(properties?: jira.FindUsersAndGroupsResponse.IFoundUsers);

            /** FoundUsers users. */
            public users: jira.FindUsersAndGroupsResponse.FoundUsers.IUserPickerUser[];

            /**
             * Creates a new FoundUsers instance using the specified properties.
             * @param [properties] Properties to set
             * @returns FoundUsers instance
             */
            public static create(properties?: jira.FindUsersAndGroupsResponse.IFoundUsers): jira.FindUsersAndGroupsResponse.FoundUsers;

            /**
             * Encodes the specified FoundUsers message. Does not implicitly {@link jira.FindUsersAndGroupsResponse.FoundUsers.verify|verify} messages.
             * @param message FoundUsers message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encode(message: jira.FindUsersAndGroupsResponse.IFoundUsers, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Encodes the specified FoundUsers message, length delimited. Does not implicitly {@link jira.FindUsersAndGroupsResponse.FoundUsers.verify|verify} messages.
             * @param message FoundUsers message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encodeDelimited(message: jira.FindUsersAndGroupsResponse.IFoundUsers, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Decodes a FoundUsers message from the specified reader or buffer.
             * @param reader Reader or buffer to decode from
             * @param [length] Message length if known beforehand
             * @returns FoundUsers
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.FindUsersAndGroupsResponse.FoundUsers;

            /**
             * Decodes a FoundUsers message from the specified reader or buffer, length delimited.
             * @param reader Reader or buffer to decode from
             * @returns FoundUsers
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.FindUsersAndGroupsResponse.FoundUsers;

            /**
             * Verifies a FoundUsers message.
             * @param message Plain object to verify
             * @returns `null` if valid, otherwise the reason why it is not
             */
            public static verify(message: { [k: string]: any }): (string|null);

            /**
             * Creates a FoundUsers message from a plain object. Also converts values to their respective internal types.
             * @param object Plain object
             * @returns FoundUsers
             */
            public static fromObject(object: { [k: string]: any }): jira.FindUsersAndGroupsResponse.FoundUsers;

            /**
             * Creates a plain object from a FoundUsers message. Also converts values to other types if specified.
             * @param message FoundUsers
             * @param [options] Conversion options
             * @returns Plain object
             */
            public static toObject(message: jira.FindUsersAndGroupsResponse.FoundUsers, options?: $protobuf.IConversionOptions): { [k: string]: any };

            /**
             * Converts this FoundUsers to JSON.
             * @returns JSON object
             */
            public toJSON(): { [k: string]: any };

            /**
             * Gets the default type url for FoundUsers
             * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
             * @returns The default type url
             */
            public static getTypeUrl(typeUrlPrefix?: string): string;
        }

        namespace FoundUsers {

            /** Properties of a UserPickerUser. */
            interface IUserPickerUser {

                /** UserPickerUser accountId */
                accountId?: (string|null);

                /** UserPickerUser html */
                html?: (string|null);
            }

            /** Represents a UserPickerUser. */
            class UserPickerUser implements IUserPickerUser {

                /**
                 * Constructs a new UserPickerUser.
                 * @param [properties] Properties to set
                 */
                constructor(properties?: jira.FindUsersAndGroupsResponse.FoundUsers.IUserPickerUser);

                /** UserPickerUser accountId. */
                public accountId?: (string|null);

                /** UserPickerUser html. */
                public html?: (string|null);

                /** UserPickerUser _accountId. */
                public _accountId?: "accountId";

                /** UserPickerUser _html. */
                public _html?: "html";

                /**
                 * Creates a new UserPickerUser instance using the specified properties.
                 * @param [properties] Properties to set
                 * @returns UserPickerUser instance
                 */
                public static create(properties?: jira.FindUsersAndGroupsResponse.FoundUsers.IUserPickerUser): jira.FindUsersAndGroupsResponse.FoundUsers.UserPickerUser;

                /**
                 * Encodes the specified UserPickerUser message. Does not implicitly {@link jira.FindUsersAndGroupsResponse.FoundUsers.UserPickerUser.verify|verify} messages.
                 * @param message UserPickerUser message or plain object to encode
                 * @param [writer] Writer to encode to
                 * @returns Writer
                 */
                public static encode(message: jira.FindUsersAndGroupsResponse.FoundUsers.IUserPickerUser, writer?: $protobuf.Writer): $protobuf.Writer;

                /**
                 * Encodes the specified UserPickerUser message, length delimited. Does not implicitly {@link jira.FindUsersAndGroupsResponse.FoundUsers.UserPickerUser.verify|verify} messages.
                 * @param message UserPickerUser message or plain object to encode
                 * @param [writer] Writer to encode to
                 * @returns Writer
                 */
                public static encodeDelimited(message: jira.FindUsersAndGroupsResponse.FoundUsers.IUserPickerUser, writer?: $protobuf.Writer): $protobuf.Writer;

                /**
                 * Decodes a UserPickerUser message from the specified reader or buffer.
                 * @param reader Reader or buffer to decode from
                 * @param [length] Message length if known beforehand
                 * @returns UserPickerUser
                 * @throws {Error} If the payload is not a reader or valid buffer
                 * @throws {$protobuf.util.ProtocolError} If required fields are missing
                 */
                public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.FindUsersAndGroupsResponse.FoundUsers.UserPickerUser;

                /**
                 * Decodes a UserPickerUser message from the specified reader or buffer, length delimited.
                 * @param reader Reader or buffer to decode from
                 * @returns UserPickerUser
                 * @throws {Error} If the payload is not a reader or valid buffer
                 * @throws {$protobuf.util.ProtocolError} If required fields are missing
                 */
                public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.FindUsersAndGroupsResponse.FoundUsers.UserPickerUser;

                /**
                 * Verifies a UserPickerUser message.
                 * @param message Plain object to verify
                 * @returns `null` if valid, otherwise the reason why it is not
                 */
                public static verify(message: { [k: string]: any }): (string|null);

                /**
                 * Creates a UserPickerUser message from a plain object. Also converts values to their respective internal types.
                 * @param object Plain object
                 * @returns UserPickerUser
                 */
                public static fromObject(object: { [k: string]: any }): jira.FindUsersAndGroupsResponse.FoundUsers.UserPickerUser;

                /**
                 * Creates a plain object from a UserPickerUser message. Also converts values to other types if specified.
                 * @param message UserPickerUser
                 * @param [options] Conversion options
                 * @returns Plain object
                 */
                public static toObject(message: jira.FindUsersAndGroupsResponse.FoundUsers.UserPickerUser, options?: $protobuf.IConversionOptions): { [k: string]: any };

                /**
                 * Converts this UserPickerUser to JSON.
                 * @returns JSON object
                 */
                public toJSON(): { [k: string]: any };

                /**
                 * Gets the default type url for UserPickerUser
                 * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
                 * @returns The default type url
                 */
                public static getTypeUrl(typeUrlPrefix?: string): string;
            }
        }
    }

    /** Properties of a CreateIssueRequest. */
    interface ICreateIssueRequest {

        /** CreateIssueRequest update */
        update?: (jira.IUpdate|null);

        /** CreateIssueRequest fields */
        fields?: (jira.CreateIssueRequest.IFields|null);
    }

    /** Represents a CreateIssueRequest. */
    class CreateIssueRequest implements ICreateIssueRequest {

        /**
         * Constructs a new CreateIssueRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.ICreateIssueRequest);

        /** CreateIssueRequest update. */
        public update?: (jira.IUpdate|null);

        /** CreateIssueRequest fields. */
        public fields?: (jira.CreateIssueRequest.IFields|null);

        /** CreateIssueRequest _update. */
        public _update?: "update";

        /** CreateIssueRequest _fields. */
        public _fields?: "fields";

        /**
         * Creates a new CreateIssueRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns CreateIssueRequest instance
         */
        public static create(properties?: jira.ICreateIssueRequest): jira.CreateIssueRequest;

        /**
         * Encodes the specified CreateIssueRequest message. Does not implicitly {@link jira.CreateIssueRequest.verify|verify} messages.
         * @param message CreateIssueRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.ICreateIssueRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified CreateIssueRequest message, length delimited. Does not implicitly {@link jira.CreateIssueRequest.verify|verify} messages.
         * @param message CreateIssueRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.ICreateIssueRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a CreateIssueRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns CreateIssueRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.CreateIssueRequest;

        /**
         * Decodes a CreateIssueRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns CreateIssueRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.CreateIssueRequest;

        /**
         * Verifies a CreateIssueRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a CreateIssueRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns CreateIssueRequest
         */
        public static fromObject(object: { [k: string]: any }): jira.CreateIssueRequest;

        /**
         * Creates a plain object from a CreateIssueRequest message. Also converts values to other types if specified.
         * @param message CreateIssueRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.CreateIssueRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this CreateIssueRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for CreateIssueRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    namespace CreateIssueRequest {

        /** Properties of a Fields. */
        interface IFields {

            /** Fields project */
            project?: (jira.IProject|null);

            /** Fields summary */
            summary?: (string|null);

            /** Fields issuetype */
            issuetype?: (jira.IIssueType|null);

            /** Fields assignee */
            assignee?: (jira.IAssignee|null);

            /** Fields reporter */
            reporter?: (jira.IReporter|null);

            /** Fields priority */
            priority?: (jira.IPriority|null);

            /** Fields labels */
            labels?: (string[]|null);

            /** Fields timetracking */
            timetracking?: (jira.ITimeTracking|null);

            /** Fields security */
            security?: (jira.ISecurity|null);

            /** Fields versions */
            versions?: (jira.IVersion[]|null);

            /** Fields environment */
            environment?: (string|null);

            /** Fields description */
            description?: (string|null);

            /** Fields duedate */
            duedate?: (string|null);

            /** Fields fixVersions */
            fixVersions?: (jira.IVersion[]|null);

            /** Fields components */
            components?: (jira.IComponent[]|null);

            /** Fields parent */
            parent?: (jira.IParent|null);
        }

        /** Represents a Fields. */
        class Fields implements IFields {

            /**
             * Constructs a new Fields.
             * @param [properties] Properties to set
             */
            constructor(properties?: jira.CreateIssueRequest.IFields);

            /** Fields project. */
            public project?: (jira.IProject|null);

            /** Fields summary. */
            public summary?: (string|null);

            /** Fields issuetype. */
            public issuetype?: (jira.IIssueType|null);

            /** Fields assignee. */
            public assignee?: (jira.IAssignee|null);

            /** Fields reporter. */
            public reporter?: (jira.IReporter|null);

            /** Fields priority. */
            public priority?: (jira.IPriority|null);

            /** Fields labels. */
            public labels: string[];

            /** Fields timetracking. */
            public timetracking?: (jira.ITimeTracking|null);

            /** Fields security. */
            public security?: (jira.ISecurity|null);

            /** Fields versions. */
            public versions: jira.IVersion[];

            /** Fields environment. */
            public environment?: (string|null);

            /** Fields description. */
            public description?: (string|null);

            /** Fields duedate. */
            public duedate?: (string|null);

            /** Fields fixVersions. */
            public fixVersions: jira.IVersion[];

            /** Fields components. */
            public components: jira.IComponent[];

            /** Fields parent. */
            public parent?: (jira.IParent|null);

            /** Fields _project. */
            public _project?: "project";

            /** Fields _summary. */
            public _summary?: "summary";

            /** Fields _issuetype. */
            public _issuetype?: "issuetype";

            /** Fields _assignee. */
            public _assignee?: "assignee";

            /** Fields _reporter. */
            public _reporter?: "reporter";

            /** Fields _priority. */
            public _priority?: "priority";

            /** Fields _timetracking. */
            public _timetracking?: "timetracking";

            /** Fields _security. */
            public _security?: "security";

            /** Fields _environment. */
            public _environment?: "environment";

            /** Fields _description. */
            public _description?: "description";

            /** Fields _duedate. */
            public _duedate?: "duedate";

            /** Fields _parent. */
            public _parent?: "parent";

            /**
             * Creates a new Fields instance using the specified properties.
             * @param [properties] Properties to set
             * @returns Fields instance
             */
            public static create(properties?: jira.CreateIssueRequest.IFields): jira.CreateIssueRequest.Fields;

            /**
             * Encodes the specified Fields message. Does not implicitly {@link jira.CreateIssueRequest.Fields.verify|verify} messages.
             * @param message Fields message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encode(message: jira.CreateIssueRequest.IFields, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Encodes the specified Fields message, length delimited. Does not implicitly {@link jira.CreateIssueRequest.Fields.verify|verify} messages.
             * @param message Fields message or plain object to encode
             * @param [writer] Writer to encode to
             * @returns Writer
             */
            public static encodeDelimited(message: jira.CreateIssueRequest.IFields, writer?: $protobuf.Writer): $protobuf.Writer;

            /**
             * Decodes a Fields message from the specified reader or buffer.
             * @param reader Reader or buffer to decode from
             * @param [length] Message length if known beforehand
             * @returns Fields
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.CreateIssueRequest.Fields;

            /**
             * Decodes a Fields message from the specified reader or buffer, length delimited.
             * @param reader Reader or buffer to decode from
             * @returns Fields
             * @throws {Error} If the payload is not a reader or valid buffer
             * @throws {$protobuf.util.ProtocolError} If required fields are missing
             */
            public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.CreateIssueRequest.Fields;

            /**
             * Verifies a Fields message.
             * @param message Plain object to verify
             * @returns `null` if valid, otherwise the reason why it is not
             */
            public static verify(message: { [k: string]: any }): (string|null);

            /**
             * Creates a Fields message from a plain object. Also converts values to their respective internal types.
             * @param object Plain object
             * @returns Fields
             */
            public static fromObject(object: { [k: string]: any }): jira.CreateIssueRequest.Fields;

            /**
             * Creates a plain object from a Fields message. Also converts values to other types if specified.
             * @param message Fields
             * @param [options] Conversion options
             * @returns Plain object
             */
            public static toObject(message: jira.CreateIssueRequest.Fields, options?: $protobuf.IConversionOptions): { [k: string]: any };

            /**
             * Converts this Fields to JSON.
             * @returns JSON object
             */
            public toJSON(): { [k: string]: any };

            /**
             * Gets the default type url for Fields
             * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
             * @returns The default type url
             */
            public static getTypeUrl(typeUrlPrefix?: string): string;
        }
    }

    /** Properties of a CreateIssueResponse. */
    interface ICreateIssueResponse {

        /** CreateIssueResponse id */
        id?: (string|null);

        /** CreateIssueResponse key */
        key?: (string|null);

        /** CreateIssueResponse self */
        self?: (string|null);

        /** CreateIssueResponse projectLeoError */
        projectLeoError?: (jira.IErrorResponse|null);
    }

    /** Represents a CreateIssueResponse. */
    class CreateIssueResponse implements ICreateIssueResponse {

        /**
         * Constructs a new CreateIssueResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: jira.ICreateIssueResponse);

        /** CreateIssueResponse id. */
        public id?: (string|null);

        /** CreateIssueResponse key. */
        public key?: (string|null);

        /** CreateIssueResponse self. */
        public self?: (string|null);

        /** CreateIssueResponse projectLeoError. */
        public projectLeoError?: (jira.IErrorResponse|null);

        /** CreateIssueResponse _id. */
        public _id?: "id";

        /** CreateIssueResponse _key. */
        public _key?: "key";

        /** CreateIssueResponse _self. */
        public _self?: "self";

        /** CreateIssueResponse _projectLeoError. */
        public _projectLeoError?: "projectLeoError";

        /**
         * Creates a new CreateIssueResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns CreateIssueResponse instance
         */
        public static create(properties?: jira.ICreateIssueResponse): jira.CreateIssueResponse;

        /**
         * Encodes the specified CreateIssueResponse message. Does not implicitly {@link jira.CreateIssueResponse.verify|verify} messages.
         * @param message CreateIssueResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: jira.ICreateIssueResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified CreateIssueResponse message, length delimited. Does not implicitly {@link jira.CreateIssueResponse.verify|verify} messages.
         * @param message CreateIssueResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: jira.ICreateIssueResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a CreateIssueResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns CreateIssueResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): jira.CreateIssueResponse;

        /**
         * Decodes a CreateIssueResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns CreateIssueResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): jira.CreateIssueResponse;

        /**
         * Verifies a CreateIssueResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a CreateIssueResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns CreateIssueResponse
         */
        public static fromObject(object: { [k: string]: any }): jira.CreateIssueResponse;

        /**
         * Creates a plain object from a CreateIssueResponse message. Also converts values to other types if specified.
         * @param message CreateIssueResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: jira.CreateIssueResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this CreateIssueResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for CreateIssueResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace mailjet. */
export namespace mailjet {

    /** MailjetSendResponseStatus enum. */
    enum MailjetSendResponseStatus {
        UNKNOWN = 0,
        success = 1,
        error = 2
    }

    /** Properties of a MailjetSendRequest. */
    interface IMailjetSendRequest {

        /** MailjetSendRequest SandboxMode */
        SandboxMode?: (boolean|null);

        /** MailjetSendRequest AdvanceErrorHandling */
        AdvanceErrorHandling?: (boolean|null);

        /** MailjetSendRequest messages */
        messages?: (mailjet.IMailjetSendRequestMessage[]|null);
    }

    /** Represents a MailjetSendRequest. */
    class MailjetSendRequest implements IMailjetSendRequest {

        /**
         * Constructs a new MailjetSendRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendRequest);

        /** MailjetSendRequest SandboxMode. */
        public SandboxMode?: (boolean|null);

        /** MailjetSendRequest AdvanceErrorHandling. */
        public AdvanceErrorHandling?: (boolean|null);

        /** MailjetSendRequest messages. */
        public messages: mailjet.IMailjetSendRequestMessage[];

        /** MailjetSendRequest _SandboxMode. */
        public _SandboxMode?: "SandboxMode";

        /** MailjetSendRequest _AdvanceErrorHandling. */
        public _AdvanceErrorHandling?: "AdvanceErrorHandling";

        /**
         * Creates a new MailjetSendRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendRequest instance
         */
        public static create(properties?: mailjet.IMailjetSendRequest): mailjet.MailjetSendRequest;

        /**
         * Encodes the specified MailjetSendRequest message. Does not implicitly {@link mailjet.MailjetSendRequest.verify|verify} messages.
         * @param message MailjetSendRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendRequest message, length delimited. Does not implicitly {@link mailjet.MailjetSendRequest.verify|verify} messages.
         * @param message MailjetSendRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendRequest;

        /**
         * Decodes a MailjetSendRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendRequest;

        /**
         * Verifies a MailjetSendRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendRequest
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendRequest;

        /**
         * Creates a plain object from a MailjetSendRequest message. Also converts values to other types if specified.
         * @param message MailjetSendRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendResponse. */
    interface IMailjetSendResponse {

        /** MailjetSendResponse Messages */
        Messages?: (mailjet.IMailjetSendResponseMessage[]|null);
    }

    /** Represents a MailjetSendResponse. */
    class MailjetSendResponse implements IMailjetSendResponse {

        /**
         * Constructs a new MailjetSendResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendResponse);

        /** MailjetSendResponse Messages. */
        public Messages: mailjet.IMailjetSendResponseMessage[];

        /**
         * Creates a new MailjetSendResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendResponse instance
         */
        public static create(properties?: mailjet.IMailjetSendResponse): mailjet.MailjetSendResponse;

        /**
         * Encodes the specified MailjetSendResponse message. Does not implicitly {@link mailjet.MailjetSendResponse.verify|verify} messages.
         * @param message MailjetSendResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendResponse message, length delimited. Does not implicitly {@link mailjet.MailjetSendResponse.verify|verify} messages.
         * @param message MailjetSendResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendResponse;

        /**
         * Decodes a MailjetSendResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendResponse;

        /**
         * Verifies a MailjetSendResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendResponse
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendResponse;

        /**
         * Creates a plain object from a MailjetSendResponse message. Also converts values to other types if specified.
         * @param message MailjetSendResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendRequestMessage. */
    interface IMailjetSendRequestMessage {

        /** MailjetSendRequestMessage From */
        From?: (mailjet.IMailjetSendRequestFrom|null);

        /** MailjetSendRequestMessage To */
        To?: (mailjet.IMailjetSendRequestTo[]|null);

        /** MailjetSendRequestMessage ReplyTo */
        ReplyTo?: (mailjet.IMailjetSendRequestReplyTo|null);

        /** MailjetSendRequestMessage Subject */
        Subject?: (string|null);

        /** MailjetSendRequestMessage TextPart */
        TextPart?: (string|null);

        /** MailjetSendRequestMessage HTMLPart */
        HTMLPart?: (string|null);

        /** MailjetSendRequestMessage CustomID */
        CustomID?: (string|null);
    }

    /** Represents a MailjetSendRequestMessage. */
    class MailjetSendRequestMessage implements IMailjetSendRequestMessage {

        /**
         * Constructs a new MailjetSendRequestMessage.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendRequestMessage);

        /** MailjetSendRequestMessage From. */
        public From?: (mailjet.IMailjetSendRequestFrom|null);

        /** MailjetSendRequestMessage To. */
        public To: mailjet.IMailjetSendRequestTo[];

        /** MailjetSendRequestMessage ReplyTo. */
        public ReplyTo?: (mailjet.IMailjetSendRequestReplyTo|null);

        /** MailjetSendRequestMessage Subject. */
        public Subject?: (string|null);

        /** MailjetSendRequestMessage TextPart. */
        public TextPart?: (string|null);

        /** MailjetSendRequestMessage HTMLPart. */
        public HTMLPart?: (string|null);

        /** MailjetSendRequestMessage CustomID. */
        public CustomID?: (string|null);

        /** MailjetSendRequestMessage _ReplyTo. */
        public _ReplyTo?: "ReplyTo";

        /** MailjetSendRequestMessage _Subject. */
        public _Subject?: "Subject";

        /** MailjetSendRequestMessage _TextPart. */
        public _TextPart?: "TextPart";

        /** MailjetSendRequestMessage _HTMLPart. */
        public _HTMLPart?: "HTMLPart";

        /** MailjetSendRequestMessage _CustomID. */
        public _CustomID?: "CustomID";

        /**
         * Creates a new MailjetSendRequestMessage instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendRequestMessage instance
         */
        public static create(properties?: mailjet.IMailjetSendRequestMessage): mailjet.MailjetSendRequestMessage;

        /**
         * Encodes the specified MailjetSendRequestMessage message. Does not implicitly {@link mailjet.MailjetSendRequestMessage.verify|verify} messages.
         * @param message MailjetSendRequestMessage message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendRequestMessage, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendRequestMessage message, length delimited. Does not implicitly {@link mailjet.MailjetSendRequestMessage.verify|verify} messages.
         * @param message MailjetSendRequestMessage message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendRequestMessage, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendRequestMessage message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendRequestMessage
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendRequestMessage;

        /**
         * Decodes a MailjetSendRequestMessage message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendRequestMessage
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendRequestMessage;

        /**
         * Verifies a MailjetSendRequestMessage message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendRequestMessage message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendRequestMessage
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendRequestMessage;

        /**
         * Creates a plain object from a MailjetSendRequestMessage message. Also converts values to other types if specified.
         * @param message MailjetSendRequestMessage
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendRequestMessage, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendRequestMessage to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendRequestMessage
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendRequestFrom. */
    interface IMailjetSendRequestFrom {

        /** MailjetSendRequestFrom Email */
        Email?: (string|null);

        /** MailjetSendRequestFrom Name */
        Name?: (string|null);
    }

    /** Represents a MailjetSendRequestFrom. */
    class MailjetSendRequestFrom implements IMailjetSendRequestFrom {

        /**
         * Constructs a new MailjetSendRequestFrom.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendRequestFrom);

        /** MailjetSendRequestFrom Email. */
        public Email?: (string|null);

        /** MailjetSendRequestFrom Name. */
        public Name?: (string|null);

        /** MailjetSendRequestFrom _Name. */
        public _Name?: "Name";

        /**
         * Creates a new MailjetSendRequestFrom instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendRequestFrom instance
         */
        public static create(properties?: mailjet.IMailjetSendRequestFrom): mailjet.MailjetSendRequestFrom;

        /**
         * Encodes the specified MailjetSendRequestFrom message. Does not implicitly {@link mailjet.MailjetSendRequestFrom.verify|verify} messages.
         * @param message MailjetSendRequestFrom message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendRequestFrom, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendRequestFrom message, length delimited. Does not implicitly {@link mailjet.MailjetSendRequestFrom.verify|verify} messages.
         * @param message MailjetSendRequestFrom message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendRequestFrom, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendRequestFrom message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendRequestFrom
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendRequestFrom;

        /**
         * Decodes a MailjetSendRequestFrom message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendRequestFrom
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendRequestFrom;

        /**
         * Verifies a MailjetSendRequestFrom message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendRequestFrom message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendRequestFrom
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendRequestFrom;

        /**
         * Creates a plain object from a MailjetSendRequestFrom message. Also converts values to other types if specified.
         * @param message MailjetSendRequestFrom
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendRequestFrom, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendRequestFrom to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendRequestFrom
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendRequestTo. */
    interface IMailjetSendRequestTo {

        /** MailjetSendRequestTo Email */
        Email?: (string|null);

        /** MailjetSendRequestTo Name */
        Name?: (string|null);
    }

    /** Represents a MailjetSendRequestTo. */
    class MailjetSendRequestTo implements IMailjetSendRequestTo {

        /**
         * Constructs a new MailjetSendRequestTo.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendRequestTo);

        /** MailjetSendRequestTo Email. */
        public Email?: (string|null);

        /** MailjetSendRequestTo Name. */
        public Name?: (string|null);

        /** MailjetSendRequestTo _Name. */
        public _Name?: "Name";

        /**
         * Creates a new MailjetSendRequestTo instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendRequestTo instance
         */
        public static create(properties?: mailjet.IMailjetSendRequestTo): mailjet.MailjetSendRequestTo;

        /**
         * Encodes the specified MailjetSendRequestTo message. Does not implicitly {@link mailjet.MailjetSendRequestTo.verify|verify} messages.
         * @param message MailjetSendRequestTo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendRequestTo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendRequestTo message, length delimited. Does not implicitly {@link mailjet.MailjetSendRequestTo.verify|verify} messages.
         * @param message MailjetSendRequestTo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendRequestTo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendRequestTo message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendRequestTo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendRequestTo;

        /**
         * Decodes a MailjetSendRequestTo message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendRequestTo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendRequestTo;

        /**
         * Verifies a MailjetSendRequestTo message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendRequestTo message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendRequestTo
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendRequestTo;

        /**
         * Creates a plain object from a MailjetSendRequestTo message. Also converts values to other types if specified.
         * @param message MailjetSendRequestTo
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendRequestTo, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendRequestTo to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendRequestTo
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendRequestReplyTo. */
    interface IMailjetSendRequestReplyTo {

        /** MailjetSendRequestReplyTo Email */
        Email?: (string|null);

        /** MailjetSendRequestReplyTo Name */
        Name?: (string|null);
    }

    /** Represents a MailjetSendRequestReplyTo. */
    class MailjetSendRequestReplyTo implements IMailjetSendRequestReplyTo {

        /**
         * Constructs a new MailjetSendRequestReplyTo.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendRequestReplyTo);

        /** MailjetSendRequestReplyTo Email. */
        public Email?: (string|null);

        /** MailjetSendRequestReplyTo Name. */
        public Name?: (string|null);

        /** MailjetSendRequestReplyTo _Name. */
        public _Name?: "Name";

        /**
         * Creates a new MailjetSendRequestReplyTo instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendRequestReplyTo instance
         */
        public static create(properties?: mailjet.IMailjetSendRequestReplyTo): mailjet.MailjetSendRequestReplyTo;

        /**
         * Encodes the specified MailjetSendRequestReplyTo message. Does not implicitly {@link mailjet.MailjetSendRequestReplyTo.verify|verify} messages.
         * @param message MailjetSendRequestReplyTo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendRequestReplyTo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendRequestReplyTo message, length delimited. Does not implicitly {@link mailjet.MailjetSendRequestReplyTo.verify|verify} messages.
         * @param message MailjetSendRequestReplyTo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendRequestReplyTo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendRequestReplyTo message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendRequestReplyTo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendRequestReplyTo;

        /**
         * Decodes a MailjetSendRequestReplyTo message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendRequestReplyTo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendRequestReplyTo;

        /**
         * Verifies a MailjetSendRequestReplyTo message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendRequestReplyTo message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendRequestReplyTo
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendRequestReplyTo;

        /**
         * Creates a plain object from a MailjetSendRequestReplyTo message. Also converts values to other types if specified.
         * @param message MailjetSendRequestReplyTo
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendRequestReplyTo, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendRequestReplyTo to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendRequestReplyTo
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendResponseMessage. */
    interface IMailjetSendResponseMessage {

        /** MailjetSendResponseMessage Status */
        Status?: (mailjet.MailjetSendResponseStatus|null);

        /** MailjetSendResponseMessage Errors */
        Errors?: (mailjet.IMailjetSendResponseError[]|null);

        /** MailjetSendResponseMessage CustomID */
        CustomID?: (string|null);

        /** MailjetSendResponseMessage To */
        To?: (mailjet.IMailjetSendResponseTo[]|null);

        /** MailjetSendResponseMessage Cc */
        Cc?: (mailjet.IMailjetSendResponseCc[]|null);

        /** MailjetSendResponseMessage Bcc */
        Bcc?: (mailjet.IMailjetSendResponseBcc[]|null);
    }

    /** Represents a MailjetSendResponseMessage. */
    class MailjetSendResponseMessage implements IMailjetSendResponseMessage {

        /**
         * Constructs a new MailjetSendResponseMessage.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendResponseMessage);

        /** MailjetSendResponseMessage Status. */
        public Status?: (mailjet.MailjetSendResponseStatus|null);

        /** MailjetSendResponseMessage Errors. */
        public Errors: mailjet.IMailjetSendResponseError[];

        /** MailjetSendResponseMessage CustomID. */
        public CustomID?: (string|null);

        /** MailjetSendResponseMessage To. */
        public To: mailjet.IMailjetSendResponseTo[];

        /** MailjetSendResponseMessage Cc. */
        public Cc: mailjet.IMailjetSendResponseCc[];

        /** MailjetSendResponseMessage Bcc. */
        public Bcc: mailjet.IMailjetSendResponseBcc[];

        /** MailjetSendResponseMessage _Status. */
        public _Status?: "Status";

        /** MailjetSendResponseMessage _CustomID. */
        public _CustomID?: "CustomID";

        /**
         * Creates a new MailjetSendResponseMessage instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendResponseMessage instance
         */
        public static create(properties?: mailjet.IMailjetSendResponseMessage): mailjet.MailjetSendResponseMessage;

        /**
         * Encodes the specified MailjetSendResponseMessage message. Does not implicitly {@link mailjet.MailjetSendResponseMessage.verify|verify} messages.
         * @param message MailjetSendResponseMessage message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendResponseMessage, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendResponseMessage message, length delimited. Does not implicitly {@link mailjet.MailjetSendResponseMessage.verify|verify} messages.
         * @param message MailjetSendResponseMessage message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendResponseMessage, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendResponseMessage message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendResponseMessage
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendResponseMessage;

        /**
         * Decodes a MailjetSendResponseMessage message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendResponseMessage
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendResponseMessage;

        /**
         * Verifies a MailjetSendResponseMessage message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendResponseMessage message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendResponseMessage
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendResponseMessage;

        /**
         * Creates a plain object from a MailjetSendResponseMessage message. Also converts values to other types if specified.
         * @param message MailjetSendResponseMessage
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendResponseMessage, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendResponseMessage to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendResponseMessage
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendResponseError. */
    interface IMailjetSendResponseError {

        /** MailjetSendResponseError ErrorIdentifier */
        ErrorIdentifier?: (string|null);

        /** MailjetSendResponseError ErrorCode */
        ErrorCode?: (string|null);

        /** MailjetSendResponseError StatusCode */
        StatusCode?: (number|null);

        /** MailjetSendResponseError ErrorMessage */
        ErrorMessage?: (string|null);

        /** MailjetSendResponseError ErrorRelatedTo */
        ErrorRelatedTo?: (string|null);
    }

    /** Represents a MailjetSendResponseError. */
    class MailjetSendResponseError implements IMailjetSendResponseError {

        /**
         * Constructs a new MailjetSendResponseError.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendResponseError);

        /** MailjetSendResponseError ErrorIdentifier. */
        public ErrorIdentifier?: (string|null);

        /** MailjetSendResponseError ErrorCode. */
        public ErrorCode?: (string|null);

        /** MailjetSendResponseError StatusCode. */
        public StatusCode?: (number|null);

        /** MailjetSendResponseError ErrorMessage. */
        public ErrorMessage?: (string|null);

        /** MailjetSendResponseError ErrorRelatedTo. */
        public ErrorRelatedTo?: (string|null);

        /** MailjetSendResponseError _ErrorIdentifier. */
        public _ErrorIdentifier?: "ErrorIdentifier";

        /** MailjetSendResponseError _ErrorCode. */
        public _ErrorCode?: "ErrorCode";

        /** MailjetSendResponseError _StatusCode. */
        public _StatusCode?: "StatusCode";

        /** MailjetSendResponseError _ErrorMessage. */
        public _ErrorMessage?: "ErrorMessage";

        /** MailjetSendResponseError _ErrorRelatedTo. */
        public _ErrorRelatedTo?: "ErrorRelatedTo";

        /**
         * Creates a new MailjetSendResponseError instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendResponseError instance
         */
        public static create(properties?: mailjet.IMailjetSendResponseError): mailjet.MailjetSendResponseError;

        /**
         * Encodes the specified MailjetSendResponseError message. Does not implicitly {@link mailjet.MailjetSendResponseError.verify|verify} messages.
         * @param message MailjetSendResponseError message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendResponseError, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendResponseError message, length delimited. Does not implicitly {@link mailjet.MailjetSendResponseError.verify|verify} messages.
         * @param message MailjetSendResponseError message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendResponseError, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendResponseError message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendResponseError
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendResponseError;

        /**
         * Decodes a MailjetSendResponseError message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendResponseError
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendResponseError;

        /**
         * Verifies a MailjetSendResponseError message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendResponseError message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendResponseError
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendResponseError;

        /**
         * Creates a plain object from a MailjetSendResponseError message. Also converts values to other types if specified.
         * @param message MailjetSendResponseError
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendResponseError, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendResponseError to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendResponseError
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendResponseTo. */
    interface IMailjetSendResponseTo {

        /** MailjetSendResponseTo Email */
        Email?: (string|null);

        /** MailjetSendResponseTo MessageUUID */
        MessageUUID?: (string|null);

        /** MailjetSendResponseTo MessageID */
        MessageID?: (Long|null);

        /** MailjetSendResponseTo MessageHref */
        MessageHref?: (string|null);
    }

    /** Represents a MailjetSendResponseTo. */
    class MailjetSendResponseTo implements IMailjetSendResponseTo {

        /**
         * Constructs a new MailjetSendResponseTo.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendResponseTo);

        /** MailjetSendResponseTo Email. */
        public Email?: (string|null);

        /** MailjetSendResponseTo MessageUUID. */
        public MessageUUID?: (string|null);

        /** MailjetSendResponseTo MessageID. */
        public MessageID?: (Long|null);

        /** MailjetSendResponseTo MessageHref. */
        public MessageHref?: (string|null);

        /** MailjetSendResponseTo _Email. */
        public _Email?: "Email";

        /** MailjetSendResponseTo _MessageUUID. */
        public _MessageUUID?: "MessageUUID";

        /** MailjetSendResponseTo _MessageID. */
        public _MessageID?: "MessageID";

        /** MailjetSendResponseTo _MessageHref. */
        public _MessageHref?: "MessageHref";

        /**
         * Creates a new MailjetSendResponseTo instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendResponseTo instance
         */
        public static create(properties?: mailjet.IMailjetSendResponseTo): mailjet.MailjetSendResponseTo;

        /**
         * Encodes the specified MailjetSendResponseTo message. Does not implicitly {@link mailjet.MailjetSendResponseTo.verify|verify} messages.
         * @param message MailjetSendResponseTo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendResponseTo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendResponseTo message, length delimited. Does not implicitly {@link mailjet.MailjetSendResponseTo.verify|verify} messages.
         * @param message MailjetSendResponseTo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendResponseTo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendResponseTo message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendResponseTo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendResponseTo;

        /**
         * Decodes a MailjetSendResponseTo message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendResponseTo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendResponseTo;

        /**
         * Verifies a MailjetSendResponseTo message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendResponseTo message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendResponseTo
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendResponseTo;

        /**
         * Creates a plain object from a MailjetSendResponseTo message. Also converts values to other types if specified.
         * @param message MailjetSendResponseTo
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendResponseTo, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendResponseTo to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendResponseTo
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendResponseCc. */
    interface IMailjetSendResponseCc {

        /** MailjetSendResponseCc Email */
        Email?: (string|null);

        /** MailjetSendResponseCc MessageUUID */
        MessageUUID?: (string|null);

        /** MailjetSendResponseCc MessageID */
        MessageID?: (Long|null);

        /** MailjetSendResponseCc MessageHref */
        MessageHref?: (string|null);
    }

    /** Represents a MailjetSendResponseCc. */
    class MailjetSendResponseCc implements IMailjetSendResponseCc {

        /**
         * Constructs a new MailjetSendResponseCc.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendResponseCc);

        /** MailjetSendResponseCc Email. */
        public Email?: (string|null);

        /** MailjetSendResponseCc MessageUUID. */
        public MessageUUID?: (string|null);

        /** MailjetSendResponseCc MessageID. */
        public MessageID?: (Long|null);

        /** MailjetSendResponseCc MessageHref. */
        public MessageHref?: (string|null);

        /** MailjetSendResponseCc _Email. */
        public _Email?: "Email";

        /** MailjetSendResponseCc _MessageUUID. */
        public _MessageUUID?: "MessageUUID";

        /** MailjetSendResponseCc _MessageID. */
        public _MessageID?: "MessageID";

        /** MailjetSendResponseCc _MessageHref. */
        public _MessageHref?: "MessageHref";

        /**
         * Creates a new MailjetSendResponseCc instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendResponseCc instance
         */
        public static create(properties?: mailjet.IMailjetSendResponseCc): mailjet.MailjetSendResponseCc;

        /**
         * Encodes the specified MailjetSendResponseCc message. Does not implicitly {@link mailjet.MailjetSendResponseCc.verify|verify} messages.
         * @param message MailjetSendResponseCc message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendResponseCc, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendResponseCc message, length delimited. Does not implicitly {@link mailjet.MailjetSendResponseCc.verify|verify} messages.
         * @param message MailjetSendResponseCc message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendResponseCc, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendResponseCc message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendResponseCc
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendResponseCc;

        /**
         * Decodes a MailjetSendResponseCc message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendResponseCc
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendResponseCc;

        /**
         * Verifies a MailjetSendResponseCc message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendResponseCc message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendResponseCc
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendResponseCc;

        /**
         * Creates a plain object from a MailjetSendResponseCc message. Also converts values to other types if specified.
         * @param message MailjetSendResponseCc
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendResponseCc, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendResponseCc to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendResponseCc
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a MailjetSendResponseBcc. */
    interface IMailjetSendResponseBcc {

        /** MailjetSendResponseBcc Email */
        Email?: (string|null);

        /** MailjetSendResponseBcc MessageUUID */
        MessageUUID?: (string|null);

        /** MailjetSendResponseBcc MessageID */
        MessageID?: (Long|null);

        /** MailjetSendResponseBcc MessageHref */
        MessageHref?: (string|null);
    }

    /** Represents a MailjetSendResponseBcc. */
    class MailjetSendResponseBcc implements IMailjetSendResponseBcc {

        /**
         * Constructs a new MailjetSendResponseBcc.
         * @param [properties] Properties to set
         */
        constructor(properties?: mailjet.IMailjetSendResponseBcc);

        /** MailjetSendResponseBcc Email. */
        public Email?: (string|null);

        /** MailjetSendResponseBcc MessageUUID. */
        public MessageUUID?: (string|null);

        /** MailjetSendResponseBcc MessageID. */
        public MessageID?: (Long|null);

        /** MailjetSendResponseBcc MessageHref. */
        public MessageHref?: (string|null);

        /** MailjetSendResponseBcc _Email. */
        public _Email?: "Email";

        /** MailjetSendResponseBcc _MessageUUID. */
        public _MessageUUID?: "MessageUUID";

        /** MailjetSendResponseBcc _MessageID. */
        public _MessageID?: "MessageID";

        /** MailjetSendResponseBcc _MessageHref. */
        public _MessageHref?: "MessageHref";

        /**
         * Creates a new MailjetSendResponseBcc instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MailjetSendResponseBcc instance
         */
        public static create(properties?: mailjet.IMailjetSendResponseBcc): mailjet.MailjetSendResponseBcc;

        /**
         * Encodes the specified MailjetSendResponseBcc message. Does not implicitly {@link mailjet.MailjetSendResponseBcc.verify|verify} messages.
         * @param message MailjetSendResponseBcc message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: mailjet.IMailjetSendResponseBcc, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MailjetSendResponseBcc message, length delimited. Does not implicitly {@link mailjet.MailjetSendResponseBcc.verify|verify} messages.
         * @param message MailjetSendResponseBcc message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: mailjet.IMailjetSendResponseBcc, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MailjetSendResponseBcc message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MailjetSendResponseBcc
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): mailjet.MailjetSendResponseBcc;

        /**
         * Decodes a MailjetSendResponseBcc message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MailjetSendResponseBcc
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): mailjet.MailjetSendResponseBcc;

        /**
         * Verifies a MailjetSendResponseBcc message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MailjetSendResponseBcc message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MailjetSendResponseBcc
         */
        public static fromObject(object: { [k: string]: any }): mailjet.MailjetSendResponseBcc;

        /**
         * Creates a plain object from a MailjetSendResponseBcc message. Also converts values to other types if specified.
         * @param message MailjetSendResponseBcc
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: mailjet.MailjetSendResponseBcc, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MailjetSendResponseBcc to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for MailjetSendResponseBcc
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace post_service. */
export namespace post_service {

    /** Represents a PostService */
    class PostService extends $protobuf.rpc.Service {

        /**
         * Constructs a new PostService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new PostService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): PostService;

        /**
         * Calls GetProjectPosts.
         * @param request GetProjectPostsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetProjectPostsResponse
         */
        public getProjectPosts(request: post_service.IGetProjectPostsRequest, callback: post_service.PostService.GetProjectPostsCallback): void;

        /**
         * Calls GetProjectPosts.
         * @param request GetProjectPostsRequest message or plain object
         * @returns Promise
         */
        public getProjectPosts(request: post_service.IGetProjectPostsRequest): Promise<post_service.GetProjectPostsResponse>;

        /**
         * Calls UpsertProjectPost.
         * @param request UpsertProjectPostRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and UpsertProjectPostResponse
         */
        public upsertProjectPost(request: post_service.IUpsertProjectPostRequest, callback: post_service.PostService.UpsertProjectPostCallback): void;

        /**
         * Calls UpsertProjectPost.
         * @param request UpsertProjectPostRequest message or plain object
         * @returns Promise
         */
        public upsertProjectPost(request: post_service.IUpsertProjectPostRequest): Promise<post_service.UpsertProjectPostResponse>;

        /**
         * Calls UpsertProjectPostComment.
         * @param request UpsertProjectPostCommentRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and UpsertProjectPostCommentResponse
         */
        public upsertProjectPostComment(request: post_service.IUpsertProjectPostCommentRequest, callback: post_service.PostService.UpsertProjectPostCommentCallback): void;

        /**
         * Calls UpsertProjectPostComment.
         * @param request UpsertProjectPostCommentRequest message or plain object
         * @returns Promise
         */
        public upsertProjectPostComment(request: post_service.IUpsertProjectPostCommentRequest): Promise<post_service.UpsertProjectPostCommentResponse>;

        /**
         * Calls DeleteProjectPostComment.
         * @param request DeleteProjectPostCommentRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and DeleteProjectPostCommentResponse
         */
        public deleteProjectPostComment(request: post_service.IDeleteProjectPostCommentRequest, callback: post_service.PostService.DeleteProjectPostCommentCallback): void;

        /**
         * Calls DeleteProjectPostComment.
         * @param request DeleteProjectPostCommentRequest message or plain object
         * @returns Promise
         */
        public deleteProjectPostComment(request: post_service.IDeleteProjectPostCommentRequest): Promise<post_service.DeleteProjectPostCommentResponse>;

        /**
         * Calls UpsertProjectPostRating.
         * @param request UpsertProjectPostRatingRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and UpsertProjectPostRatingResponse
         */
        public upsertProjectPostRating(request: post_service.IUpsertProjectPostRatingRequest, callback: post_service.PostService.UpsertProjectPostRatingCallback): void;

        /**
         * Calls UpsertProjectPostRating.
         * @param request UpsertProjectPostRatingRequest message or plain object
         * @returns Promise
         */
        public upsertProjectPostRating(request: post_service.IUpsertProjectPostRatingRequest): Promise<post_service.UpsertProjectPostRatingResponse>;
    }

    namespace PostService {

        /**
         * Callback as used by {@link post_service.PostService#getProjectPosts}.
         * @param error Error, if any
         * @param [response] GetProjectPostsResponse
         */
        type GetProjectPostsCallback = (error: (Error|null), response?: post_service.GetProjectPostsResponse) => void;

        /**
         * Callback as used by {@link post_service.PostService#upsertProjectPost}.
         * @param error Error, if any
         * @param [response] UpsertProjectPostResponse
         */
        type UpsertProjectPostCallback = (error: (Error|null), response?: post_service.UpsertProjectPostResponse) => void;

        /**
         * Callback as used by {@link post_service.PostService#upsertProjectPostComment}.
         * @param error Error, if any
         * @param [response] UpsertProjectPostCommentResponse
         */
        type UpsertProjectPostCommentCallback = (error: (Error|null), response?: post_service.UpsertProjectPostCommentResponse) => void;

        /**
         * Callback as used by {@link post_service.PostService#deleteProjectPostComment}.
         * @param error Error, if any
         * @param [response] DeleteProjectPostCommentResponse
         */
        type DeleteProjectPostCommentCallback = (error: (Error|null), response?: post_service.DeleteProjectPostCommentResponse) => void;

        /**
         * Callback as used by {@link post_service.PostService#upsertProjectPostRating}.
         * @param error Error, if any
         * @param [response] UpsertProjectPostRatingResponse
         */
        type UpsertProjectPostRatingCallback = (error: (Error|null), response?: post_service.UpsertProjectPostRatingResponse) => void;
    }

    /** Properties of a GetProjectPostsRequest. */
    interface IGetProjectPostsRequest {

        /** GetProjectPostsRequest includeTags */
        includeTags?: (boolean|null);

        /** GetProjectPostsRequest includeComments */
        includeComments?: (boolean|null);

        /** GetProjectPostsRequest includeProjects */
        includeProjects?: (boolean|null);

        /** GetProjectPostsRequest includeRatings */
        includeRatings?: (boolean|null);

        /** GetProjectPostsRequest includeAssignments */
        includeAssignments?: (boolean|null);

        /** GetProjectPostsRequest projectIds */
        projectIds?: (number[]|null);

        /** GetProjectPostsRequest projectPostIds */
        projectPostIds?: (number[]|null);

        /** GetProjectPostsRequest assignmentIds */
        assignmentIds?: (number[]|null);

        /** GetProjectPostsRequest classXIds */
        classXIds?: (number[]|null);

        /** GetProjectPostsRequest schoolIds */
        schoolIds?: (number[]|null);

        /** GetProjectPostsRequest userXIds */
        userXIds?: (number[]|null);

        /** GetProjectPostsRequest beingEdited */
        beingEdited?: (boolean|null);

        /** GetProjectPostsRequest page */
        page?: (number|null);

        /** GetProjectPostsRequest pageSize */
        pageSize?: (number|null);
    }

    /** Represents a GetProjectPostsRequest. */
    class GetProjectPostsRequest implements IGetProjectPostsRequest {

        /**
         * Constructs a new GetProjectPostsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IGetProjectPostsRequest);

        /** GetProjectPostsRequest includeTags. */
        public includeTags?: (boolean|null);

        /** GetProjectPostsRequest includeComments. */
        public includeComments?: (boolean|null);

        /** GetProjectPostsRequest includeProjects. */
        public includeProjects?: (boolean|null);

        /** GetProjectPostsRequest includeRatings. */
        public includeRatings?: (boolean|null);

        /** GetProjectPostsRequest includeAssignments. */
        public includeAssignments?: (boolean|null);

        /** GetProjectPostsRequest projectIds. */
        public projectIds: number[];

        /** GetProjectPostsRequest projectPostIds. */
        public projectPostIds: number[];

        /** GetProjectPostsRequest assignmentIds. */
        public assignmentIds: number[];

        /** GetProjectPostsRequest classXIds. */
        public classXIds: number[];

        /** GetProjectPostsRequest schoolIds. */
        public schoolIds: number[];

        /** GetProjectPostsRequest userXIds. */
        public userXIds: number[];

        /** GetProjectPostsRequest beingEdited. */
        public beingEdited?: (boolean|null);

        /** GetProjectPostsRequest page. */
        public page?: (number|null);

        /** GetProjectPostsRequest pageSize. */
        public pageSize?: (number|null);

        /** GetProjectPostsRequest _includeTags. */
        public _includeTags?: "includeTags";

        /** GetProjectPostsRequest _includeComments. */
        public _includeComments?: "includeComments";

        /** GetProjectPostsRequest _includeProjects. */
        public _includeProjects?: "includeProjects";

        /** GetProjectPostsRequest _includeRatings. */
        public _includeRatings?: "includeRatings";

        /** GetProjectPostsRequest _includeAssignments. */
        public _includeAssignments?: "includeAssignments";

        /** GetProjectPostsRequest _beingEdited. */
        public _beingEdited?: "beingEdited";

        /** GetProjectPostsRequest _page. */
        public _page?: "page";

        /** GetProjectPostsRequest _pageSize. */
        public _pageSize?: "pageSize";

        /**
         * Creates a new GetProjectPostsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetProjectPostsRequest instance
         */
        public static create(properties?: post_service.IGetProjectPostsRequest): post_service.GetProjectPostsRequest;

        /**
         * Encodes the specified GetProjectPostsRequest message. Does not implicitly {@link post_service.GetProjectPostsRequest.verify|verify} messages.
         * @param message GetProjectPostsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IGetProjectPostsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetProjectPostsRequest message, length delimited. Does not implicitly {@link post_service.GetProjectPostsRequest.verify|verify} messages.
         * @param message GetProjectPostsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IGetProjectPostsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetProjectPostsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetProjectPostsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.GetProjectPostsRequest;

        /**
         * Decodes a GetProjectPostsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetProjectPostsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.GetProjectPostsRequest;

        /**
         * Verifies a GetProjectPostsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetProjectPostsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetProjectPostsRequest
         */
        public static fromObject(object: { [k: string]: any }): post_service.GetProjectPostsRequest;

        /**
         * Creates a plain object from a GetProjectPostsRequest message. Also converts values to other types if specified.
         * @param message GetProjectPostsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.GetProjectPostsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetProjectPostsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetProjectPostsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetProjectPostsResponse. */
    interface IGetProjectPostsResponse {

        /** GetProjectPostsResponse projectPosts */
        projectPosts?: (pl_types.IProjectPost[]|null);

        /** GetProjectPostsResponse page */
        page?: (number|null);

        /** GetProjectPostsResponse pageSize */
        pageSize?: (number|null);

        /** GetProjectPostsResponse totalProjectPosts */
        totalProjectPosts?: (Long|null);
    }

    /** Represents a GetProjectPostsResponse. */
    class GetProjectPostsResponse implements IGetProjectPostsResponse {

        /**
         * Constructs a new GetProjectPostsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IGetProjectPostsResponse);

        /** GetProjectPostsResponse projectPosts. */
        public projectPosts: pl_types.IProjectPost[];

        /** GetProjectPostsResponse page. */
        public page?: (number|null);

        /** GetProjectPostsResponse pageSize. */
        public pageSize?: (number|null);

        /** GetProjectPostsResponse totalProjectPosts. */
        public totalProjectPosts?: (Long|null);

        /** GetProjectPostsResponse _page. */
        public _page?: "page";

        /** GetProjectPostsResponse _pageSize. */
        public _pageSize?: "pageSize";

        /** GetProjectPostsResponse _totalProjectPosts. */
        public _totalProjectPosts?: "totalProjectPosts";

        /**
         * Creates a new GetProjectPostsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetProjectPostsResponse instance
         */
        public static create(properties?: post_service.IGetProjectPostsResponse): post_service.GetProjectPostsResponse;

        /**
         * Encodes the specified GetProjectPostsResponse message. Does not implicitly {@link post_service.GetProjectPostsResponse.verify|verify} messages.
         * @param message GetProjectPostsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IGetProjectPostsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetProjectPostsResponse message, length delimited. Does not implicitly {@link post_service.GetProjectPostsResponse.verify|verify} messages.
         * @param message GetProjectPostsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IGetProjectPostsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetProjectPostsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetProjectPostsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.GetProjectPostsResponse;

        /**
         * Decodes a GetProjectPostsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetProjectPostsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.GetProjectPostsResponse;

        /**
         * Verifies a GetProjectPostsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetProjectPostsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetProjectPostsResponse
         */
        public static fromObject(object: { [k: string]: any }): post_service.GetProjectPostsResponse;

        /**
         * Creates a plain object from a GetProjectPostsResponse message. Also converts values to other types if specified.
         * @param message GetProjectPostsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.GetProjectPostsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetProjectPostsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetProjectPostsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertProjectPostRequest. */
    interface IUpsertProjectPostRequest {

        /** UpsertProjectPostRequest projectPost */
        projectPost?: (pl_types.IProjectPost|null);
    }

    /** Represents an UpsertProjectPostRequest. */
    class UpsertProjectPostRequest implements IUpsertProjectPostRequest {

        /**
         * Constructs a new UpsertProjectPostRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IUpsertProjectPostRequest);

        /** UpsertProjectPostRequest projectPost. */
        public projectPost?: (pl_types.IProjectPost|null);

        /** UpsertProjectPostRequest _projectPost. */
        public _projectPost?: "projectPost";

        /**
         * Creates a new UpsertProjectPostRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertProjectPostRequest instance
         */
        public static create(properties?: post_service.IUpsertProjectPostRequest): post_service.UpsertProjectPostRequest;

        /**
         * Encodes the specified UpsertProjectPostRequest message. Does not implicitly {@link post_service.UpsertProjectPostRequest.verify|verify} messages.
         * @param message UpsertProjectPostRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IUpsertProjectPostRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertProjectPostRequest message, length delimited. Does not implicitly {@link post_service.UpsertProjectPostRequest.verify|verify} messages.
         * @param message UpsertProjectPostRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IUpsertProjectPostRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertProjectPostRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertProjectPostRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.UpsertProjectPostRequest;

        /**
         * Decodes an UpsertProjectPostRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertProjectPostRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.UpsertProjectPostRequest;

        /**
         * Verifies an UpsertProjectPostRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertProjectPostRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertProjectPostRequest
         */
        public static fromObject(object: { [k: string]: any }): post_service.UpsertProjectPostRequest;

        /**
         * Creates a plain object from an UpsertProjectPostRequest message. Also converts values to other types if specified.
         * @param message UpsertProjectPostRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.UpsertProjectPostRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertProjectPostRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertProjectPostRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertProjectPostResponse. */
    interface IUpsertProjectPostResponse {

        /** UpsertProjectPostResponse projectPostId */
        projectPostId?: (number|null);
    }

    /** Represents an UpsertProjectPostResponse. */
    class UpsertProjectPostResponse implements IUpsertProjectPostResponse {

        /**
         * Constructs a new UpsertProjectPostResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IUpsertProjectPostResponse);

        /** UpsertProjectPostResponse projectPostId. */
        public projectPostId?: (number|null);

        /** UpsertProjectPostResponse _projectPostId. */
        public _projectPostId?: "projectPostId";

        /**
         * Creates a new UpsertProjectPostResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertProjectPostResponse instance
         */
        public static create(properties?: post_service.IUpsertProjectPostResponse): post_service.UpsertProjectPostResponse;

        /**
         * Encodes the specified UpsertProjectPostResponse message. Does not implicitly {@link post_service.UpsertProjectPostResponse.verify|verify} messages.
         * @param message UpsertProjectPostResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IUpsertProjectPostResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertProjectPostResponse message, length delimited. Does not implicitly {@link post_service.UpsertProjectPostResponse.verify|verify} messages.
         * @param message UpsertProjectPostResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IUpsertProjectPostResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertProjectPostResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertProjectPostResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.UpsertProjectPostResponse;

        /**
         * Decodes an UpsertProjectPostResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertProjectPostResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.UpsertProjectPostResponse;

        /**
         * Verifies an UpsertProjectPostResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertProjectPostResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertProjectPostResponse
         */
        public static fromObject(object: { [k: string]: any }): post_service.UpsertProjectPostResponse;

        /**
         * Creates a plain object from an UpsertProjectPostResponse message. Also converts values to other types if specified.
         * @param message UpsertProjectPostResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.UpsertProjectPostResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertProjectPostResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertProjectPostResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertProjectPostCommentRequest. */
    interface IUpsertProjectPostCommentRequest {

        /** UpsertProjectPostCommentRequest projectPostComment */
        projectPostComment?: (pl_types.IProjectPostComment|null);
    }

    /** Represents an UpsertProjectPostCommentRequest. */
    class UpsertProjectPostCommentRequest implements IUpsertProjectPostCommentRequest {

        /**
         * Constructs a new UpsertProjectPostCommentRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IUpsertProjectPostCommentRequest);

        /** UpsertProjectPostCommentRequest projectPostComment. */
        public projectPostComment?: (pl_types.IProjectPostComment|null);

        /** UpsertProjectPostCommentRequest _projectPostComment. */
        public _projectPostComment?: "projectPostComment";

        /**
         * Creates a new UpsertProjectPostCommentRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertProjectPostCommentRequest instance
         */
        public static create(properties?: post_service.IUpsertProjectPostCommentRequest): post_service.UpsertProjectPostCommentRequest;

        /**
         * Encodes the specified UpsertProjectPostCommentRequest message. Does not implicitly {@link post_service.UpsertProjectPostCommentRequest.verify|verify} messages.
         * @param message UpsertProjectPostCommentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IUpsertProjectPostCommentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertProjectPostCommentRequest message, length delimited. Does not implicitly {@link post_service.UpsertProjectPostCommentRequest.verify|verify} messages.
         * @param message UpsertProjectPostCommentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IUpsertProjectPostCommentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertProjectPostCommentRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertProjectPostCommentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.UpsertProjectPostCommentRequest;

        /**
         * Decodes an UpsertProjectPostCommentRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertProjectPostCommentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.UpsertProjectPostCommentRequest;

        /**
         * Verifies an UpsertProjectPostCommentRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertProjectPostCommentRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertProjectPostCommentRequest
         */
        public static fromObject(object: { [k: string]: any }): post_service.UpsertProjectPostCommentRequest;

        /**
         * Creates a plain object from an UpsertProjectPostCommentRequest message. Also converts values to other types if specified.
         * @param message UpsertProjectPostCommentRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.UpsertProjectPostCommentRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertProjectPostCommentRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertProjectPostCommentRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertProjectPostCommentResponse. */
    interface IUpsertProjectPostCommentResponse {

        /** UpsertProjectPostCommentResponse projectPostComment */
        projectPostComment?: (pl_types.IProjectPostComment|null);
    }

    /** Represents an UpsertProjectPostCommentResponse. */
    class UpsertProjectPostCommentResponse implements IUpsertProjectPostCommentResponse {

        /**
         * Constructs a new UpsertProjectPostCommentResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IUpsertProjectPostCommentResponse);

        /** UpsertProjectPostCommentResponse projectPostComment. */
        public projectPostComment?: (pl_types.IProjectPostComment|null);

        /** UpsertProjectPostCommentResponse _projectPostComment. */
        public _projectPostComment?: "projectPostComment";

        /**
         * Creates a new UpsertProjectPostCommentResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertProjectPostCommentResponse instance
         */
        public static create(properties?: post_service.IUpsertProjectPostCommentResponse): post_service.UpsertProjectPostCommentResponse;

        /**
         * Encodes the specified UpsertProjectPostCommentResponse message. Does not implicitly {@link post_service.UpsertProjectPostCommentResponse.verify|verify} messages.
         * @param message UpsertProjectPostCommentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IUpsertProjectPostCommentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertProjectPostCommentResponse message, length delimited. Does not implicitly {@link post_service.UpsertProjectPostCommentResponse.verify|verify} messages.
         * @param message UpsertProjectPostCommentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IUpsertProjectPostCommentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertProjectPostCommentResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertProjectPostCommentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.UpsertProjectPostCommentResponse;

        /**
         * Decodes an UpsertProjectPostCommentResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertProjectPostCommentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.UpsertProjectPostCommentResponse;

        /**
         * Verifies an UpsertProjectPostCommentResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertProjectPostCommentResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertProjectPostCommentResponse
         */
        public static fromObject(object: { [k: string]: any }): post_service.UpsertProjectPostCommentResponse;

        /**
         * Creates a plain object from an UpsertProjectPostCommentResponse message. Also converts values to other types if specified.
         * @param message UpsertProjectPostCommentResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.UpsertProjectPostCommentResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertProjectPostCommentResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertProjectPostCommentResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a DeleteProjectPostCommentRequest. */
    interface IDeleteProjectPostCommentRequest {

        /** DeleteProjectPostCommentRequest projectPostCommentId */
        projectPostCommentId?: (number|null);
    }

    /** Represents a DeleteProjectPostCommentRequest. */
    class DeleteProjectPostCommentRequest implements IDeleteProjectPostCommentRequest {

        /**
         * Constructs a new DeleteProjectPostCommentRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IDeleteProjectPostCommentRequest);

        /** DeleteProjectPostCommentRequest projectPostCommentId. */
        public projectPostCommentId?: (number|null);

        /** DeleteProjectPostCommentRequest _projectPostCommentId. */
        public _projectPostCommentId?: "projectPostCommentId";

        /**
         * Creates a new DeleteProjectPostCommentRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns DeleteProjectPostCommentRequest instance
         */
        public static create(properties?: post_service.IDeleteProjectPostCommentRequest): post_service.DeleteProjectPostCommentRequest;

        /**
         * Encodes the specified DeleteProjectPostCommentRequest message. Does not implicitly {@link post_service.DeleteProjectPostCommentRequest.verify|verify} messages.
         * @param message DeleteProjectPostCommentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IDeleteProjectPostCommentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified DeleteProjectPostCommentRequest message, length delimited. Does not implicitly {@link post_service.DeleteProjectPostCommentRequest.verify|verify} messages.
         * @param message DeleteProjectPostCommentRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IDeleteProjectPostCommentRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a DeleteProjectPostCommentRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns DeleteProjectPostCommentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.DeleteProjectPostCommentRequest;

        /**
         * Decodes a DeleteProjectPostCommentRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns DeleteProjectPostCommentRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.DeleteProjectPostCommentRequest;

        /**
         * Verifies a DeleteProjectPostCommentRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a DeleteProjectPostCommentRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns DeleteProjectPostCommentRequest
         */
        public static fromObject(object: { [k: string]: any }): post_service.DeleteProjectPostCommentRequest;

        /**
         * Creates a plain object from a DeleteProjectPostCommentRequest message. Also converts values to other types if specified.
         * @param message DeleteProjectPostCommentRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.DeleteProjectPostCommentRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this DeleteProjectPostCommentRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for DeleteProjectPostCommentRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a DeleteProjectPostCommentResponse. */
    interface IDeleteProjectPostCommentResponse {
    }

    /** Represents a DeleteProjectPostCommentResponse. */
    class DeleteProjectPostCommentResponse implements IDeleteProjectPostCommentResponse {

        /**
         * Constructs a new DeleteProjectPostCommentResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IDeleteProjectPostCommentResponse);

        /**
         * Creates a new DeleteProjectPostCommentResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns DeleteProjectPostCommentResponse instance
         */
        public static create(properties?: post_service.IDeleteProjectPostCommentResponse): post_service.DeleteProjectPostCommentResponse;

        /**
         * Encodes the specified DeleteProjectPostCommentResponse message. Does not implicitly {@link post_service.DeleteProjectPostCommentResponse.verify|verify} messages.
         * @param message DeleteProjectPostCommentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IDeleteProjectPostCommentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified DeleteProjectPostCommentResponse message, length delimited. Does not implicitly {@link post_service.DeleteProjectPostCommentResponse.verify|verify} messages.
         * @param message DeleteProjectPostCommentResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IDeleteProjectPostCommentResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a DeleteProjectPostCommentResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns DeleteProjectPostCommentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.DeleteProjectPostCommentResponse;

        /**
         * Decodes a DeleteProjectPostCommentResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns DeleteProjectPostCommentResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.DeleteProjectPostCommentResponse;

        /**
         * Verifies a DeleteProjectPostCommentResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a DeleteProjectPostCommentResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns DeleteProjectPostCommentResponse
         */
        public static fromObject(object: { [k: string]: any }): post_service.DeleteProjectPostCommentResponse;

        /**
         * Creates a plain object from a DeleteProjectPostCommentResponse message. Also converts values to other types if specified.
         * @param message DeleteProjectPostCommentResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.DeleteProjectPostCommentResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this DeleteProjectPostCommentResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for DeleteProjectPostCommentResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertProjectPostRatingRequest. */
    interface IUpsertProjectPostRatingRequest {

        /** UpsertProjectPostRatingRequest projectPostRating */
        projectPostRating?: (pl_types.IProjectPostRating|null);
    }

    /** Represents an UpsertProjectPostRatingRequest. */
    class UpsertProjectPostRatingRequest implements IUpsertProjectPostRatingRequest {

        /**
         * Constructs a new UpsertProjectPostRatingRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IUpsertProjectPostRatingRequest);

        /** UpsertProjectPostRatingRequest projectPostRating. */
        public projectPostRating?: (pl_types.IProjectPostRating|null);

        /** UpsertProjectPostRatingRequest _projectPostRating. */
        public _projectPostRating?: "projectPostRating";

        /**
         * Creates a new UpsertProjectPostRatingRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertProjectPostRatingRequest instance
         */
        public static create(properties?: post_service.IUpsertProjectPostRatingRequest): post_service.UpsertProjectPostRatingRequest;

        /**
         * Encodes the specified UpsertProjectPostRatingRequest message. Does not implicitly {@link post_service.UpsertProjectPostRatingRequest.verify|verify} messages.
         * @param message UpsertProjectPostRatingRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IUpsertProjectPostRatingRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertProjectPostRatingRequest message, length delimited. Does not implicitly {@link post_service.UpsertProjectPostRatingRequest.verify|verify} messages.
         * @param message UpsertProjectPostRatingRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IUpsertProjectPostRatingRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertProjectPostRatingRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertProjectPostRatingRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.UpsertProjectPostRatingRequest;

        /**
         * Decodes an UpsertProjectPostRatingRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertProjectPostRatingRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.UpsertProjectPostRatingRequest;

        /**
         * Verifies an UpsertProjectPostRatingRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertProjectPostRatingRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertProjectPostRatingRequest
         */
        public static fromObject(object: { [k: string]: any }): post_service.UpsertProjectPostRatingRequest;

        /**
         * Creates a plain object from an UpsertProjectPostRatingRequest message. Also converts values to other types if specified.
         * @param message UpsertProjectPostRatingRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.UpsertProjectPostRatingRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertProjectPostRatingRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertProjectPostRatingRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertProjectPostRatingResponse. */
    interface IUpsertProjectPostRatingResponse {

        /** UpsertProjectPostRatingResponse id */
        id?: (number|null);
    }

    /** Represents an UpsertProjectPostRatingResponse. */
    class UpsertProjectPostRatingResponse implements IUpsertProjectPostRatingResponse {

        /**
         * Constructs a new UpsertProjectPostRatingResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: post_service.IUpsertProjectPostRatingResponse);

        /** UpsertProjectPostRatingResponse id. */
        public id?: (number|null);

        /** UpsertProjectPostRatingResponse _id. */
        public _id?: "id";

        /**
         * Creates a new UpsertProjectPostRatingResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertProjectPostRatingResponse instance
         */
        public static create(properties?: post_service.IUpsertProjectPostRatingResponse): post_service.UpsertProjectPostRatingResponse;

        /**
         * Encodes the specified UpsertProjectPostRatingResponse message. Does not implicitly {@link post_service.UpsertProjectPostRatingResponse.verify|verify} messages.
         * @param message UpsertProjectPostRatingResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: post_service.IUpsertProjectPostRatingResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertProjectPostRatingResponse message, length delimited. Does not implicitly {@link post_service.UpsertProjectPostRatingResponse.verify|verify} messages.
         * @param message UpsertProjectPostRatingResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: post_service.IUpsertProjectPostRatingResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertProjectPostRatingResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertProjectPostRatingResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): post_service.UpsertProjectPostRatingResponse;

        /**
         * Decodes an UpsertProjectPostRatingResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertProjectPostRatingResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): post_service.UpsertProjectPostRatingResponse;

        /**
         * Verifies an UpsertProjectPostRatingResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertProjectPostRatingResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertProjectPostRatingResponse
         */
        public static fromObject(object: { [k: string]: any }): post_service.UpsertProjectPostRatingResponse;

        /**
         * Creates a plain object from an UpsertProjectPostRatingResponse message. Also converts values to other types if specified.
         * @param message UpsertProjectPostRatingResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: post_service.UpsertProjectPostRatingResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertProjectPostRatingResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertProjectPostRatingResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace project_management. */
export namespace project_management {

    /** Represents a ProjectManagementService */
    class ProjectManagementService extends $protobuf.rpc.Service {

        /**
         * Constructs a new ProjectManagementService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new ProjectManagementService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): ProjectManagementService;

        /**
         * Calls GetMotivations.
         * @param request GetMotivationsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetMotivationsResponse
         */
        public getMotivations(request: project_management.IGetMotivationsRequest, callback: project_management.ProjectManagementService.GetMotivationsCallback): void;

        /**
         * Calls GetMotivations.
         * @param request GetMotivationsRequest message or plain object
         * @returns Promise
         */
        public getMotivations(request: project_management.IGetMotivationsRequest): Promise<project_management.GetMotivationsResponse>;

        /**
         * Calls GetKnowledgeAndSkills.
         * @param request GetKnowledgeAndSkillsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetKnowledgeAndSkillsResponse
         */
        public getKnowledgeAndSkills(request: project_management.IGetKnowledgeAndSkillsRequest, callback: project_management.ProjectManagementService.GetKnowledgeAndSkillsCallback): void;

        /**
         * Calls GetKnowledgeAndSkills.
         * @param request GetKnowledgeAndSkillsRequest message or plain object
         * @returns Promise
         */
        public getKnowledgeAndSkills(request: project_management.IGetKnowledgeAndSkillsRequest): Promise<project_management.GetKnowledgeAndSkillsResponse>;

        /**
         * Calls UpsertKnowledgeAndSkill.
         * @param request UpsertKnowledgeAndSkillRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and UpsertKnowledgeAndSkillResponse
         */
        public upsertKnowledgeAndSkill(request: project_management.IUpsertKnowledgeAndSkillRequest, callback: project_management.ProjectManagementService.UpsertKnowledgeAndSkillCallback): void;

        /**
         * Calls UpsertKnowledgeAndSkill.
         * @param request UpsertKnowledgeAndSkillRequest message or plain object
         * @returns Promise
         */
        public upsertKnowledgeAndSkill(request: project_management.IUpsertKnowledgeAndSkillRequest): Promise<project_management.UpsertKnowledgeAndSkillResponse>;

        /**
         * Calls GenerateProjects.
         * @param request GenerateProjectsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GenerateProjectsResponse
         */
        public generateProjects(request: project_management.IGenerateProjectsRequest, callback: project_management.ProjectManagementService.GenerateProjectsCallback): void;

        /**
         * Calls GenerateProjects.
         * @param request GenerateProjectsRequest message or plain object
         * @returns Promise
         */
        public generateProjects(request: project_management.IGenerateProjectsRequest): Promise<project_management.GenerateProjectsResponse>;

        /**
         * Calls RegisterAnonymousProjects.
         * @param request RegisterAnonymousProjectsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and RegisterAnonymousProjectsResponse
         */
        public registerAnonymousProjects(request: project_management.IRegisterAnonymousProjectsRequest, callback: project_management.ProjectManagementService.RegisterAnonymousProjectsCallback): void;

        /**
         * Calls RegisterAnonymousProjects.
         * @param request RegisterAnonymousProjectsRequest message or plain object
         * @returns Promise
         */
        public registerAnonymousProjects(request: project_management.IRegisterAnonymousProjectsRequest): Promise<project_management.RegisterAnonymousProjectsResponse>;

        /**
         * Calls GetProjectInputs.
         * @param request GetProjectInputsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetProjectInputsResponse
         */
        public getProjectInputs(request: project_management.IGetProjectInputsRequest, callback: project_management.ProjectManagementService.GetProjectInputsCallback): void;

        /**
         * Calls GetProjectInputs.
         * @param request GetProjectInputsRequest message or plain object
         * @returns Promise
         */
        public getProjectInputs(request: project_management.IGetProjectInputsRequest): Promise<project_management.GetProjectInputsResponse>;

        /**
         * Calls GetProjects.
         * @param request GetProjectsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetProjectsResponse
         */
        public getProjects(request: project_management.IGetProjectsRequest, callback: project_management.ProjectManagementService.GetProjectsCallback): void;

        /**
         * Calls GetProjects.
         * @param request GetProjectsRequest message or plain object
         * @returns Promise
         */
        public getProjects(request: project_management.IGetProjectsRequest): Promise<project_management.GetProjectsResponse>;

        /**
         * Calls GetProjectDefinitionCategoryTypes.
         * @param request GetProjectDefinitionCategoryTypesRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetProjectDefinitionCategoryTypesResponse
         */
        public getProjectDefinitionCategoryTypes(request: project_management.IGetProjectDefinitionCategoryTypesRequest, callback: project_management.ProjectManagementService.GetProjectDefinitionCategoryTypesCallback): void;

        /**
         * Calls GetProjectDefinitionCategoryTypes.
         * @param request GetProjectDefinitionCategoryTypesRequest message or plain object
         * @returns Promise
         */
        public getProjectDefinitionCategoryTypes(request: project_management.IGetProjectDefinitionCategoryTypesRequest): Promise<project_management.GetProjectDefinitionCategoryTypesResponse>;

        /**
         * Calls UpdateProject.
         * @param request UpdateProjectRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and UpdateProjectResponse
         */
        public updateProject(request: project_management.IUpdateProjectRequest, callback: project_management.ProjectManagementService.UpdateProjectCallback): void;

        /**
         * Calls UpdateProject.
         * @param request UpdateProjectRequest message or plain object
         * @returns Promise
         */
        public updateProject(request: project_management.IUpdateProjectRequest): Promise<project_management.UpdateProjectResponse>;
    }

    namespace ProjectManagementService {

        /**
         * Callback as used by {@link project_management.ProjectManagementService#getMotivations}.
         * @param error Error, if any
         * @param [response] GetMotivationsResponse
         */
        type GetMotivationsCallback = (error: (Error|null), response?: project_management.GetMotivationsResponse) => void;

        /**
         * Callback as used by {@link project_management.ProjectManagementService#getKnowledgeAndSkills}.
         * @param error Error, if any
         * @param [response] GetKnowledgeAndSkillsResponse
         */
        type GetKnowledgeAndSkillsCallback = (error: (Error|null), response?: project_management.GetKnowledgeAndSkillsResponse) => void;

        /**
         * Callback as used by {@link project_management.ProjectManagementService#upsertKnowledgeAndSkill}.
         * @param error Error, if any
         * @param [response] UpsertKnowledgeAndSkillResponse
         */
        type UpsertKnowledgeAndSkillCallback = (error: (Error|null), response?: project_management.UpsertKnowledgeAndSkillResponse) => void;

        /**
         * Callback as used by {@link project_management.ProjectManagementService#generateProjects}.
         * @param error Error, if any
         * @param [response] GenerateProjectsResponse
         */
        type GenerateProjectsCallback = (error: (Error|null), response?: project_management.GenerateProjectsResponse) => void;

        /**
         * Callback as used by {@link project_management.ProjectManagementService#registerAnonymousProjects}.
         * @param error Error, if any
         * @param [response] RegisterAnonymousProjectsResponse
         */
        type RegisterAnonymousProjectsCallback = (error: (Error|null), response?: project_management.RegisterAnonymousProjectsResponse) => void;

        /**
         * Callback as used by {@link project_management.ProjectManagementService#getProjectInputs}.
         * @param error Error, if any
         * @param [response] GetProjectInputsResponse
         */
        type GetProjectInputsCallback = (error: (Error|null), response?: project_management.GetProjectInputsResponse) => void;

        /**
         * Callback as used by {@link project_management.ProjectManagementService#getProjects}.
         * @param error Error, if any
         * @param [response] GetProjectsResponse
         */
        type GetProjectsCallback = (error: (Error|null), response?: project_management.GetProjectsResponse) => void;

        /**
         * Callback as used by {@link project_management.ProjectManagementService#getProjectDefinitionCategoryTypes}.
         * @param error Error, if any
         * @param [response] GetProjectDefinitionCategoryTypesResponse
         */
        type GetProjectDefinitionCategoryTypesCallback = (error: (Error|null), response?: project_management.GetProjectDefinitionCategoryTypesResponse) => void;

        /**
         * Callback as used by {@link project_management.ProjectManagementService#updateProject}.
         * @param error Error, if any
         * @param [response] UpdateProjectResponse
         */
        type UpdateProjectCallback = (error: (Error|null), response?: project_management.UpdateProjectResponse) => void;
    }

    /** Properties of a GetMotivationsRequest. */
    interface IGetMotivationsRequest {
    }

    /** Represents a GetMotivationsRequest. */
    class GetMotivationsRequest implements IGetMotivationsRequest {

        /**
         * Constructs a new GetMotivationsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetMotivationsRequest);

        /**
         * Creates a new GetMotivationsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetMotivationsRequest instance
         */
        public static create(properties?: project_management.IGetMotivationsRequest): project_management.GetMotivationsRequest;

        /**
         * Encodes the specified GetMotivationsRequest message. Does not implicitly {@link project_management.GetMotivationsRequest.verify|verify} messages.
         * @param message GetMotivationsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetMotivationsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetMotivationsRequest message, length delimited. Does not implicitly {@link project_management.GetMotivationsRequest.verify|verify} messages.
         * @param message GetMotivationsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetMotivationsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetMotivationsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetMotivationsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetMotivationsRequest;

        /**
         * Decodes a GetMotivationsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetMotivationsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetMotivationsRequest;

        /**
         * Verifies a GetMotivationsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetMotivationsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetMotivationsRequest
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetMotivationsRequest;

        /**
         * Creates a plain object from a GetMotivationsRequest message. Also converts values to other types if specified.
         * @param message GetMotivationsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetMotivationsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetMotivationsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetMotivationsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetMotivationsResponse. */
    interface IGetMotivationsResponse {
    }

    /** Represents a GetMotivationsResponse. */
    class GetMotivationsResponse implements IGetMotivationsResponse {

        /**
         * Constructs a new GetMotivationsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetMotivationsResponse);

        /**
         * Creates a new GetMotivationsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetMotivationsResponse instance
         */
        public static create(properties?: project_management.IGetMotivationsResponse): project_management.GetMotivationsResponse;

        /**
         * Encodes the specified GetMotivationsResponse message. Does not implicitly {@link project_management.GetMotivationsResponse.verify|verify} messages.
         * @param message GetMotivationsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetMotivationsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetMotivationsResponse message, length delimited. Does not implicitly {@link project_management.GetMotivationsResponse.verify|verify} messages.
         * @param message GetMotivationsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetMotivationsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetMotivationsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetMotivationsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetMotivationsResponse;

        /**
         * Decodes a GetMotivationsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetMotivationsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetMotivationsResponse;

        /**
         * Verifies a GetMotivationsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetMotivationsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetMotivationsResponse
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetMotivationsResponse;

        /**
         * Creates a plain object from a GetMotivationsResponse message. Also converts values to other types if specified.
         * @param message GetMotivationsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetMotivationsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetMotivationsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetMotivationsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GenerateProjectsRequest. */
    interface IGenerateProjectsRequest {

        /** GenerateProjectsRequest definition */
        definition?: (pl_types.IProjectDefinition|null);
    }

    /** Represents a GenerateProjectsRequest. */
    class GenerateProjectsRequest implements IGenerateProjectsRequest {

        /**
         * Constructs a new GenerateProjectsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGenerateProjectsRequest);

        /** GenerateProjectsRequest definition. */
        public definition?: (pl_types.IProjectDefinition|null);

        /** GenerateProjectsRequest _definition. */
        public _definition?: "definition";

        /**
         * Creates a new GenerateProjectsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GenerateProjectsRequest instance
         */
        public static create(properties?: project_management.IGenerateProjectsRequest): project_management.GenerateProjectsRequest;

        /**
         * Encodes the specified GenerateProjectsRequest message. Does not implicitly {@link project_management.GenerateProjectsRequest.verify|verify} messages.
         * @param message GenerateProjectsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGenerateProjectsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GenerateProjectsRequest message, length delimited. Does not implicitly {@link project_management.GenerateProjectsRequest.verify|verify} messages.
         * @param message GenerateProjectsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGenerateProjectsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GenerateProjectsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GenerateProjectsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GenerateProjectsRequest;

        /**
         * Decodes a GenerateProjectsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GenerateProjectsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GenerateProjectsRequest;

        /**
         * Verifies a GenerateProjectsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GenerateProjectsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GenerateProjectsRequest
         */
        public static fromObject(object: { [k: string]: any }): project_management.GenerateProjectsRequest;

        /**
         * Creates a plain object from a GenerateProjectsRequest message. Also converts values to other types if specified.
         * @param message GenerateProjectsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GenerateProjectsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GenerateProjectsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GenerateProjectsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GenerateProjectsResponse. */
    interface IGenerateProjectsResponse {

        /** GenerateProjectsResponse projectInputId */
        projectInputId?: (number|null);
    }

    /** Represents a GenerateProjectsResponse. */
    class GenerateProjectsResponse implements IGenerateProjectsResponse {

        /**
         * Constructs a new GenerateProjectsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGenerateProjectsResponse);

        /** GenerateProjectsResponse projectInputId. */
        public projectInputId?: (number|null);

        /** GenerateProjectsResponse _projectInputId. */
        public _projectInputId?: "projectInputId";

        /**
         * Creates a new GenerateProjectsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GenerateProjectsResponse instance
         */
        public static create(properties?: project_management.IGenerateProjectsResponse): project_management.GenerateProjectsResponse;

        /**
         * Encodes the specified GenerateProjectsResponse message. Does not implicitly {@link project_management.GenerateProjectsResponse.verify|verify} messages.
         * @param message GenerateProjectsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGenerateProjectsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GenerateProjectsResponse message, length delimited. Does not implicitly {@link project_management.GenerateProjectsResponse.verify|verify} messages.
         * @param message GenerateProjectsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGenerateProjectsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GenerateProjectsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GenerateProjectsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GenerateProjectsResponse;

        /**
         * Decodes a GenerateProjectsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GenerateProjectsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GenerateProjectsResponse;

        /**
         * Verifies a GenerateProjectsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GenerateProjectsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GenerateProjectsResponse
         */
        public static fromObject(object: { [k: string]: any }): project_management.GenerateProjectsResponse;

        /**
         * Creates a plain object from a GenerateProjectsResponse message. Also converts values to other types if specified.
         * @param message GenerateProjectsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GenerateProjectsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GenerateProjectsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GenerateProjectsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetProjectInputsRequest. */
    interface IGetProjectInputsRequest {

        /** GetProjectInputsRequest userXIds */
        userXIds?: (number[]|null);

        /** GetProjectInputsRequest projectInputIds */
        projectInputIds?: (number[]|null);

        /** GetProjectInputsRequest includeComplete */
        includeComplete?: (boolean|null);

        /** GetProjectInputsRequest includeProcessing */
        includeProcessing?: (boolean|null);

        /** GetProjectInputsRequest includeAssignment */
        includeAssignment?: (boolean|null);
    }

    /** Represents a GetProjectInputsRequest. */
    class GetProjectInputsRequest implements IGetProjectInputsRequest {

        /**
         * Constructs a new GetProjectInputsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetProjectInputsRequest);

        /** GetProjectInputsRequest userXIds. */
        public userXIds: number[];

        /** GetProjectInputsRequest projectInputIds. */
        public projectInputIds: number[];

        /** GetProjectInputsRequest includeComplete. */
        public includeComplete?: (boolean|null);

        /** GetProjectInputsRequest includeProcessing. */
        public includeProcessing?: (boolean|null);

        /** GetProjectInputsRequest includeAssignment. */
        public includeAssignment?: (boolean|null);

        /** GetProjectInputsRequest _includeComplete. */
        public _includeComplete?: "includeComplete";

        /** GetProjectInputsRequest _includeProcessing. */
        public _includeProcessing?: "includeProcessing";

        /** GetProjectInputsRequest _includeAssignment. */
        public _includeAssignment?: "includeAssignment";

        /**
         * Creates a new GetProjectInputsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetProjectInputsRequest instance
         */
        public static create(properties?: project_management.IGetProjectInputsRequest): project_management.GetProjectInputsRequest;

        /**
         * Encodes the specified GetProjectInputsRequest message. Does not implicitly {@link project_management.GetProjectInputsRequest.verify|verify} messages.
         * @param message GetProjectInputsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetProjectInputsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetProjectInputsRequest message, length delimited. Does not implicitly {@link project_management.GetProjectInputsRequest.verify|verify} messages.
         * @param message GetProjectInputsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetProjectInputsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetProjectInputsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetProjectInputsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetProjectInputsRequest;

        /**
         * Decodes a GetProjectInputsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetProjectInputsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetProjectInputsRequest;

        /**
         * Verifies a GetProjectInputsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetProjectInputsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetProjectInputsRequest
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetProjectInputsRequest;

        /**
         * Creates a plain object from a GetProjectInputsRequest message. Also converts values to other types if specified.
         * @param message GetProjectInputsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetProjectInputsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetProjectInputsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetProjectInputsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetProjectInputsResponse. */
    interface IGetProjectInputsResponse {

        /** GetProjectInputsResponse projects */
        projects?: (pl_types.IProjectDefinition[]|null);
    }

    /** Represents a GetProjectInputsResponse. */
    class GetProjectInputsResponse implements IGetProjectInputsResponse {

        /**
         * Constructs a new GetProjectInputsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetProjectInputsResponse);

        /** GetProjectInputsResponse projects. */
        public projects: pl_types.IProjectDefinition[];

        /**
         * Creates a new GetProjectInputsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetProjectInputsResponse instance
         */
        public static create(properties?: project_management.IGetProjectInputsResponse): project_management.GetProjectInputsResponse;

        /**
         * Encodes the specified GetProjectInputsResponse message. Does not implicitly {@link project_management.GetProjectInputsResponse.verify|verify} messages.
         * @param message GetProjectInputsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetProjectInputsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetProjectInputsResponse message, length delimited. Does not implicitly {@link project_management.GetProjectInputsResponse.verify|verify} messages.
         * @param message GetProjectInputsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetProjectInputsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetProjectInputsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetProjectInputsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetProjectInputsResponse;

        /**
         * Decodes a GetProjectInputsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetProjectInputsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetProjectInputsResponse;

        /**
         * Verifies a GetProjectInputsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetProjectInputsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetProjectInputsResponse
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetProjectInputsResponse;

        /**
         * Creates a plain object from a GetProjectInputsResponse message. Also converts values to other types if specified.
         * @param message GetProjectInputsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetProjectInputsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetProjectInputsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetProjectInputsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetProjectsRequest. */
    interface IGetProjectsRequest {

        /** GetProjectsRequest userXIds */
        userXIds?: (number[]|null);

        /** GetProjectsRequest projectIds */
        projectIds?: (number[]|null);

        /** GetProjectsRequest includeInactive */
        includeInactive?: (boolean|null);

        /** GetProjectsRequest includeTags */
        includeTags?: (boolean|null);

        /** GetProjectsRequest includeInputs */
        includeInputs?: (boolean|null);

        /** GetProjectsRequest includeInputOptions */
        includeInputOptions?: (boolean|null);

        /** GetProjectsRequest includeFulfillments */
        includeFulfillments?: (boolean|null);

        /** GetProjectsRequest includeAssignment */
        includeAssignment?: (boolean|null);

        /** GetProjectsRequest includeMilestones */
        includeMilestones?: (boolean|null);
    }

    /** Represents a GetProjectsRequest. */
    class GetProjectsRequest implements IGetProjectsRequest {

        /**
         * Constructs a new GetProjectsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetProjectsRequest);

        /** GetProjectsRequest userXIds. */
        public userXIds: number[];

        /** GetProjectsRequest projectIds. */
        public projectIds: number[];

        /** GetProjectsRequest includeInactive. */
        public includeInactive?: (boolean|null);

        /** GetProjectsRequest includeTags. */
        public includeTags?: (boolean|null);

        /** GetProjectsRequest includeInputs. */
        public includeInputs?: (boolean|null);

        /** GetProjectsRequest includeInputOptions. */
        public includeInputOptions?: (boolean|null);

        /** GetProjectsRequest includeFulfillments. */
        public includeFulfillments?: (boolean|null);

        /** GetProjectsRequest includeAssignment. */
        public includeAssignment?: (boolean|null);

        /** GetProjectsRequest includeMilestones. */
        public includeMilestones?: (boolean|null);

        /** GetProjectsRequest _includeInactive. */
        public _includeInactive?: "includeInactive";

        /** GetProjectsRequest _includeTags. */
        public _includeTags?: "includeTags";

        /** GetProjectsRequest _includeInputs. */
        public _includeInputs?: "includeInputs";

        /** GetProjectsRequest _includeInputOptions. */
        public _includeInputOptions?: "includeInputOptions";

        /** GetProjectsRequest _includeFulfillments. */
        public _includeFulfillments?: "includeFulfillments";

        /** GetProjectsRequest _includeAssignment. */
        public _includeAssignment?: "includeAssignment";

        /** GetProjectsRequest _includeMilestones. */
        public _includeMilestones?: "includeMilestones";

        /**
         * Creates a new GetProjectsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetProjectsRequest instance
         */
        public static create(properties?: project_management.IGetProjectsRequest): project_management.GetProjectsRequest;

        /**
         * Encodes the specified GetProjectsRequest message. Does not implicitly {@link project_management.GetProjectsRequest.verify|verify} messages.
         * @param message GetProjectsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetProjectsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetProjectsRequest message, length delimited. Does not implicitly {@link project_management.GetProjectsRequest.verify|verify} messages.
         * @param message GetProjectsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetProjectsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetProjectsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetProjectsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetProjectsRequest;

        /**
         * Decodes a GetProjectsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetProjectsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetProjectsRequest;

        /**
         * Verifies a GetProjectsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetProjectsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetProjectsRequest
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetProjectsRequest;

        /**
         * Creates a plain object from a GetProjectsRequest message. Also converts values to other types if specified.
         * @param message GetProjectsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetProjectsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetProjectsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetProjectsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetProjectsResponse. */
    interface IGetProjectsResponse {

        /** GetProjectsResponse projects */
        projects?: (pl_types.IProject[]|null);
    }

    /** Represents a GetProjectsResponse. */
    class GetProjectsResponse implements IGetProjectsResponse {

        /**
         * Constructs a new GetProjectsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetProjectsResponse);

        /** GetProjectsResponse projects. */
        public projects: pl_types.IProject[];

        /**
         * Creates a new GetProjectsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetProjectsResponse instance
         */
        public static create(properties?: project_management.IGetProjectsResponse): project_management.GetProjectsResponse;

        /**
         * Encodes the specified GetProjectsResponse message. Does not implicitly {@link project_management.GetProjectsResponse.verify|verify} messages.
         * @param message GetProjectsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetProjectsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetProjectsResponse message, length delimited. Does not implicitly {@link project_management.GetProjectsResponse.verify|verify} messages.
         * @param message GetProjectsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetProjectsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetProjectsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetProjectsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetProjectsResponse;

        /**
         * Decodes a GetProjectsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetProjectsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetProjectsResponse;

        /**
         * Verifies a GetProjectsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetProjectsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetProjectsResponse
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetProjectsResponse;

        /**
         * Creates a plain object from a GetProjectsResponse message. Also converts values to other types if specified.
         * @param message GetProjectsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetProjectsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetProjectsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetProjectsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpdateProjectRequest. */
    interface IUpdateProjectRequest {

        /** UpdateProjectRequest project */
        project?: (pl_types.IProject|null);
    }

    /** Represents an UpdateProjectRequest. */
    class UpdateProjectRequest implements IUpdateProjectRequest {

        /**
         * Constructs a new UpdateProjectRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IUpdateProjectRequest);

        /** UpdateProjectRequest project. */
        public project?: (pl_types.IProject|null);

        /** UpdateProjectRequest _project. */
        public _project?: "project";

        /**
         * Creates a new UpdateProjectRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpdateProjectRequest instance
         */
        public static create(properties?: project_management.IUpdateProjectRequest): project_management.UpdateProjectRequest;

        /**
         * Encodes the specified UpdateProjectRequest message. Does not implicitly {@link project_management.UpdateProjectRequest.verify|verify} messages.
         * @param message UpdateProjectRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IUpdateProjectRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpdateProjectRequest message, length delimited. Does not implicitly {@link project_management.UpdateProjectRequest.verify|verify} messages.
         * @param message UpdateProjectRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IUpdateProjectRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpdateProjectRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpdateProjectRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.UpdateProjectRequest;

        /**
         * Decodes an UpdateProjectRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpdateProjectRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.UpdateProjectRequest;

        /**
         * Verifies an UpdateProjectRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpdateProjectRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpdateProjectRequest
         */
        public static fromObject(object: { [k: string]: any }): project_management.UpdateProjectRequest;

        /**
         * Creates a plain object from an UpdateProjectRequest message. Also converts values to other types if specified.
         * @param message UpdateProjectRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.UpdateProjectRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpdateProjectRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpdateProjectRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpdateProjectResponse. */
    interface IUpdateProjectResponse {

        /** UpdateProjectResponse project */
        project?: (pl_types.IProject|null);
    }

    /** Represents an UpdateProjectResponse. */
    class UpdateProjectResponse implements IUpdateProjectResponse {

        /**
         * Constructs a new UpdateProjectResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IUpdateProjectResponse);

        /** UpdateProjectResponse project. */
        public project?: (pl_types.IProject|null);

        /** UpdateProjectResponse _project. */
        public _project?: "project";

        /**
         * Creates a new UpdateProjectResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpdateProjectResponse instance
         */
        public static create(properties?: project_management.IUpdateProjectResponse): project_management.UpdateProjectResponse;

        /**
         * Encodes the specified UpdateProjectResponse message. Does not implicitly {@link project_management.UpdateProjectResponse.verify|verify} messages.
         * @param message UpdateProjectResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IUpdateProjectResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpdateProjectResponse message, length delimited. Does not implicitly {@link project_management.UpdateProjectResponse.verify|verify} messages.
         * @param message UpdateProjectResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IUpdateProjectResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpdateProjectResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpdateProjectResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.UpdateProjectResponse;

        /**
         * Decodes an UpdateProjectResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpdateProjectResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.UpdateProjectResponse;

        /**
         * Verifies an UpdateProjectResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpdateProjectResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpdateProjectResponse
         */
        public static fromObject(object: { [k: string]: any }): project_management.UpdateProjectResponse;

        /**
         * Creates a plain object from an UpdateProjectResponse message. Also converts values to other types if specified.
         * @param message UpdateProjectResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.UpdateProjectResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpdateProjectResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpdateProjectResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetProjectDefinitionCategoryTypesRequest. */
    interface IGetProjectDefinitionCategoryTypesRequest {

        /** GetProjectDefinitionCategoryTypesRequest includeDemos */
        includeDemos?: (boolean|null);
    }

    /** Represents a GetProjectDefinitionCategoryTypesRequest. */
    class GetProjectDefinitionCategoryTypesRequest implements IGetProjectDefinitionCategoryTypesRequest {

        /**
         * Constructs a new GetProjectDefinitionCategoryTypesRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetProjectDefinitionCategoryTypesRequest);

        /** GetProjectDefinitionCategoryTypesRequest includeDemos. */
        public includeDemos?: (boolean|null);

        /** GetProjectDefinitionCategoryTypesRequest _includeDemos. */
        public _includeDemos?: "includeDemos";

        /**
         * Creates a new GetProjectDefinitionCategoryTypesRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetProjectDefinitionCategoryTypesRequest instance
         */
        public static create(properties?: project_management.IGetProjectDefinitionCategoryTypesRequest): project_management.GetProjectDefinitionCategoryTypesRequest;

        /**
         * Encodes the specified GetProjectDefinitionCategoryTypesRequest message. Does not implicitly {@link project_management.GetProjectDefinitionCategoryTypesRequest.verify|verify} messages.
         * @param message GetProjectDefinitionCategoryTypesRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetProjectDefinitionCategoryTypesRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetProjectDefinitionCategoryTypesRequest message, length delimited. Does not implicitly {@link project_management.GetProjectDefinitionCategoryTypesRequest.verify|verify} messages.
         * @param message GetProjectDefinitionCategoryTypesRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetProjectDefinitionCategoryTypesRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetProjectDefinitionCategoryTypesRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetProjectDefinitionCategoryTypesRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetProjectDefinitionCategoryTypesRequest;

        /**
         * Decodes a GetProjectDefinitionCategoryTypesRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetProjectDefinitionCategoryTypesRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetProjectDefinitionCategoryTypesRequest;

        /**
         * Verifies a GetProjectDefinitionCategoryTypesRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetProjectDefinitionCategoryTypesRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetProjectDefinitionCategoryTypesRequest
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetProjectDefinitionCategoryTypesRequest;

        /**
         * Creates a plain object from a GetProjectDefinitionCategoryTypesRequest message. Also converts values to other types if specified.
         * @param message GetProjectDefinitionCategoryTypesRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetProjectDefinitionCategoryTypesRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetProjectDefinitionCategoryTypesRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetProjectDefinitionCategoryTypesRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetProjectDefinitionCategoryTypesResponse. */
    interface IGetProjectDefinitionCategoryTypesResponse {

        /** GetProjectDefinitionCategoryTypesResponse inputCategories */
        inputCategories?: (pl_types.IProjectInputCategory[]|null);
    }

    /** Represents a GetProjectDefinitionCategoryTypesResponse. */
    class GetProjectDefinitionCategoryTypesResponse implements IGetProjectDefinitionCategoryTypesResponse {

        /**
         * Constructs a new GetProjectDefinitionCategoryTypesResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetProjectDefinitionCategoryTypesResponse);

        /** GetProjectDefinitionCategoryTypesResponse inputCategories. */
        public inputCategories: pl_types.IProjectInputCategory[];

        /**
         * Creates a new GetProjectDefinitionCategoryTypesResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetProjectDefinitionCategoryTypesResponse instance
         */
        public static create(properties?: project_management.IGetProjectDefinitionCategoryTypesResponse): project_management.GetProjectDefinitionCategoryTypesResponse;

        /**
         * Encodes the specified GetProjectDefinitionCategoryTypesResponse message. Does not implicitly {@link project_management.GetProjectDefinitionCategoryTypesResponse.verify|verify} messages.
         * @param message GetProjectDefinitionCategoryTypesResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetProjectDefinitionCategoryTypesResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetProjectDefinitionCategoryTypesResponse message, length delimited. Does not implicitly {@link project_management.GetProjectDefinitionCategoryTypesResponse.verify|verify} messages.
         * @param message GetProjectDefinitionCategoryTypesResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetProjectDefinitionCategoryTypesResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetProjectDefinitionCategoryTypesResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetProjectDefinitionCategoryTypesResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetProjectDefinitionCategoryTypesResponse;

        /**
         * Decodes a GetProjectDefinitionCategoryTypesResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetProjectDefinitionCategoryTypesResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetProjectDefinitionCategoryTypesResponse;

        /**
         * Verifies a GetProjectDefinitionCategoryTypesResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetProjectDefinitionCategoryTypesResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetProjectDefinitionCategoryTypesResponse
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetProjectDefinitionCategoryTypesResponse;

        /**
         * Creates a plain object from a GetProjectDefinitionCategoryTypesResponse message. Also converts values to other types if specified.
         * @param message GetProjectDefinitionCategoryTypesResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetProjectDefinitionCategoryTypesResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetProjectDefinitionCategoryTypesResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetProjectDefinitionCategoryTypesResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a RegisterAnonymousProjectsRequest. */
    interface IRegisterAnonymousProjectsRequest {

        /** RegisterAnonymousProjectsRequest projectInputId */
        projectInputId?: (number|null);
    }

    /** Represents a RegisterAnonymousProjectsRequest. */
    class RegisterAnonymousProjectsRequest implements IRegisterAnonymousProjectsRequest {

        /**
         * Constructs a new RegisterAnonymousProjectsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IRegisterAnonymousProjectsRequest);

        /** RegisterAnonymousProjectsRequest projectInputId. */
        public projectInputId?: (number|null);

        /** RegisterAnonymousProjectsRequest _projectInputId. */
        public _projectInputId?: "projectInputId";

        /**
         * Creates a new RegisterAnonymousProjectsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns RegisterAnonymousProjectsRequest instance
         */
        public static create(properties?: project_management.IRegisterAnonymousProjectsRequest): project_management.RegisterAnonymousProjectsRequest;

        /**
         * Encodes the specified RegisterAnonymousProjectsRequest message. Does not implicitly {@link project_management.RegisterAnonymousProjectsRequest.verify|verify} messages.
         * @param message RegisterAnonymousProjectsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IRegisterAnonymousProjectsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified RegisterAnonymousProjectsRequest message, length delimited. Does not implicitly {@link project_management.RegisterAnonymousProjectsRequest.verify|verify} messages.
         * @param message RegisterAnonymousProjectsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IRegisterAnonymousProjectsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a RegisterAnonymousProjectsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns RegisterAnonymousProjectsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.RegisterAnonymousProjectsRequest;

        /**
         * Decodes a RegisterAnonymousProjectsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns RegisterAnonymousProjectsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.RegisterAnonymousProjectsRequest;

        /**
         * Verifies a RegisterAnonymousProjectsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a RegisterAnonymousProjectsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns RegisterAnonymousProjectsRequest
         */
        public static fromObject(object: { [k: string]: any }): project_management.RegisterAnonymousProjectsRequest;

        /**
         * Creates a plain object from a RegisterAnonymousProjectsRequest message. Also converts values to other types if specified.
         * @param message RegisterAnonymousProjectsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.RegisterAnonymousProjectsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this RegisterAnonymousProjectsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for RegisterAnonymousProjectsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a RegisterAnonymousProjectsResponse. */
    interface IRegisterAnonymousProjectsResponse {
    }

    /** Represents a RegisterAnonymousProjectsResponse. */
    class RegisterAnonymousProjectsResponse implements IRegisterAnonymousProjectsResponse {

        /**
         * Constructs a new RegisterAnonymousProjectsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IRegisterAnonymousProjectsResponse);

        /**
         * Creates a new RegisterAnonymousProjectsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns RegisterAnonymousProjectsResponse instance
         */
        public static create(properties?: project_management.IRegisterAnonymousProjectsResponse): project_management.RegisterAnonymousProjectsResponse;

        /**
         * Encodes the specified RegisterAnonymousProjectsResponse message. Does not implicitly {@link project_management.RegisterAnonymousProjectsResponse.verify|verify} messages.
         * @param message RegisterAnonymousProjectsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IRegisterAnonymousProjectsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified RegisterAnonymousProjectsResponse message, length delimited. Does not implicitly {@link project_management.RegisterAnonymousProjectsResponse.verify|verify} messages.
         * @param message RegisterAnonymousProjectsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IRegisterAnonymousProjectsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a RegisterAnonymousProjectsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns RegisterAnonymousProjectsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.RegisterAnonymousProjectsResponse;

        /**
         * Decodes a RegisterAnonymousProjectsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns RegisterAnonymousProjectsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.RegisterAnonymousProjectsResponse;

        /**
         * Verifies a RegisterAnonymousProjectsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a RegisterAnonymousProjectsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns RegisterAnonymousProjectsResponse
         */
        public static fromObject(object: { [k: string]: any }): project_management.RegisterAnonymousProjectsResponse;

        /**
         * Creates a plain object from a RegisterAnonymousProjectsResponse message. Also converts values to other types if specified.
         * @param message RegisterAnonymousProjectsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.RegisterAnonymousProjectsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this RegisterAnonymousProjectsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for RegisterAnonymousProjectsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetKnowledgeAndSkillsRequest. */
    interface IGetKnowledgeAndSkillsRequest {

        /** GetKnowledgeAndSkillsRequest types */
        types?: (pl_types.KnowledgeAndSkill.Type[]|null);
    }

    /** Represents a GetKnowledgeAndSkillsRequest. */
    class GetKnowledgeAndSkillsRequest implements IGetKnowledgeAndSkillsRequest {

        /**
         * Constructs a new GetKnowledgeAndSkillsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetKnowledgeAndSkillsRequest);

        /** GetKnowledgeAndSkillsRequest types. */
        public types: pl_types.KnowledgeAndSkill.Type[];

        /**
         * Creates a new GetKnowledgeAndSkillsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetKnowledgeAndSkillsRequest instance
         */
        public static create(properties?: project_management.IGetKnowledgeAndSkillsRequest): project_management.GetKnowledgeAndSkillsRequest;

        /**
         * Encodes the specified GetKnowledgeAndSkillsRequest message. Does not implicitly {@link project_management.GetKnowledgeAndSkillsRequest.verify|verify} messages.
         * @param message GetKnowledgeAndSkillsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetKnowledgeAndSkillsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetKnowledgeAndSkillsRequest message, length delimited. Does not implicitly {@link project_management.GetKnowledgeAndSkillsRequest.verify|verify} messages.
         * @param message GetKnowledgeAndSkillsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetKnowledgeAndSkillsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetKnowledgeAndSkillsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetKnowledgeAndSkillsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetKnowledgeAndSkillsRequest;

        /**
         * Decodes a GetKnowledgeAndSkillsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetKnowledgeAndSkillsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetKnowledgeAndSkillsRequest;

        /**
         * Verifies a GetKnowledgeAndSkillsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetKnowledgeAndSkillsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetKnowledgeAndSkillsRequest
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetKnowledgeAndSkillsRequest;

        /**
         * Creates a plain object from a GetKnowledgeAndSkillsRequest message. Also converts values to other types if specified.
         * @param message GetKnowledgeAndSkillsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetKnowledgeAndSkillsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetKnowledgeAndSkillsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetKnowledgeAndSkillsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetKnowledgeAndSkillsResponse. */
    interface IGetKnowledgeAndSkillsResponse {

        /** GetKnowledgeAndSkillsResponse knowledgeAndSkills */
        knowledgeAndSkills?: (pl_types.IKnowledgeAndSkill[]|null);
    }

    /** Represents a GetKnowledgeAndSkillsResponse. */
    class GetKnowledgeAndSkillsResponse implements IGetKnowledgeAndSkillsResponse {

        /**
         * Constructs a new GetKnowledgeAndSkillsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IGetKnowledgeAndSkillsResponse);

        /** GetKnowledgeAndSkillsResponse knowledgeAndSkills. */
        public knowledgeAndSkills: pl_types.IKnowledgeAndSkill[];

        /**
         * Creates a new GetKnowledgeAndSkillsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetKnowledgeAndSkillsResponse instance
         */
        public static create(properties?: project_management.IGetKnowledgeAndSkillsResponse): project_management.GetKnowledgeAndSkillsResponse;

        /**
         * Encodes the specified GetKnowledgeAndSkillsResponse message. Does not implicitly {@link project_management.GetKnowledgeAndSkillsResponse.verify|verify} messages.
         * @param message GetKnowledgeAndSkillsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IGetKnowledgeAndSkillsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetKnowledgeAndSkillsResponse message, length delimited. Does not implicitly {@link project_management.GetKnowledgeAndSkillsResponse.verify|verify} messages.
         * @param message GetKnowledgeAndSkillsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IGetKnowledgeAndSkillsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetKnowledgeAndSkillsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetKnowledgeAndSkillsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.GetKnowledgeAndSkillsResponse;

        /**
         * Decodes a GetKnowledgeAndSkillsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetKnowledgeAndSkillsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.GetKnowledgeAndSkillsResponse;

        /**
         * Verifies a GetKnowledgeAndSkillsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetKnowledgeAndSkillsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetKnowledgeAndSkillsResponse
         */
        public static fromObject(object: { [k: string]: any }): project_management.GetKnowledgeAndSkillsResponse;

        /**
         * Creates a plain object from a GetKnowledgeAndSkillsResponse message. Also converts values to other types if specified.
         * @param message GetKnowledgeAndSkillsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.GetKnowledgeAndSkillsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetKnowledgeAndSkillsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetKnowledgeAndSkillsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertKnowledgeAndSkillRequest. */
    interface IUpsertKnowledgeAndSkillRequest {

        /** UpsertKnowledgeAndSkillRequest knowledgeAndSkill */
        knowledgeAndSkill?: (pl_types.IKnowledgeAndSkill|null);
    }

    /** Represents an UpsertKnowledgeAndSkillRequest. */
    class UpsertKnowledgeAndSkillRequest implements IUpsertKnowledgeAndSkillRequest {

        /**
         * Constructs a new UpsertKnowledgeAndSkillRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IUpsertKnowledgeAndSkillRequest);

        /** UpsertKnowledgeAndSkillRequest knowledgeAndSkill. */
        public knowledgeAndSkill?: (pl_types.IKnowledgeAndSkill|null);

        /** UpsertKnowledgeAndSkillRequest _knowledgeAndSkill. */
        public _knowledgeAndSkill?: "knowledgeAndSkill";

        /**
         * Creates a new UpsertKnowledgeAndSkillRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertKnowledgeAndSkillRequest instance
         */
        public static create(properties?: project_management.IUpsertKnowledgeAndSkillRequest): project_management.UpsertKnowledgeAndSkillRequest;

        /**
         * Encodes the specified UpsertKnowledgeAndSkillRequest message. Does not implicitly {@link project_management.UpsertKnowledgeAndSkillRequest.verify|verify} messages.
         * @param message UpsertKnowledgeAndSkillRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IUpsertKnowledgeAndSkillRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertKnowledgeAndSkillRequest message, length delimited. Does not implicitly {@link project_management.UpsertKnowledgeAndSkillRequest.verify|verify} messages.
         * @param message UpsertKnowledgeAndSkillRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IUpsertKnowledgeAndSkillRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertKnowledgeAndSkillRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertKnowledgeAndSkillRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.UpsertKnowledgeAndSkillRequest;

        /**
         * Decodes an UpsertKnowledgeAndSkillRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertKnowledgeAndSkillRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.UpsertKnowledgeAndSkillRequest;

        /**
         * Verifies an UpsertKnowledgeAndSkillRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertKnowledgeAndSkillRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertKnowledgeAndSkillRequest
         */
        public static fromObject(object: { [k: string]: any }): project_management.UpsertKnowledgeAndSkillRequest;

        /**
         * Creates a plain object from an UpsertKnowledgeAndSkillRequest message. Also converts values to other types if specified.
         * @param message UpsertKnowledgeAndSkillRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.UpsertKnowledgeAndSkillRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertKnowledgeAndSkillRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertKnowledgeAndSkillRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertKnowledgeAndSkillResponse. */
    interface IUpsertKnowledgeAndSkillResponse {

        /** UpsertKnowledgeAndSkillResponse knowledgeAndSkill */
        knowledgeAndSkill?: (pl_types.IKnowledgeAndSkill|null);
    }

    /** Represents an UpsertKnowledgeAndSkillResponse. */
    class UpsertKnowledgeAndSkillResponse implements IUpsertKnowledgeAndSkillResponse {

        /**
         * Constructs a new UpsertKnowledgeAndSkillResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: project_management.IUpsertKnowledgeAndSkillResponse);

        /** UpsertKnowledgeAndSkillResponse knowledgeAndSkill. */
        public knowledgeAndSkill?: (pl_types.IKnowledgeAndSkill|null);

        /** UpsertKnowledgeAndSkillResponse _knowledgeAndSkill. */
        public _knowledgeAndSkill?: "knowledgeAndSkill";

        /**
         * Creates a new UpsertKnowledgeAndSkillResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertKnowledgeAndSkillResponse instance
         */
        public static create(properties?: project_management.IUpsertKnowledgeAndSkillResponse): project_management.UpsertKnowledgeAndSkillResponse;

        /**
         * Encodes the specified UpsertKnowledgeAndSkillResponse message. Does not implicitly {@link project_management.UpsertKnowledgeAndSkillResponse.verify|verify} messages.
         * @param message UpsertKnowledgeAndSkillResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: project_management.IUpsertKnowledgeAndSkillResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertKnowledgeAndSkillResponse message, length delimited. Does not implicitly {@link project_management.UpsertKnowledgeAndSkillResponse.verify|verify} messages.
         * @param message UpsertKnowledgeAndSkillResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: project_management.IUpsertKnowledgeAndSkillResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertKnowledgeAndSkillResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertKnowledgeAndSkillResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): project_management.UpsertKnowledgeAndSkillResponse;

        /**
         * Decodes an UpsertKnowledgeAndSkillResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertKnowledgeAndSkillResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): project_management.UpsertKnowledgeAndSkillResponse;

        /**
         * Verifies an UpsertKnowledgeAndSkillResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertKnowledgeAndSkillResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertKnowledgeAndSkillResponse
         */
        public static fromObject(object: { [k: string]: any }): project_management.UpsertKnowledgeAndSkillResponse;

        /**
         * Creates a plain object from an UpsertKnowledgeAndSkillResponse message. Also converts values to other types if specified.
         * @param message UpsertKnowledgeAndSkillResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: project_management.UpsertKnowledgeAndSkillResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertKnowledgeAndSkillResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertKnowledgeAndSkillResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace school_management. */
export namespace school_management {

    /** Represents a SchoolManagementService */
    class SchoolManagementService extends $protobuf.rpc.Service {

        /**
         * Constructs a new SchoolManagementService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new SchoolManagementService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): SchoolManagementService;

        /**
         * Calls GetSchools.
         * @param request GetSchoolsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and SchoolInformationResponse
         */
        public getSchools(request: school_management.IGetSchoolsRequest, callback: school_management.SchoolManagementService.GetSchoolsCallback): void;

        /**
         * Calls GetSchools.
         * @param request GetSchoolsRequest message or plain object
         * @returns Promise
         */
        public getSchools(request: school_management.IGetSchoolsRequest): Promise<school_management.SchoolInformationResponse>;

        /**
         * Calls GetPagedSchools.
         * @param request GetPagedSchoolsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetPagedSchoolsResponse
         */
        public getPagedSchools(request: school_management.IGetPagedSchoolsRequest, callback: school_management.SchoolManagementService.GetPagedSchoolsCallback): void;

        /**
         * Calls GetPagedSchools.
         * @param request GetPagedSchoolsRequest message or plain object
         * @returns Promise
         */
        public getPagedSchools(request: school_management.IGetPagedSchoolsRequest): Promise<school_management.GetPagedSchoolsResponse>;

        /**
         * Calls UpsertSchool.
         * @param request UpsertSchoolRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and SchoolInformationResponse
         */
        public upsertSchool(request: school_management.IUpsertSchoolRequest, callback: school_management.SchoolManagementService.UpsertSchoolCallback): void;

        /**
         * Calls UpsertSchool.
         * @param request UpsertSchoolRequest message or plain object
         * @returns Promise
         */
        public upsertSchool(request: school_management.IUpsertSchoolRequest): Promise<school_management.SchoolInformationResponse>;

        /**
         * Calls RemoveSchool.
         * @param request RemoveSchoolRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and SchoolInformationResponse
         */
        public removeSchool(request: school_management.IRemoveSchoolRequest, callback: school_management.SchoolManagementService.RemoveSchoolCallback): void;

        /**
         * Calls RemoveSchool.
         * @param request RemoveSchoolRequest message or plain object
         * @returns Promise
         */
        public removeSchool(request: school_management.IRemoveSchoolRequest): Promise<school_management.SchoolInformationResponse>;
    }

    namespace SchoolManagementService {

        /**
         * Callback as used by {@link school_management.SchoolManagementService#getSchools}.
         * @param error Error, if any
         * @param [response] SchoolInformationResponse
         */
        type GetSchoolsCallback = (error: (Error|null), response?: school_management.SchoolInformationResponse) => void;

        /**
         * Callback as used by {@link school_management.SchoolManagementService#getPagedSchools}.
         * @param error Error, if any
         * @param [response] GetPagedSchoolsResponse
         */
        type GetPagedSchoolsCallback = (error: (Error|null), response?: school_management.GetPagedSchoolsResponse) => void;

        /**
         * Callback as used by {@link school_management.SchoolManagementService#upsertSchool}.
         * @param error Error, if any
         * @param [response] SchoolInformationResponse
         */
        type UpsertSchoolCallback = (error: (Error|null), response?: school_management.SchoolInformationResponse) => void;

        /**
         * Callback as used by {@link school_management.SchoolManagementService#removeSchool}.
         * @param error Error, if any
         * @param [response] SchoolInformationResponse
         */
        type RemoveSchoolCallback = (error: (Error|null), response?: school_management.SchoolInformationResponse) => void;
    }

    /** Properties of a GetSchoolsRequest. */
    interface IGetSchoolsRequest {

        /** GetSchoolsRequest districtId */
        districtId?: (number|null);
    }

    /** Represents a GetSchoolsRequest. */
    class GetSchoolsRequest implements IGetSchoolsRequest {

        /**
         * Constructs a new GetSchoolsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: school_management.IGetSchoolsRequest);

        /** GetSchoolsRequest districtId. */
        public districtId?: (number|null);

        /** GetSchoolsRequest _districtId. */
        public _districtId?: "districtId";

        /**
         * Creates a new GetSchoolsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetSchoolsRequest instance
         */
        public static create(properties?: school_management.IGetSchoolsRequest): school_management.GetSchoolsRequest;

        /**
         * Encodes the specified GetSchoolsRequest message. Does not implicitly {@link school_management.GetSchoolsRequest.verify|verify} messages.
         * @param message GetSchoolsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: school_management.IGetSchoolsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetSchoolsRequest message, length delimited. Does not implicitly {@link school_management.GetSchoolsRequest.verify|verify} messages.
         * @param message GetSchoolsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: school_management.IGetSchoolsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetSchoolsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetSchoolsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): school_management.GetSchoolsRequest;

        /**
         * Decodes a GetSchoolsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetSchoolsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): school_management.GetSchoolsRequest;

        /**
         * Verifies a GetSchoolsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetSchoolsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetSchoolsRequest
         */
        public static fromObject(object: { [k: string]: any }): school_management.GetSchoolsRequest;

        /**
         * Creates a plain object from a GetSchoolsRequest message. Also converts values to other types if specified.
         * @param message GetSchoolsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: school_management.GetSchoolsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetSchoolsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetSchoolsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetPagedSchoolsRequest. */
    interface IGetPagedSchoolsRequest {

        /** GetPagedSchoolsRequest districtId */
        districtId?: (number|null);

        /** GetPagedSchoolsRequest page */
        page?: (number|null);

        /** GetPagedSchoolsRequest pageSize */
        pageSize?: (number|null);

        /** GetPagedSchoolsRequest searchText */
        searchText?: (string|null);
    }

    /** Represents a GetPagedSchoolsRequest. */
    class GetPagedSchoolsRequest implements IGetPagedSchoolsRequest {

        /**
         * Constructs a new GetPagedSchoolsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: school_management.IGetPagedSchoolsRequest);

        /** GetPagedSchoolsRequest districtId. */
        public districtId?: (number|null);

        /** GetPagedSchoolsRequest page. */
        public page?: (number|null);

        /** GetPagedSchoolsRequest pageSize. */
        public pageSize?: (number|null);

        /** GetPagedSchoolsRequest searchText. */
        public searchText?: (string|null);

        /** GetPagedSchoolsRequest _districtId. */
        public _districtId?: "districtId";

        /** GetPagedSchoolsRequest _page. */
        public _page?: "page";

        /** GetPagedSchoolsRequest _pageSize. */
        public _pageSize?: "pageSize";

        /** GetPagedSchoolsRequest _searchText. */
        public _searchText?: "searchText";

        /**
         * Creates a new GetPagedSchoolsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetPagedSchoolsRequest instance
         */
        public static create(properties?: school_management.IGetPagedSchoolsRequest): school_management.GetPagedSchoolsRequest;

        /**
         * Encodes the specified GetPagedSchoolsRequest message. Does not implicitly {@link school_management.GetPagedSchoolsRequest.verify|verify} messages.
         * @param message GetPagedSchoolsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: school_management.IGetPagedSchoolsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetPagedSchoolsRequest message, length delimited. Does not implicitly {@link school_management.GetPagedSchoolsRequest.verify|verify} messages.
         * @param message GetPagedSchoolsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: school_management.IGetPagedSchoolsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetPagedSchoolsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetPagedSchoolsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): school_management.GetPagedSchoolsRequest;

        /**
         * Decodes a GetPagedSchoolsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetPagedSchoolsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): school_management.GetPagedSchoolsRequest;

        /**
         * Verifies a GetPagedSchoolsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetPagedSchoolsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetPagedSchoolsRequest
         */
        public static fromObject(object: { [k: string]: any }): school_management.GetPagedSchoolsRequest;

        /**
         * Creates a plain object from a GetPagedSchoolsRequest message. Also converts values to other types if specified.
         * @param message GetPagedSchoolsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: school_management.GetPagedSchoolsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetPagedSchoolsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetPagedSchoolsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetPagedSchoolsResponse. */
    interface IGetPagedSchoolsResponse {

        /** GetPagedSchoolsResponse schools */
        schools?: (pl_types.ISchool[]|null);

        /** GetPagedSchoolsResponse totalSchools */
        totalSchools?: (number|null);
    }

    /** Represents a GetPagedSchoolsResponse. */
    class GetPagedSchoolsResponse implements IGetPagedSchoolsResponse {

        /**
         * Constructs a new GetPagedSchoolsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: school_management.IGetPagedSchoolsResponse);

        /** GetPagedSchoolsResponse schools. */
        public schools: pl_types.ISchool[];

        /** GetPagedSchoolsResponse totalSchools. */
        public totalSchools?: (number|null);

        /** GetPagedSchoolsResponse _totalSchools. */
        public _totalSchools?: "totalSchools";

        /**
         * Creates a new GetPagedSchoolsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetPagedSchoolsResponse instance
         */
        public static create(properties?: school_management.IGetPagedSchoolsResponse): school_management.GetPagedSchoolsResponse;

        /**
         * Encodes the specified GetPagedSchoolsResponse message. Does not implicitly {@link school_management.GetPagedSchoolsResponse.verify|verify} messages.
         * @param message GetPagedSchoolsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: school_management.IGetPagedSchoolsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetPagedSchoolsResponse message, length delimited. Does not implicitly {@link school_management.GetPagedSchoolsResponse.verify|verify} messages.
         * @param message GetPagedSchoolsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: school_management.IGetPagedSchoolsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetPagedSchoolsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetPagedSchoolsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): school_management.GetPagedSchoolsResponse;

        /**
         * Decodes a GetPagedSchoolsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetPagedSchoolsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): school_management.GetPagedSchoolsResponse;

        /**
         * Verifies a GetPagedSchoolsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetPagedSchoolsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetPagedSchoolsResponse
         */
        public static fromObject(object: { [k: string]: any }): school_management.GetPagedSchoolsResponse;

        /**
         * Creates a plain object from a GetPagedSchoolsResponse message. Also converts values to other types if specified.
         * @param message GetPagedSchoolsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: school_management.GetPagedSchoolsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetPagedSchoolsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetPagedSchoolsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertSchoolRequest. */
    interface IUpsertSchoolRequest {

        /** UpsertSchoolRequest school */
        school?: (pl_types.ISchool|null);
    }

    /** Represents an UpsertSchoolRequest. */
    class UpsertSchoolRequest implements IUpsertSchoolRequest {

        /**
         * Constructs a new UpsertSchoolRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: school_management.IUpsertSchoolRequest);

        /** UpsertSchoolRequest school. */
        public school?: (pl_types.ISchool|null);

        /** UpsertSchoolRequest _school. */
        public _school?: "school";

        /**
         * Creates a new UpsertSchoolRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertSchoolRequest instance
         */
        public static create(properties?: school_management.IUpsertSchoolRequest): school_management.UpsertSchoolRequest;

        /**
         * Encodes the specified UpsertSchoolRequest message. Does not implicitly {@link school_management.UpsertSchoolRequest.verify|verify} messages.
         * @param message UpsertSchoolRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: school_management.IUpsertSchoolRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertSchoolRequest message, length delimited. Does not implicitly {@link school_management.UpsertSchoolRequest.verify|verify} messages.
         * @param message UpsertSchoolRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: school_management.IUpsertSchoolRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertSchoolRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertSchoolRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): school_management.UpsertSchoolRequest;

        /**
         * Decodes an UpsertSchoolRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertSchoolRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): school_management.UpsertSchoolRequest;

        /**
         * Verifies an UpsertSchoolRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertSchoolRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertSchoolRequest
         */
        public static fromObject(object: { [k: string]: any }): school_management.UpsertSchoolRequest;

        /**
         * Creates a plain object from an UpsertSchoolRequest message. Also converts values to other types if specified.
         * @param message UpsertSchoolRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: school_management.UpsertSchoolRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertSchoolRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertSchoolRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a RemoveSchoolRequest. */
    interface IRemoveSchoolRequest {

        /** RemoveSchoolRequest districtId */
        districtId?: (number|null);

        /** RemoveSchoolRequest schoolId */
        schoolId?: (number|null);
    }

    /** Represents a RemoveSchoolRequest. */
    class RemoveSchoolRequest implements IRemoveSchoolRequest {

        /**
         * Constructs a new RemoveSchoolRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: school_management.IRemoveSchoolRequest);

        /** RemoveSchoolRequest districtId. */
        public districtId?: (number|null);

        /** RemoveSchoolRequest schoolId. */
        public schoolId?: (number|null);

        /** RemoveSchoolRequest _districtId. */
        public _districtId?: "districtId";

        /** RemoveSchoolRequest _schoolId. */
        public _schoolId?: "schoolId";

        /**
         * Creates a new RemoveSchoolRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns RemoveSchoolRequest instance
         */
        public static create(properties?: school_management.IRemoveSchoolRequest): school_management.RemoveSchoolRequest;

        /**
         * Encodes the specified RemoveSchoolRequest message. Does not implicitly {@link school_management.RemoveSchoolRequest.verify|verify} messages.
         * @param message RemoveSchoolRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: school_management.IRemoveSchoolRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified RemoveSchoolRequest message, length delimited. Does not implicitly {@link school_management.RemoveSchoolRequest.verify|verify} messages.
         * @param message RemoveSchoolRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: school_management.IRemoveSchoolRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a RemoveSchoolRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns RemoveSchoolRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): school_management.RemoveSchoolRequest;

        /**
         * Decodes a RemoveSchoolRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns RemoveSchoolRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): school_management.RemoveSchoolRequest;

        /**
         * Verifies a RemoveSchoolRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a RemoveSchoolRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns RemoveSchoolRequest
         */
        public static fromObject(object: { [k: string]: any }): school_management.RemoveSchoolRequest;

        /**
         * Creates a plain object from a RemoveSchoolRequest message. Also converts values to other types if specified.
         * @param message RemoveSchoolRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: school_management.RemoveSchoolRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this RemoveSchoolRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for RemoveSchoolRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a SchoolInformationResponse. */
    interface ISchoolInformationResponse {

        /** SchoolInformationResponse districtId */
        districtId?: (number|null);

        /** SchoolInformationResponse nextSchoolId */
        nextSchoolId?: (number|null);

        /** SchoolInformationResponse schools */
        schools?: (pl_types.ISchool[]|null);
    }

    /** Represents a SchoolInformationResponse. */
    class SchoolInformationResponse implements ISchoolInformationResponse {

        /**
         * Constructs a new SchoolInformationResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: school_management.ISchoolInformationResponse);

        /** SchoolInformationResponse districtId. */
        public districtId?: (number|null);

        /** SchoolInformationResponse nextSchoolId. */
        public nextSchoolId?: (number|null);

        /** SchoolInformationResponse schools. */
        public schools: pl_types.ISchool[];

        /** SchoolInformationResponse _districtId. */
        public _districtId?: "districtId";

        /** SchoolInformationResponse _nextSchoolId. */
        public _nextSchoolId?: "nextSchoolId";

        /**
         * Creates a new SchoolInformationResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns SchoolInformationResponse instance
         */
        public static create(properties?: school_management.ISchoolInformationResponse): school_management.SchoolInformationResponse;

        /**
         * Encodes the specified SchoolInformationResponse message. Does not implicitly {@link school_management.SchoolInformationResponse.verify|verify} messages.
         * @param message SchoolInformationResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: school_management.ISchoolInformationResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified SchoolInformationResponse message, length delimited. Does not implicitly {@link school_management.SchoolInformationResponse.verify|verify} messages.
         * @param message SchoolInformationResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: school_management.ISchoolInformationResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a SchoolInformationResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns SchoolInformationResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): school_management.SchoolInformationResponse;

        /**
         * Decodes a SchoolInformationResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns SchoolInformationResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): school_management.SchoolInformationResponse;

        /**
         * Verifies a SchoolInformationResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a SchoolInformationResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns SchoolInformationResponse
         */
        public static fromObject(object: { [k: string]: any }): school_management.SchoolInformationResponse;

        /**
         * Creates a plain object from a SchoolInformationResponse message. Also converts values to other types if specified.
         * @param message SchoolInformationResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: school_management.SchoolInformationResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this SchoolInformationResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for SchoolInformationResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace tag_service. */
export namespace tag_service {

    /** Represents a TagService */
    class TagService extends $protobuf.rpc.Service {

        /**
         * Constructs a new TagService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new TagService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): TagService;

        /**
         * Calls GetAllPreviousTags.
         * @param request GetAllPreviousTagsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetAllPreviousTagsResponse
         */
        public getAllPreviousTags(request: tag_service.IGetAllPreviousTagsRequest, callback: tag_service.TagService.GetAllPreviousTagsCallback): void;

        /**
         * Calls GetAllPreviousTags.
         * @param request GetAllPreviousTagsRequest message or plain object
         * @returns Promise
         */
        public getAllPreviousTags(request: tag_service.IGetAllPreviousTagsRequest): Promise<tag_service.GetAllPreviousTagsResponse>;
    }

    namespace TagService {

        /**
         * Callback as used by {@link tag_service.TagService#getAllPreviousTags}.
         * @param error Error, if any
         * @param [response] GetAllPreviousTagsResponse
         */
        type GetAllPreviousTagsCallback = (error: (Error|null), response?: tag_service.GetAllPreviousTagsResponse) => void;
    }

    /** Properties of a GetAllPreviousTagsRequest. */
    interface IGetAllPreviousTagsRequest {

        /** GetAllPreviousTagsRequest userXId */
        userXId?: (number|null);
    }

    /** Represents a GetAllPreviousTagsRequest. */
    class GetAllPreviousTagsRequest implements IGetAllPreviousTagsRequest {

        /**
         * Constructs a new GetAllPreviousTagsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: tag_service.IGetAllPreviousTagsRequest);

        /** GetAllPreviousTagsRequest userXId. */
        public userXId?: (number|null);

        /** GetAllPreviousTagsRequest _userXId. */
        public _userXId?: "userXId";

        /**
         * Creates a new GetAllPreviousTagsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetAllPreviousTagsRequest instance
         */
        public static create(properties?: tag_service.IGetAllPreviousTagsRequest): tag_service.GetAllPreviousTagsRequest;

        /**
         * Encodes the specified GetAllPreviousTagsRequest message. Does not implicitly {@link tag_service.GetAllPreviousTagsRequest.verify|verify} messages.
         * @param message GetAllPreviousTagsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: tag_service.IGetAllPreviousTagsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetAllPreviousTagsRequest message, length delimited. Does not implicitly {@link tag_service.GetAllPreviousTagsRequest.verify|verify} messages.
         * @param message GetAllPreviousTagsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: tag_service.IGetAllPreviousTagsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetAllPreviousTagsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetAllPreviousTagsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): tag_service.GetAllPreviousTagsRequest;

        /**
         * Decodes a GetAllPreviousTagsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetAllPreviousTagsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): tag_service.GetAllPreviousTagsRequest;

        /**
         * Verifies a GetAllPreviousTagsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetAllPreviousTagsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetAllPreviousTagsRequest
         */
        public static fromObject(object: { [k: string]: any }): tag_service.GetAllPreviousTagsRequest;

        /**
         * Creates a plain object from a GetAllPreviousTagsRequest message. Also converts values to other types if specified.
         * @param message GetAllPreviousTagsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: tag_service.GetAllPreviousTagsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetAllPreviousTagsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetAllPreviousTagsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetAllPreviousTagsResponse. */
    interface IGetAllPreviousTagsResponse {

        /** GetAllPreviousTagsResponse tags */
        tags?: (pl_types.ITag[]|null);
    }

    /** Represents a GetAllPreviousTagsResponse. */
    class GetAllPreviousTagsResponse implements IGetAllPreviousTagsResponse {

        /**
         * Constructs a new GetAllPreviousTagsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: tag_service.IGetAllPreviousTagsResponse);

        /** GetAllPreviousTagsResponse tags. */
        public tags: pl_types.ITag[];

        /**
         * Creates a new GetAllPreviousTagsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetAllPreviousTagsResponse instance
         */
        public static create(properties?: tag_service.IGetAllPreviousTagsResponse): tag_service.GetAllPreviousTagsResponse;

        /**
         * Encodes the specified GetAllPreviousTagsResponse message. Does not implicitly {@link tag_service.GetAllPreviousTagsResponse.verify|verify} messages.
         * @param message GetAllPreviousTagsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: tag_service.IGetAllPreviousTagsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetAllPreviousTagsResponse message, length delimited. Does not implicitly {@link tag_service.GetAllPreviousTagsResponse.verify|verify} messages.
         * @param message GetAllPreviousTagsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: tag_service.IGetAllPreviousTagsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetAllPreviousTagsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetAllPreviousTagsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): tag_service.GetAllPreviousTagsResponse;

        /**
         * Decodes a GetAllPreviousTagsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetAllPreviousTagsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): tag_service.GetAllPreviousTagsResponse;

        /**
         * Verifies a GetAllPreviousTagsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetAllPreviousTagsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetAllPreviousTagsResponse
         */
        public static fromObject(object: { [k: string]: any }): tag_service.GetAllPreviousTagsResponse;

        /**
         * Creates a plain object from a GetAllPreviousTagsResponse message. Also converts values to other types if specified.
         * @param message GetAllPreviousTagsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: tag_service.GetAllPreviousTagsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetAllPreviousTagsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetAllPreviousTagsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace task_service. */
export namespace task_service {

    /** Represents a TaskService */
    class TaskService extends $protobuf.rpc.Service {

        /**
         * Constructs a new TaskService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new TaskService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): TaskService;

        /**
         * Calls GetTaskQueuesStatus.
         * @param request GetTaskQueuesStatusRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetTaskQueuesStatusResponse
         */
        public getTaskQueuesStatus(request: task_service.IGetTaskQueuesStatusRequest, callback: task_service.TaskService.GetTaskQueuesStatusCallback): void;

        /**
         * Calls GetTaskQueuesStatus.
         * @param request GetTaskQueuesStatusRequest message or plain object
         * @returns Promise
         */
        public getTaskQueuesStatus(request: task_service.IGetTaskQueuesStatusRequest): Promise<task_service.GetTaskQueuesStatusResponse>;

        /**
         * Calls ScanForTasks.
         * @param request ScanForTasksRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and ScanForTasksResponse
         */
        public scanForTasks(request: task_service.IScanForTasksRequest, callback: task_service.TaskService.ScanForTasksCallback): void;

        /**
         * Calls ScanForTasks.
         * @param request ScanForTasksRequest message or plain object
         * @returns Promise
         */
        public scanForTasks(request: task_service.IScanForTasksRequest): Promise<task_service.ScanForTasksResponse>;

        /**
         * Calls ResetTaskQueues.
         * @param request ResetTaskQueuesRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and ResetTaskQueuesResponse
         */
        public resetTaskQueues(request: task_service.IResetTaskQueuesRequest, callback: task_service.TaskService.ResetTaskQueuesCallback): void;

        /**
         * Calls ResetTaskQueues.
         * @param request ResetTaskQueuesRequest message or plain object
         * @returns Promise
         */
        public resetTaskQueues(request: task_service.IResetTaskQueuesRequest): Promise<task_service.ResetTaskQueuesResponse>;
    }

    namespace TaskService {

        /**
         * Callback as used by {@link task_service.TaskService#getTaskQueuesStatus}.
         * @param error Error, if any
         * @param [response] GetTaskQueuesStatusResponse
         */
        type GetTaskQueuesStatusCallback = (error: (Error|null), response?: task_service.GetTaskQueuesStatusResponse) => void;

        /**
         * Callback as used by {@link task_service.TaskService#scanForTasks}.
         * @param error Error, if any
         * @param [response] ScanForTasksResponse
         */
        type ScanForTasksCallback = (error: (Error|null), response?: task_service.ScanForTasksResponse) => void;

        /**
         * Callback as used by {@link task_service.TaskService#resetTaskQueues}.
         * @param error Error, if any
         * @param [response] ResetTaskQueuesResponse
         */
        type ResetTaskQueuesCallback = (error: (Error|null), response?: task_service.ResetTaskQueuesResponse) => void;
    }

    /** Properties of a GetTaskQueuesStatusRequest. */
    interface IGetTaskQueuesStatusRequest {
    }

    /** Represents a GetTaskQueuesStatusRequest. */
    class GetTaskQueuesStatusRequest implements IGetTaskQueuesStatusRequest {

        /**
         * Constructs a new GetTaskQueuesStatusRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.IGetTaskQueuesStatusRequest);

        /**
         * Creates a new GetTaskQueuesStatusRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetTaskQueuesStatusRequest instance
         */
        public static create(properties?: task_service.IGetTaskQueuesStatusRequest): task_service.GetTaskQueuesStatusRequest;

        /**
         * Encodes the specified GetTaskQueuesStatusRequest message. Does not implicitly {@link task_service.GetTaskQueuesStatusRequest.verify|verify} messages.
         * @param message GetTaskQueuesStatusRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.IGetTaskQueuesStatusRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetTaskQueuesStatusRequest message, length delimited. Does not implicitly {@link task_service.GetTaskQueuesStatusRequest.verify|verify} messages.
         * @param message GetTaskQueuesStatusRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.IGetTaskQueuesStatusRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetTaskQueuesStatusRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetTaskQueuesStatusRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.GetTaskQueuesStatusRequest;

        /**
         * Decodes a GetTaskQueuesStatusRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetTaskQueuesStatusRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.GetTaskQueuesStatusRequest;

        /**
         * Verifies a GetTaskQueuesStatusRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetTaskQueuesStatusRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetTaskQueuesStatusRequest
         */
        public static fromObject(object: { [k: string]: any }): task_service.GetTaskQueuesStatusRequest;

        /**
         * Creates a plain object from a GetTaskQueuesStatusRequest message. Also converts values to other types if specified.
         * @param message GetTaskQueuesStatusRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.GetTaskQueuesStatusRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetTaskQueuesStatusRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetTaskQueuesStatusRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetTaskQueuesStatusResponse. */
    interface IGetTaskQueuesStatusResponse {

        /** GetTaskQueuesStatusResponse taskQueueStatuses */
        taskQueueStatuses?: (task_service.ITaskQueueStatus[]|null);
    }

    /** Represents a GetTaskQueuesStatusResponse. */
    class GetTaskQueuesStatusResponse implements IGetTaskQueuesStatusResponse {

        /**
         * Constructs a new GetTaskQueuesStatusResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.IGetTaskQueuesStatusResponse);

        /** GetTaskQueuesStatusResponse taskQueueStatuses. */
        public taskQueueStatuses: task_service.ITaskQueueStatus[];

        /**
         * Creates a new GetTaskQueuesStatusResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetTaskQueuesStatusResponse instance
         */
        public static create(properties?: task_service.IGetTaskQueuesStatusResponse): task_service.GetTaskQueuesStatusResponse;

        /**
         * Encodes the specified GetTaskQueuesStatusResponse message. Does not implicitly {@link task_service.GetTaskQueuesStatusResponse.verify|verify} messages.
         * @param message GetTaskQueuesStatusResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.IGetTaskQueuesStatusResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetTaskQueuesStatusResponse message, length delimited. Does not implicitly {@link task_service.GetTaskQueuesStatusResponse.verify|verify} messages.
         * @param message GetTaskQueuesStatusResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.IGetTaskQueuesStatusResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetTaskQueuesStatusResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetTaskQueuesStatusResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.GetTaskQueuesStatusResponse;

        /**
         * Decodes a GetTaskQueuesStatusResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetTaskQueuesStatusResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.GetTaskQueuesStatusResponse;

        /**
         * Verifies a GetTaskQueuesStatusResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetTaskQueuesStatusResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetTaskQueuesStatusResponse
         */
        public static fromObject(object: { [k: string]: any }): task_service.GetTaskQueuesStatusResponse;

        /**
         * Creates a plain object from a GetTaskQueuesStatusResponse message. Also converts values to other types if specified.
         * @param message GetTaskQueuesStatusResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.GetTaskQueuesStatusResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetTaskQueuesStatusResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetTaskQueuesStatusResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ScanForTasksRequest. */
    interface IScanForTasksRequest {

        /** ScanForTasksRequest name */
        name?: (string|null);
    }

    /** Represents a ScanForTasksRequest. */
    class ScanForTasksRequest implements IScanForTasksRequest {

        /**
         * Constructs a new ScanForTasksRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.IScanForTasksRequest);

        /** ScanForTasksRequest name. */
        public name?: (string|null);

        /** ScanForTasksRequest _name. */
        public _name?: "name";

        /**
         * Creates a new ScanForTasksRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ScanForTasksRequest instance
         */
        public static create(properties?: task_service.IScanForTasksRequest): task_service.ScanForTasksRequest;

        /**
         * Encodes the specified ScanForTasksRequest message. Does not implicitly {@link task_service.ScanForTasksRequest.verify|verify} messages.
         * @param message ScanForTasksRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.IScanForTasksRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ScanForTasksRequest message, length delimited. Does not implicitly {@link task_service.ScanForTasksRequest.verify|verify} messages.
         * @param message ScanForTasksRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.IScanForTasksRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ScanForTasksRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ScanForTasksRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.ScanForTasksRequest;

        /**
         * Decodes a ScanForTasksRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ScanForTasksRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.ScanForTasksRequest;

        /**
         * Verifies a ScanForTasksRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ScanForTasksRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ScanForTasksRequest
         */
        public static fromObject(object: { [k: string]: any }): task_service.ScanForTasksRequest;

        /**
         * Creates a plain object from a ScanForTasksRequest message. Also converts values to other types if specified.
         * @param message ScanForTasksRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.ScanForTasksRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ScanForTasksRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ScanForTasksRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ScanForTasksResponse. */
    interface IScanForTasksResponse {
    }

    /** Represents a ScanForTasksResponse. */
    class ScanForTasksResponse implements IScanForTasksResponse {

        /**
         * Constructs a new ScanForTasksResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.IScanForTasksResponse);

        /**
         * Creates a new ScanForTasksResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ScanForTasksResponse instance
         */
        public static create(properties?: task_service.IScanForTasksResponse): task_service.ScanForTasksResponse;

        /**
         * Encodes the specified ScanForTasksResponse message. Does not implicitly {@link task_service.ScanForTasksResponse.verify|verify} messages.
         * @param message ScanForTasksResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.IScanForTasksResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ScanForTasksResponse message, length delimited. Does not implicitly {@link task_service.ScanForTasksResponse.verify|verify} messages.
         * @param message ScanForTasksResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.IScanForTasksResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ScanForTasksResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ScanForTasksResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.ScanForTasksResponse;

        /**
         * Decodes a ScanForTasksResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ScanForTasksResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.ScanForTasksResponse;

        /**
         * Verifies a ScanForTasksResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ScanForTasksResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ScanForTasksResponse
         */
        public static fromObject(object: { [k: string]: any }): task_service.ScanForTasksResponse;

        /**
         * Creates a plain object from a ScanForTasksResponse message. Also converts values to other types if specified.
         * @param message ScanForTasksResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.ScanForTasksResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ScanForTasksResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ScanForTasksResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a TaskQueueStatus. */
    interface ITaskQueueStatus {

        /** TaskQueueStatus name */
        name?: (string|null);

        /** TaskQueueStatus processingTasks */
        processingTasks?: (number|null);

        /** TaskQueueStatus pendingTasks */
        pendingTasks?: (number|null);

        /** TaskQueueStatus processedTasks */
        processedTasks?: (number|null);

        /** TaskQueueStatus skippedTasks */
        skippedTasks?: (number|null);

        /** TaskQueueStatus submittedTasks */
        submittedTasks?: (number|null);

        /** TaskQueueStatus retries */
        retries?: (number|null);

        /** TaskQueueStatus failures */
        failures?: (number|null);

        /** TaskQueueStatus errors */
        errors?: (number|null);

        /** TaskQueueStatus lastFailure */
        lastFailure?: (string|null);

        /** TaskQueueStatus processingTimeMs */
        processingTimeMs?: (Long|null);

        /** TaskQueueStatus failedProcessingTimeMs */
        failedProcessingTimeMs?: (Long|null);
    }

    /** Represents a TaskQueueStatus. */
    class TaskQueueStatus implements ITaskQueueStatus {

        /**
         * Constructs a new TaskQueueStatus.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.ITaskQueueStatus);

        /** TaskQueueStatus name. */
        public name?: (string|null);

        /** TaskQueueStatus processingTasks. */
        public processingTasks?: (number|null);

        /** TaskQueueStatus pendingTasks. */
        public pendingTasks?: (number|null);

        /** TaskQueueStatus processedTasks. */
        public processedTasks?: (number|null);

        /** TaskQueueStatus skippedTasks. */
        public skippedTasks?: (number|null);

        /** TaskQueueStatus submittedTasks. */
        public submittedTasks?: (number|null);

        /** TaskQueueStatus retries. */
        public retries?: (number|null);

        /** TaskQueueStatus failures. */
        public failures?: (number|null);

        /** TaskQueueStatus errors. */
        public errors?: (number|null);

        /** TaskQueueStatus lastFailure. */
        public lastFailure?: (string|null);

        /** TaskQueueStatus processingTimeMs. */
        public processingTimeMs?: (Long|null);

        /** TaskQueueStatus failedProcessingTimeMs. */
        public failedProcessingTimeMs?: (Long|null);

        /** TaskQueueStatus _name. */
        public _name?: "name";

        /** TaskQueueStatus _processingTasks. */
        public _processingTasks?: "processingTasks";

        /** TaskQueueStatus _pendingTasks. */
        public _pendingTasks?: "pendingTasks";

        /** TaskQueueStatus _processedTasks. */
        public _processedTasks?: "processedTasks";

        /** TaskQueueStatus _skippedTasks. */
        public _skippedTasks?: "skippedTasks";

        /** TaskQueueStatus _submittedTasks. */
        public _submittedTasks?: "submittedTasks";

        /** TaskQueueStatus _retries. */
        public _retries?: "retries";

        /** TaskQueueStatus _failures. */
        public _failures?: "failures";

        /** TaskQueueStatus _errors. */
        public _errors?: "errors";

        /** TaskQueueStatus _lastFailure. */
        public _lastFailure?: "lastFailure";

        /** TaskQueueStatus _processingTimeMs. */
        public _processingTimeMs?: "processingTimeMs";

        /** TaskQueueStatus _failedProcessingTimeMs. */
        public _failedProcessingTimeMs?: "failedProcessingTimeMs";

        /**
         * Creates a new TaskQueueStatus instance using the specified properties.
         * @param [properties] Properties to set
         * @returns TaskQueueStatus instance
         */
        public static create(properties?: task_service.ITaskQueueStatus): task_service.TaskQueueStatus;

        /**
         * Encodes the specified TaskQueueStatus message. Does not implicitly {@link task_service.TaskQueueStatus.verify|verify} messages.
         * @param message TaskQueueStatus message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.ITaskQueueStatus, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified TaskQueueStatus message, length delimited. Does not implicitly {@link task_service.TaskQueueStatus.verify|verify} messages.
         * @param message TaskQueueStatus message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.ITaskQueueStatus, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a TaskQueueStatus message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns TaskQueueStatus
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.TaskQueueStatus;

        /**
         * Decodes a TaskQueueStatus message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns TaskQueueStatus
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.TaskQueueStatus;

        /**
         * Verifies a TaskQueueStatus message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a TaskQueueStatus message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns TaskQueueStatus
         */
        public static fromObject(object: { [k: string]: any }): task_service.TaskQueueStatus;

        /**
         * Creates a plain object from a TaskQueueStatus message. Also converts values to other types if specified.
         * @param message TaskQueueStatus
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.TaskQueueStatus, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this TaskQueueStatus to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for TaskQueueStatus
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ResetTaskQueuesRequest. */
    interface IResetTaskQueuesRequest {

        /** ResetTaskQueuesRequest name */
        name?: (string|null);
    }

    /** Represents a ResetTaskQueuesRequest. */
    class ResetTaskQueuesRequest implements IResetTaskQueuesRequest {

        /**
         * Constructs a new ResetTaskQueuesRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.IResetTaskQueuesRequest);

        /** ResetTaskQueuesRequest name. */
        public name?: (string|null);

        /** ResetTaskQueuesRequest _name. */
        public _name?: "name";

        /**
         * Creates a new ResetTaskQueuesRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ResetTaskQueuesRequest instance
         */
        public static create(properties?: task_service.IResetTaskQueuesRequest): task_service.ResetTaskQueuesRequest;

        /**
         * Encodes the specified ResetTaskQueuesRequest message. Does not implicitly {@link task_service.ResetTaskQueuesRequest.verify|verify} messages.
         * @param message ResetTaskQueuesRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.IResetTaskQueuesRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ResetTaskQueuesRequest message, length delimited. Does not implicitly {@link task_service.ResetTaskQueuesRequest.verify|verify} messages.
         * @param message ResetTaskQueuesRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.IResetTaskQueuesRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ResetTaskQueuesRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ResetTaskQueuesRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.ResetTaskQueuesRequest;

        /**
         * Decodes a ResetTaskQueuesRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ResetTaskQueuesRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.ResetTaskQueuesRequest;

        /**
         * Verifies a ResetTaskQueuesRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ResetTaskQueuesRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ResetTaskQueuesRequest
         */
        public static fromObject(object: { [k: string]: any }): task_service.ResetTaskQueuesRequest;

        /**
         * Creates a plain object from a ResetTaskQueuesRequest message. Also converts values to other types if specified.
         * @param message ResetTaskQueuesRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.ResetTaskQueuesRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ResetTaskQueuesRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ResetTaskQueuesRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ResetTaskQueuesResponse. */
    interface IResetTaskQueuesResponse {
    }

    /** Represents a ResetTaskQueuesResponse. */
    class ResetTaskQueuesResponse implements IResetTaskQueuesResponse {

        /**
         * Constructs a new ResetTaskQueuesResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.IResetTaskQueuesResponse);

        /**
         * Creates a new ResetTaskQueuesResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ResetTaskQueuesResponse instance
         */
        public static create(properties?: task_service.IResetTaskQueuesResponse): task_service.ResetTaskQueuesResponse;

        /**
         * Encodes the specified ResetTaskQueuesResponse message. Does not implicitly {@link task_service.ResetTaskQueuesResponse.verify|verify} messages.
         * @param message ResetTaskQueuesResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.IResetTaskQueuesResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ResetTaskQueuesResponse message, length delimited. Does not implicitly {@link task_service.ResetTaskQueuesResponse.verify|verify} messages.
         * @param message ResetTaskQueuesResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.IResetTaskQueuesResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ResetTaskQueuesResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ResetTaskQueuesResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.ResetTaskQueuesResponse;

        /**
         * Decodes a ResetTaskQueuesResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ResetTaskQueuesResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.ResetTaskQueuesResponse;

        /**
         * Verifies a ResetTaskQueuesResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ResetTaskQueuesResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ResetTaskQueuesResponse
         */
        public static fromObject(object: { [k: string]: any }): task_service.ResetTaskQueuesResponse;

        /**
         * Creates a plain object from a ResetTaskQueuesResponse message. Also converts values to other types if specified.
         * @param message ResetTaskQueuesResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.ResetTaskQueuesResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ResetTaskQueuesResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ResetTaskQueuesResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a ReplyToPostTask. */
    interface IReplyToPostTask {

        /** ReplyToPostTask projectPostId */
        projectPostId?: (number|null);
    }

    /** Represents a ReplyToPostTask. */
    class ReplyToPostTask implements IReplyToPostTask {

        /**
         * Constructs a new ReplyToPostTask.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.IReplyToPostTask);

        /** ReplyToPostTask projectPostId. */
        public projectPostId?: (number|null);

        /** ReplyToPostTask _projectPostId. */
        public _projectPostId?: "projectPostId";

        /**
         * Creates a new ReplyToPostTask instance using the specified properties.
         * @param [properties] Properties to set
         * @returns ReplyToPostTask instance
         */
        public static create(properties?: task_service.IReplyToPostTask): task_service.ReplyToPostTask;

        /**
         * Encodes the specified ReplyToPostTask message. Does not implicitly {@link task_service.ReplyToPostTask.verify|verify} messages.
         * @param message ReplyToPostTask message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.IReplyToPostTask, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified ReplyToPostTask message, length delimited. Does not implicitly {@link task_service.ReplyToPostTask.verify|verify} messages.
         * @param message ReplyToPostTask message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.IReplyToPostTask, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a ReplyToPostTask message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns ReplyToPostTask
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.ReplyToPostTask;

        /**
         * Decodes a ReplyToPostTask message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns ReplyToPostTask
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.ReplyToPostTask;

        /**
         * Verifies a ReplyToPostTask message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a ReplyToPostTask message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns ReplyToPostTask
         */
        public static fromObject(object: { [k: string]: any }): task_service.ReplyToPostTask;

        /**
         * Creates a plain object from a ReplyToPostTask message. Also converts values to other types if specified.
         * @param message ReplyToPostTask
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.ReplyToPostTask, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this ReplyToPostTask to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for ReplyToPostTask
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a FillInMissingProjectInfoTask. */
    interface IFillInMissingProjectInfoTask {

        /** FillInMissingProjectInfoTask projectId */
        projectId?: (number|null);
    }

    /** Represents a FillInMissingProjectInfoTask. */
    class FillInMissingProjectInfoTask implements IFillInMissingProjectInfoTask {

        /**
         * Constructs a new FillInMissingProjectInfoTask.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.IFillInMissingProjectInfoTask);

        /** FillInMissingProjectInfoTask projectId. */
        public projectId?: (number|null);

        /** FillInMissingProjectInfoTask _projectId. */
        public _projectId?: "projectId";

        /**
         * Creates a new FillInMissingProjectInfoTask instance using the specified properties.
         * @param [properties] Properties to set
         * @returns FillInMissingProjectInfoTask instance
         */
        public static create(properties?: task_service.IFillInMissingProjectInfoTask): task_service.FillInMissingProjectInfoTask;

        /**
         * Encodes the specified FillInMissingProjectInfoTask message. Does not implicitly {@link task_service.FillInMissingProjectInfoTask.verify|verify} messages.
         * @param message FillInMissingProjectInfoTask message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.IFillInMissingProjectInfoTask, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified FillInMissingProjectInfoTask message, length delimited. Does not implicitly {@link task_service.FillInMissingProjectInfoTask.verify|verify} messages.
         * @param message FillInMissingProjectInfoTask message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.IFillInMissingProjectInfoTask, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a FillInMissingProjectInfoTask message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns FillInMissingProjectInfoTask
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.FillInMissingProjectInfoTask;

        /**
         * Decodes a FillInMissingProjectInfoTask message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns FillInMissingProjectInfoTask
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.FillInMissingProjectInfoTask;

        /**
         * Verifies a FillInMissingProjectInfoTask message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a FillInMissingProjectInfoTask message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns FillInMissingProjectInfoTask
         */
        public static fromObject(object: { [k: string]: any }): task_service.FillInMissingProjectInfoTask;

        /**
         * Creates a plain object from a FillInMissingProjectInfoTask message. Also converts values to other types if specified.
         * @param message FillInMissingProjectInfoTask
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.FillInMissingProjectInfoTask, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this FillInMissingProjectInfoTask to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for FillInMissingProjectInfoTask
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GenerateProjectsTask. */
    interface IGenerateProjectsTask {

        /** GenerateProjectsTask projectInputId */
        projectInputId?: (number|null);
    }

    /** Represents a GenerateProjectsTask. */
    class GenerateProjectsTask implements IGenerateProjectsTask {

        /**
         * Constructs a new GenerateProjectsTask.
         * @param [properties] Properties to set
         */
        constructor(properties?: task_service.IGenerateProjectsTask);

        /** GenerateProjectsTask projectInputId. */
        public projectInputId?: (number|null);

        /** GenerateProjectsTask _projectInputId. */
        public _projectInputId?: "projectInputId";

        /**
         * Creates a new GenerateProjectsTask instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GenerateProjectsTask instance
         */
        public static create(properties?: task_service.IGenerateProjectsTask): task_service.GenerateProjectsTask;

        /**
         * Encodes the specified GenerateProjectsTask message. Does not implicitly {@link task_service.GenerateProjectsTask.verify|verify} messages.
         * @param message GenerateProjectsTask message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: task_service.IGenerateProjectsTask, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GenerateProjectsTask message, length delimited. Does not implicitly {@link task_service.GenerateProjectsTask.verify|verify} messages.
         * @param message GenerateProjectsTask message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: task_service.IGenerateProjectsTask, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GenerateProjectsTask message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GenerateProjectsTask
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): task_service.GenerateProjectsTask;

        /**
         * Decodes a GenerateProjectsTask message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GenerateProjectsTask
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): task_service.GenerateProjectsTask;

        /**
         * Verifies a GenerateProjectsTask message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GenerateProjectsTask message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GenerateProjectsTask
         */
        public static fromObject(object: { [k: string]: any }): task_service.GenerateProjectsTask;

        /**
         * Creates a plain object from a GenerateProjectsTask message. Also converts values to other types if specified.
         * @param message GenerateProjectsTask
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: task_service.GenerateProjectsTask, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GenerateProjectsTask to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GenerateProjectsTask
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}

/** Namespace user_x_management. */
export namespace user_x_management {

    /** Represents a UserXManagementService */
    class UserXManagementService extends $protobuf.rpc.Service {

        /**
         * Constructs a new UserXManagementService service.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         */
        constructor(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean);

        /**
         * Creates new UserXManagementService service using the specified rpc implementation.
         * @param rpcImpl RPC implementation
         * @param [requestDelimited=false] Whether requests are length-delimited
         * @param [responseDelimited=false] Whether responses are length-delimited
         * @returns RPC service. Useful where requests and/or responses are streamed.
         */
        public static create(rpcImpl: $protobuf.RPCImpl, requestDelimited?: boolean, responseDelimited?: boolean): UserXManagementService;

        /**
         * Calls UpsertUserX.
         * @param request UpsertUserXRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and UpsertUserXResponse
         */
        public upsertUserX(request: user_x_management.IUpsertUserXRequest, callback: user_x_management.UserXManagementService.UpsertUserXCallback): void;

        /**
         * Calls UpsertUserX.
         * @param request UpsertUserXRequest message or plain object
         * @returns Promise
         */
        public upsertUserX(request: user_x_management.IUpsertUserXRequest): Promise<user_x_management.UpsertUserXResponse>;

        /**
         * Calls RegisterUserX.
         * @param request RegisterUserXRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and RegisterUserXResponse
         */
        public registerUserX(request: user_x_management.IRegisterUserXRequest, callback: user_x_management.UserXManagementService.RegisterUserXCallback): void;

        /**
         * Calls RegisterUserX.
         * @param request RegisterUserXRequest message or plain object
         * @returns Promise
         */
        public registerUserX(request: user_x_management.IRegisterUserXRequest): Promise<user_x_management.RegisterUserXResponse>;

        /**
         * Calls RemoveUserX.
         * @param request RemoveUserXRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and RemoveUserXResponse
         */
        public removeUserX(request: user_x_management.IRemoveUserXRequest, callback: user_x_management.UserXManagementService.RemoveUserXCallback): void;

        /**
         * Calls RemoveUserX.
         * @param request RemoveUserXRequest message or plain object
         * @returns Promise
         */
        public removeUserX(request: user_x_management.IRemoveUserXRequest): Promise<user_x_management.RemoveUserXResponse>;

        /**
         * Calls GetUserXs.
         * @param request GetUserXsRequest message or plain object
         * @param callback Node-style callback called with the error, if any, and GetUserXsResponse
         */
        public getUserXs(request: user_x_management.IGetUserXsRequest, callback: user_x_management.UserXManagementService.GetUserXsCallback): void;

        /**
         * Calls GetUserXs.
         * @param request GetUserXsRequest message or plain object
         * @returns Promise
         */
        public getUserXs(request: user_x_management.IGetUserXsRequest): Promise<user_x_management.GetUserXsResponse>;
    }

    namespace UserXManagementService {

        /**
         * Callback as used by {@link user_x_management.UserXManagementService#upsertUserX}.
         * @param error Error, if any
         * @param [response] UpsertUserXResponse
         */
        type UpsertUserXCallback = (error: (Error|null), response?: user_x_management.UpsertUserXResponse) => void;

        /**
         * Callback as used by {@link user_x_management.UserXManagementService#registerUserX}.
         * @param error Error, if any
         * @param [response] RegisterUserXResponse
         */
        type RegisterUserXCallback = (error: (Error|null), response?: user_x_management.RegisterUserXResponse) => void;

        /**
         * Callback as used by {@link user_x_management.UserXManagementService#removeUserX}.
         * @param error Error, if any
         * @param [response] RemoveUserXResponse
         */
        type RemoveUserXCallback = (error: (Error|null), response?: user_x_management.RemoveUserXResponse) => void;

        /**
         * Callback as used by {@link user_x_management.UserXManagementService#getUserXs}.
         * @param error Error, if any
         * @param [response] GetUserXsResponse
         */
        type GetUserXsCallback = (error: (Error|null), response?: user_x_management.GetUserXsResponse) => void;
    }

    /** Properties of a FullUserXDetails. */
    interface IFullUserXDetails {

        /** FullUserXDetails userX */
        userX?: (pl_types.IUserX|null);

        /** FullUserXDetails district */
        district?: (pl_types.IDistrict|null);

        /** FullUserXDetails schools */
        schools?: (pl_types.ISchool[]|null);

        /** FullUserXDetails classXs */
        classXs?: (pl_types.IClassX[]|null);

        /** FullUserXDetails districtStudentId */
        districtStudentId?: (number|null);

        /** FullUserXDetails studentGrade */
        studentGrade?: (number|null);
    }

    /** Represents a FullUserXDetails. */
    class FullUserXDetails implements IFullUserXDetails {

        /**
         * Constructs a new FullUserXDetails.
         * @param [properties] Properties to set
         */
        constructor(properties?: user_x_management.IFullUserXDetails);

        /** FullUserXDetails userX. */
        public userX?: (pl_types.IUserX|null);

        /** FullUserXDetails district. */
        public district?: (pl_types.IDistrict|null);

        /** FullUserXDetails schools. */
        public schools: pl_types.ISchool[];

        /** FullUserXDetails classXs. */
        public classXs: pl_types.IClassX[];

        /** FullUserXDetails districtStudentId. */
        public districtStudentId?: (number|null);

        /** FullUserXDetails studentGrade. */
        public studentGrade?: (number|null);

        /** FullUserXDetails _userX. */
        public _userX?: "userX";

        /** FullUserXDetails _district. */
        public _district?: "district";

        /** FullUserXDetails _districtStudentId. */
        public _districtStudentId?: "districtStudentId";

        /** FullUserXDetails _studentGrade. */
        public _studentGrade?: "studentGrade";

        /**
         * Creates a new FullUserXDetails instance using the specified properties.
         * @param [properties] Properties to set
         * @returns FullUserXDetails instance
         */
        public static create(properties?: user_x_management.IFullUserXDetails): user_x_management.FullUserXDetails;

        /**
         * Encodes the specified FullUserXDetails message. Does not implicitly {@link user_x_management.FullUserXDetails.verify|verify} messages.
         * @param message FullUserXDetails message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: user_x_management.IFullUserXDetails, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified FullUserXDetails message, length delimited. Does not implicitly {@link user_x_management.FullUserXDetails.verify|verify} messages.
         * @param message FullUserXDetails message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: user_x_management.IFullUserXDetails, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a FullUserXDetails message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns FullUserXDetails
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): user_x_management.FullUserXDetails;

        /**
         * Decodes a FullUserXDetails message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns FullUserXDetails
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): user_x_management.FullUserXDetails;

        /**
         * Verifies a FullUserXDetails message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a FullUserXDetails message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns FullUserXDetails
         */
        public static fromObject(object: { [k: string]: any }): user_x_management.FullUserXDetails;

        /**
         * Creates a plain object from a FullUserXDetails message. Also converts values to other types if specified.
         * @param message FullUserXDetails
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: user_x_management.FullUserXDetails, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this FullUserXDetails to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for FullUserXDetails
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertUserXRequest. */
    interface IUpsertUserXRequest {

        /** UpsertUserXRequest userX */
        userX?: (user_x_management.IFullUserXDetails|null);

        /** UpsertUserXRequest currentPassword */
        currentPassword?: (string|null);

        /** UpsertUserXRequest newPassword */
        newPassword?: (string|null);

        /** UpsertUserXRequest verifyPassword */
        verifyPassword?: (string|null);
    }

    /** Represents an UpsertUserXRequest. */
    class UpsertUserXRequest implements IUpsertUserXRequest {

        /**
         * Constructs a new UpsertUserXRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: user_x_management.IUpsertUserXRequest);

        /** UpsertUserXRequest userX. */
        public userX?: (user_x_management.IFullUserXDetails|null);

        /** UpsertUserXRequest currentPassword. */
        public currentPassword?: (string|null);

        /** UpsertUserXRequest newPassword. */
        public newPassword?: (string|null);

        /** UpsertUserXRequest verifyPassword. */
        public verifyPassword?: (string|null);

        /** UpsertUserXRequest _userX. */
        public _userX?: "userX";

        /** UpsertUserXRequest _currentPassword. */
        public _currentPassword?: "currentPassword";

        /** UpsertUserXRequest _newPassword. */
        public _newPassword?: "newPassword";

        /** UpsertUserXRequest _verifyPassword. */
        public _verifyPassword?: "verifyPassword";

        /**
         * Creates a new UpsertUserXRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertUserXRequest instance
         */
        public static create(properties?: user_x_management.IUpsertUserXRequest): user_x_management.UpsertUserXRequest;

        /**
         * Encodes the specified UpsertUserXRequest message. Does not implicitly {@link user_x_management.UpsertUserXRequest.verify|verify} messages.
         * @param message UpsertUserXRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: user_x_management.IUpsertUserXRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertUserXRequest message, length delimited. Does not implicitly {@link user_x_management.UpsertUserXRequest.verify|verify} messages.
         * @param message UpsertUserXRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: user_x_management.IUpsertUserXRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertUserXRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertUserXRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): user_x_management.UpsertUserXRequest;

        /**
         * Decodes an UpsertUserXRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertUserXRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): user_x_management.UpsertUserXRequest;

        /**
         * Verifies an UpsertUserXRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertUserXRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertUserXRequest
         */
        public static fromObject(object: { [k: string]: any }): user_x_management.UpsertUserXRequest;

        /**
         * Creates a plain object from an UpsertUserXRequest message. Also converts values to other types if specified.
         * @param message UpsertUserXRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: user_x_management.UpsertUserXRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertUserXRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertUserXRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of an UpsertUserXResponse. */
    interface IUpsertUserXResponse {

        /** UpsertUserXResponse userX */
        userX?: (user_x_management.IFullUserXDetails|null);

        /** UpsertUserXResponse error */
        error?: (string|null);
    }

    /** Represents an UpsertUserXResponse. */
    class UpsertUserXResponse implements IUpsertUserXResponse {

        /**
         * Constructs a new UpsertUserXResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: user_x_management.IUpsertUserXResponse);

        /** UpsertUserXResponse userX. */
        public userX?: (user_x_management.IFullUserXDetails|null);

        /** UpsertUserXResponse error. */
        public error?: (string|null);

        /** UpsertUserXResponse _userX. */
        public _userX?: "userX";

        /** UpsertUserXResponse _error. */
        public _error?: "error";

        /**
         * Creates a new UpsertUserXResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns UpsertUserXResponse instance
         */
        public static create(properties?: user_x_management.IUpsertUserXResponse): user_x_management.UpsertUserXResponse;

        /**
         * Encodes the specified UpsertUserXResponse message. Does not implicitly {@link user_x_management.UpsertUserXResponse.verify|verify} messages.
         * @param message UpsertUserXResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: user_x_management.IUpsertUserXResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified UpsertUserXResponse message, length delimited. Does not implicitly {@link user_x_management.UpsertUserXResponse.verify|verify} messages.
         * @param message UpsertUserXResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: user_x_management.IUpsertUserXResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes an UpsertUserXResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns UpsertUserXResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): user_x_management.UpsertUserXResponse;

        /**
         * Decodes an UpsertUserXResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns UpsertUserXResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): user_x_management.UpsertUserXResponse;

        /**
         * Verifies an UpsertUserXResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates an UpsertUserXResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns UpsertUserXResponse
         */
        public static fromObject(object: { [k: string]: any }): user_x_management.UpsertUserXResponse;

        /**
         * Creates a plain object from an UpsertUserXResponse message. Also converts values to other types if specified.
         * @param message UpsertUserXResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: user_x_management.UpsertUserXResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this UpsertUserXResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for UpsertUserXResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a RegisterUserXRequest. */
    interface IRegisterUserXRequest {

        /** RegisterUserXRequest firstName */
        firstName?: (string|null);

        /** RegisterUserXRequest lastName */
        lastName?: (string|null);

        /** RegisterUserXRequest emailAddress */
        emailAddress?: (string|null);

        /** RegisterUserXRequest password */
        password?: (string|null);

        /** RegisterUserXRequest verifyPassword */
        verifyPassword?: (string|null);

        /** RegisterUserXRequest profession */
        profession?: (string|null);

        /** RegisterUserXRequest reasonForInterest */
        reasonForInterest?: (string|null);

        /** RegisterUserXRequest districtName */
        districtName?: (string|null);

        /** RegisterUserXRequest schoolName */
        schoolName?: (string|null);

        /** RegisterUserXRequest addressLine_1 */
        addressLine_1?: (string|null);

        /** RegisterUserXRequest addressLine_2 */
        addressLine_2?: (string|null);

        /** RegisterUserXRequest city */
        city?: (string|null);

        /** RegisterUserXRequest state */
        state?: (string|null);

        /** RegisterUserXRequest zipCode */
        zipCode?: (string|null);

        /** RegisterUserXRequest numTeachers */
        numTeachers?: (number|null);

        /** RegisterUserXRequest numStudents */
        numStudents?: (number|null);
    }

    /** Represents a RegisterUserXRequest. */
    class RegisterUserXRequest implements IRegisterUserXRequest {

        /**
         * Constructs a new RegisterUserXRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: user_x_management.IRegisterUserXRequest);

        /** RegisterUserXRequest firstName. */
        public firstName?: (string|null);

        /** RegisterUserXRequest lastName. */
        public lastName?: (string|null);

        /** RegisterUserXRequest emailAddress. */
        public emailAddress?: (string|null);

        /** RegisterUserXRequest password. */
        public password?: (string|null);

        /** RegisterUserXRequest verifyPassword. */
        public verifyPassword?: (string|null);

        /** RegisterUserXRequest profession. */
        public profession?: (string|null);

        /** RegisterUserXRequest reasonForInterest. */
        public reasonForInterest?: (string|null);

        /** RegisterUserXRequest districtName. */
        public districtName?: (string|null);

        /** RegisterUserXRequest schoolName. */
        public schoolName?: (string|null);

        /** RegisterUserXRequest addressLine_1. */
        public addressLine_1?: (string|null);

        /** RegisterUserXRequest addressLine_2. */
        public addressLine_2?: (string|null);

        /** RegisterUserXRequest city. */
        public city?: (string|null);

        /** RegisterUserXRequest state. */
        public state?: (string|null);

        /** RegisterUserXRequest zipCode. */
        public zipCode?: (string|null);

        /** RegisterUserXRequest numTeachers. */
        public numTeachers?: (number|null);

        /** RegisterUserXRequest numStudents. */
        public numStudents?: (number|null);

        /** RegisterUserXRequest _firstName. */
        public _firstName?: "firstName";

        /** RegisterUserXRequest _lastName. */
        public _lastName?: "lastName";

        /** RegisterUserXRequest _emailAddress. */
        public _emailAddress?: "emailAddress";

        /** RegisterUserXRequest _password. */
        public _password?: "password";

        /** RegisterUserXRequest _verifyPassword. */
        public _verifyPassword?: "verifyPassword";

        /** RegisterUserXRequest _profession. */
        public _profession?: "profession";

        /** RegisterUserXRequest _reasonForInterest. */
        public _reasonForInterest?: "reasonForInterest";

        /** RegisterUserXRequest _districtName. */
        public _districtName?: "districtName";

        /** RegisterUserXRequest _schoolName. */
        public _schoolName?: "schoolName";

        /** RegisterUserXRequest _addressLine_1. */
        public _addressLine_1?: "addressLine_1";

        /** RegisterUserXRequest _addressLine_2. */
        public _addressLine_2?: "addressLine_2";

        /** RegisterUserXRequest _city. */
        public _city?: "city";

        /** RegisterUserXRequest _state. */
        public _state?: "state";

        /** RegisterUserXRequest _zipCode. */
        public _zipCode?: "zipCode";

        /** RegisterUserXRequest _numTeachers. */
        public _numTeachers?: "numTeachers";

        /** RegisterUserXRequest _numStudents. */
        public _numStudents?: "numStudents";

        /**
         * Creates a new RegisterUserXRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns RegisterUserXRequest instance
         */
        public static create(properties?: user_x_management.IRegisterUserXRequest): user_x_management.RegisterUserXRequest;

        /**
         * Encodes the specified RegisterUserXRequest message. Does not implicitly {@link user_x_management.RegisterUserXRequest.verify|verify} messages.
         * @param message RegisterUserXRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: user_x_management.IRegisterUserXRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified RegisterUserXRequest message, length delimited. Does not implicitly {@link user_x_management.RegisterUserXRequest.verify|verify} messages.
         * @param message RegisterUserXRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: user_x_management.IRegisterUserXRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a RegisterUserXRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns RegisterUserXRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): user_x_management.RegisterUserXRequest;

        /**
         * Decodes a RegisterUserXRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns RegisterUserXRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): user_x_management.RegisterUserXRequest;

        /**
         * Verifies a RegisterUserXRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a RegisterUserXRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns RegisterUserXRequest
         */
        public static fromObject(object: { [k: string]: any }): user_x_management.RegisterUserXRequest;

        /**
         * Creates a plain object from a RegisterUserXRequest message. Also converts values to other types if specified.
         * @param message RegisterUserXRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: user_x_management.RegisterUserXRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this RegisterUserXRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for RegisterUserXRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a RegisterUserXResponse. */
    interface IRegisterUserXResponse {

        /** RegisterUserXResponse accountAlreadyExists */
        accountAlreadyExists?: (boolean|null);
    }

    /** Represents a RegisterUserXResponse. */
    class RegisterUserXResponse implements IRegisterUserXResponse {

        /**
         * Constructs a new RegisterUserXResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: user_x_management.IRegisterUserXResponse);

        /** RegisterUserXResponse accountAlreadyExists. */
        public accountAlreadyExists?: (boolean|null);

        /** RegisterUserXResponse _accountAlreadyExists. */
        public _accountAlreadyExists?: "accountAlreadyExists";

        /**
         * Creates a new RegisterUserXResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns RegisterUserXResponse instance
         */
        public static create(properties?: user_x_management.IRegisterUserXResponse): user_x_management.RegisterUserXResponse;

        /**
         * Encodes the specified RegisterUserXResponse message. Does not implicitly {@link user_x_management.RegisterUserXResponse.verify|verify} messages.
         * @param message RegisterUserXResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: user_x_management.IRegisterUserXResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified RegisterUserXResponse message, length delimited. Does not implicitly {@link user_x_management.RegisterUserXResponse.verify|verify} messages.
         * @param message RegisterUserXResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: user_x_management.IRegisterUserXResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a RegisterUserXResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns RegisterUserXResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): user_x_management.RegisterUserXResponse;

        /**
         * Decodes a RegisterUserXResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns RegisterUserXResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): user_x_management.RegisterUserXResponse;

        /**
         * Verifies a RegisterUserXResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a RegisterUserXResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns RegisterUserXResponse
         */
        public static fromObject(object: { [k: string]: any }): user_x_management.RegisterUserXResponse;

        /**
         * Creates a plain object from a RegisterUserXResponse message. Also converts values to other types if specified.
         * @param message RegisterUserXResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: user_x_management.RegisterUserXResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this RegisterUserXResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for RegisterUserXResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a RemoveUserXRequest. */
    interface IRemoveUserXRequest {

        /** RemoveUserXRequest userXId */
        userXId?: (number|null);
    }

    /** Represents a RemoveUserXRequest. */
    class RemoveUserXRequest implements IRemoveUserXRequest {

        /**
         * Constructs a new RemoveUserXRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: user_x_management.IRemoveUserXRequest);

        /** RemoveUserXRequest userXId. */
        public userXId?: (number|null);

        /** RemoveUserXRequest _userXId. */
        public _userXId?: "userXId";

        /**
         * Creates a new RemoveUserXRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns RemoveUserXRequest instance
         */
        public static create(properties?: user_x_management.IRemoveUserXRequest): user_x_management.RemoveUserXRequest;

        /**
         * Encodes the specified RemoveUserXRequest message. Does not implicitly {@link user_x_management.RemoveUserXRequest.verify|verify} messages.
         * @param message RemoveUserXRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: user_x_management.IRemoveUserXRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified RemoveUserXRequest message, length delimited. Does not implicitly {@link user_x_management.RemoveUserXRequest.verify|verify} messages.
         * @param message RemoveUserXRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: user_x_management.IRemoveUserXRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a RemoveUserXRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns RemoveUserXRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): user_x_management.RemoveUserXRequest;

        /**
         * Decodes a RemoveUserXRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns RemoveUserXRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): user_x_management.RemoveUserXRequest;

        /**
         * Verifies a RemoveUserXRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a RemoveUserXRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns RemoveUserXRequest
         */
        public static fromObject(object: { [k: string]: any }): user_x_management.RemoveUserXRequest;

        /**
         * Creates a plain object from a RemoveUserXRequest message. Also converts values to other types if specified.
         * @param message RemoveUserXRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: user_x_management.RemoveUserXRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this RemoveUserXRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for RemoveUserXRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a RemoveUserXResponse. */
    interface IRemoveUserXResponse {
    }

    /** Represents a RemoveUserXResponse. */
    class RemoveUserXResponse implements IRemoveUserXResponse {

        /**
         * Constructs a new RemoveUserXResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: user_x_management.IRemoveUserXResponse);

        /**
         * Creates a new RemoveUserXResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns RemoveUserXResponse instance
         */
        public static create(properties?: user_x_management.IRemoveUserXResponse): user_x_management.RemoveUserXResponse;

        /**
         * Encodes the specified RemoveUserXResponse message. Does not implicitly {@link user_x_management.RemoveUserXResponse.verify|verify} messages.
         * @param message RemoveUserXResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: user_x_management.IRemoveUserXResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified RemoveUserXResponse message, length delimited. Does not implicitly {@link user_x_management.RemoveUserXResponse.verify|verify} messages.
         * @param message RemoveUserXResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: user_x_management.IRemoveUserXResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a RemoveUserXResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns RemoveUserXResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): user_x_management.RemoveUserXResponse;

        /**
         * Decodes a RemoveUserXResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns RemoveUserXResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): user_x_management.RemoveUserXResponse;

        /**
         * Verifies a RemoveUserXResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a RemoveUserXResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns RemoveUserXResponse
         */
        public static fromObject(object: { [k: string]: any }): user_x_management.RemoveUserXResponse;

        /**
         * Creates a plain object from a RemoveUserXResponse message. Also converts values to other types if specified.
         * @param message RemoveUserXResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: user_x_management.RemoveUserXResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this RemoveUserXResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for RemoveUserXResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetUserXsRequest. */
    interface IGetUserXsRequest {

        /** GetUserXsRequest includeSchools */
        includeSchools?: (boolean|null);

        /** GetUserXsRequest includeClassXs */
        includeClassXs?: (boolean|null);

        /** GetUserXsRequest ofSelf */
        ofSelf?: (boolean|null);

        /** GetUserXsRequest inDistrictIds */
        inDistrictIds?: (number[]|null);

        /** GetUserXsRequest inUserXIds */
        inUserXIds?: (number[]|null);

        /** GetUserXsRequest inSchoolIds */
        inSchoolIds?: (number[]|null);

        /** GetUserXsRequest inClassXIds */
        inClassXIds?: (number[]|null);

        /** GetUserXsRequest hasEmailAddress */
        hasEmailAddress?: (string|null);

        /** GetUserXsRequest adminXsOnly */
        adminXsOnly?: (boolean|null);

        /** GetUserXsRequest teachersOnly */
        teachersOnly?: (boolean|null);

        /** GetUserXsRequest studentsOnly */
        studentsOnly?: (boolean|null);

        /** GetUserXsRequest firstLastEmailSearchText */
        firstLastEmailSearchText?: (string|null);

        /** GetUserXsRequest page */
        page?: (number|null);

        /** GetUserXsRequest pageSize */
        pageSize?: (number|null);
    }

    /** Represents a GetUserXsRequest. */
    class GetUserXsRequest implements IGetUserXsRequest {

        /**
         * Constructs a new GetUserXsRequest.
         * @param [properties] Properties to set
         */
        constructor(properties?: user_x_management.IGetUserXsRequest);

        /** GetUserXsRequest includeSchools. */
        public includeSchools?: (boolean|null);

        /** GetUserXsRequest includeClassXs. */
        public includeClassXs?: (boolean|null);

        /** GetUserXsRequest ofSelf. */
        public ofSelf?: (boolean|null);

        /** GetUserXsRequest inDistrictIds. */
        public inDistrictIds: number[];

        /** GetUserXsRequest inUserXIds. */
        public inUserXIds: number[];

        /** GetUserXsRequest inSchoolIds. */
        public inSchoolIds: number[];

        /** GetUserXsRequest inClassXIds. */
        public inClassXIds: number[];

        /** GetUserXsRequest hasEmailAddress. */
        public hasEmailAddress?: (string|null);

        /** GetUserXsRequest adminXsOnly. */
        public adminXsOnly?: (boolean|null);

        /** GetUserXsRequest teachersOnly. */
        public teachersOnly?: (boolean|null);

        /** GetUserXsRequest studentsOnly. */
        public studentsOnly?: (boolean|null);

        /** GetUserXsRequest firstLastEmailSearchText. */
        public firstLastEmailSearchText?: (string|null);

        /** GetUserXsRequest page. */
        public page?: (number|null);

        /** GetUserXsRequest pageSize. */
        public pageSize?: (number|null);

        /** GetUserXsRequest _includeSchools. */
        public _includeSchools?: "includeSchools";

        /** GetUserXsRequest _includeClassXs. */
        public _includeClassXs?: "includeClassXs";

        /** GetUserXsRequest _ofSelf. */
        public _ofSelf?: "ofSelf";

        /** GetUserXsRequest _hasEmailAddress. */
        public _hasEmailAddress?: "hasEmailAddress";

        /** GetUserXsRequest _adminXsOnly. */
        public _adminXsOnly?: "adminXsOnly";

        /** GetUserXsRequest _teachersOnly. */
        public _teachersOnly?: "teachersOnly";

        /** GetUserXsRequest _studentsOnly. */
        public _studentsOnly?: "studentsOnly";

        /** GetUserXsRequest _firstLastEmailSearchText. */
        public _firstLastEmailSearchText?: "firstLastEmailSearchText";

        /** GetUserXsRequest _page. */
        public _page?: "page";

        /** GetUserXsRequest _pageSize. */
        public _pageSize?: "pageSize";

        /**
         * Creates a new GetUserXsRequest instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetUserXsRequest instance
         */
        public static create(properties?: user_x_management.IGetUserXsRequest): user_x_management.GetUserXsRequest;

        /**
         * Encodes the specified GetUserXsRequest message. Does not implicitly {@link user_x_management.GetUserXsRequest.verify|verify} messages.
         * @param message GetUserXsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: user_x_management.IGetUserXsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetUserXsRequest message, length delimited. Does not implicitly {@link user_x_management.GetUserXsRequest.verify|verify} messages.
         * @param message GetUserXsRequest message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: user_x_management.IGetUserXsRequest, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetUserXsRequest message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetUserXsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): user_x_management.GetUserXsRequest;

        /**
         * Decodes a GetUserXsRequest message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetUserXsRequest
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): user_x_management.GetUserXsRequest;

        /**
         * Verifies a GetUserXsRequest message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetUserXsRequest message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetUserXsRequest
         */
        public static fromObject(object: { [k: string]: any }): user_x_management.GetUserXsRequest;

        /**
         * Creates a plain object from a GetUserXsRequest message. Also converts values to other types if specified.
         * @param message GetUserXsRequest
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: user_x_management.GetUserXsRequest, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetUserXsRequest to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetUserXsRequest
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }

    /** Properties of a GetUserXsResponse. */
    interface IGetUserXsResponse {

        /** GetUserXsResponse userXs */
        userXs?: (user_x_management.IFullUserXDetails[]|null);

        /** GetUserXsResponse totalUserXs */
        totalUserXs?: (Long|null);
    }

    /** Represents a GetUserXsResponse. */
    class GetUserXsResponse implements IGetUserXsResponse {

        /**
         * Constructs a new GetUserXsResponse.
         * @param [properties] Properties to set
         */
        constructor(properties?: user_x_management.IGetUserXsResponse);

        /** GetUserXsResponse userXs. */
        public userXs: user_x_management.IFullUserXDetails[];

        /** GetUserXsResponse totalUserXs. */
        public totalUserXs?: (Long|null);

        /** GetUserXsResponse _totalUserXs. */
        public _totalUserXs?: "totalUserXs";

        /**
         * Creates a new GetUserXsResponse instance using the specified properties.
         * @param [properties] Properties to set
         * @returns GetUserXsResponse instance
         */
        public static create(properties?: user_x_management.IGetUserXsResponse): user_x_management.GetUserXsResponse;

        /**
         * Encodes the specified GetUserXsResponse message. Does not implicitly {@link user_x_management.GetUserXsResponse.verify|verify} messages.
         * @param message GetUserXsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: user_x_management.IGetUserXsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified GetUserXsResponse message, length delimited. Does not implicitly {@link user_x_management.GetUserXsResponse.verify|verify} messages.
         * @param message GetUserXsResponse message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: user_x_management.IGetUserXsResponse, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a GetUserXsResponse message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns GetUserXsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): user_x_management.GetUserXsResponse;

        /**
         * Decodes a GetUserXsResponse message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns GetUserXsResponse
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): user_x_management.GetUserXsResponse;

        /**
         * Verifies a GetUserXsResponse message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a GetUserXsResponse message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns GetUserXsResponse
         */
        public static fromObject(object: { [k: string]: any }): user_x_management.GetUserXsResponse;

        /**
         * Creates a plain object from a GetUserXsResponse message. Also converts values to other types if specified.
         * @param message GetUserXsResponse
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: user_x_management.GetUserXsResponse, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this GetUserXsResponse to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };

        /**
         * Gets the default type url for GetUserXsResponse
         * @param [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
         * @returns The default type url
         */
        public static getTypeUrl(typeUrlPrefix?: string): string;
    }
}
