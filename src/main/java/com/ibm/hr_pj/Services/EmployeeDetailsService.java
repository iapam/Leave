package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Dto.EmployeeDetailsRegistrationRequest;
import com.ibm.hr_pj.Models.EmployeeDetails;
import com.ibm.hr_pj.Models.Login;
import com.ibm.hr_pj.Repositories.EmployeeDetailsRepository;
import com.ibm.hr_pj.Repositories.LoginRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDetailsService implements UserDetailsService {
    private final LoginRepository loginRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private EmployeeDetailsRegistrationRequest employeeDetailsrequest;
    @Override
    public UserDetails loadUserByUsername(String staffId) throws UsernameNotFoundException {

        return loginRepository
                .findLoginByEmployeeId(staffId)
                .orElseThrow(()->new IllegalStateException("See Your HR for registration"));
    }
    public EmployeeDetailsRegistrationRequest employeeDetails(Login login) {
        EmployeeDetails employeeDetails=employeeDetailsRepository.findEmployeeDetailsByLogin(login);

        return new EmployeeDetailsRegistrationRequest(employeeDetails.getName(), employeeDetails.getUnit(),
                employeeDetails.getAnnualSalary(), employeeDetails.getGrade(), employeeDetails.getProfession(),
                employeeDetails.getEmploymentType(), employeeDetails.getPhoneNumber(), employeeDetails.getEmail(),
                employeeDetails.getAddress());
    }
}
