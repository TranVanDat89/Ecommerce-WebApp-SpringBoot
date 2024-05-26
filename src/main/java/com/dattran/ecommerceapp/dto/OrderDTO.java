package com.dattran.ecommerceapp.dto;

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
    //@Min(value = 1, message = "User's ID must be > 0")
    private String userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String status;

    private String address;

    private String note;

    @Min(value = 0, message = "Total money must be >= 0")
    private Double totalMoney;

    private String shippingMethod;

    private String shippingAddress;

    private String paymentMethod;

//    @JsonProperty("coupon_code")
//    private String couponCode;

    private List<CartItemDTO> cartItems;
}
