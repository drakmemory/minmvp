<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const currentPage = ref(1)
const totalPages = ref(5)
const zoom = ref(100)
const loading = ref(false)

// Sample PDF pages (simulated rendering)
const pages = [
  { title: '第一页 - 封面', content: '系统设计文档\nMinMVP 技术栈演示平台\nv1.0.0' },
  { title: '第二页 - 技术架构', content: '后端技术栈:\n- Spring Boot 2.0.9\n- MyBatis-Plus 3.1.2\n- Elasticsearch 7.3.2\n- Kafka\n- Redis' },
  { title: '第三页 - 前端技术', content: '前端技术栈:\n- Vue 3\n- Element Plus\n- ECharts\n- Axios\n- Day.js' },
  { title: '第四页 - API 文档', content: 'REST API 接口说明\nGET /api/users\nPOST /api/users\nPUT /api/users/{id}\nDELETE /api/users/{id}' },
  { title: '第五页 - 部署说明', content: '部署环境:\n- JDK 1.8\n- MySQL 5.7\n- Redis\n- Elasticsearch 7.3.2\n- Kafka' },
]

function prevPage() {
  if (currentPage.value > 1) currentPage.value--
}

function nextPage() {
  if (currentPage.value < totalPages.value) currentPage.value++
}

function handleLoadPDF(file) {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success(`PDF 文件 "${file.name}" 加载成功（vue-pdf 组件渲染）`)
  }, 1500)
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span><el-icon><Reading /></el-icon> PDF 预览</span>
        <div>
          <el-tag size="small" type="success">vue-pdf 4.3.0</el-tag>
          <el-tag size="small" type="info" style="margin-left: 6px">PDF.js 底层</el-tag>
        </div>
      </div>
    </template>

    <el-alert
      title="集成 vue-pdf 组件实现 PDF 文件在线预览，底层使用 PDF.js 渲染，后端通过 FastDFS/OSS 提供 PDF 文件流。"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 16px"
    />

    <el-row :gutter="16">
      <!-- PDF Controls -->
      <el-col :span="24" style="margin-bottom: 12px">
        <div class="pdf-toolbar">
          <el-upload
            action="#"
            :auto-upload="false"
            :show-file-list="false"
            accept=".pdf"
            :on-change="(f) => handleLoadPDF(f)"
          >
            <el-button type="primary" :icon="'Upload'">加载 PDF 文件</el-button>
          </el-upload>

          <div class="page-controls">
            <el-button :icon="'ArrowLeft'" :disabled="currentPage === 1" @click="prevPage" />
            <span class="page-info">第 {{ currentPage }} / {{ totalPages }} 页</span>
            <el-button :icon="'ArrowRight'" :disabled="currentPage === totalPages" @click="nextPage" />
          </div>

          <div class="zoom-controls">
            <el-button size="small" @click="zoom = Math.max(50, zoom - 10)">缩小</el-button>
            <span class="zoom-value">{{ zoom }}%</span>
            <el-button size="small" @click="zoom = Math.min(200, zoom + 10)">放大</el-button>
          </div>
        </div>
      </el-col>

      <!-- PDF Viewer Area -->
      <el-col :span="24">
        <div class="pdf-container" v-loading="loading" element-loading-text="PDF 加载中...">
          <div
            class="pdf-page"
            :style="{ transform: `scale(${zoom / 100})`, transformOrigin: 'top center' }"
          >
            <!-- Simulated PDF page -->
            <div class="pdf-page-content">
              <div class="pdf-page-header">
                <el-icon><Document /></el-icon>
                {{ pages[currentPage - 1].title }}
              </div>
              <div class="pdf-page-body">
                <pre>{{ pages[currentPage - 1].content }}</pre>
              </div>
              <div class="pdf-page-footer">
                MinMVP 技术文档 · 第 {{ currentPage }} 页 / 共 {{ totalPages }} 页
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- Tech Info -->
    <el-divider />
    <el-descriptions :column="3" border size="small">
      <el-descriptions-item label="vue-pdf 版本">4.3.0</el-descriptions-item>
      <el-descriptions-item label="底层引擎">PDF.js (Mozilla)</el-descriptions-item>
      <el-descriptions-item label="支持功能">翻页、缩放、打印</el-descriptions-item>
      <el-descriptions-item label="文件来源">FastDFS/OSS 文件流</el-descriptions-item>
      <el-descriptions-item label="后端接口">GET /api/files/{id}/stream</el-descriptions-item>
      <el-descriptions-item label="跨域处理">后端配置 CORS</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; align-items: center; justify-content: space-between; }
.pdf-toolbar {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 8px 12px;
  background: #f5f7fa;
  border-radius: 6px;
  flex-wrap: wrap;
}
.page-controls { display: flex; align-items: center; gap: 8px; }
.page-info { font-size: 14px; color: #606266; white-space: nowrap; }
.zoom-controls { display: flex; align-items: center; gap: 8px; }
.zoom-value { font-size: 14px; color: #606266; min-width: 45px; text-align: center; }
.pdf-container {
  background: #525659;
  border-radius: 6px;
  padding: 24px;
  display: flex;
  justify-content: center;
  min-height: 400px;
}
.pdf-page {
  transition: transform 0.2s;
}
.pdf-page-content {
  width: 600px;
  background: #fff;
  box-shadow: 0 4px 20px rgba(0,0,0,0.3);
  border-radius: 2px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
.pdf-page-header {
  background: #409EFF;
  color: #fff;
  padding: 16px 24px;
  font-size: 18px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
}
.pdf-page-body {
  flex: 1;
  padding: 32px 40px;
  min-height: 300px;
}
.pdf-page-body pre {
  font-family: 'Microsoft YaHei', sans-serif;
  font-size: 14px;
  line-height: 2;
  white-space: pre-wrap;
  color: #303133;
}
.pdf-page-footer {
  border-top: 1px solid #eee;
  padding: 12px 24px;
  text-align: center;
  font-size: 12px;
  color: #909399;
}
</style>
