package com.ibm.hr_pj.Secuurity.SecurityConfigurations;

import com.ibm.hr_pj.Secuurity.PasswordEncoder;
import com.ibm.hr_pj.Services.EmployeeDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final EmployeeDetailsService userDetailService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/register/employee")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();


       return httpSecurity.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder.passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }
    public AuthenticationManagerBuilder authenticationManagerBuilder(AuthenticationManagerBuilder auth){
        return auth.authenticationProvider(authenticationProvider());
    }

}
