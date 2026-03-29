<script setup>
import { ref, computed } from 'vue'
import { useAppStore } from '../store/index.js'
import UserManagement from '../components/modules/UserManagement.vue'
import DataStatistics from '../components/modules/DataStatistics.vue'
import SearchModule from '../components/modules/SearchModule.vue'
import MessageNotification from '../components/modules/MessageNotification.vue'
import TaskStatus from '../components/modules/TaskStatus.vue'
import FileManagement from '../components/modules/FileManagement.vue'
import ExcelModule from '../components/modules/ExcelModule.vue'
import ApiDocs from '../components/modules/ApiDocs.vue'
import PermissionControl from '../components/modules/PermissionControl.vue'
import RichTextEditor from '../components/modules/RichTextEditor.vue'
import PdfPreview from '../components/modules/PdfPreview.vue'

const store = useAppStore()
const activeTab = ref('user')
const isCollapse = ref(false)

const menuItems = computed(() => {
  const all = [
    { name: 'user', label: '用户管理', icon: 'UserFilled', tag: 'MyBatis-Plus + Redis' },
    { name: 'stats', label: '数据统计', icon: 'DataAnalysis', tag: 'XXL-Job + ECharts' },
    { name: 'search', label: '全文搜索', icon: 'Search', tag: 'Elasticsearch' },
    { name: 'message', label: '消息通知', icon: 'Bell', tag: 'Kafka + Pinia' },
    { name: 'task', label: '定时任务', icon: 'Timer', tag: 'XXL-Job' },
    { name: 'file', label: '文件管理', icon: 'Folder', tag: 'FastDFS/OSS' },
    { name: 'excel', label: 'Excel导入导出', icon: 'Document', tag: 'EasyExcel' },
    { name: 'api', label: 'API文档', icon: 'Connection', tag: 'Swagger/Knife4j' },
    { name: 'permission', label: '权限控制', icon: 'Lock', tag: 'HSAF' },
    { name: 'richtext', label: '富文本编辑', icon: 'Edit', tag: 'CKEditor' },
    { name: 'pdf', label: 'PDF预览', icon: 'Reading', tag: 'vue-pdf' },
  ]
  // HSAF role-based visibility: hide permission control for non-admin
  if (!store.isAdmin) {
    return all.filter(item => item.name !== 'permission')
  }
  return all
})
</script>

<template>
  <el-container class="layout-container">
    <!-- Top Navigation Bar -->
    <el-header class="layout-header">
      <div class="header-left">
        <el-button
          :icon="isCollapse ? 'Expand' : 'Fold'"
          text
          size="large"
          class="collapse-btn"
          @click="isCollapse = !isCollapse"
        />
        <span class="logo-text">
          <el-icon><Grid /></el-icon>
          MinMVP 技术栈演示平台
        </span>
      </div>
      <div class="header-right">
        <el-tag type="success" size="small" class="stack-tag">Spring Boot 2.0.9</el-tag>
        <el-tag type="primary" size="small" class="stack-tag">Vue 3</el-tag>
        <el-tag size="small" class="stack-tag">Element Plus</el-tag>
        <el-divider direction="vertical" />
        <el-badge :value="store.messages.length" class="badge-item">
          <el-icon size="20"><Bell /></el-icon>
        </el-badge>
        <el-dropdown class="user-dropdown">
          <span class="user-info">
            <el-avatar :src="store.currentUser.avatar" size="small" />
            <span class="username">{{ store.currentUser.name }}</span>
            <el-tag
              :type="store.currentUser.role === 'admin' ? 'danger' : 'info'"
              size="small"
            >{{ store.currentUser.role === 'admin' ? '管理员' : '用户' }}</el-tag>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>
                <el-icon><User /></el-icon> 个人信息
              </el-dropdown-item>
              <el-dropdown-item divided>
                <el-icon><SwitchButton /></el-icon> 退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container class="main-container">
      <!-- Left Sidebar Menu -->
      <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
        <el-menu
          :default-active="activeTab"
          :collapse="isCollapse"
          :collapse-transition="false"
          background-color="#001529"
          text-color="#ffffffa6"
          active-text-color="#ffffff"
          class="side-menu"
          @select="activeTab = $event"
        >
          <el-menu-item
            v-for="item in menuItems"
            :key="item.name"
            :index="item.name"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            <template #title>
              <span>{{ item.label }}</span>
              <el-tag size="small" class="menu-tag" type="info">{{ item.tag }}</el-tag>
            </template>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- Main Content Area -->
      <el-main class="layout-main">
        <!-- Breadcrumb -->
        <el-breadcrumb separator="/" class="breadcrumb">
          <el-breadcrumb-item>首页</el-breadcrumb-item>
          <el-breadcrumb-item>
            {{ menuItems.find(m => m.name === activeTab)?.label }}
          </el-breadcrumb-item>
        </el-breadcrumb>

        <!-- Tech Stack Info Bar -->
        <el-alert
          :title="`当前模块技术栈: ${menuItems.find(m => m.name === activeTab)?.tag || ''}`"
          type="info"
          :closable="false"
          class="tech-alert"
          show-icon
        />

        <!-- Module Content via Tabs -->
        <div class="content-area">
          <UserManagement v-if="activeTab === 'user'" />
          <DataStatistics v-else-if="activeTab === 'stats'" />
          <SearchModule v-else-if="activeTab === 'search'" />
          <MessageNotification v-else-if="activeTab === 'message'" />
          <TaskStatus v-else-if="activeTab === 'task'" />
          <FileManagement v-else-if="activeTab === 'file'" />
          <ExcelModule v-else-if="activeTab === 'excel'" />
          <ApiDocs v-else-if="activeTab === 'api'" />
          <PermissionControl v-else-if="activeTab === 'permission'" />
          <RichTextEditor v-else-if="activeTab === 'richtext'" />
          <PdfPreview v-else-if="activeTab === 'pdf'" />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  height: 100vh;
  overflow: hidden;
}

.layout-header {
  background: #001529;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  height: 60px !important;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-btn {
  color: #ffffffa6 !important;
  font-size: 18px;
}

.logo-text {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stack-tag {
  cursor: default;
}

.badge-item {
  cursor: pointer;
  color: #ffffffa6;
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #ffffffa6;
}

.username {
  font-size: 14px;
}

.main-container {
  height: calc(100vh - 60px);
  overflow: hidden;
}

.layout-aside {
  background: #001529;
  transition: width 0.3s;
  overflow: hidden;
  height: 100%;
}

.side-menu {
  border-right: none;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
}

.menu-tag {
  margin-left: 8px;
  font-size: 10px;
  opacity: 0.7;
}

.layout-main {
  background: #f0f2f5;
  padding: 16px;
  overflow-y: auto;
}

.breadcrumb {
  margin-bottom: 12px;
}

.tech-alert {
  margin-bottom: 16px;
}

.content-area {
  background: transparent;
}
</style>
