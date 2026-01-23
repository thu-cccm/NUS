import request from '/@/utils/request';

export function useVmsPartyActivityApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/partyActivity/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/partyActivity/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/partyActivity/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/partyActivity/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/partyActivity/${id}`,
				method: 'delete',
			});
		},
		export(data: object) {
			return request({
				url: '/manage/vms/partyActivity/export',
				method: 'get',
				params: data,
				responseType: 'blob',
			});
		},
	};
}

