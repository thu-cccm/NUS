declare type MittType<T = any> = {
	openSetingsDrawer?: string;
	restoreDefault?: string;
	setSendColumnsChildren: T;
	setSendClassicChildren: T;
	getBreadcrumbIndexSetFilterRoutes?: string;
	layoutMobileResize: T;
	openOrCloseSortable?: string;
	openShareTagsView?: string;
	onTagsViewRefreshRouterView?: T;
	onCurrentContextmenuClick?: T;
};

declare type LayoutMobileResize = {
	layout: string;
	clientWidth: number;
};

declare type MittMenu = {
	children: RouteRecordRaw[];
	item?: RouteItem;
};
