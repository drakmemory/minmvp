<script setup>
import { ref, computed } from 'vue'
import { useAppStore } from '../../store/index.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const store = useAppStore()

const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(5)
const dialogVisible = ref(false)
const isEdit = ref(false)

const formRef = ref(null)
const form = ref({
  id: null,
  username: '',
  name: '',
  phone: '',
  email: '',
  role: 'user',
  status: 1
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
}

const filteredUsers = computed(() => {
  if (!searchKeyword.value) return store.users
  return store.users.filter(u =>
    u.name.includes(searchKeyword.value) ||
    u.username.includes(searchKeyword.value) ||
    u.phone.includes(searchKeyword.value)
  )
})

const pagedUsers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredUsers.value.slice(start, start + pageSize.value)
})

function openAdd() {
  isEdit.value = false
  form.value = { id: null, username: '', name: '', phone: '', email: '', role: 'user', status: 1 }
  dialogVisible.value = true
}

function openEdit(row) {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确认删除用户 "${row.name}"？`, '警告', { type: 'warning' })
  store.deleteUser(row.id)
  ElMessage.success('删除成功')
}

function handleSubmit() {
  formRef.value.validate(valid => {
    if (!valid) return
    if (isEdit.value) {
      store.updateUser(form.value)
      ElMessage.success('更新成功（已同步Redis缓存）')
    } else {
      store.addUser(form.value)
      ElMessage.success('新增成功（已写入MySQL + Redis）')
    }
    dialogVisible.value = false
  })
}
</script>

<template>
  <div>
    <el-card class="module-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><UserFilled /></el-icon> 用户管理</span>
          <div class="tech-badges">
            <el-tag size="small" type="success">MyBatis-Plus 3.1.2</el-tag>
            <el-tag size="small" type="warning">Redis 缓存</el-tag>
            <el-tag size="small">MySQL 5.7</el-tag>
            <el-tag size="small" type="info">MapStruct DTO</el-tag>
          </div>
        </div>
      </template>

      <!-- Search & Actions -->
      <div class="toolbar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索用户名/姓名/手机号"
          :prefix-icon="'Search'"
          clearable
          style="width: 300px"
          @clear="currentPage = 1"
          @input="currentPage = 1"
        />
        <div class="action-btns">
          <el-button type="primary" :icon="'Plus'" @click="openAdd">新增用户</el-button>
          <el-button :icon="'Refresh'" @click="ElMessage.info('已从Redis缓存刷新数据')">
            刷新缓存
          </el-button>
        </div>
      </div>

      <!-- Data Table (el-table) -->
      <el-table :data="pagedUsers" stripe border style="width: 100%" class="user-table">
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="role" label="角色">
          <template #default="{ row }">
            <el-tag
              :type="row.role === 'admin' ? 'danger' : row.role === 'manager' ? 'warning' : 'info'"
              size="small"
            >{{ row.role }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-switch :model-value="row.status === 1" disabled />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" text @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" text @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination (el-pagination) -->
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="filteredUsers.length"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
      />
    </el-card>

    <!-- Add/Edit Dialog (el-dialog) -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      width="500px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role">
            <el-option label="管理员" value="admin" />
            <el-option label="管理人员" value="manager" />
            <el-option label="普通用户" value="user" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.module-card { margin-bottom: 16px; }
.card-header { display: flex; align-items: center; justify-content: space-between; }
.tech-badges { display: flex; gap: 6px; flex-wrap: wrap; }
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.action-btns { display: flex; gap: 8px; }
.user-table { margin-bottom: 16px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
