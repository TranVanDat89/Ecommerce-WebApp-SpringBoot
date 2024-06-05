package com.dattran.ecommerceapp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTO {
    String orderId;
    String productId;
    String userId;
    String content;
    Integer star;
}
