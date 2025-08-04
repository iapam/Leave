package com.ibm.hr_pj.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDetailsDto {
    private long id;
    private String department;
    private String name;
    private String dateApplied;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType;
    private long numberOfDays;
}
