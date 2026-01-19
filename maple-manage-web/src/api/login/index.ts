import request from '/@/utils/request';

export function useLoginApi() {
	return {
		signIn: (data: object) => {
			return request({
				url: '/manage/sso/login',
				method: 'post',
				data,
			});
		},
		signOut: () => {
			return request({
				url: '/manage/sso/logout',
				method: 'get',
			});
		},
	};
}
