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

@Entity(name = Project.ENTITY_NAME)
@Table(name = Project.TABLE_NAME, schema = "leo_temp")
public class Project implements Serializable {

  public static final String ENTITY_NAME = "Project";
  public static final String TABLE_NAME = "project";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_GENERATOR_NAME = "generator";
  public static final String COLUMN_FAVORITE_NAME = "favorite";
  public static final String COLUMN_THUMBSSTATE_NAME = "thumbs_state";
  public static final String COLUMN_THUMBSSTATEREASON_NAME = "thumbs_state_reason";
  public static final String COLUMN_ARCHIVED_NAME = "archived";
  public static final String COLUMN_ACTIVE_NAME = "active";
  private static final long serialVersionUID = 7055676245998610020L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String shortDescr;

  private String longDescrHtml;

  private String generator;

  private Boolean favorite;

  private String thumbsState;

  private String thumbsStateReason;

  private Boolean archived;

  private Boolean active;

  private ProjectInput projectInput;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Project setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Project setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public Project setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public Project setName(String name) {
    this.name = name;
    return this;
  }

  @Lob
  @Column(name = COLUMN_SHORTDESCR_NAME)
  public String getShortDescr() {
    return shortDescr;
  }

  public Project setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  public Project setLongDescrHtml(String longDescrHtml) {
    this.longDescrHtml = longDescrHtml;
    return this;
  }

  @Lob
  @Column(name = COLUMN_GENERATOR_NAME)
  public String getGenerator() {
    return generator;
  }

  public Project setGenerator(String generator) {
    this.generator = generator;
    return this;
  }

  @Column(name = COLUMN_FAVORITE_NAME)
  public Boolean getFavorite() {
    return favorite;
  }

  public Project setFavorite(Boolean favorite) {
    this.favorite = favorite;
    return this;
  }

  @Lob
  @Column(name = COLUMN_THUMBSSTATE_NAME)
  public String getThumbsState() {
    return thumbsState;
  }

  public Project setThumbsState(String thumbsState) {
    this.thumbsState = thumbsState;
    return this;
  }

  @Lob
  @Column(name = COLUMN_THUMBSSTATEREASON_NAME)
  public String getThumbsStateReason() {
    return thumbsStateReason;
  }

  public Project setThumbsStateReason(String thumbsStateReason) {
    this.thumbsStateReason = thumbsStateReason;
    return this;
  }

  @Column(name = COLUMN_ARCHIVED_NAME)
  public Boolean getArchived() {
    return archived;
  }

  public Project setArchived(Boolean archived) {
    this.archived = archived;
    return this;
  }

  @Column(name = COLUMN_ACTIVE_NAME)
  public Boolean getActive() {
    return active;
  }

  public Project setActive(Boolean active) {
    this.active = active;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_input_id")
  public ProjectInput getProjectInput() {
    return projectInput;
  }

  public Project setProjectInput(ProjectInput projectInput) {
    this.projectInput = projectInput;
    return this;
  }
}
