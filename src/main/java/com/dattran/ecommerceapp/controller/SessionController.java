package com.dattran.ecommerceapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/session")
public class SessionController {
    @GetMapping("/id")
    public String getSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getId();
    }
}
