// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: project_management.proto

package org.davincischools.leo.protos.project_management;

public interface GetKnowledgeAndSkillsRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:project_management.GetKnowledgeAndSkillsRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .pl_types.KnowledgeAndSkill.Type types = 1;</code>
   * @return A list containing the types.
   */
  java.util.List<org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Type> getTypesList();
  /**
   * <code>repeated .pl_types.KnowledgeAndSkill.Type types = 1;</code>
   * @return The count of types.
   */
  int getTypesCount();
  /**
   * <code>repeated .pl_types.KnowledgeAndSkill.Type types = 1;</code>
   * @param index The index of the element to return.
   * @return The types at the given index.
   */
  org.davincischools.leo.protos.pl_types.KnowledgeAndSkill.Type getTypes(int index);
  /**
   * <code>repeated .pl_types.KnowledgeAndSkill.Type types = 1;</code>
   * @return A list containing the enum numeric values on the wire for types.
   */
  java.util.List<java.lang.Integer>
  getTypesValueList();
  /**
   * <code>repeated .pl_types.KnowledgeAndSkill.Type types = 1;</code>
   * @param index The index of the value to return.
   * @return The enum numeric value on the wire of types at the given index.
   */
  int getTypesValue(int index);
}
