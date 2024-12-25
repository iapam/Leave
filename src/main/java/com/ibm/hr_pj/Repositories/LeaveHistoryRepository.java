package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.LeaveHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveHistoryRepository extends JpaRepository<LeaveHistoryModel,Long> {
}
