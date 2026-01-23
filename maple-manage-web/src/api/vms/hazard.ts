import request from '/@/utils/request';

export function useVmsHazardApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/hazard/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/hazard/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/hazard/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/hazard/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/hazard/${id}`,
				method: 'delete',
			});
		},
		export(data: object) {
			return request({
				url: '/manage/vms/hazard/export',
				method: 'get',
				params: data,
				responseType: 'blob',
			});
		},
	};
}

