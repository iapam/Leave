package com.ibm.hr_pj.Models;

import com.ibm.hr_pj.Repositories.LeaveRequestModelRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MedSupApprovalModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long numberOfDaysApproved;
    private LocalDate commencementDate;
    private LocalDate endDate;
    private LocalDate resumptionDate;
    private String status;
    @ManyToOne
    @JoinColumn
    private Leave_RequestModel leaveId;

    public MedSupApprovalModel(long numberOfDaysApproved, LocalDate commencementDate, LocalDate endDate, LocalDate resumptionDate, String status,Leave_RequestModel leaveId) {
        this.numberOfDaysApproved = numberOfDaysApproved;
        this.commencementDate = commencementDate;
        this.endDate = endDate;
        this.resumptionDate = resumptionDate;
        this.status = status;
        this.leaveId=leaveId;
    }
}
