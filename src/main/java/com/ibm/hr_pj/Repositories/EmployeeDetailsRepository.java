package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.EmployeeDetails;
import com.ibm.hr_pj.Models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails,String> {
    EmployeeDetails findEmployeeDetailsByLogin(Login login);

}
