package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsVoteService;
import com.maple.vms.vo.model.VoteModel;
import com.maple.vms.vo.model.VoteRecordModel;
import com.maple.vms.vo.model.VoteRecordPageResult;
import com.maple.vms.vo.model.VoteTrendModel;
import com.maple.vms.vo.query.VotePageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * 民主互动接口.
 */
@Api(tags = "新农村管理-民主互动接口")
@RestController
@RequestMapping("/manage/vms/vote")
@RequiredArgsConstructor
public class VoteController {

    private final IVmsVoteService voteService;

    @ApiOperation(value = "获取投票列表")
    @PostMapping("/getPageList")
    public IPage<VoteModel> getPageList(@RequestBody VotePageQuery req) {
        return voteService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询投票信息")
    @GetMapping("/{id}")
    public VoteModel getVoteById(@PathVariable("id") Long id) {
        return voteService.getVoteById(id);
    }

    @ApiOperation(value = "新增投票议题")
    @PostMapping("/create")
    public Long createVote(@RequestBody VoteModel model) {
        return voteService.createVote(model);
    }

    @ApiOperation(value = "修改投票议题")
    @PostMapping("/update")
    public void updateVote(@RequestBody VoteModel model) {
        voteService.updateVote(model);
    }

    @ApiOperation(value = "删除投票议题")
    @DeleteMapping("/{id}")
    public void deleteVote(@PathVariable("id") Long id) {
        voteService.deleteVote(id);
    }

    @ApiOperation(value = "村民投票")
    @PostMapping("/cast")
    public void castVote(@RequestParam("id") Long id, @RequestParam("agree") boolean agree) {
        voteService.castVote(id, agree);
    }

    @ApiOperation(value = "管理员结束投票")
    @PostMapping("/end")
    public void endVote(@RequestParam("id") Long id) {
        voteService.endVote(id);
    }

    @ApiOperation(value = "获取投票记录")
    @GetMapping("/records")
    public List<VoteRecordModel> getVoteRecords(@RequestParam("id") Long id,
                                                @RequestParam(value = "agree", required = false) Boolean agree,
                                                @RequestParam(value = "keyword", required = false) String keyword) {
        return voteService.getVoteRecords(id, agree, keyword);
    }

    @ApiOperation(value = "投票记录分页")
    @GetMapping("/records/page")
    public VoteRecordPageResult getVoteRecordPage(@RequestParam("id") Long id,
                                                  @RequestParam(value = "current", defaultValue = "1") int current,
                                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                                  @RequestParam(value = "agree", required = false) Boolean agree,
                                                  @RequestParam(value = "keyword", required = false) String keyword) {
        return voteService.getVoteRecordPage(id, current, size, agree, keyword);
    }

    @ApiOperation(value = "导出投票记录")
    @GetMapping("/records/export")
    public void exportVoteRecords(@RequestParam("id") Long id,
                                  @RequestParam(value = "agree", required = false) Boolean agree,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  HttpServletResponse response) throws IOException {
        List<VoteRecordModel> records = voteService.getVoteRecords(id, agree, keyword);
        StringBuilder sb = new StringBuilder();
        sb.append('\uFEFF');
        sb.append("姓名,投票结果,投票时间\n");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (VoteRecordModel record : records) {
            sb.append(safeCsv(record.getResidentName())).append(',')
              .append(Boolean.TRUE.equals(record.getAgree()) ? "赞成" : "反对").append(',')
              .append(record.getCreateTime() == null ? "" : formatter.format(record.getCreateTime()))
              .append('\n');
        }
        String fileName = "vote-records-" + id + ".csv";
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        response.getOutputStream().write(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    @ApiOperation(value = "获取投票趋势")
    @GetMapping("/trend")
    public List<VoteTrendModel> getVoteTrend(@RequestParam("id") Long id,
                                             @RequestParam(value = "days", required = false) Integer days,
                                             @RequestParam(value = "unit", required = false) String unit) {
        return voteService.getVoteTrend(id, days, unit);
    }

    private String safeCsv(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}

