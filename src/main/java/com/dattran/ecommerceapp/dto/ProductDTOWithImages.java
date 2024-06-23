package com.dattran.ecommerceapp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.annotations.NotNull;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTOWithImages {
    @NotNull
    String name;
    @NotNull
    Double price;
    @NotNull
    String categoryName;
    @NotNull
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
    List<MultipartFile> images;
}
