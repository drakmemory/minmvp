<template>
  <div class="module-wrap">
    <el-alert title="后端: EasyExcel 2.1.6 导入导出" type="info" :closable="false" show-icon class="tech-note" />

    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>📤 数据导出</span></template>
          <el-form label-width="80px">
            <el-form-item label="导出范围">
              <el-radio-group v-model="exportRange">
                <el-radio value="all">全部数据</el-radio>
                <el-radio value="page">当前页</el-radio>
                <el-radio value="selected">选中数据</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="文件格式">
              <el-radio-group v-model="exportFormat">
                <el-radio value="xlsx">Excel (.xlsx)</el-radio>
                <el-radio value="csv">CSV (.csv)</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
          <el-button type="success" @click="doExport" style="width:100%">
            <el-icon><Download /></el-icon> 导出数据
          </el-button>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>📥 数据导入</span></template>
          <el-upload
            action="#"
            :auto-upload="false"
            :on-change="handleImport"
            accept=".xlsx,.csv,.xls"
            :limit="1"
          >
            <el-button type="primary"><el-icon><Upload /></el-icon> 选择Excel文件</el-button>
            <template #tip>
              <div style="color:#999;font-size:12px;margin-top:8px">支持 .xlsx / .xls / .csv 格式</div>
            </template>
          </el-upload>
          <el-button type="warning" @click="loadMockImport" style="width:100%;margin-top:12px">
            模拟导入示例数据
          </el-button>
        </el-card>
      </el-col>
    </el-row>

    <el-card v-if="importedData.length" shadow="hover">
      <template #header>
        <span>导入数据预览 <el-badge :value="importedData.length" type="primary" /></span>
      </template>
      <el-table :data="importedData" stripe border>
        <el-table-column prop="row" label="行号" width="60" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="department" label="部门" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.valid ? 'success' : 'danger'" size="small">
              {{ row.valid ? '有效' : '无效' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:12px;text-align:right">
        <el-button type="primary" @click="confirmImport">确认导入到数据库</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const exportRange = ref('all')
const exportFormat = ref('xlsx')
const importedData = ref([])

const mockImportRows = [
  { row: 1, name: '新用户甲', phone: '13711110001', email: 'a@test.com', department: '技术部', valid: true },
  { row: 2, name: '新用户乙', phone: '13711110002', email: 'b@test.com', department: '市场部', valid: true },
  { row: 3, name: '新用户丙', phone: '无效号码', email: 'invalid', department: '运营部', valid: false },
  { row: 4, name: '新用户丁', phone: '13711110004', email: 'd@test.com', department: '财务部', valid: true },
]

const exportData = [
  { id: 1, name: '张三', phone: '13800001111', email: 'zhangsan@example.com', role: '管理员', status: '启用' },
  { id: 2, name: '李四', phone: '13800002222', email: 'lisi@example.com', role: '普通用户', status: '启用' },
  { id: 3, name: '王五', phone: '13800003333', email: 'wangwu@example.com', role: '普通用户', status: '禁用' },
]

function doExport() {
  const headers = ['ID', '姓名', '手机号', '邮箱', '角色', '状态']
  const rows = exportData.map(r => [r.id, r.name, r.phone, r.email, r.role, r.status])
  const csv = [headers, ...rows].map(r => r.join(',')).join('\n')
  const blob = new Blob(['\uFEFF' + csv], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `用户数据_${new Date().toLocaleDateString('zh-CN').replace(/\//g, '')}.${exportFormat.value === 'csv' ? 'csv' : 'csv'}`
  a.click()
  URL.revokeObjectURL(url)
  ElMessage.success('数据导出成功')
}

function handleImport(file) {
  ElMessage.info(`正在解析文件: ${file.name}`)
  loadMockImport()
}

function loadMockImport() {
  importedData.value = mockImportRows
  ElMessage.success('文件解析完成，请确认数据后导入')
}

function confirmImport() {
  const validCount = importedData.value.filter(r => r.valid).length
  ElMessage.success(`成功导入 ${validCount} 条数据，${importedData.value.length - validCount} 条数据无效已跳过`)
  importedData.value = []
}
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
</style>
