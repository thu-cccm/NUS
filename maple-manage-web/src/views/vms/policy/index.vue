<template>
	<div class="vms-policy layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<el-tabs v-model="state.archiveTab" @tab-change="onArchiveTabChange" class="mb10">
				<el-tab-pane label="全部" name="all" />
				<el-tab-pane label="未归档" name="active" />
				<el-tab-pane label="已归档" name="archived" />
			</el-tabs>
			<div class="vms-policy-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="关键词">
						<el-input v-model="state.tableData.param.query.title" placeholder="标题/摘要关键词" clearable />
					</el-form-item>
					<el-form-item label="分类">
						<el-select v-model="state.tableData.param.query.policyCategory" placeholder="请选择分类" clearable>
							<el-option v-for="item in policyCategoryOptions" :key="item.value" :label="item.label" :value="item.value" />
						</el-select>
					</el-form-item>
					<el-form-item label="归档状态">
						<el-select v-model="state.tableData.param.query.archiveStatus" placeholder="请选择" clearable>
							<el-option label="未归档" :value="0" />
							<el-option label="已归档" :value="1" />
						</el-select>
					</el-form-item>
					<el-form-item label="有效状态">
						<el-select v-model="state.tableData.param.query.expired" placeholder="请选择" clearable>
							<el-option label="未过期" :value="0" />
							<el-option label="已过期" :value="1" />
						</el-select>
					</el-form-item>
					<el-form-item label="推送对象">
						<el-select v-model="state.tableData.param.query.targetGroup" placeholder="请选择对象" clearable>
							<el-option label="全员" value="all" />
							<el-option label="党员" value="party" />
							<el-option label="贫困户" value="poor" />
						</el-select>
					</el-form-item>
					<el-form-item v-if="isAdmin" label="状态">
						<el-select v-model="state.tableData.param.query.status" placeholder="请选择状态" clearable>
							<el-option label="草稿" :value="0" />
							<el-option label="发布" :value="1" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>

			<div class="vms-policy-actions">
				<el-button v-if="isAdmin" type="primary" @click="openDialog('add')">新增政策</el-button>
				<el-button v-if="isAdmin" @click="goDictManage">分类管理</el-button>
				<el-button v-if="isAdmin" type="warning" @click="batchArchive(1)">批量归档</el-button>
				<el-button v-if="isAdmin" @click="batchArchive(0)">批量恢复</el-button>
				<el-button v-if="isAdmin" type="primary" @click="openBatchExpire">批量设置过期时间</el-button>
			</div>

			<div class="vms-policy-filters">
				<el-tag
					:type="state.activeCategory === '' ? 'primary' : 'info'"
					class="mr5"
					@click="setActiveCategory('')"
				>
					全部
				</el-tag>
				<el-tag
					v-for="item in policyCategoryOptions"
					:key="item.value"
					:type="state.activeCategory === item.value ? 'primary' : 'info'"
					class="mr5"
					@click="setActiveCategory(item.value)"
				>
					{{ item.label }}
				</el-tag>
			</div>

			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" style="width: 100%" @selection-change="onSelectionChange">
				<el-table-column v-if="isAdmin" type="selection" width="50" />
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="title" label="政策标题" min-width="200" show-overflow-tooltip />
				<el-table-column prop="policyCategory" label="分类" width="120">
					<template #default="scope">
						<el-tag v-if="scope.row.policyCategory" type="info">{{ scope.row.policyCategory }}</el-tag>
						<span v-else>-</span>
					</template>
				</el-table-column>
				<el-table-column label="有效状态" width="110">
					<template #default="scope">
						<el-tag :type="scope.row.expired ? 'danger' : 'success'">
							{{ scope.row.expired ? '已过期' : '未过期' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="归档状态" width="110">
					<template #default="scope">
						<el-tag :type="scope.row.archiveStatus === 1 ? 'info' : 'success'">
							{{ scope.row.archiveStatus === 1 ? '已归档' : '未归档' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="targetGroup" label="推送对象" min-width="140">
					<template #default="scope">
						<el-tag v-for="group in parseTargetGroups(scope.row.targetGroup)" :key="group" :type="groupTagType(group)" class="mr5">
							{{ targetGroupLabel(group) }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="policyFile" label="政策附件" width="120">
					<template #default="scope">
						<el-button
							v-if="scope.row.policyFile"
							size="small"
							text
							type="primary"
							@click="previewPolicy(scope.row.policyFile)"
						>
							预览
						</el-button>
						<span v-else>-</span>
					</template>
				</el-table-column>
				<el-table-column v-if="isAdmin" prop="status" label="状态" width="100">
					<template #default="scope">
						<el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
							{{ scope.row.status === 1 ? '发布' : '草稿' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="发布时间" width="180">
					<template #default="scope">
						<span>{{ parseDateTime(scope.row.createTime) }}</span>
					</template>
				</el-table-column>
				<el-table-column v-if="isAdmin" label="阅读进度" min-width="180">
					<template #default="scope">
						<div class="read-progress">
							<el-progress
								:percentage="readPercent(scope.row)"
								:status="scope.row.readCount >= scope.row.targetCount ? 'success' : 'primary'"
								:stroke-width="10"
							/>
							<span class="read-count">{{ scope.row.readCount || 0 }}/{{ scope.row.targetCount || 0 }}</span>
						</div>
					</template>
				</el-table-column>
				<el-table-column v-else label="阅读状态" width="120">
					<template #default="scope">
						<el-tag :type="scope.row.readStatus ? 'success' : 'info'">
							{{ scope.row.readStatus ? '已读' : '未读' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="260">
					<template #default="scope">
						<el-button size="small" text type="primary" @click="openDetail(scope.row)">详情</el-button>
						<el-button v-if="isAdmin" size="small" text type="primary" @click="openReadDialog(scope.row)">阅读明细</el-button>
						<el-button v-if="isAdmin" size="small" text type="primary" @click="openDialog('edit', scope.row)">编辑</el-button>
						<el-button v-if="isAdmin" size="small" text type="warning" @click="toggleArchive(scope.row)">
							{{ scope.row.archiveStatus === 1 ? '恢复' : '归档' }}
						</el-button>
						<el-button v-if="isAdmin" size="small" text type="danger" @click="onRowDel(scope.row)">删除</el-button>
					</template>
				</el-table-column>
				<template #empty>
					<el-empty description="暂无相关记录" />
				</template>
			</el-table>

			<el-pagination
				@size-change="onHandleSizeChange"
				@current-change="onHandleCurrentChange"
				class="mt15"
				:pager-count="5"
				:page-sizes="[10, 20, 30]"
				v-model:current-page="state.tableData.param.page.current"
				background
				v-model:page-size="state.tableData.param.page.size"
				layout="total, sizes, prev, pager, next, jumper"
				:total="state.tableData.total"
			/>
		</el-card>

		<el-dialog v-model="state.dialogVisible" :title="state.dialogTitle" width="720px">
			<el-form :model="state.form" label-width="90px">
				<el-form-item label="政策标题">
					<el-input v-model="state.form.title" placeholder="请输入政策标题" />
				</el-form-item>
				<el-form-item label="政策分类">
					<el-select v-model="state.form.policyCategory" placeholder="请选择分类">
						<el-option v-for="item in policyCategoryOptions" :key="item.value" :label="item.label" :value="item.value" />
					</el-select>
				</el-form-item>
				<el-form-item label="过期时间">
					<el-date-picker v-model="state.form.expireTime" type="datetime" placeholder="请选择过期时间" />
				</el-form-item>
				<el-form-item label="推送对象">
					<el-select v-model="state.form.targetGroup" multiple collapse-tags placeholder="请选择对象" @change="onTargetGroupChange">
						<el-option label="全员" value="all" />
						<el-option label="党员" value="party" />
						<el-option label="贫困户" value="poor" />
					</el-select>
				</el-form-item>
				<el-form-item label="置顶">
					<el-switch v-model="state.form.isTop" :active-value="1" :inactive-value="0" active-text="是" inactive-text="否" />
				</el-form-item>
				<el-form-item v-if="isAdmin" label="状态">
					<el-select v-model="state.form.status" placeholder="请选择状态">
						<el-option label="草稿" :value="0" />
						<el-option label="发布" :value="1" />
					</el-select>
				</el-form-item>
				<el-form-item label="政策附件">
					<el-upload
						:action="uploadAction"
						:headers="uploadHeaders"
						:limit="1"
						accept=".pdf"
						:on-success="handlePolicyUploadSuccess"
						:on-remove="handlePolicyRemove"
						:file-list="policyFileList"
					>
						<el-button type="primary">上传PDF</el-button>
					</el-upload>
					<div class="policy-tip">仅支持PDF，上传后可预览/下载</div>
				</el-form-item>
				<el-form-item label="政策摘要">
					<el-input v-model="state.form.content" type="textarea" rows="4" placeholder="请输入政策摘要" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-drawer v-model="state.detailVisible" title="政策详情" size="50%">
			<div class="policy-detail">
				<h3>{{ state.detail.title }}</h3>
				<div class="policy-meta">
					<span>
						推送对象：
						<el-tag v-for="group in parseTargetGroups(state.detail.targetGroup)" :key="group" :type="groupTagType(group)" class="mr5">
							{{ targetGroupLabel(group) }}
						</el-tag>
					</span>
					<span>分类：{{ state.detail.policyCategory || '未设置' }}</span>
					<span>有效状态：{{ state.detail.expired ? '已过期' : '未过期' }}</span>
					<span>归档状态：{{ state.detail.archiveStatus === 1 ? '已归档' : '未归档' }}</span>
					<span>发布时间：{{ parseDateTime(state.detail.createTime) }}</span>
				</div>
				<div v-if="state.detail.policyFile" class="policy-attachment">
					<el-button type="primary" size="small" @click="previewPolicy(state.detail.policyFile)">预览政策附件</el-button>
				</div>
				<div class="policy-content" v-html="state.detail.content"></div>
			</div>
		</el-drawer>

		<el-dialog v-model="state.readVisible" title="阅读明细" width="720px">
			<div class="read-toolbar">
				<el-input v-model="state.readKeyword" placeholder="请输入姓名" clearable class="read-input" />
				<el-button type="primary" @click="loadReadRecords">查询</el-button>
				<el-button @click="resetReadFilter">清空</el-button>
				<el-button @click="exportReadRecords">导出CSV</el-button>
			</div>
			<el-table :data="paginatedReadRecords" v-loading="state.readLoading" style="width: 100%">
				<el-table-column prop="residentId" label="村民ID" width="120" />
				<el-table-column prop="residentName" label="姓名" width="140" />
				<el-table-column prop="readTime" label="阅读时间" min-width="200">
					<template #default="scope">
						<span>{{ parseDateTime(scope.row.readTime) }}</span>
					</template>
				</el-table-column>
				<template #empty>
					<el-empty description="暂无相关记录" />
				</template>
			</el-table>
			<el-pagination
				v-if="state.readRecords.length > 0"
				class="mt15"
				@size-change="onReadSizeChange"
				@current-change="onReadCurrentChange"
				:pager-count="5"
				:page-sizes="[10, 20, 30]"
				v-model:current-page="state.readPage.current"
				background
				v-model:page-size="state.readPage.size"
				layout="total, sizes, prev, pager, next, jumper"
				:total="state.readRecords.length"
			/>
		</el-dialog>

		<el-dialog v-model="state.expireVisible" title="批量设置过期时间" width="420px">
			<el-form>
				<el-form-item label="过期时间">
					<el-date-picker v-model="state.expireTime" type="datetime" placeholder="请选择过期时间" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.expireVisible = false">取消</el-button>
				<el-button type="primary" @click="submitBatchExpire">确认</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsPolicy">
import { computed, onMounted, reactive, ref, nextTick, getCurrentInstance } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsNoticeApi } from '/@/api/vms/notice';
import { useUserInfo } from '/@/stores/userInfo';
import { parseDateTime } from '/@/utils/formatTime';
import { Session } from '/@/utils/storage';
import { useRouter } from 'vue-router';

const noticeApi = useVmsNoticeApi();
const userInfo = useUserInfo();
const router = useRouter();
const queryRef = ref();
const uploadAction = `${import.meta.env.VITE_API_URL || '/manageApi'}/manage/file/uploadFile`;
const uploadHeaders = { Authorization: Session.get('token') || '' };
const { proxy } = getCurrentInstance() as any;
const { vms_policy_category } = proxy.parseDict('vms_policy_category');

const isAdmin = computed(() => userInfo.userInfos.roles?.includes('1'));

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { title: '', targetGroup: '', status: '', policyCategory: '', archiveStatus: '', expired: '' },
		},
	},
	archiveTab: 'active',
	activeCategory: '',
	dialogVisible: false,
	dialogTitle: '新增政策',
	dialogType: 'add',
	detailVisible: false,
	detail: {
		title: '',
		targetGroup: '',
		content: '',
		policyFile: '',
		policyCategory: '',
		expireTime: '',
		archiveStatus: 0,
		expired: false,
		createTime: '',
	},
	form: {
		id: undefined,
		title: '',
		content: '',
		type: '政策',
		policyFile: '',
		policyCategory: '',
		expireTime: '',
		archiveStatus: 0,
		targetGroup: ['all'],
		isTop: 0,
		status: 1,
	},
	readVisible: false,
	readLoading: false,
	readRecords: [] as Array<any>,
	readKeyword: '',
	readPage: {
		current: 1,
		size: 10,
	},
	currentReadId: undefined as undefined | number,
	selectedRows: [] as Array<any>,
	expireVisible: false,
	expireTime: '',
});

const policyCategoryOptions = computed(() => {
	const dict = vms_policy_category?.value || {};
	const items = Object.values(dict) as Array<{ label: string; value: string }>;
	if (items.length > 0) {
		return items.map((item) => ({ label: item.label, value: item.value }));
	}
	return [
		{ label: '补贴政策', value: '补贴政策' },
		{ label: '建设资金', value: '建设资金' },
		{ label: '教育医疗', value: '教育医疗' },
		{ label: '环境治理', value: '环境治理' },
		{ label: '产业扶持', value: '产业扶持' },
		{ label: '其他', value: '其他' },
	];
});

const targetGroupLabel = (val: string) => {
	if (val === 'party') return '党员';
	if (val === 'poor') return '贫困户';
	return '全员';
};

const groupTagType = (val: string) => {
	if (val === 'party') return 'success';
	if (val === 'poor') return 'danger';
	return 'info';
};

const parseTargetGroups = (value: string) => {
	if (!value) return ['all'];
	return value.split(',').filter((item) => item);
};

const toGroupString = (value: string[] | string) => {
	if (Array.isArray(value)) {
		return value.length ? value.join(',') : 'all';
	}
	return value || 'all';
};

const normalizeTargetGroups = (value: string[]) => {
	if (!value || value.length === 0) return ['all'];
	if (value.includes('all') && value.length > 1) {
		return ['all'];
	}
	return value;
};

const onTargetGroupChange = (val: string[]) => {
	state.form.targetGroup = normalizeTargetGroups(val);
};

const getTableData = () => {
	state.tableData.loading = true;
	const query = { ...state.tableData.param.query, type: '政策' };
	if (query.status === '' || query.status === null || query.status === undefined) {
		delete (query as any).status;
	}
	if (!query.policyCategory) {
		delete (query as any).policyCategory;
	}
	if (query.archiveStatus === '' || query.archiveStatus === null || query.archiveStatus === undefined) {
		delete (query as any).archiveStatus;
	}
	noticeApi.getPageList({ ...state.tableData.param, query }).then((res: any) => {
		const records = res.records || [];
		if (query.expired === 0) {
			state.tableData.records = records.filter((item: any) => !item.expired);
		} else if (query.expired === 1) {
			state.tableData.records = records.filter((item: any) => item.expired);
		} else {
			state.tableData.records = records;
		}
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	nextTick(() => queryRef.value?.resetFields());
	onArchiveTabChange();
};

const onArchiveTabChange = () => {
	if (state.archiveTab === 'active') {
		state.tableData.param.query.archiveStatus = 0;
	} else if (state.archiveTab === 'archived') {
		state.tableData.param.query.archiveStatus = 1;
	} else {
		state.tableData.param.query.archiveStatus = '';
	}
	state.tableData.param.page.current = 1;
	getTableData();
};

const setActiveCategory = (val: string) => {
	state.activeCategory = val;
	state.tableData.param.query.policyCategory = val;
	state.tableData.param.page.current = 1;
	getTableData();
};

const goDictManage = () => {
	router.push('/system/dic');
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增政策' : '编辑政策';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row, targetGroup: parseTargetGroups(row.targetGroup), type: '政策' };
	} else {
		state.form = {
			id: undefined,
			title: '',
			content: '',
			type: '政策',
			policyFile: '',
			policyCategory: '',
			expireTime: '',
			archiveStatus: 0,
			targetGroup: ['all'],
			isTop: 0,
			status: 1,
		};
	}
};

const submitForm = () => {
	const api = state.dialogType === 'add' ? noticeApi.create : noticeApi.update;
	if (!state.form.title) {
		ElMessage.error('请输入政策标题');
		return;
	}
	if (!state.form.policyCategory) {
		ElMessage.error('请选择政策分类');
		return;
	}
	if (!state.form.policyFile) {
		ElMessage.error('请上传政策附件');
		return;
	}
	if (!state.form.content) {
		ElMessage.error('请输入政策摘要');
		return;
	}
	const payload = { ...state.form, targetGroup: toGroupString(state.form.targetGroup) };
	api(payload).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('确认删除该政策吗?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => noticeApi.delete(row.id))
		.then(() => {
			ElMessage.success('删除成功');
			getTableData();
		})
		.catch(() => null);
};

const openDetail = (row: any) => {
	state.detail = { ...row };
	state.detailVisible = true;
	if (!isAdmin.value && !row.readStatus) {
		noticeApi.markRead(row.id).then(() => {
			row.readStatus = true;
			state.detail.readStatus = true;
		});
	}
};

const toggleArchive = (row: any) => {
	const next = row.archiveStatus === 1 ? 0 : 1;
	ElMessageBox.confirm(`确认${next === 1 ? '归档' : '恢复'}该政策吗?`, '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => noticeApi.archivePolicy(row.id, next))
		.then(() => {
			ElMessage.success(next === 1 ? '已归档' : '已恢复');
			getTableData();
		})
		.catch(() => null);
};

const openReadDialog = (row: any) => {
	state.currentReadId = row.id;
	state.readVisible = true;
	state.readKeyword = '';
	state.readPage = { current: 1, size: 10 };
	loadReadRecords();
};

const loadReadRecords = () => {
	if (!state.currentReadId) return;
	state.readLoading = true;
	noticeApi.getReadRecords(state.currentReadId, state.readKeyword).then((res: any) => {
		state.readRecords = res || [];
		state.readLoading = false;
	});
};

const resetReadFilter = () => {
	state.readKeyword = '';
	loadReadRecords();
};

const exportReadRecords = () => {
	if (!state.currentReadId) return;
	if (!state.readRecords.length) {
		ElMessage.warning('暂无可导出记录');
		return;
	}
	noticeApi.exportReadRecords(state.currentReadId, state.readKeyword);
};

const paginatedReadRecords = computed(() => {
	const start = (state.readPage.current - 1) * state.readPage.size;
	const end = start + state.readPage.size;
	return state.readRecords.slice(start, end);
});

const onReadSizeChange = (val: number) => {
	state.readPage.size = val;
	state.readPage.current = 1;
};

const onReadCurrentChange = (val: number) => {
	state.readPage.current = val;
};

const handlePolicyUploadSuccess = (res: string) => {
	state.form.policyFile = res;
	ElMessage.success('上传成功');
};

const handlePolicyRemove = () => {
	state.form.policyFile = '';
};

const previewPolicy = (fileName: string) => {
	if (!fileName) return;
	const base = import.meta.env.VITE_API_URL || '/manageApi';
	const url = `${base}/manage/file/download/${encodeURIComponent(fileName)}`;
	window.open(url, '_blank');
};

const policyFileList = computed(() => {
	if (!state.form.policyFile) return [];
	return [{ name: state.form.policyFile, url: state.form.policyFile }];
});

const readPercent = (row: any) => {
	const total = row?.targetCount || 0;
	if (!total) return 0;
	return Math.round(((row.readCount || 0) / total) * 100);
};

const onHandleSizeChange = (val: number) => {
	state.tableData.param.page.size = val;
	getTableData();
};

const onHandleCurrentChange = (val: number) => {
	state.tableData.param.page.current = val;
	getTableData();
};

const batchArchive = (status: number) => {
	if (!state.selectedRows.length) {
		ElMessage.warning('请先选择政策');
		return;
	}
	const text = status === 1 ? '归档' : '恢复';
	ElMessageBox.confirm(`确认批量${text}选中政策吗?`, '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => Promise.all(state.selectedRows.map((row) => noticeApi.archivePolicy(row.id, status))))
		.then(() => {
			ElMessage.success(`批量${text}成功`);
			state.selectedRows = [];
			getTableData();
		})
		.catch(() => null);
};

const onSelectionChange = (rows: any[]) => {
	state.selectedRows = rows || [];
};

const openBatchExpire = () => {
	if (!state.selectedRows.length) {
		ElMessage.warning('请先选择政策');
		return;
	}
	state.expireTime = '';
	state.expireVisible = true;
};

const submitBatchExpire = () => {
	if (!state.expireTime) {
		ElMessage.error('请选择过期时间');
		return;
	}
	const ids = state.selectedRows.map((row) => row.id);
	noticeApi.batchExpire(ids, new Date(state.expireTime).getTime()).then(() => {
		ElMessage.success('批量设置成功');
		state.expireVisible = false;
		state.selectedRows = [];
		getTableData();
	});
};

onMounted(() => {
	userInfo.setUserInfos().then(() => {
		onArchiveTabChange();
		getTableData();
	});
});
</script>

<style scoped lang="scss">
.vms-policy {
	.vms-policy-search {
		margin-bottom: 10px;
	}
	.vms-policy-actions {
		display: flex;
		justify-content: flex-end;
		margin-bottom: 10px;
		gap: 10px;
	}
	.vms-policy-filters {
		margin-bottom: 10px;
		display: flex;
		flex-wrap: wrap;
		gap: 6px;
	}
	.policy-detail {
		.policy-meta {
			display: flex;
			gap: 15px;
			color: var(--el-text-color-secondary);
			margin-bottom: 10px;
		}
		.policy-attachment {
			margin-bottom: 10px;
		}
		.policy-content {
			line-height: 1.7;
		}
	}
	.read-progress {
		display: flex;
		align-items: center;
		gap: 8px;
		.read-count {
			font-size: 12px;
			color: var(--el-text-color-secondary);
			white-space: nowrap;
		}
	}
	.read-toolbar {
		display: flex;
		gap: 10px;
		margin-bottom: 10px;
		.read-input {
			width: 220px;
		}
	}
	.policy-tip {
		color: var(--el-text-color-secondary);
		font-size: 12px;
		margin-top: 6px;
	}
}
</style>

