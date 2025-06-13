<!--
<template>
  <div>
    &lt;!&ndash;头部&ndash;&gt;
    <div class="systemuser-header">
      <div class="systemuser-header-left">
        <img src="@/assets/images/logo.png" alt="">
        <div class="title">项目前台</div>
      </div>
      <div class="systemuser-header-center">
        <div class="systemuser-header-nav">
          <el-menu :default-active="router.currentRoute.value.path" mode="horizontal" router>
						<el-menu-item index="/systemuser/home">首页</el-menu-item>
						<el-menu-item index="/systemuser/person">个人中心</el-menu-item>
          </el-menu>
        </div>
      </div>
      <div class="systemuser-header-right">
        <div v-if="!data.user.username">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="systemuser-header-dropdown">
              <img :src="data.user.avatar" alt="">
              <div style="margin-left: 10px">
                <span>{{ data.user.name }}</span><el-icon><arrow-down /></el-icon>
              </div>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    &lt;!&ndash;主体&ndash;&gt;
    <div>
      <router-view ref="child" @updateUser="updateUser" />
    </div>
  </div>

</template>

<script setup>
import {reactive} from "vue";
import router from "@/router";

const data = reactive({
  user: JSON.parse(localStorage.getItem("loginUser") || '{}'),
})

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('loginUser') || '{}')   // 重新获取下用户的最新信息
}

// 退出登录
const logout = () => {
  localStorage.removeItem("loginUser");
  router.push("/login");
}
</script>

<style scoped>
  @import "@/assets/css/systemuser.css";
</style>-->
