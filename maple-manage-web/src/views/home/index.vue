<template>
	<div class="dashboard layout-pd">
		<el-row :gutter="15">
			<el-col v-for="card in state.cards" :key="card.key" :xs="24" :sm="6" class="mb15">
				<el-card shadow="hover" class="dashboard-card">
					<div class="card-title">{{ card.label }}</div>
					<div class="card-value">{{ card.value }}</div>
					<div class="card-desc">{{ card.desc }}</div>
				</el-card>
			</el-col>
		</el-row>

		<el-row :gutter="15" class="mb15">
			<el-col :xs="24" :sm="12">
				<el-card shadow="hover" header="人口结构分布">
					<div ref="populationChartRef" class="chart"></div>
				</el-card>
			</el-col>
			<el-col :xs="24" :sm="12">
				<el-card shadow="hover" header="土地利用面积对比">
					<div ref="landAreaChartRef" class="chart"></div>
				</el-card>
			</el-col>
		</el-row>

		<el-row :gutter="15" class="mb15">
			<el-col :span="24">
				<el-card shadow="hover" header="办事效率趋势">
					<div ref="applyTrendChartRef" class="chart"></div>
				</el-card>
			</el-col>
		</el-row>

		<el-row :gutter="15">
			<el-col :xs="24" :sm="14">
				<el-card shadow="hover" header="最新待审核事务申请">
					<el-table :data="state.pendingList" border height="360">
						<el-table-column prop="applyType" label="申请类型" min-width="120" />
						<el-table-column prop="content" label="申请说明" min-width="240" show-overflow-tooltip />
						<el-table-column prop="residentId" label="申请人ID" width="110" />
						<el-table-column prop="createTime" label="提交时间" width="180">
							<template #default="scope">
								<span>{{ formatDateTime(scope.row.createTime) }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="status" label="状态" width="120">
							<template #default>
								<el-tag type="warning">待审核</el-tag>
							</template>
						</el-table-column>
					</el-table>
				</el-card>
			</el-col>
			<el-col :xs="24" :sm="10">
				<el-card shadow="hover" header="最新公告">
					<div class="notice-list">
						<div class="notice-item" v-for="notice in state.noticeList" :key="notice.id">
							<div class="notice-title">{{ notice.title }}</div>
							<div class="notice-time">{{ formatDateTime(notice.createTime) }}</div>
						</div>
						<el-empty v-if="state.noticeList.length === 0" description="暂无相关记录" />
					</div>
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>

<script setup lang="ts" name="home">
import { onMounted, onBeforeUnmount, reactive, ref } from 'vue';
import * as echarts from 'echarts';
import { useVmsDashboardApi } from '/@/api/vms/dashboard';
import { useVmsApplyApi } from '/@/api/vms/apply';
import { useVmsNoticeApi } from '/@/api/vms/notice';
import { parseDateTime } from '/@/utils/formatTime';

const dashboardApi = useVmsDashboardApi();
const applyApi = useVmsApplyApi();
const noticeApi = useVmsNoticeApi();

const populationChartRef = ref<HTMLDivElement | null>(null);
const landAreaChartRef = ref<HTMLDivElement | null>(null);
const applyTrendChartRef = ref<HTMLDivElement | null>(null);
let populationChart: echarts.ECharts | null = null;
let landAreaChart: echarts.ECharts | null = null;
let applyTrendChart: echarts.ECharts | null = null;

const state = reactive({
	cards: [
		{ key: 'totalResidents', label: '总人口数', value: 0, desc: '登记在册村民总数' },
		{ key: 'partyMembers', label: '党员人数', value: 0, desc: '党员结构统计' },
		{ key: 'poorHouseholds', label: '贫困户数', value: 0, desc: '重点帮扶对象' },
		{ key: 'pendingApplyCount', label: '本月待办', value: 0, desc: '待审核事务申请' },
	],
	populationStats: [] as Array<{ label: string; count: number }>,
	landAreaStats: [] as Array<{ type: string; area: number }>,
	applyTrends: [] as Array<{ month: string; totalCount: number; finishedCount: number }>,
	pendingList: [] as Array<any>,
	noticeList: [] as Array<any>,
});

const formatDateTime = (value: string) => {
	if (!value) return '';
	return parseDateTime(value);
};

const renderCharts = () => {
	if (populationChartRef.value) {
		populationChart = echarts.init(populationChartRef.value);
		populationChart.setOption({
			tooltip: { trigger: 'item' },
			series: [
				{
					name: '人口结构',
					type: 'pie',
					radius: ['40%', '70%'],
					avoidLabelOverlap: false,
					itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
					label: { show: true, formatter: '{b}: {c} ({d}%)' },
					data: state.populationStats.map((item) => ({ value: item.count, name: item.label })),
				},
			],
		});
	}
	if (landAreaChartRef.value) {
		landAreaChart = echarts.init(landAreaChartRef.value);
		landAreaChart.setOption({
			tooltip: { trigger: 'axis' },
			xAxis: {
				type: 'category',
				data: state.landAreaStats.map((item) => item.type),
			},
			yAxis: { type: 'value' },
			series: [
				{
					type: 'bar',
					data: state.landAreaStats.map((item) => item.area),
					itemStyle: { color: '#409EFF' },
					barWidth: 28,
				},
			],
		});
	}
	if (applyTrendChartRef.value) {
		applyTrendChart = echarts.init(applyTrendChartRef.value);
		applyTrendChart.setOption({
			tooltip: { trigger: 'axis' },
			legend: { data: ['申请数量', '办结数量'] },
			xAxis: { type: 'category', data: state.applyTrends.map((item) => item.month) },
			yAxis: { type: 'value' },
			series: [
				{
					name: '申请数量',
					type: 'line',
					data: state.applyTrends.map((item) => item.totalCount),
					itemStyle: { color: '#409EFF' },
				},
				{
					name: '办结数量',
					type: 'line',
					data: state.applyTrends.map((item) => item.finishedCount),
					itemStyle: { color: '#67C23A' },
				},
			],
		});
	}
};

const loadDashboard = async () => {
	const summary: any = await dashboardApi.getSummary();
	state.cards = state.cards.map((card) => ({
		...card,
		value: summary[card.key] ?? 0,
	}));
	state.populationStats = summary.populationStats || [];
	state.landAreaStats = summary.landAreaStats || [];
	state.applyTrends = summary.applyTrends || [];
	renderCharts();
};

const loadPendingApply = async () => {
	const res: any = await applyApi.getPageList({
		page: { current: 1, size: 5 },
		query: { status: 0 },
	});
	state.pendingList = res?.records || [];
};

const loadNotices = async () => {
	const res: any = await noticeApi.getTop(6);
	state.noticeList = res || [];
};

onMounted(async () => {
	await loadDashboard();
	await loadPendingApply();
	await loadNotices();
});

onBeforeUnmount(() => {
	populationChart?.dispose();
	landAreaChart?.dispose();
	applyTrendChart?.dispose();
});
</script>

<style scoped lang="scss">
.dashboard {
	.dashboard-card {
		min-height: 120px;
		.card-title {
			color: var(--el-text-color-secondary);
			font-size: 14px;
		}
		.card-value {
			font-size: 30px;
			font-weight: 600;
			color: var(--el-text-color-primary);
			margin: 8px 0;
		}
		.card-desc {
			color: var(--el-text-color-placeholder);
			font-size: 12px;
		}
	}
	.chart {
		height: 320px;
		width: 100%;
	}
	.notice-list {
		display: flex;
		flex-direction: column;
		gap: 12px;
		min-height: 360px;
		.notice-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			border-bottom: 1px dashed var(--el-border-color-light);
			padding-bottom: 6px;
			.notice-title {
				flex: 1;
				margin-right: 12px;
				color: var(--el-text-color-primary);
			}
			.notice-time {
				color: var(--el-text-color-secondary);
				font-size: 12px;
			}
		}
	}
}
</style>

