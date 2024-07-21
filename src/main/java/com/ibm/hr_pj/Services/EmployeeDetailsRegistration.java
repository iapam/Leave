package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Models.EmployeeDetails;
import com.ibm.hr_pj.Repositories.EmployeeDetailsRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmployeeDetailsRegistration {
    private EmployeeDetailsRepository employeeDetailsRepository;
  public void employeeDetailsRegistration(EmployeeDetails employeeDetails){
     employeeDetailsRepository.save(employeeDetails);
  }
}
