// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tag_service.proto

package org.davincischools.leo.protos.tag_service;

public interface GetAllPreviousTagsResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:tag_service.GetAllPreviousTagsResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .pl_types.Tag tags = 1;</code>
   */
  java.util.List<org.davincischools.leo.protos.pl_types.Tag> 
      getTagsList();
  /**
   * <code>repeated .pl_types.Tag tags = 1;</code>
   */
  org.davincischools.leo.protos.pl_types.Tag getTags(int index);
  /**
   * <code>repeated .pl_types.Tag tags = 1;</code>
   */
  int getTagsCount();
  /**
   * <code>repeated .pl_types.Tag tags = 1;</code>
   */
  java.util.List<? extends org.davincischools.leo.protos.pl_types.TagOrBuilder> 
      getTagsOrBuilderList();
  /**
   * <code>repeated .pl_types.Tag tags = 1;</code>
   */
  org.davincischools.leo.protos.pl_types.TagOrBuilder getTagsOrBuilder(
      int index);
}
