package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsAgricultureService;
import com.maple.vms.vo.model.AgricultureModel;
import com.maple.vms.vo.query.AgriculturePageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 农业生产接口.
 */
@Api(tags = "新农村管理-农业生产接口")
@RestController
@RequestMapping("/manage/vms/agriculture")
@RequiredArgsConstructor
public class AgricultureController {

    private final IVmsAgricultureService agricultureService;

    @ApiOperation(value = "获取农业生产列表")
    @PostMapping("/getPageList")
    public IPage<AgricultureModel> getPageList(@RequestBody AgriculturePageQuery req) {
        return agricultureService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询农业生产信息")
    @GetMapping("/{id}")
    public AgricultureModel getAgricultureById(@PathVariable("id") Long id) {
        return agricultureService.getAgricultureById(id);
    }

    @ApiOperation(value = "新增农业生产信息")
    @PostMapping("/create")
    public Long createAgriculture(@RequestBody AgricultureModel model) {
        return agricultureService.createAgriculture(model);
    }

    @ApiOperation(value = "修改农业生产信息")
    @PostMapping("/update")
    public void updateAgriculture(@RequestBody AgricultureModel model) {
        agricultureService.updateAgriculture(model);
    }

    @ApiOperation(value = "删除农业生产信息")
    @DeleteMapping("/{id}")
    public void deleteAgriculture(@PathVariable("id") Long id) {
        agricultureService.deleteAgriculture(id);
    }

    @ApiOperation(value = "导出农业生产信息")
    @GetMapping("/export")
    public void exportAgriculture(@RequestParam(value = "season", required = false) String season,
                                  @RequestParam(value = "cropName", required = false) String cropName,
                                  HttpServletResponse response) {
        AgricultureModel model = new AgricultureModel();
        model.setSeason(season);
        model.setCropName(cropName);
        AgriculturePageQuery query = new AgriculturePageQuery();
        query.setQuery(model);
        List<AgricultureModel> list = agricultureService.getList(query);

        String fileName = "agriculture-export.csv";
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("种植季度,作物名称,种植面积(亩),预计产量(公斤),预估单价(元/公斤),负责人ID\n");
            for (AgricultureModel item : list) {
                sb.append(safeCsv(item.getSeason()))
                        .append(',')
                        .append(safeCsv(item.getCropName()))
                        .append(',')
                        .append(item.getPlantArea() == null ? "" : item.getPlantArea())
                        .append(',')
                        .append(item.getExpectYield() == null ? "" : item.getExpectYield())
                        .append(',')
                        .append(item.getMarketPrice() == null ? "" : item.getMarketPrice())
                        .append(',')
                        .append(item.getResidentId() == null ? "" : item.getResidentId())
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

