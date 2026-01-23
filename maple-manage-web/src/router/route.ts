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
                ],
            },
        ],
    },
];

export const notFoundAndNoPower = [
    {
        path: '/:path(.*)*',
        name: 'notFound',
        redirect: '/home',
        meta: {
            title: 'message.staticRoutes.notFound',
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
