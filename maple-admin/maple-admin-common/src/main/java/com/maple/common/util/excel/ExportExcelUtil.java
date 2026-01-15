package com.maple.common.util.excel;

import com.maple.common.util.excel.bean.ExportTableBean;
import com.maple.common.util.excel.bean.ExportExcelTheme;
import com.maple.common.util.excel.bean.ExportSheetBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportExcelUtil {

    private ExportExcelUtil() {
    }

    public static void exportExcel(ExportTableBean table, OutputStream out) {
        List<ExportTableBean> exportExcelBeans = new ArrayList<>();
        exportExcelBeans.add(table);
        ExportSheetBean sheetBean = new ExportSheetBean();
        sheetBean.setSheetName("sheet1");
        sheetBean.setList(exportExcelBeans);
        exportExcelOneSheetMoreTable(sheetBean, out);
    }

    public static void exportExcel(ExportTableBean table, String sheetName, String passWord, OutputStream out) {
        List<ExportTableBean> exportExcelBeans = new ArrayList<>();
        exportExcelBeans.add(table);
        ExportSheetBean sheetBean = new ExportSheetBean();
        sheetBean.setSheetName(sheetName);
        sheetBean.setProtectSheet(passWord);
        sheetBean.setList(exportExcelBeans);
        exportExcelOneSheetMoreTable(sheetBean, out);
    }

    public static void exportExcelOneSheetMoreTable(ExportSheetBean sheet, OutputStream out) {
        
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            creatMoreTableSheet(sheet, workbook, 1);
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportExcelMoreSheetMoreTable(List<ExportSheetBean> list, OutputStream out) {
        
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            for (int i = 0; i < list.size(); i++) {
                creatMoreTableSheet(list.get(i), workbook, i);
            }
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void creatMoreTableSheet(ExportSheetBean sheetBean, XSSFWorkbook workbook, int i) {
        List<ExportTableBean> list = sheetBean.getList();
        
        XSSFSheet sheet = workbook.createSheet(sheetBean.getSheetName() == null ? "sheet" + i : sheetBean.getSheetName());
        
        sheet.setDefaultColumnWidth(16);

        int listCount = 0;
        for (ExportTableBean table : list) {
            
            ExportExcelTheme excelTheme = table.getTheme();
            if (excelTheme == null) {
                excelTheme = ExportExcelTheme.DEFAULT;
            }
            XSSFCellStyle titleStyle = createTitleStyle(workbook,
                    excelTheme.getTitleBackgroundColor(),
                    excelTheme.getTitleFontColor(),
                    excelTheme.getBorderColor());
            XSSFCellStyle titleStyleOne = createLineStyle(workbook,
                    excelTheme.getContentOneColor(),
                    excelTheme.getContentFrontColor(),
                    excelTheme.getBorderColor());
            XSSFCellStyle titleStyleTwo = createLineStyle(workbook,
                    excelTheme.getContentTwoColor(),
                    excelTheme.getContentFrontColor(),
                    excelTheme.getBorderColor());

            createTitle(table, sheet, titleStyle, listCount);

            createExcelData(table, sheet, titleStyleOne, titleStyleTwo, listCount);

            listCount = listCount + table.getDataList().size() + 2;
        }

        if (StringUtils.isNotBlank(sheetBean.getProtectSheet())) {
            sheet.protectSheet(sheetBean.getProtectSheet());
        }
    }

    private static void createTitle(ExportTableBean table, XSSFSheet sheet, XSSFCellStyle titleStyle, int listCount) {
        
        Integer horizontalNum = null;
        Object horizontalValue = null;
        XSSFRow row = sheet.createRow(listCount);
        for (int i = 0; i < table.getHeaders().size(); i++) {
            sheet.setColumnWidth(i, 256 * table.getWidth().get(i) + 184);
            XSSFCell cell = row.createCell(i);
            
            cell.setCellStyle(titleStyle);

            XSSFRichTextString text = new XSSFRichTextString(table.getHeaders().get(i));
            cell.setCellValue(text);

            if (table.getHorizontalMergerColumnHeaders() != null
                    && table.getHorizontalMergerColumnHeaders().contains(table.getHeaders().get(i))) {
                if (horizontalNum == null) {
                    horizontalNum = i;
                    horizontalValue = table.getHeaders().get(i);
                } else {
                    
                    if (!horizontalValue.equals(table.getHeaders().get(i))) {
                        if (i - horizontalNum > 1) {
                            CellRangeAddress cra = new CellRangeAddress(listCount, listCount, horizontalNum, i - 1);
                            sheet.addMergedRegion(cra);
                        }
                        horizontalNum = i;
                        horizontalValue = table.getHeaders().get(i);
                        
                    } else if (i == table.getHeaders().size() - 1 && i - horizontalNum >= 1) {
                        CellRangeAddress cra = new CellRangeAddress(listCount, listCount, horizontalNum, i);
                        sheet.addMergedRegion(cra);
                    }
                }
            } else {
                if (horizontalNum != null && i - horizontalNum > 1) {
                    CellRangeAddress cra = new CellRangeAddress(listCount, listCount, horizontalNum, i - 1);
                    sheet.addMergedRegion(cra);
                } else {
                    horizontalNum = null;
                    horizontalValue = null;
                }
            }
        }
    }

    private static void createExcelData(ExportTableBean table, XSSFSheet sheet, XSSFCellStyle titleStyleOne, XSSFCellStyle titleStyleTwo, int listCount) {
        
        Integer[] verticalNum = new Integer[table.getKeys().size()];
        Object[] verticalValue = new Object[table.getKeys().size()];
        
        for (int i = 0; i < table.getDataList().size(); i++) {
            int line = i + listCount + 1;
            XSSFRow row = sheet.createRow(line);
            
            if ("序号".equals(table.getHeaders().get(0))) {
                XSSFCell cell = row.createCell(0);
                
                if (i % 2 == 0) {
                    cell.setCellStyle(titleStyleOne);
                } else {
                    cell.setCellStyle(titleStyleTwo);
                }
                cell.setCellValue(i + 1 + "");
            }

            Map<String, Object> obj = table.getDataList().get(i);
            for (int j = 0; j < table.getKeys().size(); j++) {
                if ("order".equals(table.getKeys().get(j))) {
                    continue;
                }
                XSSFCell cell = row.createCell(j);
                
                if (i % 2 == 0) {
                    cell.setCellStyle(titleStyleOne);
                } else {
                    cell.setCellStyle(titleStyleTwo);
                }
                if (obj.get(table.getKeys().get(j)) == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(obj.get(table.getKeys().get(j)) + "");
                }

                if (table.getVerticalMergerColumnHeaders() != null
                        && table.getVerticalMergerColumnHeaders().contains(table.getHeaders().get(j))) {
                    if (verticalNum[j] == null) {
                        verticalNum[j] = line;
                        verticalValue[j] = obj.get(table.getKeys().get(j));
                    } else {
                        
                        if (verticalValue[j] != null && !verticalValue[j].equals(obj.get(table.getKeys().get(j)))) {
                            if (line - verticalNum[j] > 1) {
                                CellRangeAddress cra = new CellRangeAddress(verticalNum[j], line - 1, j, j);
                                sheet.addMergedRegion(cra);
                            }
                            verticalNum[j] = line;
                            verticalValue[j] = obj.get(table.getKeys().get(j));
                            
                        } else if (i == table.getDataList().size() - 1 && line - verticalNum[j] >= 1) {
                            CellRangeAddress cra = new CellRangeAddress(verticalNum[j], line, j, j);
                            sheet.addMergedRegion(cra);
                        }
                    }
                } else {
                    if (verticalNum[j] != null && line - verticalNum[j] > 1) {
                        CellRangeAddress cra = new CellRangeAddress(verticalNum[j], line - 1, j, j);
                        sheet.addMergedRegion(cra);
                    } else {
                        verticalNum[j] = null;
                        verticalValue[j] = null;
                    }
                }
            }
        }
    }

    private static XSSFCellStyle createTitleStyle(XSSFWorkbook workbook, XSSFColor bgColor, XSSFColor fontColor, XSSFColor borderColor) {
        
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(bgColor);
        createBorder(style, borderColor);
        
        short fontSize = 13;
        XSSFFont font = createFont(workbook, fontColor, fontSize, true);
        
        style.setFont(font);
        return style;
    }

    private static XSSFCellStyle createLineStyle(XSSFWorkbook workbook, XSSFColor bgColor, XSSFColor fontColor, XSSFColor borderColor) {
        
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(bgColor);
        
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        createBorder(style, borderColor);

        createBorder(style, borderColor);
        
        short fontSize = 12;
        XSSFFont font = createFont(workbook, fontColor, fontSize, false);
        
        style.setFont(font);
        return style;
    }

    private static void createBorder(XSSFCellStyle style, XSSFColor borderColor) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(borderColor);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(borderColor);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(borderColor);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(borderColor);
    }

    private static XSSFFont createFont(XSSFWorkbook workbook, XSSFColor color, short fontSize, boolean isBold) {
        XSSFFont font = workbook.createFont();
        font.setColor(color);
        font.setFontHeightInPoints(fontSize);
        font.setBold(isBold);
        return font;
    }

    public static void updateNameUnicode(HttpServletRequest request, HttpServletResponse response, String exportFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        String firefox = "firefox";
        
        if (agent.contains(firefox)) {
            exportFileName = new String(exportFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        } else {
            exportFileName = java.net.URLEncoder.encode(exportFileName, "UTF-8");
        }
        
        response.setHeader("Content-Disposition", "attachment;filename=" + exportFileName + ".xlsx");
    }

    public static void testExcel(OutputStream out) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("测试");

            String[] name = {"NO_FILL",
                    "SOLID_FOREGROUND",
                    "FINE_DOTS",
                    "ALT_BARS",
                    "SPARSE_DOTS",
                    "THICK_HORZ_BANDS",
                    "THICK_VERT_BANDS",
                    "THICK_BACKWARD_DIAG",
                    "THICK_FORWARD_DIAG",
                    "BIG_SPOTS",
                    "BRICKS",
                    "THIN_HORZ_BANDS",
                    "THIN_VERT_BANDS",
                    "THIN_BACKWARD_DIAG",
                    "THIN_FORWARD_DIAG",
                    "SQUARES",
                    "DIAMONDS",
                    "LESS_DOTS",
                    "LEAST_DOTS"};

            String[] name2 = {
                    "GENERAL",
                    "LEFT",
                    "CENTER",
                    "RIGHT",
                    "FILL",
                    "JUSTIFY",
                    "CENTER_SELECTION",
                    "DISTRIBUTED"
            };

            String[] name3 = {
                    "TOP",
                    "CENTER",
                    "BOTTOM",
                    "JUSTIFY",
                    "DISTRIBUTED"
            };

            HorizontalAlignment[] aaa = HorizontalAlignment.values();
            VerticalAlignment[] bbb = VerticalAlignment.values();
            for (int i = 0; i < 19; i++) {
                sheet.setColumnWidth(i, 256 * 20 + 184);
                XSSFRow row = sheet.createRow(i);
                row.setHeight((short) (30 * 20));
                
                XSSFCellStyle style = workbook.createCellStyle();
                style.setFillPattern(FillPatternType.forInt(i));
                XSSFCell cell = row.createCell(0);
                cell.setCellStyle(style);

                style.setFillForegroundColor(new XSSFColor(new Color(237, 125, 49), new DefaultIndexedColorMap()));
                XSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(name[i]);
                if (i < 8) {
                    XSSFCellStyle style2 = workbook.createCellStyle();
                    style2.setAlignment(aaa[i]);
                    XSSFCell cell2 = row.createCell(3);
                    cell2.setCellStyle(style2);
                    cell2.setCellValue("水平对齐方式");

                    XSSFCell cell3 = row.createCell(4);
                    cell3.setCellValue(name2[i]);
                }

                if (i < 5) {
                    XSSFCellStyle style3 = workbook.createCellStyle();
                    style3.setVerticalAlignment(bbb[i]);
                    XSSFCell cell2 = row.createCell(6);
                    cell2.setCellStyle(style3);
                    cell2.setCellValue("垂直对齐方式");

                    XSSFCell cell3 = row.createCell(7);
                    cell3.setCellValue(name3[i]);
                }
            }
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
