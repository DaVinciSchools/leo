// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mailjet.proto

package org.davincischools.leo.protos.mailjet;

public interface MailjetSendResponseErrorOrBuilder extends
    // @@protoc_insertion_point(interface_extends:mailjet.MailjetSendResponseError)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional string ErrorIdentifier = 1;</code>
   * @return Whether the errorIdentifier field is set.
   */
  boolean hasErrorIdentifier();
  /**
   * <code>optional string ErrorIdentifier = 1;</code>
   * @return The errorIdentifier.
   */
  java.lang.String getErrorIdentifier();
  /**
   * <code>optional string ErrorIdentifier = 1;</code>
   * @return The bytes for errorIdentifier.
   */
  com.google.protobuf.ByteString
      getErrorIdentifierBytes();

  /**
   * <code>optional string ErrorCode = 2;</code>
   * @return Whether the errorCode field is set.
   */
  boolean hasErrorCode();
  /**
   * <code>optional string ErrorCode = 2;</code>
   * @return The errorCode.
   */
  java.lang.String getErrorCode();
  /**
   * <code>optional string ErrorCode = 2;</code>
   * @return The bytes for errorCode.
   */
  com.google.protobuf.ByteString
      getErrorCodeBytes();

  /**
   * <code>optional int32 StatusCode = 3;</code>
   * @return Whether the statusCode field is set.
   */
  boolean hasStatusCode();
  /**
   * <code>optional int32 StatusCode = 3;</code>
   * @return The statusCode.
   */
  int getStatusCode();

  /**
   * <code>optional string ErrorMessage = 4;</code>
   * @return Whether the errorMessage field is set.
   */
  boolean hasErrorMessage();
  /**
   * <code>optional string ErrorMessage = 4;</code>
   * @return The errorMessage.
   */
  java.lang.String getErrorMessage();
  /**
   * <code>optional string ErrorMessage = 4;</code>
   * @return The bytes for errorMessage.
   */
  com.google.protobuf.ByteString
      getErrorMessageBytes();

  /**
   * <code>optional string ErrorRelatedTo = 5;</code>
   * @return Whether the errorRelatedTo field is set.
   */
  boolean hasErrorRelatedTo();
  /**
   * <code>optional string ErrorRelatedTo = 5;</code>
   * @return The errorRelatedTo.
   */
  java.lang.String getErrorRelatedTo();
  /**
   * <code>optional string ErrorRelatedTo = 5;</code>
   * @return The bytes for errorRelatedTo.
   */
  com.google.protobuf.ByteString
      getErrorRelatedToBytes();
}
