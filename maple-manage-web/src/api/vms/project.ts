import request from '/@/utils/request';

export function useVmsProjectApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/project/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/project/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/project/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/project/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/project/${id}`,
				method: 'delete',
			});
		},
		export(data: object) {
			return request({
				url: '/manage/vms/project/export',
				method: 'get',
				params: data,
				responseType: 'blob',
			});
		},
	};
}

