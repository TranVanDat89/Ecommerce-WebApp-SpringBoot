package com.dattran.ecommerceapp.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("quantity")
    private Integer quantity;
}
