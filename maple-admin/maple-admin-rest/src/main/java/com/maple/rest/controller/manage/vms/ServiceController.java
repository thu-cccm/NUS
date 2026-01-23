package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsServiceService;
import com.maple.vms.vo.model.ServiceModel;
import com.maple.vms.vo.query.ServicePageQuery;
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
 * 便民服务接口.
 */
@Api(tags = "新农村管理-便民服务接口")
@RestController
@RequestMapping("/manage/vms/service")
@RequiredArgsConstructor
public class ServiceController {

    private final IVmsServiceService serviceService;

    @ApiOperation(value = "获取便民服务列表")
    @PostMapping("/getPageList")
    public IPage<ServiceModel> getPageList(@RequestBody ServicePageQuery req) {
        return serviceService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询便民服务信息")
    @GetMapping("/{id}")
    public ServiceModel getServiceById(@PathVariable("id") Long id) {
        return serviceService.getServiceById(id);
    }

    @ApiOperation(value = "新增便民服务信息")
    @PostMapping("/create")
    public Long createService(@RequestBody ServiceModel model) {
        return serviceService.createService(model);
    }

    @ApiOperation(value = "修改便民服务信息")
    @PostMapping("/update")
    public void updateService(@RequestBody ServiceModel model) {
        serviceService.updateService(model);
    }

    @ApiOperation(value = "删除便民服务信息")
    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable("id") Long id) {
        serviceService.deleteService(id);
    }

    @ApiOperation(value = "导出便民服务信息")
    @GetMapping("/export")
    public void exportService(@RequestParam(value = "serviceName", required = false) String serviceName,
                             @RequestParam(value = "serviceType", required = false) String serviceType,
                             @RequestParam(value = "category", required = false) String category,
                             HttpServletResponse response) {
        ServiceModel model = new ServiceModel();
        model.setServiceName(serviceName);
        model.setServiceType(serviceType);
        model.setCategory(category);
        ServicePageQuery query = new ServicePageQuery();
        query.setQuery(model);
        List<ServiceModel> list = serviceService.getList(query);

        String fileName = "service-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("服务名称,服务类型,分类,联系电话,联系人,地址,服务描述,排班/开放时间/招工信息,状态\n");
            for (ServiceModel item : list) {
                sb.append(safeCsv(item.getServiceName()))
                        .append(',')
                        .append(safeCsv(item.getServiceType()))
                        .append(',')
                        .append(safeCsv(item.getCategory()))
                        .append(',')
                        .append(safeCsv(item.getContactPhone()))
                        .append(',')
                        .append(safeCsv(item.getContactPerson()))
                        .append(',')
                        .append(safeCsv(item.getAddress()))
                        .append(',')
                        .append(safeCsv(item.getDescription()))
                        .append(',')
                        .append(safeCsv(item.getSchedule()))
                        .append(',')
                        .append(item.getStatus() == 1 ? "启用" : "停用")
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

