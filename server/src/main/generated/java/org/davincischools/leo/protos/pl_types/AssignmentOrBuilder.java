// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: pl_types.proto

package org.davincischools.leo.protos.pl_types;

public interface AssignmentOrBuilder extends
    // @@protoc_insertion_point(interface_extends:pl_types.Assignment)
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
   * <code>optional string name = 3;</code>
   * @return Whether the name field is set.
   */
  boolean hasName();
  /**
   * <code>optional string name = 3;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>optional string name = 3;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>optional string nickname = 6;</code>
   * @return Whether the nickname field is set.
   */
  boolean hasNickname();
  /**
   * <code>optional string nickname = 6;</code>
   * @return The nickname.
   */
  java.lang.String getNickname();
  /**
   * <code>optional string nickname = 6;</code>
   * @return The bytes for nickname.
   */
  com.google.protobuf.ByteString
      getNicknameBytes();

  /**
   * <code>optional string short_descr = 4;</code>
   * @return Whether the shortDescr field is set.
   */
  boolean hasShortDescr();
  /**
   * <code>optional string short_descr = 4;</code>
   * @return The shortDescr.
   */
  java.lang.String getShortDescr();
  /**
   * <code>optional string short_descr = 4;</code>
   * @return The bytes for shortDescr.
   */
  com.google.protobuf.ByteString
      getShortDescrBytes();

  /**
   * <code>optional string long_descr_html = 5;</code>
   * @return Whether the longDescrHtml field is set.
   */
  boolean hasLongDescrHtml();
  /**
   * <code>optional string long_descr_html = 5;</code>
   * @return The longDescrHtml.
   */
  java.lang.String getLongDescrHtml();
  /**
   * <code>optional string long_descr_html = 5;</code>
   * @return The bytes for longDescrHtml.
   */
  com.google.protobuf.ByteString
      getLongDescrHtmlBytes();

  /**
   * <code>optional .pl_types.ClassX class_x = 2;</code>
   * @return Whether the classX field is set.
   */
  boolean hasClassX();
  /**
   * <code>optional .pl_types.ClassX class_x = 2;</code>
   * @return The classX.
   */
  org.davincischools.leo.protos.pl_types.ClassX getClassX();
  /**
   * <code>optional .pl_types.ClassX class_x = 2;</code>
   */
  org.davincischools.leo.protos.pl_types.ClassXOrBuilder getClassXOrBuilder();

  /**
   * <code>repeated .pl_types.KnowledgeAndSkill knowledge_and_skills = 7;</code>
   */
  java.util.List<org.davincischools.leo.protos.pl_types.KnowledgeAndSkill> 
      getKnowledgeAndSkillsList();
  /**
   * <code>repeated .pl_types.KnowledgeAndSkill knowledge_and_skills = 7;</code>
   */
  org.davincischools.leo.protos.pl_types.KnowledgeAndSkill getKnowledgeAndSkills(int index);
  /**
   * <code>repeated .pl_types.KnowledgeAndSkill knowledge_and_skills = 7;</code>
   */
  int getKnowledgeAndSkillsCount();
  /**
   * <code>repeated .pl_types.KnowledgeAndSkill knowledge_and_skills = 7;</code>
   */
  java.util.List<? extends org.davincischools.leo.protos.pl_types.KnowledgeAndSkillOrBuilder> 
      getKnowledgeAndSkillsOrBuilderList();
  /**
   * <code>repeated .pl_types.KnowledgeAndSkill knowledge_and_skills = 7;</code>
   */
  org.davincischools.leo.protos.pl_types.KnowledgeAndSkillOrBuilder getKnowledgeAndSkillsOrBuilder(
      int index);

  /**
   * <code>repeated .pl_types.ProjectDefinition project_definitions = 8;</code>
   */
  java.util.List<org.davincischools.leo.protos.pl_types.ProjectDefinition> 
      getProjectDefinitionsList();
  /**
   * <code>repeated .pl_types.ProjectDefinition project_definitions = 8;</code>
   */
  org.davincischools.leo.protos.pl_types.ProjectDefinition getProjectDefinitions(int index);
  /**
   * <code>repeated .pl_types.ProjectDefinition project_definitions = 8;</code>
   */
  int getProjectDefinitionsCount();
  /**
   * <code>repeated .pl_types.ProjectDefinition project_definitions = 8;</code>
   */
  java.util.List<? extends org.davincischools.leo.protos.pl_types.ProjectDefinitionOrBuilder> 
      getProjectDefinitionsOrBuilderList();
  /**
   * <code>repeated .pl_types.ProjectDefinition project_definitions = 8;</code>
   */
  org.davincischools.leo.protos.pl_types.ProjectDefinitionOrBuilder getProjectDefinitionsOrBuilder(
      int index);
}
