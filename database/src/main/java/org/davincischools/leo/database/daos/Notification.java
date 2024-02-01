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
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = Notification.ENTITY_NAME)
@Table(name = Notification.TABLE_NAME, schema = "leo_test")
public class Notification implements Serializable {

  public static final String ENTITY_NAME = "Notification";
  public static final String TABLE_NAME = "notification";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_LINK_NAME = "link";
  public static final String COLUMN_ACKNOWLEDGED_NAME = "acknowledged";
  public static final String COLUMN_POSTDATE_NAME = "post_date";
  public static final String COLUMN_STATUS_NAME = "status";
  public static final String COLUMN_SEVERITY_NAME = "severity";
  public static final String COLUMN_COMPLETEBYDATE_NAME = "complete_by_date";
  public static final String COLUMN_COMPLETEDDATE_NAME = "completed_date";
  public static final String COLUMN_SNOOZEDUNTILDATE_NAME = "snoozed_until_date";
  private static final long serialVersionUID = -559738785454574115L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String title;

  private String longDescrHtml;

  private String link;

  private Boolean acknowledged = false;

  private Instant postDate;

  private StatusType status;

  private SeverityType severity;

  private Instant completeByDate;

  private Instant completedDate;

  private Instant snoozedUntilDate;

  private UserX postedByUserX;

  private UserX postedToUserX;

  private Deadline deadline;

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

  @Column(name = COLUMN_TITLE_NAME, nullable = false, length = 1024)
  public String getTitle() {
    return title;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  @Column(name = COLUMN_LINK_NAME, nullable = false, length = 1024)
  public String getLink() {
    return link;
  }

  @Column(name = COLUMN_ACKNOWLEDGED_NAME, nullable = false)
  public Boolean getAcknowledged() {
    return acknowledged;
  }

  @Column(name = COLUMN_POSTDATE_NAME, nullable = false)
  public Instant getPostDate() {
    return postDate;
  }

  @Lob
  @Enumerated(EnumType.STRING)
  @Column(name = COLUMN_STATUS_NAME, nullable = false)
  public StatusType getStatus() {
    return status;
  }

  @Lob
  @Enumerated(EnumType.STRING)
  @Column(name = COLUMN_SEVERITY_NAME, nullable = false)
  public SeverityType getSeverity() {
    return severity;
  }

  @Column(name = COLUMN_COMPLETEBYDATE_NAME)
  public Instant getCompleteByDate() {
    return completeByDate;
  }

  @Column(name = COLUMN_COMPLETEDDATE_NAME)
  public Instant getCompletedDate() {
    return completedDate;
  }

  @Column(name = COLUMN_SNOOZEDUNTILDATE_NAME)
  public Instant getSnoozedUntilDate() {
    return snoozedUntilDate;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "posted_by_user_x_id")
  @PropagateDeleteFrom
  public UserX getPostedByUserX() {
    return postedByUserX;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "posted_to_user_x_id", nullable = false)
  @PropagateDeleteFrom
  public UserX getPostedToUserX() {
    return postedToUserX;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "deadline_id")
  @PropagateDeleteFrom
  public Deadline getDeadline() {
    return deadline;
  }

  public enum SeverityType {
    INFO,
    NORMAL,
    URGENT
  }

  public enum StatusType {
    PENDING,
    SNOOZED,
    COMPLETED,
    HIDDEN
  }
}
