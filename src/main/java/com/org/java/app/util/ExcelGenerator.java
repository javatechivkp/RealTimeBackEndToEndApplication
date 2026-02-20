package com.org.java.app.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.java.app.entity.Employee;

public final class ExcelGenerator {

	private ExcelGenerator() {}

	public static byte[] generateEmployeesExcel(List<Employee> employees) {
		try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			Sheet sheet = wb.createSheet("Employees");

			String[] headers = new String[] {"EmpId","Name","Age","Salary","Email","WorkLocation","Platform","projectName","addharNumber","panNumber","mobbileNumber"};

			CellStyle headerStyle = wb.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerStyle);
			}

			int rowIdx = 1;
			for (Employee e : employees) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(e.getEmpid());
				row.createCell(1).setCellValue((e.getName()));
				row.createCell(2).setCellValue((e.getAge()));
				row.createCell(3).setCellValue(e.getSalary());
				row.createCell(4).setCellValue(e.getEmail());
				row.createCell(5).setCellValue((e.getWorkLocation()));
				row.createCell(6).setCellValue((e.getPlatform()));
				row.createCell(7).setCellValue(e.getProjectName());
				row.createCell(8).setCellValue(e.getAddharNumber());
				row.createCell(9).setCellValue(e.getPanNumber());
				row.createCell(10).setCellValue(e.getMobbileNumber());
				
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			wb.write(baos);
			return baos.toByteArray();
		} catch (IOException ex) {
			throw new IllegalStateException("Failed to generate Excel", ex);
		}
	}

	private static String ns(String s) { return s == null ? "" : s; }
}



