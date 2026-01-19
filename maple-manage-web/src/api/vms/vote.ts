import request from '/@/utils/request';

export function useVmsVoteApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/vote/getPageList',
				method: 'post',
				data,
			});
		},
		getById(id: number) {
			return request({
				url: `/manage/vms/vote/${id}`,
				method: 'get',
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/vote/create',
				method: 'post',
				data,
			});
		},
		update(data: object) {
			return request({
				url: '/manage/vms/vote/update',
				method: 'post',
				data,
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/vote/${id}`,
				method: 'delete',
			});
		},
		castVote(id: number, agree: boolean) {
			return request({
				url: '/manage/vms/vote/cast',
				method: 'post',
				params: { id, agree },
			});
		},
		endVote(id: number) {
			return request({
				url: '/manage/vms/vote/end',
				method: 'post',
				params: { id },
			});
		},
	};
}

