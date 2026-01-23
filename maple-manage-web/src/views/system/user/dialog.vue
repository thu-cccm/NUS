<template>
  <div class="system-user-dialog-container">
    <el-dialog :title="state.dialog.title" v-model="state.dialog.isShowDialog" width="50%">
      <el-form ref="userDialogFormRef" :model="state.ruleForm" :rules="state.rules" size="default" label-width="90px">
        <el-row :gutter="35">

          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="用户类型" prop="userType">
              <el-radio-group v-model="state.ruleForm.userType">
                <el-radio
                    v-for="dict in system_user_type"
                    :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <template v-if="state.ruleForm.userType === '1'">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="所属部门">
                <el-tree-select
                    :props="{ value: 'id', label: 'deptName' }"
                    v-model="state.ruleForm.deptId"
                    :data="state.deptTreeData"
                    check-strictly
                    filterable
                    :render-after-expand="false"
                    :placeholder="''"
                    class="w100"
                />
              </el-form-item>
            </el-col>
          </template>
          <template v-if="state.ruleForm.userType === '2'">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="用户ID" prop="openId">
                <el-input v-model="state.ruleForm.openId" placeholder="请输入用户ID" clearable></el-input>
              </el-form-item>
            </el-col>
          </template>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="用户账号" prop="account">
              <el-input v-model="state.ruleForm.account" placeholder="请输入用户账号" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="用户角色" prop="roleIds">
              <el-select
                  v-model="state.ruleForm.roleIds"
                  placeholder="用户角色"
                  clearable
                  multiple
                  collapse-tags
                  @change="onRoleChange"
                  class="w100">
                <el-option
                    v-for="role in state.roleOptions"
                    :key="role.id"
                    :label="role.roleName"
                    :value="role.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="state.ruleForm.nickName" placeholder="请输入用户昵称" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="真实姓名" prop="userName">
              <el-input v-model="state.ruleForm.userName" placeholder="请输入真实姓名" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="用户性别" prop="sex">
              <el-select v-model="state.ruleForm.sex" placeholder="请选择用户性别" clearable class="w100">
                <el-option
                    v-for="dict in sys_user_sex"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="用户邮箱" prop="email">
              <el-input v-model="state.ruleForm.email" placeholder="请输入用户邮箱" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="state.ruleForm.phone" placeholder="请输入手机号码" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="头像" prop="avatar">
              <el-upload
                class="avatar-uploader"
                action="/manageApi/manage/file/uploadImage"
                :show-file-list="false"
                :on-success="onAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img v-if="state.ruleForm.avatar" :src="state.ruleForm.avatar" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><ele-Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </el-col>
          <template v-if="state.dialog.type === 'add'">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="密码" prop="password">
                <el-input v-model="state.ruleForm.password" placeholder="请输入密码" clearable></el-input>
              </el-form-item>
            </el-col>
          </template>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="帐号状态" prop="status">
              <el-switch
                v-model="state.ruleForm.status"
                active-value=1
                inactive-value=0
                inline-prompt
                active-text="启用"
                inactive-text="停用"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" :rows="3"  v-model="state.ruleForm.remark" placeholder="请输入备注" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="onCancel" size="default">取 消</el-button>
          <el-button type="primary" @click="onSubmit" size="default">{{ state.dialog.submitTxt }}</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="dictDialog">
import { onMounted, nextTick, reactive, ref, getCurrentInstance, computed } from 'vue';
import { useUserApi } from '/@/api/system/user';
import { useRoleApi } from '/@/api/system/role';
import { useDeptApi } from '/@/api/system/dept';
import { ElMessage } from "element-plus";

const emit = defineEmits(['refresh']);

const { proxy } = getCurrentInstance();
const { sys_status,system_user_type,sys_user_sex } = proxy.parseDict("sys_status","system_user_type","sys_user_sex");

