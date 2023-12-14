// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mailjet.proto

package org.davincischools.leo.protos.mailjet;

public interface MailjetSendRequestMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:mailjet.MailjetSendRequestMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.mailjet.MailjetSendRequestFrom From = 1;</code>
   * @return Whether the from field is set.
   */
  boolean hasFrom();
  /**
   * <code>.mailjet.MailjetSendRequestFrom From = 1;</code>
   * @return The from.
   */
  org.davincischools.leo.protos.mailjet.MailjetSendRequestFrom getFrom();
  /**
   * <code>.mailjet.MailjetSendRequestFrom From = 1;</code>
   */
  org.davincischools.leo.protos.mailjet.MailjetSendRequestFromOrBuilder getFromOrBuilder();

  /**
   * <code>repeated .mailjet.MailjetSendRequestTo To = 2;</code>
   */
  java.util.List<org.davincischools.leo.protos.mailjet.MailjetSendRequestTo> 
      getToList();
  /**
   * <code>repeated .mailjet.MailjetSendRequestTo To = 2;</code>
   */
  org.davincischools.leo.protos.mailjet.MailjetSendRequestTo getTo(int index);
  /**
   * <code>repeated .mailjet.MailjetSendRequestTo To = 2;</code>
   */
  int getToCount();
  /**
   * <code>repeated .mailjet.MailjetSendRequestTo To = 2;</code>
   */
  java.util.List<? extends org.davincischools.leo.protos.mailjet.MailjetSendRequestToOrBuilder> 
      getToOrBuilderList();
  /**
   * <code>repeated .mailjet.MailjetSendRequestTo To = 2;</code>
   */
  org.davincischools.leo.protos.mailjet.MailjetSendRequestToOrBuilder getToOrBuilder(
      int index);

  /**
   * <code>optional .mailjet.MailjetSendRequestReplyTo ReplyTo = 3;</code>
   * @return Whether the replyTo field is set.
   */
  boolean hasReplyTo();
  /**
   * <code>optional .mailjet.MailjetSendRequestReplyTo ReplyTo = 3;</code>
   * @return The replyTo.
   */
  org.davincischools.leo.protos.mailjet.MailjetSendRequestReplyTo getReplyTo();
  /**
   * <code>optional .mailjet.MailjetSendRequestReplyTo ReplyTo = 3;</code>
   */
  org.davincischools.leo.protos.mailjet.MailjetSendRequestReplyToOrBuilder getReplyToOrBuilder();

  /**
   * <code>optional string Subject = 4;</code>
   * @return Whether the subject field is set.
   */
  boolean hasSubject();
  /**
   * <code>optional string Subject = 4;</code>
   * @return The subject.
   */
  java.lang.String getSubject();
  /**
   * <code>optional string Subject = 4;</code>
   * @return The bytes for subject.
   */
  com.google.protobuf.ByteString
      getSubjectBytes();

  /**
   * <code>optional string TextPart = 5;</code>
   * @return Whether the textPart field is set.
   */
  boolean hasTextPart();
  /**
   * <code>optional string TextPart = 5;</code>
   * @return The textPart.
   */
  java.lang.String getTextPart();
  /**
   * <code>optional string TextPart = 5;</code>
   * @return The bytes for textPart.
   */
  com.google.protobuf.ByteString
      getTextPartBytes();

  /**
   * <code>optional string HTMLPart = 6;</code>
   * @return Whether the hTMLPart field is set.
   */
  boolean hasHTMLPart();
  /**
   * <code>optional string HTMLPart = 6;</code>
   * @return The hTMLPart.
   */
  java.lang.String getHTMLPart();
  /**
   * <code>optional string HTMLPart = 6;</code>
   * @return The bytes for hTMLPart.
   */
  com.google.protobuf.ByteString
      getHTMLPartBytes();

  /**
   * <code>optional string CustomID = 7;</code>
   * @return Whether the customID field is set.
   */
  boolean hasCustomID();
  /**
   * <code>optional string CustomID = 7;</code>
   * @return The customID.
   */
  java.lang.String getCustomID();
  /**
   * <code>optional string CustomID = 7;</code>
   * @return The bytes for customID.
   */
  com.google.protobuf.ByteString
      getCustomIDBytes();
}
