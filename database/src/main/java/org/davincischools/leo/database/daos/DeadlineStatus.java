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
import org.davincischools.leo.database.daos.Notification.StatusType;

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
  public static final String COLUMN_ASSIGNMENT_NAME = "assignment";
  private static final long serialVersionUID = 7904148426487768279L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private StatusType status;

  private UserX userX;

  private Deadline deadline;

  private Boolean assignment;

  private Project project;

  private Post post;

  private CommentX commentX;

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

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  public UserX getUserX() {
    return userX;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "deadline_id", nullable = false)
  @PropagateDeleteFrom
  public Deadline getDeadline() {
    return deadline;
  }

  @Column(name = COLUMN_ASSIGNMENT_NAME)
  public Boolean getAssignment() {
    return assignment;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  public Project getProject() {
    return project;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  public Post getPost() {
    return post;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_x_id")
  public CommentX getCommentX() {
    return commentX;
  }
}
