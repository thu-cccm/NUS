<template>
	<div class="vms-resident layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-resident-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="姓名">
						<el-input v-model="state.tableData.param.query.realName" placeholder="请输入姓名" clearable />
					</el-form-item>
					<el-form-item label="村组">
						<el-input v-model="state.tableData.param.query.groupNo" placeholder="请输入村组" clearable />
					</el-form-item>
					<el-form-item label="贫困户">
						<el-select v-model="state.tableData.param.query.isPoor" placeholder="请选择" clearable>
							<el-option label="是" :value="1" />
							<el-option label="否" :value="0" />
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
				<el-table-column prop="realName" label="姓名" min-width="120" />
				<el-table-column prop="gender" label="性别" width="80">
					<template #default="scope">
						<el-tag :type="scope.row.gender === 1 ? 'success' : 'info'">
							{{ scope.row.gender === 1 ? '男' : '女' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="age" label="年龄" width="80" />
				<el-table-column prop="phone" label="联系电话" min-width="140" />
				<el-table-column prop="groupNo" label="村组" width="90">
					<template #default="scope">
						<span>{{ scope.row.groupNo }} 组</span>
					</template>
				</el-table-column>
				<el-table-column prop="politics" label="政治面貌" width="120" />
				<el-table-column prop="isPoor" label="贫困户" width="100">
					<template #default="scope">
						<el-tag :type="scope.row.isPoor === 1 ? 'danger' : 'success'">
							{{ scope.row.isPoor === 1 ? '是' : '否' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="healthStatus" label="健康状况" width="120" />
				<el-table-column prop="createTime" label="建档时间" width="180">
					<template #default="scope">
						<span>{{ parseDateTime(scope.row.createTime) }}</span>
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
				<el-form-item label="姓名">
					<el-input v-model="state.form.realName" />
				</el-form-item>
				<el-form-item label="身份证号">
					<el-input v-model="state.form.idCard" />
				</el-form-item>
				<el-form-item label="性别">
					<el-radio-group v-model="state.form.gender">
						<el-radio :label="1">男</el-radio>
						<el-radio :label="0">女</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="年龄">
					<el-input-number v-model="state.form.age" :min="1" :max="120" />
				</el-form-item>
				<el-form-item label="联系电话">
					<el-input v-model="state.form.phone" />
				</el-form-item>
				<el-form-item label="村组">
					<el-input-number v-model="state.form.groupNo" :min="1" :max="10" />
				</el-form-item>
				<el-form-item label="政治面貌">
					<el-select v-model="state.form.politics">
						<el-option label="群众" value="群众" />
						<el-option label="党员" value="党员" />
						<el-option label="团员" value="团员" />
					</el-select>
				</el-form-item>
				<el-form-item label="贫困户">
					<el-radio-group v-model="state.form.isPoor">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="0">否</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="健康状况">
					<el-select v-model="state.form.healthStatus">
						<el-option label="健康" value="健康" />
						<el-option label="慢性病" value="慢性病" />
						<el-option label="残疾" value="残疾" />
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

<script setup lang="ts" name="vmsResident">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsResidentApi } from '/@/api/vms/resident';
import { parseDateTime } from '/@/utils/formatTime';

const residentApi = useVmsResidentApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: {
				realName: '',
				groupNo: '',
				isPoor: '',
			},
		},
	},
	dialogVisible: false,
	dialogTitle: '新增居民',
	dialogType: 'add',
	form: {
		id: undefined,
		realName: '',
		idCard: '',
		gender: 1,
		age: 30,
		phone: '',
		groupNo: 1,
		politics: '群众',
		isPoor: 0,
		healthStatus: '健康',
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	residentApi.getPageList(state.tableData.param).then((res: any) => {
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
	state.dialogTitle = type === 'add' ? '新增居民' : '编辑居民';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			realName: '',
			idCard: '',
			gender: 1,
			age: 30,
			phone: '',
			groupNo: 1,
			politics: '群众',
			isPoor: 0,
			healthStatus: '健康',
		};
	}
};

const submitForm = () => {
	const api = state.dialogType === 'add' ? residentApi.create : residentApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该居民信息，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => residentApi.delete(row.id))
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
.vms-resident {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

