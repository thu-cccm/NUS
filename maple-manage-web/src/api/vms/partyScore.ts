import request from '/@/utils/request';

export function useVmsPartyScoreApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/partyScore/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/partyScore/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/partyScore/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/partyScore/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/partyScore/${id}`,
				method: 'delete',
			});
		},
		getRanking(limit: number) {
			return request({
				url: '/manage/vms/partyScore/ranking',
				method: 'get',
				params: { limit },
			});
		},
		export(data: object) {
			return request({
				url: '/manage/vms/partyScore/export',
				method: 'get',
				params: data,
				responseType: 'blob',
			});
		},
	};
}

