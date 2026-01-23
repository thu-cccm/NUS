import request from '/@/utils/request';

export function useVmsAssetApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/asset/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/asset/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/asset/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/asset/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/asset/${id}`,
				method: 'delete',
			});
		},
		export(data: object) {
			return request({
				url: '/manage/vms/asset/export',
				method: 'get',
				params: data,
				responseType: 'blob',
			});
		},
	};
}

