<template>
  <div>
    <div class="card">
      尊敬的 <span style="font-weight: bold">{{ data.user?.name }}</span> 您好！欢迎使用本系统，祝您今天有个好心情！
    </div>

    <div style="margin-top: 10px; display: flex; grid-gap: 10px">
      <!-- 收费标准卡片 -->
      <div class="card" style="width: 50%; position: relative">
        <div style="font-size: 20px; font-weight: bold; padding: 15px 20px; display: flex; justify-content: space-between; align-items: center">
          收费标准
          <!-- 调大按钮，默认尺寸，并加上更醒目的样式 -->
          <el-button
              v-if="isAdmin"
              type="primary"
              size="medium"
              style="font-size: 14px; padding: 6px 20px"
              @click="openEditDialog"
          >
            更改收费标准
          </el-button>
        </div>
        <div style="padding: 0 20px; line-height: 1.8;" v-if="charge">
          <ul>
            <li>🔸 免费时长：<strong>{{ charge.freeMinutes }} 分钟</strong></li>
            <li>🔸 首小时费用：<strong>{{ charge.firstHour }} 元</strong></li>
            <li>🔸 之后每小时：<strong>{{ charge.perHour }} 元</strong></li>
            <li>🔸 24 小时封顶：<strong>{{ charge.dailyCap }} 元</strong></li>
            <li>🔸 月租卡费用：<strong>{{ charge.monthlyFee }} 元/月</strong>（有效期内免费停车）</li>
          </ul>
        </div>
        <div v-else style="text-align: center; padding: 20px;">正在加载收费标准...</div>
      </div>

      <!-- 系统公告卡片 -->
      <div class="card" style="width: 50%">
        <div style="font-size: 20px; font-weight: bold; padding: 15px 20px">系统公告</div>
        <el-timeline style="max-width: 650px">
          <el-timeline-item
              v-for="notice in data.noticeList"
              :key="notice.id"
              color="#0bbd87"
              placement="top"
              :timestamp="notice.time"
          >
            <div style="font-weight: bold; font-size: 15px">{{ notice.title }}</div>
            <div style="margin-top: 10px; line-height: 20px">{{ notice.content }}</div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>

    <!-- 编辑收费标准弹窗 -->
    <el-dialog
        title="编辑收费标准"
        v-model="editVisible"
        width="400px"
        :destroy-on-close="true"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="免费时长" prop="freeMinutes">
          <el-input-number v-model="form.freeMinutes" :min="0" /> 分钟
        </el-form-item>
        <el-form-item label="首小时费" prop="firstHour">
          <el-input-number v-model="form.firstHour" :min="0" :step="0.01" /> 元
        </el-form-item>
        <el-form-item label="每小时费" prop="perHour">
          <el-input-number v-model="form.perHour" :min="0" :step="0.01" /> 元
        </el-form-item>
        <el-form-item label="24h 封顶" prop="dailyCap">
          <el-input-number v-model="form.dailyCap" :min="0" :step="0.01" /> 元
        </el-form-item>
        <el-form-item label="月租卡费" prop="monthlyFee">
          <el-input-number v-model="form.monthlyFee" :min="0" :step="0.01" /> 元/月
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import request from '@/utils/request.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const data = reactive({
  user: JSON.parse(localStorage.getItem('loginUser') || '{}'),
  noticeList: []
})
const charge = ref(null)
const editVisible = ref(false)
const formRef = ref(null)

// 编辑表单
const form = reactive({
  freeMinutes: 0,
  firstHour: 0,
  perHour: 0,
  dailyCap: 0,
  monthlyFee: 0
})

// 校验规则
const rules = {
  freeMinutes: [{ required: true, message: '请输入免费时长', trigger: 'blur' }],
  firstHour: [{ required: true, message: '请输入首小时费用', trigger: 'blur' }],
  perHour: [{ required: true, message: '请输入每小时费用', trigger: 'blur' }],
  dailyCap: [{ required: true, message: '请输入24h封顶费用', trigger: 'blur' }],
  monthlyFee: [{ required: true, message: '请输入月租卡费用', trigger: 'blur' }]
}

// 是否管理员
const isAdmin = computed(() =>
    data.user.roleList?.includes('ADMIN')
)

// 加载公告
const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === 200) {
      data.noticeList = res.data
    }
  })
}

// 加载收费标准
const loadCharge = () => {
  request.get('/charge/getInfo').then(res => {
    if (res.code === 200 && res.data) {
      charge.value = res.data
    }
  })
}

// 打开编辑弹窗，填充表单
const openEditDialog = () => {
  Object.assign(form, charge.value)
  editVisible.value = true
}

// 保存编辑
const saveEdit = () => {
  formRef.value.validate(valid => {
    if (!valid) return
    request.post('/charge/update', form).then(res => {
      if (res.code === 200) {
        ElMessage.success('保存成功')
        editVisible.value = false
        loadCharge()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}

onMounted(() => {
  loadCharge()
  loadNotice()
})
</script>
