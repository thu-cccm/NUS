<template>
	<div class="vms-apply layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-apply-header">
				<el-tabs v-model="state.activeTab" @tab-change="onTabChange">
					<el-tab-pane label="全部" name="all" />
					<el-tab-pane label="待审核" name="pending" />
					<el-tab-pane label="已通过" name="approved" />
					<el-tab-pane label="已驳回" name="rejected" />
					<el-tab-pane label="公示中" name="publicing" />
					<el-tab-pane label="已公示" name="publiced" />
					<el-tab-pane label="已归档" name="archived" />
				</el-tabs>
				<div class="apply-actions">
					<el-button type="primary" @click="openDialog('add')">新增申请</el-button>
					<el-button v-if="isAdmin" @click="exportApply">导出CSV</el-button>
				</div>
			</div>

			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="applyType" label="申请类型" width="140" />
				<el-table-column prop="content" label="申请内容" min-width="220" show-overflow-tooltip />
				<el-table-column prop="auditReply" label="审核意见" min-width="200" show-overflow-tooltip />
				<el-table-column prop="residentId" label="申请人ID" width="120" />
				<el-table-column prop="createTime" label="提交时间" width="180">
					<template #default="scope">
						<span>{{ parseDateTime(scope.row.createTime) }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态" width="120">
					<template #default="scope">
						<el-tag :type="statusTag(scope.row.status)">
							{{ statusLabel(scope.row.status) }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="publicStatus" label="公示状态" width="120">
					<template #default="scope">
						<el-tag :type="publicTag(scope.row.publicStatus)">
							{{ publicLabel(scope.row.publicStatus) }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="publicEnd" label="公示截止" width="170">
					<template #default="scope">
						<span>{{ scope.row.publicEnd ? parseDateTime(scope.row.publicEnd) : '-' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="公示剩余" width="140">
					<template #default="scope">
						<span>{{ calcPublicRemain(scope.row) }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="archiveStatus" label="归档状态" width="110">
					<template #default="scope">
						<el-tag :type="scope.row.archiveStatus === 1 ? 'info' : 'success'">
							{{ scope.row.archiveStatus === 1 ? '已归档' : '未归档' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template #default="scope">
						<el-button size="small" text type="primary" @click="openDetail(scope.row)">详情</el-button>
						<el-button
							v-if="isAdmin || scope.row.status === 2"
							size="small"
							text
							type="primary"
							@click="openDialog(scope.row.status === 2 && !isAdmin ? 'resubmit' : 'edit', scope.row)"
						>
							{{ scope.row.status === 2 && !isAdmin ? '重新提交' : '编辑' }}
						</el-button>
						<el-button
							v-if="isAdmin || scope.row.status !== 1"
							size="small"
							text
							type="danger"
							@click="onRowDel(scope.row)"
						>
							删除
						</el-button>
						<el-button
							v-if="isAdmin && scope.row.status !== 0"
							size="small"
							text
							type="warning"
							@click="toggleArchive(scope.row)"
						>
							{{ scope.row.archiveStatus === 1 ? '恢复' : '归档' }}
						</el-button>
						<el-button
							v-if="isAdmin && scope.row.status === 0"
							size="small"
							text
							type="warning"
							@click="openAudit(scope.row)"
						>
							审核
						</el-button>
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

		<el-dialog v-model="state.dialogVisible" :title="state.dialogTitle" width="620px">
			<el-steps :active="applyStep.active" :process-status="applyStep.status" align-center class="mb20">
				<el-step title="提交申请" />
				<el-step title="村委初审" />
				<el-step title="公示" />
				<el-step title="完成" />
			</el-steps>
			<el-form :model="state.form">
				<el-form-item v-if="state.form.status === 2 && state.form.auditReply" label="驳回原因">
					<el-input v-model="state.form.auditReply" type="textarea" rows="2" readonly />
				</el-form-item>
				<el-form-item label="申请类型">
					<el-select v-model="state.form.applyType">
						<el-option label="低保申请" value="低保申请" />
						<el-option label="宅基地审批" value="宅基地审批" />
						<el-option label="户口迁移" value="户口迁移" />
					</el-select>
				</el-form-item>
				<el-form-item label="申请说明">
					<el-input v-model="state.form.content" type="textarea" rows="4" />
				</el-form-item>
				<el-form-item label="佐证材料">
					<el-upload
						:action="uploadAction"
						list-type="picture-card"
						:headers="uploadHeaders"
						:on-success="handleUploadSuccess"
						:on-remove="handleUploadRemove"
						:file-list="state.uploadFileList"
						:limit="5"
						accept=".jpg,.jpeg,.png"
					>
						<el-icon><ele-Plus /></el-icon>
					</el-upload>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-dialog v-model="state.auditVisible" title="事务审核" width="460px">
			<el-form :model="state.auditForm">
				<el-form-item label="审核结果">
					<el-radio-group v-model="state.auditForm.status">
						<el-radio :label="1">通过</el-radio>
						<el-radio :label="2">驳回</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item v-if="state.auditForm.status === 1" label="公示截止">
					<el-date-picker v-model="state.auditForm.publicEndTime" type="datetime" placeholder="请选择公示截止时间" />
				</el-form-item>
				<el-form-item label="回复意见">
					<el-input v-model="state.auditForm.reply" type="textarea" rows="3" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.auditVisible = false">取消</el-button>
				<el-button type="primary" @click="submitAudit">确认</el-button>
			</template>
		</el-dialog>

		<el-drawer v-model="state.detailVisible" title="申请详情" size="520px">
			<el-descriptions :column="1" border>
				<el-descriptions-item label="申请类型">{{ state.detail.applyType }}</el-descriptions-item>
				<el-descriptions-item label="申请内容">{{ state.detail.content }}</el-descriptions-item>
				<el-descriptions-item label="申请人ID">{{ state.detail.residentId }}</el-descriptions-item>
				<el-descriptions-item label="提交时间">{{ parseDateTime(state.detail.createTime) }}</el-descriptions-item>
				<el-descriptions-item label="状态">{{ statusLabel(state.detail.status) }}</el-descriptions-item>
				<el-descriptions-item label="公示状态">{{ publicLabel(state.detail.publicStatus) }}</el-descriptions-item>
				<el-descriptions-item label="公示开始">{{ state.detail.publicStart ? parseDateTime(state.detail.publicStart) : '暂无' }}</el-descriptions-item>
				<el-descriptions-item label="公示截止">{{ state.detail.publicEnd ? parseDateTime(state.detail.publicEnd) : '暂无' }}</el-descriptions-item>
				<el-descriptions-item label="公示剩余">{{ calcPublicRemain(state.detail) }}</el-descriptions-item>
				<el-descriptions-item label="归档状态">{{ state.detail.archiveStatus === 1 ? '已归档' : '未归档' }}</el-descriptions-item>
				<el-descriptions-item label="归档时间">{{ state.detail.archiveTime ? parseDateTime(state.detail.archiveTime) : '暂无' }}</el-descriptions-item>
				<el-descriptions-item label="审核人">{{ state.detail.auditBy || '暂无' }}</el-descriptions-item>
				<el-descriptions-item label="审核时间">{{ state.detail.auditTime ? parseDateTime(state.detail.auditTime) : '暂无' }}</el-descriptions-item>
				<el-descriptions-item label="审核意见">{{ state.detail.auditReply || '暂无' }}</el-descriptions-item>
			</el-descriptions>
			<div class="proof-preview" v-if="state.detailFiles.length">
				<div class="proof-title">佐证材料</div>
				<el-image v-for="(img, idx) in state.detailFiles" :key="idx" :src="img" fit="cover" class="proof-img" />
			</div>
		</el-drawer>
	</div>
</template>

<script setup lang="ts" name="vmsApply">
import { computed, onMounted, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Session } from '/@/utils/storage';
import { parseDateTime } from '/@/utils/formatTime';
import { useVmsApplyApi } from '/@/api/vms/apply';
import { useUserInfo } from '/@/stores/userInfo';

const applyApi = useVmsApplyApi();
const userInfo = useUserInfo();

const uploadAction = `${import.meta.env.VITE_API_URL || '/manageApi'}/manage/file/uploadImage`;
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
			query: { status: '', archiveStatus: 0, publicStatus: '' },
		},
	},
	dialogVisible: false,
	dialogTitle: '新增申请',
	dialogType: 'add',
	form: {
		id: undefined,
		applyType: '低保申请',
		content: '',
		proofFiles: '',
		status: 0,
		publicStatus: 0,
		publicStart: '',
		publicEnd: '',
		auditReply: '',
		archiveStatus: 0,
		archiveTime: '',
	},
	uploadFileList: [] as Array<any>,
	auditVisible: false,
	auditForm: {
		applyId: undefined,
		status: 1,
		reply: '',
		publicEndTime: '',
	},
	detailVisible: false,
	detail: {
		applyType: '',
		content: '',
		residentId: '',
		createTime: '',
		status: 0,
		publicStatus: 0,
		publicStart: '',
		publicEnd: '',
		auditBy: '',
		auditTime: '',
		auditReply: '',
		proofFiles: '',
		archiveStatus: 0,
		archiveTime: '',
	},
	detailFiles: [] as Array<string>,
});

const statusLabel = (status: number) => {
	if (status === 1) return '已通过';
	if (status === 2) return '已驳回';
	return '待审核';
};

const statusTag = (status: number) => {
	if (status === 1) return 'success';
	if (status === 2) return 'danger';
	return 'warning';
};

const publicLabel = (status: number) => {
	if (status === 1) return '公示中';
	if (status === 2) return '已公示';
	return '未公示';
};

const publicTag = (status: number) => {
	if (status === 1) return 'warning';
	if (status === 2) return 'success';
	return 'info';
};

const calcPublicRemain = (row: any) => {
	if (!row?.publicStatus || row.publicStatus !== 1 || !row.publicEnd) return '-';
	const end = new Date(row.publicEnd).getTime();
	if (Number.isNaN(end)) return '-';
	const diff = end - Date.now();
	if (diff <= 0) return '已到期';
	const days = Math.floor(diff / (24 * 60 * 60 * 1000));
	const hours = Math.floor((diff % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000));
	if (days > 0) return `${days}天${hours}小时`;
	const minutes = Math.floor((diff % (60 * 60 * 1000)) / (60 * 1000));
	return `${hours}小时${minutes}分钟`;
};

const onTabChange = () => {
	if (state.activeTab === 'pending') {
		state.tableData.param.query.status = 0;
		state.tableData.param.query.archiveStatus = 0;
		state.tableData.param.query.publicStatus = '';
	} else if (state.activeTab === 'approved') {
		state.tableData.param.query.status = 1;
		state.tableData.param.query.archiveStatus = 0;
		state.tableData.param.query.publicStatus = '';
	} else if (state.activeTab === 'rejected') {
		state.tableData.param.query.status = 2;
		state.tableData.param.query.archiveStatus = 0;
		state.tableData.param.query.publicStatus = '';
	} else if (state.activeTab === 'publicing') {
		state.tableData.param.query.status = 1;
		state.tableData.param.query.archiveStatus = 0;
		state.tableData.param.query.publicStatus = 1;
	} else if (state.activeTab === 'publiced') {
		state.tableData.param.query.status = 1;
		state.tableData.param.query.archiveStatus = 0;
		state.tableData.param.query.publicStatus = 2;
	} else if (state.activeTab === 'archived') {
		state.tableData.param.query.status = '';
		state.tableData.param.query.archiveStatus = 1;
		state.tableData.param.query.publicStatus = '';
	} else {
		state.tableData.param.query.status = '';
		state.tableData.param.query.archiveStatus = 0;
		state.tableData.param.query.publicStatus = '';
	}
	state.tableData.param.page.current = 1;
	getTableData();
};

const getTableData = () => {
	state.tableData.loading = true;
	applyApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records;
		state.tableData.total = res.total;
		state.tableData.loading = false;
	});
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增申请' : type === 'resubmit' ? '重新提交' : '编辑申请';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
		state.uploadFileList = buildFileList(state.form.proofFiles);
	} else if (type === 'resubmit' && row) {
		state.form = { ...row };
		state.uploadFileList = buildFileList(state.form.proofFiles);
	} else {
		state.form = {
			id: undefined,
			applyType: '低保申请',
			content: '',
			proofFiles: '',
			status: 0,
			publicStatus: 0,
			publicStart: '',
			publicEnd: '',
			auditReply: '',
			archiveStatus: 0,
			archiveTime: '',
		};
		state.uploadFileList = [];
	}
};

const buildFileList = (files: string) => {
	if (!files) return [];
	return files
		.split(',')
		.filter((item) => item)
		.map((url: string, index: number) => ({ name: `材料${index + 1}`, url }));
};

const syncProofFiles = () => {
	const urls = state.uploadFileList
		.map((item: any) => item.url || item.response?.data || item.response || item.fileServiceName)
		.filter((item: string) => item);
	state.form.proofFiles = urls.join(',');
};

const handleUploadSuccess = (response: any, file: any, fileList: any[]) => {
	let url = '';
	if (response?.data) url = response.data;
	else if (response?.fileServiceName) url = response.fileServiceName;
	else if (typeof response === 'string') url = response;
	if (!url) return;
	file.url = url;
	state.uploadFileList = fileList;
	syncProofFiles();
};

const handleUploadRemove = (file: any, fileList: any[]) => {
	state.uploadFileList = fileList;
	syncProofFiles();
};

const submitForm = () => {
	const api =
		state.dialogType === 'add'
			? applyApi.create
			: state.dialogType === 'resubmit'
				? applyApi.resubmit
				: applyApi.update;
	api(state.form).then(() => {
		ElMessage.success(state.dialogType === 'resubmit' ? '已重新提交' : '保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该申请，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => applyApi.delete(row.id))
		.then(() => {
			ElMessage.success('删除成功');
			getTableData();
		})
		.catch(() => null);
};

const openAudit = (row: any) => {
	const now = new Date();
	const defaultEnd = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000);
	state.auditVisible = true;
	state.auditForm = {
		applyId: row.id,
		status: 1,
		reply: '',
		publicEndTime: defaultEnd,
	};
};

const submitAudit = () => {
	if (state.auditForm.status === 2 && !state.auditForm.reply) {
		ElMessage.error('驳回时必须填写驳回原因');
		return;
	}
	const payload: any = { ...state.auditForm };
	if (payload.publicEndTime) {
		payload.publicEndTime = new Date(payload.publicEndTime).getTime();
	}
	applyApi.audit(payload).then(() => {
		ElMessage.success('审核完成');
		state.auditVisible = false;
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

const exportApply = () => {
	const params: any = {};
	if (state.tableData.param.query.status !== '') params.status = state.tableData.param.query.status;
	if (state.tableData.param.query.publicStatus !== '') params.publicStatus = state.tableData.param.query.publicStatus;
	if (state.tableData.param.query.archiveStatus !== '') params.archiveStatus = state.tableData.param.query.archiveStatus;
	applyApi.export(params);
};

const openDetail = (row: any) => {
	state.detail = { ...row };
	state.detailFiles = row.proofFiles ? row.proofFiles.split(',').filter((item: string) => item) : [];
	state.detailVisible = true;
};

const toggleArchive = (row: any) => {
	const next = row.archiveStatus === 1 ? 0 : 1;
	ElMessageBox.confirm(`确认${next === 1 ? '归档' : '恢复'}该申请吗?`, '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => applyApi.archive(row.id, next))
		.then(() => {
			ElMessage.success(next === 1 ? '已归档' : '已恢复');
			getTableData();
		})
		.catch(() => null);
};

onMounted(() => {
	userInfo.setUserInfos().then(() => {
		onTabChange();
		getTableData();
	});
});

const applyStep = computed(() => {
	const status = state.form.status ?? 0;
	const publicStatus = state.form.publicStatus ?? 0;
	if (status === 2) {
		return { active: 2, status: 'error' };
	}
	if (status === 1 && publicStatus === 1) {
		return { active: 3, status: 'process' };
	}
	if (status === 1 && publicStatus === 2) {
		return { active: 4, status: 'finish' };
	}
	return { active: 1, status: 'process' };
});
</script>

<style scoped lang="scss">
.vms-apply {
	.vms-apply-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 10px;
	}
	.apply-actions {
		display: flex;
		gap: 10px;
	}
	.proof-preview {
		margin-top: 15px;
		.proof-title {
			font-weight: 600;
			margin-bottom: 8px;
		}
		.proof-img {
			width: 120px;
			height: 90px;
			margin-right: 10px;
			margin-bottom: 10px;
			border-radius: 4px;
		}
	}
}
</style>

