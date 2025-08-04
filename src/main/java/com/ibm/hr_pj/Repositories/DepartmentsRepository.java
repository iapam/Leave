package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DepartmentsRepository extends JpaRepository<Departments,Long> {
    Departments findByDepartmentName(String departmentName);
    Departments findByDepartmentId(Long departmentID);
    Departments findBySupervisorID(String supervisorID);
    @Modifying
    @Transactional
    @Query("update Departments set numberOfEmployeesAtTheDepartment=:numberOfEmployeesAtDepartment where departmentId=:departmentId")
    int updateDepartments(Long departmentId,int numberOfEmployeesAtDepartment);
}

