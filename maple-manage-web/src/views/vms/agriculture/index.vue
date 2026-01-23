<template>
	<div class="vms-agriculture layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-agriculture-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="季度">
						<el-input v-model="state.tableData.param.query.season" placeholder="如：2024春季" clearable />
					</el-form-item>
					<el-form-item label="作物">
						<el-input v-model="state.tableData.param.query.cropName" placeholder="如：水稻" clearable />
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="residentId" label="负责人ID" width="110" />
				<el-table-column prop="season" label="季度" width="120" />
				<el-table-column prop="cropName" label="作物名称" width="120" />
				<el-table-column prop="plantArea" label="种植面积(亩)" width="130" />
				<el-table-column prop="expectYield" label="预计产量(公斤)" width="150" />
				<el-table-column prop="marketPrice" label="预估单价" width="120" />
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
				<el-form-item label="负责人ID">
					<el-input-number v-model="state.form.residentId" :min="1" />
				</el-form-item>
				<el-form-item label="季度">
					<el-input v-model="state.form.season" />
				</el-form-item>
				<el-form-item label="作物名称">
					<el-input v-model="state.form.cropName" />
				</el-form-item>
				<el-form-item label="种植面积(亩)">
					<el-input-number v-model="state.form.plantArea" :min="0" :precision="2" />
				</el-form-item>
				<el-form-item label="预计产量(公斤)">
					<el-input-number v-model="state.form.expectYield" :min="0" :precision="2" />
				</el-form-item>
				<el-form-item label="预估单价">
					<el-input-number v-model="state.form.marketPrice" :min="0" :precision="2" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsAgriculture">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsAgricultureApi } from '/@/api/vms/agriculture';

const agricultureApi = useVmsAgricultureApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { season: '', cropName: '' },
		},
	},
	dialogVisible: false,
	dialogTitle: '新增生产记录',
	dialogType: 'add',
	form: {
		id: undefined,
		residentId: 1,
		season: '2024春季',
		cropName: '水稻',
		plantArea: 0,
		expectYield: 0,
		marketPrice: 0,
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	agricultureApi.getPageList(state.tableData.param).then((res: any) => {
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
	state.dialogTitle = type === 'add' ? '新增生产记录' : '编辑生产记录';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			residentId: 1,
			season: '2024春季',
			cropName: '水稻',
			plantArea: 0,
			expectYield: 0,
			marketPrice: 0,
		};
	}
};

const submitForm = () => {
	const api = state.dialogType === 'add' ? agricultureApi.create : agricultureApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该记录，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => agricultureApi.delete(row.id))
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
.vms-agriculture {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

