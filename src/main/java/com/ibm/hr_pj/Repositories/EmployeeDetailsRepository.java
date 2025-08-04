package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.Departments;
import com.ibm.hr_pj.Models.EmployeeDetail;
import com.ibm.hr_pj.Models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetail,String> {
    EmployeeDetail findEmployeeDetailsByLogin(Login login);
    EmployeeDetail findByPhoneNumber(String phoneNumber);
    List<EmployeeDetail>findEmployeeDetailByDepartments(Departments departmentId);
    @Modifying
    @Transactional
    @Query("update EmployeeDetail set status=:status where login=:employeeId")
    int updateDetailsStatus(Login employeeId,String status);

}
