<template>
	<slot v-if="getUserAuthBtnList" />
</template>

<script setup lang="ts" name="authAll">
import { computed } from 'vue';
import { storeToRefs } from 'pinia';
import { useUserInfo } from '/@/stores/userInfo';
import { judementSameArr } from '/@/utils/arrayOperation';

const props = defineProps({
	value: {
		type: Array,
		default: () => [],
	},
});

const stores = useUserInfo();
const { userInfos } = storeToRefs(stores);

const getUserAuthBtnList = computed(() => {
	return judementSameArr(props.value, userInfos.value.authBtnList);
});
</script>
