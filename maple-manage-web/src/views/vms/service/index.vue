<template>
	<div class="vms-service layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-service-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="服务名称">
						<el-input v-model="state.tableData.param.query.serviceName" placeholder="请输入服务名称" clearable />
					</el-form-item>
					<el-form-item label="服务类型">
						<el-select v-model="state.tableData.param.query.serviceType" placeholder="请选择" clearable>
							<el-option label="医疗卫生" value="医疗卫生" />
							<el-option label="文化教育" value="文化教育" />
							<el-option label="就业招聘" value="就业招聘" />
						</el-select>
					</el-form-item>
					<el-form-item label="分类">
						<el-input v-model="state.tableData.param.query.category" placeholder="请输入分类" clearable />
					</el-form-item>
					<el-form-item label="状态">
						<el-select v-model="state.tableData.param.query.status" placeholder="请选择" clearable>
							<el-option label="启用" :value="1" />
							<el-option label="停用" :value="0" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增服务</el-button>
				<el-button @click="exportService">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="serviceName" label="服务名称" min-width="150" />
				<el-table-column prop="serviceType" label="服务类型" width="120">
					<template #default="scope">
						<el-tag :type="getServiceTypeColor(scope.row.serviceType)">
							{{ scope.row.serviceType }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="category" label="分类" width="120" />
				<el-table-column prop="contactPhone" label="联系电话" width="120" />
				<el-table-column prop="contactPerson" label="联系人" width="100" />
				<el-table-column prop="address" label="地址" min-width="150" show-overflow-tooltip />
				<el-table-column prop="status" label="状态" width="100">
					<template #default="scope">
						<el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
							{{ scope.row.status === 1 ? '启用' : '停用' }}
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
				<el-form-item label="服务名称" required>
					<el-input v-model="state.form.serviceName" placeholder="请输入服务名称" />
				</el-form-item>
				<el-form-item label="服务类型" required>
					<el-select v-model="state.form.serviceType" placeholder="请选择" style="width: 100%" @change="onServiceTypeChange">
						<el-option label="医疗卫生" value="医疗卫生" />
						<el-option label="文化教育" value="文化教育" />
						<el-option label="就业招聘" value="就业招聘" />
					</el-select>
				</el-form-item>
				<el-form-item label="分类" required>
					<el-input v-model="state.form.category" :placeholder="getCategoryPlaceholder()" />
				</el-form-item>
				<el-form-item label="联系电话">
					<el-input v-model="state.form.contactPhone" placeholder="请输入联系电话" />
				</el-form-item>
				<el-form-item label="联系人">
					<el-input v-model="state.form.contactPerson" placeholder="请输入联系人" />
				</el-form-item>
				<el-form-item label="地址">
					<el-input v-model="state.form.address" placeholder="请输入地址" />
				</el-form-item>
				<el-form-item label="服务描述">
					<el-input v-model="state.form.description" type="textarea" :rows="4" placeholder="请输入服务描述" />
				</el-form-item>
				<el-form-item :label="getScheduleLabel()">
					<el-input v-model="state.form.schedule" type="textarea" :rows="4" :placeholder="getSchedulePlaceholder()" />
				</el-form-item>
				<el-form-item label="状态" required>
					<el-radio-group v-model="state.form.status">
						<el-radio :label="1">启用</el-radio>
						<el-radio :label="0">停用</el-radio>
					</el-radio-group>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-drawer v-model="state.detailVisible" title="便民服务详情" size="50%">
			<el-descriptions :column="2" border v-if="state.detailData">
				<el-descriptions-item label="服务名称" :span="2">{{ state.detailData.serviceName }}</el-descriptions-item>
				<el-descriptions-item label="服务类型">
					<el-tag :type="getServiceTypeColor(state.detailData.serviceType)">
						{{ state.detailData.serviceType }}
					</el-tag>
				</el-descriptions-item>
				<el-descriptions-item label="状态">
					<el-tag :type="state.detailData.status === 1 ? 'success' : 'danger'">
						{{ state.detailData.status === 1 ? '启用' : '停用' }}
					</el-tag>
				</el-descriptions-item>
				<el-descriptions-item label="分类">{{ state.detailData.category || '-' }}</el-descriptions-item>
				<el-descriptions-item label="联系电话">{{ state.detailData.contactPhone || '-' }}</el-descriptions-item>
				<el-descriptions-item label="联系人">{{ state.detailData.contactPerson || '-' }}</el-descriptions-item>
				<el-descriptions-item label="地址" :span="2">{{ state.detailData.address || '-' }}</el-descriptions-item>
				<el-descriptions-item label="服务描述" :span="2">
					<div style="white-space: pre-wrap;">{{ state.detailData.description || '-' }}</div>
				</el-descriptions-item>
				<el-descriptions-item :label="getScheduleLabel()" :span="2">
					<div style="white-space: pre-wrap;">{{ state.detailData.schedule || '-' }}</div>
				</el-descriptions-item>
			</el-descriptions>
		</el-drawer>
	</div>
</template>

<script setup lang="ts" name="vmsService">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsServiceApi } from '/@/api/vms/service';

