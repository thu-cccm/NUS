package com.maple.generator.util;

import com.maple.generator.constant.GenConfig;
import com.maple.generator.constant.GenConstants;
import com.maple.generator.vo.model.GenTableColumnModel;
import com.maple.generator.vo.model.GenTableModel;
import org.apache.velocity.VelocityContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VelocityUtils {

    private static final String PROJECT_PATH = "main/java";

    private static final String MYBATIS_PATH = "main/resources/mapper";

    private static final String DEFAULT_PARENT_MENU_ID = "3";

    public static VelocityContext prepareContext(GenTableModel genTable) {
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tplCategory", genTable.getTplCategory());
        velocityContext.put("tableName", genTable.getTableName());
        velocityContext.put("functionName", StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        velocityContext.put("ClassName", genTable.getClassName());
        velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
        velocityContext.put("moduleName", genTable.getModuleName());
        velocityContext.put("BusinessName", StringUtils.capitalize(genTable.getBusinessName()));
        velocityContext.put("businessName", genTable.getBusinessName());
        velocityContext.put("basePackage", getPackagePrefix(packageName));
        velocityContext.put("packageName", packageName);
        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("datetime", DateUtils.getDate());
        velocityContext.put("pkColumn", genTable.getPkColumn());
        velocityContext.put("PkJavaField", StringUtils.capitalize(genTable.getPkColumn().getJavaField()));
        velocityContext.put("importList", getImportList(genTable));
        velocityContext.put("permissionPrefix", getPermissionPrefix(moduleName, businessName));
        velocityContext.put("columns", genTable.getColumns());
        velocityContext.put("table", genTable);
        Set<String> set = genTable.getColumns().stream().map(GenTableColumnModel::getDictType).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        velocityContext.put("dictTypeKey", StringUtils.join(set, ","));
        velocityContext.put("dictTypeValue", "\"" + StringUtils.join(set, "\",\"") + "\"");
        velocityContext.put("parentMenuId", genTable.getParentMenuId());

        velocityContext.put("baseBeanPackage", GenConfig.getBaseBeanPackage());
        velocityContext.put("transformUtilsPackage", GenConfig.getTransformUtilsPackage());
        if (GenConstants.TPL_TREE.equals(tplCategory)) {
            setTreeVelocityContext(velocityContext, genTable);
        }
        if (GenConstants.TPL_SUB.equals(tplCategory)) {
            setSubVelocityContext(velocityContext, genTable);
        }
        return velocityContext;
    }

    public static void setTreeVelocityContext(VelocityContext context, GenTableModel genTable) {
        context.put("treeCode", StringUtils.toCamelCase(genTable.getTreeCode()));
        context.put("treeParentCode", StringUtils.toCamelCase(genTable.getTreeParentCode()));
        context.put("treeName", StringUtils.toCamelCase(genTable.getTreeName()));
        context.put("expandColumn", getExpandColumn(genTable));
        context.put("tree_parent_code", genTable.getTreeParentCode());
        context.put("tree_name", genTable.getTreeName());
    }

    public static void setSubVelocityContext(VelocityContext context, GenTableModel genTable) {
        GenTableModel subTable = genTable.getSubTable();
        String subTableName = genTable.getSubTableName();
        String subTableFkName = genTable.getSubTableFkName();
        String subClassName = genTable.getSubTable().getClassName();
        String subTableFkClassName = StringUtils.convertToCamelCase(subTableFkName);

        context.put("subTable", subTable);
        context.put("subTableName", subTableName);
        context.put("subTableFkName", subTableFkName);
        context.put("subTableFkClassName", subTableFkClassName);
        context.put("subTableFkclassName", StringUtils.uncapitalize(subTableFkClassName));
        context.put("subClassName", subClassName);
        context.put("subclassName", StringUtils.uncapitalize(subClassName));
        context.put("subImportList", getImportList(genTable.getSubTable()));
    }

    public static List<String> getTemplateList(String tplCategory) {
        List<String> templates = new ArrayList<>();
        if (GenConfig.extendsBaseBean) {
            templates.add("vm/java/bean-base.java.vm");
        } else {
            templates.add("vm/java/bean.java.vm");
        }
        templates.add("vm/java/model.java.vm");
        templates.add("vm/java/mapper.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/xml/mapper.xml.vm");
        if (GenConstants.TPL_CRUD.equals(tplCategory)) {
            templates.add("vm/java/pageQuery.java.vm");
            templates.add("vm/vue/index.vue.vm");
        } else if (GenConstants.TPL_TREE.equals(tplCategory)) {
            templates.add("vm/vue/index-tree.vue.vm");
        } else if (GenConstants.TPL_SUB.equals(tplCategory)) {
            if (GenConfig.extendsBaseBean) {
                templates.add("vm/java/sub-bean-base.java.vm");
            } else {
                templates.add("vm/java/sub-bean.java.vm");
            }
            templates.add("vm/java/pageQuery.java.vm");
            templates.add("vm/java/sub-model.java.vm");
            templates.add("vm/vue/index.vue.vm");
            templates.add("vm/vue/sub-dialog.vue.vm");
        }
        templates.add("vm/vue/dialog.vue.vm");
        templates.add("vm/ts/api.ts.vm");
        templates.add("vm/sql/sql.vm");
        return templates;
    }

    public static String getFileName(String template, GenTableModel genTable) {

        String fileName = "";

        String packageName = genTable.getPackageName();

        String moduleName = genTable.getModuleName();

        String className = genTable.getClassName();

        String businessName = genTable.getBusinessName();

        String javaPath = PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
        String mybatisPath = MYBATIS_PATH + "/" + moduleName;
        String vuePath = "vue";

        if (GenConfig.extendsBaseBean) {
            if (template.contains("bean-base.java.vm")) {
                fileName = StringUtils.format("{}/bean/{}.java", javaPath, className);
            } else if (template.contains("sub-bean-base.java.vm") && StringUtils.equals(GenConstants.TPL_SUB, genTable.getTplCategory())) {
                fileName = StringUtils.format("{}/bean/{}.java", javaPath, genTable.getSubTable().getClassName());
            }
        } else {
            if (template.contains("bean.java.vm")) {
                fileName = StringUtils.format("{}/bean/{}.java", javaPath, className);
            } else if (template.contains("sub-bean.java.vm") && StringUtils.equals(GenConstants.TPL_SUB, genTable.getTplCategory())) {
                fileName = StringUtils.format("{}/bean/{}.java", javaPath, genTable.getSubTable().getClassName());
            }
        }

        if (template.contains("model.java.vm")) {
            fileName = StringUtils.format("{}/vo/model/{}Model.java", javaPath, className);
        } else if (template.contains("sub-model.java.vm") && StringUtils.equals(GenConstants.TPL_SUB, genTable.getTplCategory())) {
            fileName = StringUtils.format("{}/vo/model/{}Model.java", javaPath, genTable.getSubTable().getClassName());
        } else if (template.contains("pageQuery.java.vm")) {
            fileName = StringUtils.format("{}/vo/query/{}PageQuery.java", javaPath, className);
        } else if (template.contains("mapper.java.vm")) {
            fileName = StringUtils.format("{}/mapper/{}Mapper.java", javaPath, className);
        } else if (template.contains("service.java.vm")) {
            fileName = StringUtils.format("{}/service/I{}Service.java", javaPath, className);
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = StringUtils.format("{}/service/impl/{}ServiceImpl.java", javaPath, className);
        } else if (template.contains("controller.java.vm")) {
            fileName = StringUtils.format("{}/controller/{}Controller.java", javaPath, className);
        } else if (template.contains("mapper.xml.vm")) {
            fileName = StringUtils.format("{}/{}Mapper.xml", mybatisPath, className);
        } else if (template.contains("sql.vm")) {
            fileName = businessName + "Menu.sql";
        } else if (template.contains("api.ts.vm")) {
            fileName = StringUtils.format("{}/api/{}/{}.ts", vuePath, moduleName, businessName);
        } else if (template.contains("index.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        } else if (template.contains("dialog.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/dialog.vue", vuePath, moduleName, businessName);
        } else if (template.contains("sub-dialog.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/subDialog.vue", vuePath, moduleName, businessName);
        }
        return fileName;
    }

    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        return StringUtils.substring(packageName, 0, lastIndex);
    }

    public static HashSet<String> getImportList(GenTableModel genTable) {
        List<GenTableColumnModel> columns = genTable.getColumns();
        GenTableModel subGenTable = genTable.getSubTable();
        HashSet<String> importList = new HashSet<>();
        if (StringUtils.isNotNull(subGenTable)) {
            importList.add("java.util.List");
        }
        for (GenTableColumnModel column : columns) {
            if (!column.isSuperColumn() && GenConstants.TYPE_DATE.equals(column.getJavaType())) {
                importList.add("java.util.Date");
            } else if (!column.isSuperColumn() && GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType())) {
                importList.add("java.math.BigDecimal");
            }
        }
        return importList;
    }

    public static String getPermissionPrefix(String moduleName, String businessName) {
        return StringUtils.format("{}:{}", moduleName, businessName);
    }

    public static int getExpandColumn(GenTableModel genTable) {
        int num = 0;
        for (GenTableColumnModel column : genTable.getColumns()) {
            if (Boolean.TRUE.equals(column.getIsList())) {
                num++;
                String columnName = column.getColumnName();
                if (columnName.equals(genTable.getTreeName())) {
                    break;
                }
            }
        }
        return num;
    }
}
