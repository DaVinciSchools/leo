package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  public static final String COLUMN_TEMPLATE_NAME = "template";
  public static final String COLUMN_TEMPLATENAME_NAME = "template_name";
  private static final long serialVersionUID = 2938206953277094250L;

  private Integer id;

  private Instant creationTime;

  private Boolean template;

  private String templateName;

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

  @Column(name = COLUMN_TEMPLATE_NAME)
  public Boolean getTemplate() {
    return template;
  }

  public ProjectDefinition setTemplate(Boolean template) {
    this.template = template;
    return this;
  }

  @Column(name = COLUMN_TEMPLATENAME_NAME)
  public String getTemplateName() {
    return templateName;
  }

  public ProjectDefinition setTemplateName(String templateName) {
    this.templateName = templateName;
    return this;
  }
}
