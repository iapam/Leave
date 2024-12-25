package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Dto.*;
import com.ibm.hr_pj.Models.*;
import com.ibm.hr_pj.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class EmployeeService implements UserDetailsService {
    private final LoginRepository loginRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final LeaveRequestModelRepository leaveRequestModelRepository;
    private final MedSupRepository medSupRepository;
    private final LeaveHistoryRepository leaveHistoryRepository;
    private LeaveHistoryDto leaveHistoryDto;
    @Override
    public UserDetails loadUserByUsername(String staffId) throws UsernameNotFoundException {

        return loginRepository
                .findLoginByEmployeeId(staffId)
                .orElseThrow(()->new IllegalStateException("See Your HR for registration"));
    }
    public EmployeeDetailsRegistrationRequest employeeDetails(Login login) {
        EmployeeDetail employeeDetails=employeeDetailsRepository.findEmployeeDetailsByLogin(login);

        return new EmployeeDetailsRegistrationRequest(employeeDetails.getName(), employeeDetails.getUnit(),
                employeeDetails.getAnnualSalary(), employeeDetails.getGrade(), employeeDetails.getProfession(),
                employeeDetails.getEmploymentType(), employeeDetails.getPhoneNumber(), employeeDetails.getEmail(),
                employeeDetails.getAddress());
    }
    public String leaveApplication(LeaveRequest leaveRequest,Login login) throws ParseException {
        long totalNumberOfDaysLeaveEnjoyed=0;
        List<Leave_RequestModel>leaveRequestModels=leaveRequestModelRepository.findByLogin(login);
        EmployeeDetail employeeDetails=employeeDetailsRepository.findEmployeeDetailsByLogin(login);
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate=LocalDate.parse(leaveRequest.getStartDate(),dateTimeFormatter);
        LocalDate endDate=LocalDate.parse(leaveRequest.getEndDate(),dateTimeFormatter);
         String apply_year=String.valueOf(startDate.getYear());
         for (Leave_RequestModel checkPreviousEndDates:leaveRequestModels){
             System.out.println("The date "+checkPreviousEndDates.getEndDate());
             if(checkPreviousEndDates.getEndDate().isBefore(startDate)|| checkPreviousEndDates.getEndDate().equals(endDate)){
                 return "previous leave is not been enjoyed finish";
             }
         }
        for(Leave_RequestModel leaveRequestModel:leaveRequestModels){
            System.out.println("The number of days"+leaveRequestModel.getNumberOfDays());
                totalNumberOfDaysLeaveEnjoyed=totalNumberOfDaysLeaveEnjoyed+leaveRequestModel.getNumberOfDays();
            System.out.println("The sum"+totalNumberOfDaysLeaveEnjoyed);
            String applied_year=String.valueOf(leaveRequestModel.getStartDate().getYear());

            if(apply_year.equals(applied_year)){
                long numberOfDaysapplying=leaveDaysCalculator(startDate,endDate);
                long daysRemain=employeeDetails.getNumberOfDaysEntitled()-totalNumberOfDaysLeaveEnjoyed;
                if(numberOfDaysapplying>daysRemain){
                    LocalDate date=calculateEndDate(startDate,daysRemain);
                    String endDateCalculate=String.valueOf(date);
                    return "end date must be "+endDateCalculate+" if the start date is "+startDate+" because you have "+daysRemain+" days remaining for this year's leave";
                }
                if(daysRemain==0) {
                    return "you already use all your number of days for leave";
                }
            }
        }
        String thisYear=String.valueOf(LocalDate.now().getYear());
        if(!thisYear.equals(apply_year)){
            return "Leave application must be this year";
        }
        if(leaveRequest.getStartDate().equals(leaveRequest.getEndDate())){
            return "Leave Start date shouldn't be equal to end date";
        }
        if(startDate.isBefore(LocalDate.now())){
            return "Request date shouldn't before today or you cannot backdate";
        }
        leaveRequestModelRepository.save(new Leave_RequestModel(leaveRequest.getLeaveType(),
                startDate,endDate,LocalDateTime.now(),"pending","pending",0,"pending","pending",login));

        return "leave applied";
    }

    public String updateLeaveStatus(StatusUpdateDto statusUpdateDto, Login login) {
        EmployeeDetail unitHead=employeeDetailsRepository.findEmployeeDetailsByLogin(login);

        if(unitHead.getUnit().equals(statusUpdateDto.getUnit())){
            Leave_RequestModel empLeave=leaveRequestModelRepository.findLeave_RequestModelById(statusUpdateDto.getLeaveId());
            if(statusUpdateDto.getStatus().equals("approved")){
            long numberOfDays=leaveDaysCalculator(empLeave.getStartDate(),empLeave.getEndDate());
                System.out.println("The number of days"+numberOfDays);
                leaveRequestModelRepository.updateUnitHeadStatusApproved(statusUpdateDto.getStatus(), statusUpdateDto.isReplacementNeeded(),
                    LocalDate.now(), statusUpdateDto.getLeaveId(),null,numberOfDays);
                MedSupApprovalModel updateLeaveToMedsup=new MedSupApprovalModel(
                        numberOfDays, empLeave.getStartDate(),empLeave.getEndDate(),empLeave.getEndDate().minusDays(1),"pending",empLeave
                );
                MedSupApprovalModel alreadyUpdateMedSupLeave=medSupRepository.findByLeaveId(empLeave);
                if(alreadyUpdateMedSupLeave!=null){
                    return "This leave has already been submitted to the medsup for approval";
                }
                medSupRepository.save(updateLeaveToMedsup);
                System.out.println("evidence "+statusUpdateDto.isReplacementNeeded());
                return "This leave has been approved";
            }else {
                if(statusUpdateDto.getReason()==null){
                    return "Specify reasons";
                }
               leaveRequestModelRepository.updateUnitHeadStatusDenied(statusUpdateDto.getStatus(),
                       statusUpdateDto.getReason(),LocalDate.now(),statusUpdateDto.getLeaveId(),null,false);
               return "This leave has been denied";
            }

        }else {
            return "Employee not from your unit";
        }

    }
    public static long leaveDaysCalculator(LocalDate startDate,LocalDate endDate){
        long daysBetween= ChronoUnit.DAYS.between(startDate,endDate);
        long workingDays=0;
        for (int i=0;i<=daysBetween;i++){
            LocalDate currentDate=startDate.plusDays(i);
            DayOfWeek dayOfWeek=currentDate.getDayOfWeek();
            if (dayOfWeek!=DayOfWeek.SATURDAY&&dayOfWeek!=DayOfWeek.SUNDAY){
                workingDays++;
            }
        }
  return workingDays;
    }
    public static LocalDate calculateEndDate(LocalDate startDate,long days){
        LocalDate currentDate=startDate;
        long addedDays=0;
        while (addedDays<days){
            currentDate=currentDate.plusDays(1);
            if(currentDate.getDayOfWeek()!=DayOfWeek.SATURDAY && currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY){
                addedDays++;
            }
        }
        return currentDate;

    }

    public String medsupStatusUpdate(MedSupStatusDto medSupStatusDto){
        //todo medsup find this leave from supervisors table to used it find the person who applied this leave
        Leave_RequestModel empIdLeave=leaveRequestModelRepository.findLeave_RequestModelById(medSupStatusDto.getId());
        EmployeeDetail employeeDetail=employeeDetailsRepository.findEmployeeDetailsByLogin(empIdLeave.getLogin());
        medSupRepository.updateMedSupApprovalModel(medSupStatusDto.getId(), medSupStatusDto.getStatus());
        leaveHistoryRepository.save(new LeaveHistoryModel(employeeDetail.getName(),
                empIdLeave.getStartDate(),empIdLeave.getEndDate(),
                empIdLeave.getDateApplied(),empIdLeave.getLeaveType(),
                employeeDetail.getPhoneNumber(),"onLeave",employeeDetail.getPhoneNumber()));
        //todo how to confirm the leave after after approvals
        return "Leave successfully approved";
    }
}
