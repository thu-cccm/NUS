<template>
	<div class="vms-vote layout-padding">
			<el-card shadow="hover" class="layout-padding-auto">
				<div class="vms-vote-header">
					<div class="vms-vote-title">民主议事投票</div>
					<div class="vms-vote-actions">
						<el-input v-model="state.query.keyword" placeholder="请输入议题关键词" clearable class="vote-search" />
						<el-select v-model="state.query.status" placeholder="状态" clearable class="vote-filter">
							<el-option label="进行中" :value="0" />
							<el-option label="已结束" :value="1" />
						</el-select>
						<el-button type="primary" @click="getPageList">查询</el-button>
						<el-button @click="resetQuery">重置</el-button>
						<el-button v-if="isAdmin" type="primary" @click="openDialog('add')">发起投票</el-button>
					</div>
				</div>
				<el-row :gutter="15" v-loading="state.loading">
				<el-col :xs="24" :sm="8" v-for="item in state.list" :key="item.id" class="mb15">
					<el-card shadow="hover" class="vote-card">
						<div class="vote-title">{{ item.title }}</div>
						<div class="vote-content">{{ item.content }}</div>
						<div class="vote-meta">
							<span>截止：{{ formatDate(item.endTime) }}</span>
							<div class="vote-tags">
								<el-tag v-if="item.isAnonymous === 1" type="info">匿名</el-tag>
								<el-tag :type="item.status === 1 || isExpired(item.endTime) ? 'info' : 'success'">
									{{ item.status === 1 || isExpired(item.endTime) ? '已结束' : '进行中' }}
								</el-tag>
							</div>
						</div>
						<div class="vote-stats">
							<span>赞成 {{ item.agreeCount }}</span>
							<span>反对 {{ item.disagreeCount }}</span>
						</div>
						<div class="vote-footer">
							<el-button size="small" text type="primary" @click="openResult(item)">查看结果</el-button>
							<div class="vote-actions">
								<template v-if="!isAdmin">
									<el-button
										size="small"
										type="success"
										:disabled="item.status === 1 || item.voted || isExpired(item.endTime)"
										@click="onVote(item.id, true)"
									>
										赞成
									</el-button>
									<el-button
										size="small"
										type="danger"
										:disabled="item.status === 1 || item.voted || isExpired(item.endTime)"
										@click="onVote(item.id, false)"
									>
										反对
									</el-button>
								</template>
								<template v-else>
									<el-button size="small" type="warning" @click="endVote(item.id)">结束</el-button>
									<el-button size="small" type="primary" @click="openDialog('edit', item)">编辑</el-button>
									<el-button size="small" type="danger" @click="removeVote(item.id)">删除</el-button>
								</template>
							</div>
						</div>
					</el-card>
				</el-col>
				<el-col v-if="state.list.length === 0" :span="24">
					<el-empty description="暂无相关记录" />
				</el-col>
			</el-row>
			<el-pagination
				v-if="state.total > 0"
				class="mt15"
				@size-change="onHandleSizeChange"
				@current-change="onHandleCurrentChange"
				:pager-count="5"
				:page-sizes="[10, 20, 30]"
				v-model:current-page="state.page.current"
				background
				v-model:page-size="state.page.size"
				layout="total, sizes, prev, pager, next, jumper"
				:total="state.total"
			/>
		</el-card>

		<el-dialog v-model="state.dialogVisible" :title="state.dialogTitle" width="520px">
			<el-form :model="state.form">
				<el-form-item label="议题标题">
					<el-input v-model="state.form.title" placeholder="请输入议题标题" />
				</el-form-item>
				<el-form-item label="议题内容">
					<el-input v-model="state.form.content" type="textarea" rows="4" placeholder="请输入议题说明" />
				</el-form-item>
				<el-form-item label="匿名投票">
					<el-switch v-model="state.form.isAnonymous" :active-value="1" :inactive-value="0" active-text="匿名" inactive-text="实名" />
				</el-form-item>
				<el-form-item label="截止时间">
					<el-date-picker v-model="state.form.endTime" type="datetime" placeholder="请选择截止时间" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-dialog v-model="state.resultVisible" title="投票结果" width="720px">
			<el-tabs v-model="state.resultTab">
				<el-tab-pane label="结果概览" name="summary">
					<div class="result-summary">
						<el-card shadow="hover" class="summary-card">
							<div class="summary-title">总票数</div>
							<div class="summary-value">{{ state.resultSummary.total }}</div>
						</el-card>
						<el-card shadow="hover" class="summary-card">
							<div class="summary-title">赞成票</div>
							<div class="summary-value">{{ state.resultSummary.agree }}</div>
						</el-card>
						<el-card shadow="hover" class="summary-card">
							<div class="summary-title">反对票</div>
							<div class="summary-value">{{ state.resultSummary.disagree }}</div>
						</el-card>
						<el-card shadow="hover" class="summary-card">
							<div class="summary-title">赞成率</div>
							<div class="summary-value">{{ state.resultSummary.rate }}%</div>
						</el-card>
					</div>
					<div ref="resultChartRef" class="result-chart"></div>
					<div class="trend-toolbar">
						<el-select v-model="state.trendUnit" size="small" class="trend-select" @change="onTrendUnitChange">
							<el-option label="按天" value="day" />
							<el-option label="按周" value="week" />
							<el-option label="按月" value="month" />
						</el-select>
						<el-button size="small" :type="state.trendDays === 7 ? 'primary' : 'default'" @click="changeTrendDays(7)">近7天</el-button>
						<el-button size="small" :type="state.trendDays === 30 ? 'primary' : 'default'" @click="changeTrendDays(30)">近30天</el-button>
						<el-button size="small" :type="state.trendDays === 90 ? 'primary' : 'default'" @click="changeTrendDays(90)">近90天</el-button>
					</div>
					<div ref="trendChartRef" class="result-chart mt20"></div>
				</el-tab-pane>
				<el-tab-pane label="投票记录" name="records">
					<div v-if="!isAdmin" class="result-tip">仅管理员可查看投票记录明细</div>
					<div v-else class="record-toolbar">
						<el-select v-model="state.recordFilter.agree" placeholder="全部" clearable size="small" class="record-filter">
							<el-option label="赞成" :value="true" />
							<el-option label="反对" :value="false" />
						</el-select>
						<el-input v-model="state.recordFilter.keyword" placeholder="请输入姓名" clearable size="small" class="record-filter" />
						<el-select v-model="state.recordPage.size" size="small" class="record-filter" @change="onRecordSizeChange">
							<el-option label="10条/页" :value="10" />
							<el-option label="20条/页" :value="20" />
							<el-option label="50条/页" :value="50" />
						</el-select>
						<el-button size="small" type="primary" @click="refreshRecords">查询</el-button>
						<el-button size="small" @click="resetRecordFilter">清空</el-button>
						<el-button size="small" @click="exportRecords">导出CSV</el-button>
					</div>
					<div v-if="isAdmin" class="record-summary">
						<el-tag type="info">总票数 {{ recordSummary.total }}</el-tag>
						<el-tag type="success">赞成 {{ recordSummary.agree }}</el-tag>
						<el-tag type="danger">反对 {{ recordSummary.disagree }}</el-tag>
						<span class="record-total">共 {{ state.recordTotal }} 条记录</span>
					</div>
					<el-table v-else :data="state.recordList" v-loading="state.recordLoading" style="width: 100%">
						<el-table-column prop="residentName" label="姓名" min-width="120" />
						<el-table-column prop="agree" label="投票结果" width="120">
							<template #default="scope">
								<el-tag :type="scope.row.agree ? 'success' : 'danger'">
									{{ scope.row.agree ? '赞成' : '反对' }}
								</el-tag>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="投票时间" width="180">
							<template #default="scope">{{ formatDate(scope.row.createTime) }}</template>
						</el-table-column>
						<template #empty>
							<el-empty description="暂无相关记录" />
						</template>
					</el-table>
					<el-pagination
						v-if="isAdmin && state.recordTotal > 0"
						class="mt10"
						:pager-count="5"
						:page-size="state.recordPage.size"
						:current-page="state.recordPage.current"
						:total="state.recordTotal"
						layout="total, prev, pager, next"
						@current-change="onRecordPageChange"
					/>
				</el-tab-pane>
			</el-tabs>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="vmsVote">
