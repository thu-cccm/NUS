import { ref, toRefs } from 'vue';
import { useDictStore } from '/@/stores/dict'
import { useDictApi } from '/@/api/system/dict'

const useDict = useDictApi();

export function parseDict(...args) {
    const res = ref({});
    return (() => {
        for (let dictType of args) {
            res.value[dictType] = [];

            const dictStore = useDictStore().getDict(dictType);

            if (dictStore) {
                res.value[dictType] = dictStore;
            } else {

                useDict.getDictByCode(dictType).then(resp => {
                    res.value[dictType] = resp.map(p => ({
                        label: p.dictLabel,
                        value: p.dictValue,
                        elTagType: p.listClass,
                        elTagClass: p.cssClass
                    })).reduce((acc, item) => {
                        acc[item.value] = item;
                        return acc;
                    }, {})
                    useDictStore().setDict(dictType, res.value[dictType]);
                })
            }
        }
        return toRefs(res.value);
    })()
}