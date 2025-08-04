package com.ibm.hr_pj.Services;

import com.ibm.hr_pj.Dto.LeaveFormDto;
import com.ibm.hr_pj.Dto.Login_Request;
import com.ibm.hr_pj.Dto.MedSupStatusDto;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PdfGeneratorService {
    public void exportPdf(HttpServletResponse response, List<LeaveFormDto> LeaveFormDto) throws IOException, JRException {
        File file= ResourceUtils.getFile("classpath:LeavePdf.jrxml");
        JasperReport jasperReport= JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(LeaveFormDto);
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("CreatedBy","IBM");
        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=employee.pdf");
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }
}
