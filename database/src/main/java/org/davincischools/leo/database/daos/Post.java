package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
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
@Entity(name = Post.ENTITY_NAME)
@Table(
    name = Post.TABLE_NAME,
    schema = "leo_test",
    indexes = {@Index(name = "long_descr_text", columnList = "long_descr_text")})
public class Post implements Serializable {

  public static final String ENTITY_NAME = "Post";
  public static final String TABLE_NAME = "post";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_DELETED_NAME = "deleted";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_LASTUPDATED_NAME = "last_updated";
  public static final String COLUMN_LONGDESCRHTML_NAME = "long_descr_html";
  public static final String COLUMN_LONGDESCRTEXT_NAME = "long_descr_text";
  @Serial private static final long serialVersionUID = -6552774988126536653L;

  private Integer id;

  private Instant creationTime;

  private Instant deleted;

  private String name;

  private Instant lastUpdated;

  private String longDescrHtml;

  private String longDescrText;

  private UserX userX;

  private UserX postToUserX;

  private Project project;

  private ProjectPost projectPost;

  private Set<CommentX> commentXES = new LinkedHashSet<>();

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

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  @Column(name = COLUMN_LASTUPDATED_NAME, nullable = false)
  public Instant getLastUpdated() {
    return lastUpdated;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRHTML_NAME, nullable = false)
  public String getLongDescrHtml() {
    return longDescrHtml;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCRTEXT_NAME, nullable = false)
  public String getLongDescrText() {
    return longDescrText;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  @PropagateDeleteFrom
  public UserX getUserX() {
    return userX;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "post_to_user_x_id", nullable = false)
  public UserX getPostToUserX() {
    return postToUserX;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  @PropagateDeleteFrom
  public Project getProject() {
    return project;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_post_id")
  public ProjectPost getProjectPost() {
    return projectPost;
  }

  @OneToMany(mappedBy = "post")
  public Set<CommentX> getCommentXES() {
    return commentXES;
  }
}
