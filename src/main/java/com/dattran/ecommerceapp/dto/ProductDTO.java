package com.dattran.ecommerceapp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    String name;
    Double price;
    String categoryName;
    Integer quantity;
    String brand;
    String weight;
    String servingSize;
    String serving;
    String calories;
    String ingredientList;
    String proteinPerServing;
    String origin;
    String flavors;
    String introduction;
    String instruction;
    String advantage;
    String warning;
}
