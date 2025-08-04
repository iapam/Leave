package com.ibm.hr_pj.Dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeDetailsRegistrationRequest {
    private String name;
    private String unit;
    private String annualSalary;
    private String grade;
    private String profession;
    private String employmentType;
    private String phoneNumber;
    private String email;
    private String address;
    private String department;
}
