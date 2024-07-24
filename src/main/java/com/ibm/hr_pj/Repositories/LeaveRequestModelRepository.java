package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.Leave_RequestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestModelRepository extends JpaRepository<Leave_RequestModel,Long> {
}
