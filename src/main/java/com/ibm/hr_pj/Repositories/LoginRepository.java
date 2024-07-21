package com.ibm.hr_pj.Repositories;

import com.ibm.hr_pj.Models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
@EnableJpaRepositories
public interface LoginRepository extends JpaRepository<Login,String> {
Optional<Login> findLoginByEmployeeId(String employeeId);
Login findByEmployeeId(String employeeId);
}
