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
		resubmit(data: object) {
			return request({
				url: '/manage/vms/apply/resubmit',
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
		archive(id: number, archiveStatus: number) {
			return request({
				url: '/manage/vms/apply/archive',
				method: 'post',
				params: { id, archiveStatus },
			});
		},
		export(params: { status?: number; publicStatus?: number; archiveStatus?: number; applyType?: string }) {
			return request({
				url: '/manage/vms/apply/export',
				method: 'get',
				params,
				responseType: 'blob',
			});
		},
	};
}

