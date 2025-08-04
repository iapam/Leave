package com.ibm.hr_pj.Dto;

import com.ibm.hr_pj.Models.Departments;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UnitDto {
    private String unitName;
    private String unitSupervisor;
    private int numberOfEmployeesAtTheUnit;
    private Departments departments;
}
