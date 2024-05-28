package com.dattran.ecommerceapp.dto.response;

import com.dattran.ecommerceapp.entity.Order;
import com.dattran.ecommerceapp.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Order order;
    List<DetailResponse> detailResponses;
}

