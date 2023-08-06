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

@Entity(name = ProjectDefinition.ENTITY_NAME)
@Table(name = ProjectDefinition.TABLE_NAME, schema = "leo_temp")
public class ProjectDefinition implements Serializable {

  public static final String ENTITY_NAME = "ProjectDefinition";
  public static final String TABLE_NAME = "project_definition";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_TEMPLATE_NAME = "template";
  private static final long serialVersionUID = -4720744188005192435L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private Boolean template;

  private UserX userX;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectDefinition setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectDefinition setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public ProjectDefinition setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public ProjectDefinition setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_TEMPLATE_NAME)
  public Boolean getTemplate() {
    return template;
  }

  public ProjectDefinition setTemplate(Boolean template) {
    this.template = template;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_x_id")
  public UserX getUserX() {
    return userX;
  }

  public ProjectDefinition setUserX(UserX userX) {
    this.userX = userX;
    return this;
  }
}
