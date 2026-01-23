import request from '/@/utils/request';

export function useVmsDisputeApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/dispute/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/dispute/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/dispute/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/dispute/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/dispute/${id}`,
				method: 'delete',
			});
		},
		export(data: object) {
			return request({
				url: '/manage/vms/dispute/export',
				method: 'get',
				params: data,
				responseType: 'blob',
			});
		},
	};
}

