import request from '/@/utils/request';

export function useVmsLandApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/land/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/land/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/land/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/land/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/land/${id}`,
				method: 'delete',
			});
		},
		export(params: object) {
			return request({
				url: '/manage/vms/land/export',
				method: 'get',
				params,
				responseType: 'blob',
			});
		},
	};
}

