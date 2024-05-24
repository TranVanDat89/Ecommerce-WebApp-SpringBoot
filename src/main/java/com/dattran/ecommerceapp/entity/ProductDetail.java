package com.dattran.ecommerceapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "product_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(name = "introduction", columnDefinition = "LONGTEXT")
    String introduction;
    @Column(name = "instruction", columnDefinition = "LONGTEXT")
    String instruction;
    @Column(name = "advantage", columnDefinition = "LONGTEXT")
    String advantage;
    @Column(name = "warning", columnDefinition = "LONGTEXT")
    String warning;
}
