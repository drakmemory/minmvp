<template>
  <div class="module-wrap">
    <el-alert title="后端: MyBatis-Plus CRUD | Redis缓存用户列表" type="info" :closable="false" show-icon class="tech-note" />

    <div class="toolbar">
      <el-input v-model="searchQuery" placeholder="搜索用户名/手机号" clearable style="width:240px">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-button type="primary" @click="handleSearch"><el-icon><Search /></el-icon> 搜索</el-button>
      <el-button type="success" @click="openAddDialog"><el-icon><Plus /></el-icon> 新增用户</el-button>
    </div>

    <el-table :data="pagedUsers" stripe border style="width:100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="role" label="角色" width="80" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === '启用' ? 'success' : 'danger'" size="small">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="currentPage"
      :page-size="pageSize"
      :total="filteredUsers.length"
      layout="total, prev, pager, next"
      class="pagination"
    />

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="480px">
      <el-form :model="formData" label-width="80px">
        <el-form-item label="姓名"><el-input v-model="formData.name" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="formData.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="formData.email" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="formData.role" style="width:100%">
            <el-option label="管理员" value="管理员" />
            <el-option label="普通用户" value="普通用户" />
            <el-option label="访客" value="访客" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="formData.statusBool" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = 6
const dialogVisible = ref(false)
const isEdit = ref(false)
const formData = ref({ name: '', phone: '', email: '', role: '普通用户', statusBool: true })

const users = ref([
  { id: 1, name: '张三', phone: '13800001111', email: 'zhangsan@example.com', role: '管理员', status: '启用', createTime: '2024-01-01 10:00:00' },
  { id: 2, name: '李四', phone: '13800002222', email: 'lisi@example.com', role: '普通用户', status: '启用', createTime: '2024-01-02 11:00:00' },
  { id: 3, name: '王五', phone: '13800003333', email: 'wangwu@example.com', role: '普通用户', status: '禁用', createTime: '2024-01-03 12:00:00' },
  { id: 4, name: '赵六', phone: '13800004444', email: 'zhaoliu@example.com', role: '访客', status: '启用', createTime: '2024-01-04 09:00:00' },
  { id: 5, name: '孙七', phone: '13800005555', email: 'sunqi@example.com', role: '普通用户', status: '启用', createTime: '2024-01-05 14:00:00' },
  { id: 6, name: '周八', phone: '13800006666', email: 'zhouba@example.com', role: '普通用户', status: '启用', createTime: '2024-01-06 08:00:00' },
  { id: 7, name: '吴九', phone: '13800007777', email: 'wujiu@example.com', role: '访客', status: '禁用', createTime: '2024-01-07 15:00:00' },
  { id: 8, name: '郑十', phone: '13800008888', email: 'zhengshi@example.com', role: '普通用户', status: '启用', createTime: '2024-01-08 16:00:00' },
  { id: 9, name: '冯十一', phone: '13800009999', email: 'fengsy@example.com', role: '普通用户', status: '启用', createTime: '2024-01-09 17:00:00' },
  { id: 10, name: '陈十二', phone: '13800010000', email: 'chense@example.com', role: '管理员', status: '启用', createTime: '2024-01-10 18:00:00' },
])

const filteredUsers = computed(() => {
  if (!searchQuery.value) return users.value
  const q = searchQuery.value.toLowerCase()
  return users.value.filter(u => u.name.includes(q) || u.phone.includes(q))
})

const pagedUsers = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredUsers.value.slice(start, start + pageSize)
})

function handleSearch() {
  currentPage.value = 1
}

function openAddDialog() {
  isEdit.value = false
  formData.value = { name: '', phone: '', email: '', role: '普通用户', statusBool: true }
  dialogVisible.value = true
}

function openEditDialog(row) {
  isEdit.value = true
  formData.value = { ...row, statusBool: row.status === '启用', editId: row.id }
  dialogVisible.value = true
}

function handleSave() {
  if (isEdit.value) {
    const idx = users.value.findIndex(u => u.id === formData.value.editId)
    if (idx !== -1) {
      users.value[idx] = { ...users.value[idx], ...formData.value, status: formData.value.statusBool ? '启用' : '禁用' }
    }
    ElMessage.success('用户信息已更新')
  } else {
    users.value.push({
      id: users.value.length + 1,
      ...formData.value,
      status: formData.value.statusBool ? '启用' : '禁用',
      createTime: new Date().toLocaleString('zh-CN'),
    })
    ElMessage.success('用户添加成功')
  }
  dialogVisible.value = false
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定删除用户 "${row.name}"?`, '提示', { type: 'warning' })
    .then(() => {
      users.value = users.value.filter(u => u.id !== row.id)
      ElMessage.success('已删除')
    })
    .catch(() => {})
}
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 16px; align-items: center; flex-wrap: wrap; }
.pagination { margin-top: 16px; justify-content: flex-end; display: flex; }
</style>
