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

@Entity(name = AssignmentKnowledgeAndSkill.ENTITY_NAME)
@Table(name = AssignmentKnowledgeAndSkill.TABLE_NAME, schema = "leo_temp")
public class AssignmentKnowledgeAndSkill implements Serializable {

  public static final String ENTITY_NAME = "AssignmentKnowledgeAndSkill";
  public static final String TABLE_NAME = "assignment__knowledge_and_skill";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  private static final long serialVersionUID = -1342008478808447218L;

  private AssignmentKnowledgeAndSkillId id;

  private Assignment assignment;

  private KnowledgeAndSkill knowledgeAndSkill;

  private Instant creationTime;

  private Instant deleted;

  @EmbeddedId
  public AssignmentKnowledgeAndSkillId getId() {
    return id;
  }

  public AssignmentKnowledgeAndSkill setId(AssignmentKnowledgeAndSkillId id) {
    this.id = id;
    return this;
  }

  @MapsId("assignmentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  public Assignment getAssignment() {
    return assignment;
  }

  public AssignmentKnowledgeAndSkill setAssignment(Assignment assignment) {
    this.assignment = assignment;
    return this;
  }

  @MapsId("knowledgeAndSkillId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "knowledge_and_skill_id", nullable = false)
  public KnowledgeAndSkill getKnowledgeAndSkill() {
    return knowledgeAndSkill;
  }

  public AssignmentKnowledgeAndSkill setKnowledgeAndSkill(KnowledgeAndSkill knowledgeAndSkill) {
    this.knowledgeAndSkill = knowledgeAndSkill;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public AssignmentKnowledgeAndSkill setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_DELETED_NAME)
  public Instant getDeleted() {
    return deleted;
  }

  public AssignmentKnowledgeAndSkill setDeleted(Instant deleted) {
    this.deleted = deleted;
    return this;
  }
}
