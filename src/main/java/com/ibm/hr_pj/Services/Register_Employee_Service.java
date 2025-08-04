package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Dto.Register_Employee_Request;
import com.ibm.hr_pj.Models.*;
import com.ibm.hr_pj.Repositories.DepartmentsRepository;
import com.ibm.hr_pj.Repositories.UnitsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@AllArgsConstructor
public class Register_Employee_Service {
    private LoginRegistrationService loginRegistrationService;
    private EmployeeDetailsRegistration employeeDetailsRegistration;
    private UnitsRepository unitsRepository;
    private DepartmentsRepository departmentsRepository;
    public String registerEmployeeService(Register_Employee_Request employeeRequest,EmployeeRole employeeRole) {
        if(employeeRequest.getEmploymentType().equals("Contract")){
            Random rand = new Random();
            String id=String.format("%08d", rand.nextInt(100000000));
            employeeRequest.setEmployeeId(id);
        }
        Departments departments=departmentsRepository.findByDepartmentName(employeeRequest.getDepartment());
        Unit unit=unitsRepository.findByUnitName(employeeRequest.getUnit());
        if(employeeRole.name().equals("EMPLOYEE")){
            if(unit==null || departments==null){
                return "Cannot Register Employee without department or Unit";
            }
        }
        if(employeeRole.name().equals("ADMINISTRATOR")|| employeeRole.name().equals("NURSE_MANAGER") ||employeeRole.name().equals("CLINICAL_COORDINATOR")||employeeRole.name().equals("ACCOUNTANT")){
            Departments isSupervisor=departmentsRepository.findBySupervisorID(employeeRequest.getEmployeeId());
            if (isSupervisor==null){
                return "The Employee is not a supervisor";
            }

        }
      Login login=loginRegistrationService.loginDetails(new Login(employeeRequest.getEmployeeId(),employeeRequest.getPassword(), employeeRequest.getPhoneNumber(),employeeRole));
      if(login==null){
          return "Employee Already Registered";
      }
      int numberOfEmployeeAtUnit= unit.getNumberOfEmployeesAtTheUnit()+1;
      int numberOfEmployeesAtDepartment=departments.getNumberOfEmployeesAtTheDepartment()+1;
        employeeDetailsRegistration.employeeDetailsRegistration(new EmployeeDetail(employeeRequest.getPhoneNumber(),
                employeeRequest.getName(),employeeRequest.getAnnualSalary()
                ,employeeRequest.getGrade(),employeeRequest.getProfession(),employeeRequest.getEmploymentType(),
                employeeRequest.getEmail(),employeeRequest.getAddress(),login,employeeRequest.getNumberOfDaysEntitled(),"active",unit,departments));
      unitsRepository.updateUnit(unit.getUnitId(), numberOfEmployeeAtUnit);
      departmentsRepository.updateDepartments(departments.getDepartmentId(),numberOfEmployeesAtDepartment);
        return "Employee Registered";

    }
}
