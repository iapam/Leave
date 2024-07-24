package com.ibm.hr_pj.Controllers;

import com.ibm.hr_pj.Dto.EmployeeDetailsRegistrationRequest;
import com.ibm.hr_pj.Models.EmployeeDetails;
import com.ibm.hr_pj.Models.Login;
import com.ibm.hr_pj.Services.EmployeeDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeDetailsService employeeDetailsService;
    @GetMapping("/employee/profile")
    public EmployeeDetailsRegistrationRequest employeeProfile() {
        Login login= (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EmployeeDetailsRegistrationRequest employeeDetails=employeeDetailsService.employeeDetails(login);
        return employeeDetails;
    }
    public String leaveRequest(){

        return "leave applied successfully";
    }

}
