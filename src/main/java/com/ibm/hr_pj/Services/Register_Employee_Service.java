package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Dto.Register_Employee_Request;
import com.ibm.hr_pj.Models.EmployeeDetails;
import com.ibm.hr_pj.Models.EmployeeRole;
import com.ibm.hr_pj.Models.Login;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class Register_Employee_Service {
    private LoginRegistrationService loginRegistrationService;
    private EmployeeDetailsRegistration employeeDetailsRegistration;
    public String registerEmployeeService(Register_Employee_Request employeeRequest,EmployeeRole employeeRole) {
        if(employeeRequest.getEmploymentType().equals("Contract")){
            Random rand = new Random();
            String id=String.format("%08d", rand.nextInt(100000000));
            employeeRequest.setEmployeeId(id);
        }
      Login login=loginRegistrationService.loginDetails(new Login(employeeRequest.getEmployeeId(), employeeRequest.getPassword(), employeeRequest.getPhoneNumber(),employeeRole));
      if(login==null){
          return "Employee Already Registered";
      }
        employeeDetailsRegistration.employeeDetailsRegistration(new EmployeeDetails(employeeRequest.getPhoneNumber(),
                employeeRequest.getName(),employeeRequest.getUnit(),employeeRequest.getAnnualSalary()
                ,employeeRequest.getGrade(),employeeRequest.getProfession(),employeeRequest.getEmploymentType(),
                employeeRequest.getEmail(),employeeRequest.getAddress(),login));

        return "Employee Registered";

    }
}
