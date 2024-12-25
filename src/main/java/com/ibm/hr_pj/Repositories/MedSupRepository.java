package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.Leave_RequestModel;
import com.ibm.hr_pj.Models.MedSupApprovalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface MedSupRepository extends JpaRepository<MedSupApprovalModel,Long> {
    MedSupApprovalModel findByLeaveId(Leave_RequestModel leaveRequestModel);
    @Modifying
    @Transactional
    @Query("update MedSupApprovalModel set status=:status where id=:id")
    void updateMedSupApprovalModel(Long id,String status);
}
