<template>
  <div class="login-container">
    <!-- 背景层 -->
    <div class="background-overlay"></div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <div class="card-header">
        <h1 class="title">{{ isLogin ? 'Login' : 'Sign Up' }}</h1>
      </div>

      <div class="card-body">
        <!-- 登录表单 -->
        <div v-if="isLogin" class="form-container">
          <el-form
              :model="loginForm"
              :rules="loginRules"
              ref="loginRef"
              @keyup.enter="doLogin"
          >
            <div class="input-group">
              <el-form-item prop="username">
                <div class="input-wrapper">
                  <i class="input-icon fas fa-envelope"></i>
                  <el-input
                      v-model="loginForm.username"
                      placeholder="Username"
                      class="custom-input"

                  />
                </div>
              </el-form-item>
            </div>

            <div class="input-group">
              <el-form-item prop="password">
                <div class="input-wrapper">
                  <i class="input-icon fas fa-lock"></i>
                  <el-input
                      v-model="loginForm.password"
                      type="password"
                      placeholder="Password"
                      show-password
                      class="custom-input"
                  />
                </div>
              </el-form-item>
            </div>

            <div class="form-options">
              <label class="remember-me">
                <el-checkbox v-model="rememberMe" class="custom-checkbox">
                  Remember me
                </el-checkbox>
              </label>
              <a href="#" class="forgot-link">Forgot password?</a>
            </div>

            <el-button
                class="login-btn"
                @click="doLogin"
                :loading="loginLoading"
            >
              {{ loginLoading ? 'Logging in...' : 'Login' }}
            </el-button>
          </el-form>
        </div>

        <!-- 注册表单 -->
        <div v-else class="form-container">
          <el-form
              :model="regForm"
              :rules="regRules"
              ref="regRef"
              @keyup.enter="doRegister"
          >
            <div class="input-group">
              <el-form-item prop="username">
                <div class="input-wrapper">
                  <i class="input-icon fas fa-user"></i>
                  <el-input
                      v-model="regForm.username"
                      placeholder="Username"
                      class="custom-input"
                  />
                </div>
              </el-form-item>
            </div>

            <div class="input-group">
              <el-form-item prop="email">
                <div class="input-wrapper">
                  <i class="input-icon fas fa-envelope"></i>
                  <el-input
                      v-model="regForm.email"
                      placeholder="Email Address"
                      class="custom-input"
                  />
                </div>
              </el-form-item>
            </div>

            <div class="input-group">
              <el-form-item prop="password">
                <div class="input-wrapper">
                  <i class="input-icon fas fa-lock"></i>
                  <el-input
                      v-model="regForm.password"
                      type="password"
                      placeholder="Password"
                      show-password
                      class="custom-input"
                  />
                </div>
              </el-form-item>
            </div>

            <el-button
                class="login-btn"
                @click="doRegister"
                :loading="registerLoading"
            >
              {{ registerLoading ? 'Creating Account...' : 'Sign Up' }}
            </el-button>
          </el-form>
        </div>

        <!-- 切换登录/注册 -->
        <div class="switch-form">
          <span v-if="isLogin">
            Don't have an account?
            <a @click="isLogin = false" class="switch-link">Sign up now</a>
          </span>
          <span v-else>
            Already have an account?
            <a @click="isLogin = true" class="switch-link">Login here</a>
          </span>
        </div>

        <!-- 分割线 -->
        <div class="divider">
          <span>Log in via</span>
        </div>

        <!-- 第三方登录 -->
        <div class="oauth-container">
          <button class="oauth-btn google-btn" @click="oauth('google')">
            <i class="fab fa-google"></i>
            Google
          </button>
          <button class="oauth-btn github-btn" @click="oauth('github')">
            <i class="fab fa-github"></i>
            GitHub
          </button>
          <button class="oauth-btn gitee-btn" @click="oauth('gitee')">
            <i class="fab fa-git-alt"></i>
            Gitee
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { loginApi, registerApi,thirdLoginApi } from "@/api/login.js";
import router from "@/router/index.js";
import { useUserStore } from '@/stores/user'

const isLogin = ref(true);
const loginForm = ref({ username: "", password: "" });
const regForm = ref({ username: "", password: "", email: "" });
const rememberMe = ref(false);
const loginRef = ref(null);
const regRef = ref(null);
const loginLoading = ref(false);
const registerLoading = ref(false);
const userStore = useUserStore();
const loginRules = {
  username: [{ required: true, message: "请输入用户名或邮箱", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
};

const regRules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 4, message: "密码长度至少4位", trigger: "blur" }
  ],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "邮箱格式不正确", trigger: "blur" },
  ],
};

// 登录
async function doLogin() {
  if (!loginRef.value) return;

  try {
    const valid = await loginRef.value.validate();
    if (!valid) return;

    loginLoading.value = true;
    const res = await loginApi(loginForm.value);

    // TODO
    /*if (rememberMe.value) {
      localStorage.setItem("loginUser", JSON.stringify(res.data));
    }*/
    userStore.setUser(res.data)
    localStorage.setItem("Token", res.data.token);
    ElMessage.success("登录成功");
    router.push("/home");
  } catch (err) {
    ElMessage.error(err.message || "登录失败");
  } finally {
    loginLoading.value = false;
  }
}