const serviceApi = useVmsServiceApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { serviceName: '', serviceType: '', category: '', status: '' },
		},
	},
	dialogVisible: false,
	detailVisible: false,
	dialogTitle: '新增便民服务',
	dialogType: 'add',
	detailData: null,
	form: {
		id: undefined,
		serviceName: '',
		serviceType: '',
		category: '',
		contactPhone: '',
		contactPerson: '',
		address: '',
		description: '',
		schedule: '',
		status: 1,
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	serviceApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	state.tableData.param.query = { serviceName: '', serviceType: '', category: '', status: '' };
	nextTick(() => getTableData());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增便民服务' : '编辑便民服务';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			serviceName: '',
			serviceType: '',
			category: '',
			contactPhone: '',
			contactPerson: '',
			address: '',
			description: '',
			schedule: '',
			status: 1,
		};
	}
};

const openDetail = (row: any) => {
	state.detailData = row;
	state.detailVisible = true;
};

const onServiceTypeChange = () => {
	// 根据服务类型自动填充分类提示
	if (state.form.serviceType === '医疗卫生') {
		state.form.category = state.form.category || '村卫生室';
	} else if (state.form.serviceType === '文化教育') {
		state.form.category = state.form.category || '小学';
	} else if (state.form.serviceType === '就业招聘') {
		state.form.category = state.form.category || '工厂';
	}
};

const getCategoryPlaceholder = () => {
	if (state.form.serviceType === '医疗卫生') return '如：村卫生室、卫生院等';
	if (state.form.serviceType === '文化教育') return '如：小学、农家书屋等';
	if (state.form.serviceType === '就业招聘') return '如：工厂、企业等';
	return '请输入分类';
};

const getScheduleLabel = () => {
	if (state.form.serviceType === '医疗卫生') return '坐诊医生排班';
	if (state.form.serviceType === '文化教育') return '开放时间/招生政策';
	if (state.form.serviceType === '就业招聘') return '招工信息';
	return '排班/开放时间/招工信息';
};

const getSchedulePlaceholder = () => {
	if (state.form.serviceType === '医疗卫生') return '如：周一至周五 8:00-17:00，张医生坐诊';
	if (state.form.serviceType === '文化教育') return '如：周一至周日 9:00-18:00 开放';
	if (state.form.serviceType === '就业招聘') return '如：招聘普工10名，月薪3000-5000元';
	return '请输入相关信息';
};

const getServiceTypeColor = (type: string) => {
	if (type === '医疗卫生') return 'success';
	if (type === '文化教育') return 'primary';
	if (type === '就业招聘') return 'warning';
	return 'info';
};

const submitForm = () => {
	if (!state.form.serviceName) {
		ElMessage.error('请输入服务名称');
		return;
	}
	if (!state.form.serviceType) {
		ElMessage.error('请选择服务类型');
		return;
	}
	if (!state.form.category) {
		ElMessage.error('请输入分类');
		return;
	}
	const api = state.dialogType === 'add' ? serviceApi.create : serviceApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该服务记录，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => serviceApi.delete(row.id))
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

const exportService = () => {
	const params: any = {};
	if (state.tableData.param.query.serviceName) params.serviceName = state.tableData.param.query.serviceName;
	if (state.tableData.param.query.serviceType) params.serviceType = state.tableData.param.query.serviceType;
	if (state.tableData.param.query.category) params.category = state.tableData.param.query.category;
	serviceApi.export(params);
};

onMounted(() => {
	getTableData();
});
</script>

<style scoped lang="scss">
.vms-service {
	.el-form {
		margin-bottom: 10px;
	}
}
</style>

