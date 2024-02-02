package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Entity(name = Deadline.ENTITY_NAME)
@Table(name = Deadline.TABLE_NAME, schema = "leo_test")
public class Deadline implements Serializable {

  public static final String ENTITY_NAME = "Deadline";
  public static final String TABLE_NAME = "deadline";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_SOURCEDEADLINEINDEX_NAME = "source_deadline_index";
  public static final String COLUMN_STARTTIME_NAME = "start_time";
  public static final String COLUMN_DEADLINETIME_NAME = "deadline_time";
  @Serial private static final long serialVersionUID = -1148831749684806852L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Integer sourceDeadlineIndex;

  private Instant startTime;

  private Instant deadlineTime;

  private DeadlineSource deadlineSource;

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

  @Column(name = COLUMN_SOURCEDEADLINEINDEX_NAME, nullable = false)
  public Integer getSourceDeadlineIndex() {
    return sourceDeadlineIndex;
  }

  @Column(name = COLUMN_STARTTIME_NAME, nullable = false)
  public Instant getStartTime() {
    return startTime;
  }

  @Column(name = COLUMN_DEADLINETIME_NAME, nullable = false)
  public Instant getDeadlineTime() {
    return deadlineTime;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "deadline_source_id", nullable = false)
  @PropagateDeleteFrom
  public DeadlineSource getDeadlineSource() {
    return deadlineSource;
  }

  @OneToMany(mappedBy = "deadline")
  public Set<DeadlineStatus> getDeadlineStatuses() {
    return deadlineStatuses;
  }

  @OneToMany(mappedBy = "deadline")
  public Set<Notification> getNotifications() {
    return notifications;
  }
}
