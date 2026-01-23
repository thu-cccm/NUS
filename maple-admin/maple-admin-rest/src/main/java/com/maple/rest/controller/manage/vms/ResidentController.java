package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsResidentService;
import com.maple.vms.vo.model.ResidentModel;
import com.maple.vms.vo.query.ResidentPageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

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

    @ApiOperation(value = "按户分组查询居民信息")
    @PostMapping("/getByFamily")
    public List<ResidentModel> getResidentsByFamily(@RequestBody ResidentPageQuery req) {
        return residentService.getResidentsByFamily(req);
    }

    @ApiOperation(value = "迁出操作")
    @PostMapping("/migrateOut")
    public void migrateOut(@RequestParam("id") Long id, @RequestParam("migrateTo") String migrateTo) {
        residentService.migrateOut(id, migrateTo);
    }

    @ApiOperation(value = "迁入操作")
    @PostMapping("/migrateIn")
    public void migrateIn(@RequestParam("id") Long id) {
        residentService.migrateIn(id);
    }

    @ApiOperation(value = "导出居民信息")
    @GetMapping("/export")
    public void exportResident(@RequestParam(value = "realName", required = false) String realName,
                              @RequestParam(value = "groupNo", required = false) Integer groupNo,
                              @RequestParam(value = "isPoor", required = false) Integer isPoor,
                              HttpServletResponse response) {
        ResidentModel model = new ResidentModel();
        model.setRealName(realName);
        model.setGroupNo(groupNo);
        model.setIsPoor(isPoor);
        ResidentPageQuery query = new ResidentPageQuery();
        query.setQuery(model);
        List<ResidentModel> list = residentService.getList(query);

        String fileName = "resident-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("姓名,身份证号,性别,年龄,联系电话,所属村组,政治面貌,是否贫困户,健康状况,建档时间\n");
            for (ResidentModel item : list) {
                sb.append(safeCsv(item.getRealName()))
                        .append(',')
                        .append(safeCsv(item.getIdCard()))
                        .append(',')
                        .append(item.getGender() != null && item.getGender() == 1 ? "男" : "女")
                        .append(',')
                        .append(item.getAge() == null ? "" : item.getAge())
                        .append(',')
                        .append(safeCsv(item.getPhone()))
                        .append(',')
                        .append(item.getGroupNo() == null ? "" : item.getGroupNo() + "组")
                        .append(',')
                        .append(safeCsv(item.getPolitics()))
                        .append(',')
                        .append(item.getIsPoor() != null && item.getIsPoor() == 1 ? "是" : "否")
                        .append(',')
                        .append(safeCsv(item.getHealthStatus()))
                        .append(',')
                        .append(item.getCreateTime() == null ? "" : formatter.format(item.getCreateTime()))
                        .append('\n');
            }
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    private String safeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}

