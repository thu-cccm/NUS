CREATE TABLE IF NOT EXISTS `sys_config` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
    `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
    `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
    `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
    `config_type` tinyint(1) DEFAULT '0' COMMENT '是否内置',
    `belong_dept_id` bigint(20) DEFAULT NULL COMMENT '归属部门id',
    `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统管理-参数配置表';

INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `belong_dept_id`, `create_id`, `create_time`, `update_id`, `update_time`, `remark`) VALUES
(1, '系统门户', 'portal_url', 'http://localhost:8080', 0, NULL, 1, '2024-04-29 10:54:42', 1, '2024-04-29 11:00:00', '新农村建设信息管理系统门户地址');

CREATE TABLE IF NOT EXISTS `sys_dict_data` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
    `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
    `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
    `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
    `dict_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '字典类型',
    `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
    `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
    `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认（1是 0否）',
    `status` tinyint(4) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统管理-字典数据表';

INSERT INTO `sys_dict_data` (`id`, `dict_sort`, `dict_label`, `dict_value`, `dict_code`, `css_class`, `list_class`, `is_default`, `status`, `create_id`, `create_time`, `update_id`, `update_time`, `remark`) VALUES
(1, 1, '男', '0', 'sys_user_sex', '', 'primary', 1, 1, 1, '2024-02-21 16:56:12', 1, '2024-02-21 16:56:12', '性别男'),
(2, 2, '女', '1', 'sys_user_sex', '', 'primary', 0, 1, 1, '2024-02-21 16:56:12', 1, '2024-02-21 16:56:12', '性别女'),
(3, 3, '未知', '2', 'sys_user_sex', '', 'info', 0, 1, 1, '2024-02-21 16:56:12', 1, '2024-02-21 16:56:12', '性别未知'),
(4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 1, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '显示菜单'),
(5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '隐藏菜单'),
(6, 1, '正常', '1', 'sys_normal_disable', '', 'primary', 1, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '正常状态'),
(7, 2, '停用', '0', 'sys_normal_disable', '', 'danger', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '停用状态'),
(8, 1, '启用', '1', 'sys_status', '', 'primary', 1, 1, 1, '2024-04-11 11:09:41', 1, '2024-04-11 11:09:41', ''),
(9, 2, '禁用', '0', 'sys_status', '', 'info', 0, 1, 1, '2024-04-11 11:09:41', 1, '2024-04-11 11:09:41', ''),
(10, 1, '新增', '1', 'sys_oper_type', '', 'info', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '新增操作'),
(11, 2, '修改', '2', 'sys_oper_type', '', 'info', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '修改操作'),
(12, 3, '删除', '3', 'sys_oper_type', '', 'danger', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '删除操作'),
(13, 4, '授权', '4', 'sys_oper_type', '', 'primary', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '授权操作'),
(14, 5, '导出', '5', 'sys_oper_type', '', 'warning', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '导出操作'),
(15, 6, '导入', '6', 'sys_oper_type', '', 'warning', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '导入操作'),
(16, 7, '强退', '7', 'sys_oper_type', '', 'danger', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '强退操作'),
(17, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '生成操作'),
(18, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '清空操作'),
(19, 0, '审核中', '0', 'approve_status', '', 'info', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '正常状态'),
(20, 1, '通过', '1', 'approve_status', '', 'primary', 0, 1, NULL, '2021-05-17 17:52:08', NULL, NULL, '停用状态'),
(21, 2, '拒绝', '2', 'approve_status', NULL, 'danger', 0, 1, NULL, '2024-02-26 13:39:48', NULL, NULL, NULL),
(22, 3, '删除', '3', 'approve_status', NULL, 'info', 0, 1, NULL, '2024-02-26 13:39:48', NULL, NULL, NULL),
(23, 1, '系统用户', '1', 'system_user_type', '', 'primary', 0, 1, 1, '2024-03-25 13:30:40', 1, '2024-03-25 13:30:40', ''),
(24, 2, '小程序用户', '2', 'system_user_type', '', 'success', 0, 1, 1, '2024-03-25 13:30:40', 1, '2024-03-25 13:30:40', ''),
(25, 1, '目录', 'M', 'menu_type', '', 'success', 0, 1, 1, '2024-03-27 14:24:51', 1, '2024-03-27 14:24:51', ''),
(26, 2, '菜单', 'C', 'menu_type', '', 'warning', 0, 1, 1, '2024-03-27 14:24:51', 1, '2024-03-27 14:24:51', ''),
(27, 3, '按钮', 'F', 'menu_type', '', 'danger', 0, 1, 1, '2024-03-27 14:24:51', 1, '2024-03-27 14:24:51', ''),
(28, 1, '全部数据权限', '1', 'sys_role_data_scope', '', 'info', 0, 1, 1, '2024-05-06 14:10:04', 1, '2024-05-06 14:10:04', ''),
(29, 2, '自定义数据权限', '2', 'sys_role_data_scope', '', 'info', 0, 1, 1, '2024-05-06 14:10:04', 1, '2024-05-06 14:10:04', ''),
(30, 3, '本部门数据权限', '3', 'sys_role_data_scope', '', 'info', 0, 1, 1, '2024-05-06 14:10:04', 1, '2024-05-06 14:10:04', ''),
(31, 4, '本部门及以下数据权限', '4', 'sys_role_data_scope', '', 'info', 0, 1, 1, '2024-05-06 14:10:04', 1, '2024-05-06 14:10:04', ''),
(32, 5, '本人创建的数据权限', '5', 'sys_role_data_scope', '', 'info', 0, 1, 1, '2024-05-06 14:10:04', 1, '2024-05-06 14:10:04', ''),
(33, 1, '补贴政策', '补贴政策', 'vms_policy_category', '', 'info', 1, 1, 1, '2026-01-22 10:00:00', 1, '2026-01-22 10:00:00', ''),
(34, 2, '建设资金', '建设资金', 'vms_policy_category', '', 'info', 0, 1, 1, '2026-01-22 10:00:00', 1, '2026-01-22 10:00:00', ''),
(35, 3, '教育医疗', '教育医疗', 'vms_policy_category', '', 'info', 0, 1, 1, '2026-01-22 10:00:00', 1, '2026-01-22 10:00:00', ''),
(36, 4, '环境治理', '环境治理', 'vms_policy_category', '', 'info', 0, 1, 1, '2026-01-22 10:00:00', 1, '2026-01-22 10:00:00', ''),
(37, 5, '产业扶持', '产业扶持', 'vms_policy_category', '', 'info', 0, 1, 1, '2026-01-22 10:00:00', 1, '2026-01-22 10:00:00', ''),
(38, 6, '其他', '其他', 'vms_policy_category', '', 'info', 0, 1, 1, '2026-01-22 10:00:00', 1, '2026-01-22 10:00:00', '');

CREATE TABLE IF NOT EXISTS `sys_dict_type` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
    `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
    `dict_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '字典类型',
    `status` tinyint(4) DEFAULT '1' COMMENT '状态（1正常 0停用）',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `dict_type` (`dict_code`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统管理-字典类型表';

INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_code`, `status`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`) VALUES
(1, '性别', 'sys_user_sex', 1, '用户性别列表', NULL, '2021-05-17 17:52:08', 1, '2024-02-21 16:56:12'),
(2, '菜单状态', 'sys_show_hide', 1, '菜单状态列表', NULL, '2021-05-17 17:52:08', NULL, NULL),
(3, '系统开关', 'sys_normal_disable', 1, '系统开关列表', NULL, '2021-05-17 17:52:08', NULL, NULL),
(4, '系统状态', 'sys_status', 1, '系统状态列表', NULL, '2021-05-17 17:52:08', 1, '2024-04-11 11:09:41'),
(5, '操作类型', 'sys_oper_type', 1, '操作类型列表', NULL, '2021-05-17 17:52:08', NULL, NULL),
(6, '审核状态', 'approve_status', 1, '审核状态列表', NULL, '2021-05-17 17:52:08', NULL, NULL),
(7, '系统用户类型', 'system_user_type', 1, '系统用户类型，后台的为系统用户，只可登录管理系统；web端的为小程序用户，必须有openId', 1, '2024-03-25 13:29:55', 1, '2024-03-25 13:30:40'),
(8, '菜单类型', 'menu_type', 1, '菜单类型（M目录 C菜单 F按钮）', 1, '2024-03-27 14:24:51', 1, '2024-03-27 14:24:51'),
(9, '数据权限', 'sys_role_data_scope', 1, '1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：本人创建的数据权限', 1, '2024-05-06 14:10:04', 1, '2024-05-06 14:10:04'),
(10, '政策分类', 'vms_policy_category', 1, '新农村政策库分类', 1, '2026-01-22 10:00:00', 1, '2026-01-22 10:00:00');

CREATE TABLE IF NOT EXISTS `sys_notice` (
    `id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
    `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
    `notice_content` text COMMENT '公告内容',
    `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
    `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
    `create_name` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
    `update_name` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统管理-通知公告表';

CREATE TABLE IF NOT EXISTS `sys_operate_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
    `title` varchar(50) DEFAULT '' COMMENT '模块标题',
    `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0查询 1新增 2修改 3删除 4其他）',
    `method` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '方法名称',
    `resp_time` bigint(20) DEFAULT NULL COMMENT '响应时间',
    `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
    `browser` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '浏览器类型',
    `operate_type` int(1) DEFAULT '0' COMMENT '操作类别（0网站用户 1后台用户 2小程序 3其他）',
    `operate_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '请求URL',
    `operate_ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '主机地址',
    `operate_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '操作地点',
    `operate_param` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '请求参数',
    `json_result` text COMMENT '返回参数',
    `status` tinyint(4) DEFAULT NULL COMMENT '操作状态（0正常 1异常）',
    `error_msg` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '错误消息',
    `create_id` bigint(20) DEFAULT NULL COMMENT '操作人id',
    `create_time` datetime DEFAULT NULL COMMENT '操作时间',
    `update_id` bigint(20) DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统管理-操作日志记录';

CREATE TABLE IF NOT EXISTS `usc_dept` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
    `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
    `ancestors` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '祖级列表',
    `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
    `sort_num` bigint(20) DEFAULT NULL COMMENT '显示顺序',
    `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
    `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
    `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
    `status` tinyint(4) DEFAULT NULL COMMENT '部门状态（0正常 1停用）',
    `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户中心-部门表';

INSERT INTO `usc_dept` (`id`, `parent_id`, `ancestors`, `dept_name`, `sort_num`, `leader`, `phone`, `email`, `status`, `create_id`, `create_time`, `update_id`, `update_time`, `is_delete`) VALUES
(1, 0, '', '新农村信息中心', 0, '李主任', '18300000000', 'service@village.local', 1, NULL, '2024-03-25 16:47:02', NULL, '2024-03-25 16:47:02', 0),
(2, 1, '1', '村委会', 1, '张书记', '18888888888', NULL, 1, 1, '2024-04-11 11:06:16', 1, '2024-04-11 11:07:10', 0);

CREATE TABLE IF NOT EXISTS `usc_menu` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '路由名称',
    `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
    `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
    `ancestors` varchar(255) DEFAULT NULL COMMENT '祖级菜单',
    `sort_num` bigint(20) DEFAULT NULL COMMENT '显示顺序',
    `path` varchar(200) DEFAULT '' COMMENT '路由地址',
    `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
    `redirect` varchar(255) DEFAULT NULL COMMENT '重定向地址',
    `link_url` varchar(255) DEFAULT NULL COMMENT '链接地址',
    `is_iframe` tinyint(1) DEFAULT '0' COMMENT '是否内嵌窗口',
    `is_link` tinyint(1) DEFAULT '0' COMMENT '是否外链',
    `is_keep_alive` tinyint(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
    `is_hide` tinyint(1) DEFAULT '0' COMMENT '是否隐藏',
    `is_affix` tinyint(1) DEFAULT '0' COMMENT '是否固定',
    `status` tinyint(4) DEFAULT NULL COMMENT '菜单状态（0正常 1停用）',
    `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
    `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
    `remark` varchar(500) DEFAULT '' COMMENT '备注',
    `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户中心-菜单权限表';

INSERT INTO `usc_menu` (`id`, `name`, `title`, `menu_type`, `parent_id`, `ancestors`, `sort_num`, `path`, `component`, `redirect`, `link_url`, `is_iframe`, `is_link`, `is_keep_alive`, `is_hide`, `is_affix`, `status`, `perms`, `icon`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`) VALUES
(1, 'home', '首页', 'C', 0, '', 1, '/home', 'home/index', '/home', '', 0, 0, 1, 0, 1, 1, '', 'iconfont icon-shouye', '', 1, '2024-03-28 11:09:37', 1, '2024-03-28 13:04:26'),
(2, 'system', '基础设置', 'M', 0, '', 3, '/system', 'system/user/index', '/system/user', '', 0, 0, 1, 0, 0, 1, '', 'iconfont icon-xitongshezhi', '', 1, '2024-03-28 13:06:21', 1, '2024-04-29 09:44:53'),
(3, 'systemUser', '用户管理', 'C', 2, '2', 1, '/system/user', 'system/user/index', NULL, '', 0, 0, 1, 0, 0, 1, '', 'iconfont icon-icon-', '', 1, '2024-03-28 13:19:11', 1, '2024-03-28 13:21:01'),
(4, 'systemRole', '角色管理', 'C', 2, '2', 2, '/system/role', 'system/role/index', NULL, '', 0, 0, 1, 0, 0, 1, '', 'iconfont icon-shuxingtu', '', 1, '2024-03-28 15:11:42', 1, '2024-03-28 15:13:00'),
(6, 'systemDept', '部门管理', 'C', 2, '2', 4, '/system/dept', 'system/dept/index', NULL, '', 0, 0, 1, 0, 0, 1, '', 'ele-OfficeBuilding', '', 1, '2024-03-28 15:11:42', 1, '2024-03-28 15:13:00'),
(11, '', '修改', 'F', 3, '2,3', 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 'system:user:update', '', '', 1, '2024-04-29 14:25:41', 1, '2024-04-29 14:25:41'),
(12, 'vms', '新农村治理', 'M', 0, '', 2, '/vms', 'vms/resident/index', '/vms/resident', '', 0, 0, 1, 0, 0, 1, '', 'iconfont icon-shouye', '', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00'),
(13, 'vmsResident', '人口档案', 'C', 12, '12', 1, '/vms/resident', 'vms/resident/index', NULL, '', 0, 0, 1, 0, 0, 1, '', 'ele-User', '', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00'),
(14, 'vmsLand', '土地资源', 'C', 12, '12', 2, '/vms/land', 'vms/land/index', NULL, '', 0, 0, 1, 0, 0, 1, '', 'ele-MapLocation', '', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00'),
(15, 'vmsAgriculture', '农业生产', 'C', 12, '12', 3, '/vms/agriculture', 'vms/agriculture/index', NULL, '', 0, 0, 1, 0, 0, 1, '', 'ele-Food', '', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00'),
(16, 'vmsInfrastructure', '基础设施', 'C', 12, '12', 4, '/vms/infrastructure', 'vms/infrastructure/index', NULL, '', 0, 0, 1, 0, 0, 1, '', 'ele-OfficeBuilding', '', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00'),
(17, 'vmsApply', '事务申请', 'C', 12, '12', 5, '/vms/apply', 'vms/apply/index', NULL, '', 0, 0, 1, 0, 0, 1, '', 'ele-Document', '', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00'),
(18, 'vmsVote', '民主互动', 'C', 12, '12', 6, '/vms/vote', 'vms/vote/index', NULL, '', 0, 0, 1, 0, 0, 1, '', 'ele-ChatLineRound', '', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00');

CREATE TABLE IF NOT EXISTS `usc_role` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name` varchar(30) NOT NULL COMMENT '角色名称',
    `role_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
    `sort_num` bigint(20) NOT NULL DEFAULT '0' COMMENT '显示顺序',
    `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
    `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户中心-角色信息表';

INSERT INTO `usc_role` (`id`, `role_name`, `role_key`, `sort_num`, `data_scope`, `menu_check_strictly`, `dept_check_strictly`, `status`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `is_delete`) VALUES
(1, '系统管理员', 'admin', 1, '1', 1, 1, 1, '系统管理员', NULL, '2021-05-17 14:03:57', 1, '2025-01-01 00:00:00', 0),
(2, '村民', 'villager', 2, '5', 1, 1, 1, '村民用户', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 0);

CREATE TABLE IF NOT EXISTS `usc_role_dept` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户中心-角色和部门关联表';

CREATE TABLE IF NOT EXISTS `usc_role_menu` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户中心-角色和菜单关联表';

INSERT INTO `usc_role_menu` (`id`, `role_id`, `menu_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 1, 5),
(6, 1, 6),
(7, 1, 7),
(8, 1, 8),
(9, 1, 9),
(10, 1, 10),
(11, 1, 11),
(12, 1, 12),
(13, 1, 13),
(14, 1, 14),
(15, 1, 15),
(16, 1, 16),
(17, 1, 17),
(18, 1, 18),
(19, 2, 1),
(20, 2, 12),
(21, 2, 13),
(22, 2, 14),
(23, 2, 15),
(24, 2, 16),
(25, 2, 17),
(26, 2, 18);

CREATE TABLE IF NOT EXISTS `usc_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '小程序的openId',
    `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
    `account` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户账号',
    `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户姓名',
    `nick_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户昵称',
    `user_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '00' COMMENT '用户类型（00系统用户,01小程序用户）',
    `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
    `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '手机号码',
    `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
    `salt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户加密盐值',
    `password` varchar(100) DEFAULT '' COMMENT '密码',
    `status` tinyint(4) DEFAULT NULL COMMENT '帐号状态（0正常 1停用）',
    `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
    `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
    `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除标志',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户中心-用户信息表';

INSERT INTO `usc_user` (`id`, `open_id`, `dept_id`, `account`, `user_name`, `nick_name`, `user_type`, `email`, `phone`, `sex`, `avatar`, `salt`, `password`, `status`, `login_ip`, `login_date`, `create_id`, `create_time`, `update_id`, `update_time`, `is_delete`, `remark`) VALUES
(1, NULL, 2, 'admin', '管理员', '系统管理员', '1', 'admin@village.local', '18300000000', '0', '', '439495', '27c3b87192fdaf6c54a0c3de1f339f1d', 1, '127.0.0.1', '2020-10-22 00:00:00', 1, '2020-10-22 14:27:04', 1, '2025-01-01 00:00:00', 0, '系统管理员'),
(2, NULL, 2, 'user001', '村民用户', '村民用户', '1', '', '18100000001', '0', '', '439495', '27c3b87192fdaf6c54a0c3de1f339f1d', 1, '', NULL, 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 0, '测试村民账号');

CREATE TABLE IF NOT EXISTS `usc_user_role` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNI_USER_ROLE` (`user_id`,`role_id`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户中心-用户和角色关联表';

INSERT INTO `usc_user_role` (`id`, `user_id`, `role_id`) VALUES
(1, 1, 1),
(2, 2, 2);
