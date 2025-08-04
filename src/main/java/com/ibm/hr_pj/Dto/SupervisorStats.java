package com.ibm.hr_pj.Dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorStats {
    private int totalApprovedLeaves;
    private int totalNumberOfEmployeesInDepartment;
    private int totalLeavesPending;
}