const useUser = useUserApi();
const useRole = useRoleApi();
const useDept = useDeptApi();
const userDialogFormRef = ref();
const state = reactive({
  ruleForm: {
    openId: '',
    deptId: '',
    account: '',
    userName: '',
    nickName: '',
    userType: '1',
    email: '',
    phone: '',
    sex: '',
    avatar: '',
    password: '',
    status: 1,
    remark: '',
    roleIds: [],
  },
  dialog: {
    isShowDialog: false,
    type: '',
    title: '',
    submitTxt: '',
  },
  rules: {
    account: { required: true, message: '请输入用户账号', trigger: 'blur' },
    userType: { required: true, message: '请输入用户类型', trigger: 'blur' },
    password: { required: true, message: '请输入用户密码', trigger: 'blur' },
    nickName: { required: true, message: '请输入用户昵称', trigger: 'blur' },
    sex: { required: true, message: '请选择用户性别', trigger: 'blur' },
  },
  roleOptions: [],
  deptTreeData: [],
});

const normalizedRoleIds = computed(() => (Array.isArray(state.ruleForm.roleIds) ? state.ruleForm.roleIds : []));

const openDialog = (type: string, row) => {
  resetForm();
  if (type === 'edit') {
    useUser.getUserById(row.id).then(res => {
      state.ruleForm = res;
      if (!Array.isArray(state.ruleForm.roleIds)) {
        state.ruleForm.roleIds = [];
      }
      state.dialog.title = '修改用户中心-用户信息';
      state.dialog.submitTxt = '修 改';
    });
  } else {
    state.dialog.title = '新增用户中心-用户信息';
    state.dialog.submitTxt = '新 增';

    nextTick(() => {
      userDialogFormRef.value.resetFields();
    });
  }
  state.dialog.type = type;
  state.dialog.isShowDialog = true;
};

const onRoleChange = (value) => {
  if (!Array.isArray(value)) return;
  const roleMap = new Map(state.roleOptions.map((r) => [r.id, r.roleName]));
  const specialIds = value.filter((id) => {
    const name = roleMap.get(id);
    return name === '管理员' || name === '普通用户';
  });
  if (specialIds.length > 1) {
    const keep = specialIds[specialIds.length - 1];
    state.ruleForm.roleIds = value.filter((id) => {
      const name = roleMap.get(id);
      if (name === '管理员' || name === '普通用户') {
        return id === keep;
      }
      return true;
    });
  }
};

const onAvatarSuccess = (response) => {
  if (response?.status === false) {
    ElMessage.error(response?.msg || '头像上传失败');
    return;
  }
  const url = response?.data || response?.url || response?.fileServiceName;
  if (url) {
    state.ruleForm.avatar = url;
  }
};

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/');
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isImage) {
    ElMessage.error('只能上传图片格式');
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB');
  }
  return isImage && isLt2M;
};

const closeDialog = () => {
  state.dialog.isShowDialog = false;
};

const onCancel = () => {
  closeDialog();
};

const onSubmit = () => {

  Promise.all([
    currentValidate(userDialogFormRef),
  ]).then(res => {
    const validateResult = res.every(item => !!item);
    if (validateResult) {
      if(state.dialog.type == 'add'){
        useUser.createUser(state.ruleForm).then(() => {
          ElMessage.success('创建成功');
          closeDialog();
          emit('refresh');
        });
      } else {
        useUser.updateUser(state.ruleForm).then(() => {
          ElMessage.success('修改成功');
          closeDialog();
          emit('refresh');
        });
      }
    } else {
      ElMessage.error("表单校验未通过，请重新检查提交内容");
    }
  });
};

const getRoleList = () => {
  useRole.getRoleList().then(res => {
    state.roleOptions = res;
  });
};

const getDeptList = () => {
  useDept.getTreeList({}).then(res => {
    state.deptTreeData = res;
  });
};

const currentValidate = (pageRef) => {
  return new Promise((resolve) => {
    pageRef.value.validate((valid: boolean) => {
      if (valid) resolve(valid);
    });
  });
};

const resetForm = () => {
  state.ruleForm = {
    openId: '',
    deptId: '',
    account: '',
    userName: '',
    nickName: '',
    userType: '1',
    email: '',
    phone: '',
    sex: '',
    avatar: '',
    password: '',
    status: 1,
    remark: '',
    roleIds: [],
  };
}

onMounted(() => {
  getRoleList();
  getDeptList();
});

defineExpose({
  openDialog,
});
</script>

<style scoped lang="scss">
.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 120px;
    height: 120px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
  .avatar {
    width: 120px;
    height: 120px;
    display: block;
    object-fit: cover;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: var(--el-text-color-placeholder);
  }
}
</style>
