<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { basicAuthHeader, getAuth } from '../auth'

// ── 状态 ──────────────────────────────────────────────────────────
const connStatus = ref<'checking' | 'ok' | 'fail'>('checking')
const title      = ref('')
const content    = ref('')
const submitting = ref(false)
const tip        = ref<{ type: 'success' | 'error'; text: string } | null>(null)

// ── 连通检测：验证后台认证是否正常 ──────────────────────────────
async function checkConnection() {
  connStatus.value = 'checking'
  try {
    await axios.get('/api/admins/hi', {
      headers: { Authorization: basicAuthHeader() }
    })
    connStatus.value = 'ok'
  } catch {
    connStatus.value = 'fail'
  }
}

// ── 发布新闻：提交标题和内容到后端 ──────────────────────────────
async function publish() {
  if (!title.value.trim()) {
    tip.value = { type: 'error', text: '标题不能为空' }
    return
  }
  if (!content.value.trim()) {
    tip.value = { type: 'error', text: '正文不能为空' }
    return
  }
  submitting.value = true
  tip.value = null
  try {
    await axios.post('/api/admins/news',
      { title: title.value, content: content.value },
      { headers: { Authorization: basicAuthHeader() } }
    )
    tip.value = { type: 'success', text: '新闻发布成功！' }
    title.value   = ''
    content.value = ''
  } catch (err: any) {
    const msg = err.response?.data?.message ?? '发布失败，请重试'
    tip.value = { type: 'error', text: msg }
  } finally {
    submitting.value = false
  }
}

// ── 编辑器「保存」图标触发发布 ────────────────────────────────────
function onEditorSave() { publish() }

onMounted(() => checkConnection())
</script>

<template>
  <div class="page-container">
    <!-- 🌸 樱花飘落装饰（CSS花瓣） -->
    <div class="sakura-container">
      <div v-for="i in 6" :key="'sakura-'+i" class="sakura" :class="'sakura-' + i"></div>
    </div>

    <!-- ✨ 闪光粒子 -->
    <div class="sparkle-container">
      <div v-for="i in 10" :key="'spark-'+i" class="sparkle" :class="'sparkle-' + i"></div>
    </div>

    <!-- 页面标题 — 二次元风格 -->
    <div class="page-header">
      <div class="header-deco">
        <span class="deco-star">✦</span>
        <span class="deco-star deco-star-2">✧</span>
      </div>
      <h1 class="page-title">✍️ 发布新闻</h1>
      <p class="page-desc">🌟 撰写并发布新的新闻资讯 🌟</p>
      <div class="title-underline">
        <span class="underline-dot"></span>
        <span class="underline-line"></span>
        <span class="underline-dot"></span>
      </div>
    </div>

    <div class="card admin-card">
      <!-- 连接状态指示条 — 二次元风格 -->
      <div class="conn-bar" :class="connStatus">
        <span class="conn-icon">
          <span v-if="connStatus === 'checking'" class="conn-spinner"></span>
          <span v-else-if="connStatus === 'ok'">✅</span>
          <span v-else>❌</span>
        </span>
        <span v-if="connStatus === 'checking'">检测后台连接中…</span>
        <span v-else-if="connStatus === 'ok'">后台连接正常，可以发布 ✨</span>
        <span v-else>
          后台认证失败 💦 —
          <button class="link-btn" @click="checkConnection">重试</button>
        </span>
      </div>

      <!-- 标题输入 — 二次元风格 -->
      <div class="form-group">
        <label class="form-label">📝 新闻标题</label>
        <input
          class="form-input title-input"
          v-model="title"
          placeholder="请输入新闻标题"
          :disabled="submitting"
        />
      </div>

      <!-- Markdown 编辑器 -->
      <div class="form-group">
        <label class="form-label">📄 新闻正文（Markdown）</label>
        <MdEditor
          v-model="content"
          language="zh-CN"
          :disabled="submitting"
          @onSave="onEditorSave"
          style="height: 480px;"
        />
      </div>

      <!-- 提示信息 -->
      <p v-if="tip" :class="tip.type === 'success' ? 'tip-success' : 'tip-error'">
        {{ tip.type === 'success' ? '🎉 ' : '💦 ' }}{{ tip.text }}
      </p>

      <!-- 发布按钮区域 — 二次元风格 -->
      <div class="action-row">
        <button
          class="btn btn-primary publish-btn"
          :disabled="submitting || connStatus !== 'ok'"
          @click="publish"
        >
          <span v-if="submitting" class="spinner"></span>
          {{ submitting ? '发布中…' : '🌟 发布新闻 🌟' }}
        </button>
        <span class="action-hint">或点击编辑器工具栏的保存图标</span>
      </div>
    </div>

    <!-- 底部装饰 -->
    <div class="page-footer-deco">
      <span>✿</span>
      <span>❀</span>
      <span>✿</span>
    </div>
  </div>
