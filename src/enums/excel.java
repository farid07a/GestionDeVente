/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enums;

import entity.VersementEntreprise;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author pc
 */
public class excel {

    
    
    public excel() {
    }
    
    public void exportVersementEntreprise(List<VersementEntreprise> list, File destination)
        throws Exception {
        File template = new File("resources/template/RapportVersementEntreprise.xlsx");

if (!template.exists()) {
    throw new FileNotFoundException(template.getAbsolutePath());
}


        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(template))) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            
            int rowIndex = 4; // لأن Header في الصف الثالث
            
            // Style للحدود
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            
            DataFormat format = workbook.createDataFormat();
            
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.cloneStyleFrom(style); // ينسخ الحدود والمحاذاة
            numberStyle.setDataFormat(format.getFormat("#,##0.00"));
            
            
            Cell cell;
            
            cell = sheet.getRow(1).getCell(4);
            cell.setCellValue(LocalDate.now().toString());
            cell.setCellStyle(style);
            
            for (VersementEntreprise v : list) {
                
                Row row = sheet.getRow(rowIndex);
                row.setHeightInPoints(30);
                if (row == null) {
                    row = sheet.createRow(rowIndex);
                }
                
                
                
                cell = row.createCell(0);
                cell.setCellValue(v.getId());
                cell.setCellStyle(style);
                
                cell = row.createCell(1);
                cell.setCellValue(v.getEntreprise().getNom_ar());
                cell.setCellStyle(style);
                
                cell = row.createCell(2);
                cell.setCellValue(v.getMontant());
                cell.setCellStyle(style);
                
                cell = row.createCell(3);
                cell.setCellValue(v.getReste_credit());
                cell.setCellStyle(style);
                
                cell = row.createCell(4);
                cell.setCellValue(v.getDate_versement().toString());
                cell.setCellStyle(style);
                
                rowIndex++;
            }
            Cell cellSomme;
            Row row = sheet.getRow(rowIndex  );
            row.setHeightInPoints(30);
            cellSomme = row.createCell(1);
            cellSomme.setCellValue(" اجــمـالي الـبـالـغ الـمـدفـوعـة :");
            cellSomme.setCellStyle(style);
            
            int indexLastCell = rowIndex-1;
            cellSomme = row.createCell(2);
            cellSomme.setCellFormula("SUM( C" +3 + ":C" + indexLastCell + " )");
            cellSomme.setCellStyle(numberStyle);
            
            rowIndex = rowIndex+1;
            
            row = sheet.getRow(rowIndex );
            cellSomme = row.createCell(1);
            cellSomme.setCellValue("الـديــون الـغـير مــدفوعـة :");
            cellSomme.setCellStyle(style);
            
            cellSomme = row.createCell(2);
            cellSomme.setCellValue(0.00);
            cellSomme.setCellStyle(numberStyle );
            
            // Auto Size
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream fos = new FileOutputStream(destination);
            
            workbook.getCreationHelper()  // pour l fct calcul
                    .createFormulaEvaluator()
                    .evaluateAll();
            workbook.write(fos);
            fos.close();
        }
        
        
        if (Desktop.isDesktopSupported()) {
    Desktop desktop = Desktop.getDesktop();
    if (destination.exists()) {
        // فتح الملف تلقائياً
        desktop.open(destination); 
        
        // إذا أردت استدعاء نافذة الطباعة مباشرة بدون فتح Excel للمعاينة:
         //desktop.print(destination);
         Thread.sleep(2500); 

        // 3. محاكاة الضغط على Ctrl + P لفتح نافذة المعاينة والطباعة تلقائياً
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_P);
        robot.keyRelease(KeyEvent.VK_P);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
        
        }       
}
}
