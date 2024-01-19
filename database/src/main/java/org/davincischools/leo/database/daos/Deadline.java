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
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
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
@Entity(name = Deadline.ENTITY_NAME)
@Table(name = Deadline.TABLE_NAME, schema = "leo_test")
public class Deadline implements Serializable {

  public static final String ENTITY_NAME = "Deadline";
  public static final String TABLE_NAME = "deadline";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_DEADLINE_NAME = "deadline";
  public static final String COLUMN_ENDDEADLINEDATE_NAME = "end_deadline_date";
  public static final String COLUMN_REQUIREMENTTYPE_NAME = "requirement_type";
  public static final String COLUMN_REQUIREMENTCOUNT_NAME = "requirement_count";
  public static final String COLUMN_REPEATTYPE_NAME = "repeat_type";
  public static final String COLUMN_REPEATFREQUENCYMS_NAME = "repeat_frequency_ms";
  public static final String COLUMN_REPEATCOUNT_NAME = "repeat_count";
  public static final String COLUMN_REPEATENDDATE_NAME = "repeat_end_date";
  private static final long serialVersionUID = -3329375378199229790L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String title;

  private String longDescrHtml;

  private Instant deadline;

  private Instant endDeadlineDate;

  private RequirementType requirementType;

  private Integer requirementCount;

  private RepeatType repeatType;

  private Integer repeatFrequencyMs;

  private Integer repeatCount;

  private Instant repeatEndDate;

  private UserX userX;

  private Assignment assignment;

  private Set<DeadlineStatus> deadlineStatuses = new LinkedHashSet<>();

  private Set<Notification> notifications = new LinkedHashSet<>();

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

  @Column(name = COLUMN_DEADLINE_NAME, nullable = false)
  public Instant getDeadline() {
    return deadline;
  }

  @Column(name = COLUMN_ENDDEADLINEDATE_NAME, nullable = false)
  public Instant getEndDeadlineDate() {
    return endDeadlineDate;
  }

  @Lob
  @Enumerated(EnumType.STRING)
  @Column(name = COLUMN_REQUIREMENTTYPE_NAME, nullable = false)
  public RequirementType getRequirementType() {
    return requirementType;
  }

  @Column(name = COLUMN_REQUIREMENTCOUNT_NAME, nullable = false)
  public Integer getRequirementCount() {
    return requirementCount;
  }

  @Lob
  @Enumerated(EnumType.STRING)
  @Column(name = COLUMN_REPEATTYPE_NAME)
  public RepeatType getRepeatType() {
    return repeatType;
  }

  @Column(name = COLUMN_REPEATFREQUENCYMS_NAME)
  public Integer getRepeatFrequencyMs() {
    return repeatFrequencyMs;
  }

  @Column(name = COLUMN_REPEATCOUNT_NAME)
  public Integer getRepeatCount() {
    return repeatCount;
  }

  @Column(name = COLUMN_REPEATENDDATE_NAME)
  public Instant getRepeatEndDate() {
    return repeatEndDate;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  public UserX getUserX() {
    return userX;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  @PropagateDeleteFrom
  public Assignment getAssignment() {
    return assignment;
  }

  @OneToMany(mappedBy = "deadline")
  public Set<DeadlineStatus> getDeadlineStatuses() {
    return deadlineStatuses;
  }

  @OneToMany(mappedBy = "deadline")
  public Set<Notification> getNotifications() {
    return notifications;
  }

  public enum RepeatType {
    COUNT,
    END_DATE
  }

  public enum RequirementType {
    ASSIGNMENT,
    PROJECT,
    PROJECT_POST,
    PROJECT_POST_COMMENT
  }
}
