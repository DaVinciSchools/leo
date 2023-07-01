package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class ProjectImageId implements Serializable {

  public static final String COLUMN_PROJECTID_NAME = "project_id";
  public static final String COLUMN_IMAGEID_NAME = "image_id";
  private static final long serialVersionUID = -2494415493626965214L;

  private Integer projectId;

  private Integer imageId;

  @Column(name = COLUMN_PROJECTID_NAME, nullable = false)
  public Integer getProjectId() {
    return projectId;
  }

  public ProjectImageId setProjectId(Integer projectId) {
    this.projectId = projectId;
    return this;
  }

  @Column(name = COLUMN_IMAGEID_NAME, nullable = false)
  public Integer getImageId() {
    return imageId;
  }

  public ProjectImageId setImageId(Integer imageId) {
    this.imageId = imageId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ProjectImageId entity = (ProjectImageId) o;
    return Objects.equals(this.imageId, entity.imageId)
        && Objects.equals(this.projectId, entity.projectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imageId, projectId);
  }
}
