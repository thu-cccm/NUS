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
}

