<template>
	<div class="vms-resident layout-padding">
		<el-card shadow="hover" class="layout-padding-auto">
			<div class="vms-resident-search">
				<el-form :inline="true" ref="queryRef" :model="state.tableData.param.query">
					<el-form-item label="姓名">
						<el-input v-model="state.tableData.param.query.realName" placeholder="请输入姓名" clearable />
					</el-form-item>
					<el-form-item label="村组">
						<el-input-number v-model="state.tableData.param.query.groupNo" :min="1" placeholder="请输入村组" clearable />
					</el-form-item>
					<el-form-item label="贫困户">
						<el-select v-model="state.tableData.param.query.isPoor" placeholder="请选择" clearable>
							<el-option label="是" :value="1" />
							<el-option label="否" :value="0" />
						</el-select>
					</el-form-item>
					<el-form-item label="迁移状态">
						<el-select v-model="state.tableData.param.query.migrateStatus" placeholder="请选择" clearable>
							<el-option label="在村" value="在村" />
							<el-option label="迁入" value="迁入" />
							<el-option label="迁出" value="迁出" />
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="getTableData">查询</el-button>
					<el-button @click="resetQuery">重置</el-button>
				</el-form>
			</div>
			<el-row>
				<el-button type="primary" @click="openDialog('add')">新增</el-button>
				<el-button @click="toggleViewMode">{{ state.viewMode === 'table' ? '按户分组' : '列表视图' }}</el-button>
				<el-button @click="exportResident">导出CSV</el-button>
			</el-row>
			<!-- 列表视图 -->
			<el-table v-if="state.viewMode === 'table'" :data="state.tableData.records" v-loading="state.tableData.loading" stripe style="width: 100%">
				<el-table-column type="index" label="序号" width="60" />
				<el-table-column prop="familyId" label="户号" width="120" />
				<el-table-column prop="isHouseholder" label="户主" width="80">
					<template #default="scope">
						<el-tag v-if="scope.row.isHouseholder === 1" type="warning">户主</el-tag>
						<span v-else>-</span>
					</template>
				</el-table-column>
				<el-table-column prop="realName" label="姓名" min-width="120">
					<template #default="scope">
						<span>{{ scope.row.realName }}</span>
						<el-tag v-for="tag in scope.row.tagList" :key="tag" size="small" type="info" style="margin-left: 5px">
							{{ tag }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="gender" label="性别" width="80">
					<template #default="scope">
						<el-tag :type="scope.row.gender === 1 ? 'success' : 'info'">
							{{ scope.row.gender === 1 ? '男' : '女' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="age" label="年龄" width="80" />
				<el-table-column prop="idCard" label="身份证号" min-width="180">
					<template #default="scope">
						<span>{{ maskIdCard(scope.row.idCard) }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="联系电话" min-width="140">
					<template #default="scope">
						<span>{{ maskPhone(scope.row.phone) }}</span>
					</template>
				</el-table-column>
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
				<el-table-column prop="migrateStatus" label="迁移状态" width="100">
					<template #default="scope">
						<el-tag :type="getMigrateStatusType(scope.row.migrateStatus)">
							{{ scope.row.migrateStatus || '在村' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="建档时间" width="180">
					<template #default="scope">
						<span>{{ parseDateTime(scope.row.createTime) }}</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="250" fixed="right">
					<template #default="scope">
						<el-button size="small" text type="primary" @click="openDialog('edit', scope.row)">编辑</el-button>
						<el-button v-if="scope.row.migrateStatus !== '迁出'" size="small" text type="warning" @click="openMigrateOut(scope.row)">迁出</el-button>
						<el-button v-if="scope.row.migrateStatus === '迁出'" size="small" text type="success" @click="doMigrateIn(scope.row)">迁入</el-button>
						<el-button size="small" text type="danger" @click="onRowDel(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!-- 按户分组视图 -->
			<div v-else class="family-group-view">
				<div v-for="family in state.familyGroups" :key="family.familyId" class="family-group mb15">
					<el-card shadow="hover">
						<template #header>
							<div class="family-header">
								<span class="family-title">户号：{{ family.familyId || '未分组' }}</span>
								<el-button size="small" text type="primary" @click="toggleFamilyExpand(family.familyId)">
									{{ family.expanded ? '收起' : '展开' }}
								</el-button>
							</div>
						</template>
						<div v-if="family.expanded">
							<el-table :data="family.members" stripe size="small">
								<el-table-column prop="realName" label="姓名" width="120">
									<template #default="scope">
										<span>{{ scope.row.realName }}</span>
										<el-tag v-if="scope.row.isHouseholder === 1" type="warning" size="small" style="margin-left: 5px">户主</el-tag>
										<el-tag v-for="tag in scope.row.tagList" :key="tag" size="small" type="info" style="margin-left: 5px">
											{{ tag }}
										</el-tag>
									</template>
								</el-table-column>
								<el-table-column prop="gender" label="性别" width="80">
									<template #default="scope">
										<el-tag :type="scope.row.gender === 1 ? 'success' : 'info'" size="small">
											{{ scope.row.gender === 1 ? '男' : '女' }}
										</el-tag>
									</template>
								</el-table-column>
								<el-table-column prop="age" label="年龄" width="80" />
								<el-table-column prop="idCard" label="身份证号" min-width="180">
									<template #default="scope">
										<span>{{ maskIdCard(scope.row.idCard) }}</span>
									</template>
								</el-table-column>
								<el-table-column prop="phone" label="联系电话" min-width="140">
									<template #default="scope">
										<span>{{ maskPhone(scope.row.phone) }}</span>
									</template>
								</el-table-column>
								<el-table-column prop="politics" label="政治面貌" width="120" />
								<el-table-column prop="migrateStatus" label="迁移状态" width="100">
									<template #default="scope">
										<el-tag :type="getMigrateStatusType(scope.row.migrateStatus)" size="small">
											{{ scope.row.migrateStatus || '在村' }}
										</el-tag>
									</template>
								</el-table-column>
								<el-table-column label="操作" width="200">
									<template #default="scope">
										<el-button size="small" text type="primary" @click="openDialog('edit', scope.row)">编辑</el-button>
										<el-button v-if="scope.row.migrateStatus !== '迁出'" size="small" text type="warning" @click="openMigrateOut(scope.row)">迁出</el-button>
										<el-button v-if="scope.row.migrateStatus === '迁出'" size="small" text type="success" @click="doMigrateIn(scope.row)">迁入</el-button>
									</template>
								</el-table-column>
							</el-table>
						</div>
					</el-card>
				</div>
			</div>
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
				<el-form-item label="户号">
					<el-input v-model="state.form.familyId" placeholder="请输入户号，同一户号表示同一家庭" />
				</el-form-item>
				<el-form-item label="是否户主">
					<el-radio-group v-model="state.form.isHouseholder">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="0">否</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="姓名" required>
					<el-input v-model="state.form.realName" />
				</el-form-item>
				<el-form-item label="身份证号" required>
					<el-input v-model="state.form.idCard" />
				</el-form-item>
				<el-form-item label="性别" required>
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
					<el-select v-model="state.form.politics" style="width: 100%">
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
					<el-select v-model="state.form.healthStatus" style="width: 100%">
						<el-option label="健康" value="健康" />
						<el-option label="慢性病" value="慢性病" />
						<el-option label="残疾" value="残疾" />
					</el-select>
				</el-form-item>
				<el-form-item label="特殊人群标记">
					<el-checkbox-group v-model="state.form.tagList">
						<el-checkbox label="留守儿童">留守儿童</el-checkbox>
						<el-checkbox label="空巢老人">空巢老人</el-checkbox>
						<el-checkbox label="退役军人">退役军人</el-checkbox>
						<el-checkbox label="残疾人">残疾人</el-checkbox>
					</el-checkbox-group>
				</el-form-item>
				<el-form-item label="迁移状态">
					<el-select v-model="state.form.migrateStatus" style="width: 100%">
						<el-option label="在村" value="在村" />
						<el-option label="迁入" value="迁入" />
						<el-option label="迁出" value="迁出" />
					</el-select>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.dialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitForm">保存</el-button>
			</template>
		</el-dialog>

		<el-dialog v-model="state.migrateOutVisible" title="迁出操作" width="400px">
			<el-form :model="state.migrateForm" label-width="100px">
				<el-form-item label="姓名">
					<el-input v-model="state.migrateForm.realName" readonly />
				</el-form-item>
				<el-form-item label="迁出地" required>
					<el-input v-model="state.migrateForm.migrateTo" placeholder="请输入迁出地" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="state.migrateOutVisible = false">取消</el-button>
				<el-button type="primary" @click="doMigrateOut">确认迁出</el-button>
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
				groupNo: null,
				isPoor: '',
				migrateStatus: '',
			},
		},
	},
	viewMode: 'table', // 'table' | 'family'
	familyGroups: [] as Array<{ familyId: string; members: any[]; expanded: boolean }>,
	dialogVisible: false,
	migrateOutVisible: false,
	dialogTitle: '新增居民',
	dialogType: 'add',
	form: {
		id: undefined,
		familyId: '',
		isHouseholder: 0,
		realName: '',
		idCard: '',
		gender: 1,
		age: 30,
		phone: '',
		groupNo: 1,
		politics: '群众',
		isPoor: 0,
		healthStatus: '健康',
		tagList: [] as string[],
		migrateStatus: '在村',
	},
	migrateForm: {
		id: undefined,
		realName: '',
		migrateTo: '',
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
	state.tableData.param.query = {
		realName: '',
		groupNo: null,
		isPoor: '',
		migrateStatus: '',
	};
	nextTick(() => getTableData());
};

const toggleViewMode = () => {
	state.viewMode = state.viewMode === 'table' ? 'family' : 'table';
	if (state.viewMode === 'family') {
		loadFamilyGroups();
	} else {
		getTableData();
	}
};

const loadFamilyGroups = () => {
	state.tableData.loading = true;
	residentApi.getByFamily({ query: {} }).then((res: any) => {
		const groups = new Map<string, any[]>();
		(res || []).forEach((item: any) => {
			const familyId = item.familyId || '未分组';
			if (!groups.has(familyId)) {
				groups.set(familyId, []);
			}
			groups.get(familyId)!.push(item);
		});
		state.familyGroups = Array.from(groups.entries()).map(([familyId, members]) => ({
			familyId,
			members,
			expanded: true,
		}));
		state.tableData.loading = false;
	});
};

const toggleFamilyExpand = (familyId: string) => {
	const group = state.familyGroups.find((g) => g.familyId === familyId);
	if (group) {
		group.expanded = !group.expanded;
	}
};

const maskPhone = (value: string) => {
	if (!value) return '';
	return value.replace(/^(\d{3})\d{4}(\d{4})$/, '$1****$2');
};

const maskIdCard = (value: string) => {
	if (!value) return '';
	if (value.length < 8) return value;
	const prefix = value.slice(0, 4);
	const suffix = value.slice(-4);
	return `${prefix}**********${suffix}`;
};

const openDialog = (type: string, row?: any) => {
	state.dialogType = type;
	state.dialogTitle = type === 'add' ? '新增居民' : '编辑居民';
	state.dialogVisible = true;
	if (type === 'edit' && row) {
		state.form = {
			...row,
			tagList: row.tagList || [],
		};
	} else {
		state.form = {
			id: undefined,
			familyId: '',
			isHouseholder: 0,
			realName: '',
			idCard: '',
			gender: 1,
			age: 30,
			phone: '',
			groupNo: 1,
			politics: '群众',
			isPoor: 0,
			healthStatus: '健康',
			tagList: [],
			migrateStatus: '在村',
		};
	}
};

const openMigrateOut = (row: any) => {
	state.migrateForm = {
		id: row.id,
		realName: row.realName,
		migrateTo: '',
	};
	state.migrateOutVisible = true;
};

const doMigrateOut = () => {
	if (!state.migrateForm.id) {
		ElMessage.error('迁出记录异常，请刷新后重试');
		return;
	}
	if (!state.migrateForm.migrateTo) {
		ElMessage.error('请输入迁出地');
		return;
	}
	residentApi.migrateOut(state.migrateForm.id, state.migrateForm.migrateTo).then(() => {
		ElMessage.success('迁出成功');
		state.migrateOutVisible = false;
		if (state.viewMode === 'family') {
			loadFamilyGroups();
		} else {
			getTableData();
		}
	});
};

const doMigrateIn = (row: any) => {
	ElMessageBox.confirm('确认将该居民标记为迁入吗?', '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => residentApi.migrateIn(row.id))
		.then(() => {
			ElMessage.success('迁入成功');
			if (state.viewMode === 'family') {
				loadFamilyGroups();
			} else {
				getTableData();
			}
		})
		.catch(() => null);
};

const getMigrateStatusType = (status: string) => {
	if (status === '在村') return 'success';
	if (status === '迁入') return 'primary';
	if (status === '迁出') return 'info';
	return 'info';
};

const submitForm = () => {
	if (!state.form.realName) {
		ElMessage.error('请输入姓名');
		return;
	}
	if (!state.form.idCard) {
		ElMessage.error('请输入身份证号');
		return;
	}
	const api = state.dialogType === 'add' ? residentApi.create : residentApi.update;
	api(state.form).then(() => {
		ElMessage.success('保存成功');
		state.dialogVisible = false;
		if (state.viewMode === 'family') {
			loadFamilyGroups();
		} else {
			getTableData();
		}
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

const exportResident = () => {
	const params: any = {};
	if (state.tableData.param.query.realName) params.realName = state.tableData.param.query.realName;
	if (state.tableData.param.query.groupNo) params.groupNo = state.tableData.param.query.groupNo;
	if (state.tableData.param.query.isPoor !== '' && state.tableData.param.query.isPoor !== null) params.isPoor = state.tableData.param.query.isPoor;
	residentApi.export(params);
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

