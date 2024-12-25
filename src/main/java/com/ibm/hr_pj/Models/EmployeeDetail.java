package com.ibm.hr_pj.Models;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmployeeDetail {
    @Id
    private String phoneNumber;
    private String name;
    private String unit;
    private String annualSalary;
    private String grade;
    private String profession;
    private String employmentType;
    private String email;
    private String address;
    @ManyToOne
    @JoinColumn
    private Login login;
    private long numberOfDaysEntitled;
    private String status;
    private String department;
}