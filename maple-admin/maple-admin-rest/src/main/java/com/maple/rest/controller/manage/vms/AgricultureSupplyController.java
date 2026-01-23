package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsAgricultureSupplyService;
import com.maple.vms.vo.model.AgricultureSupplyModel;
import com.maple.vms.vo.query.AgricultureSupplyPageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农资发放记录接口.
 */
@Api(tags = "新农村管理-农资发放接口")
@RestController
@RequestMapping("/manage/vms/agricultureSupply")
@RequiredArgsConstructor
public class AgricultureSupplyController {

    private final IVmsAgricultureSupplyService supplyService;

    @ApiOperation(value = "获取农资发放列表")
    @PostMapping("/getPageList")
    public IPage<AgricultureSupplyModel> getPageList(@RequestBody AgricultureSupplyPageQuery req) {
        return supplyService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询农资发放信息")
    @GetMapping("/{id}")
    public AgricultureSupplyModel getAgricultureSupplyById(@PathVariable("id") Long id) {
        return supplyService.getAgricultureSupplyById(id);
    }

    @ApiOperation(value = "新增农资发放信息")
    @PostMapping("/create")
    public Long createAgricultureSupply(@RequestBody AgricultureSupplyModel model) {
        return supplyService.createAgricultureSupply(model);
    }

    @ApiOperation(value = "修改农资发放信息")
    @PostMapping("/update")
    public void updateAgricultureSupply(@RequestBody AgricultureSupplyModel model) {
        supplyService.updateAgricultureSupply(model);
    }

    @ApiOperation(value = "删除农资发放信息")
    @DeleteMapping("/{id}")
    public void deleteAgricultureSupply(@PathVariable("id") Long id) {
        supplyService.deleteAgricultureSupply(id);
    }
}

