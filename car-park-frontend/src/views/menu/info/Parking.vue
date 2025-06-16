<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.userName" placeholder="请输入用户姓名查询"
                v-if="data.user.roleList.includes('ADMIN')"
                style="width: 240px; margin-right: 5px"></el-input>
      <el-input v-model="data.vehicleName" placeholder="请输入车牌号查询" style="width: 240px"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      <el-button type="warning" plain style="margin-left: 10px"
                 v-if="data.user.roleList.includes('USER')"
                 @click="openBindDialog">绑定我的车辆</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px" >
      <el-button type="primary" plain @click="handleAdd">车辆入场</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" stripe>
        <el-table-column prop="userName" label="用户"></el-table-column>
        <el-table-column prop="vehicleName" label="车牌号"></el-table-column>
        <el-table-column prop="locationName" label="区域名称"></el-table-column>
        <el-table-column prop="parkingLotName" label="车位编号"></el-table-column>
        <el-table-column prop="startTime" label="入场时间"></el-table-column>
        <el-table-column prop="endTime" label="出场时间"></el-table-column>
        <el-table-column prop="status" label="状态"></el-table-column>
        <el-table-column label="操作" width="100" fixed="right" v-if="data.user.roleList.includes('ADMIN')">
          <template v-slot="scope">
            <el-button type="primary" @click="handleEdit(scope.row)" :disabled="scope.row.status === '已出场'">车辆出场</el-button>
          </template>
        </el-table-column>
      </el-table>

    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="停车信息" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :rules="data.rules" :model="data.form" label-width="80px"  style="padding: 20px 30px" ref="formRef">
        <el-form-item label="用户" prop="userId" v-if="data.flag">
          <el-select style="width: 100%" v-model="data.form.userId" @change="initVehicle">
            <el-option v-for="item in data.userList" :key="item.id" :value="item.id" :label="item.username"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="车辆" prop="vehicleId" v-if="data.flag">
          <el-select style="width: 100%" v-model="data.form.vehicleId">
            <el-option v-for="item in data.vehicleList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="区域" prop="locationId" v-if="data.flag">
          <el-select style="width: 100%" v-model="data.form.locationId" @change="initParkingLot">
            <el-option v-for="item in data.locationList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="车位" prop="parkingLotId" v-if="data.flag">
          <el-select style="width: 100%" v-model="data.form.parkingLotId">
            <el-option v-for="item in data.parkingLotList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="入场时间" prop="startTime" v-if="data.flag">
          <el-date-picker placeholder="请选择日期时间" type="datetime" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" v-model="data.form.startTime" style="width: 100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="出场时间" prop="endTime" v-if="!data.flag">
          <el-date-picker placeholder="请选择日期时间" type="datetime" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" v-model="data.form.endTime" style="width: 100%"></el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
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
          <el-input v-model="data.bindForm.name" placeholder="请输入车牌号 例如 粤X 12345" />
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
import { reactive, ref } from "vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";
const baseUrl = import.meta.env.VITE_BASE_URL

// 车牌校验正则：支持传统蓝牌、黄牌和新能源车牌
// 传统蓝黄牌：省份简称 + 大写字母 + 5 位字母或数字
// 新能源车牌：省份简称 + 大写字母 + D/F + 5 位字母或数字
const platePattern = /^[\u4e00-\u9fa5][A-Z]\s(?:[A-Z0-9]{5}|[DF][A-HJ-NP-Z0-9]{5})$/;

function validatePlate(rule, value, callback) {
  if (!value) {
    return callback(new Error('请输入车牌号'));
  }
  if (!platePattern.test(value)) {
    return callback(new Error('请输入正确的车牌号,确保省份与号码之间有一个空格'));
  }
  callback();
}

const data = reactive({
  user: JSON.parse(localStorage.getItem('loginUser') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 5,
  formVisible: false,
  form: {},
  userName: null,
  vehicleName: null,
  userList: [],
  vehicleList: [],
  locationList: [],
  parkingLotList: [], // 修复：应该是parkingLotList而不是parkingList
  bindDialogVisible: false,
  bindForm: {
    name: ''
  },
  flag: true,
  rules: {
    userId: [
      { required: true, message: '请选择用户', trigger: 'blur' },
    ],
    vehicleId: [
      { required: true, message: '请选择车辆', trigger: 'blur' },
    ],
    locationId: [
      { required: true, message: '请选择区域', trigger: 'blur' },
    ],
    parkingLotId: [
      { required: true, message: '请选择车位', trigger: 'blur' },
    ],
    startTime: [
      { required: true, message: '请选择入场时间', trigger: 'blur' },
    ],
    endTime: [
      { required: true, message: '请选择出场时间', trigger: 'blur' },
    ]
  },

  bindRules: {
    name: [
      { validator: validatePlate, trigger: 'blur' }
    ]
  }
})

const formRef = ref()
const bindFormRef = ref() // 确保有这个ref

const loadUser = () => {
  request.get('/user/selectAll').then(res => {
    if (res.code === 200) {
      data.userList = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadUser()

const initVehicle = (userId) => {
  request.get('/vehicle/selectAll', {
    params: {
      userId: userId
    }
  }).then(res => {
    if (res.code === 200) {
      data.vehicleList = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadLocation = () => {
  request.get('/location/selectAll').then(res => {
    if (res.code === 200) {
      data.locationList = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadLocation()

const initParkingLot = (locationId) => {
  request.get('/parkingLot/selectAll', {
    params: {
      locationId: locationId,
      status: '空闲'
    }
  }).then(res => {
    if (res.code === 200) {
      data.parkingLotList = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const load = () => {
  request.get('/parking/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userName: data.userName,
      vehicleName: data.vehicleName
    }
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total
  })
}

const handleAdd = () => {
  data.form = {}
  data.flag = true
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.flag = false
  data.formVisible = true
}


const openBindDialog = () => {
  data.bindForm = { name: '' }
  data.bindDialogVisible = true
}


const doBindVehicle = () => {
  bindFormRef.value.validate(valid => {
    if (!valid) return

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
    })
  })
}

const add = () => {
  request.post('/parking/add', data.form).then(res => {
    if (res.code === 200) {
      data.formVisible = false
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/parking/update', data.form).then(res => {
    if (res.code === 200) {
      data.formVisible = false
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const reset = () => {
  data.userName = null
  data.vehicleName = null
  load()
}

load()
</script>