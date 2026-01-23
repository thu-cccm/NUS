import request from '/@/utils/request';

export function useVmsResidentApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/resident/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/resident/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/resident/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/resident/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/resident/${id}`,
				method: 'delete',
			});
		},
		export(params: object) {
			return request({
				url: '/manage/vms/resident/export',
				method: 'get',
				params,
				responseType: 'blob',
			});
		},
		getByFamily(data: object) {
			return request({
				url: '/manage/vms/resident/getByFamily',
				method: 'post',
				data,
			});
		},
		migrateOut(id: number, migrateTo: string) {
			return request({
				url: '/manage/vms/resident/migrateOut',
				method: 'post',
				params: { id, migrateTo },
			});
		},
		migrateIn(id: number) {
			return request({
				url: '/manage/vms/resident/migrateIn',
				method: 'post',
				params: { id },
			});
		},
	};
}

