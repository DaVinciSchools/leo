package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Entity(name = CommentX.ENTITY_NAME)
@Table(
    name = CommentX.TABLE_NAME,
    schema = "leo_test",
    indexes = {@Index(name = "long_descr_text", columnList = "long_descr_text")})
public class CommentX implements Serializable {

  public static final String ENTITY_NAME = "CommentX";
  public static final String TABLE_NAME = "comment_x";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_LASTUPDATED_NAME = "last_updated";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_LONGDESCRTEXT_NAME = "long_descr_text";
  private static final long serialVersionUID = 4780402062151491025L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Instant lastUpdated;

  private String longDescrHtml;

  private String longDescrText;

  private UserX userX;

  private Post post;

  private Project project;

  private Set<DeadlineStatus> deadlineStatuses = new LinkedHashSet<>();

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

  @Column(name = COLUMN_LASTUPDATED_NAME, nullable = false)
  public Instant getLastUpdated() {
    return lastUpdated;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME, nullable = false)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRTEXT_NAME, nullable = false)
  public String getLongDescrText() {
    return longDescrText;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  @PropagateDeleteFrom
  public UserX getUserX() {
    return userX;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  @PropagateDeleteFrom
  public Post getPost() {
    return post;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  @PropagateDeleteFrom
  public Project getProject() {
    return project;
  }

  @OneToMany(mappedBy = "commentX")
  public Set<DeadlineStatus> getDeadlineStatuses() {
    return deadlineStatuses;
  }
}
