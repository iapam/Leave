package com.ibm.hr_pj.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LeaveRequest {
    private String startDate;
    private String endDate;
    private String leaveType;
    private long remainingDate;
}
