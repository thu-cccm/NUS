package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsInfrastructureService;
import com.maple.vms.vo.model.InfrastructureModel;
import com.maple.vms.vo.query.InfrastructurePageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 基础设施接口.
 */
@Api(tags = "新农村管理-基础设施接口")
@RestController
@RequestMapping("/manage/vms/infrastructure")
@RequiredArgsConstructor
public class InfrastructureController {

    private final IVmsInfrastructureService infrastructureService;

    @ApiOperation(value = "获取基础设施列表")
    @PostMapping("/getPageList")
    public IPage<InfrastructureModel> getPageList(@RequestBody InfrastructurePageQuery req) {
        return infrastructureService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询基础设施信息")
    @GetMapping("/{id}")
    public InfrastructureModel getInfrastructureById(@PathVariable("id") Long id) {
        return infrastructureService.getInfrastructureById(id);
    }

    @ApiOperation(value = "新增基础设施信息")
    @PostMapping("/create")
    public Long createInfrastructure(@RequestBody InfrastructureModel model) {
        return infrastructureService.createInfrastructure(model);
    }

    @ApiOperation(value = "修改基础设施信息")
    @PostMapping("/update")
    public void updateInfrastructure(@RequestBody InfrastructureModel model) {
        infrastructureService.updateInfrastructure(model);
    }

    @ApiOperation(value = "删除基础设施信息")
    @DeleteMapping("/{id}")
    public void deleteInfrastructure(@PathVariable("id") Long id) {
        infrastructureService.deleteInfrastructure(id);
    }
}

