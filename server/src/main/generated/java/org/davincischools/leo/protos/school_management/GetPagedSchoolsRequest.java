// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: school_management.proto

package org.davincischools.leo.protos.school_management;

/**
 * Protobuf type {@code school_management.GetPagedSchoolsRequest}
 */
public final class GetPagedSchoolsRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:school_management.GetPagedSchoolsRequest)
    GetPagedSchoolsRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use GetPagedSchoolsRequest.newBuilder() to construct.
  private GetPagedSchoolsRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private GetPagedSchoolsRequest() {
    searchText_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new GetPagedSchoolsRequest();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.davincischools.leo.protos.school_management.SchoolManagement.internal_static_school_management_GetPagedSchoolsRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.davincischools.leo.protos.school_management.SchoolManagement.internal_static_school_management_GetPagedSchoolsRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest.class, org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest.Builder.class);
  }

  private int bitField0_;
  public static final int DISTRICT_ID_FIELD_NUMBER = 1;
  private int districtId_ = 0;
  /**
   * <code>optional int32 district_id = 1;</code>
   * @return Whether the districtId field is set.
   */
  @java.lang.Override
  public boolean hasDistrictId() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional int32 district_id = 1;</code>
   * @return The districtId.
   */
  @java.lang.Override
  public int getDistrictId() {
    return districtId_;
  }

  public static final int PAGE_FIELD_NUMBER = 2;
  private int page_ = 0;
  /**
   * <pre>
   * Zero-based page.
   * </pre>
   *
   * <code>optional int32 page = 2;</code>
   * @return Whether the page field is set.
   */
  @java.lang.Override
  public boolean hasPage() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <pre>
   * Zero-based page.
   * </pre>
   *
   * <code>optional int32 page = 2;</code>
   * @return The page.
   */
  @java.lang.Override
  public int getPage() {
    return page_;
  }

  public static final int PAGE_SIZE_FIELD_NUMBER = 3;
  private int pageSize_ = 0;
  /**
   * <code>optional int32 page_size = 3;</code>
   * @return Whether the pageSize field is set.
   */
  @java.lang.Override
  public boolean hasPageSize() {
    return ((bitField0_ & 0x00000004) != 0);
  }
  /**
   * <code>optional int32 page_size = 3;</code>
   * @return The pageSize.
   */
  @java.lang.Override
  public int getPageSize() {
    return pageSize_;
  }

  public static final int SEARCH_TEXT_FIELD_NUMBER = 4;
  @SuppressWarnings("serial")
  private volatile java.lang.Object searchText_ = "";
  /**
   * <code>optional string search_text = 4;</code>
   * @return Whether the searchText field is set.
   */
  @java.lang.Override
  public boolean hasSearchText() {
    return ((bitField0_ & 0x00000008) != 0);
  }
  /**
   * <code>optional string search_text = 4;</code>
   * @return The searchText.
   */
  @java.lang.Override
  public java.lang.String getSearchText() {
    java.lang.Object ref = searchText_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      searchText_ = s;
      return s;
    }
  }
  /**
   * <code>optional string search_text = 4;</code>
   * @return The bytes for searchText.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getSearchTextBytes() {
    java.lang.Object ref = searchText_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      searchText_ = b;
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
      output.writeInt32(1, districtId_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeInt32(2, page_);
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      output.writeInt32(3, pageSize_);
    }
    if (((bitField0_ & 0x00000008) != 0)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, searchText_);
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
        .computeInt32Size(1, districtId_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, page_);
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, pageSize_);
    }
    if (((bitField0_ & 0x00000008) != 0)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, searchText_);
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
    if (!(obj instanceof org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest)) {
      return super.equals(obj);
    }
    org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest other = (org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest) obj;

    if (hasDistrictId() != other.hasDistrictId()) return false;
    if (hasDistrictId()) {
      if (getDistrictId()
          != other.getDistrictId()) return false;
    }
    if (hasPage() != other.hasPage()) return false;
    if (hasPage()) {
      if (getPage()
          != other.getPage()) return false;
    }
    if (hasPageSize() != other.hasPageSize()) return false;
    if (hasPageSize()) {
      if (getPageSize()
          != other.getPageSize()) return false;
    }
    if (hasSearchText() != other.hasSearchText()) return false;
    if (hasSearchText()) {
      if (!getSearchText()
          .equals(other.getSearchText())) return false;
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
    if (hasDistrictId()) {
      hash = (37 * hash) + DISTRICT_ID_FIELD_NUMBER;
      hash = (53 * hash) + getDistrictId();
    }
    if (hasPage()) {
      hash = (37 * hash) + PAGE_FIELD_NUMBER;
      hash = (53 * hash) + getPage();
    }
    if (hasPageSize()) {
      hash = (37 * hash) + PAGE_SIZE_FIELD_NUMBER;
      hash = (53 * hash) + getPageSize();
    }
    if (hasSearchText()) {
      hash = (37 * hash) + SEARCH_TEXT_FIELD_NUMBER;
      hash = (53 * hash) + getSearchText().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest parseFrom(
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
  public static Builder newBuilder(org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest prototype) {
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
   * Protobuf type {@code school_management.GetPagedSchoolsRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:school_management.GetPagedSchoolsRequest)
      org.davincischools.leo.protos.school_management.GetPagedSchoolsRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.davincischools.leo.protos.school_management.SchoolManagement.internal_static_school_management_GetPagedSchoolsRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.davincischools.leo.protos.school_management.SchoolManagement.internal_static_school_management_GetPagedSchoolsRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest.class, org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest.Builder.class);
    }

    // Construct using org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest.newBuilder()
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
      districtId_ = 0;
      page_ = 0;
      pageSize_ = 0;
      searchText_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.davincischools.leo.protos.school_management.SchoolManagement.internal_static_school_management_GetPagedSchoolsRequest_descriptor;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest getDefaultInstanceForType() {
      return org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest.getDefaultInstance();
    }

    @java.lang.Override
    public org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest build() {
      org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest buildPartial() {
      org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest result = new org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.districtId_ = districtId_;
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.page_ = page_;
        to_bitField0_ |= 0x00000002;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.pageSize_ = pageSize_;
        to_bitField0_ |= 0x00000004;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.searchText_ = searchText_;
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
      if (other instanceof org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest) {
        return mergeFrom((org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest other) {
      if (other == org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest.getDefaultInstance()) return this;
      if (other.hasDistrictId()) {
        setDistrictId(other.getDistrictId());
      }
      if (other.hasPage()) {
        setPage(other.getPage());
      }
      if (other.hasPageSize()) {
        setPageSize(other.getPageSize());
      }
      if (other.hasSearchText()) {
        searchText_ = other.searchText_;
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
              districtId_ = input.readInt32();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 16: {
              page_ = input.readInt32();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 24: {
              pageSize_ = input.readInt32();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 34: {
              searchText_ = input.readStringRequireUtf8();
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

    private int districtId_ ;
    /**
     * <code>optional int32 district_id = 1;</code>
     * @return Whether the districtId field is set.
     */
    @java.lang.Override
    public boolean hasDistrictId() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>optional int32 district_id = 1;</code>
     * @return The districtId.
     */
    @java.lang.Override
    public int getDistrictId() {
      return districtId_;
    }
    /**
     * <code>optional int32 district_id = 1;</code>
     * @param value The districtId to set.
     * @return This builder for chaining.
     */
    public Builder setDistrictId(int value) {

      districtId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 district_id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearDistrictId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      districtId_ = 0;
      onChanged();
      return this;
    }

    private int page_ ;
    /**
     * <pre>
     * Zero-based page.
     * </pre>
     *
     * <code>optional int32 page = 2;</code>
     * @return Whether the page field is set.
     */
    @java.lang.Override
    public boolean hasPage() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <pre>
     * Zero-based page.
     * </pre>
     *
     * <code>optional int32 page = 2;</code>
     * @return The page.
     */
    @java.lang.Override
    public int getPage() {
      return page_;
    }
    /**
     * <pre>
     * Zero-based page.
     * </pre>
     *
     * <code>optional int32 page = 2;</code>
     * @param value The page to set.
     * @return This builder for chaining.
     */
    public Builder setPage(int value) {

      page_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Zero-based page.
     * </pre>
     *
     * <code>optional int32 page = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearPage() {
      bitField0_ = (bitField0_ & ~0x00000002);
      page_ = 0;
      onChanged();
      return this;
    }

    private int pageSize_ ;
    /**
     * <code>optional int32 page_size = 3;</code>
     * @return Whether the pageSize field is set.
     */
    @java.lang.Override
    public boolean hasPageSize() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>optional int32 page_size = 3;</code>
     * @return The pageSize.
     */
    @java.lang.Override
    public int getPageSize() {
      return pageSize_;
    }
    /**
     * <code>optional int32 page_size = 3;</code>
     * @param value The pageSize to set.
     * @return This builder for chaining.
     */
    public Builder setPageSize(int value) {

      pageSize_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 page_size = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearPageSize() {
      bitField0_ = (bitField0_ & ~0x00000004);
      pageSize_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object searchText_ = "";
    /**
     * <code>optional string search_text = 4;</code>
     * @return Whether the searchText field is set.
     */
    public boolean hasSearchText() {
      return ((bitField0_ & 0x00000008) != 0);
    }
    /**
     * <code>optional string search_text = 4;</code>
     * @return The searchText.
     */
    public java.lang.String getSearchText() {
      java.lang.Object ref = searchText_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        searchText_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string search_text = 4;</code>
     * @return The bytes for searchText.
     */
    public com.google.protobuf.ByteString
        getSearchTextBytes() {
      java.lang.Object ref = searchText_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        searchText_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string search_text = 4;</code>
     * @param value The searchText to set.
     * @return This builder for chaining.
     */
    public Builder setSearchText(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      searchText_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>optional string search_text = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearSearchText() {
      searchText_ = getDefaultInstance().getSearchText();
      bitField0_ = (bitField0_ & ~0x00000008);
      onChanged();
      return this;
    }
    /**
     * <code>optional string search_text = 4;</code>
     * @param value The bytes for searchText to set.
     * @return This builder for chaining.
     */
    public Builder setSearchTextBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      searchText_ = value;
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


    // @@protoc_insertion_point(builder_scope:school_management.GetPagedSchoolsRequest)
  }

  // @@protoc_insertion_point(class_scope:school_management.GetPagedSchoolsRequest)
  private static final org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest();
  }

  public static org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<GetPagedSchoolsRequest>
      PARSER = new com.google.protobuf.AbstractParser<GetPagedSchoolsRequest>() {
    @java.lang.Override
    public GetPagedSchoolsRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<GetPagedSchoolsRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<GetPagedSchoolsRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.davincischools.leo.protos.school_management.GetPagedSchoolsRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

