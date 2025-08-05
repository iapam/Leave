package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Dto.*;
import com.ibm.hr_pj.Models.*;
import com.ibm.hr_pj.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService implements UserDetailsService {
    private final LoginRepository loginRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final LeaveRequestModelRepository leaveRequestModelRepository;
    private final MedSupRepository medSupRepository;
    private final LeaveHistoryRepository leaveHistoryRepository;
    private final DepartmentsRepository departmentsRepository;
    private final UnitsRepository unitsRepository;
    private final SmsSender smsSender;
    private LeaveHistoryDto leaveHistoryDto;
    @Autowired
    private EmailSenderService emailSenderService;
    @Override
    public UserDetails loadUserByUsername(String staffId) throws UsernameNotFoundException {

        return loginRepository
                .findLoginByEmployeeId(staffId)
                .orElseThrow(()->new IllegalStateException("See Your HR for registration"));
    }
    public EmployeeDetailsRegistrationRequest employeeDetails(Login login) {
        EmployeeDetail employeeDetails=employeeDetailsRepository.findEmployeeDetailsByLogin(login);

        return new EmployeeDetailsRegistrationRequest(employeeDetails.getName(), employeeDetails.getUnitId().getUnitName(),
                employeeDetails.getAnnualSalary(), employeeDetails.getGrade(), employeeDetails.getProfession(),
                employeeDetails.getEmploymentType(), employeeDetails.getPhoneNumber(), employeeDetails.getEmail(),
                employeeDetails.getAddress(),employeeDetails.getDepartments().getDepartmentName());
    }
    public String leaveApplication(LeaveRequest leaveRequest,Login login) throws ParseException, IOException {
        DateTimeFormatter dateTimeFormatterS= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        long totalNumberOfDaysLeaveEnjoyed=0;
        EmployeeDetail employeeDetail=employeeDetailsRepository.findEmployeeDetailsByLogin(login);
        List<Leave_RequestModel>leaveRequestModels=leaveRequestModelRepository.findByLogin(login);
        EmployeeDetail employeeDetails=employeeDetailsRepository.findEmployeeDetailsByLogin(login);
        Departments supervisorDetails=departmentsRepository.findByDepartmentId(employeeDetail.getDepartments().getDepartmentId());
        Unit unitName=unitsRepository.findByUnitId(employeeDetail.getUnitId().getUnitId());
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate=LocalDate.parse(leaveRequest.getStartDate(),dateTimeFormatter);
        LocalDate endDate=LocalDate.parse(leaveRequest.getEndDate(),dateTimeFormatter).minusDays(1);
        long numberOfDays=leaveDaysCalculator(startDate,endDate);
         String apply_year=String.valueOf(startDate.getYear());
         String weekend=startDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        System.out.println("The test= "+weekend);
         if(weekend.equals("Saturday") || weekend.equals("Sunday")){
             System.out.println("Start date cannot be weekend");
             return "Start date cannot be weekend";
         }
         for(Leave_RequestModel checkLeaveStatus:leaveRequestModels){
             if(checkLeaveStatus.getSupervisorStatus().equals("pending")){
                 return "You have pending leave request";
             }
         }
         if(numberOfDays>employeeDetail.getNumberOfDaysEntitled()){
             LocalDate date=calculateEndDate(startDate,employeeDetail.getNumberOfDaysEntitled());
             String endDateCalculate=String.valueOf(date);
             return "Base on your start date your end date can be "+endDateCalculate;
         }
         for (Leave_RequestModel checkPreviousEndDates:leaveRequestModels){
             System.out.println("The date "+checkPreviousEndDates.getEndDate());
             System.out.println("The pr date "+endDate);
             if(endDate.isBefore(checkPreviousEndDates.getEndDate()) || checkPreviousEndDates.getEndDate().equals(endDate)){
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
        if(endDate.isBefore(startDate)){
            return "end date must not be before start date";
        }
        if(startDate.isBefore(LocalDate.now())){
            return "Request date shouldn't before today or you cannot backdate";
        }
        leaveRequestModelRepository.save(new Leave_RequestModel(leaveRequest.getLeaveType(),
                startDate,endDate,LocalDateTime.now().format(dateTimeFormatterS),"pending","pending",0,"pending","pending",login,employeeDetail.getDepartments()));
        smsSender.sendSupervisorSms(supervisorDetails.getSupervisorPhoneNumber(), employeeDetail.getName(),unitName.getUnitName());


        return "leave applied";
    }

    public String updateLeaveStatus(StatusUpdateDto statusUpdateDto, Login login) throws IOException {
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateTimeFormatters= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        EmployeeDetail unitHead=employeeDetailsRepository.findEmployeeDetailsByLogin(login);
      Departments departmentOfEmployee=departmentsRepository.findByDepartmentId(unitHead.getDepartments().getDepartmentId());
        if(departmentOfEmployee.getDepartmentName().equals(statusUpdateDto.getDepartment())){
            System.out.println("The id "+statusUpdateDto.getLeaveId());
            Leave_RequestModel empLeave=leaveRequestModelRepository.findLeave_RequestModelById(statusUpdateDto.getLeaveId());
            EmployeeDetail applicant=employeeDetailsRepository.findEmployeeDetailsByLogin(empLeave.getLogin());
            Unit applicantUnit=unitsRepository.findByUnitId(applicant.getUnitId().getUnitId());
            if(statusUpdateDto.getStatus().equals("approved")){
            long numberOfDays=leaveDaysCalculator(empLeave.getStartDate(),empLeave.getEndDate());
                System.out.println("The number of days"+numberOfDays);
                leaveRequestModelRepository.updateUnitHeadStatusApproved(statusUpdateDto.getStatus(), statusUpdateDto.isReplacementNeeded(),
                    LocalDate.now(), statusUpdateDto.getLeaveId(),null,numberOfDays);

                MedSupApprovalModel updateLeaveToMedsup=new MedSupApprovalModel(
                        numberOfDays, empLeave.getStartDate(),empLeave.getEndDate(),empLeave.getEndDate().plusDays(1),"pending",empLeave,LocalDateTime.now().format(dateTimeFormatter)
                );
                MedSupApprovalModel alreadyUpdateMedSupLeave=medSupRepository.findByLeaveId(empLeave);
                if(alreadyUpdateMedSupLeave!=null){
                    return "This leave has already been submitted to the medsup for approval";
                }
                medSupRepository.save(updateLeaveToMedsup);
                smsSender.sendMedsupSms("0593606766",applicant.getName(),applicantUnit.getUnitName());
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

    public static LocalDate leaveStartDateCalc(LocalDate toDaysDate,long days){
        LocalDate currentDate=toDaysDate;
        long addedDays=0;
        while (addedDays<days){
            currentDate=currentDate.plusDays(1);
                addedDays++;
        }
        return currentDate;
    }

    public void medsupStatusUpdate(Long id,String status){
        //todo medsup find this leave from supervisors table to used it find the person who applied this leave
        Leave_RequestModel empIdLeave=leaveRequestModelRepository.findLeave_RequestModelById(id);
        EmployeeDetail employeeDetail=employeeDetailsRepository.findEmployeeDetailsByLogin(empIdLeave.getLogin());
        medSupRepository.updateMedSupApprovalModel(empIdLeave, status);
        leaveHistoryRepository.save(new LeaveHistoryModel(employeeDetail.getName(),
                empIdLeave.getStartDate(),empIdLeave.getEndDate(),
                empIdLeave.getDateApplied(),empIdLeave.getLeaveType(),
                employeeDetail.getPhoneNumber(),"onLeave",employeeDetail.getLogin().getEmployeeId(),empIdLeave));
        emailSenderService.sendEmail(employeeDetail.getEmail(),"Confirmation of Leave","Your leave application have been approved " +
                "successfully, go to the HR office and sign on the hard copy for it to be official");
        System.out.println("The status "+status);

        //todo how to confirm the leave after after approvals

    }

    public DefaultLeaveApplicationDto getEmployeeLeaveDetails(Login user) {
        long totalNumberOfDaysLeaveEnjoyed=0;
        LocalDate startDate=leaveStartDateCalc(LocalDate.now(),30);
        EmployeeDetail employeeDetail=employeeDetailsRepository.findEmployeeDetailsByLogin(user);
        List<Leave_RequestModel>leaveRequestModels=leaveRequestModelRepository.findByLogin(user);
            long daysRemain=employeeDetail.getNumberOfDaysEntitled()-totalNumberOfDaysLeaveEnjoyed;
        for(Leave_RequestModel leaveRequestModel:leaveRequestModels){
            totalNumberOfDaysLeaveEnjoyed=totalNumberOfDaysLeaveEnjoyed+leaveRequestModel.getNumberOfDays();
            daysRemain=employeeDetail.getNumberOfDaysEntitled()-totalNumberOfDaysLeaveEnjoyed;
        }
        LocalDate endDate=calculateEndDate(startDate,daysRemain);
        DefaultLeaveApplicationDto defaultLeaveApplicationDto=new DefaultLeaveApplicationDto(employeeDetail.getName(),
                employeeDetail.getName(),daysRemain,startDate,endDate,user.getEmployeeId());
        return defaultLeaveApplicationDto;
    }
public List<LeaveDetailsDto> leaveDetailsDto(Departments department){
        List<LeaveDetailsDto>allLeaveRequests=new ArrayList<>();
List<Leave_RequestModel> leave_requestModel=leaveRequestModelRepository.findLeave_RequestModelByDepartmentAndSupervisorStatus(department,"pending");
for (Leave_RequestModel leaveRequestModel:leave_requestModel){
    EmployeeDetail detail=employeeDetailsRepository.findEmployeeDetailsByLogin(leaveRequestModel.getLogin());
    LeaveDetailsDto detailsDto=new LeaveDetailsDto(leaveRequestModel.getId(),leaveRequestModel.getDepartment().getDepartmentName(),detail.getName(),leaveRequestModel.getDateApplied(),
            leaveRequestModel.getStartDate(),leaveRequestModel.getEndDate(),leaveRequestModel.getLeaveType(),leaveDaysCalculator(leaveRequestModel.getStartDate(),leaveRequestModel.getEndDate()));
    allLeaveRequests.add(detailsDto);
}
return allLeaveRequests;
}
public List<MedSupStatusDto>allPendingMesupLeaves(String status){
        List<MedSupStatusDto>allPendingLeavesInMedsup=new ArrayList<>();
        List<MedSupApprovalModel> listOfLeaves=medSupRepository.findMedSupApprovalModelByStatus(status);
        for (MedSupApprovalModel leaves: listOfLeaves){
            if(LocalDate.now().isBefore(leaves.getResumptionDate())){
                Leave_RequestModel leaveInSupervisor=leaveRequestModelRepository.findLeave_RequestModelById(leaves.getLeaveId().getId());
                EmployeeDetail employeeDetails=employeeDetailsRepository.findEmployeeDetailsByLogin(leaveInSupervisor.getLogin());
                MedSupStatusDto leaveDetails=new MedSupStatusDto();
                leaveDetails.setId(leaves.getLeaveId());
                leaveDetails.setEndDate(leaves.getEndDate());
                leaveDetails.setCommencementDate(leaves.getCommencementDate());
                leaveDetails.setNumberOfDaysApproved(leaves.getNumberOfDaysApproved());
                leaveDetails.setResumptionDate(leaves.getResumptionDate());
                leaveDetails.setStatus(leaves.getStatus());
                leaveDetails.setName(employeeDetails.getName());
                leaveDetails.setDepartmentsId(employeeDetails.getDepartments());
                leaveDetails.setUnitID(employeeDetails.getUnitId());
                leaveDetails.setEmployeeDetailsID(employeeDetails.getPhoneNumber());
                allPendingLeavesInMedsup.add(leaveDetails);
            }

        }
        return allPendingLeavesInMedsup;
}
public List<LeaveFormDto> leaveFormDto(Long leaveID,String employeePhoneNumber,Long departmentId){
        List<LeaveFormDto> leaveFormList=new ArrayList<>();
        LeaveFormDto leaveForm=new LeaveFormDto();
        Leave_RequestModel leaveRequestModel=leaveRequestModelRepository.findLeave_RequestModelById(leaveID);
        EmployeeDetail employeeDetails=employeeDetailsRepository.findByPhoneNumber(employeePhoneNumber);
        Departments departments=departmentsRepository.findByDepartmentId(departmentId);
        MedSupApprovalModel medSupApprovalModel=medSupRepository.findByLeaveId(leaveRequestModel);
        Unit unit=unitsRepository.findByUnitId(employeeDetails.getUnitId().getUnitId());
        Login login=loginRepository.findByEmployeeRole(EmployeeRole.MEDSUP);
        EmployeeDetail medsupDetails=employeeDetailsRepository.findEmployeeDetailsByLogin(login);
        leaveForm.setSurname(employeeDetails.getName());
        leaveForm.setAddress(employeeDetails.getAddress());
        leaveForm.setEndDate(medSupApprovalModel.getEndDate());
        leaveForm.setCommencementDate(medSupApprovalModel.getCommencementDate());
        leaveForm.setDateApplied(leaveRequestModel.getDateApplied());
        leaveForm.setAnnualSalary(employeeDetails.getAnnualSalary());
        leaveForm.setEmployeeType(employeeDetails.getEmploymentType());
        leaveForm.setGrade(employeeDetails.getGrade());
        leaveForm.setDateApproved(medSupApprovalModel.getApprovedDate());
        leaveForm.setDateResume(medSupApprovalModel.getResumptionDate());
        leaveForm.setResumptionDate(medSupApprovalModel.getResumptionDate());
        leaveForm.setStatus(medSupApprovalModel.getStatus());
        leaveForm.setUnit(unit.getUnitName());
        leaveForm.setDateRecommended(leaveRequestModel.getStatusUpdateDateU());
        leaveForm.setEffectiveDateForLeave(leaveRequestModel.getStartDate());
        leaveForm.setMedSupName(medsupDetails.getName());
        leaveForm.setNumberOfDaysRecommended(leaveRequestModel.getNumberOfDays());
        leaveForm.setPhoneNumber(employeeDetails.getPhoneNumber());
        leaveForm.setProfession(employeeDetails.getProfession());
        leaveForm.setReplacementNeeded(leaveRequestModel.isReplacementNeeded());
        leaveForm.setStaffID(login.getEmployeeId());
        leaveForm.setSupervisorName(departments.getDepartmentSupervisor());
        leaveForm.setNumberOfDaysApproved(medSupApprovalModel.getNumberOfDaysApproved());
        leaveFormList.add(leaveForm);
        return leaveFormList;
}
public int totalNumberOfEmployeesInDepartment(String supervisor_id){
        Departments supervisorID=departmentsRepository.findBySupervisorID(supervisor_id);
    System.out.println(supervisorID.getNumberOfEmployeesAtTheDepartment());
        return supervisorID.getNumberOfEmployeesAtTheDepartment();
}
public int totalApprovedLeaves(String supervisor_id ){
        int totalLeavesApprove=0;
    Departments supervisorID=departmentsRepository.findBySupervisorID(supervisor_id);
    String thisYear=String.valueOf(LocalDate.now().getYear());
   List<Leave_RequestModel>recommendedLeavesRequest=leaveRequestModelRepository.findLeave_RequestModelByDepartmentAndSupervisorStatus(supervisorID,"approved");
   for (Leave_RequestModel leaveRequestModel:recommendedLeavesRequest){
       String recommendedLeaveYear=String.valueOf(leaveRequestModel.getStatusUpdateDateU().getYear());
       if(thisYear.equals(recommendedLeaveYear)){
           totalLeavesApprove=totalLeavesApprove+1;
       }
   }

        return totalLeavesApprove;
}
public int totalPendingLeave(String supervisor_id){
    int totalLeavesPending=0;
    Departments supervisorID=departmentsRepository.findBySupervisorID(supervisor_id);
    String thisYear=String.valueOf(LocalDate.now().getYear());
    List<Leave_RequestModel>pendingLeavesRequest=leaveRequestModelRepository.findLeave_RequestModelByDepartmentAndSupervisorStatus(supervisorID,"pending");
    for (Leave_RequestModel leaveRequestModel:pendingLeavesRequest){
        String startDateLeaveYear=String.valueOf(leaveRequestModel.getStartDate().getYear());
        if(thisYear.equals(startDateLeaveYear)){
            totalLeavesPending=totalLeavesPending+1;
        }
    }

    return totalLeavesPending;

}
public SupervisorStats supervisorStats(String supervisor_id){
        SupervisorStats supervisorStats=new SupervisorStats();
        int totalNumberOfEmployeesInDepartment=totalNumberOfEmployeesInDepartment(supervisor_id);
        int totalApprovedLeaves=totalApprovedLeaves(supervisor_id);
        int totalLeavesPending=totalPendingLeave(supervisor_id);
        supervisorStats.setTotalApprovedLeaves(totalApprovedLeaves);
        supervisorStats.setTotalNumberOfEmployeesInDepartment(totalNumberOfEmployeesInDepartment);
        supervisorStats.setTotalLeavesPending(totalLeavesPending);

        return supervisorStats;
}
public int dueLeaves(){
        int numberOfDueLeave=0;
        List<LeaveHistoryModel>allLeaveHistory=leaveHistoryRepository.findAll();
        for(LeaveHistoryModel leaveHistoryModel:allLeaveHistory){
            if(leaveHistoryModel.getEndDate().isBefore(LocalDate.now())|| leaveHistoryModel.getEndDate().isEqual(LocalDate.now())){
                if(leaveHistoryModel.getStatus().equals("onLeave")){
                    Login login=loginRepository.findByEmployeeId(leaveHistoryModel.getEmployeeId());
                    employeeDetailsRepository.updateDetailsStatus(login,"Over Due");
                    numberOfDueLeave=numberOfDueLeave+1;
                }

            }
        }
        return numberOfDueLeave;
}
public List<DueLeavesDetatilsDto> dueLeavesDetails(){
        List<DueLeavesDetatilsDto>dueLeavesDetatilsDtos=new ArrayList<>();
    List<LeaveHistoryModel>allLeaveHistory=leaveHistoryRepository.findAll();
    for (LeaveHistoryModel leaveHistoryModel:allLeaveHistory){
        long numberOfDaysOverDue=leaveDaysCalculator(leaveHistoryModel.getEndDate(),LocalDate.now().minusDays(1));
        DueLeavesDetatilsDto dueLeavesDetatilsDto=new DueLeavesDetatilsDto();
        if(leaveHistoryModel.getEndDate().isBefore(LocalDate.now()) || leaveHistoryModel.getEndDate().isEqual(LocalDate.now())){
            if(leaveHistoryModel.getStatus().equals("onLeave")){
                Login login=loginRepository.findByEmployeeId(leaveHistoryModel.getEmployeeId());
                EmployeeDetail employeeDetail=employeeDetailsRepository.findEmployeeDetailsByLogin(login);
                Unit unit=unitsRepository.findByUnitId(employeeDetail.getUnitId().getUnitId());
                dueLeavesDetatilsDto.setEmployeeId(login.getEmployeeId());
                dueLeavesDetatilsDto.setName(employeeDetail.getName());
                dueLeavesDetatilsDto.setDateApplied(leaveHistoryModel.getDateApplied());
                dueLeavesDetatilsDto.setLeaveId(leaveHistoryModel.getLeave_requestModel().getId());
                dueLeavesDetatilsDto.setNumberOfDateOverDue(numberOfDaysOverDue);
                dueLeavesDetatilsDto.setCommencementDate(leaveHistoryModel.getStartDate());
                dueLeavesDetatilsDto.setEndDate(leaveHistoryModel.getEndDate());
                dueLeavesDetatilsDto.setUnitName(unit.getUnitName());
                dueLeavesDetatilsDtos.add(dueLeavesDetatilsDto);
            }

        }
    }
    return dueLeavesDetatilsDtos;
}
public void endLeaves(Long leaveId){
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Leave_RequestModel leaveRequestModel=leaveRequestModelRepository.findLeave_RequestModelById(leaveId);
        EmployeeDetail employeeDetail=employeeDetailsRepository.findEmployeeDetailsByLogin(leaveRequestModel.getLogin());
        leaveHistoryRepository.updateLeaveHistoryModelStatus("Ended",leaveRequestModel,LocalDateTime.now().format(dateTimeFormatter));
        employeeDetailsRepository.updateDetailsStatus(employeeDetail.getLogin(),"active");
    System.out.println("Updated end leave");
}
public int employeesOnSite(){
        List<EmployeeDetail>allEmployee=employeeDetailsRepository.findAll();
        int totalNumberOfEmployeeOnSite=0;
        for (EmployeeDetail employeeDetail:allEmployee){
            if(employeeDetail.getStatus().equals("active")){
                totalNumberOfEmployeeOnSite=totalNumberOfEmployeeOnSite+1;
            }
        }
        return totalNumberOfEmployeeOnSite;
}
public int numberOfEmployeesOnLeave(){
        List<LeaveHistoryModel>allApprovedLeaves=leaveHistoryRepository.findAll();
        int totalNumberOfEmployeeNumberOnLeave=0;
        for(LeaveHistoryModel leaveHistoryModel:allApprovedLeaves){
            if(leaveHistoryModel.getStatus().equals("onLeave")){
                totalNumberOfEmployeeNumberOnLeave=totalNumberOfEmployeeNumberOnLeave+1;
            }
        }
        return totalNumberOfEmployeeNumberOnLeave;
}
public int numberOfEmployees(){
    List<EmployeeDetail>allEmployee=employeeDetailsRepository.findAll();
    int totalNumberOfEmployees=0;
    for(EmployeeDetail employeeDetail:allEmployee){
        totalNumberOfEmployees=totalNumberOfEmployees+1;
    }
    return totalNumberOfEmployees;
}
public int numberOfEndedLeaves(){
    List<LeaveHistoryModel>allLeaves=leaveHistoryRepository.findAll();
    int totalNumberOfEndedLeaves=0;
    for(LeaveHistoryModel leaveHistoryModel:allLeaves){
        String startDateYear=String.valueOf(leaveHistoryModel.getStartDate().getYear());
        String currentYear=String.valueOf(LocalDate.now().getYear());
      if(leaveHistoryModel.getStatus().equals("Ended")&&startDateYear.equals(currentYear)){
          totalNumberOfEndedLeaves=totalNumberOfEndedLeaves+1;
      }
    }
    return totalNumberOfEndedLeaves;
}
public HRStats hrStats(){
        HRStats hrStatistic=new HRStats(dueLeaves(),numberOfEndedLeaves(),employeesOnSite(),numberOfEmployeesOnLeave(),numberOfEmployees());return hrStatistic;
}
public List<EndedLeavesDetailsDto> endedLeaveDetails(){
        List<EndedLeavesDetailsDto>endedLeavesDetailsDtos=new ArrayList<>();
    List<LeaveHistoryModel>allLeaves=leaveHistoryRepository.findAll();
    for(LeaveHistoryModel leaveHistoryModel:allLeaves){
        String startDateYear=String.valueOf(leaveHistoryModel.getStartDate().getYear());
        String currentYear=String.valueOf(LocalDate.now().getYear());
        if(leaveHistoryModel.getStatus().equals("Ended")&&startDateYear.equals(currentYear)){
            Login login=loginRepository.findByEmployeeId(leaveHistoryModel.getEmployeeId());
            EmployeeDetail employeeDetail=employeeDetailsRepository.findEmployeeDetailsByLogin(login);
            Unit unit=unitsRepository.findByUnitId(employeeDetail.getUnitId().getUnitId());
            EndedLeavesDetailsDto endedLeavesDetailsDto=new EndedLeavesDetailsDto();
            endedLeavesDetailsDto.setEmployeeId(leaveHistoryModel.getEmployeeId());
            endedLeavesDetailsDto.setName(employeeDetail.getName());
            endedLeavesDetailsDto.setCommencementDate(leaveHistoryModel.getStartDate());
            endedLeavesDetailsDto.setEndDate(leaveHistoryModel.getEndDate());
            endedLeavesDetailsDto.setAppliedDate(leaveHistoryModel.getDateApplied());
            endedLeavesDetailsDto.setEndedDate(leaveHistoryModel.getEndedDate());
            endedLeavesDetailsDto.setUnit(unit.getUnitName());
            endedLeavesDetailsDtos.add(endedLeavesDetailsDto);
        }
    }
    return endedLeavesDetailsDtos;
}
public List<AllEmployeesDetailsDto> allEmployees(){
    List<EmployeeDetail>allEmployee=employeeDetailsRepository.findAll();
    List<AllEmployeesDetailsDto>allEmployeesDetailsDtos=new ArrayList<>();
    for(EmployeeDetail employeeDetail:allEmployee){
        AllEmployeesDetailsDto allEmployeesDetailsDto=new AllEmployeesDetailsDto();
        LeaveHistoryModel leaveHistoryModel=leaveHistoryRepository.findByEmployeeId(employeeDetail.getLogin().getEmployeeId());
        if(leaveHistoryModel!=null){
            String leaveYear=String.valueOf(leaveHistoryModel.getStartDate().getYear());
            String thisYear=String.valueOf(LocalDate.now().getYear());
            if(leaveYear.equals(thisYear)&&leaveHistoryModel.getStatus().equals("onLeave")){
                employeeDetail.setStatus("On Leave");
            }else {
                employeeDetail.setStatus("Active");
            }
        }

        Unit unit=unitsRepository.findByUnitId(employeeDetail.getUnitId().getUnitId());
       allEmployeesDetailsDto.setEmployeeId(employeeDetail.getLogin().getEmployeeId());
        allEmployeesDetailsDto.setName(employeeDetail.getName());
        allEmployeesDetailsDto.setUnitName(unit.getUnitName());
        allEmployeesDetailsDto.setStatus(employeeDetail.getStatus());
        allEmployeesDetailsDtos.add(allEmployeesDetailsDto);
    }
    return allEmployeesDetailsDtos;
}
public List<EmployeesOnLeaveDto> employeesOnLeaveDetails(){
    List<LeaveHistoryModel>allApprovedLeaves=leaveHistoryRepository.findAll();
    List<EmployeesOnLeaveDto>employeesOnLeaveDtos=new ArrayList<>();
    for(LeaveHistoryModel allLeaves:allApprovedLeaves){
        if(allLeaves.getStatus().equals("onLeave")){
            EmployeesOnLeaveDto employeesOnLeaveDto=new EmployeesOnLeaveDto();
            long numberOfDaysConsumed=leaveDaysCalculator(allLeaves.getStartDate(),LocalDate.now());
            Leave_RequestModel leaveRequestModel=leaveRequestModelRepository.findLeave_RequestModelById(allLeaves.getLeave_requestModel().getId());
            EmployeeDetail employeeDetail=employeeDetailsRepository.findEmployeeDetailsByLogin(leaveRequestModel.getLogin());
            Unit unit=unitsRepository.findByUnitId(employeeDetail.getUnitId().getUnitId());
            long numberOfDaysRemaining=leaveRequestModel.getNumberOfDays()-numberOfDaysConsumed;
            employeesOnLeaveDto.setEmployeeId(leaveRequestModel.getLogin().getEmployeeId());
            employeesOnLeaveDto.setName(employeeDetail.getName());
            employeesOnLeaveDto.setUnit(unit.getUnitName());
            employeesOnLeaveDto.setNumberOfDaysRemaining(numberOfDaysRemaining);
            employeesOnLeaveDto.setAppliedDate(leaveRequestModel.getDateApplied());
            employeesOnLeaveDto.setCommencementDate(leaveRequestModel.getStartDate());
            employeesOnLeaveDto.setEndDate(leaveRequestModel.getEndDate());
            employeesOnLeaveDto.setResumptionDate(leaveRequestModel.getEndDate().plusDays(1));
            employeesOnLeaveDtos.add(employeesOnLeaveDto);
        }
    }
    return employeesOnLeaveDtos;
}
}
