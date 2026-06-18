<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { saveAuth } from '../auth'

const router  = useRouter()
const email    = ref('')
const username = ref('')
const password = ref('')
const loading  = ref(false)
const tip      = ref<{ type: 'success' | 'error'; text: string } | null>(null)

// 注册函数：发送注册请求，成功后跳转登录页
async function register() {
  if (!email.value || !username.value || !password.value) {
    tip.value = { type: 'error', text: '请填写所有字段' }
    return
  }
  loading.value = true
  tip.value = null
  try {
    await axios.post('/api/users/register', {
      email:    email.value,
      username: username.value,
      password: password.value
    })
    tip.value = { type: 'success', text: '注册成功！' }
    setTimeout(() => router.push('/login'), 1200)
  } catch (err: any) {
    const msg = err.response?.data?.message ?? '注册失败，请重试'
    tip.value = { type: 'error', text: msg }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <!-- 注册页面：二次元风格全屏布局 -->
  <div class="auth-page">
    <!-- 🌸 樱花飘落粒子（CSS花瓣） -->
    <div class="sakura-container">
      <div v-for="i in 12" :key="'sakura-'+i" class="sakura" :class="'sakura-' + i"></div>
    </div>

    <!-- ✨ 闪光粒子 -->
    <div class="sparkle-container">
      <div v-for="i in 16" :key="'spark-'+i" class="sparkle" :class="'sparkle-' + i"></div>
    </div>

    <!-- 装饰性背景圆形 -->
    <div class="bg-circle circle-1"></div>
    <div class="bg-circle circle-2"></div>

    <!-- 注册卡片 -->
    <div class="card auth-card">
      <!-- 顶部装饰图标 -->
      <div class="auth-icon-wrap">
        <span class="auth-icon">📝</span>
        <span class="icon-star star-1">✦</span>
        <span class="icon-star star-2">✧</span>
        <span class="icon-star star-3">✦</span>
      </div>
      <h2 class="auth-title">创建新账号</h2>
      <p class="auth-subtitle">🌟 注册后即可发布和管理新闻 🌟</p>

      <!-- 邮箱输入 -->
      <div class="form-group">
        <label class="form-label">邮箱</label>
        <div class="input-wrap">
          <span class="input-icon">📧</span>
          <input class="form-input has-icon" v-model="email" type="email"
                 placeholder="example@email.com" @keyup.enter="register" />
        </div>
      </div>

      <!-- 用户名输入 -->
      <div class="form-group">
        <label class="form-label">用户名</label>
        <div class="input-wrap">
          <span class="input-icon">👤</span>
          <input class="form-input has-icon" v-model="username" type="text"
                 placeholder="请输入用户名" @keyup.enter="register" />
        </div>
      </div>

      <!-- 密码输入 -->
      <div class="form-group">
        <label class="form-label">密码</label>
        <div class="input-wrap">
          <span class="input-icon">🔒</span>
          <input class="form-input has-icon" v-model="password" type="password"
                 placeholder="请输入密码" @keyup.enter="register" />
        </div>
      </div>

      <!-- 提示信息 -->
      <p v-if="tip" :class="tip.type === 'success' ? 'tip-success' : 'tip-error'">
        {{ tip.type === 'success' ? '🎉 ' : '💦 ' }}{{ tip.text }}
      </p>

      <!-- 注册按钮 -->
      <button class="btn btn-primary btn-block" :disabled="loading" @click="register">
        <span v-if="loading" class="spinner"></span>
        {{ loading ? '注册中…' : '🌟 注 册 🌟' }}
      </button>

      <!-- 切换到登录 -->
      <p class="auth-switch">
        已有账号？<router-link to="/login">去登录 ♪</router-link>
      </p>
    </div>
  </div>
</template>

<style scoped>
/* ── 注册页面全屏布局 ──────────────────────────── */
.auth-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  position: relative;
  overflow: hidden;
  /* 二次元蓝紫渐变背景 */
  background: linear-gradient(135deg, #e8eaf6 0%, #ede7f6 25%, #e1f5fe 50%, #e0f2f1 75%, #fce4ec 100%);
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

/* 用CSS绘制仿真樱花花瓣 */
.sakura::before {
  content: '';
  display: block;
  width: 12px;
  height: 12px;
  background: radial-gradient(ellipse at 30% 30%, #e1bee7 0%, #ce93d8 40%, #ab47bc 80%, #8e24aa 100%);
  border-radius: 50% 0 50% 50%;
  box-shadow: inset 0 0 3px rgba(255, 255, 255, 0.4);
  transform: rotate(-45deg);
}

.sakura-1  { left: 8%;  animation-duration: 9s;  animation-delay: 0s; }
.sakura-1::before { width: 10px; height: 10px; }
.sakura-2  { left: 18%; animation-duration: 11s; animation-delay: 1.5s; }
.sakura-2::before { width: 14px; height: 14px; }
.sakura-3  { left: 28%; animation-duration: 8s;  animation-delay: 3s; }
.sakura-3::before { width: 8px; height: 8px; }
.sakura-4  { left: 38%; animation-duration: 10s; animation-delay: 0.8s; }
.sakura-4::before { width: 12px; height: 12px; }
.sakura-5  { left: 48%; animation-duration: 12s; animation-delay: 2s; }
.sakura-5::before { width: 15px; height: 15px; }
.sakura-6  { left: 55%; animation-duration: 9.5s; animation-delay: 4s; }
.sakura-6::before { width: 10px; height: 10px; }
.sakura-7  { left: 62%; animation-duration: 10.5s; animation-delay: 1s; }
.sakura-7::before { width: 13px; height: 13px; }
.sakura-8  { left: 70%; animation-duration: 8.5s; animation-delay: 2.5s; }
.sakura-8::before { width: 9px; height: 9px; }
.sakura-9  { left: 78%; animation-duration: 11.5s; animation-delay: 3.5s; }
.sakura-9::before { width: 14px; height: 14px; }
.sakura-10 { left: 85%; animation-duration: 9.2s; animation-delay: 0.5s; }
.sakura-10::before { width: 11px; height: 11px; }
.sakura-11 { left: 92%; animation-duration: 10.8s; animation-delay: 1.8s; }
.sakura-11::before { width: 12px; height: 12px; }
.sakura-12 { left: 45%; animation-duration: 12.5s; animation-delay: 4.5s; }
.sakura-12::before { width: 10px; height: 10px; }

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

.sparkle-1  { left: 12%; top: 25%; background: #b39ddb; animation-duration: 3.2s; animation-delay: 0s; }
.sparkle-2  { left: 28%; top: 55%; background: #90caf9; animation-duration: 4.1s; animation-delay: 0.6s; }
.sparkle-3  { left: 42%; top: 18%; background: #80cbc4; animation-duration: 3.6s; animation-delay: 1.2s; }
.sparkle-4  { left: 58%; top: 72%; background: #f48fb1; animation-duration: 2.9s; animation-delay: 0.4s; }
.sparkle-5  { left: 72%; top: 35%; background: #fff59d; animation-duration: 3.4s; animation-delay: 1.8s; }
.sparkle-6  { left: 88%; top: 48%; background: #b39ddb; animation-duration: 4.4s; animation-delay: 0.9s; }
.sparkle-7  { left: 18%; top: 78%; background: #90caf9; animation-duration: 3.9s; animation-delay: 2.2s; }
.sparkle-8  { left: 35%; top: 38%; background: #80cbc4; animation-duration: 2.7s; animation-delay: 0.3s; }
.sparkle-9  { left: 52%; top: 12%; background: #f48fb1; animation-duration: 3.8s; animation-delay: 1.5s; }
.sparkle-10 { left: 68%; top: 82%; background: #fff59d; animation-duration: 4.6s; animation-delay: 0.7s; }
.sparkle-11 { left: 82%; top: 28%; background: #b39ddb; animation-duration: 3.3s; animation-delay: 2s; }
.sparkle-12 { left: 8%;  top: 50%; background: #90caf9; animation-duration: 3.1s; animation-delay: 0.5s; }
.sparkle-13 { left: 48%; top: 88%; background: #80cbc4; animation-duration: 4.2s; animation-delay: 2.5s; }
.sparkle-14 { left: 62%; top: 42%; background: #f48fb1; animation-duration: 3.5s; animation-delay: 1s; }
.sparkle-15 { left: 78%; top: 68%; background: #fff59d; animation-duration: 2.8s; animation-delay: 1.7s; }
.sparkle-16 { left: 22%; top: 32%; background: #b39ddb; animation-duration: 4s; animation-delay: 0.2s; }

@keyframes sparkleAnim {
  0%, 100% { opacity: 0; transform: scale(0); }
  50% { opacity: 1; transform: scale(1.5); box-shadow: 0 0 10px currentColor; }
}

/* ── 装饰性背景圆形 ────────────────────────────── */
.bg-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
  pointer-events: none;
}

.circle-1 {
  width: 450px;
  height: 450px;
  background: linear-gradient(135deg, #90caf9, #b39ddb);
  top: -120px;
  left: -120px;
  animation: float1 11s ease-in-out infinite;
}

.circle-2 {
  width: 350px;
  height: 350px;
  background: linear-gradient(135deg, #f48fb1, #80cbc4);
  bottom: -100px;
  right: -100px;
  animation: float2 13s ease-in-out infinite;
}

@keyframes float1 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(25px, -25px) scale(1.06); }
}

@keyframes float2 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(-20px, 20px) scale(1.04); }
}

/* ── 注册卡片 ──────────────────────────────────── */
.auth-card {
  width: 100%;
  max-width: 420px;
  padding: 40px 36px;
  position: relative;
  z-index: 2;
  animation: cardSlideIn 0.6s ease-out;
  border-radius: 24px;
  border: 2px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 8px 40px rgba(144, 202, 249, 0.15), 0 2px 8px rgba(0, 0, 0, 0.04);
}

@keyframes cardSlideIn {
  from { opacity: 0; transform: translateY(30px) scale(0.95); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}

/* ── 顶部图标 + 小星星 ────────────────────────── */
.auth-icon-wrap {
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  background: linear-gradient(135deg, #90caf9 0%, #b39ddb 50%, #f48fb1 100%);
  border-radius: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 20px rgba(144, 202, 249, 0.4);
  position: relative;
  animation: iconFloat 3s ease-in-out infinite;
}

@keyframes iconFloat {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}

.auth-icon { font-size: 34px; }

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
  color: #1565c0;
  margin-bottom: 4px;
}

.auth-subtitle {
  text-align: center;
  color: #5c6bc0;
  font-size: 14px;
  margin-bottom: 28px;
  font-weight: 500;
}

/* ── 带图标的输入框 ────────────────────────────── */
.input-wrap { position: relative; }

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
  border-color: #c5cae9;
  border-radius: 14px;
}

.has-icon:focus {
  border-color: #90caf9;
  box-shadow: 0 0 0 4px rgba(144, 202, 249, 0.15);
}

/* ── 注册按钮 — 二次元渐变 ────────────────────── */
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
  background: linear-gradient(135deg, #90caf9 0%, #b39ddb 50%, #f48fb1 100%);
  box-shadow: 0 4px 16px rgba(144, 202, 249, 0.4);
}

.btn-primary:hover {
  box-shadow: 0 6px 24px rgba(144, 202, 249, 0.5);
}

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

@keyframes spin { to { transform: rotate(360deg); } }

/* ── 切换登录链接 ──────────────────────────────── */
.auth-switch {
  text-align: center;
  margin-top: 20px;
  font-size: 13px;
  color: #5c6bc0;
}

.auth-switch a {
  color: #1565c0;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.2s;
}

.auth-switch a:hover {
  color: #0d47a1;
  text-decoration: underline;
}
</style>