</template>

<style scoped>
/* ══════════════════════════════════════════════════
   🌸 樱花飘落装饰 — CSS仿真花瓣
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

.sakura-1 { left: 12%; animation-duration: 14s; animation-delay: 0s; }
.sakura-1::before { width: 10px; height: 10px; }
.sakura-2 { left: 30%; animation-duration: 16s; animation-delay: 2s; }
.sakura-2::before { width: 14px; height: 14px; }
.sakura-3 { left: 50%; animation-duration: 12s; animation-delay: 4s; }
.sakura-3::before { width: 8px; height: 8px; }
.sakura-4 { left: 68%; animation-duration: 15s; animation-delay: 1s; }
.sakura-4::before { width: 12px; height: 12px; }
.sakura-5 { left: 82%; animation-duration: 13s; animation-delay: 3s; }
.sakura-5::before { width: 10px; height: 10px; }
.sakura-6 { left: 42%; animation-duration: 14.5s; animation-delay: 5s; }
.sakura-6::before { width: 13px; height: 13px; }

@keyframes sakuraFall {
  0% { opacity: 0; transform: translateY(-20px) rotate(0deg) translateX(0); }
  10% { opacity: 0.7; }
  25% { transform: translateY(25vh) rotate(90deg) translateX(15px); }
  50% { transform: translateY(50vh) rotate(180deg) translateX(-10px); }
  75% { transform: translateY(75vh) rotate(270deg) translateX(20px); opacity: 0.5; }
  100% { opacity: 0; transform: translateY(100vh) rotate(360deg) translateX(-5px); }
}

/* ══════════════════════════════════════════════════
   ✨ 闪光粒子
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
  width: 3px;
  height: 3px;
  border-radius: 50%;
  background: #b39ddb;
  animation: sparkleAnim ease-in-out infinite;
}

.sparkle-1  { left: 18%; top: 28%; background: #b39ddb; animation-duration: 4s; animation-delay: 0s; }
.sparkle-2  { left: 35%; top: 52%; background: #90caf9; animation-duration: 5s; animation-delay: 0.8s; }
.sparkle-3  { left: 55%; top: 18%; background: #80cbc4; animation-duration: 3.5s; animation-delay: 1.5s; }
.sparkle-4  { left: 72%; top: 68%; background: #f48fb1; animation-duration: 4.5s; animation-delay: 0.3s; }
.sparkle-5  { left: 88%; top: 38%; background: #fff59d; animation-duration: 3.8s; animation-delay: 2s; }
.sparkle-6  { left: 8%;  top: 58%; background: #b39ddb; animation-duration: 5.5s; animation-delay: 1s; }
.sparkle-7  { left: 45%; top: 78%; background: #90caf9; animation-duration: 4.2s; animation-delay: 0.5s; }
.sparkle-8  { left: 62%; top: 22%; background: #80cbc4; animation-duration: 3.2s; animation-delay: 1.8s; }
.sparkle-9  { left: 78%; top: 48%; background: #f48fb1; animation-duration: 4.8s; animation-delay: 2.5s; }
.sparkle-10 { left: 25%; top: 82%; background: #fff59d; animation-duration: 3.6s; animation-delay: 0.2s; }

@keyframes sparkleAnim {
  0%, 100% { opacity: 0; transform: scale(0); }
  50% { opacity: 0.7; transform: scale(1.5); box-shadow: 0 0 6px currentColor; }
}

/* ── 页面标题区域 — 二次元风格 ────────────────── */
.page-header {
  margin-bottom: 24px;
  animation: fadeInUp 0.5s ease-out;
  text-align: center;
  position: relative;
  z-index: 1;
}

