<template>
	<div class="vms-notice layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<el-tabs v-model="state.activeTab" @tab-change="onTabChange" class="mb10">
				<el-tab-pane label="全部" name="all" />
				<el-tab-pane label="通知" name="notice" />
				<el-tab-pane label="公示" name="public" />
				<el-tab-pane label="政策" name="policy" />
			</el-tabs>
			<div class="vms-notice-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="标题">
						<el-input v-model="state.tableData.param.query.title" placeholder="请输入标题" clearable />
					</el-form-item>
					<el-form-item label="类型">
						<el-select v-model="state.tableData.param.query.type" placeholder="请选择类型" clearable>
							<el-option label="通知" value="通知" />
							<el-option label="公示" value="公示" />
							<el-option label="政策" value="政策" />
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

			<div class="vms-notice-actions">
				<el-button v-if="isAdmin" type="primary" @click="openDialog('add')">新增公告</el-button>
			</div>

			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
				<el-table-column prop="type" label="类型" width="100" />
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
							v-if="scope.row.type === '政策' && scope.row.policyFile"
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
				<el-table-column prop="isTop" label="置顶" width="80">
					<template #default="scope">
						<el-tag :type="scope.row.isTop === 1 ? 'warning' : 'info'">
							{{ scope.row.isTop === 1 ? '是' : '否' }}
						</el-tag>
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
						<el-button v-if="isAdmin" size="small" text type="primary" @click="openDialog('edit', scope.row)">编辑</el-button>
						<el-button v-if="isAdmin" size="small" text type="primary" @click="toggleStatus(scope.row)">
							{{ scope.row.status === 1 ? '下线' : '发布' }}
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
				<el-form-item label="标题">
					<el-input v-model="state.form.title" placeholder="请输入标题" />
				</el-form-item>
				<el-form-item label="类型">
					<el-select v-model="state.form.type" placeholder="请选择类型">
						<el-option label="通知" value="通知" />
						<el-option label="公示" value="公示" />
						<el-option label="政策" value="政策" />
					</el-select>
				</el-form-item>
				<el-form-item v-if="state.form.type === '政策'" label="政策附件">
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
				<el-form-item label="内容">
					<Editor :getHtml="state.form.content" @update:getHtml="onEditorChange" height="260px" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-drawer v-model="state.detailVisible" title="公告详情" size="50%">
			<div class="notice-detail">
				<h3>{{ state.detail.title }}</h3>
				<div class="notice-meta">
					<span>类型：{{ state.detail.type }}</span>
					<span>
						推送对象：
						<el-tag v-for="group in parseTargetGroups(state.detail.targetGroup)" :key="group" :type="groupTagType(group)" class="mr5">
							{{ targetGroupLabel(group) }}
						</el-tag>
					</span>
					<span>发布时间：{{ parseDateTime(state.detail.createTime) }}</span>
				</div>
				<div v-if="state.detail.type === '政策' && state.detail.policyFile" class="notice-attachment">
					<el-button type="primary" size="small" @click="previewPolicy(state.detail.policyFile)">预览政策附件</el-button>
				</div>
				<div class="notice-content" v-html="state.detail.content"></div>
			</div>
		</el-drawer>
	</div>
</template>

<script setup lang="ts" name="vmsNotice">
import { computed, onMounted, reactive, ref, nextTick, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsNoticeApi } from '/@/api/vms/notice';
import { useUserInfo } from '/@/stores/userInfo';
import { parseDateTime } from '/@/utils/formatTime';
import Editor from '/@/components/editor/index.vue';
import { Session } from '/@/utils/storage';

const noticeApi = useVmsNoticeApi();
const userInfo = useUserInfo();
const queryRef = ref();
const uploadAction = `${import.meta.env.VITE_API_URL || '/manageApi'}/manage/file/uploadFile`;
const uploadHeaders = { Authorization: Session.get('token') || '' };

const isAdmin = computed(() => userInfo.userInfos.roles?.includes('1'));

const state = reactive({
	activeTab: 'all',
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { title: '', type: '', targetGroup: '', status: '' },
		},
	},
	dialogVisible: false,
	dialogTitle: '新增公告',
	dialogType: 'add',
	detailVisible: false,
	detail: {
		title: '',
		type: '',
		targetGroup: '',
		content: '',
		policyFile: '',
		createTime: '',
	},
	form: {
		id: undefined,
		title: '',
		content: '',
		type: '通知',
		policyFile: '',
		targetGroup: ['all'],
		isTop: 0,
		status: 1,
	},
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

const onTabChange = () => {
	if (state.activeTab === 'notice') state.tableData.param.query.type = '通知';
	else if (state.activeTab === 'public') state.tableData.param.query.type = '公示';
	else if (state.activeTab === 'policy') state.tableData.param.query.type = '政策';
	else state.tableData.param.query.type = '';
	state.tableData.param.page.current = 1;
	getTableData();
};

const getTableData = () => {
	state.tableData.loading = true;
	const query = { ...state.tableData.param.query };
	if (query.status === '' || query.status === null || query.status === undefined) {
		delete (query as any).status;
	}
	noticeApi.getPageList({ ...state.tableData.param, query }).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	nextTick(() => queryRef.value?.resetFields());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增公告' : '编辑公告';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row, targetGroup: parseTargetGroups(row.targetGroup) };
	} else {
		state.form = {
			id: undefined,
			title: '',
			content: '',
			type: '通知',
			policyFile: '',
			targetGroup: ['all'],
			isTop: 0,
			status: 1,
		};
	}
};

const submitForm = () => {
	const api = state.dialogType === 'add' ? noticeApi.create : noticeApi.update;
	if (!state.form.title) {
		ElMessage.error('请输入标题');
		return;
	}
	if (!state.form.content) {
		ElMessage.error('请输入内容');
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
	ElMessageBox.confirm('确认删除该公告吗?', '提示', {
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

watch(
	() => state.form.type,
	(val) => {
		if (val !== '政策') {
			state.form.policyFile = '';
		}
	}
);

watch(
	() => state.dialogVisible,
	(val) => {
		if (!val) {
			state.form.targetGroup = ['all'];
		}
	}
);

const onEditorChange = (val: string) => {
	state.form.content = val;
};

const toggleStatus = (row: any) => {
	const nextStatus = row.status === 1 ? 0 : 1;
	noticeApi.update({ ...row, status: nextStatus }).then(() => {
		ElMessage.success(nextStatus === 1 ? '已发布' : '已下线');
		getTableData();
	});
};

const onHandleSizeChange = (val: number) => {
	state.tableData.param.page.size = val;
	getTableData();
};

const onHandleCurrentChange = (val: number) => {
	state.tableData.param.page.current = val;
	getTableData();
};

onMounted(() => {
	userInfo.setUserInfos().then(() => {
		getTableData();
	});
});
</script>

<style scoped lang="scss">
.vms-notice {
	.vms-notice-search {
		margin-bottom: 10px;
	}
	.vms-notice-actions {
		display: flex;
		justify-content: flex-end;
		margin-bottom: 10px;
	}
	.notice-detail {
		.notice-meta {
			display: flex;
			gap: 15px;
			color: var(--el-text-color-secondary);
			margin-bottom: 10px;
		}
		.notice-attachment {
			margin-bottom: 10px;
		}
		.notice-content {
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
	.policy-tip {
		color: var(--el-text-color-secondary);
		font-size: 12px;
		margin-top: 6px;
	}
}
</style>

