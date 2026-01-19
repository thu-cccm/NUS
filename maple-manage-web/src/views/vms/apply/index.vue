<template>
	<div class="vms-apply layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-apply-header">
				<el-tabs v-model="state.activeTab" @tab-change="onTabChange">
					<el-tab-pane label="全部" name="all" />
					<el-tab-pane label="待审核" name="pending" />
					<el-tab-pane label="已通过" name="approved" />
					<el-tab-pane label="已驳回" name="rejected" />
				</el-tabs>
				<el-button type="success" plain @click="openDialog('add')">新增申请</el-button>
			</div>

			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="applyType" label="申请类型" width="140" />
				<el-table-column prop="content" label="申请内容" min-width="220" show-overflow-tooltip />
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
				<el-table-column label="操作" width="200">
					<template #default="scope">
						<el-button size="small" text type="primary" @click="openDialog('edit', scope.row)">编辑</el-button>
						<el-button size="small" text type="danger" @click="onRowDel(scope.row)">删除</el-button>
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

		<el-dialog v-model="state.dialogVisible" :title="state.dialogTitle" width="560px">
			<el-form :model="state.form">
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
				<el-form-item label="回复意见">
					<el-input v-model="state.auditForm.reply" type="textarea" rows="3" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.auditVisible = false">取消</el-button>
				<el-button type="primary" @click="submitAudit">确认</el-button>
			</template>
		</el-dialog>
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
			query: { status: '' },
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
	},
	auditVisible: false,
	auditForm: {
		applyId: undefined,
		status: 1,
		reply: '',
	},
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

const onTabChange = () => {
	if (state.activeTab === 'pending') state.tableData.param.query.status = 0;
	else if (state.activeTab === 'approved') state.tableData.param.query.status = 1;
	else if (state.activeTab === 'rejected') state.tableData.param.query.status = 2;
	else state.tableData.param.query.status = '';
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
	state.dialogTitle = type === 'add' ? '新增申请' : '编辑申请';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			applyType: '低保申请',
			content: '',
			proofFiles: '',
		};
	}
};

const handleUploadSuccess = (response: any) => {
	if (response?.code !== '0000') return;
	const url = response.data;
	const list = state.form.proofFiles ? state.form.proofFiles.split(',') : [];
	list.push(url);
	state.form.proofFiles = list.join(',');
};

const submitForm = () => {
	const api = state.dialogType === 'add' ? applyApi.create : applyApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
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
	state.auditVisible = true;
	state.auditForm = {
		applyId: row.id,
		status: 1,
		reply: '',
	};
};

const submitAudit = () => {
	applyApi.audit(state.auditForm).then(() => {
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

onMounted(() => {
	userInfo.setUserInfos().then(() => {
		getTableData();
	});
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
}
</style>

