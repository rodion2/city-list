package com.kuehnenagel.citylist.features.security;

import com.kuehnenagel.citylist.common.security.AuthenticationStatus;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/authentication")
public class AuthenticationController {


    /**
     * Validates login for User by basic authentication
     * @return
     */
    @GetMapping(produces = "application/json")
    @RequestMapping({"/validateLogin"})
    public AuthenticationStatus validateLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new AuthenticationStatus("User successfully authenticated",
            auth.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
    }
}
