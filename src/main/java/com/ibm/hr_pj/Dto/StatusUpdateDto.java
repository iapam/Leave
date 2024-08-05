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
    private String unit;
    private String status;
    private String updateDate;
    private String endDate;
    private String reason;
    private boolean replacementNeeded;
}
