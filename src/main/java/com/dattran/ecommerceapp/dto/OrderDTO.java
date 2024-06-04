package com.dattran.ecommerceapp.dto;

import com.dattran.ecommerceapp.dto.request.CartRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    String userId;
    String fullName;
    String phoneNumber;
    String note;
    @Min(value = 0, message = "Total money must be >= 0")
    Double totalMoney;
    String shippingMethod;
    String address;
    String paymentMethod;
//    @JsonProperty("coupon_code")
//    private String couponCode;
    List<CartRequest> cartItems;
}
