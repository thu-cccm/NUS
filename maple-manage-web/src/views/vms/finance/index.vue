<template>
	<div class="vms-finance layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-finance-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="类型">
						<el-select v-model="state.tableData.param.query.financeType" placeholder="请选择" clearable>
							<el-option label="收入" value="收入" />
							<el-option label="支出" value="支出" />
						</el-select>
					</el-form-item>
					<el-form-item label="分类">
						<el-select v-model="state.tableData.param.query.category" placeholder="请选择" clearable>
							<el-option label="租金收入" value="租金收入" />
							<el-option label="工程支出" value="工程支出" />
							<el-option label="补贴收入" value="补贴收入" />
							<el-option label="其他" value="其他" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增</el-button>
				<el-button @click="exportFinance">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="financeType" label="类型" width="100">
					<template #default="scope">
						<el-tag :type="scope.row.financeType === '收入' ? 'success' : 'danger'">
							{{ scope.row.financeType }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="category" label="分类" width="120" />
				<el-table-column prop="amount" label="金额(元)" width="150">
					<template #default="scope">
						<span :style="{ color: scope.row.financeType === '收入' ? '#67C23A' : '#F56C6C' }">
							{{ scope.row.financeType === '收入' ? '+' : '-' }}¥{{ scope.row.amount ? scope.row.amount.toLocaleString() : '0' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="description" label="说明" min-width="200" show-overflow-tooltip />
				<el-table-column prop="transactionDate" label="交易日期" width="120">
					<template #default="scope">
						{{ scope.row.transactionDate ? parseDate(scope.row.transactionDate) : '-' }}
					</template>
				</el-table-column>
				<el-table-column prop="payer" label="付款方/收款方" width="150" show-overflow-tooltip />
				<el-table-column prop="voucherNo" label="凭证号" width="120" />
				<el-table-column label="操作" width="160" fixed="right">
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

		<el-dialog v-model="state.dialogVisible" :title="state.dialogTitle" width="600px">
			<el-form :model="state.form" label-width="120px">
				<el-form-item label="类型" required>
					<el-radio-group v-model="state.form.financeType">
						<el-radio label="收入">收入</el-radio>
						<el-radio label="支出">支出</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="分类" required>
					<el-select v-model="state.form.category" placeholder="请选择" style="width: 100%">
						<el-option label="租金收入" value="租金收入" />
						<el-option label="工程支出" value="工程支出" />
						<el-option label="补贴收入" value="补贴收入" />
						<el-option label="其他" value="其他" />
					</el-select>
				</el-form-item>
				<el-form-item label="金额(元)" required>
					<el-input-number v-model="state.form.amount" :min="0" :precision="2" style="width: 100%" />
				</el-form-item>
				<el-form-item label="交易日期" required>
					<el-date-picker v-model="state.form.transactionDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
				</el-form-item>
				<el-form-item label="说明">
					<el-input v-model="state.form.description" type="textarea" :rows="3" placeholder="请输入说明" />
				</el-form-item>
				<el-form-item label="付款方/收款方">
					<el-input v-model="state.form.payer" placeholder="请输入付款方/收款方" />
				</el-form-item>
				<el-form-item label="凭证号">
					<el-input v-model="state.form.voucherNo" placeholder="请输入凭证号" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsFinance">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsFinanceApi } from '/@/api/vms/finance';
import { parseDate } from '/@/utils/formatTime';

const financeApi = useVmsFinanceApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { financeType: '', category: '' },
		},
	},
	dialogVisible: false,
	dialogTitle: '新增财务流水',
	dialogType: 'add',
	form: {
		id: undefined,
		financeType: '收入',
		category: '',
		amount: null,
		description: '',
		transactionDate: null,
		payer: '',
		voucherNo: '',
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	financeApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	state.tableData.param.query = { financeType: '', category: '' };
	nextTick(() => getTableData());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增财务流水' : '编辑财务流水';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			financeType: '收入',
			category: '',
			amount: null,
			description: '',
			transactionDate: null,
			payer: '',
			voucherNo: '',
		};
	}
};

const submitForm = () => {
	if (!state.form.financeType) {
		ElMessage.error('请选择类型');
		return;
	}
	if (!state.form.category) {
		ElMessage.error('请选择分类');
		return;
	}
	if (!state.form.amount || state.form.amount <= 0) {
		ElMessage.error('请输入有效的金额');
		return;
	}
	if (!state.form.transactionDate) {
		ElMessage.error('请选择交易日期');
		return;
	}
	const api = state.dialogType === 'add' ? financeApi.create : financeApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该财务流水记录，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => financeApi.delete(row.id))
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

const exportFinance = () => {
	const params: any = {};
	if (state.tableData.param.query.financeType) params.financeType = state.tableData.param.query.financeType;
	if (state.tableData.param.query.category) params.category = state.tableData.param.query.category;
	financeApi.export(params);
};

onMounted(() => {
	getTableData();
});
</script>

<style scoped lang="scss">
.vms-finance {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

