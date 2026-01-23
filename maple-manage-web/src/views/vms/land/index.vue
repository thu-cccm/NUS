<template>
	<div class="vms-land layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-land-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="地块编号">
						<el-input v-model="state.tableData.param.query.landCode" placeholder="请输入编号" clearable />
					</el-form-item>
					<el-form-item label="土地类型">
						<el-select v-model="state.tableData.param.query.type" placeholder="请选择" clearable>
							<el-option label="水田" value="水田" />
							<el-option label="旱地" value="旱地" />
							<el-option label="林地" value="林地" />
							<el-option label="宅基地" value="宅基地" />
						</el-select>
					</el-form-item>
					<el-form-item label="确权状态">
						<el-select v-model="state.tableData.param.query.status" placeholder="请选择" clearable>
							<el-option label="待确权" :value="0" />
							<el-option label="已确权" :value="1" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增</el-button>
				<el-button @click="exportLand">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="landCode" label="地块编号" min-width="150" />
				<el-table-column prop="residentId" label="户主ID" width="100" />
				<el-table-column prop="area" label="面积(亩)" width="110" />
				<el-table-column prop="type" label="类型" width="110" />
				<el-table-column prop="location" label="位置描述" min-width="180" show-overflow-tooltip />
				<el-table-column prop="status" label="确权状态" width="120">
					<template #default="scope">
						<el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
							{{ scope.row.status === 1 ? '已确权' : '待确权' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="certUrl" label="确权证书" min-width="160">
					<template #default="scope">
						<el-link v-if="scope.row.certUrl" :href="scope.row.certUrl" target="_blank">查看证书</el-link>
						<span v-else>-</span>
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
				<el-form-item label="户主ID">
					<el-input-number v-model="state.form.residentId" :min="1" />
				</el-form-item>
				<el-form-item label="地块编号">
					<el-input v-model="state.form.landCode" />
				</el-form-item>
				<el-form-item label="面积(亩)">
					<el-input-number v-model="state.form.area" :min="0" :precision="2" />
				</el-form-item>
				<el-form-item label="土地类型">
					<el-select v-model="state.form.type">
						<el-option label="水田" value="水田" />
						<el-option label="旱地" value="旱地" />
						<el-option label="林地" value="林地" />
						<el-option label="宅基地" value="宅基地" />
					</el-select>
				</el-form-item>
				<el-form-item label="位置描述">
					<el-input v-model="state.form.location" />
				</el-form-item>
				<el-form-item label="确权状态">
					<el-radio-group v-model="state.form.status">
						<el-radio :label="0">待确权</el-radio>
						<el-radio :label="1">已确权</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="证书链接">
					<el-input v-model="state.form.certUrl" placeholder="可粘贴上传后的链接" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsLand">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsLandApi } from '/@/api/vms/land';

const landApi = useVmsLandApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { landCode: '', type: '', status: '' },
		},
	},
	dialogVisible: false,
	dialogTitle: '新增土地',
	dialogType: 'add',
	form: {
		id: undefined,
		residentId: 1,
		landCode: '',
		area: 0,
		type: '水田',
		location: '',
		status: 0,
		certUrl: '',
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	landApi.getPageList(state.tableData.param).then((res: any) => {
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
	state.dialogTitle = type === 'add' ? '新增土地' : '编辑土地';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			residentId: 1,
			landCode: '',
			area: 0,
			type: '水田',
			location: '',
			status: 0,
			certUrl: '',
		};
	}
};

const submitForm = () => {
	const api = state.dialogType === 'add' ? landApi.create : landApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该地块信息，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => landApi.delete(row.id))
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

const exportLand = () => {
	const params: any = {};
	if (state.tableData.param.query.landCode) params.landCode = state.tableData.param.query.landCode;
	if (state.tableData.param.query.type) params.type = state.tableData.param.query.type;
	if (state.tableData.param.query.status !== '' && state.tableData.param.query.status !== null) params.status = state.tableData.param.query.status;
	landApi.export(params);
};

onMounted(() => {
	getTableData();
});
</script>

<style scoped lang="scss">
.vms-land {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

