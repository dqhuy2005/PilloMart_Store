package demo.restfulapi.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUlti {

    public static void writeResultsToExcel(List<String> results, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lab 6 - Test Results");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setFontHeightInPoints((short) 16);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setWrapText(true);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        Cell headerCell0 = headerRow.createCell(0);
        headerCell0.setCellValue("ID");
        headerCell0.setCellStyle(headerStyle);

        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("Test Case");
        headerCell1.setCellStyle(headerStyle);

        Cell headerCell2 = headerRow.createCell(2);
        headerCell2.setCellValue("Result");
        headerCell2.setCellStyle(headerStyle);

        for (int i = 0; i < results.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue("TC" + (i + 1));
            cell1.setCellStyle(dataStyle);

            Cell cell2 = row.createCell(1);
            cell2.setCellValue(results.get(i));
            cell2.setCellStyle(dataStyle);

            Cell cell3 = row.createCell(2);

            if (results.get(i).contains("Failed")) {
                cell3.setCellValue("FAILED");
                CellStyle failedStyle = workbook.createCellStyle();
                failedStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                failedStyle.setAlignment(HorizontalAlignment.CENTER);
                failedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                failedStyle.setBorderBottom(BorderStyle.THIN);
                failedStyle.setBorderTop(BorderStyle.THIN);
                failedStyle.setBorderLeft(BorderStyle.THIN);
                failedStyle.setBorderRight(BorderStyle.THIN);

                failedStyle.setWrapText(true);
                Font failedFont = workbook.createFont();
                failedFont.setColor(IndexedColors.WHITE.getIndex());
                cell3.setCellStyle(failedStyle);
            } else {
                cell3.setCellValue("PASSED");
                CellStyle passedStyle = workbook.createCellStyle();
                passedStyle.setWrapText(true);
                passedStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                passedStyle.setAlignment(HorizontalAlignment.CENTER);
                passedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                passedStyle.setBorderBottom(BorderStyle.THIN);
                passedStyle.setBorderTop(BorderStyle.THIN);
                passedStyle.setBorderLeft(BorderStyle.THIN);
                passedStyle.setBorderRight(BorderStyle.THIN);

                Font passedFont = workbook.createFont();
                passedFont.setColor(IndexedColors.WHITE.getIndex());
                cell3.setCellStyle(passedStyle);
            }
        }

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }

}
