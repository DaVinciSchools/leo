package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = FileX.ENTITY_NAME)
@Table(name = FileX.TABLE_NAME, schema = "leo_test")
public class FileX implements Serializable {

  public static final String ENTITY_NAME = "FileX";
  public static final String TABLE_NAME = "file_x";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_FILENAME_NAME = "file_name";
  public static final String COLUMN_FILECONTENT_NAME = "file_content";
  public static final String COLUMN_FILEKEY_NAME = "file_key";
  public static final String COLUMN_MIMETYPE_NAME = "mime_type";
  private static final long serialVersionUID = -6339924224847307229L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String fileName;

  private byte[] fileContent;

  private String fileKey;

  private String mimeType;

  private UserX userX;

  private Set<ProjectImage> projectImages = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  @Column(name = COLUMN_FILENAME_NAME, length = 256)
  public String getFileName() {
    return fileName;
  }

  @Column(name = COLUMN_FILECONTENT_NAME, nullable = false)
  public byte[] getFileContent() {
    return fileContent;
  }

  @Column(name = COLUMN_FILEKEY_NAME, nullable = false, length = 24)
  public String getFileKey() {
    return fileKey;
  }

  @Column(name = COLUMN_MIMETYPE_NAME, nullable = false)
  public String getMimeType() {
    return mimeType;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  @PropagateDeleteFrom
  public UserX getUserX() {
    return userX;
  }

  @OneToMany(mappedBy = "fileX")
  public Set<ProjectImage> getProjectImages() {
    return projectImages;
  }
}
