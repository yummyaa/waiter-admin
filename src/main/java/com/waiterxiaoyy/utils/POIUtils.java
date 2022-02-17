package com.waiterxiaoyy.utils;

import com.waiterxiaoyy.entity.SysStudent;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/2/17 16:57
 * @Version 1.0
 */
@Component
public class POIUtils {
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    public static List<SysStudent> getClassStudentList(MultipartFile multipartFile) throws Exception {
        Workbook workbook = getWorkBook(multipartFile);

        List<SysStudent> sysStudentList = new ArrayList<>();

        if(workbook != null) {
            try {
                Sheet sheet = workbook.getSheetAt(0);

                int maxRowNum = sheet.getPhysicalNumberOfRows();

                SysStudent sysStudent = null;
                for(int i = 3; i < maxRowNum; i++) {
                    Row row = sheet.getRow(i);
                    String studentId = getCellStringValue(row.getCell(1));
                    String studentName = getCellStringValue(row.getCell(2));

                    if(StringUtils.isBlank(studentId) || StringUtils.isBlank(studentName)) {
                        continue;
                    }
                    sysStudent = new SysStudent();
                    sysStudent.setStudentId(studentId);
                    sysStudent.setStudentName(studentName);
                    sysStudentList.add(sysStudent);
                }
            } catch (Exception e) {
                throw new IOException("文件格式出错");
            }
        }
        return sysStudentList;
    }

    public static void checkFile(MultipartFile file) throws IOException {
        // 判断文件是否存在
        if (null == file) {
            throw new FileNotFoundException("文件不存在！");
        }
        // 获得文件名
        String fileName = file.getOriginalFilename();
        // 判断文件是否是excel文件
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            throw new IOException(fileName + "不是excel文件");
        }
    }

    public static Workbook getWorkBook(MultipartFile file) throws IOException {
        // 获得文件名
        String fileName = file.getOriginalFilename();
        // 创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            // 获取excel文件的io流
            InputStream is = file.getInputStream();
            // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(xls)) {
                // 2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                // 2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            throw new IOException("文件格式出错");
        }
        return workbook;
    }

    public static String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        String cellValue;
        switch (type) {
            case 3:
                cellValue = "";
                break;
            case 5  :
                cellValue = "";
                break;
            case 4:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case 0:
                cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                break;
            case 1:
                cellValue = cell.getStringCellValue();
                break;
            case 2:
                cellValue = cell.getCellFormula();
                break;
            default:
                cellValue = "";
                break;
        }
        return cellValue;
    }

}
