package com.ibm.hr_pj.Dto;

import com.ibm.hr_pj.Models.EmployeeRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Login_Request {
    private String employeeId;
    private String password;
    private EmployeeRole employeeRole;
}
