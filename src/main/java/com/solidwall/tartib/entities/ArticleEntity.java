package com.solidwall.tartib.entities;

import java.util.List;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articles")
@Entity
public class ArticleEntity extends AbstractBaseEntity {

  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "short_description", length = 255, nullable = true, columnDefinition = "TEXT")
  private String short_description;

  @Column(name = "long_description", length = 255, nullable = true, columnDefinition = "TEXT")
  private String long_description;

  @Column(name = "picture", length = 255, nullable = false, columnDefinition = "TEXT")
  private String picture;

  @Column(name = "content", length = 255, nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "isActive", length = 255, nullable = false, columnDefinition = "boolean")
  private boolean isActive;

  @Column(name = "isPrivate", nullable = false, columnDefinition = "boolean")
  private boolean isPrivate;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "article_medias", joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id"))
  private List<MediaEntity> medias;

}
