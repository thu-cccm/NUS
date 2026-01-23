<template>
	<div class="vms-party-score layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-party-score-header">
				<div class="score-ranking">
					<h3>党员积分排行榜（前10名）</h3>
					<el-table :data="state.ranking" stripe style="width: 100%; margin-bottom: 20px;">
						<el-table-column type="index" label="排名" width="80">
							<template #default="scope">
								<span :style="{ color: scope.$index < 3 ? '#F56C6C' : '' }">
									{{ scope.$index + 1 }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="residentName" label="党员姓名" width="150" />
						<el-table-column prop="totalScore" label="总积分" width="120">
							<template #default="scope">
								<el-tag type="success" size="large">{{ scope.row.totalScore }}</el-tag>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
			<div class="vms-party-score-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="党员ID">
						<el-input-number v-model="state.tableData.param.query.residentId" :min="1" placeholder="请输入党员ID" clearable />
					</el-form-item>
					<el-form-item label="积分类型">
						<el-select v-model="state.tableData.param.query.scoreType" placeholder="请选择" clearable>
							<el-option label="志愿服务" value="志愿服务" />
							<el-option label="学习活动" value="学习活动" />
							<el-option label="其他" value="其他" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增积分</el-button>
				<el-button @click="exportScore">导出CSV</el-button>
			</el-row>
			<el-table :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="residentName" label="党员姓名" width="120" />
				<el-table-column prop="scoreType" label="积分类型" width="120" />
				<el-table-column prop="score" label="积分值" width="100">
					<template #default="scope">
						<el-tag type="success">{{ scope.row.score || 0 }}</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="description" label="积分说明" min-width="200" show-overflow-tooltip />
				<el-table-column prop="createTime" label="创建时间" width="160">
					<template #default="scope">
						{{ scope.row.createTime ? parseDateTime(scope.row.createTime) : '-' }}
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
				<el-form-item label="党员ID" required>
					<el-input-number v-model="state.form.residentId" :min="1" style="width: 100%" />
				</el-form-item>
				<el-form-item label="积分类型" required>
					<el-select v-model="state.form.scoreType" placeholder="请选择" style="width: 100%">
						<el-option label="志愿服务" value="志愿服务" />
						<el-option label="学习活动" value="学习活动" />
						<el-option label="其他" value="其他" />
					</el-select>
				</el-form-item>
				<el-form-item label="积分值" required>
					<el-input-number v-model="state.form.score" :min="0" style="width: 100%" />
				</el-form-item>
				<el-form-item label="积分说明">
					<el-input v-model="state.form.description" type="textarea" :rows="4" placeholder="请输入积分说明" />
				</el-form-item>
				<el-form-item label="关联活动ID">
					<el-input-number v-model="state.form.activityId" :min="0" style="width: 100%" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsPartyScore">
import { onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsPartyScoreApi } from '/@/api/vms/partyScore';
import { parseDateTime } from '/@/utils/formatTime';

const scoreApi = useVmsPartyScoreApi();
const queryRef = ref();

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { residentId: null, scoreType: '' },
		},
	},
	ranking: [],
	dialogVisible: false,
	dialogTitle: '新增积分',
	dialogType: 'add',
	form: {
		id: undefined,
		residentId: null,
		scoreType: '',
		score: 0,
		description: '',
		activityId: null,
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	scoreApi.getPageList(state.tableData.param).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const getRanking = () => {
	scoreApi.getRanking(10).then((res: any) => {
		state.ranking = res || [];
	});
};

const resetQuery = () => {
	state.tableData.param.query = { residentId: null, scoreType: '' };
	nextTick(() => getTableData());
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增积分' : '编辑积分';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			residentId: null,
			scoreType: '',
			score: 0,
			description: '',
			activityId: null,
		};
	}
};

const submitForm = () => {
	if (!state.form.residentId) {
		ElMessage.error('请输入党员ID');
		return;
	}
	if (!state.form.scoreType) {
		ElMessage.error('请选择积分类型');
		return;
	}
	if (state.form.score === null || state.form.score < 0) {
		ElMessage.error('请输入有效的积分值');
		return;
	}
	const api = state.dialogType === 'add' ? scoreApi.create : scoreApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getTableData();
		getRanking(); // 刷新排行榜
	});
};

const onRowDel = (row: any) => {
	ElMessageBox.confirm('此操作将永久删除该积分记录，是否继续?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => scoreApi.delete(row.id))
		.then(() => {
			ElMessage.success('删除成功');
			getTableData();
			getRanking(); // 刷新排行榜
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

const exportScore = () => {
	const params: any = {};
	if (state.tableData.param.query.residentId) params.residentId = state.tableData.param.query.residentId;
	if (state.tableData.param.query.scoreType) params.scoreType = state.tableData.param.query.scoreType;
	scoreApi.export(params);
};

onMounted(() => {
	getTableData();
	getRanking();
});
</script>

<style scoped lang="scss">
.vms-party-score {
	.el-form {
		margin-bottom: 10px;
	}
	.vms-party-score-header {
		margin-bottom: 20px;
		.score-ranking {
			background: #f5f7fa;
			padding: 15px;
			border-radius: 4px;
			h3 {
				margin: 0 0 15px 0;
				color: #303133;
			}
		}
	}
}
</style>

