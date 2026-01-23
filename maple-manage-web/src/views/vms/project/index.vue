<template>
	<div class="vms-project layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-project-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="项目名称">
						<el-input v-model="state.tableData.param.query.projectName" placeholder="请输入项目名称" clearable />
					</el-form-item>
					<el-form-item label="项目类型">
						<el-select v-model="state.tableData.param.query.projectType" placeholder="请选择" clearable>
							<el-option label="修路" value="修路" />
							<el-option label="水利" value="水利" />
							<el-option label="路灯" value="路灯" />
							<el-option label="其他" value="其他" />
						</el-select>
					</el-form-item>
					<el-form-item label="状态">
						<el-select v-model="state.tableData.param.query.status" placeholder="请选择" clearable>
							<el-option label="规划" value="规划" />
							<el-option label="施工中" value="施工中" />
							<el-option label="验收" value="验收" />
							<el-option label="维保" value="维保" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增</el-button>
				<el-button @click="exportProject">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="projectName" label="项目名称" min-width="150" />
				<el-table-column prop="projectType" label="项目类型" width="100" />
				<el-table-column prop="budget" label="预算(元)" width="120">
					<template #default="scope">
						{{ scope.row.budget ? '¥' + scope.row.budget.toLocaleString() : '-' }}
					</template>
				</el-table-column>
				<el-table-column prop="contractor" label="施工方" width="120" show-overflow-tooltip />
				<el-table-column prop="startDate" label="开工日期" width="120">
					<template #default="scope">
						{{ scope.row.startDate ? parseDate(scope.row.startDate) : '-' }}
					</template>
				</el-table-column>
				<el-table-column prop="progress" label="工程进度" width="200">
					<template #default="scope">
						<el-progress :percentage="scope.row.progress || 0" :status="getProgressStatus(scope.row.progress)" />
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态" width="100">
					<template #default="scope">
						<el-tag :type="getStatusType(scope.row.status)">
							{{ scope.row.status }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="200" fixed="right">
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

		<el-dialog v-model="state.dialogVisible" :title="state.dialogTitle" width="700px">
			<el-form :model="state.form" label-width="120px">
				<el-form-item label="项目名称" required>
					<el-input v-model="state.form.projectName" placeholder="请输入项目名称" />
				</el-form-item>
				<el-form-item label="项目类型" required>
					<el-select v-model="state.form.projectType" placeholder="请选择" style="width: 100%">
						<el-option label="修路" value="修路" />
						<el-option label="水利" value="水利" />
						<el-option label="路灯" value="路灯" />
						<el-option label="其他" value="其他" />
					</el-select>
				</el-form-item>
				<el-form-item label="预算(元)">
					<el-input-number v-model="state.form.budget" :min="0" :precision="2" style="width: 100%" />
				</el-form-item>
				<el-form-item label="施工方">
					<el-input v-model="state.form.contractor" placeholder="请输入施工方" />
				</el-form-item>
				<el-form-item label="开工日期">
					<el-date-picker v-model="state.form.startDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
				</el-form-item>
				<el-form-item label="预计完工日期">
					<el-date-picker v-model="state.form.endDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
				</el-form-item>
				<el-form-item label="工程进度(%)" required>
					<el-slider v-model="state.form.progress" :min="0" :max="100" show-input />
				</el-form-item>
				<el-form-item label="状态" required>
					<el-select v-model="state.form.status" placeholder="请选择" style="width: 100%">
						<el-option label="规划" value="规划" />
						<el-option label="施工中" value="施工中" />
						<el-option label="验收" value="验收" />
						<el-option label="维保" value="维保" />
					</el-select>
				</el-form-item>
				<el-form-item label="项目位置">
					<el-input v-model="state.form.location" placeholder="请输入项目位置" />
				</el-form-item>
				<el-form-item label="项目描述">
					<el-input v-model="state.form.description" type="textarea" :rows="4" placeholder="请输入项目描述" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsProject">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsProjectApi } from '/@/api/vms/project';
import { parseDate } from '/@/utils/formatTime';

const projectApi = useVmsProjectApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { projectName: '', projectType: '', status: '' },
		},
	},
	dialogVisible: false,
	dialogTitle: '新增工程项目',
	dialogType: 'add',
	form: {
		id: undefined,
		projectName: '',
		projectType: '',
		budget: null,
		contractor: '',
		startDate: null,
		endDate: null,
		progress: 0,
		status: '规划',
		description: '',
		location: '',
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	projectApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	state.tableData.param.query = { projectName: '', projectType: '', status: '' };
	nextTick(() => getTableData());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增工程项目' : '编辑工程项目';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			projectName: '',
			projectType: '',
			budget: null,
			contractor: '',
			startDate: null,
			endDate: null,
			progress: 0,
			status: '规划',
			description: '',
			location: '',
		};
	}
};

const submitForm = () => {
	if (!state.form.projectName) {
		ElMessage.error('请输入项目名称');
		return;
	}
	if (!state.form.projectType) {
		ElMessage.error('请选择项目类型');
		return;
	}
	if (!state.form.status) {
		ElMessage.error('请选择状态');
		return;
	}
	const api = state.dialogType === 'add' ? projectApi.create : projectApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该项目信息，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => projectApi.delete(row.id))
		.then(() => {
			ElMessage.success('删除成功');
			getTableData();
		})
		.catch(() => null);
};

const getStatusType = (status: string) => {
	if (status === '规划') return 'info';
	if (status === '施工中') return 'primary';
	if (status === '验收') return 'warning';
	if (status === '维保') return 'success';
	return 'info';
};

const getProgressStatus = (progress: number) => {
	if (progress === 100) return 'success';
	if (progress >= 80) return null;
	return null;
};

const onHandleSizeChange = (val: number) => {
	state.tableData.param.page.size = val;
	getTableData();
};

const onHandleCurrentChange = (val: number) => {
	state.tableData.param.page.current = val;
	getTableData();
};

const exportProject = () => {
	const params: any = {};
	if (state.tableData.param.query.projectName) params.projectName = state.tableData.param.query.projectName;
	if (state.tableData.param.query.projectType) params.projectType = state.tableData.param.query.projectType;
	if (state.tableData.param.query.status) params.status = state.tableData.param.query.status;
	projectApi.export(params);
};

onMounted(() => {
	getTableData();
});
</script>

<style scoped lang="scss">
.vms-project {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

