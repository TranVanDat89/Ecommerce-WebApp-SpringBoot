package com.dattran.ecommerceapp.dto.request;

import com.dattran.ecommerceapp.custom.Password;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    String phoneNumber;
    @Password
    String password;
}
