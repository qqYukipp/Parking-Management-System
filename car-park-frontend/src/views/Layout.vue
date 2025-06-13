<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <!-- 左侧Logo和系统名称 -->
          <div class="header-left">
            <div class="logo">
              <img src="@/assets/images/logo.png" alt="Logo" class="logo-img" />
            </div>
            <h1 class="system-title">停车场管理系统</h1>
          </div>

          <!-- 右侧用户信息和下拉菜单 -->
          <div class="header-right">
            <el-dropdown @command="handleCommand" placement="bottom-end">
              <div class="user-info">
                <span class="user-name">{{ userStore.userInfo.nickName || '管理员' }}</span>
                <el-avatar
                    :size="36"
                    :src="userStore.userInfo.avatar"
                    class="user-avatar"
                >
                  <el-icon><UserFilled /></el-icon>
                </el-avatar>
                <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="person">
                    <el-icon><User /></el-icon>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="password">
                    <el-icon><Lock /></el-icon>
                    修改密码
                  </el-dropdown-item>
                  <el-dropdown-item command="logout" divided>
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>


      <el-container class="main-container">
        <!-- 菜单栏 -->
        <el-aside width="240px" class="sidebar">
          <el-scrollbar>
            <el-menu
                router
                default-active="/index"
                class="menu"
                :collapse="false"
            >
              <div v-for="menu in menuTree" :key="menu.path">
                <el-menu-item
                    :index="menu.path"
                    v-if="!menu.children || menu.children.length === 0 && menu.menuType==='MENU'"
                    class="menu-item"
                >
                  <el-icon><DocumentIcon /></el-icon>
                  <template #title>
                    {{ menu.menuName }}
                  </template>
                </el-menu-item>

                <el-sub-menu
                    :index="menu.path"
                    v-else-if="menu.children.length > 0 && menu.menuType==='MENU'"
                    class="sub-menu"
                >
                  <template #title>
                    <el-icon><FolderIcon /></el-icon>
                    <span>{{ menu.menuName }}</span>
                  </template>
                  <el-menu-item
                      :index="child.path"
                      v-for="child in menu.children"
                      :key="child.path"
                      class="sub-menu-item"
                  >
                    <el-icon><DocumentIcon /></el-icon>
                    <span>{{ child.menuName }}</span>
                  </el-menu-item>
                </el-sub-menu>
              </div>
            </el-menu>
          </el-scrollbar>
        </el-aside>

        <!-- 内容展示 -->
        <el-main class="main-content">
          <div class="content-wrapper">
            <router-view></router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { getRouterApi } from "@/api/login.js";
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/stores/user'
import {resetRouter} from "@/router/index.js";
import {
  UserFilled,
  SwitchButton,
  Document as DocumentIcon,
  Folder as FolderIcon,
  //Car as CarIcon,
  User,
  Lock,
  ArrowDown
} from '@element-plus/icons-vue';

const router = useRouter();
const menuTree = ref([])

// ② 调用 userStore
const userStore = useUserStore()

const getMenuTree = async () => {
  try {
    const res = await getRouterApi();
    if (res.code === 200) {
      menuTree.value = res.data;
    }
  } catch (error) {
    console.error('获取菜单树失败:', error);
    ElMessage.error('获取菜单失败');
  }
}

const logout = () => {
  // 清空 Pinia 中的用户信息  有bug
  //userStore.setUser({})

  resetRouter()

  localStorage.removeItem('loginUser');
  localStorage.removeItem('Token');
  //  重置动态路由加载状态
  userStore.setDynamicRoutesLoaded(false);

  ElMessage.success('已退出登录');

  router.push('/login');
}

// 处理下拉菜单点击事件
const handleCommand = (command) => {
  switch (command) {
    case 'person':
      router.push('/person');
      break;
    case 'password':
      router.push('/password');
      break;
    case 'logout':
      logout();
      break;
  }
}



// 用 Pinia 状态来初始化（Pinia 已在页面加载时从 localStorage 读过一次）
onMounted(() => {
  getMenuTree()
})

/*// 用户数据
const userData = reactive({
  nickName: '',
  avatar: ''
})*/
</script>

<style scoped>
.common-layout {
  height: 100vh;
  background: #f0f2f5;
}

/* 头部样式 */
.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: none;
  padding: 0 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo {
  width: 48px;
  height: 48px;
  //background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
  //border: 2px solid rgba(255, 255, 255, 0.3);
}

.system-title {
  color: #ffffff;
  font-size: 24px;
  font-weight: 600;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  letter-spacing: 1px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.1);
  padding: 8px 16px;
  border-radius: 24px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-1px);
}

.dropdown-icon {
  color: rgba(255, 255, 255, 0.8);
  font-size: 12px;
  transition: transform 0.3s ease;
}

.user-info:hover .dropdown-icon {
  transform: rotate(180deg);
}

.user-name {
  color: #ffffff;
  font-weight: 500;
  font-size: 14px;
}

.user-avatar {
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.logout-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #ffffff;
  font-weight: 500;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-1px);
}

/* 下拉菜单样式 */
:deep(.el-dropdown-menu) {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  padding: 8px 0;
  min-width: 160px;
}

:deep(.el-dropdown-menu__item) {
  padding: 12px 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #333;
  transition: all 0.3s ease;
  margin: 2px 8px;
  border-radius: 8px;
}

:deep(.el-dropdown-menu__item:hover) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
}

:deep(.el-dropdown-menu__item.is-divided) {
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  margin-top: 8px;
  padding-top: 16px;
}

/* 主容器样式 */
.main-container {
  height: calc(100vh - 60px);
}

/* 侧边栏样式 */
.sidebar {
  background: #ffffff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  border-right: 1px solid #e8e8f0;
}

.menu {
  border: none;
  background: transparent;
}

.menu-item, .sub-menu-item {
  margin: 4px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.menu-item:hover, .sub-menu-item:hover {
  background: #f0f7ff;
  color: #1890ff;
}

.menu-item.is-active, .sub-menu-item.is-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
}

.sub-menu {
  margin: 4px 0;
}

.sub-menu .el-sub-menu__title {
  margin: 0 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.sub-menu .el-sub-menu__title:hover {
  background: #f0f7ff;
  color: #1890ff;
}

/* 主内容区样式 */
.main-content {
  background: #f0f2f5;
  padding: 24px;
  overflow-y: auto;
}

.content-wrapper {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  min-height: calc(100vh - 148px);
  padding: 24px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    padding: 0 16px;
  }

  .system-title {
    font-size: 18px;
  }

  .user-name {
    display: none;
  }

  .sidebar {
    width: 64px !important;
  }

  .main-content {
    padding: 16px;
  }
}

/* 滚动条美化 */
.el-scrollbar :deep(.el-scrollbar__thumb) {
  background: rgba(144, 147, 153, 0.3);
  border-radius: 4px;
}

.el-scrollbar :deep(.el-scrollbar__thumb:hover) {
  background: rgba(144, 147, 153, 0.5);
}

.logo-img {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  //border: 2px solid white;
}

</style>