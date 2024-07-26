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
    private LocalDateTime dateApplied;
    private String nurseManagerStatus;
    private String administratorStatus;
    private String medSupStatus;
    @ManyToOne
    @JoinColumn
    private Login login;

    public Leave_RequestModel(String leaveType, LocalDate startDate, LocalDateTime dateApplied,Login login) {
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.dateApplied = dateApplied;
        this.login=login;
    }
}
