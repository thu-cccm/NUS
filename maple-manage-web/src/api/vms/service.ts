import request from '/@/utils/request';

export function useVmsServiceApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/service/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/service/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/service/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/service/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/service/${id}`,
				method: 'delete',
			});
		},
		export(data: object) {
			return request({
				url: '/manage/vms/service/export',
				method: 'get',
				params: data,
				responseType: 'blob',
			});
		},
	};
}

