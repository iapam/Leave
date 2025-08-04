package com.ibm.hr_pj.Controllers;

import com.ibm.hr_pj.Dto.DefaultLeaveApplicationDto;
import com.ibm.hr_pj.Dto.Register_Employee_Request;
import com.ibm.hr_pj.Models.EmployeeDetail;
import com.ibm.hr_pj.Models.EmployeeRole;
import com.ibm.hr_pj.Models.Login;
import com.ibm.hr_pj.Services.EmailSenderService;
import com.ibm.hr_pj.Services.EmployeeService;
import com.ibm.hr_pj.Services.Register_Employee_Service;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@AllArgsConstructor
public class LoginController {
    private Register_Employee_Service register_employee_service;
    private EmployeeService employeeService;
    private EmailSenderService emailSenderService;
    @GetMapping("/")
    public String index() {
        return "/supervisorsDashboard";
    }
    @GetMapping("/employee/dashboard")
    public String employeeDashboard(){
        return "/employee";
    }
    @GetMapping("/leave/page")
    public String leavePage(Model model){
        Login user=(Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DefaultLeaveApplicationDto detail=employeeService.getEmployeeLeaveDetails(user);

        //todo calculate startDate by using LocalDate.now to 30 days and end date using start date to the end of days remaining
         model.addAttribute("defaultLeaveDetails",detail);
         model.addAttribute("leaveApplication","hello");
        return "/leave";
    }
    @GetMapping("/register/employee/page")
    public String registrationPage(Model model){
        model.addAttribute("response","");
        return "/RegistrationPage";
    }
    @PostMapping("/register/employee")
    public String registerEmployee(@ModelAttribute Register_Employee_Request registerEmployeeRequest, Model model){
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest, EmployeeRole.EMPLOYEE);
        model.addAttribute("response",Registration);
        return "/RegistrationPage";
    }
    @PostMapping("/register/unit/head")
    public String registerUnitHead(@RequestBody Register_Employee_Request registerEmployeeRequest){
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest, EmployeeRole.CLINICAL_COORDINATOR);
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
    @PostMapping("/register/HR")
    public String registerHR(@RequestBody Register_Employee_Request registerEmployeeRequest){
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest, EmployeeRole.HR);
        return Registration;
    }
    @PostMapping("/register/accountant")
    public String registerAccountant(@RequestBody Register_Employee_Request registerEmployeeRequest){
        String Registration=register_employee_service.registerEmployeeService(registerEmployeeRequest, EmployeeRole.ACCOUNTANT);
        return Registration;
    }
    // todo create hidden id in the supervisors for approving leave


}
