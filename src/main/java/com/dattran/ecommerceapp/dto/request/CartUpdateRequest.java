package com.dattran.ecommerceapp.dto.request;

import com.dattran.ecommerceapp.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartUpdateRequest {
    String productId;
    Integer quantity;
    String flavorName;
}
