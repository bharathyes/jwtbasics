package com.bharathyes.security.jwtbasics.security;

import com.bharathyes.security.jwtbasics.repository.UserRepo;
import com.bharathyes.security.jwtbasics.entity.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserDetail> userResponse = userRepo.findByEmail(email);
        if(userResponse.isEmpty()) {
            throw new UsernameNotFoundException("User with email " + email + " not found.");
        }
        UserDetail userDetail = userResponse.get();
        return new org.springframework.security.core.userdetails.User(
                email, userDetail.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
