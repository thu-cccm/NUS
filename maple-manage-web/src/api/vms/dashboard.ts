import request from '/@/utils/request';

export function useVmsDashboardApi() {
	return {
		getSummary() {
			return request({
				url: '/manage/vms/dashboard/summary',
				method: 'get',
			});
		},
	};
}

