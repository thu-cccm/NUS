package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsAssetService;
import com.maple.vms.vo.model.AssetModel;
import com.maple.vms.vo.query.AssetPageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 集体资产接口.
 */
@Api(tags = "新农村管理-集体资产接口")
@RestController
@RequestMapping("/manage/vms/asset")
@RequiredArgsConstructor
public class AssetController {

    private final IVmsAssetService assetService;

    @ApiOperation(value = "获取资产列表")
    @PostMapping("/getPageList")
    public IPage<AssetModel> getPageList(@RequestBody AssetPageQuery req) {
        return assetService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询资产信息")
    @GetMapping("/{id}")
    public AssetModel getAssetById(@PathVariable("id") Long id) {
        return assetService.getAssetById(id);
    }

    @ApiOperation(value = "新增资产信息")
    @PostMapping("/create")
    public Long createAsset(@RequestBody AssetModel model) {
        return assetService.createAsset(model);
    }

    @ApiOperation(value = "修改资产信息")
    @PostMapping("/update")
    public void updateAsset(@RequestBody AssetModel model) {
        assetService.updateAsset(model);
    }

    @ApiOperation(value = "删除资产信息")
    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable("id") Long id) {
        assetService.deleteAsset(id);
    }

    @ApiOperation(value = "导出资产信息")
    @GetMapping("/export")
    public void exportAsset(@RequestParam(value = "assetName", required = false) String assetName,
                           @RequestParam(value = "assetType", required = false) String assetType,
                           @RequestParam(value = "status", required = false) String status,
                           HttpServletResponse response) {
        AssetModel model = new AssetModel();
        model.setAssetName(assetName);
        model.setAssetType(assetType);
        model.setStatus(status);
        AssetPageQuery query = new AssetPageQuery();
        query.setQuery(model);
        List<AssetModel> list = assetService.getList(query);

        String fileName = "asset-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("资产名称,资产类型,原值(元),购买日期,保管人,状态,存放位置,承租人,合同开始日期,合同结束日期,租金(元/月)\n");
            for (AssetModel item : list) {
                sb.append(safeCsv(item.getAssetName()))
                        .append(',')
                        .append(safeCsv(item.getAssetType()))
                        .append(',')
                        .append(item.getOriginalValue() == null ? "" : item.getOriginalValue())
                        .append(',')
                        .append(item.getPurchaseDate() == null ? "" : formatter.format(item.getPurchaseDate()))
                        .append(',')
                        .append(safeCsv(item.getKeeper()))
                        .append(',')
                        .append(safeCsv(item.getStatus()))
                        .append(',')
                        .append(safeCsv(item.getLocation()))
                        .append(',')
                        .append(safeCsv(item.getLessee()))
                        .append(',')
                        .append(item.getContractStart() == null ? "" : formatter.format(item.getContractStart()))
                        .append(',')
                        .append(item.getContractEnd() == null ? "" : formatter.format(item.getContractEnd()))
                        .append(',')
                        .append(item.getRentalAmount() == null ? "" : item.getRentalAmount())
                        .append('\n');
            }
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    private String safeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}

