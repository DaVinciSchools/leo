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
import java.io.Serial;
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
@Entity(name = DeadlineStatus.ENTITY_NAME)
@Table(name = DeadlineStatus.TABLE_NAME, schema = "leo_test")
public class DeadlineStatus implements Serializable {

  public static final String ENTITY_NAME = "DeadlineStatus";
  public static final String TABLE_NAME = "deadline_status";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_STATUS_NAME = "status";
  public static final String COLUMN_METREQUIREMENTCOUNT_NAME = "met_requirement_count";
  @Serial private static final long serialVersionUID = -8771917465439930712L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private StatusType status;

  private Integer metRequirementCount;

  private UserX userX;

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

  @Lob
  @Enumerated(EnumType.STRING)
  @Column(name = COLUMN_STATUS_NAME, nullable = false)
  public StatusType getStatus() {
    return status;
  }

  @Column(name = COLUMN_METREQUIREMENTCOUNT_NAME, nullable = false)
  public Integer getMetRequirementCount() {
    return metRequirementCount;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  @PropagateDeleteFrom
  public UserX getUserX() {
    return userX;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "deadline_id", nullable = false)
  @PropagateDeleteFrom
  public Deadline getDeadline() {
    return deadline;
  }

  public enum StatusType {
    NONE,
    TEACHER_TO_REVIEW,
    STUDENT_TO_REVIEW,
    DONE,
    EXCUSED,
    LATE
  }
}
