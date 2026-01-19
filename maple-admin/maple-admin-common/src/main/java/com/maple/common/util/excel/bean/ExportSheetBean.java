package com.maple.common.util.excel.bean;

import lombok.Data;

import java.util.List;

@Data
public class ExportSheetBean {

    private String sheetName;

    private String protectSheet;

    private List<ExportTableBean> list;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getProtectSheet() {
        return protectSheet;
    }

    public void setProtectSheet(String protectSheet) {
        this.protectSheet = protectSheet;
    }

    public List<ExportTableBean> getList() {
        return list;
    }

    public void setList(List<ExportTableBean> list) {
        this.list = list;
    }
}
