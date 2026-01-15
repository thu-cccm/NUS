import { defineStore } from 'pinia';
import { Session } from '/@/utils/storage';
import { useUserApi } from '/@/api/system/user';

const userApi = useUserApi();

export const useUserInfo = defineStore('userInfo', {
	state: (): UserInfosState => ({
		userInfos: {
			id: 0,
			userName: '',
			nickName: '',
			photo: '',
			time: 0,
			roles: [],
			authBtnList: [],
		},
	}),
	actions: {
		async setUserInfos() {

			if (Session.get('userInfo')) {
				this.userInfos = Session.get('userInfo');
			} else {
				const userInfos = <UserInfos>await this.getApiUserInfo();
				this.userInfos = userInfos;
			}
		},

		async getApiUserInfo() {
			return new Promise((resolve) => {
				setTimeout(() => {
					userApi.getUserInfo().then(res => {

						const userInfos = {
							id: res.userModel.id,
							nickName: res.userModel.nickName,
							userName: res.userModel.userName,
							photo: res.userModel.avatar,
							time: new Date().getTime(),
							roles: res.roles,
							authBtnList: res.permissions,
						};
						Session.set('userInfo', userInfos);
						resolve(userInfos);
					});
				}, 0);
			});
		},
	},
});
