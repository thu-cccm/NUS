package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsPartyScoreService;
import com.maple.vms.vo.model.PartyScoreModel;
import com.maple.vms.vo.query.PartyScorePageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 党员积分接口.
 */
@Api(tags = "新农村管理-党员积分接口")
@RestController
@RequestMapping("/manage/vms/partyScore")
@RequiredArgsConstructor
public class PartyScoreController {

    private final IVmsPartyScoreService partyScoreService;

    @ApiOperation(value = "获取党员积分列表")
    @PostMapping("/getPageList")
    public IPage<PartyScoreModel> getPageList(@RequestBody PartyScorePageQuery req) {
        return partyScoreService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询党员积分信息")
    @GetMapping("/{id}")
    public PartyScoreModel getPartyScoreById(@PathVariable("id") Long id) {
        return partyScoreService.getPartyScoreById(id);
    }

    @ApiOperation(value = "新增党员积分信息")
    @PostMapping("/create")
    public Long createPartyScore(@RequestBody PartyScoreModel model) {
        return partyScoreService.createPartyScore(model);
    }

    @ApiOperation(value = "修改党员积分信息")
    @PostMapping("/update")
    public void updatePartyScore(@RequestBody PartyScoreModel model) {
        partyScoreService.updatePartyScore(model);
    }

    @ApiOperation(value = "删除党员积分信息")
    @DeleteMapping("/{id}")
    public void deletePartyScore(@PathVariable("id") Long id) {
        partyScoreService.deletePartyScore(id);
    }

    @ApiOperation(value = "获取党员积分排行榜")
    @GetMapping("/ranking")
    public List<Map<String, Object>> getScoreRanking(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return partyScoreService.getScoreRanking(limit);
    }

    @ApiOperation(value = "导出党员积分信息")
    @GetMapping("/export")
    public void exportPartyScore(@RequestParam(value = "residentId", required = false) Long residentId,
                                @RequestParam(value = "scoreType", required = false) String scoreType,
                                HttpServletResponse response) {
        PartyScoreModel model = new PartyScoreModel();
        model.setResidentId(residentId);
        model.setScoreType(scoreType);
        PartyScorePageQuery query = new PartyScorePageQuery();
        query.setQuery(model);
        List<PartyScoreModel> list = partyScoreService.getList(query);

        String fileName = "party-score-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("党员姓名,积分类型,积分值,积分说明,创建时间\n");
            for (PartyScoreModel item : list) {
                sb.append(safeCsv(item.getResidentName()))
                        .append(',')
                        .append(safeCsv(item.getScoreType()))
                        .append(',')
                        .append(item.getScore() == null ? "0" : item.getScore())
                        .append(',')
                        .append(safeCsv(item.getDescription()))
                        .append(',')
                        .append(item.getCreateTime() == null ? "" : formatter.format(item.getCreateTime()))
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

