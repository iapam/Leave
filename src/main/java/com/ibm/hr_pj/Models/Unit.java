package com.ibm.hr_pj.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long unitId;
    private String unitName;
    private String unitSupervisor;
    private int numberOfEmployeesAtTheUnit;
    @ManyToOne
    @JoinColumn
    private Departments departments;
}
