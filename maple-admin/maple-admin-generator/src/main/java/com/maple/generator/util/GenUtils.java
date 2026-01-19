package com.maple.generator.util;

import com.maple.generator.constant.GenConfig;
import com.maple.generator.constant.GenConstants;
import com.maple.generator.vo.model.GenTableColumnModel;
import com.maple.generator.vo.model.GenTableModel;
import org.apache.commons.lang3.RegExUtils;

import java.util.Arrays;

public class GenUtils {

    public static void initTable(GenTableModel genTable, String operateName) {
        genTable.setClassName(convertClassName(genTable.getTableName()));
        genTable.setPackageName(GenConfig.getPackageName());
        genTable.setModuleName(getModuleName(GenConfig.getPackageName()));
        genTable.setBusinessName(getBusinessName(genTable.getTableName()));
        genTable.setFunctionName(replaceText(genTable.getTableComment()));
        genTable.setFunctionAuthor(GenConfig.getAuthor());
        genTable.setOperateName(operateName);
    }

    public static void initColumnField(GenTableColumnModel column, GenTableModel table) {
        String dataType = getDbType(column.getColumnType());
        String columnName = column.getColumnName();
        column.setTableId(table.getId());
        column.setOperateName(table.getOperateName());

        column.setJavaField(StringUtils.toCamelCase(columnName));

        column.setJavaType(GenConstants.TYPE_STRING);

        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType)) {

            Integer columnLength = getColumnLength(column.getColumnType());
            String htmlType = columnLength >= 500 || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType) ? GenConstants.HTML_TEXTAREA : GenConstants.HTML_INPUT;
            column.setHtmlType(htmlType);
        } else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
            column.setJavaType(GenConstants.TYPE_DATE);
            column.setHtmlType(GenConstants.HTML_DATETIME);
        } else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
            column.setHtmlType(GenConstants.HTML_INPUT);

            String[] str = StringUtils.split(StringUtils.substringBetween(column.getColumnType(), "(", ")"), ",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                column.setJavaType(GenConstants.TYPE_BIGDECIMAL);
            }

            else if (GenConstants.COLUMNTYPE_TINYINT.equals(dataType) && str != null && str.length == 1 && Integer.parseInt(str[0]) == 1) {
                column.setHtmlType(GenConstants.HTML_RADIO);
                column.setJavaType(GenConstants.TYPE_BOOLEAN);
            }

            else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
                column.setJavaType(GenConstants.TYPE_INTEGER);
            }

            else {
                column.setJavaType(GenConstants.TYPE_LONG);
            }
        }

        if (!arraysContains(GenConstants.COLUMNNAME_NOT_ADD, columnName) && Boolean.TRUE.equals(!column.getIsPk())) {
            column.setIsInsert(GenConstants.REQUIRE);
        }

        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && Boolean.TRUE.equals(!column.getIsPk())) {
            column.setIsEdit(GenConstants.REQUIRE);
        }

        if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && Boolean.TRUE.equals(!column.getIsPk())) {
            column.setIsList(GenConstants.REQUIRE);
        }

        if (!arraysContains(GenConstants.COLUMNNAME_NOT_QUERY, columnName) && Boolean.TRUE.equals(!column.getIsPk())) {
            column.setIsQuery(GenConstants.REQUIRE);
        }

        if (StringUtils.endsWithIgnoreCase(columnName, "name")) {
            column.setQueryType(GenConstants.QUERY_LIKE);
        }

        if (StringUtils.endsWithIgnoreCase(columnName, "status")) {
            column.setHtmlType(GenConstants.HTML_RADIO);
        }

        else if (StringUtils.endsWithIgnoreCase(columnName, "type")
                || StringUtils.endsWithIgnoreCase(columnName, "sex")) {
            column.setHtmlType(GenConstants.HTML_SELECT);
        }

        else if (StringUtils.endsWithIgnoreCase(columnName, "image")) {
            column.setHtmlType(GenConstants.HTML_UPLOAD);
        }

        else if (StringUtils.endsWithIgnoreCase(columnName, "file")) {
            column.setHtmlType(GenConstants.HTML_UPLOAD);
        }

        else if (StringUtils.endsWithIgnoreCase(columnName, "content")) {
            column.setHtmlType(GenConstants.HTML_SUMMERNOTE);
        }
    }

    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        return StringUtils.substring(packageName, lastIndex + 1, nameLength);
    }

    public static String getBusinessName(String tableName) {
        int lastIndex = tableName.indexOf("_");
        int nameLength = tableName.length();
        return StringUtils.toCamelCase(StringUtils.substring(tableName, lastIndex + 1, nameLength));
    }

    public static String convertClassName(String tableName) {
        boolean autoRemovePre = GenConfig.getAutoRemovePre();
        String tablePrefix = GenConfig.getTablePrefix();
        if (autoRemovePre && StringUtils.isNotEmpty(tablePrefix)) {
            String[] searchList = StringUtils.split(tablePrefix, ",");
            tableName = replaceFirst(tableName, searchList);
        }
        return StringUtils.convertToCamelCase(tableName);
    }

    public static String replaceFirst(String replacementm, String[] searchList) {
        String text = replacementm;
        for (String searchString : searchList) {
            if (replacementm.startsWith(searchString)) {
                text = replacementm.replaceFirst(searchString, "");
                break;
            }
        }
        return text;
    }

    public static String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:表|tian)", "");
    }

    public static String getDbType(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            return StringUtils.substringBefore(columnType, "(");
        } else {
            return columnType;
        }
    }

    public static Integer getColumnLength(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        } else {
            return 0;
        }
    }
}
