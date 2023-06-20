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

@Entity(name = ProjectImage.ENTITY_NAME)
@Table(name = ProjectImage.TABLE_NAME, schema = "leo_temp")
public class ProjectImage implements Serializable {

  public static final String ENTITY_NAME = "ProjectImage";
  public static final String TABLE_NAME = "project__image";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_SELECTED_NAME = "selected";
  private static final long serialVersionUID = -4215191532036939149L;

  private ProjectImageId id;

  private Project project;

  private FileX image;

  private Instant creationTime;

  private Boolean selected;

  @EmbeddedId
  public ProjectImageId getId() {
    return id;
  }

  public ProjectImage setId(ProjectImageId id) {
    this.id = id;
    return this;
  }

  @MapsId("projectId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  public Project getProject() {
    return project;
  }

  public ProjectImage setProject(Project project) {
    this.project = project;
    return this;
  }

  @MapsId("imageId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "image_id", nullable = false)
  public FileX getImage() {
    return image;
  }

  public ProjectImage setImage(FileX image) {
    this.image = image;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectImage setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_SELECTED_NAME)
  public Boolean getSelected() {
    return selected;
  }

  public ProjectImage setSelected(Boolean selected) {
    this.selected = selected;
    return this;
  }
}
