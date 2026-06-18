<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { getAuth, clearAuth, isLoggedIn } from './auth'

const router = useRouter()

// isLoggedIn 现在是响应式 computed，登录/退出后导航栏会立即更新
const loggedIn = isLoggedIn
// 计算属性：当前登录的用户名
const username = computed(() => getAuth()?.username ?? '')

// 退出登录：清除认证信息并跳转到登录页
function logout() {
  clearAuth()
  router.push('/login')
}
</script>

<template>
  <!-- ── 顶部导航栏（登录后显示）— 二次元风格 ──────────────────── -->
  <nav v-if="loggedIn" class="navbar">
    <div class="navbar-inner">
      <!-- 左侧：品牌标识 -->
      <div class="navbar-left">
        <span class="navbar-logo">📰</span>
        <span class="navbar-brand">新闻管理系统</span>
        <span class="navbar-kawaii">✿</span>
      </div>
      <!-- 右侧：导航链接和用户信息 -->
      <div class="navbar-links">
        <router-link to="/" class="nav-link">
          <span class="nav-icon">📋</span>新闻列表
        </router-link>
        <router-link to="/admin" class="nav-link">
          <span class="nav-icon">✍️</span>后台发布
        </router-link>
        <div class="navbar-divider"></div>
        <div class="navbar-user">
          <span class="user-avatar">{{ username.charAt(0).toUpperCase() }}</span>
          <span class="navbar-welcome">{{ username }}</span>
        </div>
        <button class="btn btn-danger btn-sm btn-logout" @click="logout">
          退出
        </button>
      </div>
    </div>
  </nav>

  <!-- ── 页面路由出口 ───────────────────────────────────────── -->
  <router-view />
</template>

<style scoped>
/* ── 导航栏整体样式 — 二次元渐变 ──────────────── */
.navbar {
  /* 二次元紫粉渐变背景 */
  background: linear-gradient(135deg, #7b1fa2 0%, #ce93d8 40%, #f8bbd0 70%, #90caf9 100%);
  padding: 0 24px;
  box-shadow: 0 4px 24px rgba(206, 147, 216, 0.3);
  position: sticky;
  top: 0;
  z-index: 100;
}

.navbar-inner {
  max-width: 860px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* ── 左侧品牌区域 ──────────────────────────────── */
.navbar-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.navbar-logo {
  font-size: 24px;
  /* 轻微弹跳动画 */
  animation: logoBounce 2s ease infinite;
}

@keyframes logoBounce {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  25% { transform: translateY(-3px) rotate(-5deg); }
  75% { transform: translateY(-2px) rotate(5deg); }
}

.navbar-brand {
  color: #fff;
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 1px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.15);
}

/* 可爱的小花朵装饰 */
.navbar-kawaii {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
  animation: kawaiiSpin 4s ease-in-out infinite;
}

@keyframes kawaiiSpin {
  0%, 100% { transform: rotate(0deg) scale(1); }
  50% { transform: rotate(15deg) scale(1.2); }
}

/* ── 右侧链接区域 ──────────────────────────────── */
.navbar-links {
  display: flex;
  align-items: center;
  gap: 6px;
}

/* 导航链接样式 */
.nav-link {
  color: rgba(255, 255, 255, 0.85);
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  padding: 6px 14px;
  border-radius: 10px;
  transition: all 0.25s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.nav-link:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.18);
  transform: translateY(-1px);
}

/* 当前激活的链接 */
.nav-link.router-link-active {
  color: #fff;
  background: rgba(255, 255, 255, 0.25);
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(255, 255, 255, 0.1);
}

.nav-icon {
  font-size: 14px;
}

/* 分隔线 */
.navbar-divider {
  width: 1px;
  height: 24px;
  background: rgba(255, 255, 255, 0.3);
  margin: 0 8px;
}

/* ── 用户信息区域 ──────────────────────────────── */
.navbar-user {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 用户头像（首字母圆形） — 二次元渐变 */
.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: linear-gradient(135deg, #fff59d, #f8bbd0);
  color: #7b1fa2;
  font-size: 13px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
}

.user-avatar:hover {
  transform: scale(1.1) rotate(-5deg);
}

.navbar-welcome {
  color: rgba(255, 255, 255, 0.9);
  font-size: 13px;
  font-weight: 500;
}

/* 小尺寸按钮 — 二次元风格 */
.btn-sm {
  padding: 6px 16px;
  font-size: 12px;
  border-radius: 10px;
}

.btn-logout {
  transition: all 0.25s ease;
}

.btn-logout:hover {
  transform: translateY(-1px);
}
</style>
