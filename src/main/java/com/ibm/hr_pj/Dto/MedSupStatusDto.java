package com.ibm.hr_pj.Dto;

import com.ibm.hr_pj.Models.Departments;
import com.ibm.hr_pj.Models.EmployeeDetail;
import com.ibm.hr_pj.Models.Leave_RequestModel;
import com.ibm.hr_pj.Models.Unit;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class MedSupStatusDto {
    private Leave_RequestModel id;
    private Departments departmentsId;
    private Unit unitID;
    private String employeeDetailsID;
    private String name;
    private long numberOfDaysApproved;
    private LocalDate commencementDate;
    private LocalDate endDate;
    private LocalDate resumptionDate;
    private String status;
}
