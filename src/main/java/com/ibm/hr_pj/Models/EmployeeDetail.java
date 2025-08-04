package com.ibm.hr_pj.Models;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class EmployeeDetail {
    @Id
    private String phoneNumber;
    private String name;
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
    @ManyToOne
    @JoinColumn
    private Unit unitId;
    @ManyToOne
    @JoinColumn
    private Departments departments;

}
