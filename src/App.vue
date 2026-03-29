<template>
  <el-container class="app-wrapper">
    <!-- Header -->
    <el-header class="app-header">
      <div class="header-left">
        <span class="logo-icon">⚡</span>
        <span class="app-title">MinMVP演示系统</span>
      </div>
      <div class="header-right">
        <el-badge :value="unreadCount" class="badge-item">
          <el-icon size="20" color="#fff"><Bell /></el-icon>
        </el-badge>
        <span class="token-display">
          <el-tag type="info" size="small">JWT: {{ shortToken }}</el-tag>
        </span>
        <el-avatar :size="32" class="user-avatar">
          {{ store.currentUser.name.charAt(0) }}
        </el-avatar>
        <span class="user-name">{{ store.currentUser.name }}</span>
        <el-tag type="success" size="small">{{ store.currentUser.role }}</el-tag>
      </div>
    </el-header>

    <el-container>
      <!-- Sidebar -->
      <el-aside :width="isCollapsed ? '64px' : '220px'" class="app-aside">
        <el-menu
          :default-active="activeTab"
          :collapse="isCollapsed"
          background-color="#001529"
          text-color="#ffffffa0"
          active-text-color="#1890ff"
          @select="handleMenuSelect"
        >
          <div class="collapse-btn" @click="isCollapsed = !isCollapsed">
            <el-icon><Fold v-if="!isCollapsed" /><Expand v-else /></el-icon>
          </div>
          <el-menu-item v-for="item in menuItems" :key="item.key" :index="item.key">
            <el-icon><component :is="item.icon" /></el-icon>
            <template #title>{{ item.label }}</template>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- Main Content -->
      <el-main class="app-main">
        <el-tabs v-model="activeTab" type="card" class="main-tabs" @tab-click="handleTabClick">
          <el-tab-pane v-for="item in menuItems" :key="item.key" :label="item.label" :name="item.key">
            <component :is="item.component" />
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAppStore } from './store/index.js'
import UserManagement from './components/UserManagement.vue'
import DataStatistics from './components/DataStatistics.vue'
import FullTextSearch from './components/FullTextSearch.vue'
import MessageNotifications from './components/MessageNotifications.vue'
import ScheduledTasks from './components/ScheduledTasks.vue'
import FileManagement from './components/FileManagement.vue'
import ExcelExport from './components/ExcelExport.vue'
import ApiDocumentation from './components/ApiDocumentation.vue'
import PermissionControl from './components/PermissionControl.vue'
import RichTextEditor from './components/RichTextEditor.vue'
import PdfPreview from './components/PdfPreview.vue'

const store = useAppStore()
const isCollapsed = ref(false)
const activeTab = ref('user')

const menuItems = [
  { key: 'user', label: '用户管理', icon: 'User', component: UserManagement },
  { key: 'stats', label: '数据统计', icon: 'DataLine', component: DataStatistics },
  { key: 'search', label: '全文检索', icon: 'Search', component: FullTextSearch },
  { key: 'msg', label: '消息通知', icon: 'Bell', component: MessageNotifications },
  { key: 'tasks', label: '定时任务', icon: 'Clock', component: ScheduledTasks },
  { key: 'files', label: '文件管理', icon: 'Folder', component: FileManagement },
  { key: 'excel', label: 'Excel导入导出', icon: 'Document', component: ExcelExport },
  { key: 'api', label: 'API文档', icon: 'Connection', component: ApiDocumentation },
  { key: 'perm', label: '权限控制', icon: 'Lock', component: PermissionControl },
  { key: 'editor', label: '富文本编辑', icon: 'Edit', component: RichTextEditor },
  { key: 'pdf', label: 'PDF预览', icon: 'Reading', component: PdfPreview },
]

const shortToken = computed(() => {
  const t = store.userToken
  return t ? t.substring(0, 20) + '...' : ''
})

const unreadCount = computed(() => store.messages.length)

function handleMenuSelect(key) {
  activeTab.value = key
}

function handleTabClick(tab) {
  activeTab.value = tab.props.name
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif; }
</style>

<style scoped>
.app-wrapper {
  height: 100vh;
  overflow: hidden;
}

.app-header {
  background: #001529;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.3);
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  font-size: 24px;
}

.app-title {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.badge-item {
  cursor: pointer;
}

.token-display {
  max-width: 200px;
  overflow: hidden;
}

.user-avatar {
  background: #1890ff;
  color: #fff;
  font-weight: bold;
  cursor: pointer;
}

.user-name {
  color: #fff;
  font-size: 14px;
}

.app-aside {
  background: #001529;
  transition: width 0.3s;
  overflow: hidden;
  height: calc(100vh - 60px);
}

.collapse-btn {
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffffa0;
  cursor: pointer;
  border-bottom: 1px solid #ffffff10;
  transition: color 0.2s;
}

.collapse-btn:hover {
  color: #1890ff;
}

.app-main {
  background: #f0f2f5;
  padding: 16px;
  overflow-y: auto;
  height: calc(100vh - 60px);
}

.main-tabs {
  background: #fff;
  border-radius: 8px;
  padding: 0 16px 16px;
  min-height: calc(100vh - 110px);
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}

:deep(.el-tabs__header) {
  margin-bottom: 16px;
}

:deep(.el-menu) {
  border-right: none;
}
</style>
