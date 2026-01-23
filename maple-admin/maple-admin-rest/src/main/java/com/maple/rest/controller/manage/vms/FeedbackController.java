package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsFeedbackService;
import com.maple.vms.vo.model.FeedbackModel;
import com.maple.vms.vo.query.FeedbackPageQuery;
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
 * 村民互动接口.
 */
@Api(tags = "新农村管理-村民互动接口")
@RestController
@RequestMapping("/manage/vms/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final IVmsFeedbackService feedbackService;

    @ApiOperation(value = "获取留言列表")
    @PostMapping("/getPageList")
    public IPage<FeedbackModel> getPageList(@RequestBody FeedbackPageQuery req) {
        return feedbackService.getPageList(req);
    }

    @ApiOperation(value = "新增留言")
    @PostMapping("/create")
    public Long createFeedback(@RequestBody FeedbackModel model) {
        return feedbackService.createFeedback(model);
    }

    @ApiOperation(value = "回复留言")
    @PostMapping("/reply")
    public void replyFeedback(@RequestParam("id") Long id, @RequestParam("reply") String reply) {
        feedbackService.replyFeedback(id, reply);
    }

    @ApiOperation(value = "举报留言")
    @PostMapping("/report")
    public void reportFeedback(@RequestParam("id") Long id) {
        feedbackService.reportFeedback(id);
    }

    @ApiOperation(value = "隐藏/恢复留言")
    @PostMapping("/hide")
    public void hideFeedback(@RequestParam("id") Long id, @RequestParam("hidden") boolean hidden) {
        feedbackService.hideFeedback(id, hidden);
    }

    @ApiOperation(value = "删除留言")
    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable("id") Long id) {
        feedbackService.deleteFeedback(id);
    }

    @ApiOperation(value = "导出留言")
    @GetMapping("/export")
    public void exportFeedback(@RequestParam(value = "content", required = false) String content,
                               @RequestParam(value = "isPublic", required = false) Integer isPublic,
                               @RequestParam(value = "hasReply", required = false) Boolean hasReply,
                               @RequestParam(value = "isHidden", required = false) Integer isHidden,
                               HttpServletResponse response) {
        FeedbackModel model = new FeedbackModel();
        model.setContent(content);
        model.setIsPublic(isPublic);
        model.setHasReply(hasReply);
        model.setIsHidden(isHidden);
        FeedbackPageQuery query = new FeedbackPageQuery();
        query.setQuery(model);
        List<FeedbackModel> list = feedbackService.getList(query);

        String fileName = "feedback-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("留言内容,公开状态,回复状态,回复内容,举报次数,隐藏状态,创建时间\n");
            for (FeedbackModel item : list) {
                sb.append(safeCsv(item.getContent()))
                        .append(',')
                        .append(item.getIsPublic() != null && item.getIsPublic() == 1 ? "公开" : "不公开")
                        .append(',')
                        .append(item.getReplyContent() != null && !item.getReplyContent().isBlank() ? "已回复" : "未回复")
                        .append(',')
                        .append(safeCsv(item.getReplyContent()))
                        .append(',')
                        .append(item.getReportCount() == null ? 0 : item.getReportCount())
                        .append(',')
                        .append(item.getIsHidden() != null && item.getIsHidden() == 1 ? "已隐藏" : "正常")
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

