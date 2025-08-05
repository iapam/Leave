package com.ibm.hr_pj.Secuurity.SecurityConfigurations;

import com.ibm.hr_pj.Secuurity.PasswordEncoder;
import com.ibm.hr_pj.Services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService userDetailService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/register/employee","/register/unit/head","/register/administrator","/register/employee/page","/","/register/medsup","/register/HR","/register/nurse_manager")
                .permitAll()
                .requestMatchers("/leave/updateStatus","/leave/updatemedsup","/supervisor/dashboard")
                .hasAnyAuthority("ACCOUNTANT","ADMINISTRATOR","NURSE_MANAGER"," CLINICAL_COORDINATOR")
                .requestMatchers("/medsup/dashboard/page")
                .hasAuthority("MEDSUP")
                .requestMatchers("/HR/dashboard/page","/hr/overdue/leaves/table","/hr/ended/leaves/table","/hr/all/employees/table","/hr/employees/on/leave/table")
                .hasAuthority("HR")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

       return httpSecurity.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)->web.ignoring().requestMatchers("/images/**").requestMatchers("/static/images/css/assets/**").
                requestMatchers("/static/images/js/assets/**");
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
