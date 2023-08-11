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

@Entity(name = KnowledgeAndSkill.ENTITY_NAME)
@Table(name = KnowledgeAndSkill.TABLE_NAME, schema = "leo_temp")
public class KnowledgeAndSkill implements Serializable {

  public static final String ENTITY_NAME = "KnowledgeAndSkill";
  public static final String TABLE_NAME = "knowledge_and_skill";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_TYPE_NAME = "type";
  public static final String COLUMN_CATEGORY_NAME = "category";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_GLOBAL_NAME = "global";
  private static final long serialVersionUID = -4620261343456466499L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String type;

  private String category;

  private String shortDescr;

  private String longDescrHtml;

  private Boolean global;

  private UserX userX;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public KnowledgeAndSkill setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public KnowledgeAndSkill setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public KnowledgeAndSkill setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public KnowledgeAndSkill setName(String name) {
    this.name = name;
    return this;
  }

  @Lob
  @Column(name = COLUMN_TYPE_NAME, nullable = false)
  public String getType() {
    return type;
  }

  public KnowledgeAndSkill setType(String type) {
    this.type = type;
    return this;
  }

  @Column(name = COLUMN_CATEGORY_NAME)
  public String getCategory() {
    return category;
  }

  public KnowledgeAndSkill setCategory(String category) {
    this.category = category;
    return this;
  }

  @Lob
  @Column(name = COLUMN_SHORTDESCR_NAME)
  public String getShortDescr() {
    return shortDescr;
  }

  public KnowledgeAndSkill setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  public KnowledgeAndSkill setLongDescrHtml(String longDescrHtml) {
    this.longDescrHtml = longDescrHtml;
    return this;
  }

  @Column(name = COLUMN_GLOBAL_NAME)
  public Boolean getGlobal() {
    return global;
  }

  public KnowledgeAndSkill setGlobal(Boolean global) {
    this.global = global;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  public UserX getUserX() {
    return userX;
  }

  public KnowledgeAndSkill setUserX(UserX userX) {
    this.userX = userX;
    return this;
  }
}
