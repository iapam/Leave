package com.ibm.hr_pj.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Leave_RequestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate statusUpdateDateU;
    private LocalDate statusUpdateDateA;
    private LocalDate statusUpdateDateN;
    private LocalDate statusUpdateDateM;
    private String dateApplied;
    private String nurseManagerStatus;
    private long numberOfDays;
    private String medSupStatus;
    private String supervisorStatus;
    private String leaveStatus;
    private String reasons;
    private boolean replacementNeeded;
    @ManyToOne
    @JoinColumn
    private Departments department;
    @ManyToOne
    @JoinColumn
    private Login login;

    public Leave_RequestModel(String leaveType, LocalDate startDate, LocalDate endDate,String dateApplied,
                              String leaveStatus,String nurseManagerStatus,long numberOfDays,String supervisorStatus,
                              String medSupStatus,Login login,Departments department) {
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.dateApplied = dateApplied;
        this.endDate=endDate;
        this.login=login;
        this.leaveStatus=leaveStatus;
        this.nurseManagerStatus=nurseManagerStatus;
        this.supervisorStatus=supervisorStatus;
        this.numberOfDays=numberOfDays;
        this.medSupStatus=medSupStatus;
        this.department=department;
    }
}
