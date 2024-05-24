package com.dattran.ecommerceapp.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    DELIVERING("Delivering"),
    SUCCESS("Success"),
    CANCELLED("Cancelled")
    ;
    private String name;
}
