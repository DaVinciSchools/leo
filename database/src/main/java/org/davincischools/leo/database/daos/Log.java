package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = Log.ENTITY_NAME)
@Table(name = Log.TABLE_NAME, schema = "leo_test")
public class Log implements Serializable {

  public static final String ENTITY_NAME = "Log";
  public static final String TABLE_NAME = "log";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_STATUS_NAME = "status";
  public static final String COLUMN_NOTES_NAME = "notes";
  public static final String COLUMN_ISSUELINK_NAME = "issue_link";
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
  @Serial private static final long serialVersionUID = -5323470866745018909L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private UserX userX;

  private StatusType status;

  private String notes;

  private String issueLink;

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

  private Set<LogReference> logReferences = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_x_id")
  public UserX getUserX() {
    return userX;
  }

  @Lob
  @Enumerated(EnumType.STRING)
  @Column(name = COLUMN_STATUS_NAME, nullable = false)
  public StatusType getStatus() {
    return status;
  }

  @Lob
  @Column(name = COLUMN_NOTES_NAME)
  public String getNotes() {
    return notes;
  }

  @Column(name = COLUMN_ISSUELINK_NAME)
  public String getIssueLink() {
    return issueLink;
  }

  @Lob
  @Column(name = COLUMN_CALLER_NAME)
  public String getCaller() {
    return caller;
  }

  @Lob
  @Column(name = COLUMN_REQUEST_NAME, nullable = false)
  public String getRequest() {
    return request;
  }

  @Lob
  @Column(name = COLUMN_REQUESTTYPE_NAME, nullable = false)
  public String getRequestType() {
    return requestType;
  }

  @Column(name = COLUMN_REQUESTTIME_NAME, nullable = false)
  public Instant getRequestTime() {
    return requestTime;
  }

  @Lob
  @Column(name = COLUMN_INITIALRESPONSE_NAME)
  public String getInitialResponse() {
    return initialResponse;
  }

  @Lob
  @Column(name = COLUMN_INITIALRESPONSETYPE_NAME)
  public String getInitialResponseType() {
    return initialResponseType;
  }

  @Column(name = COLUMN_INITIALRESPONSETIME_NAME)
  public Instant getInitialResponseTime() {
    return initialResponseTime;
  }

  @Lob
  @Column(name = COLUMN_FINALRESPONSE_NAME)
  public String getFinalResponse() {
    return finalResponse;
  }

  @Lob
  @Column(name = COLUMN_FINALRESPONSETYPE_NAME)
  public String getFinalResponseType() {
    return finalResponseType;
  }

  @Column(name = COLUMN_FINALRESPONSETIME_NAME, nullable = false)
  public Instant getFinalResponseTime() {
    return finalResponseTime;
  }

  @Lob
  @Column(name = COLUMN_STACKTRACE_NAME)
  public String getStackTrace() {
    return stackTrace;
  }

  @Lob
  @Column(name = COLUMN_LASTINPUT_NAME)
  public String getLastInput() {
    return lastInput;
  }

  @Lob
  @Column(name = COLUMN_LASTINPUTTYPE_NAME)
  public String getLastInputType() {
    return lastInputType;
  }

  @Column(name = COLUMN_LASTINPUTTIME_NAME)
  public Instant getLastInputTime() {
    return lastInputTime;
  }

  @OneToMany(mappedBy = "log")
  public Set<LogReference> getLogReferences() {
    return logReferences;
  }

  public enum StatusType {
    SUCCESS,
    ERROR
  }
}