import { computed, onMounted, onBeforeUnmount, reactive, ref, nextTick } from 'vue';
import * as echarts from 'echarts';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsVoteApi } from '/@/api/vms/vote';
import { useUserInfo } from '/@/stores/userInfo';
import { parseDateTime } from '/@/utils/formatTime';

const voteApi = useVmsVoteApi();
const userInfo = useUserInfo();

const isAdmin = computed(() => userInfo.userInfos.roles?.includes('1'));

const state = reactive({
	list: [] as Array<any>,
	total: 0,
	page: {
		current: 1,
		size: 9,
	},
	query: {
		keyword: '',
		status: undefined as undefined | number,
	},
	loading: false,
	dialogVisible: false,
	dialogTitle: '发起投票',
	dialogType: 'add',
	resultVisible: false,
	resultTab: 'summary',
	currentVoteId: undefined as undefined | number,
	currentVoteAnonymous: false,
	recordList: [] as Array<any>,
	recordLoading: false,
	trendData: [] as Array<any>,
	trendDays: 7,
	trendUnit: 'day',
	recordFilter: {
		agree: undefined as undefined | boolean,
		keyword: '',
	},
	recordPage: {
		current: 1,
		size: 10,
	},
	recordTotal: 0,
	recordSummary: {
		total: 0,
		agree: 0,
		disagree: 0,
	},
	resultSummary: {
		total: 0,
		agree: 0,
		disagree: 0,
		rate: 0,
	},
	form: {
		id: undefined,
		title: '',
		content: '',
		isAnonymous: 0,
		endTime: '',
	},
});

