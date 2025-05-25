package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.request.LoginRequest;
import com.dattran.ecommerceapp.dto.request.UserRequest;
import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.dto.response.UserResponse;
import com.dattran.ecommerceapp.entity.Token;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/users/auth")
public class UserController {
    IUserService userService;

    @PostMapping("/register")
    public HttpResponse registerUser(@RequestBody @Valid UserRequest userRequest, HttpServletRequest httpServletRequest) {
        Token token = userService.createUser(userRequest);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .statusCode(ResponseStatus.USER_CREATED.getCode())
                .message(ResponseStatus.USER_CREATED.getMessage())
                .data(Map.of("token", token))
                .build();
        return httpResponse;
    }

    @PostMapping("/login")
    public HttpResponse login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        String token = userService.authenticate(loginRequest);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .message(ResponseStatus.LOGIN_SUCCESSFULLY.getMessage())
                .statusCode(ResponseStatus.LOGIN_SUCCESSFULLY.getCode())
                .status(HttpStatus.OK)
                .data(Map.of("token", token))
                .build();
        return httpResponse;
    }

    @PostMapping("/details")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse getUserDetails(
            @RequestHeader("Authorization") String authorizationHeader, HttpServletRequest httpServletRequest
    ) {
        String extractedToken = authorizationHeader.substring(7); // Loại bỏ "Bearer " từ chuỗi token
        UserResponse userResponse = userService.getUserDetailsFromToken(extractedToken);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .message(ResponseStatus.LOGIN_SUCCESSFULLY.getMessage())
                .statusCode(ResponseStatus.LOGIN_SUCCESSFULLY.getCode())
                .status(HttpStatus.OK)
                .data(Map.of("userResponse", userResponse))
                .build();
        return httpResponse;
    }

    @GetMapping("/all")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpResponse getAllUsers(HttpServletRequest httpServletRequest) {
        List<UserResponse> userResponses = userService.getAllUsers();
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .message(ResponseStatus.GET_USERS_SUCCESS.getMessage())
                .statusCode(ResponseStatus.GET_USERS_SUCCESS.getCode())
                .status(HttpStatus.OK)
                .data(Map.of("userResponses", userResponses))
                .build();
        return httpResponse;
    }

    @DeleteMapping("/delete/{userId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpResponse deleteUser(@PathVariable String userId, @RequestParam boolean isActive, HttpServletRequest httpServletRequest) {
        userService.deleteUser(userId, isActive);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .build();
        return httpResponse;
    }
}
