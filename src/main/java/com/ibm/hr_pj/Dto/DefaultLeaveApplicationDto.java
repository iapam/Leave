package com.ibm.hr_pj.Dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DefaultLeaveApplicationDto {
    private String firstName;
    private String lastName;
    private long remainingDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private String userId;
}
