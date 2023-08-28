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

@Entity(name = Tag.ENTITY_NAME)
@Table(name = Tag.TABLE_NAME, schema = "leo_temp")
public class Tag implements Serializable {

  public static final String ENTITY_NAME = "Tag";
  public static final String TABLE_NAME = "tag";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_TEXT_NAME = "text";
  private static final long serialVersionUID = 3128921904451530598L;

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

  public Tag setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Tag setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public Tag setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_TEXT_NAME, nullable = false, length = 32)
  public String getText() {
    return text;
  }

  public Tag setText(String text) {
    this.text = text;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_x_id")
  public UserX getUserX() {
    return userX;
  }

  public Tag setUserX(UserX userX) {
    this.userX = userX;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  public Project getProject() {
    return project;
  }

  public Tag setProject(Project project) {
    this.project = project;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_post_id")
  public ProjectPost getProjectPost() {
    return projectPost;
  }

  public Tag setProjectPost(ProjectPost projectPost) {
    this.projectPost = projectPost;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_post_comment_id")
  public ProjectPostComment getProjectPostComment() {
    return projectPostComment;
  }

  public Tag setProjectPostComment(ProjectPostComment projectPostComment) {
    this.projectPostComment = projectPostComment;
    return this;
  }
}
