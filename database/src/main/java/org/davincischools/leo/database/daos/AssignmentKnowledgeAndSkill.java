package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.davincischools.leo.database.dao_interfaces.PropagateDeleteFrom;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity(name = AssignmentKnowledgeAndSkill.ENTITY_NAME)
@Table(name = AssignmentKnowledgeAndSkill.TABLE_NAME, schema = "leo_test")
public class AssignmentKnowledgeAndSkill implements Serializable {

  public static final String ENTITY_NAME = "AssignmentKnowledgeAndSkill";
  public static final String TABLE_NAME = "assignment__knowledge_and_skill";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  @Serial private static final long serialVersionUID = -766160400666081620L;

  private AssignmentKnowledgeAndSkillId id;

  private Assignment assignment;

  private KnowledgeAndSkill knowledgeAndSkill;

  private Instant creationTime;

  private Instant deleted;

  @EmbeddedId
  public AssignmentKnowledgeAndSkillId getId() {
    return id;
  }

  @MapsId("assignmentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  @PropagateDeleteFrom
  public Assignment getAssignment() {
    return assignment;
  }

  @MapsId("knowledgeAndSkillId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "knowledge_and_skill_id", nullable = false)
  @PropagateDeleteFrom
  public KnowledgeAndSkill getKnowledgeAndSkill() {
    return knowledgeAndSkill;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }
}
