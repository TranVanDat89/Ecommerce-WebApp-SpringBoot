package com.dattran.ecommerceapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String brand;
    String weight;
    @Column(name = "serving_size")
    String servingSize;
    String serving;
    String calories;
    @Column(name = "ingredient_list")
    String ingredientList;
    @Column(name = "protein_per_serving")
    String proteinPerServing;
    String origin;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "ingredients_flavors",
            joinColumns = {
                    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "flavor_id", referencedColumnName = "id")})
    Set<Flavor> flavors = new HashSet<>();
}
