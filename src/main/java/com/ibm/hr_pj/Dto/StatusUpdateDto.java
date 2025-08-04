package com.ibm.hr_pj.Dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class StatusUpdateDto {
    private Long leaveId;
    private String department;
    private String status;
    private String updateDate;
    private String endDate;
    private String reason;
    private boolean replacementNeeded;

    public StatusUpdateDto(Long leaveId, String status,String department,boolean replacementNeeded) {
        this.leaveId = leaveId;
        this.status = status;
        this.department=department;
        this.replacementNeeded=replacementNeeded;
    }
}
