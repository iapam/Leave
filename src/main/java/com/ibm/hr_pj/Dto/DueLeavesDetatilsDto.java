package com.ibm.hr_pj.Dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DueLeavesDetatilsDto {
    private String name;
    private LocalDate commencementDate;
    private LocalDate endDate;
    private long numberOfDateOverDue;
    private Long leaveId;
    private String dateApplied;
    private String employeeId;
    private String unitName;
}
