// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: assignment_management.proto

package org.davincischools.leo.protos.assignment_management;

/**
 * Protobuf type {@code assignment_management.CreateAssignmentRequest}
 */
public final class CreateAssignmentRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:assignment_management.CreateAssignmentRequest)
    CreateAssignmentRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use CreateAssignmentRequest.newBuilder() to construct.
  private CreateAssignmentRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private CreateAssignmentRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new CreateAssignmentRequest();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.davincischools.leo.protos.assignment_management.AssignmentManagement.internal_static_assignment_management_CreateAssignmentRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.davincischools.leo.protos.assignment_management.AssignmentManagement.internal_static_assignment_management_CreateAssignmentRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest.class, org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest.Builder.class);
  }

  private int bitField0_;
  public static final int CLASS_X_ID_FIELD_NUMBER = 1;
  private int classXId_ = 0;
  /**
   * <code>optional int32 class_x_id = 1;</code>
   * @return Whether the classXId field is set.
   */
  @java.lang.Override
  public boolean hasClassXId() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional int32 class_x_id = 1;</code>
   * @return The classXId.
   */
  @java.lang.Override
  public int getClassXId() {
    return classXId_;
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
      output.writeInt32(1, classXId_);
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
        .computeInt32Size(1, classXId_);
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
    if (!(obj instanceof org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest)) {
      return super.equals(obj);
    }
    org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest other = (org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest) obj;

    if (hasClassXId() != other.hasClassXId()) return false;
    if (hasClassXId()) {
      if (getClassXId()
          != other.getClassXId()) return false;
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
    if (hasClassXId()) {
      hash = (37 * hash) + CLASS_X_ID_FIELD_NUMBER;
      hash = (53 * hash) + getClassXId();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest parseFrom(
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
  public static Builder newBuilder(org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest prototype) {
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
   * Protobuf type {@code assignment_management.CreateAssignmentRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:assignment_management.CreateAssignmentRequest)
      org.davincischools.leo.protos.assignment_management.CreateAssignmentRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.davincischools.leo.protos.assignment_management.AssignmentManagement.internal_static_assignment_management_CreateAssignmentRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.davincischools.leo.protos.assignment_management.AssignmentManagement.internal_static_assignment_management_CreateAssignmentRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest.class, org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest.Builder.class);
    }

    // Construct using org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest.newBuilder()
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
      classXId_ = 0;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.davincischools.leo.protos.assignment_management.AssignmentManagement.internal_static_assignment_management_CreateAssignmentRequest_descriptor;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest getDefaultInstanceForType() {
      return org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest.getDefaultInstance();
    }

    @java.lang.Override
    public org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest build() {
      org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest buildPartial() {
      org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest result = new org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.classXId_ = classXId_;
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
      if (other instanceof org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest) {
        return mergeFrom((org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest other) {
      if (other == org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest.getDefaultInstance()) return this;
      if (other.hasClassXId()) {
        setClassXId(other.getClassXId());
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
            case 8: {
              classXId_ = input.readInt32();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
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

    private int classXId_ ;
    /**
     * <code>optional int32 class_x_id = 1;</code>
     * @return Whether the classXId field is set.
     */
    @java.lang.Override
    public boolean hasClassXId() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>optional int32 class_x_id = 1;</code>
     * @return The classXId.
     */
    @java.lang.Override
    public int getClassXId() {
      return classXId_;
    }
    /**
     * <code>optional int32 class_x_id = 1;</code>
     * @param value The classXId to set.
     * @return This builder for chaining.
     */
    public Builder setClassXId(int value) {

      classXId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 class_x_id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearClassXId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      classXId_ = 0;
      onChanged();
      return this;
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


    // @@protoc_insertion_point(builder_scope:assignment_management.CreateAssignmentRequest)
  }

  // @@protoc_insertion_point(class_scope:assignment_management.CreateAssignmentRequest)
  private static final org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest();
  }

  public static org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<CreateAssignmentRequest>
      PARSER = new com.google.protobuf.AbstractParser<CreateAssignmentRequest>() {
    @java.lang.Override
    public CreateAssignmentRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<CreateAssignmentRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<CreateAssignmentRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.davincischools.leo.protos.assignment_management.CreateAssignmentRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

