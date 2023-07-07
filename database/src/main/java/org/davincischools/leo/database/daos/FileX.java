package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = FileX.ENTITY_NAME)
@Table(name = FileX.TABLE_NAME, schema = "leo_temp")
public class FileX implements Serializable {

  public static final String ENTITY_NAME = "FileX";
  public static final String TABLE_NAME = "file_x";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_FILECONTENT_NAME = "file_content";
  public static final String COLUMN_MIMETYPE_NAME = "mime_type";
  private static final long serialVersionUID = 4926085486314440970L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private byte[] fileContent;

  private String mimeType;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public FileX setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public FileX setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public FileX setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }

  @Column(name = COLUMN_FILECONTENT_NAME, nullable = false)
  public byte[] getFileContent() {
    return fileContent;
  }

  public FileX setFileContent(byte[] fileContent) {
    this.fileContent = fileContent;
    return this;
  }

  @Column(name = COLUMN_MIMETYPE_NAME, nullable = false)
  public String getMimeType() {
    return mimeType;
  }

  public FileX setMimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }
}
