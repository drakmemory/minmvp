import { createRouter, createWebHistory } from 'vue-router'

// All navigation is handled through tabs in App.vue; the router
// is included to support future deep-linking and browser history.
const router = createRouter({
  history: createWebHistory(),
  routes: [],
})

export default router