const resultChartRef = ref<HTMLDivElement | null>(null);
const trendChartRef = ref<HTMLDivElement | null>(null);
let resultChart: echarts.ECharts | null = null;
let trendChart: echarts.ECharts | null = null;

const getPageList = () => {
	state.loading = true;
	voteApi
		.getPageList({
			page: { current: state.page.current, size: state.page.size },
			query: { title: state.query.keyword, status: state.query.status },
		})
		.then((res: any) => {
			state.list = res.records || [];
			state.total = res.total || 0;
			state.loading = false;
		});
};

const onVote = (id: number, agree: boolean) => {
	voteApi.castVote(id, agree).then(() => {
		ElMessage.success('投票成功');
		getPageList();
	});
};

const endVote = (id: number) => {
	voteApi.endVote(id).then(() => {
		ElMessage.success('已结束');
		getPageList();
	});
};

const removeVote = (id: number) => {
	ElMessageBox.confirm('确认删除该议题吗?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => voteApi.delete(id))
		.then(() => {
			ElMessage.success('删除成功');
			getPageList();
		})
		.catch(() => null);
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '发起投票' : '编辑投票';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = { ...row };
	} else {
		state.form = {
			id: undefined,
			title: '',
			content: '',
			isAnonymous: 0,
			endTime: '',
		};
	}
};

const submitForm = () => {
	if (!state.form.title) {
		ElMessage.error('请输入议题标题');
		return;
	}
	if (!state.form.content) {
		ElMessage.error('请输入议题内容');
		return;
	}
	if (!state.form.endTime) {
		ElMessage.error('请选择截止时间');
		return;
	}
	const api = state.dialogType === 'add' ? voteApi.create : voteApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		getPageList();
	});
};

