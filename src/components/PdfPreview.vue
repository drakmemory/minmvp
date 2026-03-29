<template>
  <div class="module-wrap">
    <el-alert title="后端: FastDFS文件存储 | iframe PDF预览" type="info" :closable="false" show-icon class="tech-note" />

    <el-row :gutter="16">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><span>PDF文件选择</span></template>

          <div class="file-list">
            <div
              v-for="file in pdfFiles"
              :key="file.id"
              :class="['pdf-item', { active: currentPdf?.id === file.id }]"
              @click="selectPdf(file)"
            >
              <el-icon size="24" color="#ff4d4f"><Document /></el-icon>
              <div class="pdf-info">
                <div class="pdf-name">{{ file.name }}</div>
                <el-text type="info" size="small">{{ file.size }} · {{ file.date }}</el-text>
              </div>
            </div>
          </div>

          <el-divider />

          <el-upload
            action="#"
            :auto-upload="false"
            :on-change="handlePdfUpload"
            accept=".pdf"
          >
            <el-button type="primary" style="width:100%">
              <el-icon><Upload /></el-icon> 上传PDF文件
            </el-button>
          </el-upload>

          <div style="margin-top:12px">
            <el-input v-model="pdfUrl" placeholder="或输入PDF链接..." clearable>
              <template #append>
                <el-button @click="loadFromUrl">加载</el-button>
              </template>
            </el-input>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>{{ currentPdf ? currentPdf.name : 'PDF预览' }}</span>
              <div v-if="currentPdf">
                <el-button size="small" type="primary" @click="downloadPdf">
                  <el-icon><Download /></el-icon> 下载
                </el-button>
                <el-button size="small" @click="printPdf">
                  <el-icon><Printer /></el-icon> 打印
                </el-button>
              </div>
            </div>
          </template>

          <div v-if="currentPdf" class="preview-container">
            <iframe
              :src="currentPdf.url"
              class="pdf-iframe"
              frameborder="0"
            />
          </div>

          <el-empty v-else description="请从左侧选择PDF文件" :image-size="80">
            <el-button type="primary" @click="selectPdf(pdfFiles[0])">加载示例PDF</el-button>
          </el-empty>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const currentPdf = ref(null)
const pdfUrl = ref('')

const pdfFiles = ref([
  {
    id: 1,
    name: 'W3C HTML5 规范.pdf',
    size: '2.4 MB',
    date: '2024-01-10',
    url: 'https://www.w3.org/TR/html5/print.pdf',
  },
  {
    id: 2,
    name: '系统操作手册.pdf',
    size: '1.1 MB',
    date: '2024-01-12',
    url: 'https://mozilla.github.io/pdf.js/web/compressed.tracemonkey-pldi-09.pdf',
  },
  {
    id: 3,
    name: '财务报表2024.pdf',
    size: '856 KB',
    date: '2024-01-15',
    url: 'https://mozilla.github.io/pdf.js/web/compressed.tracemonkey-pldi-09.pdf',
  },
])

function selectPdf(file) {
  currentPdf.value = file
}

function handlePdfUpload(file) {
  const url = URL.createObjectURL(file.raw)
  const newFile = {
    id: Date.now(),
    name: file.name,
    size: (file.size / 1024).toFixed(0) + ' KB',
    date: new Date().toLocaleDateString('zh-CN'),
    url,
  }
  pdfFiles.value.push(newFile)
  currentPdf.value = newFile
  ElMessage.success(`文件 "${file.name}" 已加载`)
}

function loadFromUrl() {
  if (!pdfUrl.value) return
  const file = {
    id: Date.now(),
    name: 'PDF文件 (URL)',
    size: '-',
    date: new Date().toLocaleDateString('zh-CN'),
    url: pdfUrl.value,
  }
  pdfFiles.value.push(file)
  currentPdf.value = file
  pdfUrl.value = ''
}

function downloadPdf() {
  ElMessage.success(`开始下载: ${currentPdf.value.name}`)
}

function printPdf() {
  ElMessage.info('打印功能调用中...')
}
</script>

<style scoped>
.module-wrap { padding: 8px 0; }
.tech-note { margin-bottom: 16px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.file-list { display: flex; flex-direction: column; gap: 8px; }
.pdf-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border-radius: 6px;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.2s;
}
.pdf-item:hover { background: #f0f7ff; }
.pdf-item.active { border-color: #1890ff; background: #f0f7ff; }
.pdf-info { flex: 1; overflow: hidden; }
.pdf-name { font-size: 14px; font-weight: 500; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.preview-container { height: 600px; }
.pdf-iframe { width: 100%; height: 100%; border-radius: 4px; }
</style>
