<template>
	<div class="vms-infrastructure layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-infrastructure-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="项目名称">
						<el-input v-model="state.tableData.param.query.projectName" placeholder="请输入项目名称" clearable />
					</el-form-item>
					<el-form-item label="状态">
						<el-select v-model="state.tableData.param.query.status" placeholder="请选择" clearable>
							<el-option label="规划中" value="规划中" />
							<el-option label="施工中" value="施工中" />
							<el-option label="已验收" value="已验收" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="success" plain @click="openDialog('add')">新增</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="projectName" label="项目名称" min-width="200" />
				<el-table-column prop="budget" label="预算(元)" width="120" />
				<el-table-column prop="startDate" label="开工日期" width="140">
					<template #default="scope">
						<span>{{ parseDateTime(scope.row.startDate) }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="contractor" label="施工单位" width="160" />
				<el-table-column label="进度" width="160">
					<template #default="scope">
						<el-progress :percentage="scope.row.progress || 0" :stroke-width="14" />
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态" width="120">
					<template #default="scope">
						<el-tag :type="statusTag(scope.row.status)">
							{{ scope.row.status }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="160">
					<template #default="scope">
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

		<el-dialog v-model="state.dialogVisible" :title="state.dialogTitle" width="520px">
			<el-form :model="state.form">
				<el-form-item label="项目名称">
					<el-input v-model="state.form.projectName" />
				</el-form-item>
				<el-form-item label="预算(元)">
					<el-input-number v-model="state.form.budget" :min="0" :precision="2" />
				</el-form-item>
				<el-form-item label="开工日期">
					<el-date-picker v-model="state.form.startDate" type="date" placeholder="选择日期" />
				</el-form-item>
				<el-form-item label="施工单位">
					<el-input v-model="state.form.contractor" />
				</el-form-item>
				<el-form-item label="进度">
					<el-slider v-model="state.form.progress" :min="0" :max="100" />
				</el-form-item>
				<el-form-item label="状态">
					<el-select v-model="state.form.status">
						<el-option label="规划中" value="规划中" />
						<el-option label="施工中" value="施工中" />
						<el-option label="已验收" value="已验收" />
					</el-select>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsInfrastructure">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsInfrastructureApi } from '/@/api/vms/infrastructure';
import { parseDateTime } from '/@/utils/formatTime';

const infrastructureApi = useVmsInfrastructureApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { projectName: '', status: '' },
		},
	},
	dialogVisible: false,
	dialogTitle: '新增项目',
	dialogType: 'add',
	form: {
		id: undefined,
		projectName: '',
		budget: 0,
		startDate: '',
		contractor: '',
		progress: 0,
		status: '规划中',
	},
});

const statusTag = (status: string) => {
	if (status === '已验收') return 'success';
	if (status === '施工中') return 'warning';
	return 'info';
};

const getTableData = () => {
	state.tableData.loading = true;
	infrastructureApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records;
		state.tableData.total = res.total;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	nextTick(() => queryRef.value?.resetFields());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增项目' : '编辑项目';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			projectName: '',
			budget: 0,
			startDate: '',
			contractor: '',
			progress: 0,
			status: '规划中',
		};
	}
};

const submitForm = () => {
	const api = state.dialogType === 'add' ? infrastructureApi.create : infrastructureApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该项目，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => infrastructureApi.delete(row.id))
		.then(() => {
			ElMessage.success('删除成功');
			getTableData();
		})
		.catch(() => null);
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
	getTableData();
});
</script>

<style scoped lang="scss">
.vms-infrastructure {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

