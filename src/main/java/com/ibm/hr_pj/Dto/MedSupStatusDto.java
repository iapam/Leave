package com.ibm.hr_pj.Dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class MedSupStatusDto {
    private Long id;
    private long numberOfDaysApproved;
    private LocalDate commencementDate;
    private LocalDate endDate;
    private LocalDate resumptionDate;
    private String status;
}
