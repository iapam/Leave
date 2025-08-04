package com.ibm.hr_pj.Dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
 public class HRStats {
    private int numberOfDueLeaves;
    private int numberOfEndedLeaves;
    private int numberOfEmployeeOnSite;
    private int numberOfEmployeeOnLeave;
    private int numberOfEmployee;
}
