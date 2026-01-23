<template>
	<div class="vms-dispute layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-dispute-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="当事人A">
						<el-input v-model="state.tableData.param.query.partyA" placeholder="请输入当事人A" clearable />
					</el-form-item>
					<el-form-item label="矛盾类型">
						<el-select v-model="state.tableData.param.query.conflictType" placeholder="请选择" clearable>
							<el-option label="土地" value="土地" />
							<el-option label="邻里" value="邻里" />
							<el-option label="家庭" value="家庭" />
							<el-option label="其他" value="其他" />
						</el-select>
					</el-form-item>
					<el-form-item label="状态">
						<el-select v-model="state.tableData.param.query.status" placeholder="请选择" clearable>
							<el-option label="待处理" :value="0" />
							<el-option label="处理中" :value="1" />
							<el-option label="已结案" :value="2" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增</el-button>
				<el-button @click="exportDispute">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="partyA" label="当事人A" width="120" />
				<el-table-column prop="partyB" label="当事人B" width="120" />
				<el-table-column prop="conflictType" label="矛盾类型" width="100" />
				<el-table-column prop="happenTime" label="发生时间" width="160">
					<template #default="scope">
						{{ scope.row.happenTime ? parseDateTime(scope.row.happenTime) : '-' }}
					</template>
				</el-table-column>
				<el-table-column prop="description" label="纠纷描述" min-width="200" show-overflow-tooltip />
				<el-table-column prop="mediator" label="调解员" width="100" />
				<el-table-column prop="status" label="状态" width="100">
					<template #default="scope">
						<el-tag :type="getStatusType(scope.row.status)">
							{{ getStatusText(scope.row.status) }}
						</el-tag>
					</template>
				</el-table-column>
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
				<el-form-item label="当事人A" required>
					<el-input v-model="state.form.partyA" placeholder="请输入当事人A" />
				</el-form-item>
				<el-form-item label="当事人B">
					<el-input v-model="state.form.partyB" placeholder="请输入当事人B" />
				</el-form-item>
				<el-form-item label="矛盾类型" required>
					<el-select v-model="state.form.conflictType" placeholder="请选择" style="width: 100%">
						<el-option label="土地" value="土地" />
						<el-option label="邻里" value="邻里" />
						<el-option label="家庭" value="家庭" />
						<el-option label="其他" value="其他" />
					</el-select>
				</el-form-item>
				<el-form-item label="发生时间">
					<el-date-picker v-model="state.form.happenTime" type="datetime" placeholder="选择时间" style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
				</el-form-item>
				<el-form-item label="纠纷描述" required>
					<el-input v-model="state.form.description" type="textarea" :rows="4" placeholder="请输入纠纷描述" />
				</el-form-item>
				<el-form-item label="调解员">
					<el-input v-model="state.form.mediator" placeholder="请输入调解员" />
				</el-form-item>
				<el-form-item label="状态" required>
					<el-radio-group v-model="state.form.status">
						<el-radio :label="0">待处理</el-radio>
						<el-radio :label="1">处理中</el-radio>
						<el-radio :label="2">已结案</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="调解结果">
					<el-input v-model="state.form.resultContent" type="textarea" :rows="4" placeholder="请输入调解结果" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-drawer v-model="state.detailVisible" title="矛盾纠纷详情" size="50%">
			<el-descriptions :column="2" border v-if="state.detailData">
				<el-descriptions-item label="当事人A">{{ state.detailData.partyA }}</el-descriptions-item>
				<el-descriptions-item label="当事人B">{{ state.detailData.partyB || '-' }}</el-descriptions-item>
				<el-descriptions-item label="矛盾类型">{{ state.detailData.conflictType }}</el-descriptions-item>
				<el-descriptions-item label="状态">
					<el-tag :type="getStatusType(state.detailData.status)">{{ getStatusText(state.detailData.status) }}</el-tag>
				</el-descriptions-item>
				<el-descriptions-item label="发生时间" :span="2">
					{{ state.detailData.happenTime ? parseDateTime(state.detailData.happenTime) : '-' }}
				</el-descriptions-item>
				<el-descriptions-item label="纠纷描述" :span="2">
					{{ state.detailData.description || '-' }}
				</el-descriptions-item>
				<el-descriptions-item label="调解员">{{ state.detailData.mediator || '-' }}</el-descriptions-item>
				<el-descriptions-item label="创建时间">{{ state.detailData.createTime ? parseDateTime(state.detailData.createTime) : '-' }}</el-descriptions-item>
				<el-descriptions-item label="调解结果" :span="2">
					{{ state.detailData.resultContent || '-' }}
				</el-descriptions-item>
			</el-descriptions>
		</el-drawer>
	</div>
</template>

<script setup lang="ts" name="vmsDispute">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsDisputeApi } from '/@/api/vms/dispute';
import { parseDateTime } from '/@/utils/formatTime';

const disputeApi = useVmsDisputeApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { partyA: '', conflictType: '', status: '' },
		},
	},
	dialogVisible: false,
	detailVisible: false,
	dialogTitle: '新增矛盾纠纷',
	dialogType: 'add',
	detailData: null,
	form: {
		id: undefined,
		partyA: '',
		partyB: '',
		conflictType: '',
		happenTime: null,
		description: '',
		mediator: '',
		status: 0,
		resultContent: '',
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	disputeApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	state.tableData.param.query = { partyA: '', conflictType: '', status: '' };
	nextTick(() => getTableData());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增矛盾纠纷' : '编辑矛盾纠纷';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			partyA: '',
			partyB: '',
			conflictType: '',
			happenTime: null,
			description: '',
			mediator: '',
			status: 0,
			resultContent: '',
		};
	}
};

const openDetail = (row: any) => {
	state.detailData = row;
	state.detailVisible = true;
};

const submitForm = () => {
	if (!state.form.partyA) {
		ElMessage.error('请输入当事人A');
		return;
	}
	if (!state.form.conflictType) {
		ElMessage.error('请选择矛盾类型');
		return;
	}
	if (!state.form.description) {
		ElMessage.error('请输入纠纷描述');
		return;
	}
	const api = state.dialogType === 'add' ? disputeApi.create : disputeApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该矛盾纠纷记录，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => disputeApi.delete(row.id))
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
	if (status === 0) return '待处理';
	if (status === 1) return '处理中';
	if (status === 2) return '已结案';
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

const exportDispute = () => {
	const params: any = {};
	if (state.tableData.param.query.partyA) params.partyA = state.tableData.param.query.partyA;
	if (state.tableData.param.query.conflictType) params.conflictType = state.tableData.param.query.conflictType;
	if (state.tableData.param.query.status !== '' && state.tableData.param.query.status !== null) params.status = state.tableData.param.query.status;
	disputeApi.export(params);
};

onMounted(() => {
	getTableData();
});
</script>

<style scoped lang="scss">
.vms-dispute {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

