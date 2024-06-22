package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.dto.request.LoginRequest;
import com.dattran.ecommerceapp.dto.request.UserRequest;
import com.dattran.ecommerceapp.dto.response.CommentResponse;
import com.dattran.ecommerceapp.dto.response.UserResponse;
import com.dattran.ecommerceapp.entity.Role;
import com.dattran.ecommerceapp.entity.Token;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.mapper.UserMapper;
import com.dattran.ecommerceapp.repository.RoleRepository;
import com.dattran.ecommerceapp.repository.TokenRepository;
import com.dattran.ecommerceapp.repository.UserRepository;
import com.dattran.ecommerceapp.service.IUserService;
import com.dattran.ecommerceapp.util.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements IUserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    TokenRepository tokenRepository;
    JwtTokenUtil jwtTokenUtil;
    @Override
    @Transactional
    public Token createUser(UserRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ResponseStatus.PHONE_NUMBER_EXISTED);
        }
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(()->new AppException(ResponseStatus.ROLE_NOT_FOUND));
        User user = userMapper.toUser(request);
        user.setRole(userRole);
        user.setActive(true);
        if (user.getDateOfBirth() == null) {
            user.setDateOfBirth(LocalDate.now().minusYears(18));
        }
        if (user.getAddress() == null) {
            user.setAddress("Việt Nam");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        UserResponse userResponse = userMapper.toUserResponse(userRepository.save(user));
        User savedUser = userRepository.save(user);
        String token = generateActivationCode(6);
        Token validateToken = Token.builder()
                .token(token)
                .user(savedUser)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();
        return tokenRepository.save(validateToken);
    }
    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
    @Override
    public String authenticate(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber());
        if (optionalUser.isEmpty()) {
            throw new AppException(ResponseStatus.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AppException(ResponseStatus.PASSWORD_NOT_MATCH);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getPhoneNumber(), loginRequest.getPassword(),
                user.getAuthorities()
        );
//        Can error
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(user);
    }

    @Override
    public UserResponse getUserDetailsFromToken(String token) {
        if(jwtTokenUtil.isTokenExpired(token)) {
            throw new AppException(ResponseStatus.TOKEN_EXPIRED);
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            UserResponse userResponse = userMapper.toUserResponse(user.get());
            return userResponse;
        } else {
            throw new AppException(ResponseStatus.USER_NOT_FOUND);
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> UserResponse.builder()
                        .fullName(user.getFullName())
                        .id(user.getId())
                        .phoneNumber(user.getPhoneNumber())
                        .active(user.getActive())
                        .address(user.getAddress())
                        .dateOfBirth(user.getDateOfBirth())
                        .role(user.getRole().getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public long countTotalUsers() {
        return userRepository.count();
    }

    @Override
    public Map<String, Object> countTotalUsersByYear(int year) {
        if (year == 2023) {
            return Map.of("totalUsers", userRepository.countUsersByYear(2023));
        }
        Map<String, Object> result = new HashMap<>();
        Long totalUserNow = userRepository.countUsersByYear(year);
        result.put("totalUsers", totalUserNow);
        Long totalUsersPreviousYear = userRepository.countUsersByYear(year - 1) == 0 ? 1 : userRepository.countUsersByYear(year - 1);
        if (totalUserNow > totalUsersPreviousYear) {
            Double rate = (1 - ((double) totalUsersPreviousYear / totalUserNow))*100;
            result.put("up", rate);
        } else {
            Double rate = (1 - ((double) totalUserNow / totalUsersPreviousYear))*100;
            result.put("down", rate);
        }
        return result;
    }
}
