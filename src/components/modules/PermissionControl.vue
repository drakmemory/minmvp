<script setup>
import { ref } from 'vue'
import { useAppStore } from '../../store/index.js'
import { ElMessage } from 'element-plus'

const store = useAppStore()

const roles = [
  {
    name: 'admin',
    label: '管理员',
    description: '系统最高权限，可操作所有功能',
    color: '#F56C6C',
    permissions: [
      '用户管理:查看', '用户管理:新增', '用户管理:编辑', '用户管理:删除',
      '数据统计:查看', '全文搜索:使用', '消息通知:查看', '定时任务:管理',
      '文件管理:上传', '文件管理:下载', '文件管理:删除',
      'Excel:导入', 'Excel:导出', 'API文档:查看', '权限控制:管理',
      '富文本:编辑', 'PDF预览:使用'
    ]
  },
  {
    name: 'manager',
    label: '管理人员',
    description: '中级权限，可管理用户和文件',
    color: '#E6A23C',
    permissions: [
      '用户管理:查看', '用户管理:新增', '用户管理:编辑',
      '数据统计:查看', '全文搜索:使用', '消息通知:查看',
      '文件管理:上传', '文件管理:下载',
      'Excel:导出', '富文本:编辑', 'PDF预览:使用'
    ]
  },
  {
    name: 'user',
    label: '普通用户',
    description: '基础权限，仅可查看和个人操作',
    color: '#909399',
    permissions: [
      '用户管理:查看', '数据统计:查看', '全文搜索:使用',
      '消息通知:查看', '文件管理:下载', 'Excel:导出', 'PDF预览:使用'
    ]
  }
]

const allPermissions = [
  { group: '用户管理', perms: ['查看', '新增', '编辑', '删除'] },
  { group: '数据统计', perms: ['查看'] },
  { group: '全文搜索', perms: ['使用'] },
  { group: '消息通知', perms: ['查看'] },
  { group: '定时任务', perms: ['管理'] },
  { group: '文件管理', perms: ['上传', '下载', '删除'] },
  { group: 'Excel', perms: ['导入', '导出'] },
  { group: 'API文档', perms: ['查看'] },
  { group: '权限控制', perms: ['管理'] },
  { group: '富文本', perms: ['编辑'] },
  { group: 'PDF预览', perms: ['使用'] },
]

function hasPermission(role, group, perm) {
  return role.permissions.includes(`${group}:${perm}`)
}

function switchRole(roleName) {
  store.currentUser.role = roleName
  ElMessage.success(`已切换角色为: ${roles.find(r => r.name === roleName)?.label}（HSAF权限已更新）`)
}
</script>

<template>
  <div>
    <!-- Current User Role -->
    <el-card style="margin-bottom: 16px">
      <template #header>
        <div class="card-header">
          <span><el-icon><Lock /></el-icon> 权限控制（HSAF框架）</span>
          <div>
            <el-tag size="small" type="danger">HSAF 1.2.7</el-tag>
            <el-tag size="small" type="warning" style="margin-left: 6px">RBAC 模型</el-tag>
            <el-tag size="small" type="info" style="margin-left: 6px">JWT 认证</el-tag>
          </div>
        </div>
      </template>

      <el-alert
        title="基于 HSAF 内部框架实现 RBAC（Role-Based Access Control）权限控制，结合 JWT Token 进行无状态认证。"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 16px"
      />

      <!-- Role Switcher Demo -->
      <div class="role-switcher">
        <span class="switcher-label">演示角色切换：</span>
        <el-radio-group
          :model-value="store.currentUser.role"
          @update:model-value="switchRole"
          size="large"
        >
          <el-radio-button v-for="role in roles" :key="role.name" :value="role.name">
            <el-tag :color="role.color" style="color: white; border: none">{{ role.label }}</el-tag>
          </el-radio-button>
        </el-radio-group>
        <el-tag type="info" style="margin-left: 16px">
          当前: {{ roles.find(r => r.name === store.currentUser.role)?.label }}
        </el-tag>
      </div>
    </el-card>

    <!-- Permission Matrix -->
    <el-card style="margin-bottom: 16px">
      <template #header>权限矩阵</template>
      <el-table :data="allPermissions" border>
        <el-table-column prop="group" label="功能模块" width="120" />
        <el-table-column
          v-for="role in roles"
          :key="role.name"
          :label="role.label"
          align="center"
        >
          <template #default="{ row }">
            <div class="perm-cell">
              <el-tag
                v-for="perm in row.perms"
                :key="perm"
                :type="hasPermission(role, row.group, perm) ? 'success' : 'danger'"
                size="small"
                class="perm-tag"
              >
                {{ hasPermission(role, row.group, perm) ? '✓' : '✗' }} {{ perm }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- JWT Token Info -->
    <el-card>
      <template #header>JWT Token 信息</template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="Token">
          <el-text truncated style="max-width: 300px; font-family: monospace; font-size: 12px">
            {{ store.token }}
          </el-text>
        </el-descriptions-item>
        <el-descriptions-item label="库版本">java-jwt 3.8.1</el-descriptions-item>
        <el-descriptions-item label="算法">HS256</el-descriptions-item>
        <el-descriptions-item label="有效期">24小时</el-descriptions-item>
        <el-descriptions-item label="Payload">
          <el-tag size="small">userId</el-tag>
          <el-tag size="small" style="margin-left: 4px">role</el-tag>
          <el-tag size="small" style="margin-left: 4px">exp</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="配置加密">Jasypt 加密敏感配置</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<style scoped>
.card-header { display: flex; align-items: center; justify-content: space-between; }
.role-switcher { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.switcher-label { font-weight: 600; white-space: nowrap; }
.perm-cell { display: flex; flex-wrap: wrap; gap: 4px; justify-content: center; }
.perm-tag { margin: 1px; }
</style>
