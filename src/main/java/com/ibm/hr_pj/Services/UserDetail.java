package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Repositories.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetail implements UserDetailsService {
    private final LoginRepository loginRepository;
    @Override
    public UserDetails loadUserByUsername(String staffId) throws UsernameNotFoundException {

        return loginRepository
                .findLoginBystaffId(staffId)
                .orElseThrow(()->new IllegalStateException("See Your HR for registration"));
    }
}
