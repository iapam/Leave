package com.ibm.hr_pj.Dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EndedLeavesDetailsDto {
    private String employeeId;
    private String name;
    private String unit;
    private String appliedDate;
    private LocalDate commencementDate;
    private LocalDate endDate;
    private String endedDate;
}
