<template>
  <div class="module-wrap">
    <el-alert title="后端: FastDFS/OSS 文件存储" type="info" :closable="false" show-icon class="tech-note" />

    <el-upload
      class="upload-area"
      drag
      action="#"
      :auto-upload="false"
      :on-change="handleFileChange"
      multiple
    >
      <el-icon size="48" color="#1890ff"><UploadFilled /></el-icon>
      <div class="upload-text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div class="upload-tip">支持 jpg/png/pdf/xlsx 等格式，单文件不超过 50MB</div>
      </template>
    </el-upload>

    <el-table :data="files" stripe border style="width:100%;margin-top:16px">
      <el-table-column prop="name" label="文件名" />
      <el-table-column prop="size" label="大小" width="100" />
      <el-table-column label="类型" width="90">
        <template #default="{ row }">
          <el-tag :type="typeColor(row.type)" size="small">{{ row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="uploadTime" label="上传时间" width="160" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="previewFile(row)">预览</el-button>
          <el-button size="small" type="success" @click="downloadFile(row)">下载</el-button>
          <el-button size="small" type="danger" @click="deleteFile(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="previewVisible" title="文件预览" width="600px">
      <div class="preview-content">
        <el-image v-if="previewFile_.type === 'IMAGE'" :src="previewFile_.url" fit="contain" style="width:100%;max-height:400px" />
        <div v-else class="preview-placeholder">
          <el-icon size="64" color="#999"><Document /></el-icon>
          <el-text type="info">{{ previewFile_.name }}</el-text>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const previewVisible = ref(false)
const previewFile_ = ref({})

const files = ref([
  { id: 1, name: 'avatar.jpg', size: '245 KB', type: 'IMAGE', uploadTime: '2024-01-15 10:00:00', url: 'https://picsum.photos/400/300' },
  { id: 2, name: 'report.pdf', size: '1.2 MB', type: 'PDF', uploadTime: '2024-01-15 11:00:00', url: '' },
  { id: 3, name: 'data.xlsx', size: '89 KB', type: 'EXCEL', uploadTime: '2024-01-15 12:00:00', url: '' },
  { id: 4, name: 'banner.png', size: '512 KB', type: 'IMAGE', uploadTime: '2024-01-15 13:00:00', url: 'https://picsum.photos/400/200' },
])

function typeColor(type) {
  return { IMAGE: 'success', PDF: 'danger', EXCEL: 'warning', WORD: 'primary' }[type] || 'info'
}

function handleFileChange(file) {
  files.value.push({
    id: Date.now(),
    name: file.name,
    size: (file.size / 1024).toFixed(0) + ' KB',
    type: file.name.match(/\.(jpg|jpeg|png|gif)$/i) ? 'IMAGE' : file.name.match(/\.pdf$/i) ? 'PDF' : 'OTHER',
    uploadTime: new Date().toLocaleString('zh-CN'),
    url: file.url || '',
  })
  ElMessage.success(`文件 "${file.name}" 已添加到列表`)
}

function previewFile(row) {
  previewFile_.value = row
  previewVisible.value = true
}

function downloadFile(row) {
  ElMessage.success(`开始下载: ${row.name}`)
}

function deleteFile(row) {
  ElMessageBox.confirm(`确定删除文件 "${row.name}"?`, '提示', { type: 'warning' })
    .then(() => {
      files.value = files.value.filter(f => f.id !== row.id)
      ElMessage.success('文件已删除')
    }).catch(() => {})
}
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.upload-area { width: 100%; margin-bottom: 8px; }
.upload-text { font-size: 16px; color: #606266; }
.upload-text em { color: #1890ff; font-style: normal; }
.upload-tip { color: #999; font-size: 12px; margin-top: 4px; }
.preview-content { text-align: center; }
.preview-placeholder { display: flex; flex-direction: column; align-items: center; gap: 12px; padding: 40px; }
</style>
