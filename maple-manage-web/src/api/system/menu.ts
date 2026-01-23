import request from '/@/utils/request';

export function useMenuApi() {
	return {
		getRouters() {
			return request({
				url: '/manage/sso/getRouters',
				method: 'get',
			});
		},
		getTreeList(data: object) {
			return request({
				url: '/manage/system/menu/getTreeList',
				method: 'post',
				data,
			});
		},
	};
}

