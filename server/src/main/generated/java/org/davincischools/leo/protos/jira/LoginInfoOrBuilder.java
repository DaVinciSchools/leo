// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: jira.proto

package org.davincischools.leo.protos.jira;

public interface LoginInfoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:jira.LoginInfo)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional int32 failed_login_count = 1;</code>
   * @return Whether the failedLoginCount field is set.
   */
  boolean hasFailedLoginCount();
  /**
   * <code>optional int32 failed_login_count = 1;</code>
   * @return The failedLoginCount.
   */
  int getFailedLoginCount();

  /**
   * <code>optional int32 login_count = 2;</code>
   * @return Whether the loginCount field is set.
   */
  boolean hasLoginCount();
  /**
   * <code>optional int32 login_count = 2;</code>
   * @return The loginCount.
   */
  int getLoginCount();

  /**
   * <code>optional string last_failed_login_time = 3;</code>
   * @return Whether the lastFailedLoginTime field is set.
   */
  boolean hasLastFailedLoginTime();
  /**
   * <code>optional string last_failed_login_time = 3;</code>
   * @return The lastFailedLoginTime.
   */
  java.lang.String getLastFailedLoginTime();
  /**
   * <code>optional string last_failed_login_time = 3;</code>
   * @return The bytes for lastFailedLoginTime.
   */
  com.google.protobuf.ByteString
      getLastFailedLoginTimeBytes();

  /**
   * <code>optional string previous_login_time = 4;</code>
   * @return Whether the previousLoginTime field is set.
   */
  boolean hasPreviousLoginTime();
  /**
   * <code>optional string previous_login_time = 4;</code>
   * @return The previousLoginTime.
   */
  java.lang.String getPreviousLoginTime();
  /**
   * <code>optional string previous_login_time = 4;</code>
   * @return The bytes for previousLoginTime.
   */
  com.google.protobuf.ByteString
      getPreviousLoginTimeBytes();
}
