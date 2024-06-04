package com.dattran.ecommerceapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("userId")
    String userId;
    @JsonProperty("totalPrice")
    Double totalPrice;
    @JsonProperty("cartItems")
    List<CartItemDTO> cartItems;
}
