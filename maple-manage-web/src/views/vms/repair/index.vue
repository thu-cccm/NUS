<template>
	<div class="vms-repair layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-repair-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="设施名称">
						<el-input v-model="state.tableData.param.query.facilityName" placeholder="请输入设施名称" clearable />
					</el-form-item>
					<el-form-item label="设施类型">
						<el-select v-model="state.tableData.param.query.facilityType" placeholder="请选择" clearable>
							<el-option label="路灯" value="路灯" />
							<el-option label="井盖" value="井盖" />
							<el-option label="护栏" value="护栏" />
							<el-option label="其他" value="其他" />
						</el-select>
					</el-form-item>
					<el-form-item label="状态">
						<el-select v-model="state.tableData.param.query.status" placeholder="请选择" clearable>
							<el-option label="待派单" :value="0" />
							<el-option label="维修中" :value="1" />
							<el-option label="已完成" :value="2" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增报修</el-button>
				<el-button @click="exportRepair">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="facilityName" label="设施名称" width="120" />
				<el-table-column prop="facilityType" label="设施类型" width="100" />
				<el-table-column prop="location" label="报修位置" min-width="150" show-overflow-tooltip />
				<el-table-column prop="description" label="报修描述" min-width="200" show-overflow-tooltip />
				<el-table-column prop="reporterName" label="报修人" width="100" />
				<el-table-column prop="reporterPhone" label="联系电话" width="120" />
				<el-table-column prop="status" label="状态" width="100">
					<template #default="scope">
						<el-tag :type="getStatusType(scope.row.status)">
							{{ getStatusText(scope.row.status) }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="assignee" label="派单人员" width="100" />
				<el-table-column prop="assignTime" label="派单时间" width="160">
					<template #default="scope">
						{{ scope.row.assignTime ? parseDateTime(scope.row.assignTime) : '-' }}
					</template>
				</el-table-column>
				<el-table-column label="操作" width="250" fixed="right">
					<template #default="scope">
						<el-button v-if="isAdmin && scope.row.status === 0" size="small" text type="primary" @click="openAssign(scope.row)">派单</el-button>
						<el-button v-if="isAdmin && scope.row.status === 1" size="small" text type="success" @click="openComplete(scope.row)">完成</el-button>
						<el-button size="small" text type="primary" @click="openDialog('edit', scope.row)">编辑</el-button>
						<el-button size="small" text type="danger" @click="onRowDel(scope.row)">删除</el-button>
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

		<el-dialog v-model="state.dialogVisible" :title="state.dialogTitle" width="600px">
			<el-form :model="state.form" label-width="120px">
				<el-form-item label="设施名称" required>
					<el-input v-model="state.form.facilityName" placeholder="请输入设施名称" />
				</el-form-item>
				<el-form-item label="设施类型" required>
					<el-select v-model="state.form.facilityType" placeholder="请选择" style="width: 100%">
						<el-option label="路灯" value="路灯" />
						<el-option label="井盖" value="井盖" />
						<el-option label="护栏" value="护栏" />
						<el-option label="其他" value="其他" />
					</el-select>
				</el-form-item>
				<el-form-item label="报修位置" required>
					<el-input v-model="state.form.location" placeholder="请输入报修位置" />
				</el-form-item>
				<el-form-item label="报修描述" required>
					<el-input v-model="state.form.description" type="textarea" :rows="4" placeholder="请输入报修描述" />
				</el-form-item>
				<el-form-item label="报修人姓名">
					<el-input v-model="state.form.reporterName" placeholder="请输入报修人姓名" />
				</el-form-item>
				<el-form-item label="联系电话">
					<el-input v-model="state.form.reporterPhone" placeholder="请输入联系电话" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-dialog v-model="state.assignVisible" title="派单" width="400px">
			<el-form :model="state.assignForm" label-width="100px">
				<el-form-item label="派单人员" required>
					<el-input v-model="state.assignForm.assignee" placeholder="请输入派单人员" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.assignVisible = false">取消</el-button>
				<el-button type="primary" @click="submitAssign">确认派单</el-button>
			</template>
		</el-dialog>

		<el-dialog v-model="state.completeVisible" title="完成维修" width="500px">
			<el-form :model="state.completeForm" label-width="100px">
				<el-form-item label="维修结果" required>
					<el-input v-model="state.completeForm.repairResult" type="textarea" :rows="4" placeholder="请输入维修结果" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.completeVisible = false">取消</el-button>
				<el-button type="primary" @click="submitComplete">确认完成</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsRepair">
import { onMounted, reactive, ref, nextTick, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsRepairApi } from '/@/api/vms/repair';
import { useUserInfo } from '/@/stores/userInfo';
import { parseDateTime } from '/@/utils/formatTime';

const repairApi = useVmsRepairApi();
const userInfo = useUserInfo();
const queryRef = ref();

const isAdmin = computed(() => userInfo.userInfos.roles?.includes('1'));

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { facilityName: '', facilityType: '', status: '' },
		},
	},
	dialogVisible: false,
	assignVisible: false,
	completeVisible: false,
	dialogTitle: '新增报修',
	dialogType: 'add',
	form: {
		id: undefined,
		facilityName: '',
		facilityType: '',
		location: '',
		description: '',
		reporterName: '',
		reporterPhone: '',
	},
	assignForm: {
		id: undefined,
		assignee: '',
	},
	completeForm: {
		id: undefined,
		repairResult: '',
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	repairApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	state.tableData.param.query = { facilityName: '', facilityType: '', status: '' };
	nextTick(() => getTableData());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增报修' : '编辑报修';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			facilityName: '',
			facilityType: '',
			location: '',
			description: '',
			reporterName: '',
			reporterPhone: '',
		};
	}
};

