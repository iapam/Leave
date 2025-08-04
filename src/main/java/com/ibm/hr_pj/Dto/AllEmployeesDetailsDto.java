package com.ibm.hr_pj.Dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AllEmployeesDetailsDto {
    private String employeeId;
    private String name;
    private String unitName;
    private String status;
}
