package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsResidentService;
import com.maple.vms.vo.model.ResidentModel;
import com.maple.vms.vo.query.ResidentPageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 农村人口档案接口.
 */
@Api(tags = "新农村管理-人口档案接口")
@RestController
@RequestMapping("/manage/vms/resident")
@RequiredArgsConstructor
public class ResidentController {

    private final IVmsResidentService residentService;

    @ApiOperation(value = "获取居民列表")
    @PostMapping("/getPageList")
    public IPage<ResidentModel> getPageList(@RequestBody ResidentPageQuery req) {
        return residentService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询居民信息")
    @GetMapping("/{id}")
    public ResidentModel getResidentById(@PathVariable("id") Long id) {
        return residentService.getResidentById(id);
    }

    @ApiOperation(value = "新增居民信息")
    @PostMapping("/create")
    public Long createResident(@RequestBody ResidentModel model) {
        return residentService.createResident(model);
    }

    @ApiOperation(value = "修改居民信息")
    @PostMapping("/update")
    public void updateResident(@RequestBody ResidentModel model) {
        residentService.updateResident(model);
    }

    @ApiOperation(value = "删除居民信息")
    @DeleteMapping("/{id}")
    public void deleteResident(@PathVariable("id") Long id) {
        residentService.deleteResident(id);
    }
}

