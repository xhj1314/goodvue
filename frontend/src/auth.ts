// auth.ts — 管理登录态（响应式状态 + localStorage 持久化）

import { ref, computed } from 'vue'

export interface AuthUser {
  username: string
  email:    string
  password: string   // 原始密码，用于 HTTP Basic 认证头
}

const KEY = 'news_auth_user'

// ── 响应式登录状态 ──────────────────────────────────
// 使用 Vue ref 保存当前用户，实现跨组件响应式更新
const currentUser = ref<AuthUser | null>(loadFromStorage())

/** 从 localStorage 读取登录信息 */
function loadFromStorage(): AuthUser | null {
  const raw = localStorage.getItem(KEY)
  if (!raw) return null
  try { return JSON.parse(raw) as AuthUser } catch { return null }
}

/** 保存登录信息（同步更新响应式状态 + localStorage） */
export function saveAuth(user: AuthUser): void {
  currentUser.value = user
  localStorage.setItem(KEY, JSON.stringify(user))
}

/** 读取登录信息（从响应式状态读取） */
export function getAuth(): AuthUser | null {
  return currentUser.value
}

/** 是否已登录（响应式计算属性） */
export const isLoggedIn = computed(() => currentUser.value !== null)

/** 退出登录（清除响应式状态 + localStorage） */
export function clearAuth(): void {
  currentUser.value = null
  localStorage.removeItem(KEY)
}

/** 生成 HTTP Basic 认证头 */
export function basicAuthHeader(): string {
  const user = currentUser.value
  if (!user) return ''
  const encoded = btoa(`${user.username}:${user.password}`)
  return `Basic ${encoded}`
}
