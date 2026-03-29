<script setup>
import { ref } from 'vue'
import { useAppStore } from '../../store/index.js'
import { ElMessage } from 'element-plus'

const store = useAppStore()
const previewVisible = ref(false)
const previewFile = ref(null)
const uploadRef = ref(null)

const fileTypeIconMap = {
  pdf: 'Document',
  excel: 'Tickets',
  image: 'Picture',
  word: 'Document',
}

const fileTypeColorMap = {
  pdf: '#F56C6C',
  excel: '#67C23A',
  image: '#409EFF',
  word: '#E6A23C',
}

function handlePreview(file) {
  previewFile.value = file
  previewVisible.value = true
}

function handleDownload(file) {
  ElMessage.success(`开始下载: ${file.name}（FastDFS/OSS 文件流）`)
}

function handleDelete(file) {
  store.files = store.files.filter(f => f.id !== file.id)
  ElMessage.success('文件已删除')
}

function handleUploadChange(uploadFile) {
  // Simulate file upload to FastDFS/OSS
  const newFile = {
    id: Date.now(),
    name: uploadFile.name,
    size: (uploadFile.size / 1024 / 1024).toFixed(2) + ' MB',
    type: getFileType(uploadFile.name),
    url: '#',
    uploadTime: new Date().toLocaleString('zh-CN', { hour12: false }),
    uploader: store.currentUser.name
  }
  store.files.push(newFile)
  ElMessage.success(`${uploadFile.name} 已上传至 FastDFS/OSS`)
}

function getFileType(name) {
  const ext = name.split('.').pop().toLowerCase()
  if (ext === 'pdf') return 'pdf'
  if (['xlsx', 'xls', 'csv'].includes(ext)) return 'excel'
  if (['png', 'jpg', 'jpeg', 'gif'].includes(ext)) return 'image'
  return 'word'
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span><el-icon><Folder /></el-icon> 文件管理</span>
        <div>
          <el-tag size="small" type="warning">FastDFS / OSS</el-tag>
          <el-tag size="small" type="info" style="margin-left: 6px">文件流传输</el-tag>
        </div>
      </div>
    </template>

    <el-alert
      title="文件通过 FastDFS/OSS 进行分布式存储，支持文件上传、下载、预览功能，后端提供文件流传输接口。"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 16px"
    />

    <!-- Upload Area -->
    <el-upload
      ref="uploadRef"
      drag
      action="#"
      :auto-upload="false"
      :on-change="handleUploadChange"
      :show-file-list="false"
      style="margin-bottom: 16px"
    >
      <el-icon size="60" color="#c0c4cc"><UploadFilled /></el-icon>
      <div class="el-upload__text">
        拖拽文件到此处，或 <em>点击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          支持 PDF、Excel、图片、Word 等格式，文件存储至 FastDFS/OSS
        </div>
      </template>
    </el-upload>

    <!-- File List -->
    <el-table :data="store.files" border stripe>
      <el-table-column label="文件名" min-width="200">
        <template #default="{ row }">
          <div class="file-name-cell">
            <el-icon :color="fileTypeColorMap[row.type]" size="20">
              <component :is="fileTypeIconMap[row.type] || 'Document'" />
            </el-icon>
            <span>{{ row.name }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="size" label="大小" width="100" />
      <el-table-column prop="uploader" label="上传者" width="100" />
      <el-table-column prop="uploadTime" label="上传时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" text @click="handlePreview(row)">预览</el-button>
          <el-button size="small" type="success" text @click="handleDownload(row)">下载</el-button>
          <el-button size="small" type="danger" text @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Preview Dialog -->
    <el-dialog v-model="previewVisible" :title="`预览: ${previewFile?.name}`" width="700px">
      <div class="preview-area">
        <template v-if="previewFile?.type === 'image'">
          <el-image :src="previewFile.url" fit="contain" style="max-height: 500px; width: 100%" />
        </template>
        <template v-else>
          <el-empty :description="`${previewFile?.name} 预览（生产环境通过 FastDFS/OSS URL 加载）`">
            <template #image>
              <el-icon size="60" color="#c0c4cc"><Document /></el-icon>
            </template>
          </el-empty>
        </template>
      </div>
      <template #footer>
        <el-button type="primary" @click="handleDownload(previewFile)">下载文件</el-button>
        <el-button @click="previewVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; align-items: center; justify-content: space-between; }
.file-name-cell { display: flex; align-items: center; gap: 8px; }
.preview-area { min-height: 200px; display: flex; align-items: center; justify-content: center; }
</style>
