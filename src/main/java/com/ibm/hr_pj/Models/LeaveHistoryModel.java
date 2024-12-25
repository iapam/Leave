package com.ibm.hr_pj.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private LocalDateTime dateApplied;
    private String leaveType;
    private String phoneNumber;
    private String status;
    private String employeeId;

    public LeaveHistoryModel(String name, LocalDate startDate, LocalDate endDate, LocalDateTime dateApplied, String leaveType, String phoneNumber, String status,String employeeId) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateApplied = dateApplied;
        this.leaveType = leaveType;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.employeeId=employeeId;
    }
}
