import { defineStore } from 'pinia';

export const useThemeConfig = defineStore('themeConfig', {
	state: (): ThemeConfigState => ({
		themeConfig: {

			isDrawer: false,

			primary: '#4CAF50',

			isIsDark: false,

			topBar: '#ffffff',

			topBarColor: '#606266',

			isTopBarColorGradual: false,

			menuBar: '#304156',

			menuBarColor: '#eaeaea',

			menuBarActiveColor: 'rgba(0, 0, 0, 0.2)',

			isMenuBarColorGradual: false,

			columnsMenuBar: '#545c64',

			columnsMenuBarColor: '#e6e6e6',

			isColumnsMenuBarColorGradual: false,

			isColumnsMenuHoverPreload: false,

			isCollapse: false,

			isUniqueOpened: true,

			isFixedHeader: false,

			isFixedHeaderChange: false,

			isClassicSplitMenu: false,

			isLockScreen: false,

			lockScreenTime: 30,

			isShowLogo: true,

			isShowLogoChange: false,

			isBreadcrumb: true,

			isTagsview: true,

			isBreadcrumbIcon: false,

			isTagsviewIcon: false,

			isCacheTagsView: false,

			isSortableTagsView: true,

			isShareTagsView: false,

			isFooter: false,

			isGrayscale: false,

			isInvert: false,

			isWartermark: false,

			wartermarkText: '新农村建设信息管理系统',

			tagsStyle: 'tags-style-five',

			animation: 'slide-right',

			columnsAsideStyle: 'columns-round',

			columnsAsideLayout: 'columns-vertical',

			layout: 'defaults',

			isRequestRoutes: true,

			globalTitle: '新农村建设信息管理系统',

			globalViceTitle: '新农村数字化治理平台',

			globalViceTitleMsg: '乡村治理 · 数据赋能 · 服务便民',

			globalI18n: 'zh-cn',

			globalComponentSize: 'large',
		},
	}),
	actions: {
		setThemeConfig(data: ThemeConfigState) {
			this.themeConfig = data.themeConfig;
		},
	},
});
