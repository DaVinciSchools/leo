// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: pl_types.proto

package org.davincischools.leo.protos.pl_types;

public interface UserXOrBuilder extends
    // @@protoc_insertion_point(interface_extends:pl_types.UserX)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional int32 id = 1;</code>
   * @return Whether the id field is set.
   */
  boolean hasId();
  /**
   * <code>optional int32 id = 1;</code>
   * @return The id.
   */
  int getId();

  /**
   * <code>optional int32 district_id = 2;</code>
   * @return Whether the districtId field is set.
   */
  boolean hasDistrictId();
  /**
   * <code>optional int32 district_id = 2;</code>
   * @return The districtId.
   */
  int getDistrictId();

  /**
   * <code>optional string first_name = 3;</code>
   * @return Whether the firstName field is set.
   */
  boolean hasFirstName();
  /**
   * <code>optional string first_name = 3;</code>
   * @return The firstName.
   */
  java.lang.String getFirstName();
  /**
   * <code>optional string first_name = 3;</code>
   * @return The bytes for firstName.
   */
  com.google.protobuf.ByteString
      getFirstNameBytes();

  /**
   * <code>optional string last_name = 4;</code>
   * @return Whether the lastName field is set.
   */
  boolean hasLastName();
  /**
   * <code>optional string last_name = 4;</code>
   * @return The lastName.
   */
  java.lang.String getLastName();
  /**
   * <code>optional string last_name = 4;</code>
   * @return The bytes for lastName.
   */
  com.google.protobuf.ByteString
      getLastNameBytes();

  /**
   * <code>optional string email_address = 5;</code>
   * @return Whether the emailAddress field is set.
   */
  boolean hasEmailAddress();
  /**
   * <code>optional string email_address = 5;</code>
   * @return The emailAddress.
   */
  java.lang.String getEmailAddress();
  /**
   * <code>optional string email_address = 5;</code>
   * @return The bytes for emailAddress.
   */
  com.google.protobuf.ByteString
      getEmailAddressBytes();

  /**
   * <code>optional bool is_admin_x = 6;</code>
   * @return Whether the isAdminX field is set.
   */
  boolean hasIsAdminX();
  /**
   * <code>optional bool is_admin_x = 6;</code>
   * @return The isAdminX.
   */
  boolean getIsAdminX();

  /**
   * <code>optional int32 admin_x_id = 7;</code>
   * @return Whether the adminXId field is set.
   */
  boolean hasAdminXId();
  /**
   * <code>optional int32 admin_x_id = 7;</code>
   * @return The adminXId.
   */
  int getAdminXId();

  /**
   * <code>optional bool is_teacher = 8;</code>
   * @return Whether the isTeacher field is set.
   */
  boolean hasIsTeacher();
  /**
   * <code>optional bool is_teacher = 8;</code>
   * @return The isTeacher.
   */
  boolean getIsTeacher();

  /**
   * <code>optional int32 teacher_id = 9;</code>
   * @return Whether the teacherId field is set.
   */
  boolean hasTeacherId();
  /**
   * <code>optional int32 teacher_id = 9;</code>
   * @return The teacherId.
   */
  int getTeacherId();

  /**
   * <code>optional bool is_student = 10;</code>
   * @return Whether the isStudent field is set.
   */
  boolean hasIsStudent();
  /**
   * <code>optional bool is_student = 10;</code>
   * @return The isStudent.
   */
  boolean getIsStudent();

  /**
   * <code>optional int32 student_id = 11;</code>
   * @return Whether the studentId field is set.
   */
  boolean hasStudentId();
  /**
   * <code>optional int32 student_id = 11;</code>
   * @return The studentId.
   */
  int getStudentId();

  /**
   * <code>optional bool is_demo = 12;</code>
   * @return Whether the isDemo field is set.
   */
  boolean hasIsDemo();
  /**
   * <code>optional bool is_demo = 12;</code>
   * @return The isDemo.
   */
  boolean getIsDemo();

  /**
   * <code>optional bool is_authenticated = 13;</code>
   * @return Whether the isAuthenticated field is set.
   */
  boolean hasIsAuthenticated();
  /**
   * <code>optional bool is_authenticated = 13;</code>
   * @return The isAuthenticated.
   */
  boolean getIsAuthenticated();
}
