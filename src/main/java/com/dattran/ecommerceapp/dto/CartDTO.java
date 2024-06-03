package com.dattran.ecommerceapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
