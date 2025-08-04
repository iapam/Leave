package com.ibm.hr_pj.Services;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class SmsSender {
    public String sendSupervisorSms(String supervisorNumber,String applicantName,String unitName) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://sms.arkesel.com/sms/api?action=send-sms&api_key=UnhvQ0JsSndOWk5Dd2hnek5rekc&to="+supervisorNumber+"&from=AACMH&sms=Dear Sir/Madam, \nYour Subordinate "+applicantName+" in "+unitName+" unit has apply for leave awaiting your status update")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println("the reponse "+response);
        return "";
    }
    public String sendMedsupSms(String supervisorNumber,String applicantName,String unitName) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://sms.arkesel.com/sms/api?action=send-sms&api_key=UnhvQ0JsSndOWk5Dd2hnek5rekc&to="+supervisorNumber+"&from=AACMH&sms=Dear Sir/Madam, \nYour Subordinate "+applicantName+" in "+unitName+" unit has apply for leave  and has been recommended by his/her supervisor awaiting your status update")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println("the reponse "+response);
        return "";
    }
}
