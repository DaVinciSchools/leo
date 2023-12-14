// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: project_management.proto

package org.davincischools.leo.protos.project_management;

/**
 * Protobuf type {@code project_management.GetProjectsResponse}
 */
public final class GetProjectsResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:project_management.GetProjectsResponse)
    GetProjectsResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use GetProjectsResponse.newBuilder() to construct.
  private GetProjectsResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private GetProjectsResponse() {
    projects_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new GetProjectsResponse();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GetProjectsResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GetProjectsResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.davincischools.leo.protos.project_management.GetProjectsResponse.class, org.davincischools.leo.protos.project_management.GetProjectsResponse.Builder.class);
  }

  public static final int PROJECTS_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private java.util.List<org.davincischools.leo.protos.pl_types.Project> projects_;
  /**
   * <code>repeated .pl_types.Project projects = 1;</code>
   */
  @java.lang.Override
  public java.util.List<org.davincischools.leo.protos.pl_types.Project> getProjectsList() {
    return projects_;
  }
  /**
   * <code>repeated .pl_types.Project projects = 1;</code>
   */
  @java.lang.Override
  public java.util.List<? extends org.davincischools.leo.protos.pl_types.ProjectOrBuilder> 
      getProjectsOrBuilderList() {
    return projects_;
  }
  /**
   * <code>repeated .pl_types.Project projects = 1;</code>
   */
  @java.lang.Override
  public int getProjectsCount() {
    return projects_.size();
  }
  /**
   * <code>repeated .pl_types.Project projects = 1;</code>
   */
  @java.lang.Override
  public org.davincischools.leo.protos.pl_types.Project getProjects(int index) {
    return projects_.get(index);
  }
  /**
   * <code>repeated .pl_types.Project projects = 1;</code>
   */
  @java.lang.Override
  public org.davincischools.leo.protos.pl_types.ProjectOrBuilder getProjectsOrBuilder(
      int index) {
    return projects_.get(index);
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
    for (int i = 0; i < projects_.size(); i++) {
      output.writeMessage(1, projects_.get(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < projects_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, projects_.get(i));
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
    if (!(obj instanceof org.davincischools.leo.protos.project_management.GetProjectsResponse)) {
      return super.equals(obj);
    }
    org.davincischools.leo.protos.project_management.GetProjectsResponse other = (org.davincischools.leo.protos.project_management.GetProjectsResponse) obj;

    if (!getProjectsList()
        .equals(other.getProjectsList())) return false;
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
    if (getProjectsCount() > 0) {
      hash = (37 * hash) + PROJECTS_FIELD_NUMBER;
      hash = (53 * hash) + getProjectsList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.project_management.GetProjectsResponse parseFrom(
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
  public static Builder newBuilder(org.davincischools.leo.protos.project_management.GetProjectsResponse prototype) {
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
   * Protobuf type {@code project_management.GetProjectsResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:project_management.GetProjectsResponse)
      org.davincischools.leo.protos.project_management.GetProjectsResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GetProjectsResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GetProjectsResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.davincischools.leo.protos.project_management.GetProjectsResponse.class, org.davincischools.leo.protos.project_management.GetProjectsResponse.Builder.class);
    }

    // Construct using org.davincischools.leo.protos.project_management.GetProjectsResponse.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      if (projectsBuilder_ == null) {
        projects_ = java.util.Collections.emptyList();
      } else {
        projects_ = null;
        projectsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.davincischools.leo.protos.project_management.ProjectManagement.internal_static_project_management_GetProjectsResponse_descriptor;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.project_management.GetProjectsResponse getDefaultInstanceForType() {
      return org.davincischools.leo.protos.project_management.GetProjectsResponse.getDefaultInstance();
    }

    @java.lang.Override
    public org.davincischools.leo.protos.project_management.GetProjectsResponse build() {
      org.davincischools.leo.protos.project_management.GetProjectsResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.project_management.GetProjectsResponse buildPartial() {
      org.davincischools.leo.protos.project_management.GetProjectsResponse result = new org.davincischools.leo.protos.project_management.GetProjectsResponse(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(org.davincischools.leo.protos.project_management.GetProjectsResponse result) {
      if (projectsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          projects_ = java.util.Collections.unmodifiableList(projects_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.projects_ = projects_;
      } else {
        result.projects_ = projectsBuilder_.build();
      }
    }

    private void buildPartial0(org.davincischools.leo.protos.project_management.GetProjectsResponse result) {
      int from_bitField0_ = bitField0_;
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
      if (other instanceof org.davincischools.leo.protos.project_management.GetProjectsResponse) {
        return mergeFrom((org.davincischools.leo.protos.project_management.GetProjectsResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.davincischools.leo.protos.project_management.GetProjectsResponse other) {
      if (other == org.davincischools.leo.protos.project_management.GetProjectsResponse.getDefaultInstance()) return this;
      if (projectsBuilder_ == null) {
        if (!other.projects_.isEmpty()) {
          if (projects_.isEmpty()) {
            projects_ = other.projects_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureProjectsIsMutable();
            projects_.addAll(other.projects_);
          }
          onChanged();
        }
      } else {
        if (!other.projects_.isEmpty()) {
          if (projectsBuilder_.isEmpty()) {
            projectsBuilder_.dispose();
            projectsBuilder_ = null;
            projects_ = other.projects_;
            bitField0_ = (bitField0_ & ~0x00000001);
            projectsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getProjectsFieldBuilder() : null;
          } else {
            projectsBuilder_.addAllMessages(other.projects_);
          }
        }
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
            case 10: {
              org.davincischools.leo.protos.pl_types.Project m =
                  input.readMessage(
                      org.davincischools.leo.protos.pl_types.Project.parser(),
                      extensionRegistry);
              if (projectsBuilder_ == null) {
                ensureProjectsIsMutable();
                projects_.add(m);
              } else {
                projectsBuilder_.addMessage(m);
              }
              break;
            } // case 10
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

    private java.util.List<org.davincischools.leo.protos.pl_types.Project> projects_ =
      java.util.Collections.emptyList();
    private void ensureProjectsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        projects_ = new java.util.ArrayList<org.davincischools.leo.protos.pl_types.Project>(projects_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.davincischools.leo.protos.pl_types.Project, org.davincischools.leo.protos.pl_types.Project.Builder, org.davincischools.leo.protos.pl_types.ProjectOrBuilder> projectsBuilder_;

    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public java.util.List<org.davincischools.leo.protos.pl_types.Project> getProjectsList() {
      if (projectsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(projects_);
      } else {
        return projectsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public int getProjectsCount() {
      if (projectsBuilder_ == null) {
        return projects_.size();
      } else {
        return projectsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public org.davincischools.leo.protos.pl_types.Project getProjects(int index) {
      if (projectsBuilder_ == null) {
        return projects_.get(index);
      } else {
        return projectsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public Builder setProjects(
        int index, org.davincischools.leo.protos.pl_types.Project value) {
      if (projectsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureProjectsIsMutable();
        projects_.set(index, value);
        onChanged();
      } else {
        projectsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public Builder setProjects(
        int index, org.davincischools.leo.protos.pl_types.Project.Builder builderForValue) {
      if (projectsBuilder_ == null) {
        ensureProjectsIsMutable();
        projects_.set(index, builderForValue.build());
        onChanged();
      } else {
        projectsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public Builder addProjects(org.davincischools.leo.protos.pl_types.Project value) {
      if (projectsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureProjectsIsMutable();
        projects_.add(value);
        onChanged();
      } else {
        projectsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public Builder addProjects(
        int index, org.davincischools.leo.protos.pl_types.Project value) {
      if (projectsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureProjectsIsMutable();
        projects_.add(index, value);
        onChanged();
      } else {
        projectsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public Builder addProjects(
        org.davincischools.leo.protos.pl_types.Project.Builder builderForValue) {
      if (projectsBuilder_ == null) {
        ensureProjectsIsMutable();
        projects_.add(builderForValue.build());
        onChanged();
      } else {
        projectsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public Builder addProjects(
        int index, org.davincischools.leo.protos.pl_types.Project.Builder builderForValue) {
      if (projectsBuilder_ == null) {
        ensureProjectsIsMutable();
        projects_.add(index, builderForValue.build());
        onChanged();
      } else {
        projectsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public Builder addAllProjects(
        java.lang.Iterable<? extends org.davincischools.leo.protos.pl_types.Project> values) {
      if (projectsBuilder_ == null) {
        ensureProjectsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, projects_);
        onChanged();
      } else {
        projectsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public Builder clearProjects() {
      if (projectsBuilder_ == null) {
        projects_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        projectsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public Builder removeProjects(int index) {
      if (projectsBuilder_ == null) {
        ensureProjectsIsMutable();
        projects_.remove(index);
        onChanged();
      } else {
        projectsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public org.davincischools.leo.protos.pl_types.Project.Builder getProjectsBuilder(
        int index) {
      return getProjectsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public org.davincischools.leo.protos.pl_types.ProjectOrBuilder getProjectsOrBuilder(
        int index) {
      if (projectsBuilder_ == null) {
        return projects_.get(index);  } else {
        return projectsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public java.util.List<? extends org.davincischools.leo.protos.pl_types.ProjectOrBuilder> 
         getProjectsOrBuilderList() {
      if (projectsBuilder_ != null) {
        return projectsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(projects_);
      }
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public org.davincischools.leo.protos.pl_types.Project.Builder addProjectsBuilder() {
      return getProjectsFieldBuilder().addBuilder(
          org.davincischools.leo.protos.pl_types.Project.getDefaultInstance());
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public org.davincischools.leo.protos.pl_types.Project.Builder addProjectsBuilder(
        int index) {
      return getProjectsFieldBuilder().addBuilder(
          index, org.davincischools.leo.protos.pl_types.Project.getDefaultInstance());
    }
    /**
     * <code>repeated .pl_types.Project projects = 1;</code>
     */
    public java.util.List<org.davincischools.leo.protos.pl_types.Project.Builder> 
         getProjectsBuilderList() {
      return getProjectsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.davincischools.leo.protos.pl_types.Project, org.davincischools.leo.protos.pl_types.Project.Builder, org.davincischools.leo.protos.pl_types.ProjectOrBuilder> 
        getProjectsFieldBuilder() {
      if (projectsBuilder_ == null) {
        projectsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            org.davincischools.leo.protos.pl_types.Project, org.davincischools.leo.protos.pl_types.Project.Builder, org.davincischools.leo.protos.pl_types.ProjectOrBuilder>(
                projects_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        projects_ = null;
      }
      return projectsBuilder_;
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


    // @@protoc_insertion_point(builder_scope:project_management.GetProjectsResponse)
  }

  // @@protoc_insertion_point(class_scope:project_management.GetProjectsResponse)
  private static final org.davincischools.leo.protos.project_management.GetProjectsResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.davincischools.leo.protos.project_management.GetProjectsResponse();
  }

  public static org.davincischools.leo.protos.project_management.GetProjectsResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<GetProjectsResponse>
      PARSER = new com.google.protobuf.AbstractParser<GetProjectsResponse>() {
    @java.lang.Override
    public GetProjectsResponse parsePartialFrom(
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

  public static com.google.protobuf.Parser<GetProjectsResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<GetProjectsResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.davincischools.leo.protos.project_management.GetProjectsResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

