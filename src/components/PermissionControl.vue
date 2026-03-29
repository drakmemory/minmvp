<template>
  <div class="module-wrap">
    <el-alert title="后端: HSAF 1.2.7 权限框架 | JWT Token认证" type="info" :closable="false" show-icon class="tech-note" />

    <el-row :gutter="16">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><span>角色选择</span></template>
          <el-radio-group v-model="selectedRole" size="large" style="display:flex;flex-direction:column;gap:12px">
            <el-radio-button value="admin" style="width:100%">
              <el-icon><UserFilled /></el-icon> 管理员 (Admin)
            </el-radio-button>
            <el-radio-button value="user" style="width:100%">
              <el-icon><User /></el-icon> 普通用户 (User)
            </el-radio-button>
            <el-radio-button value="guest" style="width:100%">
              <el-icon><View /></el-icon> 访客 (Guest)
            </el-radio-button>
          </el-radio-group>
          <el-divider />
          <div class="current-role-info">
            <el-text type="info">当前角色:</el-text>
            <el-tag :type="roleTagType(selectedRole)" style="margin-left:8px">{{ roleLabel(selectedRole) }}</el-tag>
          </div>
          <div style="margin-top:12px">
            <el-text type="info" size="small">JWT Token:</el-text>
            <el-input
              :value="store.userToken.substring(0, 30) + '...'"
              readonly
              size="small"
              style="margin-top:4px"
            />
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card shadow="hover" style="margin-bottom:16px">
          <template #header><span>权限矩阵</span></template>
          <el-table :data="permMatrix" border stripe>
            <el-table-column prop="feature" label="功能模块" width="150" />
            <el-table-column label="管理员" width="90" align="center">
              <template #default="{ row }">
                <el-icon :color="row.admin ? '#52c41a' : '#ff4d4f'" size="18">
                  <CircleCheck v-if="row.admin" /><CircleClose v-else />
                </el-icon>
              </template>
            </el-table-column>
            <el-table-column label="普通用户" width="90" align="center">
              <template #default="{ row }">
                <el-icon :color="row.user ? '#52c41a' : '#ff4d4f'" size="18">
                  <CircleCheck v-if="row.user" /><CircleClose v-else />
                </el-icon>
              </template>
            </el-table-column>
            <el-table-column label="访客" width="90" align="center">
              <template #default="{ row }">
                <el-icon :color="row.guest ? '#52c41a' : '#ff4d4f'" size="18">
                  <CircleCheck v-if="row.guest" /><CircleClose v-else />
                </el-icon>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-card shadow="hover">
          <template #header><span>动态按钮展示（基于当前角色）</span></template>
          <div class="dynamic-btns">
            <el-button type="primary" v-if="can('read')">
              <el-icon><View /></el-icon> 查看
            </el-button>
            <el-button type="success" v-if="can('write')">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button type="warning" v-if="can('export')">
              <el-icon><Download /></el-icon> 导出
            </el-button>
            <el-button type="danger" v-if="can('delete')">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
            <el-button type="info" v-if="can('import')">
              <el-icon><Upload /></el-icon> 导入
            </el-button>
            <el-button v-if="can('manage')">
              <el-icon><Setting /></el-icon> 系统设置
            </el-button>
          </div>
          <el-alert
            :title="`当前角色 [${roleLabel(selectedRole)}] 拥有以下权限: ${currentPerms.join(', ')}`"
            type="success"
            :closable="false"
            style="margin-top:12px"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAppStore } from '../store/index.js'

const store = useAppStore()
const selectedRole = ref('admin')

const allPerms = ['read', 'write', 'delete', 'export', 'import', 'manage']
const currentPerms = computed(() => allPerms.filter(p => store.hasPermission(selectedRole.value, p)))

function can(perm) {
  return store.hasPermission(selectedRole.value, perm)
}

function roleLabel(r) {
  return { admin: '管理员', user: '普通用户', guest: '访客' }[r] || r
}

function roleTagType(r) {
  return { admin: 'danger', user: 'primary', guest: 'info' }[r] || 'info'
}

const permMatrix = [
  { feature: '用户查看', admin: true, user: true, guest: true },
  { feature: '用户编辑', admin: true, user: true, guest: false },
  { feature: '用户删除', admin: true, user: false, guest: false },
  { feature: '数据导出', admin: true, user: true, guest: false },
  { feature: '数据导入', admin: true, user: false, guest: false },
  { feature: '系统管理', admin: true, user: false, guest: false },
]
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.current-role-info { display: flex; align-items: center; }
.dynamic-btns { display: flex; gap: 10px; flex-wrap: wrap; }
</style>
