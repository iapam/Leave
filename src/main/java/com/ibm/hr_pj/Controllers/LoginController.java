package com.ibm.hr_pj.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping("/")
    public String index(){

        return "no print";
    }
    public String registerEmployee(){
        return "employee registered";
    }
}
