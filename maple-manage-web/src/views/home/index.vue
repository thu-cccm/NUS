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
				<el-card shadow="hover" header="土地利用类型分布">
					<div ref="landChartRef" class="chart"></div>
				</el-card>
			</el-col>
			<el-col :xs="24" :sm="12">
				<el-card shadow="hover" header="各村组人口对比">
					<div ref="groupChartRef" class="chart"></div>
				</el-card>
			</el-col>
		</el-row>

		<el-row :gutter="15">
			<el-col :span="24">
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
		</el-row>
	</div>
</template>

<script setup lang="ts" name="home">
import { onMounted, onBeforeUnmount, reactive, ref } from 'vue';
import * as echarts from 'echarts';
import { useVmsDashboardApi } from '/@/api/vms/dashboard';
import { useVmsApplyApi } from '/@/api/vms/apply';
import { parseDateTime } from '/@/utils/formatTime';

const dashboardApi = useVmsDashboardApi();
const applyApi = useVmsApplyApi();

const landChartRef = ref<HTMLDivElement | null>(null);
const groupChartRef = ref<HTMLDivElement | null>(null);
let landChart: echarts.ECharts | null = null;
let groupChart: echarts.ECharts | null = null;

const state = reactive({
	cards: [
		{ key: 'totalResidents', label: '总人口数', value: 0, desc: '登记在册村民总数' },
		{ key: 'partyMembers', label: '党员人数', value: 0, desc: '党员结构统计' },
		{ key: 'poorHouseholds', label: '贫困户数', value: 0, desc: '重点帮扶对象' },
		{ key: 'pendingApplyCount', label: '本月待办', value: 0, desc: '待审核事务申请' },
	],
	landTypeStats: [] as Array<{ type: string; count: number }>,
	groupStats: [] as Array<{ groupNo: number; count: number }>,
	pendingList: [] as Array<any>,
});

const formatDateTime = (value: string) => {
	if (!value) return '';
	return parseDateTime(value);
};

const renderCharts = () => {
	if (landChartRef.value) {
		landChart = echarts.init(landChartRef.value);
		landChart.setOption({
			tooltip: { trigger: 'item' },
			series: [
				{
					name: '土地类型',
					type: 'pie',
					radius: ['40%', '70%'],
					avoidLabelOverlap: false,
					itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
					label: { show: true, formatter: '{b}: {c} ({d}%)' },
					data: state.landTypeStats.map((item) => ({ value: item.count, name: item.type })),
				},
			],
		});
	}
	if (groupChartRef.value) {
		groupChart = echarts.init(groupChartRef.value);
		groupChart.setOption({
			tooltip: { trigger: 'axis' },
			xAxis: {
				type: 'category',
				data: state.groupStats.map((item) => `${item.groupNo}组`),
			},
			yAxis: { type: 'value' },
			series: [
				{
					type: 'bar',
					data: state.groupStats.map((item) => item.count),
					itemStyle: { color: '#4CAF50' },
					barWidth: 28,
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
	state.landTypeStats = summary.landTypeStats || [];
	state.groupStats = summary.groupStats || [];
	renderCharts();
};

const loadPendingApply = async () => {
	const res: any = await applyApi.getPageList({
		page: { current: 1, size: 5 },
		query: { status: 0 },
	});
	state.pendingList = res?.records || [];
};

onMounted(async () => {
	await loadDashboard();
	await loadPendingApply();
});

onBeforeUnmount(() => {
	landChart?.dispose();
	groupChart?.dispose();
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
}
</style>

