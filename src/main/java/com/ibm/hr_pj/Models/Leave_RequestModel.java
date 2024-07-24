package com.ibm.hr_pj.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime dateApplied;
    private String nurseManagerStatus;
    private String administratorStatus;
    private String medSupStatus;
    @ManyToOne
    @JoinColumn
    private Login login;
}
