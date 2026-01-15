import request from '/@/utils/request';

export function useLoginApi() {
	return {
		signIn: (data: object) => {
			return request({
				url: '/sso/login',
				method: 'post',
				data,
			});
		},
		signOut: () => {
			return request({
				url: '/sso/logout',
				method: 'get',
			});
		},
	};
}
