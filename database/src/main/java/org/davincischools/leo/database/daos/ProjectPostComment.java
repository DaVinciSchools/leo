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

@Entity(name = ProjectPostComment.ENTITY_NAME)
@Table(name = ProjectPostComment.TABLE_NAME, schema = "leo_temp")
public class ProjectPostComment implements Serializable {

  public static final String ENTITY_NAME = "ProjectPostComment";
  public static final String TABLE_NAME = "project_post_comment";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_COMMENTHTML_NAME = "comment_html";
  private static final long serialVersionUID = 5927806603324710803L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String commentHtml;

  private UserX userX;

  private ProjectPost projectPost;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectPostComment setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectPostComment setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public ProjectPostComment setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Lob
  @Column(name = COLUMN_COMMENTHTML_NAME)
  public String getCommentHtml() {
    return commentHtml;
  }

  public ProjectPostComment setCommentHtml(String commentHtml) {
    this.commentHtml = commentHtml;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  public UserX getUserX() {
    return userX;
  }

  public ProjectPostComment setUserX(UserX userX) {
    this.userX = userX;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_post_id", nullable = false)
  public ProjectPost getProjectPost() {
    return projectPost;
  }

  public ProjectPostComment setProjectPost(ProjectPost projectPost) {
    this.projectPost = projectPost;
    return this;
  }
}
