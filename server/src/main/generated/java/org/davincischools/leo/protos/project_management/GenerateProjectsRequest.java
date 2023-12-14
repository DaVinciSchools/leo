// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: project_management.proto

package org.davincischools.leo.protos.project_management;

/**
 * Protobuf type {@code project_management.GenerateProjectsRequest}
 */
public final class GenerateProjectsRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:project_management.GenerateProjectsRequest)
    GenerateProjectsRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use GenerateProjectsRequest.newBuilder() to construct.
  private GenerateProjectsRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private GenerateProjectsRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new GenerateProjectsRequest();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GenerateProjectsRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GenerateProjectsRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.davincischools.leo.protos.project_management.GenerateProjectsRequest.class, org.davincischools.leo.protos.project_management.GenerateProjectsRequest.Builder.class);
  }

  private int bitField0_;
  public static final int DEFINITION_FIELD_NUMBER = 2;
  private org.davincischools.leo.protos.pl_types.ProjectDefinition definition_;
  /**
   * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
   * @return Whether the definition field is set.
   */
  @java.lang.Override
  public boolean hasDefinition() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
   * @return The definition.
   */
  @java.lang.Override
  public org.davincischools.leo.protos.pl_types.ProjectDefinition getDefinition() {
    return definition_ == null ? org.davincischools.leo.protos.pl_types.ProjectDefinition.getDefaultInstance() : definition_;
  }
  /**
   * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
   */
  @java.lang.Override
  public org.davincischools.leo.protos.pl_types.ProjectDefinitionOrBuilder getDefinitionOrBuilder() {
    return definition_ == null ? org.davincischools.leo.protos.pl_types.ProjectDefinition.getDefaultInstance() : definition_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(2, getDefinition());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getDefinition());
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.davincischools.leo.protos.project_management.GenerateProjectsRequest)) {
      return super.equals(obj);
    }
    org.davincischools.leo.protos.project_management.GenerateProjectsRequest other = (org.davincischools.leo.protos.project_management.GenerateProjectsRequest) obj;

    if (hasDefinition() != other.hasDefinition()) return false;
    if (hasDefinition()) {
      if (!getDefinition()
          .equals(other.getDefinition())) return false;
    }
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasDefinition()) {
      hash = (37 * hash) + DEFINITION_FIELD_NUMBER;
      hash = (53 * hash) + getDefinition().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.davincischools.leo.protos.project_management.GenerateProjectsRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code project_management.GenerateProjectsRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:project_management.GenerateProjectsRequest)
      org.davincischools.leo.protos.project_management.GenerateProjectsRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GenerateProjectsRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GenerateProjectsRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.davincischools.leo.protos.project_management.GenerateProjectsRequest.class, org.davincischools.leo.protos.project_management.GenerateProjectsRequest.Builder.class);
    }

    // Construct using org.davincischools.leo.protos.project_management.GenerateProjectsRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getDefinitionFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      definition_ = null;
      if (definitionBuilder_ != null) {
        definitionBuilder_.dispose();
        definitionBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GenerateProjectsRequest_descriptor;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.project_management.GenerateProjectsRequest getDefaultInstanceForType() {
      return org.davincischools.leo.protos.project_management.GenerateProjectsRequest.getDefaultInstance();
    }

    @java.lang.Override
    public org.davincischools.leo.protos.project_management.GenerateProjectsRequest build() {
      org.davincischools.leo.protos.project_management.GenerateProjectsRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.project_management.GenerateProjectsRequest buildPartial() {
      org.davincischools.leo.protos.project_management.GenerateProjectsRequest result = new org.davincischools.leo.protos.project_management.GenerateProjectsRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(org.davincischools.leo.protos.project_management.GenerateProjectsRequest result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.definition_ = definitionBuilder_ == null
            ? definition_
            : definitionBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      result.bitField0_ |= to_bitField0_;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.davincischools.leo.protos.project_management.GenerateProjectsRequest) {
        return mergeFrom((org.davincischools.leo.protos.project_management.GenerateProjectsRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.davincischools.leo.protos.project_management.GenerateProjectsRequest other) {
      if (other == org.davincischools.leo.protos.project_management.GenerateProjectsRequest.getDefaultInstance()) return this;
      if (other.hasDefinition()) {
        mergeDefinition(other.getDefinition());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 18: {
              input.readMessage(
                  getDefinitionFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000001;
              break;
            } // case 18
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private org.davincischools.leo.protos.pl_types.ProjectDefinition definition_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.davincischools.leo.protos.pl_types.ProjectDefinition, org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder, org.davincischools.leo.protos.pl_types.ProjectDefinitionOrBuilder> definitionBuilder_;
    /**
     * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
     * @return Whether the definition field is set.
     */
    public boolean hasDefinition() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
     * @return The definition.
     */
    public org.davincischools.leo.protos.pl_types.ProjectDefinition getDefinition() {
      if (definitionBuilder_ == null) {
        return definition_ == null ? org.davincischools.leo.protos.pl_types.ProjectDefinition.getDefaultInstance() : definition_;
      } else {
        return definitionBuilder_.getMessage();
      }
    }
    /**
     * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
     */
    public Builder setDefinition(org.davincischools.leo.protos.pl_types.ProjectDefinition value) {
      if (definitionBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        definition_ = value;
      } else {
        definitionBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
     */
    public Builder setDefinition(
        org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder builderForValue) {
      if (definitionBuilder_ == null) {
        definition_ = builderForValue.build();
      } else {
        definitionBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
     */
    public Builder mergeDefinition(org.davincischools.leo.protos.pl_types.ProjectDefinition value) {
      if (definitionBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0) &&
          definition_ != null &&
          definition_ != org.davincischools.leo.protos.pl_types.ProjectDefinition.getDefaultInstance()) {
          getDefinitionBuilder().mergeFrom(value);
        } else {
          definition_ = value;
        }
      } else {
        definitionBuilder_.mergeFrom(value);
      }
      if (definition_ != null) {
        bitField0_ |= 0x00000001;
        onChanged();
      }
      return this;
    }
    /**
     * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
     */
    public Builder clearDefinition() {
      bitField0_ = (bitField0_ & ~0x00000001);
      definition_ = null;
      if (definitionBuilder_ != null) {
        definitionBuilder_.dispose();
        definitionBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
     */
    public org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder getDefinitionBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getDefinitionFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
     */
    public org.davincischools.leo.protos.pl_types.ProjectDefinitionOrBuilder getDefinitionOrBuilder() {
      if (definitionBuilder_ != null) {
        return definitionBuilder_.getMessageOrBuilder();
      } else {
        return definition_ == null ?
            org.davincischools.leo.protos.pl_types.ProjectDefinition.getDefaultInstance() : definition_;
      }
    }
    /**
     * <code>optional .pl_types.ProjectDefinition definition = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.davincischools.leo.protos.pl_types.ProjectDefinition, org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder, org.davincischools.leo.protos.pl_types.ProjectDefinitionOrBuilder> 
        getDefinitionFieldBuilder() {
      if (definitionBuilder_ == null) {
        definitionBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.davincischools.leo.protos.pl_types.ProjectDefinition, org.davincischools.leo.protos.pl_types.ProjectDefinition.Builder, org.davincischools.leo.protos.pl_types.ProjectDefinitionOrBuilder>(
                getDefinition(),
                getParentForChildren(),
                isClean());
        definition_ = null;
      }
      return definitionBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:project_management.GenerateProjectsRequest)
  }

  // @@protoc_insertion_point(class_scope:project_management.GenerateProjectsRequest)
  private static final org.davincischools.leo.protos.project_management.GenerateProjectsRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.davincischools.leo.protos.project_management.GenerateProjectsRequest();
  }

  public static org.davincischools.leo.protos.project_management.GenerateProjectsRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<GenerateProjectsRequest>
      PARSER = new com.google.protobuf.AbstractParser<GenerateProjectsRequest>() {
    @java.lang.Override
    public GenerateProjectsRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<GenerateProjectsRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<GenerateProjectsRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.davincischools.leo.protos.project_management.GenerateProjectsRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

