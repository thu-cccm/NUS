import Cookies from 'js-cookie';

export const Local = {

	setKey(key: string) {

		return `${__NEXT_NAME__}:${key}`;
	},

	set<T>(key: string, val: T) {
		window.localStorage.setItem(Local.setKey(key), JSON.stringify(val));
	},

	get(key: string) {
		let json = <string>window.localStorage.getItem(Local.setKey(key));
		return JSON.parse(json);
	},

	remove(key: string) {
		window.localStorage.removeItem(Local.setKey(key));
	},

	clear() {
		window.localStorage.clear();
	},
};

export const Session = {

	set<T>(key: string, val: T) {
		if (key === 'token') return Cookies.set(key, val);
		window.sessionStorage.setItem(Local.setKey(key), JSON.stringify(val));
	},

	get(key: string) {
		if (key === 'token') return Cookies.get(key);
		let json = <string>window.sessionStorage.getItem(Local.setKey(key));
		return JSON.parse(json);
	},

	remove(key: string) {
		if (key === 'token') return Cookies.remove(key);
		window.sessionStorage.removeItem(Local.setKey(key));
	},

	clear() {
		Cookies.remove('token');
		window.sessionStorage.clear();
	},
};
