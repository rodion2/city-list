package com.kuehnenagel.citylist.common.security;

import com.kuehnenagel.citylist.features.usermanagement.User;
import com.kuehnenagel.citylist.features.usermanagement.UserRepository;
import com.kuehnenagel.citylist.common.error.ServiceError;
import com.kuehnenagel.citylist.common.error.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("cityListUserDetailService")
@RequiredArgsConstructor
public class CityListUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username).orElseThrow(
                () -> ServiceException.builder(ServiceError.USER_NOT_FOUND).messageParameters().build());
        return org.springframework.security.core.userdetails.User.builder().username(user.getName())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

}
