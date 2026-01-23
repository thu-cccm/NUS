<template>
	<div class="vms-feedback layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-feedback-header">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="关键词">
						<el-input v-model="state.tableData.param.query.content" placeholder="请输入关键词" clearable />
					</el-form-item>
					<el-form-item label="公开状态">
						<el-select v-model="state.tableData.param.query.isPublic" placeholder="请选择" clearable>
							<el-option label="公开" :value="1" />
							<el-option label="不公开" :value="0" />
						</el-select>
					</el-form-item>
					<el-form-item label="回复状态">
						<el-select v-model="state.tableData.param.query.hasReply" placeholder="请选择" clearable>
							<el-option label="已回复" :value="true" />
							<el-option label="未回复" :value="false" />
						</el-select>
					</el-form-item>
					<el-form-item v-if="isAdmin" label="隐藏状态">
						<el-select v-model="state.tableData.param.query.isHidden" placeholder="请选择" clearable>
							<el-option label="正常" :value="0" />
							<el-option label="已隐藏" :value="1" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			<div class="feedback-actions">
				<el-button v-if="isAdmin" @click="exportFeedback">导出CSV</el-button>
				<el-button type="primary" @click="openDialog">我要留言</el-button>
			</div>
			</div>

			<el-row :gutter="15" v-loading="state.tableData.loading">
				<el-col v-for="item in state.tableData.records" :key="item.id" :xs="24" :sm="12" class="mb15">
					<el-card shadow="hover" class="feedback-card">
						<div class="feedback-content">{{ item.content }}</div>
						<div class="feedback-meta">
							<span>{{ parseDateTime(item.createTime) }}</span>
							<el-tag :type="item.isPublic === 1 ? 'success' : 'info'">
								{{ item.isPublic === 1 ? '公开' : '不公开' }}
							</el-tag>
							<el-tag :type="item.replyContent ? 'success' : 'warning'">
								{{ item.replyContent ? '已回复' : '未回复' }}
							</el-tag>
							<el-tag v-if="isAdmin" :type="item.isHidden === 1 ? 'danger' : 'success'">
								{{ item.isHidden === 1 ? '已隐藏' : '正常' }}
							</el-tag>
						</div>
						<div class="feedback-report" v-if="isAdmin && item.reportCount">
							举报次数：{{ item.reportCount }}
						</div>
						<div class="feedback-reply" v-if="item.replyContent">
							<div class="reply-label">官方回复：</div>
							<div class="reply-text">{{ item.replyContent }}</div>
							<div class="reply-time">{{ parseDateTime(item.replyTime) }}</div>
						</div>
						<div class="feedback-actions">
							<template v-if="isAdmin">
								<el-button size="small" text type="primary" @click="openDetail(item)">详情</el-button>
								<el-button size="small" text type="primary" @click="openReply(item)">回复</el-button>
								<el-button size="small" text type="warning" @click="toggleHide(item)">
									{{ item.isHidden === 1 ? '恢复' : '隐藏' }}
								</el-button>
								<el-button size="small" text type="danger" @click="removeFeedback(item)">删除</el-button>
							</template>
							<template v-else>
								<el-button size="small" text type="primary" @click="openDetail(item)">详情</el-button>
								<el-button size="small" text type="warning" @click="reportFeedback(item)">举报</el-button>
							</template>
						</div>
					</el-card>
				</el-col>
				<el-col v-if="state.tableData.records.length === 0" :span="24">
					<el-empty description="暂无相关记录" />
				</el-col>
			</el-row>

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

		<el-dialog v-model="state.dialogVisible" title="发布留言" width="480px">
			<el-form :model="state.form">
				<el-form-item label="留言内容">
					<el-input v-model="state.form.content" type="textarea" rows="4" placeholder="请输入留言内容" />
				</el-form-item>
				<el-form-item label="公开显示">
					<el-switch v-model="state.form.isPublic" :active-value="1" :inactive-value="0" active-text="公开" inactive-text="不公开" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">提交</el-button>
			</template>
		</el-dialog>

		<el-dialog v-model="state.replyVisible" title="回复留言" width="480px">
			<el-form :model="state.replyForm">
				<el-form-item label="留言内容">
					<el-input v-model="state.replyForm.content" type="textarea" rows="3" readonly />
				</el-form-item>
				<el-form-item label="回复内容">
					<el-input v-model="state.replyForm.reply" type="textarea" rows="4" placeholder="请输入回复内容" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.replyVisible = false">取消</el-button>
				<el-button type="primary" @click="submitReply">确认</el-button>
			</template>
		</el-dialog>

		<el-drawer v-model="state.detailVisible" title="留言详情" size="480px">
			<el-descriptions :column="1" border>
				<el-descriptions-item label="留言内容">{{ state.detail.content }}</el-descriptions-item>
				<el-descriptions-item label="留言时间">{{ parseDateTime(state.detail.createTime) }}</el-descriptions-item>
				<el-descriptions-item label="公开状态">{{ state.detail.isPublic === 1 ? '公开' : '不公开' }}</el-descriptions-item>
				<el-descriptions-item label="回复状态">{{ state.detail.replyContent ? '已回复' : '未回复' }}</el-descriptions-item>
				<el-descriptions-item v-if="isAdmin" label="隐藏状态">{{ state.detail.isHidden === 1 ? '已隐藏' : '正常' }}</el-descriptions-item>
				<el-descriptions-item v-if="isAdmin" label="举报次数">{{ state.detail.reportCount || 0 }}</el-descriptions-item>
				<el-descriptions-item label="官方回复">
					{{ state.detail.replyContent || '暂无回复' }}
				</el-descriptions-item>
				<el-descriptions-item label="回复时间">
					{{ state.detail.replyTime ? parseDateTime(state.detail.replyTime) : '暂无' }}
				</el-descriptions-item>
			</el-descriptions>
		</el-drawer>
	</div>
