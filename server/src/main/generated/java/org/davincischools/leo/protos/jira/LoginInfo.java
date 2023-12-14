// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: jira.proto

package org.davincischools.leo.protos.jira;

/**
 * Protobuf type {@code jira.LoginInfo}
 */
public final class LoginInfo extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:jira.LoginInfo)
    LoginInfoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use LoginInfo.newBuilder() to construct.
  private LoginInfo(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private LoginInfo() {
    lastFailedLoginTime_ = "";
    previousLoginTime_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new LoginInfo();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.davincischools.leo.protos.jira.Jira.internal_static_jira_LoginInfo_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.davincischools.leo.protos.jira.Jira.internal_static_jira_LoginInfo_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.davincischools.leo.protos.jira.LoginInfo.class, org.davincischools.leo.protos.jira.LoginInfo.Builder.class);
  }

  private int bitField0_;
  public static final int FAILED_LOGIN_COUNT_FIELD_NUMBER = 1;
  private int failedLoginCount_ = 0;
  /**
   * <code>optional int32 failed_login_count = 1;</code>
   * @return Whether the failedLoginCount field is set.
   */
  @java.lang.Override
  public boolean hasFailedLoginCount() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional int32 failed_login_count = 1;</code>
   * @return The failedLoginCount.
   */
  @java.lang.Override
  public int getFailedLoginCount() {
    return failedLoginCount_;
  }

  public static final int LOGIN_COUNT_FIELD_NUMBER = 2;
  private int loginCount_ = 0;
  /**
   * <code>optional int32 login_count = 2;</code>
   * @return Whether the loginCount field is set.
   */
  @java.lang.Override
  public boolean hasLoginCount() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>optional int32 login_count = 2;</code>
   * @return The loginCount.
   */
  @java.lang.Override
  public int getLoginCount() {
    return loginCount_;
  }

  public static final int LAST_FAILED_LOGIN_TIME_FIELD_NUMBER = 3;
  @SuppressWarnings("serial")
  private volatile java.lang.Object lastFailedLoginTime_ = "";
  /**
   * <code>optional string last_failed_login_time = 3;</code>
   * @return Whether the lastFailedLoginTime field is set.
   */
  @java.lang.Override
  public boolean hasLastFailedLoginTime() {
    return ((bitField0_ & 0x00000004) != 0);
  }
  /**
   * <code>optional string last_failed_login_time = 3;</code>
   * @return The lastFailedLoginTime.
   */
  @java.lang.Override
  public java.lang.String getLastFailedLoginTime() {
    java.lang.Object ref = lastFailedLoginTime_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      lastFailedLoginTime_ = s;
      return s;
    }
  }
  /**
   * <code>optional string last_failed_login_time = 3;</code>
   * @return The bytes for lastFailedLoginTime.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getLastFailedLoginTimeBytes() {
    java.lang.Object ref = lastFailedLoginTime_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      lastFailedLoginTime_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PREVIOUS_LOGIN_TIME_FIELD_NUMBER = 4;
  @SuppressWarnings("serial")
  private volatile java.lang.Object previousLoginTime_ = "";
  /**
   * <code>optional string previous_login_time = 4;</code>
   * @return Whether the previousLoginTime field is set.
   */
  @java.lang.Override
  public boolean hasPreviousLoginTime() {
    return ((bitField0_ & 0x00000008) != 0);
  }
  /**
   * <code>optional string previous_login_time = 4;</code>
   * @return The previousLoginTime.
   */
  @java.lang.Override
  public java.lang.String getPreviousLoginTime() {
    java.lang.Object ref = previousLoginTime_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      previousLoginTime_ = s;
      return s;
    }
  }
  /**
   * <code>optional string previous_login_time = 4;</code>
   * @return The bytes for previousLoginTime.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getPreviousLoginTimeBytes() {
    java.lang.Object ref = previousLoginTime_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      previousLoginTime_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
      output.writeInt32(1, failedLoginCount_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeInt32(2, loginCount_);
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, lastFailedLoginTime_);
    }
    if (((bitField0_ & 0x00000008) != 0)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, previousLoginTime_);
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
        .computeInt32Size(1, failedLoginCount_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, loginCount_);
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, lastFailedLoginTime_);
    }
    if (((bitField0_ & 0x00000008) != 0)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, previousLoginTime_);
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
    if (!(obj instanceof org.davincischools.leo.protos.jira.LoginInfo)) {
      return super.equals(obj);
    }
    org.davincischools.leo.protos.jira.LoginInfo other = (org.davincischools.leo.protos.jira.LoginInfo) obj;

    if (hasFailedLoginCount() != other.hasFailedLoginCount()) return false;
    if (hasFailedLoginCount()) {
      if (getFailedLoginCount()
          != other.getFailedLoginCount()) return false;
    }
    if (hasLoginCount() != other.hasLoginCount()) return false;
    if (hasLoginCount()) {
      if (getLoginCount()
          != other.getLoginCount()) return false;
    }
    if (hasLastFailedLoginTime() != other.hasLastFailedLoginTime()) return false;
    if (hasLastFailedLoginTime()) {
      if (!getLastFailedLoginTime()
          .equals(other.getLastFailedLoginTime())) return false;
    }
    if (hasPreviousLoginTime() != other.hasPreviousLoginTime()) return false;
    if (hasPreviousLoginTime()) {
      if (!getPreviousLoginTime()
          .equals(other.getPreviousLoginTime())) return false;
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
    if (hasFailedLoginCount()) {
      hash = (37 * hash) + FAILED_LOGIN_COUNT_FIELD_NUMBER;
      hash = (53 * hash) + getFailedLoginCount();
    }
    if (hasLoginCount()) {
      hash = (37 * hash) + LOGIN_COUNT_FIELD_NUMBER;
      hash = (53 * hash) + getLoginCount();
    }
    if (hasLastFailedLoginTime()) {
      hash = (37 * hash) + LAST_FAILED_LOGIN_TIME_FIELD_NUMBER;
      hash = (53 * hash) + getLastFailedLoginTime().hashCode();
    }
    if (hasPreviousLoginTime()) {
      hash = (37 * hash) + PREVIOUS_LOGIN_TIME_FIELD_NUMBER;
      hash = (53 * hash) + getPreviousLoginTime().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static org.davincischools.leo.protos.jira.LoginInfo parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static org.davincischools.leo.protos.jira.LoginInfo parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.jira.LoginInfo parseFrom(
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
  public static Builder newBuilder(org.davincischools.leo.protos.jira.LoginInfo prototype) {
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
   * Protobuf type {@code jira.LoginInfo}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:jira.LoginInfo)
      org.davincischools.leo.protos.jira.LoginInfoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.davincischools.leo.protos.jira.Jira.internal_static_jira_LoginInfo_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.davincischools.leo.protos.jira.Jira.internal_static_jira_LoginInfo_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.davincischools.leo.protos.jira.LoginInfo.class, org.davincischools.leo.protos.jira.LoginInfo.Builder.class);
    }

    // Construct using org.davincischools.leo.protos.jira.LoginInfo.newBuilder()
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
      failedLoginCount_ = 0;
      loginCount_ = 0;
      lastFailedLoginTime_ = "";
      previousLoginTime_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.davincischools.leo.protos.jira.Jira.internal_static_jira_LoginInfo_descriptor;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.jira.LoginInfo getDefaultInstanceForType() {
      return org.davincischools.leo.protos.jira.LoginInfo.getDefaultInstance();
    }

    @java.lang.Override
    public org.davincischools.leo.protos.jira.LoginInfo build() {
      org.davincischools.leo.protos.jira.LoginInfo result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.jira.LoginInfo buildPartial() {
      org.davincischools.leo.protos.jira.LoginInfo result = new org.davincischools.leo.protos.jira.LoginInfo(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(org.davincischools.leo.protos.jira.LoginInfo result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.failedLoginCount_ = failedLoginCount_;
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.loginCount_ = loginCount_;
        to_bitField0_ |= 0x00000002;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.lastFailedLoginTime_ = lastFailedLoginTime_;
        to_bitField0_ |= 0x00000004;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.previousLoginTime_ = previousLoginTime_;
        to_bitField0_ |= 0x00000008;
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
      if (other instanceof org.davincischools.leo.protos.jira.LoginInfo) {
        return mergeFrom((org.davincischools.leo.protos.jira.LoginInfo)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.davincischools.leo.protos.jira.LoginInfo other) {
      if (other == org.davincischools.leo.protos.jira.LoginInfo.getDefaultInstance()) return this;
      if (other.hasFailedLoginCount()) {
        setFailedLoginCount(other.getFailedLoginCount());
      }
      if (other.hasLoginCount()) {
        setLoginCount(other.getLoginCount());
      }
      if (other.hasLastFailedLoginTime()) {
        lastFailedLoginTime_ = other.lastFailedLoginTime_;
        bitField0_ |= 0x00000004;
        onChanged();
      }
      if (other.hasPreviousLoginTime()) {
        previousLoginTime_ = other.previousLoginTime_;
        bitField0_ |= 0x00000008;
        onChanged();
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
              failedLoginCount_ = input.readInt32();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 16: {
              loginCount_ = input.readInt32();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 26: {
              lastFailedLoginTime_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            case 34: {
              previousLoginTime_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000008;
              break;
            } // case 34
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

    private int failedLoginCount_ ;
    /**
     * <code>optional int32 failed_login_count = 1;</code>
     * @return Whether the failedLoginCount field is set.
     */
    @java.lang.Override
    public boolean hasFailedLoginCount() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>optional int32 failed_login_count = 1;</code>
     * @return The failedLoginCount.
     */
    @java.lang.Override
    public int getFailedLoginCount() {
      return failedLoginCount_;
    }
    /**
     * <code>optional int32 failed_login_count = 1;</code>
     * @param value The failedLoginCount to set.
     * @return This builder for chaining.
     */
    public Builder setFailedLoginCount(int value) {

      failedLoginCount_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 failed_login_count = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearFailedLoginCount() {
      bitField0_ = (bitField0_ & ~0x00000001);
      failedLoginCount_ = 0;
      onChanged();
      return this;
    }

    private int loginCount_ ;
    /**
     * <code>optional int32 login_count = 2;</code>
     * @return Whether the loginCount field is set.
     */
    @java.lang.Override
    public boolean hasLoginCount() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>optional int32 login_count = 2;</code>
     * @return The loginCount.
     */
    @java.lang.Override
    public int getLoginCount() {
      return loginCount_;
    }
    /**
     * <code>optional int32 login_count = 2;</code>
     * @param value The loginCount to set.
     * @return This builder for chaining.
     */
    public Builder setLoginCount(int value) {

      loginCount_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 login_count = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearLoginCount() {
      bitField0_ = (bitField0_ & ~0x00000002);
      loginCount_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object lastFailedLoginTime_ = "";
    /**
     * <code>optional string last_failed_login_time = 3;</code>
     * @return Whether the lastFailedLoginTime field is set.
     */
    public boolean hasLastFailedLoginTime() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>optional string last_failed_login_time = 3;</code>
     * @return The lastFailedLoginTime.
     */
    public java.lang.String getLastFailedLoginTime() {
      java.lang.Object ref = lastFailedLoginTime_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        lastFailedLoginTime_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string last_failed_login_time = 3;</code>
     * @return The bytes for lastFailedLoginTime.
     */
    public com.google.protobuf.ByteString
        getLastFailedLoginTimeBytes() {
      java.lang.Object ref = lastFailedLoginTime_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        lastFailedLoginTime_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string last_failed_login_time = 3;</code>
     * @param value The lastFailedLoginTime to set.
     * @return This builder for chaining.
     */
    public Builder setLastFailedLoginTime(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      lastFailedLoginTime_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>optional string last_failed_login_time = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearLastFailedLoginTime() {
      lastFailedLoginTime_ = getDefaultInstance().getLastFailedLoginTime();
      bitField0_ = (bitField0_ & ~0x00000004);
      onChanged();
      return this;
    }
    /**
     * <code>optional string last_failed_login_time = 3;</code>
     * @param value The bytes for lastFailedLoginTime to set.
     * @return This builder for chaining.
     */
    public Builder setLastFailedLoginTimeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      lastFailedLoginTime_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }

    private java.lang.Object previousLoginTime_ = "";
    /**
     * <code>optional string previous_login_time = 4;</code>
     * @return Whether the previousLoginTime field is set.
     */
    public boolean hasPreviousLoginTime() {
      return ((bitField0_ & 0x00000008) != 0);
    }
    /**
     * <code>optional string previous_login_time = 4;</code>
     * @return The previousLoginTime.
     */
    public java.lang.String getPreviousLoginTime() {
      java.lang.Object ref = previousLoginTime_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        previousLoginTime_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string previous_login_time = 4;</code>
     * @return The bytes for previousLoginTime.
     */
    public com.google.protobuf.ByteString
        getPreviousLoginTimeBytes() {
      java.lang.Object ref = previousLoginTime_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        previousLoginTime_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string previous_login_time = 4;</code>
     * @param value The previousLoginTime to set.
     * @return This builder for chaining.
     */
    public Builder setPreviousLoginTime(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      previousLoginTime_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>optional string previous_login_time = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearPreviousLoginTime() {
      previousLoginTime_ = getDefaultInstance().getPreviousLoginTime();
      bitField0_ = (bitField0_ & ~0x00000008);
      onChanged();
      return this;
    }
    /**
     * <code>optional string previous_login_time = 4;</code>
     * @param value The bytes for previousLoginTime to set.
     * @return This builder for chaining.
     */
    public Builder setPreviousLoginTimeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      previousLoginTime_ = value;
      bitField0_ |= 0x00000008;
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


    // @@protoc_insertion_point(builder_scope:jira.LoginInfo)
  }

  // @@protoc_insertion_point(class_scope:jira.LoginInfo)
  private static final org.davincischools.leo.protos.jira.LoginInfo DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.davincischools.leo.protos.jira.LoginInfo();
  }

  public static org.davincischools.leo.protos.jira.LoginInfo getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<LoginInfo>
      PARSER = new com.google.protobuf.AbstractParser<LoginInfo>() {
    @java.lang.Override
    public LoginInfo parsePartialFrom(
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

  public static com.google.protobuf.Parser<LoginInfo> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<LoginInfo> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.davincischools.leo.protos.jira.LoginInfo getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

