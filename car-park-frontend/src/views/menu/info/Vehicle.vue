<template>
  <div>
    <!-- 查询区 -->
    <div class="card" style="margin-bottom: 20px; padding: 20px;">
      <el-input
          v-model="data.name"
          placeholder="请输入车牌号查询"
          style="width: 240px"
      />
      <el-button
          type="info"
          plain
          style="margin-left: 10px"
          @click="load"
      >查询</el-button>
      <el-button
          type="warning"
          plain
          style="margin-left: 10px"
          @click="reset"
      >重置</el-button>
    </div>

    <!-- 操作区 -->
    <div class="card" style="margin-bottom: 20px; padding: 20px;">
      <!--      <el-button type="primary" plain @click="handleAdd">新增</el-button>-->
      <el-button type="danger" plain @click="delBatch">解绑车辆</el-button>
      <el-button type="warning" plain style="margin-left: 10px"
                 v-if="data.user.roleList.includes('USER')"
                 @click="openBindDialog">绑定我的车辆</el-button>
      <el-button type="primary"
                 v-if="data.user.roleList.includes('ADMIN')"
                 plain
                 @click="handleSetType">设置车辆类型</el-button>
    </div>

    <!-- 表格区 -->
    <div class="card" style="margin-bottom: 20px; padding: 20px;">
      <el-table
          :data="data.tableData"
          stripe
          style="width: 100%"
          :row-style="{ height: '60px' }"
          :cell-style="{ padding: '12px 8px' }"
          @selection-change="handleSelectionChange"
      >
        <!-- 多选 -->
        <el-table-column type="selection" width="60" align="center" />

        <!-- 车牌号 -->
        <el-table-column prop="name" label="车牌号" min-width="150" align="center">
          <template #default="{ row }">
            <span style="font-weight: 500; color: #409EFF;">{{ row.name }}</span>
          </template>
        </el-table-column>

        <!-- 车辆类型 -->
        <el-table-column label="车辆类型" min-width="140" align="center">
          <template #default="{ row }">
            <el-tag
                :type="getTypeTagType(row.type)"
                size="default"
                style="padding: 8px 16px; font-weight: 500;"
            >
              {{ typeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 所属用户（仅管理员可见） -->
        <el-table-column
            prop="userName"
            label="所属用户"
            min-width="160"
            align="center"
            v-if="data.user.roleList.includes('ADMIN')"
        >
          <template #default="{ row }">
            <span style="font-weight: 500;">{{ row.userName }}</span>
          </template>
        </el-table-column>

        <!-- 月卡开始时间 -->
        <el-table-column label="月卡开始时间" min-width="180" align="center">
          <template #default="{ row }">
            <span v-if="row.startTime" style="color: #67C23A; font-weight: 500;">
              {{ formatDateTime(row.startTime) }}
            </span>
            <span v-else style="color: #909399;">--</span>
          </template>
        </el-table-column>

        <!-- 月卡结束时间 -->
        <el-table-column label="月卡结束时间" min-width="180" align="center">
          <template #default="{ row }">
            <span v-if="row.endTime" :style="{ color: getEndTimeColor(row.endTime), fontWeight: '500' }">
              {{ formatDateTime(row.endTime) }}
            </span>
            <span v-else style="color: #909399;">--</span>
          </template>
        </el-table-column>

        <!-- 购买月卡 -->
        <el-table-column label="购买月卡" min-width="180" align="center" v-if="data.user.roleList.includes('USER')">
          <template #default="{ row }">
            <el-button
                type="success"
                size="default"
                :disabled="row.type === 2"
                @click="purchaseMonthly(row)"
                style="min-width: 120px; padding: 10px 20px;"
            >
              {{ row.type === 2 ? '已购买' : '购买月卡' }}
            </el-button>
          </template>
        </el-table-column>

        <!-- 编辑 & 删除 -->
        <el-table-column label="操作" min-width="200" fixed="right" align="center">
          <template #default="{ row }">
            <div style="display: flex; justify-content: center; gap: 15px; align-items: center;">
              <el-button
                  type="primary"
                  circle
                  :icon="Edit"
                  @click="handleEdit(row)"
                  size="default"
                  style="padding: 12px;"
              />
              <el-button
                  type="danger"
                  circle
                  :icon="Delete"
                  @click="del(row.id)"
                  size="default"
                  style="padding: 12px;"
              />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="card" v-if="data.total" style="padding: 20px; text-align: center;">
      <el-pagination
          @current-change="load"
          background
          layout="total, prev, pager, next"
          :page-size="data.pageSize"
          v-model:current-page="data.pageNum"
          :total="data.total"
      />
    </div>

    <!-- 新增/编辑 弹窗 -->
    <el-dialog
        title="车辆信息"
        v-model="data.formVisible"
        width="500px"
        :close-on-click-modal="false"
        destroy-on-close
    >
      <el-form
          :model="data.form"
          :rules="data.rules"
          ref="formRef"
          label-width="100px"
          style="padding: 30px 40px"
      >
        <el-form-item label="车牌号" prop="name">
          <el-input
              v-model="data.form.name"
              placeholder="请输入车牌号，例如：粤A 12345"
              style="height: 40px;"
              @blur="validatePlateNumber"
          />
          <div style="font-size: 12px; color: #909399; margin-top: 5px;">
            支持格式：粤A 12345、京B DF1234（新能源）
          </div>
        </el-form-item>

        <el-form-item
            label="所属用户"
            prop="userId"
            v-if="data.user.roleList.includes('ADMIN')"
        >
          <el-select
              v-model="data.form.userId"
              style="width: 100%; height: 40px;"
              placeholder="请选择用户"
          >
            <el-option
                v-for="item in data.userList"
                :key="item.id"
                :value="item.id"
                :label="item.username"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="text-align: center; padding: 20px 0;">
          <el-button @click="data.formVisible = false" style="padding: 10px 30px;">取消</el-button>
          <el-button type="primary" @click="save" style="padding: 10px 30px; margin-left: 15px;">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 设置车辆类型弹窗 -->
    <el-dialog
        title="设置车辆类型"
        v-model="data.setTypeVisible"
        width="450px"
        :close-on-click-modal="false"
        destroy-on-close
    >
      <div style="padding: 20px 30px;">
        <div style="margin-bottom: 20px; color: #666; font-size: 14px;">
          已选择 <span style="color: #409EFF; font-weight: 500;">{{ data.selectedVehicles.length }}</span> 辆车辆
        </div>

        <el-form label-width="100px">
          <el-form-item label="车辆类型">
            <el-radio-group v-model="data.selectedType" style="width: 100%;">
              <div style="display: flex; flex-direction: column; gap: 12px;">
                <el-radio :label="1" size="large">
                  <div style="display: flex; align-items: center; gap: 8px;">
                    <el-tag type="primary" size="small">内部车</el-tag>
                    <span style="color: #666; font-size: 13px;">内部员工车辆</span>
                  </div>
                </el-radio>
                <el-radio :label="2" size="large">
                  <div style="display: flex; align-items: center; gap: 8px;">
                    <el-tag type="success" size="small">月租车</el-tag>
                    <span style="color: #666; font-size: 13px;">已购买月租服务</span>
                  </div>
                </el-radio>
                <el-radio :label="3" size="large">
                  <div style="display: flex; align-items: center; gap: 8px;">
                    <el-tag type="warning" size="small">临时车</el-tag>
                    <span style="color: #666; font-size: 13px;">临时访客车辆</span>
                  </div>
                </el-radio>
                <el-radio :label="4" size="large">
                  <div style="display: flex; align-items: center; gap: 8px;">
                    <el-tag type="danger" size="small">黑名单</el-tag>
                    <span style="color: #666; font-size: 13px;">禁止通行车辆</span>
                  </div>
                </el-radio>
              </div>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <div style="text-align: center; padding: 20px 0;">
          <el-button @click="data.setTypeVisible = false" style="padding: 10px 30px;">取消</el-button>
          <el-button
              type="primary"
              @click="confirmSetType"
              :disabled="!data.selectedType"
              style="padding: 10px 30px; margin-left: 15px;"
          >确认设置</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
        title="绑定车辆"
        v-model="data.bindDialogVisible"
        width="30%"
        :close-on-click-modal="false"
        destroy-on-close
    >

      <el-form :model="data.bindForm" :rules="data.bindRules" ref="bindFormRef" label-width="80px">
        <el-form-item label="车牌号" prop="name">
          <el-input
              v-model="data.bindForm.name"
              placeholder="请输入车牌号，例如：粤A 12345"
              @blur="validateBindPlateNumber"
          />
          <div style="font-size: 12px; color: #909399; margin-top: 5px;">
            支持格式：粤A 12345、京B DF1234（新能源）
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.bindDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="doBindVehicle">绑定</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {reactive, ref, onMounted} from 'vue'
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Delete, Edit} from '@element-plus/icons-vue'

// 车牌校验正则：支持传统蓝牌、黄牌和新能源车牌
// 传统蓝黄牌：省份简称 + 大写字母 + 空格 + 5 位字母或数字
// 新能源车牌：省份简称 + 大写字母 + 空格 + D/F + 5 位字母或数字
const platePattern = /^[\u4e00-\u9fa5][A-Z]\s[A-HJ-NP-Z0-9]{5}$|^[\u4e00-\u9fa5][A-Z]\s[DF][A-HJ-NP-Z0-9]{5}$/;

// 车牌号验证函数
function validatePlate(rule, value, callback) {
  if (!value) {
    return callback(new Error('请输入车牌号'));
  }

  // 去除多余空格并规范化
  const normalizedValue = value.trim().replace(/\s+/g, ' ');

  if (!platePattern.test(normalizedValue)) {
    return callback(new Error('请输入正确的车牌号格式，例如：粤A 12345 或 粤B DF1234'));
  }
  callback();
}

// 手动验证车牌号的函数
const validatePlateNumber = () => {
  if (data.form.name) {
    data.form.name = data.form.name.trim().replace(/\s+/g, ' ').toUpperCase();
  }
}

const validateBindPlateNumber = () => {
  if (data.bindForm.name) {
    data.bindForm.name = data.bindForm.name.trim().replace(/\s+/g, ' ').toUpperCase();
  }
}

const bindFormRef = ref() // 确保有这个ref

const data = reactive({
  user: JSON.parse(localStorage.getItem('loginUser') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 5,
  formVisible: false,
  form: {},
  ids: [],
  name: null,
  userList: [],
  bindDialogVisible: false,
  bindForm: {
    name: ''
  },
  rules: {
    name: [{validator: validatePlate, trigger: 'blur'}],
    userId: [{required: true, message: '请选择用户', trigger: 'blur'}]
  },
  bindRules: {
    name: [{validator: validatePlate, trigger: 'blur'}]
  },
  // 设置车辆类型相关
  setTypeVisible: false,
  selectedType: null,
  selectedVehicles: []
})

const formRef = ref()

// 加载用户列表（用于管理员选择所属用户）
const loadUser = () => {
  request.get('/user/selectAll').then(res => {
    if (res.code === 200) data.userList = res.data
    else ElMessage.error(res.msg)
  })
}

// 加载车辆列表
const load = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    name: data.name
  }
  // 普通用户只能查询自己的
  if (!data.user.roleList.includes('ADMIN')) {
    params.userId = data.user.id
  }
  request.get('/vehicle/selectPage', {params}).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total
  })
}