const openResult = (row: any) => {
	state.resultVisible = true;
	state.resultTab = 'summary';
	state.currentVoteId = row.id;
	state.currentVoteAnonymous = row.isAnonymous === 1;
	state.resultSummary.total = (row.agreeCount || 0) + (row.disagreeCount || 0);
	state.resultSummary.agree = row.agreeCount || 0;
	state.resultSummary.disagree = row.disagreeCount || 0;
	state.resultSummary.rate = state.resultSummary.total === 0 ? 0 : Math.round((state.resultSummary.agree / state.resultSummary.total) * 100);
	nextTick(() => {
		if (!resultChartRef.value) return;
		resultChart?.dispose();
		resultChart = echarts.init(resultChartRef.value);
		resultChart.setOption({
			tooltip: { trigger: 'item' },
			series: [
				{
					name: row.title,
					type: 'pie',
					radius: ['45%', '70%'],
					label: { formatter: '{b}: {c} ({d}%)' },
					data: [
						{ value: row.agreeCount || 0, name: '赞成' },
						{ value: row.disagreeCount || 0, name: '反对' },
					],
				},
			],
		});
	});
	state.trendDays = 7;
	state.trendUnit = 'day';
	state.recordFilter = { agree: undefined, keyword: '' };
	state.recordPage = { current: 1, size: 10 };
	loadTrend(row.id, state.trendDays, state.trendUnit);
	loadRecords(row.id);
};

const formatDate = (value: string) => {
	if (!value) return '-';
	return parseDateTime(value);
};

const isExpired = (value: string) => {
	if (!value) return false;
	const time = new Date(value).getTime();
	if (Number.isNaN(time)) return false;
	return time < Date.now();
};

const recordSummary = computed(() => {
	return state.recordSummary || { total: 0, agree: 0, disagree: 0 };
});

const loadRecords = (id: number) => {
	if (!isAdmin.value) {
		state.recordList = [];
		return;
	}
	if (state.currentVoteAnonymous && state.recordFilter.keyword) {
		ElMessage.warning('匿名投票无法按姓名查询');
		state.recordFilter.keyword = '';
	}
	state.recordLoading = true;
	voteApi.getRecordPage({
		id,
		current: state.recordPage.current,
		size: state.recordPage.size,
		agree: state.recordFilter.agree,
		keyword: state.recordFilter.keyword,
	}).then((res: any) => {
		state.recordList = res?.records || [];
		state.recordTotal = res?.total || 0;
		state.recordSummary = {
			total: (res?.agreeCount || 0) + (res?.disagreeCount || 0),
			agree: res?.agreeCount || 0,
			disagree: res?.disagreeCount || 0,
		};
		state.recordLoading = false;
	});
};

const loadTrend = (id: number, days = 7, unit = 'day') => {
	voteApi.getTrend(id, days, unit).then((res: any) => {
		state.trendData = res || [];
		nextTick(() => {
			if (!trendChartRef.value) return;
			trendChart?.dispose();
			trendChart = echarts.init(trendChartRef.value);
			const labelFormatter = (value: string) => {
				if (unit === 'week') return `${value}周`;
				if (unit === 'month') return `${value}月`;
				return value;
			};
			trendChart.setOption({
				tooltip: { trigger: 'axis' },
				legend: { data: ['赞成', '反对'] },
				xAxis: {
					type: 'category',
					data: state.trendData.map((item) => item.day),
					axisLabel: { formatter: labelFormatter },
				},
				yAxis: { type: 'value' },
				series: [
					{
						name: '赞成',
						type: 'line',
						data: state.trendData.map((item) => item.agreeCount),
						itemStyle: { color: '#67C23A' },
					},
					{
						name: '反对',
						type: 'line',
						data: state.trendData.map((item) => item.disagreeCount),
						itemStyle: { color: '#F56C6C' },
					},
				],
			});
		});
	});
};

const changeTrendDays = (days: number) => {
	if (state.trendDays === days) return;
	state.trendDays = days;
	if (state.currentVoteId) {
		loadTrend(state.currentVoteId, state.trendDays, state.trendUnit);
	}
};

const onTrendUnitChange = () => {
	if (state.currentVoteId) {
		loadTrend(state.currentVoteId, state.trendDays, state.trendUnit);
	}
};

const refreshRecords = () => {
	if (state.currentVoteId) {
		state.recordPage.current = 1;
		loadRecords(state.currentVoteId);
	}
};

