<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import type { News } from '../news'

const router  = useRouter()
const newsList = ref<News[]>([])
const keyword  = ref('')
const loading  = ref(false)
const errMsg   = ref('')

// 格式化时间：将 ISO 时间字符串转为中文格式
function formatDate(dt: string) {
  if (!dt) return ''
  return new Date(dt).toLocaleString('zh-CN', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
  })
}

// 加载新闻列表（支持关键字搜索）
async function loadNews(kw = '') {
  loading.value = true
  errMsg.value  = ''
  try {
    const params = kw ? { keyword: kw } : {}
    const res    = await axios.get<News[]>('/api/news', { params })
    newsList.value = res.data
  } catch {
    errMsg.value = '加载失败，请刷新重试'
  } finally {
    loading.value = false
  }
}

// 搜索新闻
function search() { loadNews(keyword.value.trim()) }

// 重置搜索
function reset() {
  keyword.value = ''
  loadNews()
}

// 跳转到新闻详情页
function goDetail(id: number) {
  router.push(`/news/${id}`)
}

onMounted(() => loadNews())
</script>

<template>
  <div class="page-container">
    <!-- 🌸 樱花飘落装饰（CSS花瓣） -->
    <div class="sakura-container">
      <div v-for="i in 8" :key="'sakura-'+i" class="sakura" :class="'sakura-' + i"></div>
    </div>

    <!-- ✨ 闪光粒子 -->
    <div class="sparkle-container">
      <div v-for="i in 12" :key="'spark-'+i" class="sparkle" :class="'sparkle-' + i"></div>
    </div>

    <!-- 页面标题区域 — 二次元风格 -->
    <div class="page-header">
      <div class="header-deco">
        <span class="deco-star">✦</span>
        <span class="deco-star deco-star-2">✧</span>
      </div>
      <h1 class="page-title">📰 新闻中心</h1>
      <p class="page-desc">✨ 浏览最新资讯，掌握时事动态 ✨</p>
      <!-- 标题下方装饰线 -->
      <div class="title-underline">
        <span class="underline-dot"></span>
        <span class="underline-line"></span>
        <span class="underline-dot"></span>
      </div>
    </div>

    <!-- 搜索栏 — 二次元圆角 -->
    <div class="card search-bar">
      <span class="search-icon">🔍</span>
      <input
        class="form-input search-input"
        v-model="keyword"
        placeholder="搜索新闻标题或内容…"
        @keyup.enter="search"
      />
      <button class="btn btn-primary btn-kawaii" @click="search" :disabled="loading">
        🔎 搜索
      </button>
      <button class="btn btn-default btn-kawaii" @click="reset" :disabled="loading">
        🔄 重置
      </button>
    </div>

    <!-- 新闻列表 -->
    <div class="card news-list-card">
      <!-- 加载中 — 可爱的加载动画 -->
      <div v-if="loading" class="center-text">
        <span class="loading-kawaii">(๑•̀ㅂ•́)و✧</span>
        <p>加载中…</p>
      </div>

      <!-- 错误提示 -->
      <div v-else-if="errMsg" class="tip-error center-text">💦 {{ errMsg }}</div>

      <!-- 空状态 — 可爱的空状态 -->
      <div v-else-if="newsList.length === 0" class="empty-state">
        <span class="empty-icon">📭</span>
        <p class="empty-text">暂无新闻{{ keyword ? `（关键字：${keyword}）` : '' }}</p>
        <span class="empty-kawaii">(´･ω･`)</span>
      </div>

      <!-- 新闻列表项 -->
      <ul v-else class="news-ul">
        <li
          v-for="(item, index) in newsList"
          :key="item.newsId"
          class="news-item"
          :style="{ animationDelay: `${index * 0.06}s` }"
          @click="goDetail(item.newsId)"
        >
          <!-- 序号标签 — 二次元风格 -->
          <span class="news-index" :class="index < 3 ? 'index-hot' : ''">
            {{ index + 1 }}
          </span>
          <!-- 新闻标题 -->
          <span class="news-title">{{ item.title }}</span>
          <!-- 发布时间 -->
          <span class="news-date">
            <span class="date-icon">🕐</span>
            {{ formatDate(item.creation) }}
          </span>
          <!-- 箭头 -->
          <span class="news-arrow">›</span>
        </li>
      </ul>
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
  background: radial-gradient(ellipse at 30% 30%, #ffcdd2 0%, #f48fb1 40%, #f06292 80%, #ec407a 100%);
  border-radius: 50% 0 50% 50%;
  box-shadow: inset 0 0 3px rgba(255, 255, 255, 0.4);
  transform: rotate(-45deg);
}

.sakura-1 { left: 10%; animation-duration: 12s; animation-delay: 0s; }
.sakura-1::before { width: 10px; height: 10px; }
.sakura-2 { left: 25%; animation-duration: 14s; animation-delay: 2s; }
.sakura-2::before { width: 14px; height: 14px; }
.sakura-3 { left: 45%; animation-duration: 10s; animation-delay: 4s; }
.sakura-3::before { width: 8px; height: 8px; }
.sakura-4 { left: 60%; animation-duration: 13s; animation-delay: 1s; }
.sakura-4::before { width: 12px; height: 12px; }
.sakura-5 { left: 75%; animation-duration: 15s; animation-delay: 3s; }
.sakura-5::before { width: 15px; height: 15px; }
.sakura-6 { left: 88%; animation-duration: 11s; animation-delay: 5s; }
.sakura-6::before { width: 10px; height: 10px; }
.sakura-7 { left: 35%; animation-duration: 12.5s; animation-delay: 2.5s; }
.sakura-7::before { width: 13px; height: 13px; }
.sakura-8 { left: 55%; animation-duration: 10.5s; animation-delay: 6s; }
.sakura-8::before { width: 9px; height: 9px; }

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

.sparkle-1  { left: 15%; top: 30%; background: #f8bbd0; animation-duration: 4s; animation-delay: 0s; }
.sparkle-2  { left: 30%; top: 50%; background: #ce93d8; animation-duration: 5s; animation-delay: 0.8s; }
.sparkle-3  { left: 50%; top: 20%; background: #90caf9; animation-duration: 3.5s; animation-delay: 1.5s; }
.sparkle-4  { left: 65%; top: 70%; background: #80deea; animation-duration: 4.5s; animation-delay: 0.3s; }
.sparkle-5  { left: 80%; top: 40%; background: #fff59d; animation-duration: 3.8s; animation-delay: 2s; }
.sparkle-6  { left: 10%; top: 60%; background: #f8bbd0; animation-duration: 5.5s; animation-delay: 1s; }
.sparkle-7  { left: 40%; top: 80%; background: #ce93d8; animation-duration: 4.2s; animation-delay: 0.5s; }
.sparkle-8  { left: 70%; top: 25%; background: #90caf9; animation-duration: 3.2s; animation-delay: 1.8s; }
.sparkle-9  { left: 90%; top: 55%; background: #80deea; animation-duration: 4.8s; animation-delay: 2.5s; }
.sparkle-10 { left: 20%; top: 15%; background: #fff59d; animation-duration: 3.6s; animation-delay: 0.2s; }
.sparkle-11 { left: 55%; top: 45%; background: #f8bbd0; animation-duration: 5.2s; animation-delay: 1.2s; }
.sparkle-12 { left: 85%; top: 75%; background: #ce93d8; animation-duration: 4.1s; animation-delay: 0.7s; }

@keyframes sparkleAnim {
  0%, 100% { opacity: 0; transform: scale(0); }
  50% { opacity: 0.8; transform: scale(1.5); box-shadow: 0 0 6px currentColor; }
}

/* ── 页面标题区域 — 二次元风格 ────────────────── */
.page-header {
  margin-bottom: 24px;
  animation: fadeInUp 0.5s ease-out;
  text-align: center;
  position: relative;
  z-index: 1;
}

/* 标题旁装饰星星 */
.header-deco {
  position: relative;
  display: inline-block;
}

.deco-star {
  position: absolute;
  font-size: 14px;
  color: #f8bbd0;
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
  color: #4a148c;
  margin-bottom: 4px;
  display: inline-block;
}

.page-desc {
  font-size: 14px;
  color: #9c27b0;
  font-weight: 500;
}

/* 标题下方装饰线 */
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
  background: linear-gradient(135deg, #f8bbd0, #ce93d8);
}

.underline-line {
  width: 60px;
  height: 2px;
  background: linear-gradient(90deg, #f8bbd0, #ce93d8, #90caf9);
  border-radius: 1px;
}

/* ── 搜索栏 — 二次元圆角 ──────────────────────── */
.search-bar {
  display: flex;
  gap: 10px;
  align-items: center;
  padding: 16px 20px;
  margin-bottom: 20px;
  border-radius: 18px;
  border: 2px solid rgba(206, 147, 216, 0.15);
  position: relative;
  z-index: 1;
}

.search-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  border-radius: 12px;
  border-color: #e1bee7;
}

.search-input:focus {
  border-color: #ce93d8;
  box-shadow: 0 0 0 3px rgba(206, 147, 216, 0.12);
}

/* 二次元风格按钮 */
.btn-kawaii {
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.25s ease;
}

.btn-kawaii:hover {
  transform: translateY(-2px);
}

/* ── 新闻列表卡片 ──────────────────────────────── */
.news-list-card {
  padding: 0;
  overflow: hidden;
  border-radius: 18px;
  border: 2px solid rgba(206, 147, 216, 0.1);
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
  font-size: 14px;
}

/* ── 空状态 — 可爱 ────────────────────────────── */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #94a3b8;
}

.empty-icon {
  font-size: 48px;
  display: block;
  margin-bottom: 12px;
}

.empty-text {
  margin-bottom: 8px;
}

.empty-kawaii {
  font-size: 20px;
  color: #ce93d8;
}

/* ── 新闻列表 ──────────────────────────────────── */
.news-ul {
  list-style: none;
}

.news-item {
  display: flex;
  align-items: center;
  padding: 18px 24px;
  border-bottom: 1px solid #f3e5f5;
  cursor: pointer;
  transition: all 0.25s ease;
  animation: listItemIn 0.4s ease-out both;
}

@keyframes listItemIn {
  from { opacity: 0; transform: translateX(-12px); }
  to { opacity: 1; transform: translateX(0); }
}

.news-item:last-child {
  border-bottom: none;
}

.news-item:hover {
  /* 二次元粉色渐变悬停 */
  background: linear-gradient(90deg, #fce4ec 0%, #f3e5f5 50%, #f8fafc 100%);
  padding-left: 28px;
}

/* ── 序号标签 — 二次元风格 ────────────────────── */
.news-index {
  width: 30px;
  height: 30px;
  border-radius: 10px;
  background: linear-gradient(135deg, #ce93d8, #90caf9);
  color: #fff;
  font-size: 12px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-right: 16px;
  transition: transform 0.2s ease;
}

/* 前三名热门标签 — 特殊颜色 */
.news-index.index-hot {
  background: linear-gradient(135deg, #f8bbd0, #ce93d8);
  box-shadow: 0 2px 8px rgba(206, 147, 216, 0.4);
}

.news-item:hover .news-index {
  transform: scale(1.1) rotate(-5deg);
}

/* ── 新闻标题 ──────────────────────────────────── */
.news-title {
  font-size: 15px;
  color: #334155;
  font-weight: 500;
  flex: 1;
  margin-right: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.2s;
}

.news-item:hover .news-title {
  color: #7b1fa2;
}

/* ── 发布时间 ──────────────────────────────────── */
.news-date {
  font-size: 12px;
  color: #9c27b0;
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 4px;
  opacity: 0.6;
}

.date-icon {
  font-size: 12px;
}

/* ── 箭头 ──────────────────────────────────────── */
.news-arrow {
  font-size: 20px;
  color: #ce93d8;
  margin-left: 12px;
  transition: all 0.25s ease;
}

.news-item:hover .news-arrow {
  color: #7b1fa2;
  transform: translateX(4px);
}

/* ── 底部装饰 ──────────────────────────────────── */
.page-footer-deco {
  text-align: center;
  margin-top: 32px;
  padding-bottom: 20px;
  font-size: 18px;
  color: #f8bbd0;
  letter-spacing: 12px;
  animation: fadeInUp 0.8s ease-out;
  position: relative;
  z-index: 1;
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(16px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
