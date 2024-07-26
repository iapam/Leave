package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.Leave_RequestModel;
import com.ibm.hr_pj.Models.Login;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestModelRepository extends JpaRepository<Leave_RequestModel,Long> {
    List<Leave_RequestModel>findByLogin(Login login);
}
