package com.maple.common.util.excel.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExportTableBean {
    
    private String title;

    private List<String> headers;

    private List<String> keys;

    private List<Integer> width;

    private List<Map<String, Object>> dataList;

    private List<String> horizontalMergerColumnHeaders;

    private List<String> verticalMergerColumnHeaders;

    private ExportExcelTheme theme;
}
