package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity(name = ProjectImage.ENTITY_NAME)
@Table(name = ProjectImage.TABLE_NAME, schema = "leo_temp")
public class ProjectImage implements Serializable {

  public static final String ENTITY_NAME = "ProjectImage";
  public static final String TABLE_NAME = "project__image";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_SELECTED_NAME = "selected";
  private static final long serialVersionUID = 1666360494323671126L;

  private ProjectImageId id;

  private Project project;

  private FileX image;

  private Instant creationTime;

  private Instant deleted;

  private Instant selected;

  @EmbeddedId
  public ProjectImageId getId() {
    return id;
  }

  @MapsId("projectId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  public Project getProject() {
    return project;
  }

  @MapsId("imageId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "image_id", nullable = false)
  public FileX getImage() {
    return image;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  @Column(name = COLUMN_SELECTED_NAME)
  public Instant getSelected() {
    return selected;
  }
}
