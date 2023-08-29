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

@Entity(name = Assignment.ENTITY_NAME)
@Table(name = Assignment.TABLE_NAME, schema = "leo_temp")
public class Assignment implements Serializable {

  public static final String ENTITY_NAME = "Assignment";
  public static final String TABLE_NAME = "assignment";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_NICKNAME_NAME = "nickname";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  private static final long serialVersionUID = 985740840592816059L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private String nickname;

  private String shortDescr;

  private String longDescrHtml;

  private ClassX classX;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Assignment setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Assignment setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public Assignment setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public Assignment setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_NICKNAME_NAME)
  public String getNickname() {
    return nickname;
  }

  public Assignment setNickname(String nickname) {
    this.nickname = nickname;
    return this;
  }

  @Lob
  @Column(name = COLUMN_SHORTDESCR_NAME)
  public String getShortDescr() {
    return shortDescr;
  }

  public Assignment setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  public Assignment setLongDescrHtml(String longDescrHtml) {
    this.longDescrHtml = longDescrHtml;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_x_id")
  public ClassX getClassX() {
    return classX;
  }

  public Assignment setClassX(ClassX classX) {
    this.classX = classX;
    return this;
  }
}