.header-deco {
  position: relative;
  display: inline-block;
}

.deco-star {
  position: absolute;
  font-size: 14px;
  color: #b39ddb;
  animation: starPulse 2s ease-in-out infinite;
}

.deco-star-2 {
  color: #90caf9;
  animation-delay: 1s;
}

@keyframes starPulse {
  0%, 100% { opacity: 0.4; transform: scale(0.8); }
  50% { opacity: 1; transform: scale(1.3); }
}

.page-title {
  font-size: 28px;
  font-weight: 800;
  color: #1565c0;
  margin-bottom: 4px;
  display: inline-block;
}

.page-desc {
  font-size: 14px;
  color: #5c6bc0;
  font-weight: 500;
}

.title-underline {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 12px;
}

.underline-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: linear-gradient(135deg, #90caf9, #b39ddb);
}

.underline-line {
  width: 60px;
  height: 2px;
  background: linear-gradient(90deg, #90caf9, #b39ddb, #f48fb1);
  border-radius: 1px;
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(16px); }
  to { opacity: 1; transform: translateY(0); }
}

/* ── 管理卡片 — 二次元风格 ────────────────────── */
.admin-card {
  padding: 28px 32px;
  border-radius: 20px;
  border: 2px solid rgba(144, 202, 249, 0.12);
  position: relative;
  z-index: 1;
}

/* ── 连接状态栏 — 二次元风格 ──────────────────── */
.conn-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 18px;
  border-radius: 14px;
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 24px;
  transition: all 0.3s ease;
}

.conn-bar.checking {
  background: linear-gradient(135deg, #fff9c4, #fff3e0);
  color: #e65100;
}

.conn-bar.ok {
  background: linear-gradient(135deg, #e8f5e9, #e0f2f1);
  color: #2e7d32;
}

.conn-bar.fail {
  background: linear-gradient(135deg, #fce4ec, #ffebee);
  color: #c62828;
}

.conn-icon {
  display: flex;
  align-items: center;
}

.conn-spinner {
  display: inline-block;
  width: 14px;
  height: 14px;
  border: 2px solid rgba(230, 81, 0, 0.2);
  border-top-color: #e65100;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.link-btn {
  background: none;
  border: none;
  color: inherit;
  text-decoration: underline;
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
}

/* ── 标题输入框 — 二次元风格 ──────────────────── */
.title-input {
  font-size: 16px;
  font-weight: 600;
  padding: 14px 18px;
  border-radius: 14px;
  border-color: #c5cae9;
}

.title-input:focus {
  border-color: #90caf9;
  box-shadow: 0 0 0 3px rgba(144, 202, 249, 0.15);
}

/* ── 发布按钮区域 — 二次元风格 ────────────────── */
.action-row {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 20px;
}

.publish-btn {
  padding: 12px 32px;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 1px;
  border-radius: 14px;
  background: linear-gradient(135deg, #90caf9 0%, #b39ddb 50%, #f48fb1 100%);
  box-shadow: 0 4px 16px rgba(144, 202, 249, 0.4);
  transition: all 0.25s ease;
}

.publish-btn:hover {
  box-shadow: 0 6px 24px rgba(144, 202, 249, 0.5);
  transform: translateY(-2px);
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

.action-hint {
  font-size: 12px;
  color: #5c6bc0;
}

/* ── 底部装饰 ──────────────────────────────────── */
.page-footer-deco {
  text-align: center;
  margin-top: 32px;
  padding-bottom: 20px;
  font-size: 18px;
  color: #b39ddb;
  letter-spacing: 12px;
  animation: fadeInUp 0.8s ease-out;
  position: relative;
  z-index: 1;
}
</style>
