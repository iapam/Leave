package com.ibm.hr_pj.Controllers;

import com.ibm.hr_pj.Dto.EmployeeDetailsRegistrationRequest;
import com.ibm.hr_pj.Dto.LeaveRequest;
import com.ibm.hr_pj.Models.Login;
import com.ibm.hr_pj.Services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeDetailsService;
    private final EmployeeService employeeService;
    @GetMapping("/employee/profile")
    public EmployeeDetailsRegistrationRequest employeeProfile() {
        Login login= (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EmployeeDetailsRegistrationRequest employeeDetails=employeeDetailsService.employeeDetails(login);
        return employeeDetails;
    }
    @PostMapping("/apply/leave")
public String leaveRequest(@RequestBody LeaveRequest leaveRequest) throws ParseException {
    Login login= (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   String leaveApplication= employeeService.leaveApplication(leaveRequest,login);
return leaveApplication;
}
}
