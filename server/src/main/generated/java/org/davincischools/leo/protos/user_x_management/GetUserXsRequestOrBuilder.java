// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user_x_management.proto

package org.davincischools.leo.protos.user_x_management;

public interface GetUserXsRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:user_x_management.GetUserXsRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional bool include_schools = 7;</code>
   * @return Whether the includeSchools field is set.
   */
  boolean hasIncludeSchools();
  /**
   * <code>optional bool include_schools = 7;</code>
   * @return The includeSchools.
   */
  boolean getIncludeSchools();

  /**
   * <code>optional bool include_class_xs = 8;</code>
   * @return Whether the includeClassXs field is set.
   */
  boolean hasIncludeClassXs();
  /**
   * <code>optional bool include_class_xs = 8;</code>
   * @return The includeClassXs.
   */
  boolean getIncludeClassXs();

  /**
   * <code>optional bool of_self = 14;</code>
   * @return Whether the ofSelf field is set.
   */
  boolean hasOfSelf();
  /**
   * <code>optional bool of_self = 14;</code>
   * @return The ofSelf.
   */
  boolean getOfSelf();

  /**
   * <code>repeated int32 in_district_ids = 9;</code>
   * @return A list containing the inDistrictIds.
   */
  java.util.List<java.lang.Integer> getInDistrictIdsList();
  /**
   * <code>repeated int32 in_district_ids = 9;</code>
   * @return The count of inDistrictIds.
   */
  int getInDistrictIdsCount();
  /**
   * <code>repeated int32 in_district_ids = 9;</code>
   * @param index The index of the element to return.
   * @return The inDistrictIds at the given index.
   */
  int getInDistrictIds(int index);

  /**
   * <code>repeated int32 in_user_x_ids = 10;</code>
   * @return A list containing the inUserXIds.
   */
  java.util.List<java.lang.Integer> getInUserXIdsList();
  /**
   * <code>repeated int32 in_user_x_ids = 10;</code>
   * @return The count of inUserXIds.
   */
  int getInUserXIdsCount();
  /**
   * <code>repeated int32 in_user_x_ids = 10;</code>
   * @param index The index of the element to return.
   * @return The inUserXIds at the given index.
   */
  int getInUserXIds(int index);

  /**
   * <code>repeated int32 in_school_ids = 4;</code>
   * @return A list containing the inSchoolIds.
   */
  java.util.List<java.lang.Integer> getInSchoolIdsList();
  /**
   * <code>repeated int32 in_school_ids = 4;</code>
   * @return The count of inSchoolIds.
   */
  int getInSchoolIdsCount();
  /**
   * <code>repeated int32 in_school_ids = 4;</code>
   * @param index The index of the element to return.
   * @return The inSchoolIds at the given index.
   */
  int getInSchoolIds(int index);

  /**
   * <code>repeated int32 in_class_x_ids = 5;</code>
   * @return A list containing the inClassXIds.
   */
  java.util.List<java.lang.Integer> getInClassXIdsList();
  /**
   * <code>repeated int32 in_class_x_ids = 5;</code>
   * @return The count of inClassXIds.
   */
  int getInClassXIdsCount();
  /**
   * <code>repeated int32 in_class_x_ids = 5;</code>
   * @param index The index of the element to return.
   * @return The inClassXIds at the given index.
   */
  int getInClassXIds(int index);

  /**
   * <code>optional string has_email_address = 11;</code>
   * @return Whether the hasEmailAddress field is set.
   */
  boolean hasHasEmailAddress();
  /**
   * <code>optional string has_email_address = 11;</code>
   * @return The hasEmailAddress.
   */
  java.lang.String getHasEmailAddress();
  /**
   * <code>optional string has_email_address = 11;</code>
   * @return The bytes for hasEmailAddress.
   */
  com.google.protobuf.ByteString
      getHasEmailAddressBytes();

  /**
   * <code>optional bool admin_xs_only = 1;</code>
   * @return Whether the adminXsOnly field is set.
   */
  boolean hasAdminXsOnly();
  /**
   * <code>optional bool admin_xs_only = 1;</code>
   * @return The adminXsOnly.
   */
  boolean getAdminXsOnly();

  /**
   * <code>optional bool teachers_only = 2;</code>
   * @return Whether the teachersOnly field is set.
   */
  boolean hasTeachersOnly();
  /**
   * <code>optional bool teachers_only = 2;</code>
   * @return The teachersOnly.
   */
  boolean getTeachersOnly();

  /**
   * <code>optional bool students_only = 3;</code>
   * @return Whether the studentsOnly field is set.
   */
  boolean hasStudentsOnly();
  /**
   * <code>optional bool students_only = 3;</code>
   * @return The studentsOnly.
   */
  boolean getStudentsOnly();

  /**
   * <code>optional string first_last_email_search_text = 6;</code>
   * @return Whether the firstLastEmailSearchText field is set.
   */
  boolean hasFirstLastEmailSearchText();
  /**
   * <code>optional string first_last_email_search_text = 6;</code>
   * @return The firstLastEmailSearchText.
   */
  java.lang.String getFirstLastEmailSearchText();
  /**
   * <code>optional string first_last_email_search_text = 6;</code>
   * @return The bytes for firstLastEmailSearchText.
   */
  com.google.protobuf.ByteString
      getFirstLastEmailSearchTextBytes();

  /**
   * <code>optional int32 page = 12;</code>
   * @return Whether the page field is set.
   */
  boolean hasPage();
  /**
   * <code>optional int32 page = 12;</code>
   * @return The page.
   */
  int getPage();

  /**
   * <code>optional int32 page_size = 13;</code>
   * @return Whether the pageSize field is set.
   */
  boolean hasPageSize();
  /**
   * <code>optional int32 page_size = 13;</code>
   * @return The pageSize.
   */
  int getPageSize();
}
