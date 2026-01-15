<template>
	<div class="icon-selector w100 h100">
		<el-input
			v-model="state.fontIconSearch"
			:placeholder="state.fontIconPlaceholder"
			:clearable="clearable"
			:disabled="disabled"
			:size="size"
			ref="inputWidthRef"
			@clear="onClearFontIcon"
			@focus="onIconFocus"
			@blur="onIconBlur"
		>
			<template #prepend>
				<SvgIcon
					:name="state.fontIconPrefix === '' ? prepend : state.fontIconPrefix"
					class="font14"
					v-if="state.fontIconPrefix === '' ? prepend?.indexOf('ele-') > -1 : state.fontIconPrefix?.indexOf('ele-') > -1"
				/>
				<i v-else :class="state.fontIconPrefix === '' ? prepend : state.fontIconPrefix" class="font14"></i>
			</template>
		</el-input>
		<el-popover
			placement="bottom"
			:width="state.fontIconWidth"
			transition="el-zoom-in-top"
			popper-class="icon-selector-popper"
			trigger="click"
			:virtual-ref="inputWidthRef"
			virtual-triggering
		>
			<template #default>
				<div class="icon-selector-warp">
					<div class="icon-selector-warp-title">{{ title }}</div>
					<el-tabs v-model="state.fontIconTabActive" @tab-click="onIconClick">
						<el-tab-pane lazy label="ali" name="ali">
							<IconList :list="fontIconSheetsFilterList" :empty="emptyDescription" :prefix="state.fontIconPrefix" @get-icon="onColClick" />
						</el-tab-pane>
						<el-tab-pane lazy label="ele" name="ele">
							<IconList :list="fontIconSheetsFilterList" :empty="emptyDescription" :prefix="state.fontIconPrefix" @get-icon="onColClick" />
						</el-tab-pane>
						<el-tab-pane lazy label="awe" name="awe">
							<IconList :list="fontIconSheetsFilterList" :empty="emptyDescription" :prefix="state.fontIconPrefix" @get-icon="onColClick" />
						</el-tab-pane>
					</el-tabs>
				</div>
			</template>
		</el-popover>
	</div>
</template>

<script setup lang="ts" name="iconSelector">
import { defineAsyncComponent, ref, reactive, onMounted, nextTick, computed, watch } from 'vue';
import type { TabsPaneContext } from 'element-plus';
import initIconfont from '/@/utils/getStyleSheets';
import '/@/theme/iconSelector.scss';

const props = defineProps({

	prepend: {
		type: String,
		default: () => 'ele-Pointer',
	},

	placeholder: {
		type: String,
		default: () => '请输入内容搜索图标或者选择图标',
	},

	size: {
		type: String,
		default: () => 'default',
	},

	title: {
		type: String,
		default: () => '请选择图标',
	},

	disabled: {
		type: Boolean,
		default: () => false,
	},

	clearable: {
		type: Boolean,
		default: () => true,
	},

	emptyDescription: {
		type: String,
		default: () => '无相关图标',
	},

	modelValue: String,
});

const emit = defineEmits(['update:modelValue', 'get', 'clear']);

const IconList = defineAsyncComponent(() => import('/@/components/iconSelector/list.vue'));

const inputWidthRef = ref();
const state = reactive({
	fontIconPrefix: '',
	fontIconWidth: 0,
	fontIconSearch: '',
	fontIconPlaceholder: '',
	fontIconTabActive: 'ali',
	fontIconList: {
		ali: [],
		ele: [],
		awe: [],
	},
});

const onIconFocus = () => {
	if (!props.modelValue) return false;
	state.fontIconSearch = '';
	state.fontIconPlaceholder = props.modelValue;
};

const onIconBlur = () => {
	const list = fontIconTabNameList();
	setTimeout(() => {
		const icon = list.filter((icon: string) => icon === state.fontIconSearch);
		if (icon.length <= 0) state.fontIconSearch = '';
	}, 300);
};

const fontIconSheetsFilterList = computed(() => {
	const list = fontIconTabNameList();
	if (!state.fontIconSearch) return list;
	let search = state.fontIconSearch.trim().toLowerCase();
	return list.filter((item: string) => {
		if (item.toLowerCase().indexOf(search) !== -1) return item;
	});
});

const fontIconTabNameList = () => {
	let iconList: any = [];
	if (state.fontIconTabActive === 'ali') iconList = state.fontIconList.ali;
	else if (state.fontIconTabActive === 'ele') iconList = state.fontIconList.ele;
	else if (state.fontIconTabActive === 'awe') iconList = state.fontIconList.awe;
	return iconList;
};

const initModeValueEcho = () => {
	if (props.modelValue === '') return ((<string | undefined>state.fontIconPlaceholder) = props.placeholder);
	(<string | undefined>state.fontIconPlaceholder) = props.modelValue;
	(<string | undefined>state.fontIconPrefix) = props.modelValue;
};

const initFontIconName = () => {
	let name = 'ali';
	if (props.modelValue!.indexOf('iconfont') > -1) name = 'ali';
	else if (props.modelValue!.indexOf('ele-') > -1) name = 'ele';
	else if (props.modelValue!.indexOf('fa') > -1) name = 'awe';

	state.fontIconTabActive = name;
	return name;
};

const initFontIconData = async (name: string) => {
	if (name === 'ali') {

		if (state.fontIconList.ali.length > 0) return;
		await initIconfont.ali().then((res: any) => {
			state.fontIconList.ali = res.map((i: string) => `iconfont ${i}`);
		});
	} else if (name === 'ele') {

		if (state.fontIconList.ele.length > 0) return;
		await initIconfont.ele().then((res: any) => {
			state.fontIconList.ele = res;
		});
	} else if (name === 'awe') {

		if (state.fontIconList.awe.length > 0) return;
		await initIconfont.awe().then((res: any) => {
			state.fontIconList.awe = res.map((i: string) => `fa ${i}`);
		});
	}

	state.fontIconPlaceholder = props.placeholder;

	initModeValueEcho();
};

const onIconClick = (pane: TabsPaneContext) => {
	initFontIconData(pane.paneName as string);
	inputWidthRef.value.focus();
};

const onColClick = (v: string) => {
	state.fontIconPlaceholder = v;
	state.fontIconPrefix = v;
	emit('get', state.fontIconPrefix);
	emit('update:modelValue', state.fontIconPrefix);
	inputWidthRef.value.focus();
};

const onClearFontIcon = () => {
	state.fontIconPrefix = '';
	emit('clear', state.fontIconPrefix);
	emit('update:modelValue', state.fontIconPrefix);
};

const getInputWidth = () => {
	nextTick(() => {
		state.fontIconWidth = inputWidthRef.value.$el.offsetWidth;
	});
};

const initResize = () => {
	window.addEventListener('resize', () => {
		getInputWidth();
	});
};

onMounted(() => {
	initFontIconData(initFontIconName());
	initResize();
	getInputWidth();
});

watch(
	() => props.modelValue,
	() => {
		initModeValueEcho();
		initFontIconName();
	}
);
</script>
