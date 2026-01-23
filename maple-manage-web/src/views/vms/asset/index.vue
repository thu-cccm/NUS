<template>
	<div class="vms-asset layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-asset-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="资产名称">
						<el-input v-model="state.tableData.param.query.assetName" placeholder="请输入资产名称" clearable />
					</el-form-item>
					<el-form-item label="资产类型">
						<el-select v-model="state.tableData.param.query.assetType" placeholder="请选择" clearable>
							<el-option label="农机" value="农机" />
							<el-option label="房产" value="房产" />
							<el-option label="设施" value="设施" />
							<el-option label="其他" value="其他" />
						</el-select>
					</el-form-item>
					<el-form-item label="状态">
						<el-select v-model="state.tableData.param.query.status" placeholder="请选择" clearable>
							<el-option label="正常" value="正常" />
							<el-option label="维修" value="维修" />
							<el-option label="报废" value="报废" />
						</el-select>
					</el-form-item>
					<el-form-item label="保管人">
						<el-input v-model="state.tableData.param.query.keeper" placeholder="请输入保管人" clearable />
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增</el-button>
				<el-button @click="exportAsset">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="assetName" label="资产名称" min-width="150" />
				<el-table-column prop="assetType" label="资产类型" width="100" />
				<el-table-column prop="originalValue" label="原值(元)" width="120">
					<template #default="scope">
						{{ scope.row.originalValue ? '¥' + scope.row.originalValue.toLocaleString() : '-' }}
					</template>
				</el-table-column>
				<el-table-column prop="purchaseDate" label="购买日期" width="120">
					<template #default="scope">
						{{ scope.row.purchaseDate ? parseDate(scope.row.purchaseDate) : '-' }}
					</template>
				</el-table-column>
				<el-table-column prop="keeper" label="保管人" width="100" />
				<el-table-column prop="status" label="状态" width="100">
					<template #default="scope">
						<el-tag :type="getStatusType(scope.row.status)">
							{{ scope.row.status }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="lessee" label="承租人" width="100" />
				<el-table-column prop="contractEnd" label="合同到期" width="120">
					<template #default="scope">
						<span v-if="scope.row.contractEnd" :style="{ color: scope.row.isExpiringSoon ? 'red' : '' }">
							{{ parseDate(scope.row.contractEnd) }}
							<el-tag v-if="scope.row.isExpiringSoon" type="danger" size="small" style="margin-left: 5px">即将到期</el-tag>
						</span>
						<span v-else>-</span>
					</template>
				</el-table-column>
				<el-table-column prop="rentalAmount" label="租金(元/月)" width="120">
					<template #default="scope">
						{{ scope.row.rentalAmount ? '¥' + scope.row.rentalAmount.toLocaleString() : '-' }}
					</template>
				</el-table-column>
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
				<el-form-item label="资产名称" required>
					<el-input v-model="state.form.assetName" placeholder="请输入资产名称" />
				</el-form-item>
				<el-form-item label="资产类型" required>
					<el-select v-model="state.form.assetType" placeholder="请选择" style="width: 100%">
						<el-option label="农机" value="农机" />
						<el-option label="房产" value="房产" />
						<el-option label="设施" value="设施" />
						<el-option label="其他" value="其他" />
					</el-select>
				</el-form-item>
				<el-form-item label="原值(元)">
					<el-input-number v-model="state.form.originalValue" :min="0" :precision="2" style="width: 100%" />
				</el-form-item>
				<el-form-item label="购买日期">
					<el-date-picker v-model="state.form.purchaseDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
				</el-form-item>
				<el-form-item label="保管人">
					<el-input v-model="state.form.keeper" placeholder="请输入保管人" />
				</el-form-item>
				<el-form-item label="状态" required>
					<el-select v-model="state.form.status" placeholder="请选择" style="width: 100%">
						<el-option label="正常" value="正常" />
						<el-option label="维修" value="维修" />
						<el-option label="报废" value="报废" />
					</el-select>
				</el-form-item>
				<el-form-item label="存放位置">
					<el-input v-model="state.form.location" placeholder="请输入存放位置" />
				</el-form-item>
				<el-form-item label="资产描述">
					<el-input v-model="state.form.description" type="textarea" :rows="3" placeholder="请输入资产描述" />
				</el-form-item>
				<el-divider>租赁信息（可选）</el-divider>
				<el-form-item label="承租人">
					<el-input v-model="state.form.lessee" placeholder="请输入承租人" />
				</el-form-item>
				<el-form-item label="合同开始日期">
					<el-date-picker v-model="state.form.contractStart" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
				</el-form-item>
				<el-form-item label="合同结束日期">
					<el-date-picker v-model="state.form.contractEnd" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
				</el-form-item>
				<el-form-item label="租金(元/月)">
					<el-input-number v-model="state.form.rentalAmount" :min="0" :precision="2" style="width: 100%" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsAsset">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsAssetApi } from '/@/api/vms/asset';
import { parseDate } from '/@/utils/formatTime';

const assetApi = useVmsAssetApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { assetName: '', assetType: '', status: '', keeper: '' },
		},
	},
	dialogVisible: false,
	dialogTitle: '新增资产',
	dialogType: 'add',
	form: {
		id: undefined,
		assetName: '',
		assetType: '',
		originalValue: null,
		purchaseDate: null,
		keeper: '',
		status: '正常',
		location: '',
		description: '',
		contractStart: null,
		contractEnd: null,
		lessee: '',
		rentalAmount: null,
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	assetApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	state.tableData.param.query = { assetName: '', assetType: '', status: '', keeper: '' };
	nextTick(() => getTableData());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增资产' : '编辑资产';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			assetName: '',
			assetType: '',
			originalValue: null,
			purchaseDate: null,
			keeper: '',
			status: '正常',
			location: '',
			description: '',
			contractStart: null,
			contractEnd: null,
			lessee: '',
			rentalAmount: null,
		};
	}
};

const submitForm = () => {
	if (!state.form.assetName) {
		ElMessage.error('请输入资产名称');
		return;
	}
	if (!state.form.assetType) {
		ElMessage.error('请选择资产类型');
		return;
	}
	if (!state.form.status) {
		ElMessage.error('请选择状态');
		return;
	}
	const api = state.dialogType === 'add' ? assetApi.create : assetApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该资产信息，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => assetApi.delete(row.id))
		.then(() => {
			ElMessage.success('删除成功');
			getTableData();
		})
		.catch(() => null);
};

const getStatusType = (status: string) => {
	if (status === '正常') return 'success';
	if (status === '维修') return 'warning';
	if (status === '报废') return 'danger';
	return 'info';
};

const onHandleSizeChange = (val: number) => {
	state.tableData.param.page.size = val;
	getTableData();
};

const onHandleCurrentChange = (val: number) => {
	state.tableData.param.page.current = val;
	getTableData();
};

const exportAsset = () => {
	const params: any = {};
	if (state.tableData.param.query.assetName) params.assetName = state.tableData.param.query.assetName;
	if (state.tableData.param.query.assetType) params.assetType = state.tableData.param.query.assetType;
	if (state.tableData.param.query.status) params.status = state.tableData.param.query.status;
	assetApi.export(params);
};

onMounted(() => {
	getTableData();
});
</script>

<style scoped lang="scss">
.vms-asset {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

