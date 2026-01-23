<template>
	<div class="vms-party-activity layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-party-activity-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="活动标题">
						<el-input v-model="state.tableData.param.query.title" placeholder="请输入活动标题" clearable />
					</el-form-item>
					<el-form-item label="活动类型">
						<el-select v-model="state.tableData.param.query.type" placeholder="请选择" clearable>
							<el-option label="三会一课" value="三会一课" />
							<el-option label="主题党日" value="主题党日" />
							<el-option label="其他" value="其他" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增活动</el-button>
				<el-button @click="exportActivity">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="title" label="活动标题" min-width="200" />
				<el-table-column prop="type" label="活动类型" width="120" />
				<el-table-column prop="holdTime" label="开展时间" width="160">
					<template #default="scope">
						{{ scope.row.holdTime ? parseDateTime(scope.row.holdTime) : '-' }}
					</template>
				</el-table-column>
				<el-table-column prop="location" label="活动地点" width="150" show-overflow-tooltip />
				<el-table-column prop="participants" label="参会人员" min-width="200" show-overflow-tooltip />
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

		<el-dialog v-model="state.dialogVisible" :title="state.dialogTitle" width="800px">
			<el-form :model="state.form" label-width="120px">
				<el-form-item label="活动标题" required>
					<el-input v-model="state.form.title" placeholder="请输入活动标题" />
				</el-form-item>
				<el-form-item label="活动类型" required>
					<el-select v-model="state.form.type" placeholder="请选择" style="width: 100%">
						<el-option label="三会一课" value="三会一课" />
						<el-option label="主题党日" value="主题党日" />
						<el-option label="其他" value="其他" />
					</el-select>
				</el-form-item>
				<el-form-item label="开展时间">
					<el-date-picker v-model="state.form.holdTime" type="datetime" placeholder="选择时间" style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
				</el-form-item>
				<el-form-item label="活动地点">
					<el-input v-model="state.form.location" placeholder="请输入活动地点" />
				</el-form-item>
				<el-form-item label="参会人员">
					<el-input v-model="state.form.participants" type="textarea" :rows="3" placeholder="请输入参会人员，多个用逗号分隔" />
				</el-form-item>
				<el-form-item label="会议纪要">
					<el-input v-model="state.form.summary" type="textarea" :rows="5" placeholder="请输入会议纪要" />
				</el-form-item>
				<el-form-item label="活动照片">
					<el-input v-model="state.form.photos" type="textarea" :rows="3" placeholder="请输入照片URL，多个用逗号分隔" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-drawer v-model="state.detailVisible" title="党建活动详情" size="60%">
			<div v-if="state.detailData" class="activity-detail">
				<el-descriptions :column="2" border>
					<el-descriptions-item label="活动标题" :span="2">{{ state.detailData.title }}</el-descriptions-item>
					<el-descriptions-item label="活动类型">{{ state.detailData.type }}</el-descriptions-item>
					<el-descriptions-item label="开展时间">{{ state.detailData.holdTime ? parseDateTime(state.detailData.holdTime) : '-' }}</el-descriptions-item>
					<el-descriptions-item label="活动地点" :span="2">{{ state.detailData.location || '-' }}</el-descriptions-item>
					<el-descriptions-item label="参会人员" :span="2">{{ state.detailData.participants || '-' }}</el-descriptions-item>
					<el-descriptions-item label="会议纪要" :span="2">
						<div style="white-space: pre-wrap;">{{ state.detailData.summary || '-' }}</div>
					</el-descriptions-item>
				</el-descriptions>
				<div v-if="state.detailData.photoList && state.detailData.photoList.length > 0" class="photo-wall">
					<h3>活动照片</h3>
					<el-row :gutter="15">
						<el-col v-for="(photo, index) in state.detailData.photoList" :key="index" :span="6" class="mb15">
							<el-image :src="photo" :preview-src-list="state.detailData.photoList" :initial-index="index" fit="cover" style="width: 100%; height: 150px; border-radius: 4px;" />
						</el-col>
					</el-row>
				</div>
			</div>
		</el-drawer>
	</div>
</template>

<script setup lang="ts" name="vmsPartyActivity">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsPartyActivityApi } from '/@/api/vms/partyActivity';
import { parseDateTime } from '/@/utils/formatTime';

const activityApi = useVmsPartyActivityApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { title: '', type: '' },
		},
	},
	dialogVisible: false,
	detailVisible: false,
	dialogTitle: '新增党建活动',
	dialogType: 'add',
	detailData: null,
	form: {
		id: undefined,
		title: '',
		type: '',
		holdTime: null,
		location: '',
		participants: '',
		summary: '',
		photos: '',
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	activityApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	state.tableData.param.query = { title: '', type: '' };
	nextTick(() => getTableData());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增党建活动' : '编辑党建活动';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = {
			id: row.id,
			title: row.title,
			type: row.type,
			holdTime: row.holdTime,
			location: row.location || '',
			participants: row.participants || '',
			summary: row.summary || '',
			photos: row.photos || '',
		};
	} else {
		state.form = {
			id: undefined,
			title: '',
			type: '',
			holdTime: null,
			location: '',
			participants: '',
			summary: '',
			photos: '',
		};
	}
};

const openDetail = (row: any) => {
	state.detailData = row;
	state.detailVisible = true;
};

const submitForm = () => {
	if (!state.form.title) {
		ElMessage.error('请输入活动标题');
		return;
	}
	if (!state.form.type) {
		ElMessage.error('请选择活动类型');
		return;
	}
	// 处理参会人员和照片列表
	const formData: any = { ...state.form };
	if (state.form.participants) {
		formData.participantList = state.form.participants.split(',').map((s: string) => s.trim()).filter((s: string) => s);
	}
	if (state.form.photos) {
		formData.photoList = state.form.photos.split(',').map((s: string) => s.trim()).filter((s: string) => s);
	}
	const api = state.dialogType === 'add' ? activityApi.create : activityApi.update;
	api(formData).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该活动记录，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => activityApi.delete(row.id))
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

const exportActivity = () => {
	const params: any = {};
	if (state.tableData.param.query.title) params.title = state.tableData.param.query.title;
	if (state.tableData.param.query.type) params.type = state.tableData.param.query.type;
	activityApi.export(params);
};

onMounted(() => {
	getTableData();
});
</script>

<style scoped lang="scss">
.vms-party-activity {
	.el-form {
		margin-bottom: 10px;
	}
	.activity-detail {
		.photo-wall {
			margin-top: 20px;
			h3 {
				margin-bottom: 15px;
			}
		}
	}
}
</style>

