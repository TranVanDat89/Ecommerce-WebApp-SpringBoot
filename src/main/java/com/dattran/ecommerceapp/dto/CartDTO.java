package com.dattran.ecommerceapp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDTO {
    String id;
    String userId;
    Double totalPrice;
    List<CartItemDTO> cartItems;
}
