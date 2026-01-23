package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsPartyActivityService;
import com.maple.vms.vo.model.PartyActivityModel;
import com.maple.vms.vo.query.PartyActivityPageQuery;
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
 * 党建活动接口.
 */
@Api(tags = "新农村管理-党建活动接口")
@RestController
@RequestMapping("/manage/vms/partyActivity")
@RequiredArgsConstructor
public class PartyActivityController {

    private final IVmsPartyActivityService partyActivityService;

    @ApiOperation(value = "获取党建活动列表")
    @PostMapping("/getPageList")
    public IPage<PartyActivityModel> getPageList(@RequestBody PartyActivityPageQuery req) {
        return partyActivityService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询党建活动信息")
    @GetMapping("/{id}")
    public PartyActivityModel getPartyActivityById(@PathVariable("id") Long id) {
        return partyActivityService.getPartyActivityById(id);
    }

    @ApiOperation(value = "新增党建活动信息")
    @PostMapping("/create")
    public Long createPartyActivity(@RequestBody PartyActivityModel model) {
        return partyActivityService.createPartyActivity(model);
    }

    @ApiOperation(value = "修改党建活动信息")
    @PostMapping("/update")
    public void updatePartyActivity(@RequestBody PartyActivityModel model) {
        partyActivityService.updatePartyActivity(model);
    }

    @ApiOperation(value = "删除党建活动信息")
    @DeleteMapping("/{id}")
    public void deletePartyActivity(@PathVariable("id") Long id) {
        partyActivityService.deletePartyActivity(id);
    }

    @ApiOperation(value = "导出党建活动信息")
    @GetMapping("/export")
    public void exportPartyActivity(@RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "type", required = false) String type,
                                   HttpServletResponse response) {
        PartyActivityModel model = new PartyActivityModel();
        model.setTitle(title);
        model.setType(type);
        PartyActivityPageQuery query = new PartyActivityPageQuery();
        query.setQuery(model);
        List<PartyActivityModel> list = partyActivityService.getList(query);

        String fileName = "party-activity-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("活动标题,活动类型,开展时间,活动地点,参会人员,会议纪要\n");
            for (PartyActivityModel item : list) {
                sb.append(safeCsv(item.getTitle()))
                        .append(',')
                        .append(safeCsv(item.getType()))
                        .append(',')
                        .append(item.getHoldTime() == null ? "" : formatter.format(item.getHoldTime()))
                        .append(',')
                        .append(safeCsv(item.getLocation()))
                        .append(',')
                        .append(safeCsv(item.getParticipants()))
                        .append(',')
                        .append(safeCsv(item.getSummary()))
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

