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
