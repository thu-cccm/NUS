import request from '/@/utils/request';

export function useVmsFinanceApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/finance/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/finance/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/finance/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/finance/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/finance/${id}`,
				method: 'delete',
			});
		},
		export(data: object) {
			return request({
				url: '/manage/vms/finance/export',
				method: 'get',
				params: data,
				responseType: 'blob',
			});
		},
	};
}

