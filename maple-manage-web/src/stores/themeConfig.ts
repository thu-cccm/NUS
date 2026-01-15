import { defineStore } from 'pinia';

export const useThemeConfig = defineStore('themeConfig', {
	state: (): ThemeConfigState => ({
		themeConfig: {

			isDrawer: false,

			primary: '#409eff',

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

			wartermarkText: '笑小枫',

			tagsStyle: 'tags-style-five',

			animation: 'slide-right',

			columnsAsideStyle: 'columns-round',

			columnsAsideLayout: 'columns-vertical',

			layout: 'defaults',

			isRequestRoutes: true,

			globalTitle: '笑小枫',

			globalViceTitle: '笑小枫',

			globalViceTitleMsg: '专注、免费、开源、维护、解疑',

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
