package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity(name = Image.ENTITY_NAME)
@Table(name = Image.TABLE_NAME, schema = "leo_temp")
public class Image implements Serializable {

  public static final String ENTITY_NAME = "Image";
  public static final String TABLE_NAME = "image";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_IMAGE_NAME = "image";
  public static final String COLUMN_MIMETYPE_NAME = "mime_type";
  private static final long serialVersionUID = -3526002040327597487L;

  private Integer id;

  private Instant creationTime;

  private byte[] image;

  private String mimeType;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Image setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Image setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_IMAGE_NAME, nullable = false)
  public byte[] getImage() {
    return image;
  }

  public Image setImage(byte[] image) {
    this.image = image;
    return this;
  }

  @Column(name = COLUMN_MIMETYPE_NAME, nullable = false)
  public String getMimeType() {
    return mimeType;
  }

  public Image setMimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }
}
