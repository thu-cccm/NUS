package com.maple.generator.constant;

public class GenConstants {

    public static final String TPL_CRUD = "crud";

    public static final String TPL_TREE = "tree";

    public static final String TPL_SUB = "sub";

    public static final String TREE_CODE = "treeCode";

    public static final String TREE_PARENT_CODE = "treeParentCode";

    public static final String TREE_NAME = "treeName";

    public static final String PARENT_MENU_ID = "parentMenuId";

    public static final String PARENT_MENU_NAME = "parentMenuName";

    public static final String[] COLUMNTYPE_STR = {"char", "varchar", "nvarchar", "varchar2"};

    public static final String[] COLUMNTYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext"};

    public static final String[] COLUMNTYPE_TIME = {"datetime", "time", "date", "timestamp"};

    public static final String COLUMNTYPE_TINYINT = "tinyint";

    public static final String[] COLUMNTYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal"};

    public static final String[] COLUMNNAME_NOT_ADD = {"id", "create_id", "create_time", "update_id",
            "update_time", "is_delete", "ancestors", "parent_id"};

    public static final String[] COLUMNNAME_NOT_EDIT = {"id", "create_id", "create_time", "update_id",
            "update_time", "is_delete", "ancestors", "parent_id"};

    public static final String[] COLUMNNAME_NOT_LIST = {"id", "create_id", "create_time", "update_id",
            "update_time", "is_delete", "ancestors", "parent_id"};

    public static final String[] COLUMNNAME_NOT_QUERY = {"id", "create_id", "create_time", "update_id", "sort_num", "remark",
            "update_time", "is_delete", "ancestors", "parent_id"};

    public static final String[] BASE_ENTITY = {"id", "createId", "createTime", "updateId", "updateTime"};

    public static final String[] TREE_ENTITY = {"id", "parentName", "parentId", "sortNum", "ancestors"};

    public static final String HTML_INPUT = "input";

    public static final String HTML_TEXTAREA = "textarea";

    public static final String HTML_SELECT = "select";

    public static final String HTML_RADIO = "radio";

    public static final String HTML_CHECKBOX = "checkbox";

    public static final String HTML_DATETIME = "datetime";

    public static final String HTML_UPLOAD = "upload";

    public static final String HTML_SUMMERNOTE = "summernote";

    public static final String TYPE_STRING = "String";

    public static final String TYPE_INTEGER = "Integer";

    public static final String TYPE_LONG = "Long";

    public static final String TYPE_DOUBLE = "Double";

    public static final String TYPE_BOOLEAN = "Boolean";

    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    public static final String TYPE_DATE = "Date";

    public static final String QUERY_LIKE = "LIKE";

    public static final String QUERY_EQ = "EQ";

    public static final Boolean REQUIRE = true;

}
