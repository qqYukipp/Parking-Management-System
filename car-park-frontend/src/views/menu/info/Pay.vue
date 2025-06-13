<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.userName" placeholder="请输入用户姓名查询" style="width: 240px; margin-right: 5px"></el-input>
      <el-input v-model="data.vehicleName" placeholder="请输入车牌号查询" style="width: 240px"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column prop="userName" label="用户"></el-table-column>
        <el-table-column prop="vehicleName" label="车牌号" width="100"></el-table-column>
        <el-table-column prop="locationName" label="区域名称" width="100"></el-table-column>
        <el-table-column prop="parkingLotName" label="车位编号" width="100"></el-table-column>
        <el-table-column prop="startTime" label="入场时间"></el-table-column>
        <el-table-column prop="endTime" label="出场时间"></el-table-column>
        <el-table-column prop="minutes" label="停车时间(分钟)"></el-table-column>
        <el-table-column prop="price" label="停车费用">
          <template v-slot="scope">
            <span style="color: red">￥{{scope.row.price}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '未缴费'" type="danger">{{scope.row.status}}</el-tag>
            <el-tag v-if="scope.row.status === '已缴费'" type="success">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right" v-if="data.user.roleList.includes('USER') ">
          <template v-slot="scope">
            <el-button type="primary" @click="pay(scope.row)" :disabled="scope.row.status === '已缴费'">缴费</el-button>
          </template>
        </el-table-column>
      </el-table>

    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";
const baseUrl = import.meta.env.VITE_BASE_URL
const data = reactive({
  user: JSON.parse(localStorage.getItem('loginUser') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  ids: [],
  userName: null,
  vehicleName: null,
  userList: [],
  vehicleList: [],
  locationList: [],
  parkingList: [],
  flag: true
})

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
  request.get('/payLot/selectAll', {
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
// 加载表格数据
const load = () => {
  request.get('/pay/selectPage', {
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

// 更新
const pay = (row) => {
  request.put('/pay/update', row).then(res => {
    if (res.code === 200) {
      data.formVisible = false
      ElMessage.success('缴费成功')
      load()
    } else {
      ElMessage.error(res.msg)
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