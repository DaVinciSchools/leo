// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user_x_management.proto

package org.davincischools.leo.protos.user_x_management;

/**
 * Protobuf type {@code user_x_management.RegisterUserXResponse}
 */
public final class RegisterUserXResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:user_x_management.RegisterUserXResponse)
    RegisterUserXResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RegisterUserXResponse.newBuilder() to construct.
  private RegisterUserXResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RegisterUserXResponse() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new RegisterUserXResponse();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RegisterUserXResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RegisterUserXResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.davincischools.leo.protos.user_x_management.RegisterUserXResponse.class, org.davincischools.leo.protos.user_x_management.RegisterUserXResponse.Builder.class);
  }

  private int bitField0_;
  public static final int ACCOUNT_ALREADY_EXISTS_FIELD_NUMBER = 1;
  private boolean accountAlreadyExists_ = false;
  /**
   * <code>optional bool account_already_exists = 1;</code>
   * @return Whether the accountAlreadyExists field is set.
   */
  @java.lang.Override
  public boolean hasAccountAlreadyExists() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional bool account_already_exists = 1;</code>
   * @return The accountAlreadyExists.
   */
  @java.lang.Override
  public boolean getAccountAlreadyExists() {
    return accountAlreadyExists_;
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
      output.writeBool(1, accountAlreadyExists_);
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
        .computeBoolSize(1, accountAlreadyExists_);
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
    if (!(obj instanceof org.davincischools.leo.protos.user_x_management.RegisterUserXResponse)) {
      return super.equals(obj);
    }
    org.davincischools.leo.protos.user_x_management.RegisterUserXResponse other = (org.davincischools.leo.protos.user_x_management.RegisterUserXResponse) obj;

    if (hasAccountAlreadyExists() != other.hasAccountAlreadyExists()) return false;
    if (hasAccountAlreadyExists()) {
      if (getAccountAlreadyExists()
          != other.getAccountAlreadyExists()) return false;
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
    if (hasAccountAlreadyExists()) {
      hash = (37 * hash) + ACCOUNT_ALREADY_EXISTS_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
          getAccountAlreadyExists());
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse parseFrom(
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
  public static Builder newBuilder(org.davincischools.leo.protos.user_x_management.RegisterUserXResponse prototype) {
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
   * Protobuf type {@code user_x_management.RegisterUserXResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:user_x_management.RegisterUserXResponse)
      org.davincischools.leo.protos.user_x_management.RegisterUserXResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RegisterUserXResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RegisterUserXResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.davincischools.leo.protos.user_x_management.RegisterUserXResponse.class, org.davincischools.leo.protos.user_x_management.RegisterUserXResponse.Builder.class);
    }

    // Construct using org.davincischools.leo.protos.user_x_management.RegisterUserXResponse.newBuilder()
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
      accountAlreadyExists_ = false;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.davincischools.leo.protos.user_x_management.UserXManagement.internal_static_user_x_management_RegisterUserXResponse_descriptor;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.user_x_management.RegisterUserXResponse getDefaultInstanceForType() {
      return org.davincischools.leo.protos.user_x_management.RegisterUserXResponse.getDefaultInstance();
    }

    @java.lang.Override
    public org.davincischools.leo.protos.user_x_management.RegisterUserXResponse build() {
      org.davincischools.leo.protos.user_x_management.RegisterUserXResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.user_x_management.RegisterUserXResponse buildPartial() {
      org.davincischools.leo.protos.user_x_management.RegisterUserXResponse result = new org.davincischools.leo.protos.user_x_management.RegisterUserXResponse(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(org.davincischools.leo.protos.user_x_management.RegisterUserXResponse result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.accountAlreadyExists_ = accountAlreadyExists_;
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
      if (other instanceof org.davincischools.leo.protos.user_x_management.RegisterUserXResponse) {
        return mergeFrom((org.davincischools.leo.protos.user_x_management.RegisterUserXResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.davincischools.leo.protos.user_x_management.RegisterUserXResponse other) {
      if (other == org.davincischools.leo.protos.user_x_management.RegisterUserXResponse.getDefaultInstance()) return this;
      if (other.hasAccountAlreadyExists()) {
        setAccountAlreadyExists(other.getAccountAlreadyExists());
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
              accountAlreadyExists_ = input.readBool();
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

    private boolean accountAlreadyExists_ ;
    /**
     * <code>optional bool account_already_exists = 1;</code>
     * @return Whether the accountAlreadyExists field is set.
     */
    @java.lang.Override
    public boolean hasAccountAlreadyExists() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>optional bool account_already_exists = 1;</code>
     * @return The accountAlreadyExists.
     */
    @java.lang.Override
    public boolean getAccountAlreadyExists() {
      return accountAlreadyExists_;
    }
    /**
     * <code>optional bool account_already_exists = 1;</code>
     * @param value The accountAlreadyExists to set.
     * @return This builder for chaining.
     */
    public Builder setAccountAlreadyExists(boolean value) {

      accountAlreadyExists_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional bool account_already_exists = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearAccountAlreadyExists() {
      bitField0_ = (bitField0_ & ~0x00000001);
      accountAlreadyExists_ = false;
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


    // @@protoc_insertion_point(builder_scope:user_x_management.RegisterUserXResponse)
  }

  // @@protoc_insertion_point(class_scope:user_x_management.RegisterUserXResponse)
  private static final org.davincischools.leo.protos.user_x_management.RegisterUserXResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.davincischools.leo.protos.user_x_management.RegisterUserXResponse();
  }

  public static org.davincischools.leo.protos.user_x_management.RegisterUserXResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RegisterUserXResponse>
      PARSER = new com.google.protobuf.AbstractParser<RegisterUserXResponse>() {
    @java.lang.Override
    public RegisterUserXResponse parsePartialFrom(
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

  public static com.google.protobuf.Parser<RegisterUserXResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RegisterUserXResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.davincischools.leo.protos.user_x_management.RegisterUserXResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