// 打开新增弹窗
const handleAdd = () => {
  data.form = {}
  if (!data.user.roleList.includes('ADMIN')) {
    data.form.userId = data.user.id
  }
  data.formVisible = true
}

// 车辆类型文字映射
const typeText = t => {
  switch (t) {
    case 1:
      return '内部车'
    case 2:
      return '月租车'
    case 3:
      return '临时车'
    case 4:
      return '黑名单'
    default:
      return '未知'
  }
}

// 车辆类型标签颜色映射
const getTypeTagType = t => {
  switch (t) {
    case 1:
      return 'primary'
    case 2:
      return 'success'
    case 3:
      return 'warning'
    case 4:
      return 'danger'
    default:
      return 'info'
  }
}

// 格式化日期时间
const formatDateTime = dateStr => {
  if (!dateStr) return '--'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).replace(/\//g, '-')
}

// 获取结束时间颜色（根据是否过期）
const getEndTimeColor = endTimeStr => {
  if (!endTimeStr) return '#909399'
  const endTime = new Date(endTimeStr)
  const now = new Date()
  const diffDays = Math.ceil((endTime - now) / (1000 * 60 * 60 * 24))

  if (diffDays < 0) return '#F56C6C' // 已过期-红色
  if (diffDays <= 7) return '#E6A23C' // 7天内到期-橙色
  return '#67C23A' // 正常-绿色
}

