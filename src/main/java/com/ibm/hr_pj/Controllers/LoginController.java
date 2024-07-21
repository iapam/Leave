package com.ibm.hr_pj.Controllers;

import com.ibm.hr_pj.Dto.Register_Employee_Request;
import com.ibm.hr_pj.Services.Register_Employee_Service;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class LoginController {
    private Register_Employee_Service register_employee_service;
    @GetMapping("/")
    public String index() {

        return "no print";
    }
    @PostMapping("/register/employee")
    public String registerEmployee(@RequestBody Register_Employee_Request registerEmployeeRequest){
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest);
        return Registration;
    }
}
