package com.ibm.hr_pj.Dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EmployeesOnLeaveDto {
    private String employeeId;
    private String name;
    private String unit;
    private long numberOfDaysRemaining;
    private LocalDate commencementDate;
    private LocalDate resumptionDate;
    private LocalDate endDate;
    private String appliedDate;
}
