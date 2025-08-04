package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.LeaveHistoryModel;
import com.ibm.hr_pj.Models.Leave_RequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface LeaveHistoryRepository extends JpaRepository<LeaveHistoryModel,Long> {
    LeaveHistoryModel findByEmployeeId(String employeeId);
    @Transactional
    @Modifying
    @Query("update LeaveHistoryModel set status=:status,endedDate=:endedDate where leave_requestModel=:leaveRequestModel")
    int updateLeaveHistoryModelStatus(String status, Leave_RequestModel leaveRequestModel, String endedDate);
}
