package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
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
@Entity(name = Tag.ENTITY_NAME)
@Table(name = Tag.TABLE_NAME, schema = "leo_test")
public class Tag implements Serializable {

  public static final String ENTITY_NAME = "Tag";
  public static final String TABLE_NAME = "tag";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_TEXT_NAME = "text";
  private static final long serialVersionUID = 7205730829734460024L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String text;

  private UserX userX;

  private Project project;

  private ProjectPost projectPost;

  private ProjectPostComment projectPostComment;

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

  @Column(name = COLUMN_TEXT_NAME, nullable = false, length = 32)
  public String getText() {
    return text;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  public UserX getUserX() {
    return userX;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  public Project getProject() {
    return project;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_post_id")
  public ProjectPost getProjectPost() {
    return projectPost;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_post_comment_id")
  public ProjectPostComment getProjectPostComment() {
    return projectPostComment;
  }
}
