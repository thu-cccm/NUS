package com.maple.rest.controller.manage.vms;

import com.maple.vms.service.IVmsDashboardService;
import com.maple.vms.vo.model.DashboardModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据看板接口.
 */
@Api(tags = "新农村管理-数据看板接口")
@RestController
@RequestMapping("/manage/vms/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IVmsDashboardService dashboardService;

    @ApiOperation(value = "获取数据看板信息")
    @GetMapping("/summary")
    public DashboardModel getSummary() {
        return dashboardService.getDashboardData();
    }
}

