import { defineStore } from 'pinia';
import { Session } from '/@/utils/storage';

export const useDictStore = defineStore('dicts',{
    state:() => ({
        dicts: new Array()
    }),
    actions:{

        getDict(_key) {
            if (_key == null || _key == "") {
                return null;
            }
            if (Session.get(_key)) {
                return Session.get(_key);
            } 
            try {
                for (let dict of this.dicts) {
                    if (dict.key == _key) {
                        return dict.value;
                    }
                }
            } catch (e) {
                console.log(e)
            }
            return null;
        },

        async setDict(_key, value) {
            if (_key !== null && _key !== "") {
                this.dicts.push({
                    key: _key,
                    value: value
                });
                Session.set(_key, value);
            }
        },

        async removeDict(_key) {
            var bln = false;
            try {
                for (let i = 0; i < this.dicts.length; i++) {
                    if (this.dicts[i].key == _key) {
                        this.dicts.splice(i, 1);
                        return true;
                    }
                }
            } catch (e) {
                bln = false;
            }
            return bln;
        },

        cleanDict() {
            this.dicts = new Array();
        },
    }
})