const resetRecordFilter = () => {
	state.recordFilter = { agree: undefined, keyword: '' };
	refreshRecords();
};

const onRecordPageChange = (val: number) => {
	state.recordPage.current = val;
	if (state.currentVoteId) {
		loadRecords(state.currentVoteId);
	}
};

const onRecordSizeChange = () => {
	state.recordPage.current = 1;
	if (state.currentVoteId) {
		loadRecords(state.currentVoteId);
	}
};

const exportRecords = () => {
	if (!state.recordTotal) {
		ElMessage.warning('暂无可导出记录');
		return;
	}
	if (!state.currentVoteId) return;
	if (state.currentVoteAnonymous && state.recordFilter.keyword) {
		ElMessage.warning('匿名投票无法按姓名导出');
		state.recordFilter.keyword = '';
	}
	voteApi.exportRecords({
		id: state.currentVoteId,
		agree: state.recordFilter.agree,
		keyword: state.recordFilter.keyword,
	}).then((res: any) => {
		const blob = new Blob([res], { type: 'text/csv;charset=utf-8;' });
		const link = document.createElement('a');
		link.href = URL.createObjectURL(blob);
		link.download = `vote-records-${state.currentVoteId}.csv`;
		link.click();
		URL.revokeObjectURL(link.href);
	});
};

onMounted(() => {
	userInfo.setUserInfos().then(() => {
		getPageList();
	});
});

const onHandleSizeChange = (val: number) => {
	state.page.size = val;
	state.page.current = 1;
	getPageList();
};

const onHandleCurrentChange = (val: number) => {
	state.page.current = val;
	getPageList();
};

const resetQuery = () => {
	state.query.keyword = '';
	state.query.status = undefined;
	state.page.current = 1;
	getPageList();
};

onBeforeUnmount(() => {
	resultChart?.dispose();
	trendChart?.dispose();
});
</script>

<style scoped lang="scss">
.vms-vote {
	.vms-vote-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 15px;
		.vms-vote-title {
			font-size: 16px;
			font-weight: 600;
		}
		.vms-vote-actions {
			display: flex;
			gap: 10px;
			align-items: center;
			.vote-search {
				width: 220px;
			}
			.vote-filter {
				width: 120px;
			}
		}
	}
	.vote-card {
		min-height: 220px;
		display: flex;
		flex-direction: column;
		gap: 10px;
		.vote-title {
			font-size: 16px;
			font-weight: 600;
		}
		.vote-content {
			color: var(--el-text-color-secondary);
			min-height: 60px;
		}
		.vote-meta {
			display: flex;
			justify-content: space-between;
			color: var(--el-text-color-secondary);
			.vote-tags {
				display: flex;
				gap: 6px;
			}
		}
		.vote-stats {
			display: flex;
			justify-content: space-between;
			color: var(--el-text-color-secondary);
		}
		.vote-footer {
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
		.vote-actions {
			display: flex;
			gap: 6px;
		}
	}
	.result-chart {
		height: 320px;
		width: 100%;
	}
	.result-tip {
		color: var(--el-text-color-secondary);
		font-size: 12px;
		padding: 10px 0;
	}
	.result-summary {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 10px;
		margin-bottom: 15px;
		.summary-card {
			text-align: center;
			.summary-title {
				color: var(--el-text-color-secondary);
				font-size: 12px;
			}
			.summary-value {
				font-size: 20px;
				font-weight: 600;
				margin-top: 6px;
			}
		}
	}
	.trend-toolbar {
		display: flex;
		gap: 8px;
		justify-content: flex-end;
		margin-bottom: 10px;
	}
	.record-toolbar {
		display: flex;
		gap: 8px;
		align-items: center;
		margin-bottom: 10px;
		.record-filter {
			width: 160px;
		}
	}
	.record-summary {
		display: flex;
		gap: 8px;
		margin-bottom: 10px;
		align-items: center;
		.record-total {
			color: var(--el-text-color-secondary);
			font-size: 12px;
		}
	}
}
</style>

