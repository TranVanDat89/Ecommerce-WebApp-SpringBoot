package com.dattran.ecommerceapp.dto.request;

import com.dattran.ecommerceapp.custom.Password;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String fullName;
    String address;
    String phoneNumber;
    @Password
    String password;
    LocalDate dateOfBirth;
    Long facebookAccountId;
    Long googleAccountId;
}
