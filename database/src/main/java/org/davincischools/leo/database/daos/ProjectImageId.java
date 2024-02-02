package org.davincischools.leo.database.daos;

import static org.davincischools.leo.database.utils.DaoUtils.getDaoClass;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Embeddable
public class ProjectImageId implements Serializable {

  public static final String COLUMN_PROJECTID_NAME = "project_id";
  public static final String COLUMN_FILEXID_NAME = "file_x_id";
  @Serial private static final long serialVersionUID = -1649723595512797026L;

  private Integer projectId;

  private Integer fileXId;

  @Column(name = COLUMN_PROJECTID_NAME, nullable = false)
  public Integer getProjectId() {
    return projectId;
  }

  @Column(name = COLUMN_FILEXID_NAME, nullable = false)
  public Integer getFileXId() {
    return fileXId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getDaoClass(this) != getDaoClass(o)) return false;
    ProjectImageId entity = (ProjectImageId) o;
    return Objects.equals(this.projectId, entity.projectId)
        && Objects.equals(this.fileXId, entity.fileXId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectId, fileXId);
  }
}
