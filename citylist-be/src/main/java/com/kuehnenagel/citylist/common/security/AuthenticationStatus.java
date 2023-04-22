package com.kuehnenagel.citylist.common.security;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationStatus {
    private String status;
    private List<String> roles;
}