</template>

<script setup lang="ts" name="vmsFeedback">
import { computed, onMounted, reactive, ref, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsFeedbackApi } from '/@/api/vms/feedback';
import { useUserInfo } from '/@/stores/userInfo';
import { parseDateTime } from '/@/utils/formatTime';

const feedbackApi = useVmsFeedbackApi();
const userInfo = useUserInfo();
const queryRef = ref();

const isAdmin = computed(() => userInfo.userInfos.roles?.includes('1'));

const state = reactive({
	tableData: {
		records: [],
		total: 0,
		loading: false,
		param: {
			page: { current: 1, size: 10 },
			query: { content: '', isPublic: '', hasReply: '', isHidden: '' },
		},
	},
	dialogVisible: false,
	replyVisible: false,
	detailVisible: false,
	form: {
		content: '',
		isPublic: 1,
	},
	replyForm: {
		id: undefined,
		content: '',
		reply: '',
	},
	detail: {
		content: '',
		isPublic: 1,
		isHidden: 0,
		reportCount: 0,
		replyContent: '',
		replyTime: '',
		createTime: '',
	},
});

const getTableData = () => {
	state.tableData.loading = true;
	const query = { ...state.tableData.param.query } as Record<string, any>;
	Object.keys(query).forEach((key) => {
		if (query[key] === '' || query[key] === null || query[key] === undefined) {
			delete query[key];
		}
	});
	feedbackApi.getPageList({ ...state.tableData.param, query }).then((res: any) => {
		state.tableData.records = res.records || [];
		state.tableData.total = res.total || 0;
		state.tableData.loading = false;
	});
};

const resetQuery = () => {
	nextTick(() => queryRef.value?.resetFields());
};

const exportFeedback = () => {
	if (!isAdmin.value) return;
	const params: any = {};
	if (state.tableData.param.query.content) params.content = state.tableData.param.query.content;
	if (state.tableData.param.query.isPublic !== '') params.isPublic = state.tableData.param.query.isPublic;
	if (state.tableData.param.query.hasReply !== '') params.hasReply = state.tableData.param.query.hasReply;
	if (state.tableData.param.query.isHidden !== '') params.isHidden = state.tableData.param.query.isHidden;
	feedbackApi.export(params);
};

const openDialog = () => {
	state.form = { content: '', isPublic: 1 };
	state.dialogVisible = true;
};

const submitForm = () => {
	if (!state.form.content) {
		ElMessage.error('请输入留言内容');
		return;
	}
	feedbackApi.create(state.form).then(() => {
		ElMessage.success('提交成功');
		state.dialogVisible = false;
		getTableData();
	});
};

const openReply = (row: any) => {
	state.replyForm = { id: row.id, reply: row.replyContent || '', content: row.content || '' };
	state.replyVisible = true;
};

const submitReply = () => {
	if (!state.replyForm.reply) {
		ElMessage.error('请输入回复内容');
		return;
	}
	feedbackApi.reply(state.replyForm.id, state.replyForm.reply).then(() => {
		ElMessage.success('回复成功');
		state.replyVisible = false;
		getTableData();
	});
};

const toggleHide = (row: any) => {
	const hidden = row.isHidden !== 1;
	ElMessageBox.confirm(`确认${hidden ? '隐藏' : '恢复'}该留言吗?`, '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => feedbackApi.hide(row.id, hidden))
		.then(() => {
			ElMessage.success(hidden ? '已隐藏' : '已恢复');
			getTableData();
		})
		.catch(() => null);
};

const reportFeedback = (row: any) => {
	ElMessageBox.confirm('确认举报该留言吗?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => feedbackApi.report(row.id))
		.then(() => {
			ElMessage.success('举报已提交');
		})
		.catch(() => null);
};

const removeFeedback = (row: any) => {
	ElMessageBox.confirm('确认删除该留言吗?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => feedbackApi.delete(row.id))
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

const openDetail = (row: any) => {
	state.detail = {
		content: row.content || '',
		isPublic: row.isPublic ?? 1,
		isHidden: row.isHidden ?? 0,
		reportCount: row.reportCount ?? 0,
		replyContent: row.replyContent || '',
		replyTime: row.replyTime || '',
		createTime: row.createTime || '',
	};
	state.detailVisible = true;
};

onMounted(() => {
	userInfo.setUserInfos().then(() => {
		getTableData();
	});
});
</script>

<style scoped lang="scss">
.vms-feedback {
	.vms-feedback-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 10px;
	}
	.feedback-actions {
		display: flex;
		gap: 10px;
	}
	.feedback-card {
		min-height: 160px;
		display: flex;
		flex-direction: column;
		gap: 10px;
		.feedback-content {
			font-size: 14px;
		}
		.feedback-meta {
			display: flex;
			justify-content: space-between;
			gap: 10px;
			color: var(--el-text-color-secondary);
			flex-wrap: wrap;
		}
		.feedback-report {
			color: var(--el-text-color-secondary);
			font-size: 12px;
		}
		.feedback-reply {
			background: var(--el-color-primary-light-9);
			padding: 8px 10px;
			border-radius: 4px;
			.reply-label {
				font-weight: 600;
				margin-bottom: 4px;
			}
			.reply-time {
				font-size: 12px;
				color: var(--el-text-color-secondary);
				margin-top: 4px;
			}
		}
		.feedback-actions {
			display: flex;
			gap: 10px;
		}
	}
}
</style>

