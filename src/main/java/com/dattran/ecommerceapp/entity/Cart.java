package com.dattran.ecommerceapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;
    @Column(name = "total_price")
    Double totalPrice = 0.0;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<CartItem> cartItems = new ArrayList<>();
}
