package com.maple.common.util.excel;

import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ImportExcelUtil {

    private ImportExcelUtil() {
    }

    private static final String EXCEL_2003L = ".xls";
    
    private static final String EXCEL_2007U = ".xlsx";

    private static final String GENERAL = "General";

    private static final String M_D_YY = "m/d/yy";

    public static List<Map<String, String>> parseExcel(InputStream in, String fileName, Map<String, String> titleKey) throws IOException {
        
        Workbook work = getWorkbook(in, fileName);
        Sheet sheet;
        Row row;
        Cell cell;
        
        List<Map<String, String>> ls = new ArrayList<>();
        
        sheet = work.getSheetAt(0);
        if (sheet == null) {
            return ls;
        }
        
        Row topRow = sheet.getRow(0);
        if (null == topRow) {
            return ls;
        }
        row = sheet.getRow(0);
        String[] title;
        if (row == null) {
            return ls;
        }
        title = new String[row.getLastCellNum()];
        for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
            cell = row.getCell(y);
            title[y] = (String) getCellValue(cell);
        }

        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            Map<String, String> m = new HashMap<>(row.getLastCellNum());
            
            for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                cell = row.getCell(y);
                String key = title[y].replace(" ", "");
                if (titleKey.get(key) == null) {
                    log.error("导入excel出现错误的标题：" + key);
                    throw new MapleCheckException(ErrorCode.OTHER_ERROR.getCode(), "excel出现错误的标题：" + key);
                }
                m.put(titleKey.get(key), String.valueOf(getCellValue(cell)).trim());
            }
            ls.add(m);
        }
        work.close();
        return ls;
    }

    public static Workbook getWorkbook(InputStream inStr, String fileName) throws IOException {
        Workbook wb;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (EXCEL_2003L.equals(fileType)) {
            
            wb = new HSSFWorkbook(inStr);
        } else if (EXCEL_2007U.equals(fileType)) {
            
            wb = new XSSFWorkbook(inStr);
        } else {
            throw new MapleCheckException(ErrorCode.OTHER_ERROR.getCode(), "解析的文件格式有误！");
        }
        return wb;
    }

    public static Object getCellValue(Cell cell) {
        Object value = new Object();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
            case FORMULA:
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (GENERAL.equals(cell.getCellStyle().getDataFormatString())) {
                    double cellValue = cell.getNumericCellValue();
                    value = replace(String.valueOf(cellValue));
                } else if (M_D_YY.equals(cell.getCellStyle().getDataFormatString())) {
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    DataFormatter formatter = new DataFormatter();
                    
                    value = formatter.formatCellValue(cell);
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

    public static String replace(String s) {
        char a = '.';
        if (s.indexOf(a) >= 0) {
            
            s = s.replaceAll("0+$", "");
            
            s = s.replaceAll("[.]$", "");
        }
        return s;
    }
}
