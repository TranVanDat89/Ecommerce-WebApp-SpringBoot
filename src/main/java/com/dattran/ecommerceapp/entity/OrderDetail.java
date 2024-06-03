package com.dattran.ecommerceapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties("inspection")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @Column(name = "price", nullable = false)
    Double price;

    @Column(name = "number_of_products", nullable = false)
    Integer numberOfProducts;

    @Column(name = "total_money", nullable = false)
    Double totalMoney;

    @Column(name = "flavor_name")
    String flavorName;
}
