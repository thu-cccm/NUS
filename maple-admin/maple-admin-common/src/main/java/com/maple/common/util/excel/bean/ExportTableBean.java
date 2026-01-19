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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<Integer> getWidth() {
        return width;
    }

    public void setWidth(List<Integer> width) {
        this.width = width;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public List<String> getHorizontalMergerColumnHeaders() {
        return horizontalMergerColumnHeaders;
    }

    public void setHorizontalMergerColumnHeaders(List<String> horizontalMergerColumnHeaders) {
        this.horizontalMergerColumnHeaders = horizontalMergerColumnHeaders;
    }

    public List<String> getVerticalMergerColumnHeaders() {
        return verticalMergerColumnHeaders;
    }

    public void setVerticalMergerColumnHeaders(List<String> verticalMergerColumnHeaders) {
        this.verticalMergerColumnHeaders = verticalMergerColumnHeaders;
    }

    public ExportExcelTheme getTheme() {
        return theme;
    }

    public void setTheme(ExportExcelTheme theme) {
        this.theme = theme;
    }
}
