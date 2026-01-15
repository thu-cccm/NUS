declare module 'vue-grid-layout';
declare module 'qrcodejs2-fixes';
declare module 'splitpanes';
declare module 'js-cookie';
declare module '@wangeditor/editor-for-vue';
declare module 'js-table2excel';
declare module 'qs';
declare module 'sortablejs';

declare module '*.json';
declare module '*.png';
declare module '*.jpg';
declare module '*.scss';
declare module '*.ts';
declare module '*.js';

declare module '*.vue' {
	import type { DefineComponent } from 'vue';
	const component: DefineComponent<{}, {}, any>;
	export default component;
}

declare interface Window {
	nextLoading: boolean;
	BMAP_SATELLITE_MAP: any;
	BMap: any;
}

declare type RouteItem<T = any> = {
	path: string;
	name?: string | symbol | undefined | null;
	redirect?: string;
	k?: T;
	meta?: {
		title?: string;
		isLink?: string;
		isHide?: boolean;
		isKeepAlive?: boolean;
		isAffix?: boolean;
		isIframe?: boolean;
		roles?: string[];
		icon?: string;
		isDynamic?: boolean;
		isDynamicPath?: string;
		isIframeOpen?: string;
		loading?: boolean;
	};
	children: T[];
	query?: { [key: string]: T };
	params?: { [key: string]: T };
	contextMenuClickId?: string | number;
	commonUrl?: string;
	isFnClick?: boolean;
	url?: string;
	transUrl?: string;
	title?: string;
	id?: string | number;
};

declare interface RouteToFrom<T = any> extends RouteItem {
	path?: string;
	children?: T[];
}

declare type RouteItems<T extends RouteItem = any> = T[];

declare type RefType<T = any> = T | null;

declare type HtmlType = HTMLElement | string | undefined | null;

declare type ChilType<T = any> = {
	children?: T[];
};

declare type EmptyArrayType<T = any> = T[];

declare type EmptyObjectType<T = any> = {
	[key: string]: T;
};

declare type SelectOptionType = {
	value: string | number;
	label: string | number;
};

declare interface WheelEventType extends WheelEvent {
	wheelDelta: number;
}

declare interface TableType<T = any> {
	total: number;
	loading: boolean;
	param: {
		page: {
			current: number;
			size: number;
		};
		query: {
			[key: any]: T;
		};
	};
}
