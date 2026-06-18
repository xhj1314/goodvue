<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { saveAuth } from '../auth'

const router   = useRouter()
const username = ref('')
const password = ref('')
const loading  = ref(false)
const tip      = ref<{ type: 'success' | 'error'; text: string } | null>(null)

// 登录函数：发送登录请求并保存认证信息
async function login() {
  if (!username.value || !password.value) {
    tip.value = { type: 'error', text: '请填写用户名和密码' }
    return
  }
  loading.value = true
  tip.value = null
  try {
    const res = await axios.post('/api/users/login', {
      username: username.value,
      password: password.value
    })
    // 保存登录态（含原始密码，用于后续 HTTP Basic 请求）
    saveAuth({
      username: res.data.username,
      email:    res.data.email,
      password: password.value
    })
    tip.value = { type: 'success', text: '登录成功！' }
    setTimeout(() => router.push('/'), 600)
  } catch (err: any) {
    const msg = err.response?.data?.message ?? '用户名或密码错误'
    tip.value = { type: 'error', text: msg }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <!-- 登录页面：二次元风格全屏布局 -->
  <div class="auth-page">
    <!-- 🌸 樱花飘落粒子（CSS花瓣） -->
    <div class="sakura-container">
      <div v-for="i in 15" :key="'sakura-'+i" class="sakura" :class="'sakura-' + i"></div>
    </div>

    <!-- ✨ 闪光粒子 -->
    <div class="sparkle-container">
      <div v-for="i in 20" :key="'spark-'+i" class="sparkle" :class="'sparkle-' + i"></div>
    </div>

    <!-- 装饰性背景圆形 -->
    <div class="bg-circle circle-1"></div>
    <div class="bg-circle circle-2"></div>

    <!-- 登录卡片 -->
    <div class="card auth-card">
      <!-- 顶部装饰图标 -->
      <div class="auth-icon-wrap">
        <span class="auth-icon">📰</span>
        <!-- 图标周围的小星星 -->
        <span class="icon-star star-1">✦</span>
        <span class="icon-star star-2">✧</span>
        <span class="icon-star star-3">✦</span>
      </div>
      <h2 class="auth-title">新闻管理系统</h2>
      <p class="auth-subtitle">✨ 登录你的账号，开始浏览新闻 ✨</p>

      <!-- 用户名输入 -->
      <div class="form-group">
        <label class="form-label">用户名</label>
        <div class="input-wrap">
          <span class="input-icon">👤</span>
          <input class="form-input has-icon" v-model="username" type="text"
                 placeholder="请输入用户名" @keyup.enter="login" />
        </div>
      </div>

      <!-- 密码输入 -->
      <div class="form-group">
        <label class="form-label">密码</label>
        <div class="input-wrap">
          <span class="input-icon">🔒</span>
          <input class="form-input has-icon" v-model="password" type="password"
                 placeholder="请输入密码" @keyup.enter="login" />
        </div>
      </div>

      <!-- 提示信息 -->
      <p v-if="tip" :class="tip.type === 'success' ? 'tip-success' : 'tip-error'">
        {{ tip.type === 'success' ? '🎉 ' : '💦 ' }}{{ tip.text }}
      </p>

      <!-- 登录按钮 -->
      <button class="btn btn-primary btn-block" :disabled="loading" @click="login">
        <span v-if="loading" class="spinner"></span>
        {{ loading ? '登录中…' : '✨ 登 录 ✨' }}
      </button>

      <!-- 切换到注册 -->
      <p class="auth-switch">
        还没有账号？<router-link to="/register">立即注册 ♪</router-link>
      </p>
    </div>
  </div>
</template>

<style scoped>
/* ── 登录页面全屏布局 ──────────────────────────── */
.auth-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  position: relative;
  overflow: hidden;
  /* 二次元渐变背景 */
  background: linear-gradient(135deg, #fce4ec 0%, #f3e5f5 25%, #e8eaf6 50%, #e0f7fa 75%, #fff3e0 100%);
}

/* ══════════════════════════════════════════════════
   🌸 樱花飘落动画 — CSS仿真花瓣
   ══════════════════════════════════════════════════ */
.sakura-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.sakura {
  position: absolute;
  opacity: 0;
  animation: sakuraFall linear infinite;
}

/* 用CSS绘制仿真樱花花瓣（椭圆形 + 缺口） */
.sakura::before {
  content: '';
  display: block;
  width: 12px;
  height: 12px;
  background: radial-gradient(ellipse at 30% 30%, #ffcdd2 0%, #f48fb1 40%, #f06292 80%, #ec407a 100%);
  border-radius: 50% 0 50% 50%;
  box-shadow: inset 0 0 3px rgba(255, 255, 255, 0.4);
  transform: rotate(-45deg);
}

/* 为每朵花瓣设置不同的起始位置、大小和速度 */
.sakura-1  { left: 5%;  animation-duration: 8s;  animation-delay: 0s; }
.sakura-1::before { width: 10px; height: 10px; }
.sakura-2  { left: 12%; animation-duration: 10s; animation-delay: 1s; }
.sakura-2::before { width: 14px; height: 14px; }
.sakura-3  { left: 20%; animation-duration: 7s;  animation-delay: 2s; }
.sakura-3::before { width: 8px; height: 8px; }
.sakura-4  { left: 28%; animation-duration: 9s;  animation-delay: 0.5s; }
.sakura-4::before { width: 12px; height: 12px; }
.sakura-5  { left: 35%; animation-duration: 11s; animation-delay: 3s; }
.sakura-5::before { width: 15px; height: 15px; }
.sakura-6  { left: 42%; animation-duration: 8s;  animation-delay: 1.5s; }
.sakura-6::before { width: 10px; height: 10px; }
.sakura-7  { left: 50%; animation-duration: 10s; animation-delay: 2.5s; }
.sakura-7::before { width: 13px; height: 13px; }
.sakura-8  { left: 58%; animation-duration: 7.5s; animation-delay: 0.8s; }
.sakura-8::before { width: 9px; height: 9px; }
.sakura-9  { left: 65%; animation-duration: 9.5s; animation-delay: 3.5s; }
.sakura-9::before { width: 14px; height: 14px; }
.sakura-10 { left: 72%; animation-duration: 8.5s; animation-delay: 1.2s; }
.sakura-10::before { width: 11px; height: 11px; }
.sakura-11 { left: 78%; animation-duration: 10.5s; animation-delay: 2.2s; }
.sakura-11::before { width: 12px; height: 12px; }
.sakura-12 { left: 85%; animation-duration: 7.2s; animation-delay: 0.3s; }
.sakura-12::before { width: 8px; height: 8px; }
.sakura-13 { left: 90%; animation-duration: 9.8s; animation-delay: 1.8s; }
.sakura-13::before { width: 13px; height: 13px; }
.sakura-14 { left: 95%; animation-duration: 8.2s; animation-delay: 2.8s; }
.sakura-14::before { width: 10px; height: 10px; }
.sakura-15 { left: 48%; animation-duration: 11.5s; animation-delay: 4s; }
.sakura-15::before { width: 11px; height: 11px; }

@keyframes sakuraFall {
  0% {
    opacity: 0;
    transform: translateY(-20px) rotate(0deg) translateX(0);
  }
  10% {
    opacity: 0.7;
  }
  25% {
    transform: translateY(25vh) rotate(90deg) translateX(15px);
  }
  50% {
    transform: translateY(50vh) rotate(180deg) translateX(-10px);
  }
  75% {
    transform: translateY(75vh) rotate(270deg) translateX(20px);
    opacity: 0.5;
  }
  100% {
    opacity: 0;
    transform: translateY(100vh) rotate(360deg) translateX(-5px);
  }
}

/* ══════════════════════════════════════════════════
   ✨ 闪光粒子动画
   ══════════════════════════════════════════════════ */
.sparkle-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.sparkle {
  position: absolute;
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: #fff;
  animation: sparkleAnim ease-in-out infinite;
}

/* 为每个闪光粒子设置不同位置和颜色 */
.sparkle-1  { left: 10%; top: 20%; background: #f8bbd0; animation-duration: 3s; animation-delay: 0s; }
.sparkle-2  { left: 25%; top: 60%; background: #ce93d8; animation-duration: 4s; animation-delay: 0.5s; }
.sparkle-3  { left: 40%; top: 15%; background: #90caf9; animation-duration: 3.5s; animation-delay: 1s; }
.sparkle-4  { left: 55%; top: 75%; background: #80deea; animation-duration: 2.8s; animation-delay: 0.3s; }
.sparkle-5  { left: 70%; top: 30%; background: #fff59d; animation-duration: 3.2s; animation-delay: 1.5s; }
.sparkle-6  { left: 85%; top: 50%; background: #f8bbd0; animation-duration: 4.2s; animation-delay: 0.8s; }
.sparkle-7  { left: 15%; top: 80%; background: #ce93d8; animation-duration: 3.8s; animation-delay: 2s; }
.sparkle-8  { left: 30%; top: 40%; background: #90caf9; animation-duration: 2.5s; animation-delay: 0.2s; }
.sparkle-9  { left: 50%; top: 10%; background: #80deea; animation-duration: 3.6s; animation-delay: 1.2s; }
.sparkle-10 { left: 65%; top: 85%; background: #fff59d; animation-duration: 4.5s; animation-delay: 0.7s; }
.sparkle-11 { left: 80%; top: 25%; background: #f8bbd0; animation-duration: 3.1s; animation-delay: 1.8s; }
.sparkle-12 { left: 5%;  top: 55%; background: #ce93d8; animation-duration: 2.9s; animation-delay: 0.4s; }
.sparkle-13 { left: 45%; top: 90%; background: #90caf9; animation-duration: 4.1s; animation-delay: 2.2s; }
.sparkle-14 { left: 60%; top: 45%; background: #80deea; animation-duration: 3.4s; animation-delay: 0.9s; }
.sparkle-15 { left: 75%; top: 70%; background: #fff59d; animation-duration: 2.7s; animation-delay: 1.6s; }
.sparkle-16 { left: 20%; top: 35%; background: #f8bbd0; animation-duration: 3.9s; animation-delay: 0.6s; }
.sparkle-17 { left: 35%; top: 65%; background: #ce93d8; animation-duration: 4.3s; animation-delay: 1.1s; }
.sparkle-18 { left: 90%; top: 40%; background: #90caf9; animation-duration: 3.3s; animation-delay: 2.5s; }
.sparkle-19 { left: 8%;  top: 15%; background: #80deea; animation-duration: 2.6s; animation-delay: 0.1s; }
.sparkle-20 { left: 92%; top: 80%; background: #fff59d; animation-duration: 3.7s; animation-delay: 1.4s; }

@keyframes sparkleAnim {
  0%, 100% {
    opacity: 0;
    transform: scale(0);
  }
  50% {
    opacity: 1;
    transform: scale(1.5);
    box-shadow: 0 0 8px currentColor;
  }
}

/* ── 装饰性背景圆形 ────────────────────────────── */
.bg-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.12;
  pointer-events: none;
}

.circle-1 {
  width: 500px;
  height: 500px;
  background: linear-gradient(135deg, #f8bbd0, #ce93d8);
  top: -150px;
  right: -150px;
  animation: float1 10s ease-in-out infinite;
}

.circle-2 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #90caf9, #80deea);
  bottom: -120px;
  left: -120px;
  animation: float2 12s ease-in-out infinite;
}

@keyframes float1 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(-30px, 30px) scale(1.05); }
}

@keyframes float2 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(20px, -20px) scale(1.08); }
}

/* ── 登录卡片 ──────────────────────────────────── */
.auth-card {
  width: 100%;
  max-width: 420px;
  padding: 40px 36px;
  position: relative;
  z-index: 2;
  animation: cardSlideIn 0.6s ease-out;
  /* 二次元风格：更圆的角 + 柔和阴影 */
  border-radius: 24px;
  border: 2px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 8px 40px rgba(206, 147, 216, 0.15), 0 2px 8px rgba(0, 0, 0, 0.04);
}

@keyframes cardSlideIn {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* ── 顶部图标 + 小星星 ────────────────────────── */
.auth-icon-wrap {
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  background: linear-gradient(135deg, #f8bbd0 0%, #ce93d8 50%, #90caf9 100%);
  border-radius: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 20px rgba(206, 147, 216, 0.4);
  position: relative;
  animation: iconFloat 3s ease-in-out infinite;
}

@keyframes iconFloat {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}

.auth-icon {
  font-size: 34px;
}

/* 图标周围旋转的小星星 */
.icon-star {
  position: absolute;
  font-size: 12px;
  color: #fff59d;
  animation: starTwinkle 1.5s ease-in-out infinite;
}

.star-1 { top: -6px; right: -6px; animation-delay: 0s; }
.star-2 { bottom: -4px; left: -8px; animation-delay: 0.5s; }
.star-3 { top: 50%; right: -10px; animation-delay: 1s; }

@keyframes starTwinkle {
  0%, 100% { opacity: 0.4; transform: scale(0.8); }
  50% { opacity: 1; transform: scale(1.2); }
}

/* ── 标题和副标题 ──────────────────────────────── */
.auth-title {
  text-align: center;
  font-size: 24px;
  font-weight: 700;
  color: #4a148c;
  margin-bottom: 4px;
}

.auth-subtitle {
  text-align: center;
  color: #9c27b0;
  font-size: 14px;
  margin-bottom: 28px;
  font-weight: 500;
}

/* ── 带图标的输入框 ────────────────────────────── */
.input-wrap {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16px;
  pointer-events: none;
}

.has-icon {
  padding-left: 42px;
  /* 二次元风格输入框 */
  border-color: #e1bee7;
  border-radius: 14px;
}

.has-icon:focus {
  border-color: #ce93d8;
  box-shadow: 0 0 0 4px rgba(206, 147, 216, 0.15);
}

/* ── 登录按钮 — 二次元渐变 ────────────────────── */
.btn-block {
  width: 100%;
  justify-content: center;
  margin-top: 12px;
  padding: 14px;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 2px;
  border-radius: 14px;
}

.btn-primary {
  background: linear-gradient(135deg, #f8bbd0 0%, #ce93d8 50%, #90caf9 100%);
  box-shadow: 0 4px 16px rgba(206, 147, 216, 0.4);
}

.btn-primary:hover {
  box-shadow: 0 6px 24px rgba(206, 147, 216, 0.5);
}

/* 加载旋转动画 */
.spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
  margin-right: 8px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ── 切换注册链接 ──────────────────────────────── */
.auth-switch {
  text-align: center;
  margin-top: 20px;
  font-size: 13px;
  color: #9c27b0;
}

.auth-switch a {
  color: #7b1fa2;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.2s;
}

.auth-switch a:hover {
  color: #4a148c;
  text-decoration: underline;
}
</style>
