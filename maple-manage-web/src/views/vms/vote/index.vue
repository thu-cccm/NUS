<template>
	<div class="vms-vote layout-padding">
		<el-row :gutter="15">
			<el-col :xs="24" :sm="8" v-for="item in state.list" :key="item.id" class="mb15">
				<el-card shadow="hover" class="vote-card">
					<div class="vote-title">{{ item.title }}</div>
					<div class="vote-content">{{ item.content }}</div>
					<div class="vote-stats">
						<span>赞成 {{ item.agreeCount }}</span>
						<span>反对 {{ item.disagreeCount }}</span>
					</div>
					<div class="vote-footer">
						<el-tag :type="item.status === 1 ? 'info' : 'success'">
							{{ item.status === 1 ? '已结束' : '进行中' }}
						</el-tag>
						<div class="vote-actions">
							<template v-if="!isAdmin">
								<el-button
									size="small"
									type="success"
									:disabled="item.status === 1 || votedIds.includes(item.id)"
									@click="onVote(item.id, true)"
								>
									赞成
								</el-button>
								<el-button
									size="small"
									type="danger"
									:disabled="item.status === 1 || votedIds.includes(item.id)"
									@click="onVote(item.id, false)"
								>
									反对
								</el-button>
							</template>
							<template v-else>
								<el-button size="small" type="warning" @click="endVote(item.id)">结束</el-button>
								<el-button size="small" type="danger" @click="removeVote(item.id)">删除</el-button>
							</template>
						</div>
					</div>
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>

<script setup lang="ts" name="vmsVote">
import { computed, onMounted, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useVmsVoteApi } from '/@/api/vms/vote';
import { useUserInfo } from '/@/stores/userInfo';

const voteApi = useVmsVoteApi();
const userInfo = useUserInfo();

const isAdmin = computed(() => userInfo.userInfos.roles?.includes('1'));

const state = reactive({
	list: [] as Array<any>,
});

const votedIds = reactive<number[]>([]);

const loadVotes = () => {
	voteApi.getPageList({ page: { current: 1, size: 50 }, query: {} }).then((res: any) => {
		state.list = res.records || [];
	});
};

const onVote = (id: number, agree: boolean) => {
	voteApi.castVote(id, agree).then(() => {
		ElMessage.success('投票成功');
		votedIds.push(id);
		loadVotes();
	});
};

const endVote = (id: number) => {
	voteApi.endVote(id).then(() => {
		ElMessage.success('已结束');
		loadVotes();
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
			loadVotes();
		})
		.catch(() => null);
};

onMounted(() => {
	userInfo.setUserInfos().then(() => {
		loadVotes();
	});
});
</script>

<style scoped lang="scss">
.vms-vote {
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
}
</style>

