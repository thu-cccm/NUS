<template>
	<slot v-if="getUserAuthBtnList" />
</template>

<script setup lang="ts" name="auth">
import { computed } from 'vue';
import { storeToRefs } from 'pinia';
import { useUserInfo } from '/@/stores/userInfo';

const props = defineProps({
	value: {
		type: String,
		default: () => '',
	},
});

const stores = useUserInfo();
const { userInfos } = storeToRefs(stores);

const getUserAuthBtnList = computed(() => {
	return userInfos.value.authBtnList.some((v: string) => v === props.value);
});
</script>
