import request from '/@/utils/request';

export function useVmsApplyApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/apply/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/apply/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/apply/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/apply/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/apply/${id}`,
				method: 'delete',
			});
		},
		audit(data: object) {
			return request({
				url: '/manage/vms/apply/audit',
				method: 'post',
				data,
			});
		},
	};
}

