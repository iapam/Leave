package com.ibm.hr_pj.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    private LocalDateTime dateApplied;
    private String nurseManagerStatus;
    private String administratorStatus;
    private String medSupStatus;
    private String unitHeadStatus;
    private String leaveStatus;
    private String reasons;
    private boolean replacementNeeded;
    @ManyToOne
    @JoinColumn
    private Login login;

    public Leave_RequestModel(String leaveType, LocalDate startDate, LocalDateTime dateApplied,
                              String leaveStatus,String nurseManagerStatus,String administratorStatus,String unitHeadStatus,
                              String medSupStatus,Login login) {
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.dateApplied = dateApplied;
        this.login=login;
        this.leaveStatus=leaveStatus;
        this.nurseManagerStatus=nurseManagerStatus;
        this.unitHeadStatus=unitHeadStatus;
        this.administratorStatus=administratorStatus;
        this.medSupStatus=medSupStatus;
    }
}
