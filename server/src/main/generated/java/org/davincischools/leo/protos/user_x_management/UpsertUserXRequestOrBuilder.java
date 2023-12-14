// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user_x_management.proto

package org.davincischools.leo.protos.user_x_management;

public interface UpsertUserXRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:user_x_management.UpsertUserXRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional .user_x_management.FullUserXDetails user_x = 1;</code>
   * @return Whether the userX field is set.
   */
  boolean hasUserX();
  /**
   * <code>optional .user_x_management.FullUserXDetails user_x = 1;</code>
   * @return The userX.
   */
  org.davincischools.leo.protos.user_x_management.FullUserXDetails getUserX();
  /**
   * <code>optional .user_x_management.FullUserXDetails user_x = 1;</code>
   */
  org.davincischools.leo.protos.user_x_management.FullUserXDetailsOrBuilder getUserXOrBuilder();

  /**
   * <pre>
   * Only used when a user changes their password.
   * </pre>
   *
   * <code>optional string current_password = 2;</code>
   * @return Whether the currentPassword field is set.
   */
  boolean hasCurrentPassword();
  /**
   * <pre>
   * Only used when a user changes their password.
   * </pre>
   *
   * <code>optional string current_password = 2;</code>
   * @return The currentPassword.
   */
  java.lang.String getCurrentPassword();
  /**
   * <pre>
   * Only used when a user changes their password.
   * </pre>
   *
   * <code>optional string current_password = 2;</code>
   * @return The bytes for currentPassword.
   */
  com.google.protobuf.ByteString
      getCurrentPasswordBytes();

  /**
   * <code>optional string new_password = 3;</code>
   * @return Whether the newPassword field is set.
   */
  boolean hasNewPassword();
  /**
   * <code>optional string new_password = 3;</code>
   * @return The newPassword.
   */
  java.lang.String getNewPassword();
  /**
   * <code>optional string new_password = 3;</code>
   * @return The bytes for newPassword.
   */
  com.google.protobuf.ByteString
      getNewPasswordBytes();

  /**
   * <code>optional string verify_password = 4;</code>
   * @return Whether the verifyPassword field is set.
   */
  boolean hasVerifyPassword();
  /**
   * <code>optional string verify_password = 4;</code>
   * @return The verifyPassword.
   */
  java.lang.String getVerifyPassword();
  /**
   * <code>optional string verify_password = 4;</code>
   * @return The bytes for verifyPassword.
   */
  com.google.protobuf.ByteString
      getVerifyPasswordBytes();
}
