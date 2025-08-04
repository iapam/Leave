package com.ibm.hr_pj.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LeaveFormDto {
    private String unit;
    private String staffID;
    private String surname;
    private String otherNames;
    private String annualSalary;
    private String grade;
    private String profession;
    private String speciality;
    private String employeeType;
    private LocalDate effectiveDateForLeave;
    private LocalDate dateResume;
    private String address;
    private String phoneNumber;
    private String dateApplied;
    private String status;
    private long numberOfDaysRecommended;
    private boolean replacementNeeded;
    private String supervisorName;
    private LocalDate dateRecommended;
    private long numberOfDaysApproved;
    private LocalDate commencementDate;
    private LocalDate endDate;
    private LocalDate resumptionDate;
    private String medSupName;
    private String dateApproved;
}
