package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsAgricultureService;
import com.maple.vms.vo.model.AgricultureModel;
import com.maple.vms.vo.query.AgriculturePageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 农业生产接口.
 */
@Api(tags = "新农村管理-农业生产接口")
@RestController
@RequestMapping("/manage/vms/agriculture")
@RequiredArgsConstructor
public class AgricultureController {

    private final IVmsAgricultureService agricultureService;

    @ApiOperation(value = "获取农业生产列表")
    @PostMapping("/getPageList")
    public IPage<AgricultureModel> getPageList(@RequestBody AgriculturePageQuery req) {
        return agricultureService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询农业生产信息")
    @GetMapping("/{id}")
    public AgricultureModel getAgricultureById(@PathVariable("id") Long id) {
        return agricultureService.getAgricultureById(id);
    }

    @ApiOperation(value = "新增农业生产信息")
    @PostMapping("/create")
    public Long createAgriculture(@RequestBody AgricultureModel model) {
        return agricultureService.createAgriculture(model);
    }

    @ApiOperation(value = "修改农业生产信息")
    @PostMapping("/update")
    public void updateAgriculture(@RequestBody AgricultureModel model) {
        agricultureService.updateAgriculture(model);
    }

    @ApiOperation(value = "删除农业生产信息")
    @DeleteMapping("/{id}")
    public void deleteAgriculture(@PathVariable("id") Long id) {
        agricultureService.deleteAgriculture(id);
    }
}

