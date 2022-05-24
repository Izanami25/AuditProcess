package com.AUI.auditprocess.Controllers;

import com.AUI.auditprocess.Entities.AuditEntity;
import com.AUI.auditprocess.Models.Audit;
import com.AUI.auditprocess.Repositories.AuditRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {
    @Autowired
    AuditRepository auditRepository;
    @GetMapping("/get-audit")
    public List<Audit> GetAudit(){
        List<Audit> result = new ArrayList<>();
        List<AuditEntity> entities = auditRepository.findAll();
        if (entities != null && entities.size() > 0){
            entities.forEach(x->{
                Audit item = new Audit();
                item.id = x.id;
                item.shortName = x.short_name;
                item.fullName = x.full_name;
                item.paragraph = x.paragraph;
                item.requirement = x.requirement;
                item.description = x.description;
                item.uploadDate = x.uploadDate;
                result.add(item);
            });
        }
        return result;
    }

    @PostMapping("/upload-audit-excel")
    public List<Audit> importExcelFile(@RequestParam("file") MultipartFile files)throws IOException {
        List<Audit> audits = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
        // Read student data form excel file sheet1.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        XSSFSheet worksheet = workbook.getSheetAt(0);
        for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
            if (index > 0) {
                XSSFRow row = worksheet.getRow(index);
                Audit audit = new Audit();
                audit.shortName = getCellValue(row, 0);
                audit.fullName = getCellValue(row, 1);
                audit.paragraph = getCellValue(row, 2);
                audit.requirement = getCellValue(row, 3);
                audit.description = getCellValue(row, 4);
                audit.uploadDate = dtf.format(now);

                audits.add(audit);
            }
        }
        // Save to db.
        List<AuditEntity> entities = new ArrayList<>();
        if (audits.size() > 0) {
            audits.forEach(x->{
                AuditEntity entity = new AuditEntity();
                entity.short_name = x.shortName;
                entity.full_name = x.fullName;
                entity.paragraph = x.paragraph;
                entity.requirement = x.requirement;
                entity.description = x.description;
                entity.uploadDate = x.uploadDate;
                entities.add(entity);
            });
            auditRepository.saveAll(entities);
        }
        return audits;
    }
    private int convertStringToInt(String str) {
        int result = 0;
        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            return result;
        }
        result = Integer.parseInt(str);
        return result;
    }
    private String getCellValue(Row row, int cellNo) {
        DataFormatter formatter = new DataFormatter();
        Cell cell = row.getCell(cellNo);
        return formatter.formatCellValue(cell);
    }
}