import { createRouter, createWebHashHistory, type RouteRecordRaw } from 'vue-router'
import { isLoggedIn } from '../auth'

import Login      from '../components/Login.vue'
import Register   from '../components/Register.vue'
import NewsList   from '../components/NewsList.vue'
import NewsDetail from '../components/NewsDetail.vue'
import Admin      from '../components/Admin.vue'

const routes: RouteRecordRaw[] = [
  { path: '/login',     component: Login    },
  { path: '/register',  component: Register },
  { path: '/',          component: NewsList,   meta: { requiresAuth: true } },
  { path: '/news/:id',  component: NewsDetail, meta: { requiresAuth: true } },
  { path: '/admin',     component: Admin,      meta: { requiresAuth: true } },
  // 未匹配路由 → 首页
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// ── 路由守卫 ──────────────────────────────────────────────────────────
router.beforeEach((to, _from, next) => {
  // isLoggedIn 是响应式 computed，用 .value 读取
  const loggedIn = isLoggedIn.value

  // 需要登录但未登录 → 跳转登录页
  if (to.meta.requiresAuth && !loggedIn) {
    return next('/login')
  }
  // 已登录却访问登录/注册页 → 跳转首页
  if ((to.path === '/login' || to.path === '/register') && loggedIn) {
    return next('/')
  }
  next()
})

export default router
