package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Notice;
import com.maple.vms.vo.model.NoticeModel;
import com.maple.vms.vo.model.NoticeReadRecordModel;
import com.maple.vms.vo.query.NoticePageQuery;

import java.util.Date;
import java.util.List;

/**
 * 通知公告服务接口.
 */
public interface IVmsNoticeService extends IService<Notice> {

    IPage<NoticeModel> getPageList(NoticePageQuery query);

    NoticeModel getNoticeById(Long id);

    Long createNotice(NoticeModel model);

    void updateNotice(NoticeModel model);

    void deleteNotice(Long id);

    List<NoticeModel> getTopList(int limit);

    void markRead(Long id);

    List<NoticeReadRecordModel> getReadRecords(Long id, String keyword);

    void archivePolicy(Long id, Integer archiveStatus);

    void batchUpdateExpireTime(List<Long> ids, Date expireTime);
}

