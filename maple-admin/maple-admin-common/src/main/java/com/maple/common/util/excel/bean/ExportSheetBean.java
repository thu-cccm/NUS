package com.maple.common.util.excel.bean;

import lombok.Data;

import java.util.List;

@Data
public class ExportSheetBean {

    private String sheetName;

    private String protectSheet;

    private List<ExportTableBean> list;
}
