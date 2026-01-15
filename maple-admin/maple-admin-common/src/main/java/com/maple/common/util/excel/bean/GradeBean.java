package com.maple.common.util.excel.bean;

import lombok.Data;

@Data
public class GradeBean {

    private String className;

    private String userName;
    
    private Integer mathGrade;
    
    private Integer chinaGrade;
    
    private Integer englishGrade;
}
