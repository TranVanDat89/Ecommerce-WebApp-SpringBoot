package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.dto.request.LoginRequest;
import com.dattran.ecommerceapp.dto.request.UserRequest;
import com.dattran.ecommerceapp.dto.response.UserResponse;
import com.dattran.ecommerceapp.entity.Role;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.mapper.UserMapper;
import com.dattran.ecommerceapp.repository.RoleRepository;
import com.dattran.ecommerceapp.repository.UserRepository;
import com.dattran.ecommerceapp.service.IUserService;
import com.dattran.ecommerceapp.util.JwtTokenUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements IUserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ResponseStatus.PHONE_NUMBER_EXISTED);
        }
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(()->new AppException(ResponseStatus.ROLE_NOT_FOUND));
//        if (request.getRoleId() == null) {
//            role = userRole;
//        } else {
//            role = roleRepository.findById(request.getRoleId()).orElseThrow();
//        }
        User user = userMapper.toUser(request);
        user.setRole(userRole);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
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
            return userMapper.toUserResponse(user.get());
        } else {
            throw new AppException(ResponseStatus.USER_NOT_FOUND);
        }
    }
}
