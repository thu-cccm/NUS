import request from '/@/utils/request';

export function useVmsFeedbackApi() {
	return {
		getPageList(data: object) {
			return request({
				url: '/manage/vms/feedback/getPageList',
				method: 'post',
				data,
			});
		},
		create(data: object) {
			return request({
				url: '/manage/vms/feedback/create',
				method: 'post',
				data,
			});
		},
		reply(id: number, reply: string) {
			return request({
				url: '/manage/vms/feedback/reply',
				method: 'post',
				params: { id, reply },
			});
		},
		report(id: number) {
			return request({
				url: '/manage/vms/feedback/report',
				method: 'post',
				params: { id },
			});
		},
		hide(id: number, hidden: boolean) {
			return request({
				url: '/manage/vms/feedback/hide',
				method: 'post',
				params: { id, hidden },
			});
		},
		delete(id: number) {
			return request({
				url: `/manage/vms/feedback/${id}`,
				method: 'delete',
			});
		},
		export(params: { content?: string; isPublic?: number; hasReply?: boolean; isHidden?: number }) {
			return request({
				url: '/manage/vms/feedback/export',
				method: 'get',
				params,
				responseType: 'blob',
			});
		},
	};
}

