package com.ibm.hr_pj.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LeaveHistoryDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime dateApplied;
    private String leaveType;
    private String phoneNumber;
    private String status;
}
