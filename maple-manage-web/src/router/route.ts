import {RouteRecordRaw} from 'vue-router';

declare module 'vue-router' {
    interface RouteMeta {
        title?: string;
        isLink?: string;
        isHide?: boolean;
        isKeepAlive?: boolean;
        isAffix?: boolean;
        isIframe?: boolean;
        roles?: string[];
        icon?: string;
    }
}

export const dynamicRoutes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: '/',
        component: () => import('/@/layout/index.vue'),
        redirect: '/home',
        meta: {
            isKeepAlive: true,
        },
        children: [
            {
                path: '/home',
                name: 'home',
                component: () => import('/@/views/home/index.vue'),
                meta: {
                    title: 'message.router.home',
                    isLink: '',
                    isHide: false,
                    isKeepAlive: true,
                    isAffix: true,
                    isIframe: false,
                    roles: ['1', '2'],
                    icon: 'iconfont icon-shouye',
                },
            },
            {
                path: '/system',
                name: 'system',
                component: () => import('/@/layout/routerView/parent.vue'),
                redirect: '/system/menu',
                meta: {
                    title: 'message.router.system',
                    isLink: '',
                    isHide: false,
                    isKeepAlive: true,
                    isAffix: false,
                    isIframe: false,
                    roles: ['1'],
                    icon: 'iconfont icon-xitongshezhi',
                },
                children: [
                    {
                        path: '/system/menu',
                        name: 'systemMenu',
                        component: () => import('/@/views/system/menu/index.vue'),
                        meta: {
                            title: 'message.router.systemMenu',
                            isLink: '',
                            isHide: false,
                            isKeepAlive: true,
                            isAffix: false,
                            isIframe: false,
                            roles: ['1'],
                            icon: 'iconfont icon-caidan',
                        },
                    },
                    {
                        path: '/system/role',
                        name: 'systemRole',
                        component: () => import('/@/views/system/role/index.vue'),
                        meta: {
                            title: 'message.router.systemRole',
                            isLink: '',
                            isHide: false,
                            isKeepAlive: true,
                            isAffix: false,
                            isIframe: false,
                            roles: ['1'],
                            icon: 'ele-ColdDrink',
                        },
                    },
                    {
                        path: '/system/user',
                        name: 'systemUser',
                        component: () => import('/@/views/system/user/index.vue'),
                        meta: {
                            title: 'message.router.systemUser',
                            isLink: '',
                            isHide: false,
                            isKeepAlive: true,
                            isAffix: false,
                            isIframe: false,
                            roles: ['1'],
                            icon: 'iconfont icon-icon-',
                        },
                    },
                    {
                        path: '/system/dept',
                        name: 'systemDept',
                        component: () => import('/@/views/system/dept/index.vue'),
                        meta: {
                            title: 'message.router.systemDept',
                            isLink: '',
                            isHide: false,
                            isKeepAlive: true,
                            isAffix: false,
                            isIframe: false,
                            roles: ['1'],
                            icon: 'ele-OfficeBuilding',
                        },
                    },
                    {
                        path: '/system/dic',
                        name: 'systemDic',
                        component: () => import('/@/views/system/dic/index.vue'),
                        meta: {
                            title: 'message.router.systemDic',
                            isLink: '',
                            isHide: false,
                            isKeepAlive: true,
                            isAffix: false,
                            isIframe: false,
                            roles: ['1'],
                            icon: 'ele-SetUp',
                        },
                    },
                ],
            },
            {
                path: '/tool',
                name: 'tool',
                component: () => import('/@/layout/routerView/parent.vue'),
                redirect: '/tool/gen',
                meta: {
                    title: 'message.router.tool',
                    isLink: '',
                    isHide: false,
                    isKeepAlive: true,
                    isAffix: false,
                    isIframe: false,
                    roles: ['1'],
                    icon: 'iconfont icon-xitongshezhi',
                },
                children: [
                    {
                        path: '/tool/gen',
                        name: 'toolGen',
                        component: () => import('/@/views/tool/gen/index.vue'),
                        meta: {
                            title: 'message.router.toolGen',
                            isLink: '',
                            isHide: false,
                            isKeepAlive: true,
                            isAffix: false,
                            isIframe: false,
                            roles: ['1'],
                            icon: 'iconfont icon-caidan',
                        },
                    },
                ],
            },

        ],
    },
];

export const notFoundAndNoPower = [
    {
        path: '/:path(.*)*',
        name: 'notFound',
        component: () => import('/@/views/error/404.vue'),
        meta: {
            title: 'message.staticRoutes.notFound',
            isHide: true,
        },
    },
    {
        path: '/401',
        name: 'noPower',
        component: () => import('/@/views/error/401.vue'),
        meta: {
            title: 'message.staticRoutes.noPower',
            isHide: true,
        },
    },
];

export const staticRoutes: Array<RouteRecordRaw> = [
    {
        path: '/login',
        name: 'login',
        component: () => import('/@/views/login/index.vue'),
        meta: {
            title: '登录',
        },
    },
];
