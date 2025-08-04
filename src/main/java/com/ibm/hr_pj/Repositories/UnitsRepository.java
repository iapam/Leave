package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UnitsRepository extends JpaRepository<Unit,Long> {
    Unit findByUnitName(String unitName);
    Unit findByUnitId(Long id);
    @Modifying
    @Transactional
    @Query("update Unit set numberOfEmployeesAtTheUnit=:numberOFEmployeesAtUnit where unitId=:unitId")
    int updateUnit(Long unitId,int numberOFEmployeesAtUnit);
}
