<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/preview.css'
import type { News } from '../news'

const route  = useRoute()
const router = useRouter()
const news   = ref<News | null>(null)
const loading = ref(true)
const errMsg  = ref('')

// 格式化时间：将 ISO 时间字符串转为中文格式
function formatDate(dt: string) {
  if (!dt) return ''
  return new Date(dt).toLocaleString('zh-CN', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
  })
}

// 返回新闻列表页（用 push 而非 back，避免回到浏览器外部页面）
function goBack() {
  router.push('/')
}

// 页面加载时获取新闻详情
onMounted(async () => {
  const id = route.params.id as string
  try {
    const res = await axios.get<News>(`/api/news/${id}`)
    news.value = res.data
  } catch (err: any) {
    errMsg.value = err.response?.data?.message ?? '新闻不存在或加载失败'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="page-container">
    <!-- 🌸 樱花飘落装饰（CSS花瓣） -->
    <div class="sakura-container">
      <div v-for="i in 6" :key="'sakura-'+i" class="sakura" :class="'sakura-' + i"></div>
    </div>

    <!-- ✨ 闪光粒子 -->
    <div class="sparkle-container">
      <div v-for="i in 8" :key="'spark-'+i" class="sparkle" :class="'sparkle-' + i"></div>
    </div>

    <div class="card detail-card">
      <!-- 加载中 — 可爱的加载动画 -->
      <div v-if="loading" class="center-text">
        <span class="loading-kawaii">(๑•̀ㅂ•́)و✧</span>
        <p>加载中…</p>
      </div>

      <!-- 错误 -->
      <div v-else-if="errMsg" class="tip-error center-text">💦 {{ errMsg }}</div>

      <!-- 新闻内容 -->
      <template v-else-if="news">
        <!-- 顶部操作栏 -->
        <div class="detail-header">
          <button class="btn btn-default back-btn" @click="goBack">
            ← 返回列表
          </button>
          <span class="header-kawaii">📖</span>
        </div>

        <!-- 文章标题区域 — 二次元风格 -->
        <div class="detail-title-area">
          <div class="title-deco">
            <span class="title-star">✦</span>
          </div>
          <h1 class="detail-title">{{ news.title }}</h1>
          <div class="detail-meta">
            <span class="meta-item meta-date">
              <span class="meta-icon">📅</span>
              {{ formatDate(news.creation) }}
            </span>
            <span class="meta-item meta-tag">
              <span class="meta-icon">📰</span>
              新闻资讯
            </span>
            <span class="meta-item meta-kawaii">
              <span class="meta-icon">✨</span>
              精选
            </span>
          </div>
        </div>

        <!-- 分隔线 — 二次元装饰 -->
        <div class="divider">
          <span class="divider-flower">✿</span>
          <span class="divider-line"></span>
          <span class="divider-flower">❀</span>
          <span class="divider-line"></span>
          <span class="divider-flower">✿</span>
        </div>

        <!-- Markdown 渲染预览 -->
        <MdPreview
          :modelValue="news.content"
          previewTheme="github"
          class="md-preview"
        />

        <!-- 底部装饰 -->
        <div class="detail-footer-deco">
          <span>— 感谢阅读 ♪ —</span>
        </div>
      </template>
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
  background: radial-gradient(ellipse at 30% 30%, #ffcdd2 0%, #f48fb1 40%, #f06292 80%, #ec407a 100%);
  border-radius: 50% 0 50% 50%;
  box-shadow: inset 0 0 3px rgba(255, 255, 255, 0.4);
  transform: rotate(-45deg);
}

.sakura-1 { left: 15%; animation-duration: 13s; animation-delay: 0s; }
.sakura-1::before { width: 10px; height: 10px; }
.sakura-2 { left: 35%; animation-duration: 15s; animation-delay: 2s; }
.sakura-2::before { width: 14px; height: 14px; }
.sakura-3 { left: 55%; animation-duration: 11s; animation-delay: 4s; }
.sakura-3::before { width: 8px; height: 8px; }
.sakura-4 { left: 70%; animation-duration: 14s; animation-delay: 1s; }
.sakura-4::before { width: 12px; height: 12px; }
.sakura-5 { left: 85%; animation-duration: 12s; animation-delay: 3s; }
.sakura-5::before { width: 10px; height: 10px; }
.sakura-6 { left: 45%; animation-duration: 13.5s; animation-delay: 5s; }
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
  background: #f8bbd0;
  animation: sparkleAnim ease-in-out infinite;
}

.sparkle-1 { left: 20%; top: 25%; background: #f8bbd0; animation-duration: 4.5s; animation-delay: 0s; }
.sparkle-2 { left: 40%; top: 55%; background: #ce93d8; animation-duration: 5.5s; animation-delay: 1s; }
.sparkle-3 { left: 60%; top: 20%; background: #90caf9; animation-duration: 3.8s; animation-delay: 2s; }
.sparkle-4 { left: 80%; top: 65%; background: #80deea; animation-duration: 4.2s; animation-delay: 0.5s; }
.sparkle-5 { left: 10%; top: 70%; background: #fff59d; animation-duration: 5s; animation-delay: 1.5s; }
.sparkle-6 { left: 50%; top: 40%; background: #f8bbd0; animation-duration: 4.8s; animation-delay: 2.5s; }
.sparkle-7 { left: 75%; top: 35%; background: #ce93d8; animation-duration: 3.5s; animation-delay: 0.8s; }
.sparkle-8 { left: 30%; top: 80%; background: #90caf9; animation-duration: 5.2s; animation-delay: 1.8s; }

@keyframes sparkleAnim {
  0%, 100% { opacity: 0; transform: scale(0); }
  50% { opacity: 0.7; transform: scale(1.5); box-shadow: 0 0 6px currentColor; }
}

/* ── 详情卡片 ──────────────────────────────────── */
.detail-card {
  padding: 36px 44px;
  border-radius: 20px;
  border: 2px solid rgba(206, 147, 216, 0.12);
  position: relative;
  z-index: 1;
}

/* ── 加载状态 — 可爱 ──────────────────────────── */
.loading-kawaii {
  font-size: 28px;
  display: block;
  margin-bottom: 8px;
  animation: loadingBounce 1s ease-in-out infinite;
}

@keyframes loadingBounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.center-text {
  text-align: center;
  padding: 48px;
  color: #94a3b8;
}

/* ── 顶部操作栏 ────────────────────────────────── */
.detail-header {
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 8px 18px;
  border-radius: 12px;
  font-size: 13px;
  transition: all 0.25s ease;
  border-color: #e1bee7;
}

.back-btn:hover {
  background: #fce4ec;
  transform: translateX(-2px);
  border-color: #ce93d8;
}

.header-kawaii {
  font-size: 20px;
  animation: floatIcon 3s ease-in-out infinite;
}

@keyframes floatIcon {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-4px); }
}

/* ── 文章标题区域 — 二次元风格 ────────────────── */
.detail-title-area {
  margin-bottom: 24px;
  position: relative;
}

.title-deco {
  margin-bottom: 8px;
}

.title-star {
  font-size: 16px;
  color: #f8bbd0;
  animation: starPulse 2s ease-in-out infinite;
}

@keyframes starPulse {
  0%, 100% { opacity: 0.4; transform: scale(0.8); }
  50% { opacity: 1; transform: scale(1.3); }
}

.detail-title {
  font-size: 28px;
  font-weight: 800;
  color: #4a148c;
  line-height: 1.4;
  margin-bottom: 16px;
  letter-spacing: -0.5px;
}

/* ── 元信息标签 — 二次元胶囊 ──────────────────── */
.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #64748b;
  padding: 6px 14px;
  border-radius: 20px;
  transition: transform 0.2s ease;
}

.meta-item:hover {
  transform: translateY(-2px);
}

.meta-date {
  background: linear-gradient(135deg, #fce4ec, #f3e5f5);
  color: #7b1fa2;
}

.meta-tag {
  background: linear-gradient(135deg, #e8eaf6, #ede7f6);
  color: #1565c0;
}

.meta-kawaii {
  background: linear-gradient(135deg, #fff3e0, #fce4ec);
  color: #c2185b;
}

.meta-icon {
  font-size: 14px;
}

/* ── 装饰分隔线 — 二次元花朵 ──────────────────── */
.divider {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin: 28px 0;
}

.divider-flower {
  font-size: 14px;
  color: #f8bbd0;
  animation: flowerPulse 3s ease-in-out infinite;
}

.divider-flower:nth-child(3) {
  color: #ce93d8;
  animation-delay: 1s;
}

@keyframes flowerPulse {
  0%, 100% { opacity: 0.5; transform: scale(0.9); }
  50% { opacity: 1; transform: scale(1.1); }
}

.divider-line {
  width: 40px;
  height: 1px;
  background: linear-gradient(90deg, #f8bbd0, #ce93d8);
}

/* ── Markdown 预览区域 ─────────────────────────── */
.md-preview {
  padding: 0 !important;
}

/* ── 底部装饰 ──────────────────────────────────── */
.detail-footer-deco {
  text-align: center;
  margin-top: 32px;
  padding-top: 20px;
  border-top: 1px solid #f3e5f5;
  font-size: 13px;
  color: #ce93d8;
  font-weight: 500;
}
</style>
