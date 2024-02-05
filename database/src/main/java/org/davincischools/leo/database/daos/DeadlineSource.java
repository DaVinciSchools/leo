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
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = DeadlineSource.ENTITY_NAME)
@Table(name = DeadlineSource.TABLE_NAME, schema = "leo_test")
public class DeadlineSource implements Serializable {

  public static final String ENTITY_NAME = "DeadlineSource";
  public static final String TABLE_NAME = "deadline_source";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_STARTTIME_NAME = "start_time";
  public static final String COLUMN_DEADLINETIME_NAME = "deadline_time";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_REQUIREMENTTYPE_NAME = "requirement_type";
  public static final String COLUMN_REQUIREMENTCOUNT_NAME = "requirement_count";
  public static final String COLUMN_REPEATTYPE_NAME = "repeat_type";
  public static final String COLUMN_REPEATFREQUENCYMS_NAME = "repeat_frequency_ms";
  public static final String COLUMN_REPEATCOUNT_NAME = "repeat_count";
  @Serial private static final long serialVersionUID = -1789499411976484554L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Instant startTime;

  private Instant deadlineTime;

  private String name;

  private String longDescrHtml;

  private RequirementType requirementType;

  private Integer requirementCount;

  private RepeatType repeatType;

  private Integer repeatFrequencyMs;

  private Integer repeatCount;

  private UserX userX;

  private Assignment assignment;

  private Set<Deadline> deadlines = new LinkedHashSet<>();

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

  @Column(name = COLUMN_STARTTIME_NAME, nullable = false)
  public Instant getStartTime() {
    return startTime;
  }

  @Column(name = COLUMN_DEADLINETIME_NAME, nullable = false)
  public Instant getDeadlineTime() {
    return deadlineTime;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false, length = 1024)
  public String getName() {
    return name;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
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

  @OneToMany(mappedBy = "deadlineSource")
  public Set<Deadline> getDeadlines() {
    return deadlines;
  }

  public enum RepeatType {
    COUNT,
    END_TIME
  }

  public enum RequirementType {
    ASSIGNMENT,
    PROJECT,
    PROJECT_POST,
    PROJECT_POST_COMMENT
  }
}
