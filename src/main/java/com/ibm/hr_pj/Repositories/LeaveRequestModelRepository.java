package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.Departments;
import com.ibm.hr_pj.Models.Leave_RequestModel;
import com.ibm.hr_pj.Models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestModelRepository extends JpaRepository<Leave_RequestModel,Long> {
    List<Leave_RequestModel>findByLogin(Login login);
    Leave_RequestModel findLeave_RequestModelById(Long id);
    @Modifying
    @Transactional
    @Query("update Leave_RequestModel set supervisorStatus=:SupervisorStatus,replacementNeeded=:replacement," +
            "statusUpdateDateU=:updateDate,reasons=:reasons,numberOfDays=:numberOfDays where id=:leaveID")
    void updateUnitHeadStatusApproved(String SupervisorStatus, boolean replacement,
                              LocalDate updateDate,Long leaveID,String reasons,long numberOfDays);
    @Modifying
    @Transactional
    @Query("update Leave_RequestModel set supervisorStatus=:SupervisorStatus," +
            "reasons=:reason,statusUpdateDateU=:updateDate,endDate=:endDate,replacementNeeded=:replacement where id=:leaveID")
    void updateUnitHeadStatusDenied(String SupervisorStatus, String reason,
                                      LocalDate updateDate,Long leaveID,LocalDate endDate, boolean replacement);
    List<Leave_RequestModel> findLeave_RequestModelByDepartmentAndSupervisorStatus(Departments department, String supervisorStatus);
}
