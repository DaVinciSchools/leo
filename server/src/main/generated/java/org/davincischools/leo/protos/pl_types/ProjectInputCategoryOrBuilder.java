// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: pl_types.proto

package org.davincischools.leo.protos.pl_types;

public interface ProjectInputCategoryOrBuilder extends
    // @@protoc_insertion_point(interface_extends:pl_types.ProjectInputCategory)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * ProjectDefinitionCategory.id
   * </pre>
   *
   * <code>optional int32 id = 1;</code>
   * @return Whether the id field is set.
   */
  boolean hasId();
  /**
   * <pre>
   * ProjectDefinitionCategory.id
   * </pre>
   *
   * <code>optional int32 id = 1;</code>
   * @return The id.
   */
  int getId();

  /**
   * <pre>
   * ProjectDefinitionCategoryType.id
   * </pre>
   *
   * <code>optional int32 type_id = 2;</code>
   * @return Whether the typeId field is set.
   */
  boolean hasTypeId();
  /**
   * <pre>
   * ProjectDefinitionCategoryType.id
   * </pre>
   *
   * <code>optional int32 type_id = 2;</code>
   * @return The typeId.
   */
  int getTypeId();

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
   * <code>optional string input_descr = 10;</code>
   * @return Whether the inputDescr field is set.
   */
  boolean hasInputDescr();
  /**
   * <code>optional string input_descr = 10;</code>
   * @return The inputDescr.
   */
  java.lang.String getInputDescr();
  /**
   * <code>optional string input_descr = 10;</code>
   * @return The bytes for inputDescr.
   */
  com.google.protobuf.ByteString
      getInputDescrBytes();

  /**
   * <code>optional string hint = 5;</code>
   * @return Whether the hint field is set.
   */
  boolean hasHint();
  /**
   * <code>optional string hint = 5;</code>
   * @return The hint.
   */
  java.lang.String getHint();
  /**
   * <code>optional string hint = 5;</code>
   * @return The bytes for hint.
   */
  com.google.protobuf.ByteString
      getHintBytes();

  /**
   * <code>optional string placeholder = 6;</code>
   * @return Whether the placeholder field is set.
   */
  boolean hasPlaceholder();
  /**
   * <code>optional string placeholder = 6;</code>
   * @return The placeholder.
   */
  java.lang.String getPlaceholder();
  /**
   * <code>optional string placeholder = 6;</code>
   * @return The bytes for placeholder.
   */
  com.google.protobuf.ByteString
      getPlaceholderBytes();

  /**
   * <code>optional .pl_types.ProjectInputCategory.ValueType value_type = 7;</code>
   * @return Whether the valueType field is set.
   */
  boolean hasValueType();
  /**
   * <code>optional .pl_types.ProjectInputCategory.ValueType value_type = 7;</code>
   * @return The enum numeric value on the wire for valueType.
   */
  int getValueTypeValue();
  /**
   * <code>optional .pl_types.ProjectInputCategory.ValueType value_type = 7;</code>
   * @return The valueType.
   */
  org.davincischools.leo.protos.pl_types.ProjectInputCategory.ValueType getValueType();

  /**
   * <code>optional int32 max_num_values = 8;</code>
   * @return Whether the maxNumValues field is set.
   */
  boolean hasMaxNumValues();
  /**
   * <code>optional int32 max_num_values = 8;</code>
   * @return The maxNumValues.
   */
  int getMaxNumValues();

  /**
   * <pre>
   * Set for non-Free_TEXT value types.
   * </pre>
   *
   * <code>repeated .pl_types.ProjectInputCategory.Option options = 9;</code>
   */
  java.util.List<org.davincischools.leo.protos.pl_types.ProjectInputCategory.Option> 
      getOptionsList();
  /**
   * <pre>
   * Set for non-Free_TEXT value types.
   * </pre>
   *
   * <code>repeated .pl_types.ProjectInputCategory.Option options = 9;</code>
   */
  org.davincischools.leo.protos.pl_types.ProjectInputCategory.Option getOptions(int index);
  /**
   * <pre>
   * Set for non-Free_TEXT value types.
   * </pre>
   *
   * <code>repeated .pl_types.ProjectInputCategory.Option options = 9;</code>
   */
  int getOptionsCount();
  /**
   * <pre>
   * Set for non-Free_TEXT value types.
   * </pre>
   *
   * <code>repeated .pl_types.ProjectInputCategory.Option options = 9;</code>
   */
  java.util.List<? extends org.davincischools.leo.protos.pl_types.ProjectInputCategory.OptionOrBuilder> 
      getOptionsOrBuilderList();
  /**
   * <pre>
   * Set for non-Free_TEXT value types.
   * </pre>
   *
   * <code>repeated .pl_types.ProjectInputCategory.Option options = 9;</code>
   */
  org.davincischools.leo.protos.pl_types.ProjectInputCategory.OptionOrBuilder getOptionsOrBuilder(
      int index);
}
