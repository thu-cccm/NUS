import request from '/@/utils/request';

export function useVmsVoteApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/vote/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/vote/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/vote/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/vote/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/vote/${id}`,
				method: 'delete',
			});
		},
		castVote(id: number, agree: boolean) {
			return request({
				url: '/manage/vms/vote/cast',
				method: 'post',
				params: { id, agree },
			});
		},
		endVote(id: number) {
			return request({
				url: '/manage/vms/vote/end',
				method: 'post',
				params: { id },
			});
		},
		getRecords(id: number, params?: { agree?: boolean; keyword?: string }) {
			return request({
				url: '/manage/vms/vote/records',
				method: 'get',
				params: { id, ...params },
			});
		},
		getTrend(id: number, days = 7, unit = 'day') {
			return request({
				url: '/manage/vms/vote/trend',
				method: 'get',
				params: { id, days, unit },
			});
		},
		getRecordPage(params: { id: number; current: number; size: number; agree?: boolean; keyword?: string }) {
			return request({
				url: '/manage/vms/vote/records/page',
				method: 'get',
				params,
			});
		},
		exportRecords(params: { id: number; agree?: boolean; keyword?: string }) {
			return request({
				url: '/manage/vms/vote/records/export',
				method: 'get',
				params,
				responseType: 'blob',
			});
		},
	};
}

