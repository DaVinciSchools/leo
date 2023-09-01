package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Embeddable
public class ProjectImageId implements Serializable {

  public static final String COLUMN_PROJECTID_NAME = "project_id";
  public static final String COLUMN_IMAGEID_NAME = "image_id";
  private static final long serialVersionUID = 5644425560298181751L;

  private Integer projectId;

  private Integer imageId;

  @Column(name = COLUMN_PROJECTID_NAME, nullable = false)
  public Integer getProjectId() {
    return projectId;
  }

  @Column(name = COLUMN_IMAGEID_NAME, nullable = false)
  public Integer getImageId() {
    return imageId;
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
