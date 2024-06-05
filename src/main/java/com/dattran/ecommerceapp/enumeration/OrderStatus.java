package com.dattran.ecommerceapp.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("Pending"),
    DELIVERING("Delivering"),
    SUCCESS("Success"),
    CANCELLED("Cancelled")
    ;
    private final String name;
}
