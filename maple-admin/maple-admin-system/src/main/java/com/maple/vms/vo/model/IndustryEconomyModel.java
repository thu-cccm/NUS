package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 产业经济维度数据模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "产业经济维度", description = "产业经济维度数据")
public class IndustryEconomyModel {

    @ApiModelProperty(value = "农业总产值(元)")
    private BigDecimal totalAgricultureValue;

    @ApiModelProperty(value = "集体资产总数")
    private Long totalAssets;

    @ApiModelProperty(value = "资产总价值(元)")
    private BigDecimal totalAssetValue;

    @ApiModelProperty(value = "本月财务收入(元)")
    private BigDecimal monthlyIncome;

    @ApiModelProperty(value = "本月财务支出(元)")
    private BigDecimal monthlyExpense;
}

