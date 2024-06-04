package com.dattran.ecommerceapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "articles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Article extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String title;
    @ManyToOne
    @JoinColumn(name = "category_id")
    ArticleCategory category;
    String imageUrl;
    @Column(name = "content", columnDefinition = "LONGTEXT")
    String content;
}
