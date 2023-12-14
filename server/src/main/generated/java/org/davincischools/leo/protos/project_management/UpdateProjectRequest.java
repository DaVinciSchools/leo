// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: project_management.proto

package org.davincischools.leo.protos.project_management;

/**
 * Protobuf type {@code project_management.UpdateProjectRequest}
 */
public final class UpdateProjectRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:project_management.UpdateProjectRequest)
    UpdateProjectRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use UpdateProjectRequest.newBuilder() to construct.
  private UpdateProjectRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private UpdateProjectRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new UpdateProjectRequest();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_UpdateProjectRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_UpdateProjectRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.davincischools.leo.protos.project_management.UpdateProjectRequest.class, org.davincischools.leo.protos.project_management.UpdateProjectRequest.Builder.class);
  }

  private int bitField0_;
  public static final int PROJECT_FIELD_NUMBER = 2;
  private org.davincischools.leo.protos.pl_types.Project project_;
  /**
   * <code>optional .pl_types.Project project = 2;</code>
   * @return Whether the project field is set.
   */
  @java.lang.Override
  public boolean hasProject() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional .pl_types.Project project = 2;</code>
   * @return The project.
   */
  @java.lang.Override
  public org.davincischools.leo.protos.pl_types.Project getProject() {
    return project_ == null ? org.davincischools.leo.protos.pl_types.Project.getDefaultInstance() : project_;
  }
  /**
   * <code>optional .pl_types.Project project = 2;</code>
   */
  @java.lang.Override
  public org.davincischools.leo.protos.pl_types.ProjectOrBuilder getProjectOrBuilder() {
    return project_ == null ? org.davincischools.leo.protos.pl_types.Project.getDefaultInstance() : project_;
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
      output.writeMessage(2, getProject());
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
        .computeMessageSize(2, getProject());
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
    if (!(obj instanceof org.davincischools.leo.protos.project_management.UpdateProjectRequest)) {
      return super.equals(obj);
    }
    org.davincischools.leo.protos.project_management.UpdateProjectRequest other = (org.davincischools.leo.protos.project_management.UpdateProjectRequest) obj;

    if (hasProject() != other.hasProject()) return false;
    if (hasProject()) {
      if (!getProject()
          .equals(other.getProject())) return false;
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
    if (hasProject()) {
      hash = (37 * hash) + PROJECT_FIELD_NUMBER;
      hash = (53 * hash) + getProject().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest parseFrom(
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
  public static Builder newBuilder(org.davincischools.leo.protos.project_management.UpdateProjectRequest prototype) {
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
   * Protobuf type {@code project_management.UpdateProjectRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:project_management.UpdateProjectRequest)
      org.davincischools.leo.protos.project_management.UpdateProjectRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_UpdateProjectRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_UpdateProjectRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.davincischools.leo.protos.project_management.UpdateProjectRequest.class, org.davincischools.leo.protos.project_management.UpdateProjectRequest.Builder.class);
    }

    // Construct using org.davincischools.leo.protos.project_management.UpdateProjectRequest.newBuilder()
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
        getProjectFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      project_ = null;
      if (projectBuilder_ != null) {
        projectBuilder_.dispose();
        projectBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_UpdateProjectRequest_descriptor;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.project_management.UpdateProjectRequest getDefaultInstanceForType() {
      return org.davincischools.leo.protos.project_management.UpdateProjectRequest.getDefaultInstance();
    }

    @java.lang.Override
    public org.davincischools.leo.protos.project_management.UpdateProjectRequest build() {
      org.davincischools.leo.protos.project_management.UpdateProjectRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.project_management.UpdateProjectRequest buildPartial() {
      org.davincischools.leo.protos.project_management.UpdateProjectRequest result = new org.davincischools.leo.protos.project_management.UpdateProjectRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(org.davincischools.leo.protos.project_management.UpdateProjectRequest result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.project_ = projectBuilder_ == null
            ? project_
            : projectBuilder_.build();
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
      if (other instanceof org.davincischools.leo.protos.project_management.UpdateProjectRequest) {
        return mergeFrom((org.davincischools.leo.protos.project_management.UpdateProjectRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.davincischools.leo.protos.project_management.UpdateProjectRequest other) {
      if (other == org.davincischools.leo.protos.project_management.UpdateProjectRequest.getDefaultInstance()) return this;
      if (other.hasProject()) {
        mergeProject(other.getProject());
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
                  getProjectFieldBuilder().getBuilder(),
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

    private org.davincischools.leo.protos.pl_types.Project project_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.davincischools.leo.protos.pl_types.Project, org.davincischools.leo.protos.pl_types.Project.Builder, org.davincischools.leo.protos.pl_types.ProjectOrBuilder> projectBuilder_;
    /**
     * <code>optional .pl_types.Project project = 2;</code>
     * @return Whether the project field is set.
     */
    public boolean hasProject() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>optional .pl_types.Project project = 2;</code>
     * @return The project.
     */
    public org.davincischools.leo.protos.pl_types.Project getProject() {
      if (projectBuilder_ == null) {
        return project_ == null ? org.davincischools.leo.protos.pl_types.Project.getDefaultInstance() : project_;
      } else {
        return projectBuilder_.getMessage();
      }
    }
    /**
     * <code>optional .pl_types.Project project = 2;</code>
     */
    public Builder setProject(org.davincischools.leo.protos.pl_types.Project value) {
      if (projectBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        project_ = value;
      } else {
        projectBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional .pl_types.Project project = 2;</code>
     */
    public Builder setProject(
        org.davincischools.leo.protos.pl_types.Project.Builder builderForValue) {
      if (projectBuilder_ == null) {
        project_ = builderForValue.build();
      } else {
        projectBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional .pl_types.Project project = 2;</code>
     */
    public Builder mergeProject(org.davincischools.leo.protos.pl_types.Project value) {
      if (projectBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0) &&
          project_ != null &&
          project_ != org.davincischools.leo.protos.pl_types.Project.getDefaultInstance()) {
          getProjectBuilder().mergeFrom(value);
        } else {
          project_ = value;
        }
      } else {
        projectBuilder_.mergeFrom(value);
      }
      if (project_ != null) {
        bitField0_ |= 0x00000001;
        onChanged();
      }
      return this;
    }
    /**
     * <code>optional .pl_types.Project project = 2;</code>
     */
    public Builder clearProject() {
      bitField0_ = (bitField0_ & ~0x00000001);
      project_ = null;
      if (projectBuilder_ != null) {
        projectBuilder_.dispose();
        projectBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>optional .pl_types.Project project = 2;</code>
     */
    public org.davincischools.leo.protos.pl_types.Project.Builder getProjectBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getProjectFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .pl_types.Project project = 2;</code>
     */
    public org.davincischools.leo.protos.pl_types.ProjectOrBuilder getProjectOrBuilder() {
      if (projectBuilder_ != null) {
        return projectBuilder_.getMessageOrBuilder();
      } else {
        return project_ == null ?
            org.davincischools.leo.protos.pl_types.Project.getDefaultInstance() : project_;
      }
    }
    /**
     * <code>optional .pl_types.Project project = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.davincischools.leo.protos.pl_types.Project, org.davincischools.leo.protos.pl_types.Project.Builder, org.davincischools.leo.protos.pl_types.ProjectOrBuilder> 
        getProjectFieldBuilder() {
      if (projectBuilder_ == null) {
        projectBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.davincischools.leo.protos.pl_types.Project, org.davincischools.leo.protos.pl_types.Project.Builder, org.davincischools.leo.protos.pl_types.ProjectOrBuilder>(
                getProject(),
                getParentForChildren(),
                isClean());
        project_ = null;
      }
      return projectBuilder_;
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


    // @@protoc_insertion_point(builder_scope:project_management.UpdateProjectRequest)
  }

  // @@protoc_insertion_point(class_scope:project_management.UpdateProjectRequest)
  private static final org.davincischools.leo.protos.project_management.UpdateProjectRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.davincischools.leo.protos.project_management.UpdateProjectRequest();
  }

  public static org.davincischools.leo.protos.project_management.UpdateProjectRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<UpdateProjectRequest>
      PARSER = new com.google.protobuf.AbstractParser<UpdateProjectRequest>() {
    @java.lang.Override
    public UpdateProjectRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<UpdateProjectRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<UpdateProjectRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.davincischools.leo.protos.project_management.UpdateProjectRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

