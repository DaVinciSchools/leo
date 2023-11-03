package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity(name = LogReference.ENTITY_NAME)
@Table(name = LogReference.TABLE_NAME, schema = "leo_test")
public class LogReference implements Serializable {

  public static final String ENTITY_NAME = "LogReference";
  public static final String TABLE_NAME = "log_reference";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  private static final long serialVersionUID = 5972493909007696874L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private Log log;

  private ProjectInput projectInput;

  private Project project;

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
  @JoinColumn(name = "log_id", nullable = false)
  public Log getLog() {
    return log;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_input_id")
  public ProjectInput getProjectInput() {
    return projectInput;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  public Project getProject() {
    return project;
  }
}
