package com.maple.common.util.excel;

import com.maple.common.config.MapleMsg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImportExcelTitle {
    @Getter
    @AllArgsConstructor
    public enum PublicDataImportExcel {

        BUSINESS_TYPE("交易类型", "businessType", 0, true),
        TEMP_DEALER_NAME("经销商名称", "tempDealerName", 40, true),
        BRANCH_NAME("区域公司", "branchName", 0, true),
        DEALER_TYPE("一级/二级", "dealerType", 0, true),
        CAR_BRAND("一级品牌", "carBrand", 0, false),
        PROVINCE_NAME("省份", "provinceName", 0, true),
        CITY_NAME("地级", "cityName", 0, true),
        AREA_NAME("区县", "areaName", 0, false),
        ADDRESS("具体地址", "address", 80, false),
        TEL("联系电话", "tel", 40, false),
        COMMENT_TEXT("备注", "commentText", 150, false),
        SOURCE_NAME("商机来源", "sourceName", 0, true),
        DFIM_USER_NAME("商机认领人", "dfimUserName", 0, false),
        ;

        private final String titleCn;

        private final String valueKey;

        private final Integer titleLength;

        private final Boolean isRequired;

        public static Map<String, String> getKeyValue() {
            return Arrays.stream(PublicDataImportExcel.values()).collect(
                    Collectors.toMap(PublicDataImportExcel::getTitleCn, PublicDataImportExcel::getValueKey));
        }

        public static Map<String, PublicDataImportExcel> getExcelTitle() {
            return Arrays.stream(PublicDataImportExcel.values()).collect(
                    Collectors.toMap(PublicDataImportExcel::getValueKey, title -> title));
        }

        public static List<String> checkData(PublicDataImportExcel publicDataImportExcel, String value) {
            return checkDataCommon(value, publicDataImportExcel.getIsRequired(), publicDataImportExcel.getTitleCn(), publicDataImportExcel.getTitleLength());
        }
    }

    @Getter
    @AllArgsConstructor
    public enum TaskImportExcel {

        PUBLIC_DATA_ID("商机ID", "publicDataId", 0, true),
        PUBLIC_DATA_NAME("商机名称", "publicDataName", 40, true),
        TASK_USER_NAME("任务担当人", "taskUserName", 0, false),
        ;

        private final String titleCn;

        private final String valueKey;

        private final Integer titleLength;

        private final Boolean isRequired;

        public static Map<String, String> getKeyValue() {
            return Arrays.stream(TaskImportExcel.values()).collect(
                    Collectors.toMap(TaskImportExcel::getTitleCn, TaskImportExcel::getValueKey));
        }

        public static Map<String, TaskImportExcel> getExcelTitle() {
            return Arrays.stream(TaskImportExcel.values()).collect(
                    Collectors.toMap(TaskImportExcel::getValueKey, title -> title));
        }

        public static List<String> checkData(TaskImportExcel taskImportExcel, String value) {
            return checkDataCommon(value, taskImportExcel.getIsRequired(), taskImportExcel.getTitleCn(), taskImportExcel.getTitleLength());
        }
    }

    private static List<String> checkDataCommon(String value, Boolean isRequired, String titleCn, Integer titleLength) {
        List<String> result = new ArrayList<>();
        if (Boolean.TRUE.equals(isRequired) && StringUtils.isBlank(value)) {
            result.add(String.format(MapleMsg.IMPORT_EXCEL_EMPTY_MSG, titleCn));
        }
        if (titleLength != 0 && value != null && value.length() > titleLength) {
            result.add(String.format(MapleMsg.IMPORT_EXCEL_LENGTH_MSG, titleCn));
        }
        return result;
    }
}
