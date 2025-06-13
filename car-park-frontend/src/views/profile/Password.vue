<template>
  <div style="width: 50%;">
    <div class="card">
      <el-form ref="formRef" :rules="data.rules" :model="data.userPasswordForm" label-width="100px" style="padding: 20px 30px">
        <el-form-item prop="password" label="原密码" >
          <el-input v-model="data.userPasswordForm.password" show-password />
        </el-form-item>
        <el-form-item prop="newPassword" label="新密码">
          <el-input v-model="data.userPasswordForm.newPassword" show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword" label="确认新密码">
          <el-input v-model="data.userPasswordForm.confirmPassword" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updatePassword">保存</el-button>
        </el-form-item>
      </el-form>
    </div>

  </div>
</template>

<script setup>
import {reactive, ref} from "vue"
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else if (value !== data.userPasswordForm.newPassword) {
    callback(new Error('确认密码错误'))
  } else {
    callback()
  }
}


const data = reactive({
  userPasswordForm: {
    id: JSON.parse(localStorage.getItem('loginUser') || '{}').id,  // 仅取 id
    //username: JSON.parse(localStorage.getItem('loginUser') || '{}').username,
    password: '',
    newPassword: '',
    confirmPassword: ''
  },
  rules: {
    password: [
      { required: true, message: '请输入原密码', trigger: 'blur' }
    ],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' }
    ],
    confirmPassword: [
      { required: true, message: '请确认新密码', trigger: 'blur' },
      { validator: validatePassword, trigger: 'blur' }
    ]
  }
})


const formRef = ref()
const updatePassword = () => {
  formRef.value.validate((valid => {
    if (valid) {
      request.put('/allUser/updatePassword', data.userPasswordForm).then(res => {
        if (res.code === 200) {
          ElMessage.success('更新密码成功')
          //为什么要登出？
          //logout()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  }))
}

const logout = () => {
  localStorage.removeItem('loginUser')
  router.push('/login')
}
</script>