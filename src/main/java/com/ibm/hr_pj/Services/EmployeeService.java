package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Dto.EmployeeDetailsRegistrationRequest;
import com.ibm.hr_pj.Dto.LeaveRequest;
import com.ibm.hr_pj.Models.EmployeeDetails;
import com.ibm.hr_pj.Models.Leave_RequestModel;
import com.ibm.hr_pj.Models.Login;
import com.ibm.hr_pj.Repositories.EmployeeDetailsRepository;
import com.ibm.hr_pj.Repositories.LeaveRequestModelRepository;
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
import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class EmployeeService implements UserDetailsService {
    private final LoginRepository loginRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final LeaveRequestModelRepository leaveRequestModelRepository;
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
    public String leaveApplication(LeaveRequest leaveRequest,Login login) throws ParseException {
        List<Leave_RequestModel>leaveRequestModels=leaveRequestModelRepository.findByLogin(login);
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate=LocalDate.parse(leaveRequest.getStartDate(),dateTimeFormatter);
         String apply_year=String.valueOf(startDate.getYear());
        for(Leave_RequestModel leaveRequestModel:leaveRequestModels){
            String applied_year=String.valueOf(leaveRequestModel.getStartDate().getYear());
            if(apply_year.equals(applied_year)){
                return "you already applied for this year";
            }
        }
        String thisYear=String.valueOf(LocalDate.now().getYear());
        if(!thisYear.equals(apply_year)){
            return "Leave application must be this year";
        }
        if(startDate.isBefore(LocalDate.now())){
            return "Request date shouldn't before today or you cannot backdate";
        }
        leaveRequestModelRepository.save(new Leave_RequestModel(leaveRequest.getLeaveType(),
                startDate,LocalDateTime.now(),"pending",login));

        return "leave applied";
    }
}
