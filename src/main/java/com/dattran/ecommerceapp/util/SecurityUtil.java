package com.dattran.ecommerceapp.util;

import com.dattran.ecommerceapp.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    public User getLoggedInUserInfor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User userInfor) {
            if (!userInfor.getActive()) {
                return null;
            }
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
