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
  public static final String COLUMN_COMMENT_NAME = "comment";
  public static final String COLUMN_COMMENTQUILL_NAME = "comment_quill";
  private static final long serialVersionUID = -4804076276016969780L;

  private Integer id;

  private Instant creationTime;

  private String comment;

  private String commentQuill;

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

  @Lob
  @Column(name = COLUMN_COMMENT_NAME)
  public String getComment() {
    return comment;
  }

  public ProjectPostComment setComment(String comment) {
    this.comment = comment;
    return this;
  }

  @Lob
  @Column(name = COLUMN_COMMENTQUILL_NAME)
  public String getCommentQuill() {
    return commentQuill;
  }

  public ProjectPostComment setCommentQuill(String commentQuill) {
    this.commentQuill = commentQuill;
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
