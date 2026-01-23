package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Finance;
import com.maple.vms.vo.model.FinanceModel;
import com.maple.vms.vo.query.FinancePageQuery;

import java.util.List;

/**
 * 财务流水服务接口.
 */
public interface IVmsFinanceService extends IService<Finance> {

    /**
     * 分页查询财务流水.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<FinanceModel> getPageList(FinancePageQuery query);

    /**
     * 获取财务流水列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 财务流水列表
     */
    List<FinanceModel> getList(FinancePageQuery query);

    /**
     * 根据ID获取财务流水信息.
     *
     * @param id 主键
     * @return 财务流水信息
     */
    FinanceModel getFinanceById(Long id);

    /**
     * 新增财务流水.
     *
     * @param model 财务流水模型
     * @return 主键ID
     */
    Long createFinance(FinanceModel model);

    /**
     * 修改财务流水.
     *
     * @param model 财务流水模型
     */
    void updateFinance(FinanceModel model);

    /**
     * 删除财务流水.
     *
     * @param id 主键
     */
    void deleteFinance(Long id);
}

