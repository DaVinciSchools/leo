package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = Log.ENTITY_NAME)
@Table(name = Log.TABLE_NAME, schema = "leo_temp")
public class Log implements Serializable {

  public static final String ENTITY_NAME = "Log";
  public static final String TABLE_NAME = "log";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_STATUS_NAME = "status";
  public static final String COLUMN_NOTES_NAME = "notes";
  public static final String COLUMN_CALLER_NAME = "caller";
  public static final String COLUMN_REQUEST_NAME = "request";
  public static final String COLUMN_REQUESTTYPE_NAME = "request_type";
  public static final String COLUMN_REQUESTTIME_NAME = "request_time";
  public static final String COLUMN_INITIALRESPONSE_NAME = "initial_response";
  public static final String COLUMN_INITIALRESPONSETYPE_NAME = "initial_response_type";
  public static final String COLUMN_INITIALRESPONSETIME_NAME = "initial_response_time";
  public static final String COLUMN_FINALRESPONSE_NAME = "final_response";
  public static final String COLUMN_FINALRESPONSETYPE_NAME = "final_response_type";
  public static final String COLUMN_FINALRESPONSETIME_NAME = "final_response_time";
  public static final String COLUMN_STACKTRACE_NAME = "stack_trace";
  public static final String COLUMN_LASTINPUT_NAME = "last_input";
  public static final String COLUMN_LASTINPUTTYPE_NAME = "last_input_type";
  public static final String COLUMN_LASTINPUTTIME_NAME = "last_input_time";
  private static final long serialVersionUID = -1127760289233684756L;

  private Integer id;

  private Instant creationTime;

  private UserX userX;

  private String status;

  private String notes;

  private String caller;

  private String request;

  private String requestType;

  private Instant requestTime;

  private String initialResponse;

  private String initialResponseType;

  private Instant initialResponseTime;

  private String finalResponse;

  private String finalResponseType;

  private Instant finalResponseTime;

  private String stackTrace;

  private String lastInput;

  private String lastInputType;

  private Instant lastInputTime;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Log setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Log setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_x_id")
  public UserX getUserX() {
    return userX;
  }

  public Log setUserX(UserX userX) {
    this.userX = userX;
    return this;
  }

  @Lob
  @Column(name = COLUMN_STATUS_NAME, nullable = false)
  public String getStatus() {
    return status;
  }

  public Log setStatus(String status) {
    this.status = status;
    return this;
  }

  @Lob
  @Column(name = COLUMN_NOTES_NAME)
  public String getNotes() {
    return notes;
  }

  public Log setNotes(String notes) {
    this.notes = notes;
    return this;
  }

  @Lob
  @Column(name = COLUMN_CALLER_NAME)
  public String getCaller() {
    return caller;
  }

  public Log setCaller(String caller) {
    this.caller = caller;
    return this;
  }

  @Lob
  @Column(name = COLUMN_REQUEST_NAME, nullable = false)
  public String getRequest() {
    return request;
  }

  public Log setRequest(String request) {
    this.request = request;
    return this;
  }

  @Lob
  @Column(name = COLUMN_REQUESTTYPE_NAME, nullable = false)
  public String getRequestType() {
    return requestType;
  }

  public Log setRequestType(String requestType) {
    this.requestType = requestType;
    return this;
  }

  @Column(name = COLUMN_REQUESTTIME_NAME, nullable = false)
  public Instant getRequestTime() {
    return requestTime;
  }

  public Log setRequestTime(Instant requestTime) {
    this.requestTime = requestTime;
    return this;
  }

  @Lob
  @Column(name = COLUMN_INITIALRESPONSE_NAME)
  public String getInitialResponse() {
    return initialResponse;
  }

  public Log setInitialResponse(String initialResponse) {
    this.initialResponse = initialResponse;
    return this;
  }

  @Lob
  @Column(name = COLUMN_INITIALRESPONSETYPE_NAME)
  public String getInitialResponseType() {
    return initialResponseType;
  }

  public Log setInitialResponseType(String initialResponseType) {
    this.initialResponseType = initialResponseType;
    return this;
  }

  @Column(name = COLUMN_INITIALRESPONSETIME_NAME)
  public Instant getInitialResponseTime() {
    return initialResponseTime;
  }

  public Log setInitialResponseTime(Instant initialResponseTime) {
    this.initialResponseTime = initialResponseTime;
    return this;
  }

  @Lob
  @Column(name = COLUMN_FINALRESPONSE_NAME)
  public String getFinalResponse() {
    return finalResponse;
  }

  public Log setFinalResponse(String finalResponse) {
    this.finalResponse = finalResponse;
    return this;
  }

  @Lob
  @Column(name = COLUMN_FINALRESPONSETYPE_NAME)
  public String getFinalResponseType() {
    return finalResponseType;
  }

  public Log setFinalResponseType(String finalResponseType) {
    this.finalResponseType = finalResponseType;
    return this;
  }

  @Column(name = COLUMN_FINALRESPONSETIME_NAME, nullable = false)
  public Instant getFinalResponseTime() {
    return finalResponseTime;
  }

  public Log setFinalResponseTime(Instant finalResponseTime) {
    this.finalResponseTime = finalResponseTime;
    return this;
  }

  @Lob
  @Column(name = COLUMN_STACKTRACE_NAME)
  public String getStackTrace() {
    return stackTrace;
  }

  public Log setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LASTINPUT_NAME)
  public String getLastInput() {
    return lastInput;
  }

  public Log setLastInput(String lastInput) {
    this.lastInput = lastInput;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LASTINPUTTYPE_NAME)
  public String getLastInputType() {
    return lastInputType;
  }

  public Log setLastInputType(String lastInputType) {
    this.lastInputType = lastInputType;
    return this;
  }

  @Column(name = COLUMN_LASTINPUTTIME_NAME)
  public Instant getLastInputTime() {
    return lastInputTime;
  }

  public Log setLastInputTime(Instant lastInputTime) {
    this.lastInputTime = lastInputTime;
    return this;
  }
}
