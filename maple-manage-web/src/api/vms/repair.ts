import request from '/@/utils/request';

export function useVmsRepairApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/repair/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/repair/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/repair/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/repair/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/repair/${id}`,
				method: 'delete',
			});
		},
		assign(id: number, assignee: string) {
			return request({
				url: '/manage/vms/repair/assign',
				method: 'post',
				params: { id, assignee },
			});
		},
		complete(id: number, repairResult: string) {
			return request({
				url: '/manage/vms/repair/complete',
				method: 'post',
				params: { id, repairResult },
			});
		},
		export(data: object) {
			return request({
				url: '/manage/vms/repair/export',
				method: 'get',
				params: data,
				responseType: 'blob',
			});
		},
	};
}