// 注册
async function doRegister() {
  if (!regRef.value) return;

  try {
    const valid = await regRef.value.validate();
    if (!valid) return;

    registerLoading.value = true;

    const res = await registerApi(regForm.value);

    // 后端返回处理
    if (res.code === 200) {
      ElMessage.success("注册成功");
      localStorage.setItem("loginUser", JSON.stringify(res.data));
      localStorage.setItem("Token", res.data.token);
      regForm.value = { username: "", password: "", email: "" };
      router.push("/home");
    } else {
      // 避免空 msg 情况
      ElMessage.error(res.msg || "注册失败");
    }

  } catch (err) {
    // 处理 HTTP 错误或 JS 错误
    ElMessage.error(err?.message || "注册失败");
  } finally {
    registerLoading.value = false;
  }
}

// 第三方登录
function oauth(provider) {
  router.push(`/oauth2/authorization/${provider}`)
}


/*// 第三方登录
function oauth(provider) {
  // 直接跳转，绕过Vue Router
  //window.location.replace(`/oauth2/authorization/${provider}`);
  // 或者使用
   window.open(`/oauth2/authorization/${provider}`, '_self');
}*/
</script>

<style scoped>
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background-image: url("@/assets/images/login-background.jpg");
  background-size: cover;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.background-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  //backdrop-filter: blur(2px);
}

.login-card {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 400px;
  margin: 20px;
  background: rgba(255, 255, 255,0.15);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.25);
}

.card-header {
  padding: 40px 40px 20px;
  text-align: center;
}

.title {
  font-size: 2rem;
  font-weight: 300;
  color: white;
  margin: 0;
  letter-spacing: 1px;
}

.card-body {
  padding: 0 40px 40px;
}

.form-container {
  text-align: center;
  margin-bottom: 30px;
}

.input-group {
  margin-bottom: 20px;

}

.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: rgba(255, 255, 255, 0.7);
  font-size: 16px;
  z-index: 2;
}

:deep(.custom-input .el-input__wrapper) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  padding: 12px 16px 12px 48px;
  height: 48px;
  box-shadow: none;
  transition: all 0.3s ease;
}

:deep(.custom-input .el-input__wrapper:hover) {
  border-color: rgba(255, 255, 255, 0.5);
  background: rgba(255, 255, 255, 0.15);
}

:deep(.custom-input .el-input__wrapper.is-focus) {
  border-color: rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.2);
  box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.1);
}

:deep(.custom-input .el-input__inner) {
  color: white;
  font-size: 14px;
  font-weight: 400;
}

:deep(.custom-input .el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.6);
}

:deep(.custom-input .el-input__suffix) {
  color: rgba(255, 255, 255, 0.7);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  font-size: 14px;
}

.remember-me {
  display: flex;
  align-items: center;
}

:deep(.custom-checkbox .el-checkbox__label) {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

:deep(.custom-checkbox .el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.8);
}

.forgot-link {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  transition: color 0.3s ease;
}

.forgot-link:hover {
  color: white;
}

.login-btn {
  width: 50%;
  height: 48px;
  background: rgba(255, 255, 255, 0.6);
  color: #333;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 20px;
}

.login-btn:hover {
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.login-btn:active {
  transform: translateY(0);
}

.switch-form {
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  margin-bottom: 30px;
}

.switch-link {
  color: white;
  text-decoration: none;
  font-weight: 500;
  cursor: pointer;
  transition: opacity 0.3s ease;
}

.switch-link:hover {
  opacity: 0.8;
}

.divider {
  position: relative;
  text-align: center;
  margin: 30px 0;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  font-weight: 500;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: rgba(255, 255, 255, 0.2);
}

.divider span {
  background: rgba(255, 255, 255, 0.1);
  padding: 0 15px;
  backdrop-filter: blur(10px);
}

.oauth-container {
  display: flex;
  gap: 12px;
}

.oauth-btn {
  flex: 1;
  padding: 12px 8px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 500;
}

.oauth-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.4);
  transform: translateY(-1px);
}

.oauth-btn i {
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-card {
    margin: 10px;
    border-radius: 12px;
  }

  .card-header {
    padding: 30px 20px 15px;
  }

  .card-body {
    padding: 0 20px 30px;
  }

  .title {
    font-size: 1.75rem;
  }

  .oauth-container {
    flex-direction: column;
  }
}

/* Element Plus 表单验证样式 */
:deep(.el-form-item__error) {
  color: #ffcccb;
  font-size: 12px;
  margin-top: 4px;
}

:deep(.el-form-item.is-error .el-input__wrapper) {
  border-color: #ff6b6b !important;
}

.custom-input {
  width: 300px; /* 可根据需要自行调整 */
}
</style>