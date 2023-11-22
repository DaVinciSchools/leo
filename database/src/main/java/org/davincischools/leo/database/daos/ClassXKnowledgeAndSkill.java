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
@Entity(name = ClassXKnowledgeAndSkill.ENTITY_NAME)
@Table(name = ClassXKnowledgeAndSkill.TABLE_NAME, schema = "leo_test")
public class ClassXKnowledgeAndSkill implements Serializable {

  public static final String ENTITY_NAME = "ClassXKnowledgeAndSkill";
  public static final String TABLE_NAME = "class_x__knowledge_and_skill";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  private static final long serialVersionUID = -7801555348220618723L;

  private ClassXKnowledgeAndSkillId id;

  private ClassX classX;

  private KnowledgeAndSkill knowledgeAndSkill;

  private Instant creationTime;

  private Instant deleted;

  @EmbeddedId
  public ClassXKnowledgeAndSkillId getId() {
    return id;
  }

  @MapsId("classXId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_x_id", nullable = false)
  @PropagateDeleteFrom
  public ClassX getClassX() {
    return classX;
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
