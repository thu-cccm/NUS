package com.maple.vms.service;

import com.maple.vms.vo.model.DashboardModel;

/**
 * 数据看板服务接口.
 */
public interface IVmsDashboardService {

    /**
     * 获取首页数据看板.
     *
     * @return 看板数据
     */
    DashboardModel getDashboardData();
}

