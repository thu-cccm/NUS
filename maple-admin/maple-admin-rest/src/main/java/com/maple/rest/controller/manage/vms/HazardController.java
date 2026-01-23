package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsHazardService;
import com.maple.vms.vo.model.HazardModel;
import com.maple.vms.vo.query.HazardPageQuery;
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
 * 安全隐患排查接口.
 */
@Api(tags = "新农村管理-安全隐患排查接口")
@RestController
@RequestMapping("/manage/vms/hazard")
@RequiredArgsConstructor
public class HazardController {

    private final IVmsHazardService hazardService;

    @ApiOperation(value = "获取安全隐患列表")
    @PostMapping("/getPageList")
    public IPage<HazardModel> getPageList(@RequestBody HazardPageQuery req) {
        return hazardService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询安全隐患信息")
    @GetMapping("/{id}")
    public HazardModel getHazardById(@PathVariable("id") Long id) {
        return hazardService.getHazardById(id);
    }

    @ApiOperation(value = "新增安全隐患信息")
    @PostMapping("/create")
    public Long createHazard(@RequestBody HazardModel model) {
        return hazardService.createHazard(model);
    }

    @ApiOperation(value = "修改安全隐患信息")
    @PostMapping("/update")
    public void updateHazard(@RequestBody HazardModel model) {
        hazardService.updateHazard(model);
    }

    @ApiOperation(value = "删除安全隐患信息")
    @DeleteMapping("/{id}")
    public void deleteHazard(@PathVariable("id") Long id) {
        hazardService.deleteHazard(id);
    }

    @ApiOperation(value = "导出安全隐患信息")
    @GetMapping("/export")
    public void exportHazard(@RequestParam(value = "hazardType", required = false) String hazardType,
                            @RequestParam(value = "location", required = false) String location,
                            @RequestParam(value = "status", required = false) Integer status,
                            HttpServletResponse response) {
        HazardModel model = new HazardModel();
        model.setHazardType(hazardType);
        model.setLocation(location);
        model.setStatus(status);
        HazardPageQuery query = new HazardPageQuery();
        query.setQuery(model);
        List<HazardModel> list = hazardService.getList(query);

        String fileName = "hazard-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("隐患类型,隐患位置,隐患描述,上报人,状态,整改时间,整改结果\n");
            for (HazardModel item : list) {
                sb.append(safeCsv(item.getHazardType()))
                        .append(',')
                        .append(safeCsv(item.getLocation()))
                        .append(',')
                        .append(safeCsv(item.getDescription()))
                        .append(',')
                        .append(safeCsv(item.getReporter()))
                        .append(',')
                        .append(getStatusText(item.getStatus()))
                        .append(',')
                        .append(item.getRectifyTime() == null ? "" : formatter.format(item.getRectifyTime()))
                        .append(',')
                        .append(safeCsv(item.getRectifyResult()))
                        .append('\n');
            }
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    private String getStatusText(Integer status) {
        if (status == null) return "";
        if (status == 0) return "待整改";
        if (status == 1) return "整改中";
        if (status == 2) return "已整改";
        return "";
    }

    private String safeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}

