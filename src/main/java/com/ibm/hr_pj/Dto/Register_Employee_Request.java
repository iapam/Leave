package com.ibm.hr_pj.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Register_Employee_Request {
    private String employeeId;
    private String name;
    private String unit;
    private String annualSalary;
    private String grade;
    private String profession;
    private String employmentType;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;
    private long numberOfDaysEntitled;
    private String department;
}
