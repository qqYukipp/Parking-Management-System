<template>
  <div>
    <div class="card">尊敬的 <span style="font-weight: bold">{{ data.user?.name }}</span> 您好！欢迎使用本系统，祝您今天有个好心情！</div>
    <div style="margin-top: 10px; display: flex; grid-gap: 10px">
      <div class="card" style="width: 50%">
        <div style="font-size: 20px; font-weight: bold; padding: 15px 20px">停车场车位情况</div>
        <div v-for="item in data.locationData" style="padding: 0 20px; margin-bottom: 15px">
          <div style="font-size: 16px; font-weight: bold; text-align: center; height: 30px; line-height: 30px; background-color: #e5f8fa">{{item.name}}</div>
          <div style="margin-top: 20px">
            <el-row>
              <el-col :span="3" v-for="it in item.parkingLots" style="margin-bottom: 10px">
                <div style="text-align: center">
                  <img v-if="it.status === '占用'" src="../../assets/images/no.png" alt="" style="width: 40px; height: 30px">
                  <img v-else src="../../assets/images/ok.png" alt="" style="width: 40px; height: 30px">
                </div>
                <div style="text-align: center">{{it.name}}</div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
      <div class="card" style="width: 50%">
        <div style="font-size: 20px; font-weight: bold; padding: 15px 20px">系统公告</div>
        <el-timeline style="max-width: 650px">
          <el-timeline-item color="#0bbd87" :key="notice.id" v-for="notice in data.noticeList" placement="top" :timestamp="notice.time">
            <div style="font-weight: bold; font-size: 15px">{{ notice.title }}</div>
            <div style="margin-top: 10px; line-height: 20px">{{ notice.content }}</div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
const data = reactive({
  user: JSON.parse(localStorage.getItem('loginUser') || '{}'),
  noticeList: [],
  locationData: []
})
const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    data.noticeList = res.data
  })
}

const loadLocation = () => {
  request.get('/location/selectAll').then(res => {
    if (res.code === 200) {
      data.locationData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadNotice()
loadLocation()
</script>

