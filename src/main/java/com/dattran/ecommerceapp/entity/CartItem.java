package com.dattran.ecommerceapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Integer quantity;
    String flavorName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    Cart cart;
}
