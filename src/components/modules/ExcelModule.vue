<script setup>
import { ref } from 'vue'
import { useAppStore } from '../../store/index.js'
import { ElMessage } from 'element-plus'

const store = useAppStore()
const importing = ref(false)
const exporting = ref(false)
const importResults = ref([])
const uploadRef = ref(null)

async function handleExport() {
  exporting.value = true
  await new Promise(r => setTimeout(r, 1500))
  exporting.value = false

  // Simulate EasyExcel export (create CSV in browser)
  const headers = ['ID', '用户名', '姓名', '手机号', '邮箱', '角色', '状态', '创建时间']
  const rows = store.users.map(u =>
    [u.id, u.username, u.name, u.phone, u.email, u.role, u.status === 1 ? '启用' : '禁用', u.createTime]
  )
  const csvContent = [headers, ...rows].map(r => r.join(',')).join('\n')
  const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `用户数据_${new Date().toLocaleDateString('zh-CN')}.csv`
  a.click()
  URL.revokeObjectURL(url)

  store.addMessage({ type: 'export', user: store.currentUser.name, content: 'Excel导出用户数据', status: 'success' })
  ElMessage.success(`成功导出 ${store.users.length} 条用户数据（EasyExcel 2.1.6）`)
}

async function handleImportChange(file) {
  importing.value = true
  importResults.value = []

  // Simulate EasyExcel import
  await new Promise(r => setTimeout(r, 1500))

  const mockImported = [
    { row: 2, name: 'import_user1', status: 'success', msg: '导入成功' },
    { row: 3, name: 'import_user2', status: 'success', msg: '导入成功' },
    { row: 4, name: 'import_user3', status: 'error', msg: '手机号格式错误' },
  ]
  importResults.value = mockImported
  importing.value = false

  store.addMessage({ type: 'import', user: store.currentUser.name, content: `Excel导入: ${file.name}`, status: 'success' })
  ElMessage.success(`文件 ${file.name} 解析完成（EasyExcel 2.1.6）`)
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span><el-icon><Document /></el-icon> Excel 导入导出</span>
        <div>
          <el-tag size="small" type="success">EasyExcel 2.1.6</el-tag>
          <el-tag size="small" type="info" style="margin-left: 6px">POI 底层</el-tag>
        </div>
      </div>
    </template>

    <el-alert
      title="使用阿里 EasyExcel 2.1.6 实现大数据量 Excel 读写，内存占用低，支持百万行数据导出。"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 20px"
    />

    <el-row :gutter="20">
      <!-- Export -->
      <el-col :span="12">
        <el-card shadow="hover" class="operation-card">
          <div class="op-title">
            <el-icon size="32" color="#67C23A"><Download /></el-icon>
            <div>
              <div class="op-name">Excel 导出</div>
              <div class="op-desc">将用户数据导出为 Excel 文件</div>
            </div>
          </div>
          <el-descriptions :column="1" size="small" class="op-info">
            <el-descriptions-item label="数据量">{{ store.users.length }} 条记录</el-descriptions-item>
            <el-descriptions-item label="格式">.xlsx / .csv</el-descriptions-item>
            <el-descriptions-item label="技术">EasyExcel @ResponseBody 流式写出</el-descriptions-item>
          </el-descriptions>
          <el-button
            type="success"
            :icon="'Download'"
            :loading="exporting"
            @click="handleExport"
            style="width: 100%"
          >
            {{ exporting ? '导出中...' : '导出用户数据' }}
          </el-button>
        </el-card>
      </el-col>

      <!-- Import -->
      <el-col :span="12">
        <el-card shadow="hover" class="operation-card">
          <div class="op-title">
            <el-icon size="32" color="#409EFF"><Upload /></el-icon>
            <div>
              <div class="op-name">Excel 导入</div>
              <div class="op-desc">从 Excel 文件批量导入用户</div>
            </div>
          </div>
          <el-descriptions :column="1" size="small" class="op-info">
            <el-descriptions-item label="格式">.xlsx / .xls</el-descriptions-item>
            <el-descriptions-item label="校验">字段非空 + 手机号格式</el-descriptions-item>
            <el-descriptions-item label="技术">EasyExcel @ExcelProperty 监听器</el-descriptions-item>
          </el-descriptions>
          <el-upload
            ref="uploadRef"
            action="#"
            :auto-upload="false"
            :on-change="handleImportChange"
            :show-file-list="false"
            accept=".xlsx,.xls,.csv"
            style="width: 100%"
          >
            <el-button
              type="primary"
              :icon="'Upload'"
              :loading="importing"
              style="width: 100%"
            >
              {{ importing ? '导入中...' : '选择 Excel 文件' }}
            </el-button>
          </el-upload>
        </el-card>
      </el-col>
    </el-row>

    <!-- Import Results -->
    <div v-if="importResults.length > 0" style="margin-top: 20px">
      <el-divider content-position="left">导入结果</el-divider>
      <el-table :data="importResults" border size="small">
        <el-table-column prop="row" label="行号" width="80" />
        <el-table-column prop="name" label="用户名" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'danger'" size="small">
              {{ row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="msg" label="说明" />
      </el-table>
    </div>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; align-items: center; justify-content: space-between; }
.operation-card { height: 100%; }
.op-title { display: flex; align-items: center; gap: 12px; margin-bottom: 16px; }
.op-name { font-size: 16px; font-weight: 600; }
.op-desc { color: #909399; font-size: 13px; }
.op-info { margin-bottom: 16px; }
</style>
