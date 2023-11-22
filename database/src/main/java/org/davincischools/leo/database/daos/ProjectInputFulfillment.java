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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity(name = ProjectInputFulfillment.ENTITY_NAME)
@Table(name = ProjectInputFulfillment.TABLE_NAME, schema = "leo_test")
public class ProjectInputFulfillment implements Serializable {

  public static final String ENTITY_NAME = "ProjectInputFulfillment";
  public static final String TABLE_NAME = "project_input_fulfillment";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_HOWPROJECTFULFILLS_NAME = "how_project_fulfills";
  public static final String COLUMN_FULFILLMENTPERCENTAGE_NAME = "fulfillment_percentage";
  public static final String COLUMN_VISIBLEINDICATOR_NAME = "visible_indicator";
  private static final long serialVersionUID = -9053034918526440029L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Project project;

  private ProjectInputValue projectInputValue;

  private String howProjectFulfills;

  private Integer fulfillmentPercentage;

  private String visibleIndicator;

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

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  public Project getProject() {
    return project;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_input_value_id", nullable = false)
  @PropagateDeleteFrom
  public ProjectInputValue getProjectInputValue() {
    return projectInputValue;
  }

  @Lob
  @Column(name = COLUMN_HOWPROJECTFULFILLS_NAME, nullable = false)
  public String getHowProjectFulfills() {
    return howProjectFulfills;
  }

  @Column(name = COLUMN_FULFILLMENTPERCENTAGE_NAME, nullable = false)
  public Integer getFulfillmentPercentage() {
    return fulfillmentPercentage;
  }

  @Lob
  @Column(name = COLUMN_VISIBLEINDICATOR_NAME, nullable = false)
  public String getVisibleIndicator() {
    return visibleIndicator;
  }
}
