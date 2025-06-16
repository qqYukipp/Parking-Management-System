<template>
  <div style="width: 50%;">
    <div class="card">
      <el-form ref="formRef" :model="userData" label-width="80px" style="padding: 20px 30px">
        <el-form-item label="头像">
          <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :http-request="uploadWithRequest"
              :on-success="handleFileUpload"
              :before-upload="beforeAvatarUpload"
          >
            <img v-if="userData.avatar" alt="" :src="userData.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item prop="username" label="用户名">
          <el-input v-model="userData.username" disabled />
        </el-form-item>
        <el-form-item prop="nickName" label="昵称">
          <el-input v-model="userData.nickName" />
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="userData.phone" />
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="userData.email" />
        </el-form-item>
        <el-form-item prop="account" label="余额" v-if="userStore.userInfo.roleList.includes('USER')">
          <span style="color: red">￥{{ userData.account }}</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="update">保 存</el-button>
          <el-button type="warning"
                     @click="rechargeInit"
                     v-if="!(userStore.userInfo.roleList.includes('ADMIN'))"
          >充 值
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-dialog title="个人充值" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="80px" style="padding: 20px 30px" ref="formRef">
        <el-form-item label="充值金额" prop="account">
          <el-input v-model="data.account" placeholder="请输入充值金额"></el-input>
        </el-form-item>
        <el-form-item label="支付方式" prop="type">
          <el-radio-group v-model="data.type" size="large">
            <el-radio-button label="微信" value="wei" />
            <el-radio-button label="支付宝" value="ali" />
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="recharge">充值</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const baseUrl = import.meta.env.VITE_BASE_URL
const userStore = useUserStore()

const data = reactive({
  account: 100,
  formVisible: false,
  type: 'wei'
})

const userData = ref({
  username: userStore.userInfo.username,
  nickName: userStore.userInfo.nickName,
  phone: userStore.userInfo.phone,
  email: userStore.userInfo.email,
  avatar: userStore.userInfo.avatar,
  account: userStore.userInfo.account
})

// 监听 userStore.userInfo 的变化，同步到 userData
watch(() => userStore.userInfo, (newUserInfo) => {
  userData.value = {
    username: newUserInfo.username,
    nickName: newUserInfo.nickName,
    phone: newUserInfo.phone,
    email: newUserInfo.email,
    avatar: newUserInfo.avatar,
    account: newUserInfo.account
  }
}, { deep: true })

const formRef = ref()

// 更新用户信息
const update = async () => {
  try {
    const url = '/allUser/update'

    // 先更新到 userStore
    userStore.setUserInfo(userData.value)

    console.log(userStore.userInfo)

    const res = await request.put(url, userStore.userInfo)
    if (res.code === 200) {
      ElMessage.success('更新成功')
      // 确保本地存储也更新了
      userStore.setUser({ ...userStore.userInfo })
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

// 加载用户信息
const loadPerson = async () => {
  try {
    const url = '/allUser/selectById/' + userStore.userInfo.id
    const res = await request.get(url)
    if (res.code === 200) {
      // 可以选择性更新一些字段，但要保持token
      // userStore.setUser(res.data)
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

onMounted(() => {
  loadPerson()
})

// 充值
const rechargeInit = () => {
  data.formVisible = true
}

const recharge = async () => {
  if (data.account <= 0) {
    ElMessage.error('请输入正确的充值金额')
    return
  }

  try {
    // 计算新的余额
    const newAccount = parseFloat(userData.value.account || 0) + parseFloat(data.account)

    // 更新本地数据
    userData.value.account = newAccount

    // 调用更新接口
    await update()

    // 关闭弹窗
    data.formVisible = false

    ElMessage.success(`充值成功！当前余额：￥${newAccount}`)
  } catch (error) {
    ElMessage.error('充值失败，请重试')
  }
}

// 上传头像成功处理
const handleFileUpload = (res) => {
  userData.value.avatar = res.data   // 同步更新到本地表单数据
}

const uploadWithRequest = async (uploadOption) => {
  // uploadOption.file 是当前要上传的 File 对象
  const form = new FormData()
  form.append('file', uploadOption.file)

  try {
    const res = await request.post('/upload', form, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    // 成功后手动调用 el-upload 的 onSuccess
    uploadOption.onSuccess(res, uploadOption.file)
  } catch (err) {
    uploadOption.onError(err)
    ElMessage.error('上传失败')
  }
}

// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('只支持上传图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('只能上传10M以内图片')
    return false
  }
  return true
}

</script>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
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
}
</style>