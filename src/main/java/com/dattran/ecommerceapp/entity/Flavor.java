package com.dattran.ecommerceapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "flavors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flavor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(name = "name", nullable = false)
    String name;
    @ManyToMany(mappedBy = "flavors", fetch = FetchType.LAZY)
    Set<Ingredient> ingredients = new HashSet<>();
}
