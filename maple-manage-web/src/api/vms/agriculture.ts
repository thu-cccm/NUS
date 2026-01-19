import request from '/@/utils/request';

export function useVmsAgricultureApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/agriculture/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/agriculture/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/agriculture/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/agriculture/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/agriculture/${id}`,
				method: 'delete',
			});
		},
	};
}

