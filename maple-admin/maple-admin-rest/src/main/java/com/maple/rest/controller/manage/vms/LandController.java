package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsLandService;
import com.maple.vms.vo.model.LandModel;
import com.maple.vms.vo.query.LandPageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 土地资源接口.
 */
@Api(tags = "新农村管理-土地资源接口")
@RestController
@RequestMapping("/manage/vms/land")
@RequiredArgsConstructor
public class LandController {

    private final IVmsLandService landService;

    @ApiOperation(value = "获取土地列表")
    @PostMapping("/getPageList")
    public IPage<LandModel> getPageList(@RequestBody LandPageQuery req) {
        return landService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询土地信息")
    @GetMapping("/{id}")
    public LandModel getLandById(@PathVariable("id") Long id) {
        return landService.getLandById(id);
    }

    @ApiOperation(value = "新增土地信息")
    @PostMapping("/create")
    public Long createLand(@RequestBody LandModel model) {
        return landService.createLand(model);
    }

    @ApiOperation(value = "修改土地信息")
    @PostMapping("/update")
    public void updateLand(@RequestBody LandModel model) {
        landService.updateLand(model);
    }

    @ApiOperation(value = "删除土地信息")
    @DeleteMapping("/{id}")
    public void deleteLand(@PathVariable("id") Long id) {
        landService.deleteLand(id);
    }
}

