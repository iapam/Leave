package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.EmployeeDetail;
import com.ibm.hr_pj.Models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetail,String> {
    EmployeeDetail findEmployeeDetailsByLogin(Login login);

}
