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
import java.time.Instant;

@Entity(name = Project.ENTITY_NAME)
@Table(name = Project.TABLE_NAME, schema = "leo_temp")
public class Project {

  public static final String ENTITY_NAME = "Project";
  public static final String TABLE_NAME = "project";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCR_NAME = "long_descr";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  public static final String COLUMN_FAVORITE_NAME = "favorite";
  public static final String COLUMN_THUMBSSTATE_NAME = "thumbs_state";
  public static final String COLUMN_ARCHIVED_NAME = "archived";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NEEDSREVIEW_NAME = "needs_review";

  private Integer id;

  private Instant creationTime;

  private String name;

  private String shortDescr;

  private String shortDescrQuill;

  private String longDescr;

  private String longDescrQuill;

  private Boolean favorite;

  private String thumbsState;

  private Boolean archived;

  private Boolean deleted;

  private Boolean needsReview;

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

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public Project setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCR_NAME, length = 1024)
  public String getShortDescr() {
    return shortDescr;
  }

  public Project setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Lob
  @Column(name = COLUMN_SHORTDESCRQUILL_NAME)
  public String getShortDescrQuill() {
    return shortDescrQuill;
  }

  public Project setShortDescrQuill(String shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCR_NAME)
  public String getLongDescr() {
    return longDescr;
  }

  public Project setLongDescr(String longDescr) {
    this.longDescr = longDescr;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRQUILL_NAME)
  public String getLongDescrQuill() {
    return longDescrQuill;
  }

  public Project setLongDescrQuill(String longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
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

  @Column(name = COLUMN_ARCHIVED_NAME)
  public Boolean getArchived() {
    return archived;
  }

  public Project setArchived(Boolean archived) {
    this.archived = archived;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Boolean getDeleted() {
    return deleted;
  }

  public Project setDeleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_NEEDSREVIEW_NAME)
  public Boolean getNeedsReview() {
    return needsReview;
  }

  public Project setNeedsReview(Boolean needsReview) {
    this.needsReview = needsReview;
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
