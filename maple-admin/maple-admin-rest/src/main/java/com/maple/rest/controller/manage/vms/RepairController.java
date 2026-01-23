package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsRepairService;
import com.maple.vms.vo.model.RepairModel;
import com.maple.vms.vo.query.RepairPageQuery;
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
 * 设施报修接口.
 */
@Api(tags = "新农村管理-设施报修接口")
@RestController
@RequestMapping("/manage/vms/repair")
@RequiredArgsConstructor
public class RepairController {

    private final IVmsRepairService repairService;

    @ApiOperation(value = "获取设施报修列表")
    @PostMapping("/getPageList")
    public IPage<RepairModel> getPageList(@RequestBody RepairPageQuery req) {
        return repairService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询设施报修信息")
    @GetMapping("/{id}")
    public RepairModel getRepairById(@PathVariable("id") Long id) {
        return repairService.getRepairById(id);
    }

    @ApiOperation(value = "新增设施报修信息")
    @PostMapping("/create")
    public Long createRepair(@RequestBody RepairModel model) {
        return repairService.createRepair(model);
    }

    @ApiOperation(value = "修改设施报修信息")
    @PostMapping("/update")
    public void updateRepair(@RequestBody RepairModel model) {
        repairService.updateRepair(model);
    }

    @ApiOperation(value = "删除设施报修信息")
    @DeleteMapping("/{id}")
    public void deleteRepair(@PathVariable("id") Long id) {
        repairService.deleteRepair(id);
    }

    @ApiOperation(value = "派单（管理员操作）")
    @PostMapping("/assign")
    public void assignRepair(@RequestParam("id") Long id, @RequestParam("assignee") String assignee) {
        repairService.assignRepair(id, assignee);
    }

    @ApiOperation(value = "完成维修（管理员操作）")
    @PostMapping("/complete")
    public void completeRepair(@RequestParam("id") Long id, @RequestParam("repairResult") String repairResult) {
        repairService.completeRepair(id, repairResult);
    }

    @ApiOperation(value = "导出设施报修信息")
    @GetMapping("/export")
    public void exportRepair(@RequestParam(value = "facilityName", required = false) String facilityName,
                            @RequestParam(value = "facilityType", required = false) String facilityType,
                            @RequestParam(value = "status", required = false) Integer status,
                            HttpServletResponse response) {
        RepairModel model = new RepairModel();
        model.setFacilityName(facilityName);
        model.setFacilityType(facilityType);
        model.setStatus(status);
        RepairPageQuery query = new RepairPageQuery();
        query.setQuery(model);
        List<RepairModel> list = repairService.getList(query);

        String fileName = "repair-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("设施名称,设施类型,报修位置,报修描述,报修人,报修人电话,状态,派单人员,派单时间,完成时间,维修结果\n");
            for (RepairModel item : list) {
                sb.append(safeCsv(item.getFacilityName()))
                        .append(',')
                        .append(safeCsv(item.getFacilityType()))
                        .append(',')
                        .append(safeCsv(item.getLocation()))
                        .append(',')
                        .append(safeCsv(item.getDescription()))
                        .append(',')
                        .append(safeCsv(item.getReporterName()))
                        .append(',')
                        .append(safeCsv(item.getReporterPhone()))
                        .append(',')
                        .append(getStatusText(item.getStatus()))
                        .append(',')
                        .append(safeCsv(item.getAssignee()))
                        .append(',')
                        .append(item.getAssignTime() == null ? "" : formatter.format(item.getAssignTime()))
                        .append(',')
                        .append(item.getCompleteTime() == null ? "" : formatter.format(item.getCompleteTime()))
                        .append(',')
                        .append(safeCsv(item.getRepairResult()))
                        .append('\n');
            }
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    private String getStatusText(Integer status) {
        if (status == null) return "";
        if (status == 0) return "待派单";
        if (status == 1) return "维修中";
        if (status == 2) return "已完成";
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

