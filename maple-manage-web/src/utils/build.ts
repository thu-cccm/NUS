import importToCDN from 'vite-plugin-cdn-import';

export const buildConfig = {
	cdn() {
		return importToCDN({
			prodUrl: 'https://unpkg.com/',
			modules: [

				{
					name: 'vue',
					var: 'Vue',
					path: 'dist/vue.global.js',
				},
				{
					name: 'vue-demi',
					var: 'VueDemi',
					path: 'lib/index.iife.js',
				},
				{
					name: 'vue-router',
					var: 'VueRouter',
					path: 'dist/vue-router.global.js',
				},
				{
					name: 'element-plus',
					var: 'ElementPlus',
					path: 'dist/index.full.js',
				},

			],
		});
	},
	external: [
		'vue',

		'vue-router',
		'element-plus',

	],
};
