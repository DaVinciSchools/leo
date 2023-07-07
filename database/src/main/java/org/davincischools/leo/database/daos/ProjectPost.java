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

@Entity(name = ProjectPost.ENTITY_NAME)
@Table(name = ProjectPost.TABLE_NAME, schema = "leo_temp")
public class ProjectPost implements Serializable {

  public static final String ENTITY_NAME = "ProjectPost";
  public static final String TABLE_NAME = "project_post";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_MESSAGEHTML_NAME = "message_html";
  private static final long serialVersionUID = 6525477675978236832L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String messageHtml;

  private UserX userX;

  private Project project;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectPost setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectPost setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public ProjectPost setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME)
  public String getName() {
    return name;
  }

  public ProjectPost setName(String name) {
    this.name = name;
    return this;
  }

  @Lob
  @Column(name = COLUMN_MESSAGEHTML_NAME)
  public String getMessageHtml() {
    return messageHtml;
  }

  public ProjectPost setMessageHtml(String messageHtml) {
    this.messageHtml = messageHtml;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  public UserX getUserX() {
    return userX;
  }

  public ProjectPost setUserX(UserX userX) {
    this.userX = userX;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  public Project getProject() {
    return project;
  }

  public ProjectPost setProject(Project project) {
    this.project = project;
    return this;
  }
}