// 打开编辑弹窗
const handleEdit = row => {
  data.form = {...row}
  data.formVisible = true
}

// 新增
const add = () => {
  // 提交前再次验证车牌号格式
  if (!platePattern.test(data.form.name)) {
    ElMessage.error('车牌号格式不正确，请检查后重新输入')
    return
  }

  request.post('/vehicle/add', data.form).then(res => {
    if (res.code === 200) {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(error => {
    ElMessage.error('操作失败，请检查车牌号格式或联系管理员')
  })
}

// 更新
const update = () => {
  // 提交前再次验证车牌号格式
  if (!platePattern.test(data.form.name)) {
    ElMessage.error('车牌号格式不正确，请检查后重新输入')
    return
  }

  request.put('/vehicle/update', data.form).then(res => {
    if (res.code === 200) {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(error => {
    ElMessage.error('操作失败，请检查车牌号格式或联系管理员')
  })
}

// 删除
const del = id => {
  ElMessageBox.confirm('解绑后数据无法恢复，是否继续？', '提示', {type: 'warning'})
      .then(() => {
        request.delete(`/vehicle/delete/${id}`).then(res => {
          if (res.code === 200) {
            ElMessage.success('解绑成功')
            load()
          } else ElMessage.error(res.msg)
        })
      })
}

// 批量删除
const handleSelectionChange = rows => {
  data.ids = rows.map(v => v.id)
  data.selectedVehicles = rows // 保存选中的车辆信息，用于设置类型功能
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning('请选择要解绑的车辆')
    return
  }
  ElMessageBox.confirm('解绑后数据无法恢复，是否继续？', '提示', {type: 'warning'})
      .then(() => {
        request.delete('/vehicle/delete/batch', {data: data.ids}).then(res => {
          if (res.code === 200) {
            ElMessage.success('解绑成功')
            load()
          } else ElMessage.error(res.msg)
        })
      })
}

// 打开设置车辆类型弹窗
const handleSetType = () => {
  if (!data.ids.length) {
    ElMessage.warning('请选择要设置类型的车辆')
    return
  }
  data.selectedType = null
  data.setTypeVisible = true
}

// 确认设置车辆类型
const confirmSetType = () => {
  if (!data.selectedType) {
    ElMessage.warning('请选择车辆类型')
    return
  }

  const typeNames = {
    1: '内部车',
    2: '月租车',
    3: '临时车',
    4: '黑名单'
  }

  ElMessageBox.confirm(
      `确认将选中的 ${data.selectedVehicles.length} 辆车辆设置为【${typeNames[data.selectedType]}】？`,
      '确认设置',
      {type: 'info'}
  ).then(() => {
    request.post(`/vehicle/setType/${data.selectedType}`, data.ids).then(res => {
      if (res.code === 200) {
        ElMessage.success('设置成功')
        data.setTypeVisible = false
        load() // 重新加载数据
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(() => {
      ElMessage.error('设置失败，请重试')
    })
  })
}

const openBindDialog = () => {
  data.bindForm = {name: ''}
  data.bindDialogVisible = true
}

const doBindVehicle = () => {
  bindFormRef.value.validate(valid => {
    if (!valid) return

    // 提交前再次验证车牌号格式
    if (!platePattern.test(data.bindForm.name)) {
      ElMessage.error('车牌号格式不正确，请检查后重新输入')
      return
    }

    request.post('/vehicle/add', {
      name: data.bindForm.name,
      userId: data.user.id
    }).then(res => {
      if (res.code === 200) {
        ElMessage.success("绑定成功")
        data.bindDialogVisible = false
        load()
      } else {
        ElMessage.error(res.msg || "绑定失败")
      }
    }).catch(error => {
      ElMessage.error('绑定失败，请检查车牌号格式或联系管理员')
    })
  })
}

// 保存（新增或更新）
const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

// 重置查询
const reset = () => {
  data.name = null
  load()
}

import {useUserStore} from "@/stores/user.js";

const userStore = useUserStore()

//购买月卡后需要更新userStore
const updateInfo = () => {
  request.get('/user/getInfo').then(res => {

    userStore.setUser(res.data)
  })
}
// 购买月卡
const purchaseMonthly = row => {
  if (row.type === 2) return
  ElMessageBox.confirm(`确认为车辆【${row.name}】购买月卡？`, '提示', {type: 'info'})
      .then(() => {
        request.post(`/vehicle/recharge/${row.id}`).then(res => {
          if (res.code === 200) {
            //updateInfo()
            console.log(res.data)
            ElMessage.success('购买成功')
            load()
          } else ElMessage.error(res.msg)
        })
      })
}

// 初始加载
onMounted(() => {
  loadUser()
  load()
})
</script>