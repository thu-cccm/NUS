package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsVoteService;
import com.maple.vms.vo.model.VoteModel;
import com.maple.vms.vo.query.VotePageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 民主互动接口.
 */
@Api(tags = "新农村管理-民主互动接口")
@RestController
@RequestMapping("/manage/vms/vote")
@RequiredArgsConstructor
public class VoteController {

    private final IVmsVoteService voteService;

    @ApiOperation(value = "获取投票列表")
    @PostMapping("/getPageList")
    public IPage<VoteModel> getPageList(@RequestBody VotePageQuery req) {
        return voteService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询投票信息")
    @GetMapping("/{id}")
    public VoteModel getVoteById(@PathVariable("id") Long id) {
        return voteService.getVoteById(id);
    }

    @ApiOperation(value = "新增投票议题")
    @PostMapping("/create")
    public Long createVote(@RequestBody VoteModel model) {
        return voteService.createVote(model);
    }

    @ApiOperation(value = "修改投票议题")
    @PostMapping("/update")
    public void updateVote(@RequestBody VoteModel model) {
        voteService.updateVote(model);
    }

    @ApiOperation(value = "删除投票议题")
    @DeleteMapping("/{id}")
    public void deleteVote(@PathVariable("id") Long id) {
        voteService.deleteVote(id);
    }

    @ApiOperation(value = "村民投票")
    @PostMapping("/cast")
    public void castVote(@RequestParam("id") Long id, @RequestParam("agree") boolean agree) {
        voteService.castVote(id, agree);
    }

    @ApiOperation(value = "管理员结束投票")
    @PostMapping("/end")
    public void endVote(@RequestParam("id") Long id) {
        voteService.endVote(id);
    }
}

