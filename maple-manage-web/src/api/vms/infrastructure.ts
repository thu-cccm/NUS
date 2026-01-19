import request from '/@/utils/request';

export function useVmsInfrastructureApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/infrastructure/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/infrastructure/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/infrastructure/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/infrastructure/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/infrastructure/${id}`,
				method: 'delete',
			});
		},
	};
}

