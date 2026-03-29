import { createRouter, createWebHistory } from 'vue-router'

// All navigation is handled through tabs in App.vue; the router
// is included to support future deep-linking and browser history.
const router = createRouter({
  history: createWebHistory(),
  routes: [],
import { createRouter, createWebHashHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// JWT auth guard (simulated)
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    // In production, check JWT token validity here
    const token = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.demo'
    if (token) {
      next()
    } else {
      next('/')
    }
  } else {
    next()
  }
})

export default router
