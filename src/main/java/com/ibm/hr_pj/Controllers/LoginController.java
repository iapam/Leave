package com.ibm.hr_pj.Controllers;

import com.ibm.hr_pj.Dto.Register_Employee_Request;
import com.ibm.hr_pj.Models.EmployeeRole;
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
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest, EmployeeRole.EMPLOYEE);
        return Registration;
    }
    @PostMapping("/register/unit/head")
    public String registerUnitHead(@RequestBody Register_Employee_Request registerEmployeeRequest){
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest, EmployeeRole.UNIT_HEAD);
     return Registration;
    }
    @PostMapping("/register/administrator")
    public String registerAdministrator(@RequestBody Register_Employee_Request registerEmployeeRequest){
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest, EmployeeRole.ADMINISTRATOR);
        return Registration;
    }
    @PostMapping("/register/nurse_manager")
    public String registerNurseManager(@RequestBody Register_Employee_Request registerEmployeeRequest){
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest, EmployeeRole.NURSE_MANAGER);
        return Registration;
    }
    @PostMapping("/register/medsup")
    public String registerMedsup(@RequestBody Register_Employee_Request registerEmployeeRequest){
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest, EmployeeRole.MEDSUP);
        return Registration;
    }

}
