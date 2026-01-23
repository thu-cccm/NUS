package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsFinanceService;
import com.maple.vms.vo.model.FinanceModel;
import com.maple.vms.vo.query.FinancePageQuery;
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
 * 财务流水接口.
 */
@Api(tags = "新农村管理-财务流水接口")
@RestController
@RequestMapping("/manage/vms/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final IVmsFinanceService financeService;

    @ApiOperation(value = "获取财务流水列表")
    @PostMapping("/getPageList")
    public IPage<FinanceModel> getPageList(@RequestBody FinancePageQuery req) {
        return financeService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询财务流水信息")
    @GetMapping("/{id}")
    public FinanceModel getFinanceById(@PathVariable("id") Long id) {
        return financeService.getFinanceById(id);
    }

    @ApiOperation(value = "新增财务流水")
    @PostMapping("/create")
    public Long createFinance(@RequestBody FinanceModel model) {
        return financeService.createFinance(model);
    }

    @ApiOperation(value = "修改财务流水")
    @PostMapping("/update")
    public void updateFinance(@RequestBody FinanceModel model) {
        financeService.updateFinance(model);
    }

    @ApiOperation(value = "删除财务流水")
    @DeleteMapping("/{id}")
    public void deleteFinance(@PathVariable("id") Long id) {
        financeService.deleteFinance(id);
    }

    @ApiOperation(value = "导出财务流水")
    @GetMapping("/export")
    public void exportFinance(@RequestParam(value = "financeType", required = false) String financeType,
                             @RequestParam(value = "category", required = false) String category,
                             HttpServletResponse response) {
        FinanceModel model = new FinanceModel();
        model.setFinanceType(financeType);
        model.setCategory(category);
        FinancePageQuery query = new FinancePageQuery();
        query.setQuery(model);
        List<FinanceModel> list = financeService.getList(query);

        String fileName = "finance-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("类型,分类,金额(元),说明,交易日期,付款方/收款方,凭证号\n");
            for (FinanceModel item : list) {
                sb.append(safeCsv(item.getFinanceType()))
                        .append(',')
                        .append(safeCsv(item.getCategory()))
                        .append(',')
                        .append(item.getAmount() == null ? "" : item.getAmount())
                        .append(',')
                        .append(safeCsv(item.getDescription()))
                        .append(',')
                        .append(item.getTransactionDate() == null ? "" : formatter.format(item.getTransactionDate()))
                        .append(',')
                        .append(safeCsv(item.getPayer()))
                        .append(',')
                        .append(safeCsv(item.getVoucherNo()))
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

