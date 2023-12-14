// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user_x_management.proto

package org.davincischools.leo.protos.user_x_management;

/**
 * Protobuf type {@code user_x_management.RemoveUserXRequest}
 */
public final class RemoveUserXRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:user_x_management.RemoveUserXRequest)
    RemoveUserXRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RemoveUserXRequest.newBuilder() to construct.
  private RemoveUserXRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RemoveUserXRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new RemoveUserXRequest();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RemoveUserXRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RemoveUserXRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.davincischools.leo.protos.user_x_management.RemoveUserXRequest.class, org.davincischools.leo.protos.user_x_management.RemoveUserXRequest.Builder.class);
  }

  private int bitField0_;
  public static final int USER_X_ID_FIELD_NUMBER = 2;
  private int userXId_ = 0;
  /**
   * <code>optional int32 user_x_id = 2;</code>
   * @return Whether the userXId field is set.
   */
  @java.lang.Override
  public boolean hasUserXId() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional int32 user_x_id = 2;</code>
   * @return The userXId.
   */
  @java.lang.Override
  public int getUserXId() {
    return userXId_;
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
      output.writeInt32(2, userXId_);
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
        .computeInt32Size(2, userXId_);
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
    if (!(obj instanceof org.davincischools.leo.protos.user_x_management.RemoveUserXRequest)) {
      return super.equals(obj);
    }
    org.davincischools.leo.protos.user_x_management.RemoveUserXRequest other = (org.davincischools.leo.protos.user_x_management.RemoveUserXRequest) obj;

    if (hasUserXId() != other.hasUserXId()) return false;
    if (hasUserXId()) {
      if (getUserXId()
          != other.getUserXId()) return false;
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
    if (hasUserXId()) {
      hash = (37 * hash) + USER_X_ID_FIELD_NUMBER;
      hash = (53 * hash) + getUserXId();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest parseFrom(
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
  public static Builder newBuilder(org.davincischools.leo.protos.user_x_management.RemoveUserXRequest prototype) {
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
   * Protobuf type {@code user_x_management.RemoveUserXRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:user_x_management.RemoveUserXRequest)
      org.davincischools.leo.protos.user_x_management.RemoveUserXRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RemoveUserXRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RemoveUserXRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.davincischools.leo.protos.user_x_management.RemoveUserXRequest.class, org.davincischools.leo.protos.user_x_management.RemoveUserXRequest.Builder.class);
    }

    // Construct using org.davincischools.leo.protos.user_x_management.RemoveUserXRequest.newBuilder()
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
      userXId_ = 0;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RemoveUserXRequest_descriptor;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.user_x_management.RemoveUserXRequest getDefaultInstanceForType() {
      return org.davincischools.leo.protos.user_x_management.RemoveUserXRequest.getDefaultInstance();
    }

    @java.lang.Override
    public org.davincischools.leo.protos.user_x_management.RemoveUserXRequest build() {
      org.davincischools.leo.protos.user_x_management.RemoveUserXRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.user_x_management.RemoveUserXRequest buildPartial() {
      org.davincischools.leo.protos.user_x_management.RemoveUserXRequest result = new org.davincischools.leo.protos.user_x_management.RemoveUserXRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(org.davincischools.leo.protos.user_x_management.RemoveUserXRequest result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.userXId_ = userXId_;
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
      if (other instanceof org.davincischools.leo.protos.user_x_management.RemoveUserXRequest) {
        return mergeFrom((org.davincischools.leo.protos.user_x_management.RemoveUserXRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.davincischools.leo.protos.user_x_management.RemoveUserXRequest other) {
      if (other == org.davincischools.leo.protos.user_x_management.RemoveUserXRequest.getDefaultInstance()) return this;
      if (other.hasUserXId()) {
        setUserXId(other.getUserXId());
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
            case 16: {
              userXId_ = input.readInt32();
              bitField0_ |= 0x00000001;
              break;
            } // case 16
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

    private int userXId_ ;
    /**
     * <code>optional int32 user_x_id = 2;</code>
     * @return Whether the userXId field is set.
     */
    @java.lang.Override
    public boolean hasUserXId() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>optional int32 user_x_id = 2;</code>
     * @return The userXId.
     */
    @java.lang.Override
    public int getUserXId() {
      return userXId_;
    }
    /**
     * <code>optional int32 user_x_id = 2;</code>
     * @param value The userXId to set.
     * @return This builder for chaining.
     */
    public Builder setUserXId(int value) {

      userXId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 user_x_id = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearUserXId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      userXId_ = 0;
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


    // @@protoc_insertion_point(builder_scope:user_x_management.RemoveUserXRequest)
  }

  // @@protoc_insertion_point(class_scope:user_x_management.RemoveUserXRequest)
  private static final org.davincischools.leo.protos.user_x_management.RemoveUserXRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.davincischools.leo.protos.user_x_management.RemoveUserXRequest();
  }

  public static org.davincischools.leo.protos.user_x_management.RemoveUserXRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RemoveUserXRequest>
      PARSER = new com.google.protobuf.AbstractParser<RemoveUserXRequest>() {
    @java.lang.Override
    public RemoveUserXRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<RemoveUserXRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RemoveUserXRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.davincischools.leo.protos.user_x_management.RemoveUserXRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

