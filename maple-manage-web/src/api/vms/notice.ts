import request from '/@/utils/request';

export function useVmsNoticeApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/notice/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/notice/${id}`,
				method: 'get',
			});
		},
		markRead(id: number) {
			return request({
				url: `/manage/vms/notice/read`,
				method: 'post',
				params: { id },
			});
		},
		getReadRecords(id: number, keyword?: string) {
			return request({
				url: `/manage/vms/notice/read/records`,
				method: 'get',
				params: { id, keyword },
			});
		},
		exportReadRecords(id: number, keyword?: string) {
			return request({
				url: `/manage/vms/notice/read/export`,
				method: 'get',
				params: { id, keyword },
				responseType: 'blob',
			});
		},
		archivePolicy(id: number, archiveStatus: number) {
			return request({
				url: `/manage/vms/notice/archive`,
				method: 'post',
				params: { id, archiveStatus },
			});
		},
		batchExpire(ids: number[], expireTime: number) {
			return request({
				url: `/manage/vms/notice/expire/batch`,
				method: 'post',
				params: { ids, expireTime },
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/notice/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/notice/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/notice/${id}`,
				method: 'delete',
			});
		},
		getTop(limit = 5) {
			return request({
				url: '/manage/vms/notice/top',
				method: 'get',
				params: { limit },
			});
		},
	};
}

