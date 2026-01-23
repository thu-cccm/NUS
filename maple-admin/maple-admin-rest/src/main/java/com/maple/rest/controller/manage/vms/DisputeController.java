package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsDisputeService;
import com.maple.vms.vo.model.DisputeModel;
import com.maple.vms.vo.query.DisputePageQuery;
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
 * 矛盾纠纷调解接口.
 */
@Api(tags = "新农村管理-矛盾纠纷调解接口")
@RestController
@RequestMapping("/manage/vms/dispute")
@RequiredArgsConstructor
public class DisputeController {

    private final IVmsDisputeService disputeService;

    @ApiOperation(value = "获取矛盾纠纷列表")
    @PostMapping("/getPageList")
    public IPage<DisputeModel> getPageList(@RequestBody DisputePageQuery req) {
        return disputeService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询矛盾纠纷信息")
    @GetMapping("/{id}")
    public DisputeModel getDisputeById(@PathVariable("id") Long id) {
        return disputeService.getDisputeById(id);
    }

    @ApiOperation(value = "新增矛盾纠纷信息")
    @PostMapping("/create")
    public Long createDispute(@RequestBody DisputeModel model) {
        return disputeService.createDispute(model);
    }

    @ApiOperation(value = "修改矛盾纠纷信息")
    @PostMapping("/update")
    public void updateDispute(@RequestBody DisputeModel model) {
        disputeService.updateDispute(model);
    }

    @ApiOperation(value = "删除矛盾纠纷信息")
    @DeleteMapping("/{id}")
    public void deleteDispute(@PathVariable("id") Long id) {
        disputeService.deleteDispute(id);
    }

    @ApiOperation(value = "导出矛盾纠纷信息")
    @GetMapping("/export")
    public void exportDispute(@RequestParam(value = "partyA", required = false) String partyA,
                             @RequestParam(value = "conflictType", required = false) String conflictType,
                             @RequestParam(value = "status", required = false) Integer status,
                             HttpServletResponse response) {
        DisputeModel model = new DisputeModel();
        model.setPartyA(partyA);
        model.setConflictType(conflictType);
        model.setStatus(status);
        DisputePageQuery query = new DisputePageQuery();
        query.setQuery(model);
        List<DisputeModel> list = disputeService.getList(query);

        String fileName = "dispute-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("当事人A,当事人B,矛盾类型,发生时间,纠纷描述,调解员,调解结果,状态\n");
            for (DisputeModel item : list) {
                sb.append(safeCsv(item.getPartyA()))
                        .append(',')
                        .append(safeCsv(item.getPartyB()))
                        .append(',')
                        .append(safeCsv(item.getConflictType()))
                        .append(',')
                        .append(item.getHappenTime() == null ? "" : formatter.format(item.getHappenTime()))
                        .append(',')
                        .append(safeCsv(item.getDescription()))
                        .append(',')
                        .append(safeCsv(item.getMediator()))
                        .append(',')
                        .append(safeCsv(item.getResultContent()))
                        .append(',')
                        .append(getStatusText(item.getStatus()))
                        .append('\n');
            }
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    private String getStatusText(Integer status) {
        if (status == null) return "";
        if (status == 0) return "待处理";
        if (status == 1) return "处理中";
        if (status == 2) return "已结案";
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

