package com.ibm.hr_pj.Models;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Departments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departmentId;
    private String departmentName;
    private String departmentSupervisor;
    private int numberOfEmployeesAtTheDepartment;
    private String supervisorID;
    private String supervisorPhoneNumber;
    public Departments(String departmentName, String departmentSupervisor) {
        this.departmentName = departmentName;
        this.departmentSupervisor = departmentSupervisor;

    }
}
