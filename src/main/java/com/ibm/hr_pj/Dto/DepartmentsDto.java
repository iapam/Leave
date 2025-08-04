package com.ibm.hr_pj.Dto;

import com.ibm.hr_pj.Models.EmployeeDetail;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentsDto {
    private String departmentName;
    private String departmentSupervisor;
    private int numberOfEmployeesAtTheDepartment;
    private EmployeeDetail employeeDetail;
}