const openAssign = (row: any) => {
	state.assignForm = { id: row.id, assignee: '' };
	state.assignVisible = true;
};

const openComplete = (row: any) => {
	state.completeForm = { id: row.id, repairResult: '' };
	state.completeVisible = true;
};

const submitForm = () => {
	if (!state.form.facilityName) {
		ElMessage.error('请输入设施名称');
		return;
	}
	if (!state.form.facilityType) {
		ElMessage.error('请选择设施类型');
		return;
	}
	if (!state.form.location) {
		ElMessage.error('请输入报修位置');
		return;
	}
	if (!state.form.description) {
		ElMessage.error('请输入报修描述');
		return;
	}
	const api = state.dialogType === 'add' ? repairApi.create : repairApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const submitAssign = () => {
	if (!state.assignForm.assignee) {
		ElMessage.error('请输入派单人员');
		return;
	}
	repairApi.assign(state.assignForm.id, state.assignForm.assignee).then(() => {
		ElMessage.success('派单成功');
		state.assignVisible = false;
		getTableData();
	});
};

const submitComplete = () => {
	if (!state.completeForm.repairResult) {
		ElMessage.error('请输入维修结果');
		return;
	}
	repairApi.complete(state.completeForm.id, state.completeForm.repairResult).then(() => {
		ElMessage.success('完成维修');
		state.completeVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该报修记录，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => repairApi.delete(row.id))
		.then(() => {
			ElMessage.success('删除成功');
			getTableData();
		})
		.catch(() => null);
};

const getStatusType = (status: number) => {
	if (status === 0) return 'warning';
	if (status === 1) return 'primary';
	if (status === 2) return 'success';
	return 'info';
};

const getStatusText = (status: number) => {
	if (status === 0) return '待派单';
	if (status === 1) return '维修中';
	if (status === 2) return '已完成';
	return '';
};

const onHandleSizeChange = (val: number) => {
	state.tableData.param.page.size = val;
	getTableData();
};

const onHandleCurrentChange = (val: number) => {
	state.tableData.param.page.current = val;
	getTableData();
};

const exportRepair = () => {
	const params: any = {};
	if (state.tableData.param.query.facilityName) params.facilityName = state.tableData.param.query.facilityName;
	if (state.tableData.param.query.facilityType) params.facilityType = state.tableData.param.query.facilityType;
	if (state.tableData.param.query.status !== '' && state.tableData.param.query.status !== null) params.status = state.tableData.param.query.status;
	repairApi.export(params);
};

onMounted(() => {
	userInfo.setUserInfos().then(() => {
		getTableData();
	});
});
</script>

<style scoped lang="scss">
.vms-repair {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

