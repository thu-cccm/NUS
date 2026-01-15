import { reactive, readonly } from "vue";
import { getUserInfo, logout } from "../api/login";
import { setToken, removeToken } from "../utils/auth";

import { defineStore } from "pinia";
import bootstrap from "bootstrap/dist/js/bootstrap.min.js";
export const useAppStore = defineStore("storeId", {
  state: () => ({
    bootstrap,
  }),
});

const state = reactive({ user: null, isLogin: false, loading: false });

export const loginUserStore = readonly(state);

export async function login(token) {
  setToken(token)
  state.loading = true;
  getUserInfo.then(res => {
    state.user = res;
    this.state.isLogin = true;
  });
  state.loading = false;
}

export async function loginOut() {
  state.loading = true;
  logout();  
  state.loading = false;
  state.user = null;
  this.state.isLogin = false;
  removeToken();
}
