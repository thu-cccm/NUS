package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsApplyService;
import com.maple.vms.vo.model.ApplyModel;
import com.maple.vms.vo.query.ApplyAuditQuery;
import com.maple.vms.vo.query.ApplyPageQuery;
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
 * 事务申请接口.
 */
@Api(tags = "新农村管理-事务申请接口")
@RestController
@RequestMapping("/manage/vms/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final IVmsApplyService applyService;

    @ApiOperation(value = "获取申请列表")
    @PostMapping("/getPageList")
    public IPage<ApplyModel> getPageList(@RequestBody ApplyPageQuery req) {
        return applyService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询申请信息")
    @GetMapping("/{id}")
    public ApplyModel getApplyById(@PathVariable("id") Long id) {
        return applyService.getApplyById(id);
    }

    @ApiOperation(value = "新增申请信息")
    @PostMapping("/create")
    public Long createApply(@RequestBody ApplyModel model) {
        return applyService.createApply(model);
    }

    @ApiOperation(value = "修改申请信息")
    @PostMapping("/update")
    public void updateApply(@RequestBody ApplyModel model) {
        applyService.updateApply(model);
    }

    @ApiOperation(value = "驳回后重新提交")
    @PostMapping("/resubmit")
    public void resubmitApply(@RequestBody ApplyModel model) {
        applyService.resubmitApply(model);
    }

    @ApiOperation(value = "删除申请信息")
    @DeleteMapping("/{id}")
    public void deleteApply(@PathVariable("id") Long id) {
        applyService.deleteApply(id);
    }

    @ApiOperation(value = "审核申请")
    @PostMapping("/audit")
    public void auditApply(@RequestBody ApplyAuditQuery query) {
        applyService.auditApply(query);
    }

    @ApiOperation(value = "归档申请")
    @PostMapping("/archive")
    public void archiveApply(@RequestParam("id") Long id, @RequestParam("archiveStatus") Integer archiveStatus) {
        applyService.archiveApply(id, archiveStatus);
    }

    @ApiOperation(value = "导出申请记录")
    @GetMapping("/export")
    public void exportApply(@RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "publicStatus", required = false) Integer publicStatus,
                            @RequestParam(value = "archiveStatus", required = false) Integer archiveStatus,
                            @RequestParam(value = "applyType", required = false) String applyType,
                            HttpServletResponse response) {
        ApplyModel query = new ApplyModel();
        query.setStatus(status);
        query.setPublicStatus(publicStatus);
        query.setArchiveStatus(archiveStatus);
        query.setApplyType(applyType);
        ApplyPageQuery pageQuery = new ApplyPageQuery();
        pageQuery.setQuery(query);
        List<ApplyModel> list = applyService.getList(pageQuery);

        String fileName = "apply-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("申请ID,申请类型,申请人ID,状态,公示状态,归档状态,审核人,审核意见,提交时间,审核时间\n");
            for (ApplyModel item : list) {
                sb.append(item.getId() == null ? "" : item.getId())
                        .append(',')
                        .append(safeCsv(item.getApplyType()))
                        .append(',')
                        .append(item.getResidentId() == null ? "" : item.getResidentId())
                        .append(',')
                        .append(statusLabel(item.getStatus()))
                        .append(',')
                        .append(publicLabel(item.getPublicStatus()))
                        .append(',')
                        .append(archiveLabel(item.getArchiveStatus()))
                        .append(',')
                        .append(safeCsv(item.getAuditBy()))
                        .append(',')
                        .append(safeCsv(item.getAuditReply()))
                        .append(',')
                        .append(item.getCreateTime() == null ? "" : formatter.format(item.getCreateTime()))
                        .append(',')
                        .append(item.getAuditTime() == null ? "" : formatter.format(item.getAuditTime()))
                        .append('\n');
            }
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    private String statusLabel(Integer status) {
        if (status == null) return "";
        if (status == 1) return "已通过";
        if (status == 2) return "已驳回";
        return "待审核";
    }

    private String publicLabel(Integer status) {
        if (status == null) return "";
        if (status == 1) return "公示中";
        if (status == 2) return "已公示";
        return "未公示";
    }

    private String archiveLabel(Integer status) {
        if (status == null) return "";
        return status == 1 ? "已归档" : "未归档";
    }

    private String safeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}

