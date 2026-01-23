package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsLandService;
import com.maple.vms.vo.model.LandModel;
import com.maple.vms.vo.query.LandPageQuery;
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
 * 土地资源接口.
 */
@Api(tags = "新农村管理-土地资源接口")
@RestController
@RequestMapping("/manage/vms/land")
@RequiredArgsConstructor
public class LandController {

    private final IVmsLandService landService;

    @ApiOperation(value = "获取土地列表")
    @PostMapping("/getPageList")
    public IPage<LandModel> getPageList(@RequestBody LandPageQuery req) {
        return landService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询土地信息")
    @GetMapping("/{id}")
    public LandModel getLandById(@PathVariable("id") Long id) {
        return landService.getLandById(id);
    }

    @ApiOperation(value = "新增土地信息")
    @PostMapping("/create")
    public Long createLand(@RequestBody LandModel model) {
        return landService.createLand(model);
    }

    @ApiOperation(value = "修改土地信息")
    @PostMapping("/update")
    public void updateLand(@RequestBody LandModel model) {
        landService.updateLand(model);
    }

    @ApiOperation(value = "删除土地信息")
    @DeleteMapping("/{id}")
    public void deleteLand(@PathVariable("id") Long id) {
        landService.deleteLand(id);
    }

    @ApiOperation(value = "导出土地信息")
    @GetMapping("/export")
    public void exportLand(@RequestParam(value = "landCode", required = false) String landCode,
                           @RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "status", required = false) Integer status,
                           HttpServletResponse response) {
        LandModel model = new LandModel();
        model.setLandCode(landCode);
        model.setType(type);
        model.setStatus(status);
        LandPageQuery query = new LandPageQuery();
        query.setQuery(model);
        List<LandModel> list = landService.getList(query);

        String fileName = "land-export.csv";
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("地块编号,户主ID,面积(亩),土地类型,位置描述,确权状态,确权证书\n");
            for (LandModel item : list) {
                sb.append(safeCsv(item.getLandCode()))
                        .append(',')
                        .append(item.getResidentId() == null ? "" : item.getResidentId())
                        .append(',')
                        .append(item.getArea() == null ? "" : item.getArea())
                        .append(',')
                        .append(safeCsv(item.getType()))
                        .append(',')
                        .append(safeCsv(item.getLocation()))
                        .append(',')
                        .append(item.getStatus() != null && item.getStatus() == 1 ? "已确权" : "待确权")
                        .append(',')
                        .append(safeCsv(item.getCertUrl()))
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

