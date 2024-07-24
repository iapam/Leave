package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Dto.EmployeeDetailsRegistrationRequest;
import com.ibm.hr_pj.Dto.LeaveRequest;
import com.ibm.hr_pj.Models.EmployeeDetails;
import com.ibm.hr_pj.Models.Login;
import com.ibm.hr_pj.Repositories.EmployeeDetailsRepository;
import com.ibm.hr_pj.Repositories.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class EmployeeService implements UserDetailsService {
    private final LoginRepository loginRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    @Override
    public UserDetails loadUserByUsername(String staffId) throws UsernameNotFoundException {

        return loginRepository
                .findLoginByEmployeeId(staffId)
                .orElseThrow(()->new IllegalStateException("See Your HR for registration"));
    }
    public EmployeeDetailsRegistrationRequest employeeDetails(Login login) {
        EmployeeDetails employeeDetails=employeeDetailsRepository.findEmployeeDetailsByLogin(login);

        return new EmployeeDetailsRegistrationRequest(employeeDetails.getName(), employeeDetails.getUnit(),
                employeeDetails.getAnnualSalary(), employeeDetails.getGrade(), employeeDetails.getProfession(),
                employeeDetails.getEmploymentType(), employeeDetails.getPhoneNumber(), employeeDetails.getEmail(),
                employeeDetails.getAddress());
    }
    public void leaveApplication(LeaveRequest leaveRequest) throws ParseException {
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate=LocalDate.parse("2020-09-12",dateTimeFormatter);
        Month month=Month.from(localDate);
        DayOfWeek day=DayOfWeek.from(localDate);
        System.out.println(day);
        System.out.println(month);

    }
}
