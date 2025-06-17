<template>
  <div style="width: 50%;">
    <div class="card">
      <el-form ref="formRef" :model="formData" label-width="80px" style="padding: 20px 30px">
        <el-form-item label="头像">
          <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :http-request="uploadWithRequest"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <img v-if="formData.avatar" alt="用户头像" :src="formData.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item prop="username" label="用户名">
          <el-input v-model="formData.username" />
        </el-form-item>

        <el-form-item prop="nickName" label="昵称">
          <el-input v-model="formData.nickName" />
        </el-form-item>

        <el-form-item prop="phone" label="电话">
          <el-input v-model="formData.phone" />
        </el-form-item>

        <el-form-item prop="email" label="邮箱">
          <el-input v-model="formData.email" />
        </el-form-item>

        <el-form-item prop="account" label="余额" v-if="isUser">
          <span style="color: red">￥{{ formData.account || 0 }}</span>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleUpdate" :loading="loading">
            保 存
          </el-button>
          <el-button
              type="warning"
              @click="openRechargeDialog"
              v-if="!isAdmin"
          >
            充 值
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 充值弹窗 -->
    <el-dialog
        title="个人充值"
        v-model="rechargeDialog.visible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >
      <el-form
          :model="rechargeDialog.form"
          label-width="80px"
          style="padding: 20px 30px"
          :rules="rechargeRules"
          ref="rechargeFormRef"
      >
        <el-form-item label="充值金额" prop="amount">
          <el-input
              v-model.number="rechargeDialog.form.amount"
              placeholder="请输入充值金额"
              type="number"
              min="1"
          />
        </el-form-item>

        <el-form-item label="支付方式" prop="type">
          <el-radio-group v-model="rechargeDialog.form.type" size="large">
            <el-radio-button label="微信" value="wechat" />
            <el-radio-button label="支付宝" value="alipay" />
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeRechargeDialog">取消</el-button>
          <el-button
              type="primary"
              @click="handleRecharge"
              :loading="rechargeDialog.loading"
          >
            充值
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const formRef = ref()
const rechargeFormRef = ref()
const loading = ref(false)

// 表单数据
const formData = reactive({
  username: '',
  nickName: '',
  phone: '',
  email: '',
  avatar: '',
  account: 0,
  roleList: []
})

// 充值弹窗数据
const rechargeDialog = reactive({
  visible: false,
  loading: false,
  form: {
    amount: 100,
    type: 'wechat'
  }
})

// 充值表单验证规则
const rechargeRules = {
  amount: [
    { required: true, message: '请输入充值金额', trigger: 'blur' },
    { type: 'number', min: 1, message: '充值金额必须大于0', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择支付方式', trigger: 'change' }
  ]
}

// 计算属性
const isUser = computed(() => {
  return userStore.userInfo.roleList?.includes('USER')
})

const isAdmin = computed(() => {
  return userStore.userInfo.roleList?.includes('ADMIN')
})

// 初始化表单数据
const initFormData = () => {
  const userInfo = userStore.userInfo
  Object.assign(formData, {
    username: userInfo.username || '',
    nickName: userInfo.nickName || '',
    phone: userInfo.phone || '',
    email: userInfo.email || '',
    avatar: userInfo.avatar || '',
    account: userInfo.account || 0,
    roleList: userInfo.roleList || []
  })
}

// 加载用户信息
const loadUserInfo = async () => {
  if (!userStore.userInfo?.id) {
    ElMessage.error('用户信息不完整')
    return
  }

  try {
    const url = `/allUser/selectById/${userStore.userInfo.id}`
    const res = await request.get(url)

    if (res.code === 200) {
      // 更新 store 中的用户信息
      userStore.setUser(res.data)
      // 同步到表单数据
      initFormData()
    } else {
      ElMessage.error(res.msg || '获取用户信息失败')
    }
  } catch (error) {
    console.error('加载用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}

// 更新用户信息
const handleUpdate = async () => {
  if (loading.value) return

  loading.value = true

  try {
    // 准备更新数据
    const updateData = {
      ...userStore.userInfo,
      username: formData.username,
      nickName: formData.nickName,
      phone: formData.phone,
      email: formData.email,
      avatar: formData.avatar
    }

    const res = await request.put('/allUser/update', updateData)

    if (res.code === 200) {
      // 使用服务器返回的最新数据更新 store
      userStore.setUser(res.data)
      // 同步到表单数据
      initFormData()
      ElMessage.success('更新成功')
    } else {
      ElMessage.error(res.msg || '更新失败')
    }
  } catch (error) {
    console.error('更新用户信息失败:', error)
    ElMessage.error('更新失败')
  } finally {
    loading.value = false
  }
}

// 打开充值弹窗
const openRechargeDialog = () => {
  rechargeDialog.visible = true
  // 重置表单
  rechargeDialog.form.amount = 100
  rechargeDialog.form.type = 'wechat'
}

// 关闭充值弹窗
const closeRechargeDialog = () => {
  rechargeDialog.visible = false
  rechargeDialog.loading = false
}

// 处理充值
const handleRecharge = async () => {
  if (!rechargeFormRef.value) return

  // 验证表单
  const valid = await rechargeFormRef.value.validate().catch(() => false)
  if (!valid) return

  if (rechargeDialog.loading) return

  rechargeDialog.loading = true

  try {
    // 计算新余额
    const newAccount = parseFloat(formData.account || 0) + parseFloat(rechargeDialog.form.amount)

    // 准备更新数据
    const updateData = {
      ...userStore.userInfo,
      account: newAccount
    }

    const res = await request.put('/allUser/update', updateData)

    if (res.code === 200) {
      // 使用服务器返回的最新数据更新本地数据
      userStore.setUser(res.data)
      // 重新初始化表单数据以反映最新余额
      initFormData()

      ElMessage.success(`充值成功！当前余额：￥${res.data.account || newAccount}`)
      closeRechargeDialog()
    } else {
      ElMessage.error(res.msg || '充值失败')
    }
  } catch (error) {
    console.error('充值失败:', error)
    ElMessage.error('充值失败，请重试')
  } finally {
    rechargeDialog.loading = false
  }
}

// 头像上传成功处理
const handleAvatarSuccess = (res) => {
  if (res?.data) {
    formData.avatar = res.data
  } else {
    ElMessage.error('头像上传失败')
  }
}

// 自定义上传方法
const uploadWithRequest = async (uploadOption) => {
  const form = new FormData()
  form.append('file', uploadOption.file)

  try {
    const res = await request.post('/upload', form, {
      headers: {'Content-Type': 'multipart/form-data'}
    })
    uploadOption.onSuccess(res, uploadOption.file)
  } catch (error) {
    console.error('上传失败:', error)
    uploadOption.onError(error)
    ElMessage.error('上传失败')
  }
}

// 文件上传前的验证
const beforeAvatarUpload = (rawFile) => {
  const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png']
  const maxSize = 10 * 1024 * 1024 // 10MB

  if (!allowedTypes.includes(rawFile.type)) {
    ElMessage.error('只支持上传 JPG/PNG 格式的图片')
    return false
  }

  if (rawFile.size > maxSize) {
    ElMessage.error('图片大小不能超过 10MB')
    return false
  }

  return true
}

// 组件挂载时初始化
onMounted(() => {
  initFormData()
  loadUserInfo()
})
</script>

<style scoped>
.card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 6px;
  object-fit: cover;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>