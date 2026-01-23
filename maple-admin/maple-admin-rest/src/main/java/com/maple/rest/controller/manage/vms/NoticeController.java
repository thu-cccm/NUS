package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsNoticeService;
import com.maple.vms.vo.model.NoticeModel;
import com.maple.vms.vo.model.NoticeReadRecordModel;
import com.maple.vms.vo.query.NoticePageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 通知公告接口.
 */
@Api(tags = "新农村管理-通知公告接口")
@RestController
@RequestMapping("/manage/vms/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final IVmsNoticeService noticeService;

    @ApiOperation(value = "获取公告列表")
    @PostMapping("/getPageList")
    public IPage<NoticeModel> getPageList(@RequestBody NoticePageQuery req) {
        return noticeService.getPageList(req);
    }

    @ApiOperation(value = "获取公告详情")
    @GetMapping("/{id}")
    public NoticeModel getNoticeById(@PathVariable("id") Long id) {
        return noticeService.getNoticeById(id);
    }

    @ApiOperation(value = "标记公告已读")
    @PostMapping("/read")
    public void markRead(@RequestParam("id") Long id) {
        noticeService.markRead(id);
    }

    @ApiOperation(value = "获取公告阅读记录")
    @GetMapping("/read/records")
    public List<NoticeReadRecordModel> getReadRecords(@RequestParam("id") Long id,
                                                      @RequestParam(value = "keyword", required = false) String keyword) {
        return noticeService.getReadRecords(id, keyword);
    }

    @ApiOperation(value = "导出公告阅读记录")
    @GetMapping("/read/export")
    public void exportReadRecords(@RequestParam("id") Long id,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  HttpServletResponse response) {
        List<NoticeReadRecordModel> records = noticeService.getReadRecords(id, keyword);
        String fileName = "notice-read-" + id + ".csv";
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("村民ID,姓名,阅读时间\n");
            for (NoticeReadRecordModel record : records) {
                sb.append(record.getResidentId() == null ? "" : record.getResidentId())
                        .append(',')
                        .append(safeCsv(record.getResidentName()))
                        .append(',')
                        .append(record.getReadTime() == null ? "" : record.getReadTime().getTime())
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

    @ApiOperation(value = "归档政策")
    @PostMapping("/archive")
    public void archivePolicy(@RequestParam("id") Long id, @RequestParam("archiveStatus") Integer archiveStatus) {
        noticeService.archivePolicy(id, archiveStatus);
    }

    @ApiOperation(value = "批量设置政策过期时间")
    @PostMapping("/expire/batch")
    public void batchExpire(@RequestParam("ids") List<Long> ids,
                            @RequestParam("expireTime") Long expireTime) {
        noticeService.batchUpdateExpireTime(ids, new java.util.Date(expireTime));
    }

    @ApiOperation(value = "新增公告")
    @PostMapping("/create")
    public Long createNotice(@RequestBody NoticeModel model) {
        return noticeService.createNotice(model);
    }

    @ApiOperation(value = "修改公告")
    @PostMapping("/update")
    public void updateNotice(@RequestBody NoticeModel model) {
        noticeService.updateNotice(model);
    }

    @ApiOperation(value = "删除公告")
    @DeleteMapping("/{id}")
    public void deleteNotice(@PathVariable("id") Long id) {
        noticeService.deleteNotice(id);
    }

    @ApiOperation(value = "获取最新公告")
    @GetMapping("/top")
    public List<NoticeModel> getTopList(@RequestParam(value = "limit", defaultValue = "5") int limit) {
        return noticeService.getTopList(limit);
    }
}

