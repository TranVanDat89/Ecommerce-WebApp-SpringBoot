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
    @JsonProperty("user_id")
    //@Min(value = 1, message = "User's ID must be > 0")
    private String userId;

    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("status")
    private String status;

    private String address;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >= 0")
    private Double totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

//    @JsonProperty("coupon_code")
//    private String couponCode;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;
}
