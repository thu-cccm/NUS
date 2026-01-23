<template>
	<div class="vms-hazard layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-hazard-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="隐患类型">
						<el-select v-model="state.tableData.param.query.hazardType" placeholder="请选择" clearable>
							<el-option label="危房" value="危房" />
							<el-option label="池塘无护栏" value="池塘无护栏" />
							<el-option label="其他" value="其他" />
						</el-select>
					</el-form-item>
					<el-form-item label="隐患位置">
						<el-input v-model="state.tableData.param.query.location" placeholder="请输入隐患位置" clearable />
					</el-form-item>
					<el-form-item label="状态">
						<el-select v-model="state.tableData.param.query.status" placeholder="请选择" clearable>
							<el-option label="待整改" :value="0" />
							<el-option label="整改中" :value="1" />
							<el-option label="已整改" :value="2" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增</el-button>
				<el-button @click="exportHazard">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="hazardType" label="隐患类型" width="120" />
				<el-table-column prop="location" label="隐患位置" min-width="150" show-overflow-tooltip />
				<el-table-column prop="description" label="隐患描述" min-width="200" show-overflow-tooltip />
				<el-table-column prop="reporter" label="上报人" width="100" />
				<el-table-column prop="status" label="状态" width="100">
					<template #default="scope">
						<el-tag :type="getStatusType(scope.row.status)">
							{{ getStatusText(scope.row.status) }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="rectifyTime" label="整改时间" width="160">
					<template #default="scope">
						{{ scope.row.rectifyTime ? parseDateTime(scope.row.rectifyTime) : '-' }}
					</template>
				</el-table-column>
				<el-table-column prop="rectifyResult" label="整改结果" min-width="150" show-overflow-tooltip />
				<el-table-column label="操作" width="200" fixed="right">
					<template #default="scope">
						<el-button size="small" text type="primary" @click="openDetail(scope.row)">详情</el-button>
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
				<el-form-item label="隐患类型" required>
					<el-select v-model="state.form.hazardType" placeholder="请选择" style="width: 100%">
						<el-option label="危房" value="危房" />
						<el-option label="池塘无护栏" value="池塘无护栏" />
						<el-option label="其他" value="其他" />
					</el-select>
				</el-form-item>
				<el-form-item label="隐患位置" required>
					<el-input v-model="state.form.location" placeholder="请输入隐患位置" />
				</el-form-item>
				<el-form-item label="隐患描述" required>
					<el-input v-model="state.form.description" type="textarea" :rows="4" placeholder="请输入隐患描述" />
				</el-form-item>
				<el-form-item label="上报人">
					<el-input v-model="state.form.reporter" placeholder="请输入上报人" />
				</el-form-item>
				<el-form-item label="状态" required>
					<el-radio-group v-model="state.form.status">
						<el-radio :label="0">待整改</el-radio>
						<el-radio :label="1">整改中</el-radio>
						<el-radio :label="2">已整改</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="整改时间">
					<el-date-picker v-model="state.form.rectifyTime" type="datetime" placeholder="选择时间" style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
				</el-form-item>
				<el-form-item label="整改结果">
					<el-input v-model="state.form.rectifyResult" type="textarea" :rows="4" placeholder="请输入整改结果" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-drawer v-model="state.detailVisible" title="安全隐患详情" size="50%">
			<el-descriptions :column="2" border v-if="state.detailData">
				<el-descriptions-item label="隐患类型">{{ state.detailData.hazardType }}</el-descriptions-item>
				<el-descriptions-item label="状态">
					<el-tag :type="getStatusType(state.detailData.status)">{{ getStatusText(state.detailData.status) }}</el-tag>
				</el-descriptions-item>
				<el-descriptions-item label="隐患位置" :span="2">{{ state.detailData.location }}</el-descriptions-item>
				<el-descriptions-item label="隐患描述" :span="2">{{ state.detailData.description || '-' }}</el-descriptions-item>
				<el-descriptions-item label="上报人">{{ state.detailData.reporter || '-' }}</el-descriptions-item>
				<el-descriptions-item label="创建时间">{{ state.detailData.createTime ? parseDateTime(state.detailData.createTime) : '-' }}</el-descriptions-item>
				<el-descriptions-item label="整改时间">{{ state.detailData.rectifyTime ? parseDateTime(state.detailData.rectifyTime) : '-' }}</el-descriptions-item>
				<el-descriptions-item label="整改结果" :span="2">{{ state.detailData.rectifyResult || '-' }}</el-descriptions-item>
			</el-descriptions>
		</el-drawer>
	</div>
</template>

<script setup lang="ts" name="vmsHazard">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsHazardApi } from '/@/api/vms/hazard';
import { parseDateTime } from '/@/utils/formatTime';

const hazardApi = useVmsHazardApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { hazardType: '', location: '', status: '' },
		},
	},
	dialogVisible: false,
	detailVisible: false,
	dialogTitle: '新增安全隐患',
	dialogType: 'add',
	detailData: null,
	form: {
		id: undefined,
		hazardType: '',
		location: '',
		description: '',
		reporter: '',
		status: 0,
		rectifyTime: null,
		rectifyResult: '',
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	hazardApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	state.tableData.param.query = { hazardType: '', location: '', status: '' };
	nextTick(() => getTableData());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增安全隐患' : '编辑安全隐患';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			hazardType: '',
			location: '',
			description: '',
			reporter: '',
			status: 0,
			rectifyTime: null,
			rectifyResult: '',
		};
	}
};

const openDetail = (row: any) => {
	state.detailData = row;
	state.detailVisible = true;
};

const submitForm = () => {
	if (!state.form.hazardType) {
		ElMessage.error('请选择隐患类型');
		return;
	}
	if (!state.form.location) {
		ElMessage.error('请输入隐患位置');
		return;
	}
	if (!state.form.description) {
		ElMessage.error('请输入隐患描述');
		return;
	}
	const api = state.dialogType === 'add' ? hazardApi.create : hazardApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该安全隐患记录，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => hazardApi.delete(row.id))
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
	if (status === 0) return '待整改';
	if (status === 1) return '整改中';
	if (status === 2) return '已整改';
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

const exportHazard = () => {
	const params: any = {};
	if (state.tableData.param.query.hazardType) params.hazardType = state.tableData.param.query.hazardType;
	if (state.tableData.param.query.location) params.location = state.tableData.param.query.location;
	if (state.tableData.param.query.status !== '' && state.tableData.param.query.status !== null) params.status = state.tableData.param.query.status;
	hazardApi.export(params);
};

onMounted(() => {
	getTableData();
});
</script>

<style scoped lang="scss">
.vms-hazard {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

