package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maple.vms.bean.Agriculture;
import com.maple.vms.bean.Apply;
import com.maple.vms.bean.Asset;
import com.maple.vms.bean.Dispute;
import com.maple.vms.bean.Finance;
import com.maple.vms.bean.Land;
import com.maple.vms.bean.Repair;
import com.maple.vms.bean.Resident;
import com.maple.vms.mapper.AgricultureMapper;
import com.maple.vms.mapper.ApplyMapper;
import com.maple.vms.mapper.AssetMapper;
import com.maple.vms.mapper.DisputeMapper;
import com.maple.vms.mapper.FinanceMapper;
import com.maple.vms.mapper.LandMapper;
import com.maple.vms.mapper.RepairMapper;
import com.maple.vms.mapper.ResidentMapper;
import com.maple.vms.mapper.ServiceMapper;
import com.maple.vms.service.IVmsDashboardService;
import com.maple.vms.vo.model.ApplyTrendModel;
import com.maple.vms.vo.model.DashboardModel;
import com.maple.vms.vo.model.GroupStatModel;
import com.maple.vms.vo.model.IndustryEconomyModel;
import com.maple.vms.vo.model.LandAreaStatModel;
import com.maple.vms.vo.model.LandTypeStatModel;
import com.maple.vms.vo.model.LandUseOverviewModel;
import com.maple.vms.vo.model.LivelihoodServiceModel;
import com.maple.vms.vo.model.PopulationOverviewModel;
import com.maple.vms.vo.model.PopulationStatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据看板服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsDashboardServiceImpl implements IVmsDashboardService {

    private final ResidentMapper residentMapper;
    private final LandMapper landMapper;
    private final ApplyMapper applyMapper;
    private final AgricultureMapper agricultureMapper;
    private final AssetMapper assetMapper;
    private final FinanceMapper financeMapper;
    private final RepairMapper repairMapper;
    private final DisputeMapper disputeMapper;
    private final ServiceMapper serviceMapper;

    @Override
    public DashboardModel getDashboardData() {
        DashboardModel model = new DashboardModel();
        model.setTotalResidents(residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class)));
        model.setPartyMembers(residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class).eq(Resident::getPolitics, "党员")));
        model.setPoorHouseholds(residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class).eq(Resident::getIsPoor, 1)));

        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDate nextMonth = firstDay.plusMonths(1);
        Date startDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(nextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        model.setPendingApplyCount(applyMapper.selectCount(Wrappers.lambdaQuery(Apply.class)
                .eq(Apply::getStatus, 0)
                .ge(Apply::getCreateTime, startDate)
                .lt(Apply::getCreateTime, endDate)));

        List<Map<String, Object>> landStats = landMapper.selectMaps(new QueryWrapper<Land>()
                .select("type", "count(*) as cnt")
                .groupBy("type"));
        model.setLandTypeStats(landStats.stream()
                .map(item -> new LandTypeStatModel(
                        String.valueOf(item.get("type")),
                        Long.parseLong(String.valueOf(item.get("cnt")))
                ))
                .collect(Collectors.toList()));

        List<Map<String, Object>> landAreaStats = landMapper.selectMaps(new QueryWrapper<Land>()
                .select("type", "sum(area) as total_area")
                .groupBy("type"));
        model.setLandAreaStats(landAreaStats.stream()
                .map(item -> new LandAreaStatModel(
                        String.valueOf(item.get("type")),
                        toBigDecimal(item.get("total_area"))
                ))
                .collect(Collectors.toList()));

        List<Map<String, Object>> groupStats = residentMapper.selectMaps(new QueryWrapper<Resident>()
                .select("group_no", "count(*) as cnt")
                .groupBy("group_no")
                .orderByAsc("group_no"));
        model.setGroupStats(groupStats.stream()
                .map(item -> new GroupStatModel(
                        Integer.parseInt(String.valueOf(item.get("group_no"))),
                        Long.parseLong(String.valueOf(item.get("cnt")))
                ))
                .collect(Collectors.toList()));

        List<Map<String, Object>> populationStats = residentMapper.selectMaps(new QueryWrapper<Resident>()
                .select("sum(case when age <= 17 then 1 else 0 end) as children_cnt",
                        "sum(case when age between 18 and 59 then 1 else 0 end) as labor_cnt",
                        "sum(case when age >= 60 then 1 else 0 end) as elder_cnt"));
        if (!populationStats.isEmpty()) {
            Map<String, Object> stat = populationStats.get(0);
            List<PopulationStatModel> popStats = new ArrayList<>();
            popStats.add(new PopulationStatModel("儿童(0-17)", toLong(stat.get("children_cnt"))));
            popStats.add(new PopulationStatModel("劳动力(18-59)", toLong(stat.get("labor_cnt"))));
            popStats.add(new PopulationStatModel("老年(60+)", toLong(stat.get("elder_cnt"))));
            model.setPopulationStats(popStats);
        }

        List<ApplyTrendModel> applyTrends = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月");
        for (int i = 5; i >= 0; i--) {
            LocalDate month = LocalDate.now().minusMonths(i).withDayOfMonth(1);
            LocalDate next = month.plusMonths(1);
            Date start = Date.from(month.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(next.atStartOfDay(ZoneId.systemDefault()).toInstant());
            long totalCount = applyMapper.selectCount(Wrappers.lambdaQuery(Apply.class)
                    .ge(Apply::getCreateTime, start)
                    .lt(Apply::getCreateTime, end));
            long finishedCount = applyMapper.selectCount(Wrappers.lambdaQuery(Apply.class)
                    .eq(Apply::getStatus, 1)
                    .ge(Apply::getCreateTime, start)
                    .lt(Apply::getCreateTime, end));
            applyTrends.add(new ApplyTrendModel(month.format(formatter), totalCount, finishedCount));
        }
        model.setApplyTrends(applyTrends);

        // 计算农业总产值
        BigDecimal totalAgriValue = calculateTotalAgricultureValue();
        model.setTotalAgricultureValue(totalAgriValue);

        // 四个核心维度数据
        model.setPopulationOverview(calculatePopulationOverview());
        model.setLandUseOverview(calculateLandUseOverview());
        model.setIndustryEconomy(calculateIndustryEconomy(totalAgriValue));
        model.setLivelihoodService(calculateLivelihoodService());

        return model;
    }

    /**
     * 计算农业总产值
     */
    private BigDecimal calculateTotalAgricultureValue() {
        List<Agriculture> agricultureList = agricultureMapper.selectList(null);
        return agricultureList.stream()
                .filter(ag -> ag.getEstimatedValue() != null)
                .map(Agriculture::getEstimatedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 人口概况维度
     */
    private PopulationOverviewModel calculatePopulationOverview() {
        long total = residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class));
        long partyMembers = residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class).eq(Resident::getPolitics, "党员"));
        long poorHouseholds = residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class).eq(Resident::getIsPoor, 1));
        long migratedOut = residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class).eq(Resident::getMigrateStatus, "迁出"));
        return new PopulationOverviewModel(total, partyMembers, poorHouseholds, migratedOut);
    }

    /**
     * 土地利用维度
     */
    private LandUseOverviewModel calculateLandUseOverview() {
        List<Map<String, Object>> landStats = landMapper.selectMaps(new QueryWrapper<Land>()
                .select("type", "sum(area) as total_area", "count(*) as cnt")
                .groupBy("type"));
        BigDecimal totalCultivated = BigDecimal.ZERO;
        BigDecimal totalHomestead = BigDecimal.ZERO;
        BigDecimal totalForest = BigDecimal.ZERO;
        long confirmedCount = 0;
        for (Map<String, Object> stat : landStats) {
            String type = String.valueOf(stat.get("type"));
            BigDecimal area = toBigDecimal(stat.get("total_area"));
            long count = toLong(stat.get("cnt"));
            if ("耕地".equals(type) || "承包地".equals(type)) {
                totalCultivated = totalCultivated.add(area);
            } else if ("宅基地".equals(type)) {
                totalHomestead = totalHomestead.add(area);
            } else if ("林地".equals(type)) {
                totalForest = totalForest.add(area);
            }
            confirmedCount += count;
        }
        return new LandUseOverviewModel(totalCultivated, totalHomestead, totalForest, confirmedCount);
    }

    /**
     * 产业经济维度
     */
    private IndustryEconomyModel calculateIndustryEconomy(BigDecimal totalAgriValue) {
        long totalAssets = assetMapper.selectCount(null);
        List<Asset> assets = assetMapper.selectList(null);
        BigDecimal totalAssetValue = assets.stream()
                .filter(a -> a.getOriginalValue() != null)
                .map(Asset::getOriginalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDate nextMonth = firstDay.plusMonths(1);
        Date startDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(nextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Finance> finances = financeMapper.selectList(Wrappers.lambdaQuery(Finance.class)
                .ge(Finance::getTransactionDate, startDate)
                .lt(Finance::getTransactionDate, endDate));
        BigDecimal monthlyIncome = finances.stream()
                .filter(f -> "收入".equals(f.getFinanceType()) && f.getAmount() != null)
                .map(Finance::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal monthlyExpense = finances.stream()
                .filter(f -> "支出".equals(f.getFinanceType()) && f.getAmount() != null)
                .map(Finance::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new IndustryEconomyModel(totalAgriValue, totalAssets, totalAssetValue, monthlyIncome, monthlyExpense);
    }

    /**
     * 民生服务维度
     */
    private LivelihoodServiceModel calculateLivelihoodService() {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDate nextMonth = firstDay.plusMonths(1);
        Date startDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(nextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        long pendingApply = applyMapper.selectCount(Wrappers.lambdaQuery(Apply.class)
                .eq(Apply::getStatus, 0)
                .ge(Apply::getCreateTime, startDate)
                .lt(Apply::getCreateTime, endDate));
        long serviceCount = serviceMapper.selectCount(Wrappers.lambdaQuery(com.maple.vms.bean.Service.class)
                .eq(com.maple.vms.bean.Service::getStatus, 1)
                .eq(com.maple.vms.bean.Service::getDeleted, 0));
        long pendingRepair = repairMapper.selectCount(Wrappers.lambdaQuery(Repair.class).eq(Repair::getStatus, 0));
        long pendingDispute = disputeMapper.selectCount(Wrappers.lambdaQuery(Dispute.class).eq(Dispute::getStatus, 0));
        return new LivelihoodServiceModel(pendingApply, serviceCount, pendingRepair, pendingDispute);
    }

    private Long toLong(Object value) {
        if (value == null) return 0L;
        return Long.parseLong(String.valueOf(value));
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        return new BigDecimal(String.valueOf(value));
    }
}

