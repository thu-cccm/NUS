package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Finance;
import com.maple.vms.mapper.FinanceMapper;
import com.maple.vms.service.IVmsFinanceService;
import com.maple.vms.vo.model.FinanceModel;
import com.maple.vms.vo.query.FinancePageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 财务流水服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsFinanceServiceImpl extends ServiceImpl<FinanceMapper, Finance> implements IVmsFinanceService {

    private final FinanceMapper financeMapper;

    @Override
    public IPage<FinanceModel> getPageList(FinancePageQuery query) {
        FinanceModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Finance> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String financeType = model == null ? null : model.getFinanceType();
        String category = model == null ? null : model.getCategory();
        IPage<Finance> result = financeMapper.selectPage(page, Wrappers.lambdaQuery(Finance.class)
                .eq(StringUtils.isNotBlank(financeType), Finance::getFinanceType, financeType)
                .eq(StringUtils.isNotBlank(category), Finance::getCategory, category)
                .eq(Finance::getDeleted, 0)
                .orderByDesc(Finance::getTransactionDate)
                .orderByDesc(Finance::getId));
        return toModelPage(result, FinanceModel.class);
    }

    @Override
    public List<FinanceModel> getList(FinancePageQuery query) {
        FinanceModel model = Objects.isNull(query) ? null : query.getQuery();
        String financeType = model == null ? null : model.getFinanceType();
        String category = model == null ? null : model.getCategory();
        List<Finance> list = financeMapper.selectList(Wrappers.lambdaQuery(Finance.class)
                .eq(StringUtils.isNotBlank(financeType), Finance::getFinanceType, financeType)
                .eq(StringUtils.isNotBlank(category), Finance::getCategory, category)
                .eq(Finance::getDeleted, 0)
                .orderByDesc(Finance::getTransactionDate)
                .orderByDesc(Finance::getId));
        return TransformUtils.mapList(list, FinanceModel.class);
    }

    @Override
    public FinanceModel getFinanceById(Long id) {
        return TransformUtils.map(financeMapper.selectById(id), FinanceModel.class);
    }

    @Override
    public Long createFinance(FinanceModel model) {
        Finance finance = TransformUtils.map(model, Finance.class);
        if (finance.getDeleted() == null) {
            finance.setDeleted(0);
        }
        financeMapper.insert(finance);
        return finance.getId();
    }

    @Override
    public void updateFinance(FinanceModel model) {
        financeMapper.updateById(TransformUtils.map(model, Finance.class));
    }

    @Override
    public void deleteFinance(Long id) {
        Finance finance = financeMapper.selectById(id);
        if (finance != null) {
            finance.setDeleted(1);
            financeMapper.updateById(finance);
        }
    }

    private <T> IPage<T> toModelPage(IPage<Finance> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

