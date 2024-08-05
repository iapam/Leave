package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Dto.Login_Request;
import com.ibm.hr_pj.Models.Login;
import com.ibm.hr_pj.Repositories.LoginRepository;
import com.ibm.hr_pj.Secuurity.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginRegistrationService {
    private LoginRepository loginRepository;
    private PasswordEncoder passwordEncoder;
    public Login loginDetails(Login login) {
        Login userexist=loginRepository.findByEmployeeIdOrPhoneNumber(login.getEmployeeId(),login.getPhoneNumber());
        if(userexist==null){
           login.setPasswords( passwordEncoder.passwordEncoder().encode(login.getPasswords()));
        }else {
            return null;
        }
      Login login1=loginRepository.save(login);
      return login1;
    }
}
