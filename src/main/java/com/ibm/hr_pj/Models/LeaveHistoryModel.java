package com.ibm.hr_pj.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LeaveHistoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long hId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String dateApplied;
    private String leaveType;
    private String phoneNumber;
    private String status;
    private String employeeId;
    private String endedDate;
    @ManyToOne
    @JoinColumn
    private Leave_RequestModel leave_requestModel;

    public LeaveHistoryModel(String name, LocalDate startDate, LocalDate endDate, String dateApplied, String leaveType, String phoneNumber, String status,String employeeId,Leave_RequestModel leave_requestModel) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateApplied = dateApplied;
        this.leaveType = leaveType;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.employeeId=employeeId;
        this.leave_requestModel=leave_requestModel;
    }
}
