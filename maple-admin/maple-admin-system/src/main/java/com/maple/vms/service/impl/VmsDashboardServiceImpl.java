package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maple.vms.bean.Apply;
import com.maple.vms.bean.Land;
import com.maple.vms.bean.Resident;
import com.maple.vms.mapper.ApplyMapper;
import com.maple.vms.mapper.LandMapper;
import com.maple.vms.mapper.ResidentMapper;
import com.maple.vms.service.IVmsDashboardService;
import com.maple.vms.vo.model.DashboardModel;
import com.maple.vms.vo.model.GroupStatModel;
import com.maple.vms.vo.model.LandTypeStatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
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

        return model;
    }
}

